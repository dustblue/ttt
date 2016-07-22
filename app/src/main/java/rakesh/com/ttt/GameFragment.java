package rakesh.com.ttt;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.HashSet;
import java.util.Set;

public class GameFragment extends Fragment {
    private static int mLargeIds[] = {R.id.large1, R.id.large2, R.id.large3, R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8, R.id.large9,};
    private static int mSmallIds[] = {R.id.small1, R.id.small2, R.id.small3, R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8, R.id.small9,};
    private static int size = 9;

    private GamePlay entireBoard = new GamePlay(this);
    private GamePlay largeBoards[] = new GamePlay[size];
    private GamePlay smallBoards[][] = new GamePlay[size][size];

    private GamePlay.Owner player = GamePlay.Owner.X;

    private Set<GamePlay> available = new HashSet<>();
    private int mLastLarge;
    private int mLastSmall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        initGame();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.large_board, container, false);
        initViews(rootView);
        updateAllTiles();
        return rootView;
    }

    public void initGame() {
        entireBoard = new GamePlay(this);
        for (int large = 0; large < size; large++) {
            largeBoards[large] = new GamePlay(this);
            for (int small = 0; small < size; small++) {
                smallBoards[large][small] = new GamePlay(this);
            }
            largeBoards[large].setBoards(smallBoards[large]);
        }
        entireBoard.setBoards(largeBoards);
        mLastSmall = -1;
        mLastLarge = -1;
        setAvailableFromLastMove(mLastSmall);
    }

    private void initViews(View rootView) {
        entireBoard.setView(rootView);
        for (int large = 0; large < size; large++) {
            View outer = rootView.findViewById(mLargeIds[large]);
            largeBoards[large].setView(outer);
            for (int small = 0; small < size; small++) {
                ImageButton inner = (ImageButton) outer.findViewById
                        (mSmallIds[small]);
                final int fLarge = large;
                final int fSmall = small;
                final GamePlay smallBoard = smallBoards[large][small];
                smallBoard.setView(inner);
                inner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isAvailable(smallBoard)) {
                            makeMove(fLarge, fSmall);
                            switchTurns();
                        }
                    }
                });
            }
        }
    }

    private void makeMove(int large, int small) {
        mLastLarge = large;
        mLastSmall = small;
        GamePlay smallBoard = smallBoards[large][small];
        GamePlay largeBoard = largeBoards[large];

        smallBoard.setOwner(player);
        setAvailableFromLastMove(small);

        GamePlay.Owner oldWinner = largeBoard.getOwner();
        GamePlay.Owner winner = largeBoard.findWinner();

        if (winner != oldWinner) {
            largeBoard.setOwner(winner);
        }

        winner = entireBoard.findWinner();
        entireBoard.setOwner(winner);

        updateAllTiles();

        if (winner != GamePlay.Owner.TIE) {
            ((GameActivity) getActivity()).reportWinner(winner);
        }
    }

    private void switchTurns() {
        player = player == GamePlay.Owner.X ? GamePlay.Owner.O : GamePlay.Owner.X;
    }

    private void clearAvailable() {
        available.clear();
    }

    private void addAvailable(GamePlay board) {
        available.add(board);
    }

    public boolean isAvailable(GamePlay board) {
        return available.contains(board);
    }

    private void setAvailableFromLastMove(int small) {
        clearAvailable();

        if (small != -1) {
            for (int dest = 0; dest < size; dest++) {
                GamePlay board = smallBoards[small][dest];
                if (board.getOwner() == GamePlay.Owner.TIE)
                    addAvailable(board);
            }
        }

        if (available.isEmpty()) {
            setAllAvailable();
        }
    }

    private void setAllAvailable() {
        for (int large = 0; large < size; large++) {
            for (int small = 0; small < size; small++) {
                GamePlay board = smallBoards[large][small];
                if (board.getOwner() == GamePlay.Owner.TIE)
                    addAvailable(board);
            }
        }
    }

    private void updateAllTiles() {
        entireBoard.updateDrawableState();
        for (int large = 0; large < 9; large++) {
            largeBoards[large].updateDrawableState();
            for (int small = 0; small < 9; small++) {
                smallBoards[large][small].updateDrawableState();
            }
        }
    }
}
package rakesh.com.ttt;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;

public class GamePlay {
    public enum Owner {
        X, O, TIE, BOTH
    }

    private static final int LEVEL_X = 0;
    private static final int LEVEL_O = 1;
    private static final int LEVEL_BLANK = 2;
    private static final int LEVEL_AVAILABLE = 3;
    private static final int LEVEL_TIE = 3;

    private final GameFragment gameFragment;
    private Owner owner = Owner.TIE;
    private View view;
    private GamePlay boards[];

    public GamePlay(GameFragment game) {
        this.gameFragment = game;
    }

    public void updateDrawableState() {
        if (view == null) return;

        int level = getLevel();

        if (view.getBackground() != null) {
            view.getBackground().setLevel(level);
        }

        if (view instanceof ImageButton) {
            Drawable drawable = ((ImageButton) view).getDrawable();
            drawable.setLevel(level);
        }
    }

    private int getLevel() {
        int level = LEVEL_BLANK;
        switch (owner) {
            case X:
                level = LEVEL_X;
                break;
            case O:
                level = LEVEL_O;
                break;
            case BOTH:
                level = LEVEL_TIE;
                break;
            case TIE:
                level = gameFragment.isAvailable(this) ? LEVEL_AVAILABLE : LEVEL_BLANK;
                break;
        }
        return level;
    }

    public Owner findWinner() {
        if (getOwner() != Owner.TIE)
            return getOwner();

        int totalX[] = new int[4];
        int totalO[] = new int[4];

        countCaptures(totalX, totalO);

        if (totalX[3] > 0) return Owner.X;

        if (totalO[3] > 0) return Owner.O;

        int total = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Owner owner = boards[3 * row + col].getOwner();
                if (owner != Owner.TIE) total++;
            }
            if (total == 9) return Owner.BOTH;
        }

        return Owner.TIE;
    }

    private void countCaptures(int totalX[], int totalO[]) {
        int capturedX, capturedO;

        for (int row = 0; row < 3; row++) {
            capturedX = capturedO = 0;
            for (int col = 0; col < 3; col++) {
                Owner owner = boards[3 * row + col].getOwner();
                if (owner == Owner.X || owner == Owner.BOTH) capturedX++;
                if (owner == Owner.O || owner == Owner.BOTH) capturedO++;
            }
            totalX[capturedX]++;
            totalO[capturedO]++;
        }

        for (int col = 0; col < 3; col++) {
            capturedX = capturedO = 0;
            for (int row = 0; row < 3; row++) {
                Owner owner = boards[3 * row + col].getOwner();
                if (owner == Owner.X || owner == Owner.BOTH) capturedX++;
                if (owner == Owner.O || owner == Owner.BOTH) capturedO++;
            }
            totalX[capturedX]++;
            totalO[capturedO]++;
        }

        capturedX = capturedO = 0;
        for (int diag = 0; diag < 3; diag++) {
            Owner owner = boards[3 * diag + diag].getOwner();
            if (owner == Owner.X || owner == Owner.BOTH) capturedX++;
            if (owner == Owner.O || owner == Owner.BOTH) capturedO++;
        }
        totalX[capturedX]++;
        totalO[capturedO]++;

        capturedX = capturedO = 0;
        for (int diag = 0; diag < 3; diag++) {
            Owner owner = boards[3 * diag + (2 - diag)].getOwner();
            if (owner == Owner.X || owner == Owner.BOTH) capturedX++;
            if (owner == Owner.O || owner == Owner.BOTH) capturedO++;
        }
        totalX[capturedX]++;
        totalO[capturedO]++;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public void setBoards(GamePlay[] subBoards) {
        this.boards = subBoards;
    }
}
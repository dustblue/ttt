package rakesh.com.ttt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class GameActivity extends Activity {
    private GameFragment gameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameFragment = (GameFragment) getFragmentManager().findFragmentById(R.id.fragment_game);
    }

    public void reportWinner(final GamePlay.Owner winner) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.winner, winner));
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        final Dialog dialog = builder.create();
        dialog.show();
        gameFragment.initGame();
    }
}
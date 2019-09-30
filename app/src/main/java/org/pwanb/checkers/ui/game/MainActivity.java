package org.pwanb.checkers.ui.game;

/**
 * ALL DAMN RIGHTS RESERVED. !!!! 2019 (R)


 /**
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * @author Codename "P.W.A.N.B"


public class MainActivity extends AppCompatActivity {

    private ImageView[][] boardMain = new ImageView[8][8];
    private Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        final double SCALE = displayMetrics.widthPixels / 1080.0;

        ImageView boardBackground = findViewById(getResources()
                .getIdentifier("Board", "id", getPackageName()));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int)(1040 *SCALE),(int)(1040 *SCALE));
        params.setMargins((int)(20 *SCALE), (int)(300 *SCALE), 0, 0);
        boardBackground.setLayoutParams(params);

        int left, height, resID;
        String fieldID;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                fieldID = "Field_" + x + "_" + y;
                resID = getResources().getIdentifier(fieldID, "id", getPackageName());
                boardMain[x][y] = findViewById(resID);
                params = new FrameLayout.LayoutParams((int)(130 *SCALE), (int)(130 *SCALE));
                left = (int) ((20 + 130*x) * SCALE);
                height = (int)((1210 - 130*y)*SCALE) ;
                params.setMargins(left, height, 0, 0);
                boardMain[x][y].setLayoutParams(params);
            }
        }

        Activity activity = this;
        board = new Board(boardMain, activity);
        board.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Rules:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("RULES");
                builder.setMessage(R.string.rules_info);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int something) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.Reset:
                board.clean();
                board.start();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

} */
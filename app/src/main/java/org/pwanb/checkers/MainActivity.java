package org.pwanb.checkers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;


public class MainActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Context context;
    private GestureDetector detector;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        context = this;
        NavController navController = Navigation.findNavController(this, R.id.fragment_container);
        navController.addOnDestinationChangedListener(this);
        NavigationUI.setupWithNavController(navigationView, navController);
        handler.post(new Hide());
        setNavigationViewAction();
        setGestureDetector();
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller,
                                     @NonNull NavDestination destination, @Nullable Bundle arguments) {
        if (destination.getId() == R.id.nav_game) {
            navigationView.getMenu().findItem(R.id.nav_game_option).setVisible(true);

        } else {
            navigationView.getMenu().findItem(R.id.nav_game_option).setVisible(false);
        }
    }

    private void setNavigationViewAction() {
        navigationView.getMenu().findItem(R.id.nav_other).getSubMenu().findItem(R.id.nav_authors).
                setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Authors");
                        builder.setMessage(R.string.chose);
                        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int something) {
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return true;
                    }
                });
        navigationView.getMenu().findItem(R.id.nav_game_option).getSubMenu().findItem(R.id.nav_computer).
                setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
        navigationView.getMenu().findItem(R.id.nav_game_option).getSubMenu().findItem(R.id.nav_player).
                setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
        navigationView.getMenu().findItem(R.id.nav_game_option).getSubMenu().findItem(R.id.nav_replay).
                setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
    }

    private void setGestureDetector() {
        View view = findViewById(R.id.fragment_container);
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {

                return true;
            }
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY)  {
                Log.i("TAG", "onDoubleTap: ");
                Log.d("TAG", "onScroll: ");
                handler.post(new Show());
                handler.postDelayed(new Hide(),2000);
                return true;}

        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });
    }


     final class Hide implements Runnable {
        @Override
        public void run() {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

    }

    private final class Show implements Runnable {

        @Override
        public void run() {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }
}







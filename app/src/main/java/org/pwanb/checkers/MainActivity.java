package org.pwanb.checkers;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import org.pwanb.checkers.ui.game.GameFragment;
import org.pwanb.checkers.ui.home.HomeFragment;
import org.pwanb.checkers.ui.rules.RulesFragment;


public class MainActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener {

    private Menu menu;
    private NavigationView navigationView;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //navigationView.getMenu().
        NavController navController = Navigation.findNavController(this, R.id.fragment_container);
        navController.addOnDestinationChangedListener(this);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller,
                                     @NonNull NavDestination destination, @Nullable Bundle arguments) {
        if(destination.getId() == R.id.nav_game) {
            navigationView.getMenu().findItem(R.id.nav_game_option).setVisible(true);
        } else {
            navigationView.getMenu().findItem(R.id.nav_game_option).setVisible(false);
        }
    }
}







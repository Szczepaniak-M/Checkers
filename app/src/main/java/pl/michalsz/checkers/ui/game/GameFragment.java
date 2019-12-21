package pl.michalsz.checkers.ui.game;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;

import pl.michalsz.checkers.R;
import pl.michalsz.checkers.ui.game.mechanics.Board;


public class GameFragment extends Fragment {

    private ImageView[][] boardUI;
    private Board boardMechanics;
    private NavigationView navigationView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        boolean whitePlayer = GameFragmentArgs.fromBundle(getArguments()).getWhite();
        boolean redPlayer = GameFragmentArgs.fromBundle(getArguments()).getRed();
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        container.removeAllViews();
        boardUI = new ImageView[8][8];
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        final double SCALE = displayMetrics.widthPixels / 1080.0;
        ImageView boardBackground = view.findViewById(R.id.Board);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (1040 * SCALE), (int) (1040 * SCALE));
        params.setMargins((int) (20 * SCALE), (int) (300 * SCALE), 0, 0);
        boardBackground.setLayoutParams(params);

        int left, height, resID;
        String fieldID;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (x % 2 == y % 2) {
                    fieldID = "Field_" + x + "_" + y;
                    resID = getResources().getIdentifier(fieldID, "id", view.getContext().getPackageName());
                    boardUI[x][y] = view.findViewById(resID);
                    params = new FrameLayout.LayoutParams((int) (130 * SCALE), (int) (130 * SCALE));
                    left = (int) ((20 + 130 * x) * SCALE);
                    height = (int) ((1210 - 130 * y) * SCALE);
                    params.setMargins(left, height, 0, 0);
                    boardUI[x][y].setLayoutParams(params);
                }
            }
        }
        navigationView = getActivity().findViewById(R.id.nav_view);
        setNavigationViewAction();
        boardMechanics = new Board(boardUI, getActivity(), whitePlayer, redPlayer);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        boardMechanics.clean();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boardMechanics.start();
            }
        }, 20);

    }

    private void setNavigationViewAction() {
        navigationView.getMenu().findItem(R.id.nav_game_option).getSubMenu().findItem(R.id.nav_replay).
                setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        boardMechanics.clean();
                        boardMechanics.start();
                        DrawerLayout mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
                        mDrawerLayout.closeDrawers();
                        return false;
                    }
                });
        navigationView.getMenu().findItem(R.id.nav_game_option).getSubMenu().findItem(R.id.nav_new_game).
                setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                        if (navController.getCurrentDestination().getId() == R.id.nav_game) {
                            GameFragmentDirections.ActionNavGameToNavGameAlert action = GameFragmentDirections.actionNavGameToNavGameAlert();
                            action.setWinner(-2);
                            navController.navigate(action);
                        }
                        DrawerLayout mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
                        mDrawerLayout.closeDrawers();
                        return false;
                    }
                });
    }
}
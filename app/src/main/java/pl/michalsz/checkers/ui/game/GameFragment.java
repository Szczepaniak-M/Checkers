package pl.michalsz.checkers.ui.game;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;

import pl.michalsz.checkers.R;
import pl.michalsz.checkers.ui.game.mechanics.Board;

public class GameFragment extends Fragment {

    private GameViewModel gameViewModel;
    private ImageView[][] boardUI;
    private Board boardMechanics;
    private NavigationView navigationView;
    private static boolean end = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        gameViewModel =
                ViewModelProviders.of(this).get(GameViewModel.class);
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
        boardMechanics = new Board(boardUI, getActivity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        boardMechanics.start();
    }

    private void setNavigationViewAction() {
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
                        boardMechanics.clean();
                        boardMechanics.start();
                        return false;
                    }
                });
    }




}
package org.pwanb.checkers.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;


import com.google.android.material.navigation.NavigationView;

import org.pwanb.checkers.R;
import org.pwanb.checkers.ui.game.GameFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button btnComputer = view.findViewById(R.id.btn_computer);
        btnComputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Chose pawns");
                builder.setMessage(R.string.chose);
                builder.setPositiveButton("White", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int something) {
                        replaceGameFragment(true, false);
                    }
                });
                builder.setNegativeButton("Red", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        replaceGameFragment(false, true);
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        Button btnPlayer = view.findViewById(R.id.btn_player);
        btnPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                replaceGameFragment(true, true);
            }
        });
        return view;
    }

    private void replaceGameFragment(boolean white, boolean red) {
        FragmentActivity fragmentActivity = getActivity();
        GameFragment gameFragment = new GameFragment ();
        Bundle args = new Bundle();
        args.putBoolean("whitePlayer", white);
        args.putBoolean("redPlayer", red);
        gameFragment.setArguments(args);
        fragmentActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, gameFragment)
                .commit();
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_game);
        navigationView.getMenu().findItem(R.id.nav_game_option).setVisible(true);
    }
}
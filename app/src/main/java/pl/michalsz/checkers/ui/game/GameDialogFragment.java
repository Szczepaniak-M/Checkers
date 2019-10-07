package pl.michalsz.checkers.ui.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import pl.michalsz.checkers.R;

public class GameDialogFragment extends DialogFragment {
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_game, container, false);
        TextView txtHeader = view.findViewById(R.id.heading_game_alert);
        TextView txtComputer = view.findViewById(R.id.action_computer);
        TextView txtPlayer = view.findViewById(R.id.action_player);
        TextView txtExit = view.findViewById(R.id.action_exit);
        navController = NavHostFragment.findNavController(this);
        int result = GameDialogFragmentArgs.fromBundle(getArguments()).getWinner();

        if (result == 1) {
            txtHeader.setText(R.string.white_win);
        } else if (result == -1) {
            txtHeader.setText(R.string.red_win);
        } else {
            txtHeader.setText(R.string.draw);
        }

        txtComputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_game_alert_to_nav_home_alert);
            }
        });

        txtPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_game_alert_to_nav_game);
            }
        });

        txtExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                System.exit(0);
            }
        });

        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getDialog().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        return view;
    }
}


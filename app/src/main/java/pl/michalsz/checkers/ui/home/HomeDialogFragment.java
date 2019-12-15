package pl.michalsz.checkers.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import pl.michalsz.checkers.R;

public class HomeDialogFragment extends DialogFragment {


    private NavController navController;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_home, container, false);
        TextView txtRed = view.findViewById(R.id.action_red);
        TextView txtWhite = view.findViewById(R.id.action_white);
        TextView txtCancel = view.findViewById(R.id.action_cancel);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        txtRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeDialogFragmentDirections.ActionNavHomeAlertToNavGame action =  HomeDialogFragmentDirections.actionNavHomeAlertToNavGame();
                action.setRed(true);
                action.setWhite(false);
                navController.navigate(action);
            }
        });

        txtWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeDialogFragmentDirections.ActionNavHomeAlertToNavGame action =  HomeDialogFragmentDirections.actionNavHomeAlertToNavGame();
                action.setRed(false);
                action.setWhite(true);
                navController.navigate(action);
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
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


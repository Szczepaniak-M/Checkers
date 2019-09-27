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
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;




import org.pwanb.checkers.R;
import org.pwanb.checkers.ui.game.GameFragmentDirections;


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
        HomeFragmentDirections.ActionNavHomeToNavGame action =  HomeFragmentDirections.actionNavHomeToNavGame();
        action.setRed(white);
        action.setRed(red);
        Navigation.findNavController(getView()).navigate(action);

    }
}
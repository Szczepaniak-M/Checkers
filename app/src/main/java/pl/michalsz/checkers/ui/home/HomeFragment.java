package pl.michalsz.checkers.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import pl.michalsz.checkers.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button btnComputer = view.findViewById(R.id.btn_computer);
        btnComputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Navigation.findNavController(getView()).navigate(R.id.action_nav_home_to_nav_home_alert);
            }
        });
        Button btnPlayer = view.findViewById(R.id.btn_player);
        btnPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                HomeFragmentDirections.ActionNavHomeToNavGame action = HomeFragmentDirections.actionNavHomeToNavGame();
                action.setRed(true);
                action.setRed(true);
                Navigation.findNavController(getView()).navigate(action);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
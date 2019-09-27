package org.pwanb.checkers.ui.rules;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.pwanb.checkers.R;

public class RulesFragment extends Fragment {

    private RulesViewModel rulesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rulesViewModel =
                ViewModelProviders.of(this).get(RulesViewModel.class);
        container.removeAllViews();
        View view = inflater.inflate(R.layout.fragment_rules, container, false);
        return view;
    }

}
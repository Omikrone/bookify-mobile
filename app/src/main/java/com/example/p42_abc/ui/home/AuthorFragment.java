package com.example.p42_abc.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.p42_abc.R;
import com.example.p42_abc.model.Author;

public class AuthorFragment extends Fragment {

    public AuthorFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_author, container, false);
        TextView authorDetails = view.findViewById(R.id.authorDetails);

        HomeViewModel viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        // Observer les données dans le ViewModel
        viewModel.getSelectedAuthor().observe(getViewLifecycleOwner(), author -> {
            if (author != null) {
                Log.d("AuthorFragment", "Selected Author: " + author.getFirstname() + " " + author.getLastname());
                // Mettre à jour le texte du TextView
                authorDetails.setText(author.getFirstname() + " " + author.getLastname());
                authorDetails.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
}

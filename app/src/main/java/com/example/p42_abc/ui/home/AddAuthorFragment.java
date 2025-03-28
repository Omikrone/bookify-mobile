package com.example.p42_abc.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.p42_abc.R;
import com.example.p42_abc.model.Author;
import com.example.p42_abc.retrofit.AuthorRequest;

public class AddAuthorFragment extends Fragment {

    private HomeViewModel _viewModel;

    public AddAuthorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_author, container, false);

        Button submitBtn = view.findViewById(R.id.submit);
        submitBtn.setOnClickListener(v -> {
            addAuthor(view);
        });

        _viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        return view;
    }

    private void addAuthor(View view) {

        EditText firstnameInput = view.findViewById(R.id.firstname);
        EditText lastnameInput = view.findViewById(R.id.lastname);

        String firstname = firstnameInput.getText().toString();
        String lastname = lastnameInput.getText().toString();

        AuthorRequest newAuthor = new AuthorRequest(firstname, lastname, "", null, null, null);
        _viewModel.addAuthor(newAuthor).observe(getViewLifecycleOwner(), addedAuthor -> {
            if (addedAuthor != null) {
                Toast.makeText(getContext(), "Auteur ajouté avec succès!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Échec de l'ajout", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.p42_abc.ui.book;

import static android.view.View.INVISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.p42_abc.R;
import com.example.p42_abc.model.BookRequest;
import com.example.p42_abc.viewmodel.BookViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddBookFragment extends Fragment {

    private BookViewModel _viewModel;
    private EditText _title;
    private EditText _description;
    private EditText _publicationYear;
    private EditText _auhorId;

    public AddBookFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_book, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Ajouter un livre");

        Button submitBtn = view.findViewById(R.id.submit);
        submitBtn.setOnClickListener(v -> {
            addAuthor();
        });

        _title = view.findViewById(R.id.bookTitleInput);
        _description = view.findViewById(R.id.descriptionBookInput);
        _publicationYear = view.findViewById(R.id.publicationYearBookInput);
        _auhorId = view.findViewById(R.id.authorIdBookInput);

        _viewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        return view;
    }

    private void addAuthor() {

        if (!validateFields()) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = _title.getText().toString();
        String description = _description.getText().toString();
        int publicationYear = Integer.parseInt(_publicationYear.getText().toString());
        int authorId = Integer.parseInt(_auhorId.getText().toString());

        BookRequest newBook = new BookRequest(title, publicationYear, description);

        _viewModel.addBook(newBook, authorId).observe(getViewLifecycleOwner(), addedAuthor -> {
            if (addedAuthor != null) {
                Toast.makeText(getContext(), "Ajout réussi!", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_home);

            } else {
                Toast.makeText(getContext(), "Échec de l'ajout", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateFields() {
        boolean isValid = true;

        if (_title.getText().toString().trim().isEmpty()) {
            _title.setBackgroundResource(R.drawable.edittext_error);
            isValid = false;
        } else {
            _title.setBackgroundResource(R.drawable.edittext_normal);
        }

        if (_auhorId.getText().toString().trim().isEmpty()) {
            _auhorId.setBackgroundResource(R.drawable.edittext_error);
            isValid = false;
        } else {
            _auhorId.setBackgroundResource(R.drawable.edittext_normal);
        }
        return isValid;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(INVISIBLE);
    }
}

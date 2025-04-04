package com.example.p42_abc.ui.book;

import static android.view.View.INVISIBLE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.MenuPopupWindow;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.p42_abc.R;
import com.example.p42_abc.databinding.FragmentAddBookBinding;
import com.example.p42_abc.model.Author;
import com.example.p42_abc.model.AuthorRequest;
import com.example.p42_abc.model.BookRequest;
import com.example.p42_abc.viewmodel.AuthorViewModel;
import com.example.p42_abc.viewmodel.BookViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddBookFragment extends Fragment {

    private BookViewModel _viewModel;
    private EditText _title;
    private EditText _description;
    private EditText _publicationYear;
    private AutoCompleteTextView _authorId;
    private FragmentAddBookBinding _binding;
    private AuthorViewModel _authorViewModel;
    private ArrayAdapter<Author> _authorAdapter;
    private Author _selectedAuthor;

    public AddBookFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentAddBookBinding.inflate(inflater, container, false);
        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _viewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        _title = view.findViewById(R.id.bookTitleInput);
        _description = view.findViewById(R.id.descriptionBookInput);
        _publicationYear = view.findViewById(R.id.publicationYearBookInput);
        _authorId = _binding.authorDropdown;

        _authorViewModel = new ViewModelProvider(requireActivity()).get(AuthorViewModel.class);
        setupAuthorDropdown();
        _authorViewModel.loadAuthors(1, null, null);

        _authorViewModel.getAuthorList().observe(getViewLifecycleOwner(), authors -> {
            if (authors != null) {
                _authorAdapter.clear();
                _authorAdapter.addAll(authors);
            }
        });

        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(INVISIBLE);

        Button submitBtn = view.findViewById(R.id.submit);
        submitBtn.setOnClickListener(v -> {
            addAuthor();
        });

        _viewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
    }


    private void setupAuthorDropdown() {
        _authorAdapter = new ArrayAdapter<Author>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line
        ) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                Author author = getItem(position);
                if (author != null) {
                    view.setText(author.getFirstname() + " " + author.getLastname());
                }
                return view;
            }
        };

        _authorId.setAdapter(_authorAdapter);

        _authorId.setOnItemClickListener((parent, view, position, id) -> {
            _selectedAuthor = _authorAdapter.getItem(position);
            if (_selectedAuthor != null) {
                _authorId.setText(_selectedAuthor.getFirstname() + " " + _selectedAuthor.getLastname(), false);
            }
        });
    }

    private void addAuthor() {

        if (!validateFields()) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = _title.getText().toString();
        String description = _description.getText().toString();

        int publicationYear = Integer.parseInt(_publicationYear.getText().toString());
        int authorId = _selectedAuthor.getId();
        Log.d("AddBookFragment", "Auteur sélectionné : " + _selectedAuthor.getId() + " " + publicationYear);

        BookRequest newBook = new BookRequest(title, publicationYear, description);

        _viewModel.addBook(newBook, authorId).observe(getViewLifecycleOwner(), addedAuthor -> {
            if (addedAuthor != null) {
                Toast.makeText(getContext(), "Ajout réussi!", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_books);

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

        if (_publicationYear.getText().toString().trim().isEmpty()) {
            _publicationYear.setBackgroundResource(R.drawable.edittext_error);
            isValid = false;
        } else {
            _publicationYear.setBackgroundResource(R.drawable.edittext_normal);
        }

        if (_description.getText().toString().trim().isEmpty()) {
            _description.setBackgroundResource(R.drawable.edittext_error);
            isValid = false;
        } else {
            _description.setBackgroundResource(R.drawable.edittext_normal);
        }

        if (_selectedAuthor == null) {
            Log.d("AddBookFragment", "Aucun auteur sélectionné");
            //_auhorId.setBackgroundResource(R.drawable.edittext_error);
            isValid = false;
        } else {
            //_auhorId.setBackgroundResource(R.drawable.edittext_normal);
        }
        return isValid;
    }

}

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
    // Références aux vues et données
    private BookViewModel _viewModel;
    private EditText _title;
    private EditText _description;
    private EditText _publicationYear;
    private AutoCompleteTextView _authorId;
    private FragmentAddBookBinding _binding;
    private AuthorViewModel _authorViewModel;
    private ArrayAdapter<Author> _authorAdapter;
    private Author _selectedAuthor; // Auteur sélectionné dans le dropdown

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialisation du ViewBinding
        _binding = FragmentAddBookBinding.inflate(inflater, container, false);
        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialisation des ViewModels
        _viewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
        _authorViewModel = new ViewModelProvider(requireActivity()).get(AuthorViewModel.class);

        // Liaison des vues
        _title = view.findViewById(R.id.bookTitleInput);
        _description = view.findViewById(R.id.descriptionBookInput);
        _publicationYear = view.findViewById(R.id.publicationYearBookInput);
        _authorId = _binding.authorDropdown;

        // Configuration du dropdown des auteurs
        setupAuthorDropdown();
        _authorViewModel.loadAuthors(1, null, null);

        // Observation de la liste des auteurs
        _authorViewModel.getAuthorList().observe(getViewLifecycleOwner(), authors -> {
            if (authors != null) {
                _authorAdapter.clear();
                _authorAdapter.addAll(authors);
            }
        });

        // Masquage du FAB
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(INVISIBLE);

        // Gestion du bouton de soumission
        Button submitBtn = view.findViewById(R.id.submit);
        submitBtn.setOnClickListener(v -> addBook()); // Renommé pour plus de clarté
    }

    /**
     * Configure le dropdown de sélection des auteurs
     */
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

        // Gestion de la sélection d'un auteur
        _authorId.setOnItemClickListener((parent, view, position, id) -> {
            _selectedAuthor = _authorAdapter.getItem(position);
            if (_selectedAuthor != null) {
                _authorId.setText(_selectedAuthor.getFirstname() + " " +_selectedAuthor.getLastname(), false);
            }
        });
    }

    /**
     * Valide et ajoute un nouveau livre
     */
    private void addBook() {
        if (!validateFields()) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        // Création de la requête
        BookRequest newBook = new BookRequest(
                _title.getText().toString(),
                Integer.parseInt(_publicationYear.getText().toString()),
                _description.getText().toString()
        );

        // Appel au ViewModel
        _viewModel.addBook(newBook, _selectedAuthor.getId())
                .observe(getViewLifecycleOwner(), addedBook -> {
                    if (addedBook != null) {
                        showSuccessAndNavigate();
                    } else {
                        Toast.makeText(getContext(), "Échec de l'ajout", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Affiche un message de succès et retourne à la liste
     */
    private void showSuccessAndNavigate() {
        Toast.makeText(getContext(), "Ajout réussi!", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(requireActivity(),
                        R.id.nav_host_fragment_activity_main)
                .navigate(R.id.navigation_books);
    }

    /**
     * Valide les champs du formulaire
     * @return true si tous les champs requis sont valides
     */
    private boolean validateFields() {
        boolean isValid = true;

        // Validation du titre
        if (_title.getText().toString().trim().isEmpty()) {
            _title.setBackgroundResource(R.drawable.edittext_error);
            isValid = false;
        } else {
            _title.setBackgroundResource(R.drawable.edittext_normal);
        }

        // Validation de l'année
        if (_publicationYear.getText().toString().trim().isEmpty()) {
            _publicationYear.setBackgroundResource(R.drawable.edittext_error);
            isValid = false;
        } else {
            _publicationYear.setBackgroundResource(R.drawable.edittext_normal);
        }

        // Validation de la description
        if (_description.getText().toString().trim().isEmpty()) {
            _description.setBackgroundResource(R.drawable.edittext_error);
            isValid = false;
        } else {
            _description.setBackgroundResource(R.drawable.edittext_normal);
        }

        // Validation de l'auteur
        if (_selectedAuthor == null) {
            isValid = false;
        }

        return isValid;
    }
}
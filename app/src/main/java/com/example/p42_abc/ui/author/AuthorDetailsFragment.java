package com.example.p42_abc.ui.author;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.p42_abc.R;
import com.example.p42_abc.ui.book.BookAdapter;
import com.example.p42_abc.viewmodel.AuthorViewModel;
import com.example.p42_abc.viewmodel.BookViewModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AuthorDetailsFragment extends Fragment {
    private AuthorViewModel _viewModel;
    private RecyclerView _booksRecyclerView;
    private BookAdapter _bookAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_author_details, container, false);

        // Initialisation des vues
        TextView authorFullName = view.findViewById(R.id.authorFullName);
        TextView authorBio = view.findViewById(R.id.authorBio);
        TextView authorBirthDate = view.findViewById(R.id.authorBirthDate);
        TextView authorDeathDate = view.findViewById(R.id.authorDeathDate);
        ImageView authorImage = view.findViewById(R.id.authorImage);
        Button authorDeleteButton = view.findViewById(R.id.authorDeleteButton);
        _booksRecyclerView = view.findViewById(R.id.booksRecyclerView);

        // Configuration de la liste des livres
        setupAuthorBooks();

        // Initialisation du ViewModel
        _viewModel = new ViewModelProvider(requireActivity()).get(AuthorViewModel.class);

        /**
         * Charge et observe les livres de l'auteur
         */
        _viewModel.loadAuthorBooks();
        _viewModel.getAuthorBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                _bookAdapter.updateList(books);
            }
        });

        /**
         * Gestion de la suppression de l'auteur
         */
        authorDeleteButton.setOnClickListener(v -> {
            if (_viewModel.deleteAuthor(_viewModel.getSelectedAuthor().getValue())) {
                showToast("Auteur supprimé!");
                navigateToAuthorsList();
            } else {
                showToast("Erreur lors de la suppression");
            }
        });

        /**
         * Observation des données de l'auteur sélectionné
         */
        _viewModel.getSelectedAuthor().observe(getViewLifecycleOwner(), author -> {
            if (author != null) {
                Log.d("AuthorDetails", "Affichage de: " + author.getFirstname());

                // Affichage des informations de base
                authorFullName.setText(author.getFirstname() + " " + author.getLastname());

                if (author.getBio() != null) {
                    authorBio.setText(author.getBio());
                }

                // Formatage et affichage des dates
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formatAndDisplayDate(author.getBirthDate(), authorBirthDate, "Date de naissance: ", outputFormatter);
                formatAndDisplayDate(author.getDeathDate(), authorDeathDate, "Date de décès: ", outputFormatter);

                // Chargement de l'image
                loadAuthorImage(author.getImage(), authorImage);
            }
        });

        return view;
    }

    /**
     * Configure le RecyclerView pour afficher les livres de l'auteur
     */
    private void setupAuthorBooks() {
        _booksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        _bookAdapter = new BookAdapter(new ArrayList<>(), book -> {
            _viewModel.clearBooks();
            BookViewModel bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
            bookViewModel.setSelectedBook(book);
            navigateToBookDetails();
        });

        _booksRecyclerView.setAdapter(_bookAdapter);
    }

    /**
     * Formate et affiche une date dans un TextView
     */
    private void formatAndDisplayDate(String isoDate, TextView textView, String prefix,
                                      DateTimeFormatter formatter) {
        if (isoDate != null) {
            LocalDateTime dateTime = Instant.parse(isoDate)
                    .atZone(ZoneOffset.UTC).toLocalDateTime();
            textView.setText(prefix + dateTime.format(formatter));
        }
    }

    /**
     * Charge l'image de l'auteur avec Glide
     */
    private void loadAuthorImage(String imageUrl, ImageView imageView) {
        Glide.with(getContext())
                .load(imageUrl)
                .circleCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView);
    }

    /**
     * Affiche un message Toast
     */
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Navigation vers la liste des auteurs
     */
    private void navigateToAuthorsList() {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_authors);
    }

    /**
     * Navigation vers les détails d'un livre
     */
    private void navigateToBookDetails() {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_authorFragment_to_bookDetailsFragment);
    }
}
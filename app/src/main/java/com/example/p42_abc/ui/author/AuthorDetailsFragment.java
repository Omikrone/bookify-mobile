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

    public AuthorDetailsFragment() {}

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_author_details, container, false);
        TextView authorFullName = view.findViewById(R.id.authorFullName);
        TextView authorBio = view.findViewById(R.id.authorBio);
        TextView authorBirthDate = view.findViewById(R.id.authorBirthDate);
        TextView authorDeathDate = view.findViewById(R.id.authorDeathDate);
        ImageView authorImage = view.findViewById(R.id.authorImage);
        Button authorDeleteButton = view.findViewById(R.id.authorDeleteButton);
        _booksRecyclerView = view.findViewById(R.id.booksRecyclerView);

        setupAuthorBooks();

        _viewModel = new ViewModelProvider(requireActivity()).get(AuthorViewModel.class);
        _viewModel.loadAuthorBooks();
        _viewModel.getAuthorBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                _bookAdapter.updateList(books);
            }
        });

        authorDeleteButton.setOnClickListener(v -> {
            if (_viewModel.deleteAuthor(_viewModel.getSelectedAuthor().getValue())) {
                Toast.makeText(getContext(), "Auteur supprimé!", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_authors);
            } else {
                Toast.makeText(getContext(), "Erreur lors de la suppression de l'auteur", Toast.LENGTH_SHORT).show();
            }
        });

        _viewModel.getSelectedAuthor().observe(getViewLifecycleOwner(), author -> {
            if (author != null) {
                Log.d("AuthorDetailsFragment", "Selected Author: " + author.getFirstname() + " " + author.getLastname());
                authorFullName.setText(author.getFirstname() + " " + author.getLastname());
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                if (author.getBio() != null) {
                    authorBio.setText(author.getBio());
                }

                if (author.getBirthDate() != null) {
                    LocalDateTime birthDateTime = Instant.parse(author.getBirthDate())
                            .atZone(ZoneOffset.UTC).toLocalDateTime();
                    authorBirthDate.setText("Date de naissance: " + birthDateTime.format(outputFormatter));
                }
                if (author.getDeathDate() != null) {
                    LocalDateTime deathDateTime = Instant.parse(author.getBirthDate())
                            .atZone(ZoneOffset.UTC).toLocalDateTime();
                    authorDeathDate.setText("Date de décès: " + deathDateTime.format(outputFormatter));
                }
                String imageUrl = author.getImage();

                Glide.with(getContext())
                        .load(imageUrl)
                        .circleCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(authorImage);
            }
        });

        return view;
    }

    public void setupAuthorBooks() {
        _booksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        _bookAdapter = new BookAdapter(new ArrayList<>(), book -> {
            _viewModel.clearBooks();
            BookViewModel bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
            bookViewModel.setSelectedBook(book);
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_authorFragment_to_bookDetailsFragment);
        });

        _booksRecyclerView.setAdapter(_bookAdapter);
    }
}

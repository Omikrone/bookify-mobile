package com.example.p42_abc.ui.book;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.p42_abc.R;
import com.example.p42_abc.model.Book;
import com.example.p42_abc.model.Tag;
import com.example.p42_abc.viewmodel.BookViewModel;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;

import java.util.List;

public class BookDetailsFragment extends Fragment {
    private View _view = null;
    private FlexboxLayout _bookTags = null; // Conteneur pour les tags
    private BookViewModel _viewModel = null;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.fragment_book_details, container, false);

        // Initialisation des vues
        TextView bookTitle = _view.findViewById(R.id.bookTitle);
        TextView bookPublicationYear = _view.findViewById(R.id.publicationYear);
        TextView bookDescription = _view.findViewById(R.id.description);
        ImageView bookImage = _view.findViewById(R.id.bookCover);
        Button bookDeleteButton = _view.findViewById(R.id.btn_delete);
        RatingBar bookRatingBar = _view.findViewById(R.id.bookRating);
        _bookTags = _view.findViewById(R.id.tagsContainer);

        // Initialisation du ViewModel
        _viewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
        Book selectedBook = _viewModel.getSelectedBook().getValue();

        // Chargement des tags associés au livre
        _viewModel.loadTags(selectedBook.getId());
        setupTags();

        /**
         * Gestion de la suppression du livre
         */
        bookDeleteButton.setOnClickListener(v -> {
            if (_viewModel.deleteBook(selectedBook)) {
                showToast("Livre supprimé!");
                navigateToBookList();
            } else {
                showToast("Erreur lors de la suppression");
            }
        });

        /**
         * Observation des données du livre sélectionné
         */
        _viewModel.getSelectedBook().observe(getViewLifecycleOwner(), book -> {
            if (book != null) {
                // Affichage des informations de base
                bookRatingBar.setRating(selectedBook.getAvgRating());
                bookTitle.setText(book.getTitle());
                bookPublicationYear.setText(String.valueOf(book.getPublicationYear()));
                bookDescription.setText(book.getDescription());

                // Chargement de l'image de couverture
                loadBookCover(selectedBook.getCover(), bookImage);
            }
        });

        return _view;
    }

    /**
     * Configure l'affichage des tags sous forme de chips
     */
    private void setupTags() {
        FlexboxLayout flexboxLayout = _view.findViewById(R.id.tagsContainer);
        flexboxLayout.removeAllViews(); // Nettoyage préalable

        _viewModel.getBookTags().observe(getViewLifecycleOwner(), tagList -> {
            if (tagList != null) {
                for (Tag tag : tagList) {
                    createTagChip(tag);
                }
            }
        });
    }

    /**
     * Crée un chip pour un tag donné
     */
    private void createTagChip(Tag tag) {
        Chip chip = new Chip(requireContext());
        // Configuration visuelle du chip
        chip.setGravity(Gravity.CENTER);
        chip.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(tag.getColor())));
        chip.setTextColor(Color.WHITE);
        chip.setChipStrokeWidth(1f);
        chip.setChipStrokeColor(ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.outline)));
        chip.setChipCornerRadius(getResources().getDimension(R.dimen.tag_corner_radius));

        // Gestion des padding
        int paddingHorizontal = getResources().getDimensionPixelSize(R.dimen.tag_padding_horizontal);
        int paddingVertical = getResources().getDimensionPixelSize(R.dimen.tag_padding_vertical);
        chip.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

        chip.setCloseIconVisible(false);
        chip.setText(tag.getName());
        _bookTags.addView(chip);
    }

    /**
     * Charge l'image de couverture avec Glide
     */
    private void loadBookCover(String imageUrl, ImageView imageView) {
        String cleanedUrl = imageUrl.trim().replaceAll("\\p{C}", "") + "?default=false";
        Glide.with(getContext())
                .load(cleanedUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imageView);
    }

    /**
     * Affiche un message Toast
     */
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Navigue vers la liste des livres
     */
    private void navigateToBookList() {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_books);
    }
}
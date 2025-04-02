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
    private FlexboxLayout _bookTags = null;
    private BookViewModel _viewModel = null;

    public BookDetailsFragment() {}

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.fragment_book_details, container, false);

        TextView bookTitle = _view.findViewById(R.id.bookTitle);
        TextView bookPublicationYear = _view.findViewById(R.id.publicationYear);
        TextView bookDescription = _view.findViewById(R.id.description);
        ImageView bookImage = _view.findViewById(R.id.bookCover);
        Button bookDeleteButton = _view.findViewById(R.id.btn_delete);
        RatingBar bookRatingBar = _view.findViewById(R.id.bookRating);
        _bookTags = _view.findViewById(R.id.tagsContainer);

        _viewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
        Book selectedBook = _viewModel.getSelectedBook().getValue();
        _viewModel.loadTags(selectedBook.getId());

        setupTags();
        bookDeleteButton.setOnClickListener(v -> {

            if (_viewModel.deleteBook(selectedBook)) {
                Toast.makeText(getContext(), "Livre supprimé!", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_books);
            } else {
                Toast.makeText(getContext(), "Erreur lors de la suppression du livre", Toast.LENGTH_SHORT).show();
            }
        });

        _viewModel.getSelectedBook().observe(getViewLifecycleOwner(), book -> {
            if (book != null) {
                bookRatingBar.setRating(selectedBook.getAvgRating());
                bookTitle.setText(book.getTitle());
                bookPublicationYear.setText("" + book.getPublicationYear());
                bookDescription.setText(book.getDescription());

                String imageUrl = selectedBook.getCover() + "?default=false";
                imageUrl = imageUrl.trim().replaceAll("\\p{C}", "");
                Glide.with(getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(bookImage);
            }
        });

        return _view;
    }

    private void setupTags() {
        MutableLiveData<List<Tag>> tags = _viewModel.getBookTags();
        FlexboxLayout flexboxLayout = _view.findViewById(R.id.tagsContainer);
        flexboxLayout.removeAllViews();

        tags.observe(getViewLifecycleOwner(), tagList -> {
            if (tagList != null) {
                for (Tag tag : tagList) {
                    ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor(tag.getColor()));
                    Chip chip = new Chip(requireContext());
                    chip.setGravity(Gravity.CENTER);
                    chip.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    chip.setChipBackgroundColor(colorStateList);
                    chip.setTextColor(Color.WHITE);
                    chip.setChipStrokeWidth(1f);
                    chip.setChipStrokeColor(ColorStateList.valueOf(
                            ContextCompat.getColor(requireContext(), R.color.outline)));
                    chip.setChipCornerRadius(getResources().getDimension(R.dimen.tag_corner_radius));
                    int paddingHorizontal = getResources().getDimensionPixelSize(R.dimen.tag_padding_horizontal);
                    int paddingVertical = getResources().getDimensionPixelSize(R.dimen.tag_padding_vertical);
                    chip.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

                    chip.setCloseIconVisible(false);
                    chip.setText(tag.getName());
                    chip.setPadding(16, 8, 16, 8);
                    _bookTags.addView(chip);
                }
            }
        });
    }
}

package com.example.p42_abc.ui.book;

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
import com.example.p42_abc.viewmodel.BookViewModel;

public class BookDetailsFragment extends Fragment {

    public BookDetailsFragment() {}

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_details, container, false);

        TextView bookTitle = view.findViewById(R.id.bookTitle);
        TextView bookPublicationYear = view.findViewById(R.id.publicationYear);
        TextView bookDescription = view.findViewById(R.id.description);

         BookViewModel viewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        viewModel.getSelectedBook().observe(getViewLifecycleOwner(), book -> {
            if (book != null) {
                Log.d("BookDetailsFragment", "Selected Book: " + book.getTitle() + " " + book.getPublicationYear());
                bookTitle.setText("Title: " + book.getTitle());
                bookPublicationYear.setText("Publication Year: " + book.getPublicationYear());
                bookDescription.setText("Description: " + book.getDescription());
            }
        });

        return view;
    }
}

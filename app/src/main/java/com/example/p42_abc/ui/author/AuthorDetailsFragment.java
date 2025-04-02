package com.example.p42_abc.ui.author;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.p42_abc.R;
import com.example.p42_abc.viewmodel.AuthorViewModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class AuthorDetailsFragment extends Fragment {

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

        AuthorViewModel viewModel = new ViewModelProvider(requireActivity()).get(AuthorViewModel.class);

        viewModel.getSelectedAuthor().observe(getViewLifecycleOwner(), author -> {
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
}

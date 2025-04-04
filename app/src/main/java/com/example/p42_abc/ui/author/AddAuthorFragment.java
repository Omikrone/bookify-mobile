package com.example.p42_abc.ui.author;

import static android.view.View.INVISIBLE;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.p42_abc.R;
import com.example.p42_abc.model.AuthorRequest;
import com.example.p42_abc.viewmodel.AuthorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class AddAuthorFragment extends Fragment {
    // Références aux vues et composants
    private AuthorViewModel _viewModel;
    private EditText _firstname;
    private EditText _lastname;
    private EditText _biography;
    private EditText _birthDate;
    private EditText _deathDate;
    private Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_author, container, false);

        // Initialisation des listeners
        Button submitBtn = view.findViewById(R.id.submit);
        submitBtn.setOnClickListener(v -> addAuthor());

        // Liaison des vues
        _firstname = view.findViewById(R.id.firstname);
        _lastname = view.findViewById(R.id.lastname);
        _biography = view.findViewById(R.id.biography);

        // Gestion des date pickers
        _birthDate = view.findViewById(R.id.birthDate);
        _birthDate.setOnClickListener(v -> {
            calendar = Calendar.getInstance();
            showDatePickerDialog(_birthDate);
        });

        _deathDate = view.findViewById(R.id.deathDate);
        _deathDate.setOnClickListener(v -> {
            calendar = Calendar.getInstance();
            showDatePickerDialog(_deathDate);
        });

        // Initialisation du ViewModel
        _viewModel = new ViewModelProvider(requireActivity()).get(AuthorViewModel.class);

        return view;
    }

    /**
     * Valide et ajoute un nouvel auteur via le ViewModel
     */
    private void addAuthor() {
        if (!validateFields()) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        // Conversion des dates au format ISO
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        String birthDateStr = parseDate(_birthDate.getText().toString(), inputFormatter);
        String deathDateStr = parseDate(_deathDate.getText().toString(), inputFormatter);

        // Création de la requête
        AuthorRequest newAuthor = new AuthorRequest(
                _firstname.getText().toString(),
                _lastname.getText().toString(),
                "", // Image vide par défaut
                _biography.getText().toString(),
                birthDateStr,
                deathDateStr
        );

        // Appel au ViewModel et gestion de la réponse
        _viewModel.addAuthor(newAuthor).observe(getViewLifecycleOwner(), addedAuthor -> {
            if (addedAuthor != null) {
                Toast.makeText(getContext(), "Ajout réussi!", Toast.LENGTH_SHORT).show();
                navigateBackToList();
            } else {
                Toast.makeText(getContext(), "Échec de l'ajout", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Valide les champs obligatoires (prénom et nom)
     * @return true si la validation est réussie
     */
    private boolean validateFields() {
        boolean isValid = true;

        if (_firstname.getText().toString().trim().isEmpty()) {
            _firstname.setBackgroundResource(R.drawable.edittext_error);
            isValid = false;
        } else {
            _firstname.setBackgroundResource(R.drawable.edittext_normal);
        }

        if (_lastname.getText().toString().trim().isEmpty()) {
            _lastname.setBackgroundResource(R.drawable.edittext_error);
            isValid = false;
        } else {
            _lastname.setBackgroundResource(R.drawable.edittext_normal);
        }
        return isValid;
    }

    /**
     * Affiche un date picker et met à jour l'EditText cible
     * @param date EditText à mettre à jour avec la date sélectionnée
     */
    private void showDatePickerDialog(EditText date) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    date.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    /**
     * Convertit une date locale en format ISO-8601
     */
    private String parseDate(String dateStr, DateTimeFormatter formatter) {
        if (dateStr.isEmpty()) return null;
        LocalDate date = LocalDate.parse(dateStr, formatter);
        return date.atStartOfDay().atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    /**
     * Retourne à la liste des auteurs après ajout réussi
     */
    private void navigateBackToList() {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_authors);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Cache le FAB dans cette vue
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(INVISIBLE);
    }
}
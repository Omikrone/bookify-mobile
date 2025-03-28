package com.example.p42_abc.ui.home;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.p42_abc.R;
import com.example.p42_abc.retrofit.AuthorRequest;

public class AddAuthorFragment extends Fragment {

    private HomeViewModel _viewModel;
    private EditText _firstname;
    private EditText _lastname;
    private EditText _biography;
    private EditText _birthDate;
    private EditText _deathDate;
    private Calendar calendar;

    public AddAuthorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_author, container, false);

        Button submitBtn = view.findViewById(R.id.submit);
        submitBtn.setOnClickListener(v -> {
            addAuthor();
        });

        _firstname = view.findViewById(R.id.firstname);
        _lastname = view.findViewById(R.id.lastname);
        _biography = view.findViewById(R.id.biography);

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

        _viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        return view;
    }

    private void addAuthor() {

        validateFields();

        String firstname = _firstname.getText().toString();
        String lastname = _lastname.getText().toString();

        AuthorRequest newAuthor = new AuthorRequest(firstname, lastname, "", null,
                _birthDate.getText().toString(), _deathDate.getText().toString());

        _viewModel.addAuthor(newAuthor).observe(getViewLifecycleOwner(), addedAuthor -> {
            if (addedAuthor != null) {
                Toast.makeText(getContext(), "Ajout réussi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Échec de l'ajout", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateFields() {
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

        if (isValid) {
            Toast.makeText(getContext(), "Tous les champs sont valides", Toast.LENGTH_SHORT).show();
        }
    }

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
}

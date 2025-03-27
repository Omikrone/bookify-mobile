package com.example.p42_abc.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p42_abc.model.Author;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Author> _selectedAuthor;
    private final MutableLiveData<List<Author>> _authorList;

    public HomeViewModel() {
        _selectedAuthor = new MutableLiveData<>();
        _authorList = new MutableLiveData<>();
        loadAuthors();
    }

    private void loadAuthors() {
        List<Author> authors = new ArrayList<>();
        String[] firstNames = {"John", "Jane", "Alice", "Bob", "Charlie", "Emma"};
        String[] lastNames = {"Doe", "Smith", "Brown", "Johnson", "White", "Davis"};

        for (int i = 0; i < 40; i++) {
            String firstName = firstNames[i % firstNames.length];
            String lastName = lastNames[i % lastNames.length];
            authors.add(new Author(lastName, firstName));
        }

        _authorList.setValue(authors);
    }


    public void setSelectedAuthor(Author author) {
        Log.d("AuthorFragment", "Author received: " + this);
        _selectedAuthor.setValue(author);
    }

    public MutableLiveData<Author> getSelectedAuthor() {
        return _selectedAuthor;
    }

    public LiveData<List<Author>> getAuthorList() {
        return _authorList;
    }
}
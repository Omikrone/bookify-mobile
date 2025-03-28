package com.example.p42_abc.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p42_abc.model.Author;
import com.example.p42_abc.retrofit.ApiService;
import com.example.p42_abc.retrofit.AuthorRequest;
import com.example.p42_abc.retrofit.RetrofitClient;

import java.util.List;

import com.example.p42_abc.retrofit.AuthorRepository;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Author> _selectedAuthor;
    private final MutableLiveData<List<Author>> _authorList;

    private final AuthorRepository _authorRepository;


    public HomeViewModel() {
        _selectedAuthor = new MutableLiveData<>();
        _authorList = new MutableLiveData<>();
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        _authorRepository = RetrofitClient.provideAuthorRepository(apiService);
        loadAuthors();
    }


    private void loadAuthors() {
        _authorRepository.getAuthors().observeForever(authors -> {
            if (authors != null) {
                _authorList.setValue(authors);
            }
        });
    }

    public MutableLiveData<Author> addAuthor(AuthorRequest authorRequest) {
        return _authorRepository.addAuthor(authorRequest);
    }


    public void setSelectedAuthor(Author author) {
        _selectedAuthor.setValue(author);
    }

    public MutableLiveData<Author> getSelectedAuthor() {
        return _selectedAuthor;
    }

    public LiveData<List<Author>> getAuthorList() {
        return _authorList;
    }
}
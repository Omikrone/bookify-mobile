package com.example.p42_abc.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p42_abc.model.Author;
import com.example.p42_abc.db.ApiService;
import com.example.p42_abc.model.AuthorRequest;
import com.example.p42_abc.db.ServiceInstantiate;

import java.util.ArrayList;
import java.util.List;

import com.example.p42_abc.db.AuthorRepository;

public class AuthorViewModel extends ViewModel {

    private final MutableLiveData<Author> _selectedAuthor;
    private final MutableLiveData<List<Author>> _authorList;

    private final AuthorRepository _authorRepository;
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);

    public AuthorViewModel() {
        _selectedAuthor = new MutableLiveData<>();
        _authorList = new MutableLiveData<>();
        ApiService apiService = ServiceInstantiate.getClient().create(ApiService.class);
        _authorRepository = ServiceInstantiate.provideAuthorRepository(apiService);
    }

    public boolean loadAuthors(int page) {
        if (Boolean.TRUE.equals(_isLoading.getValue())) return false;
        _isLoading.setValue(true);

        _authorRepository.getAuthors(page).observeForever(authors -> {
            if (authors != null) {
                List<Author> currentList = _authorList.getValue();
                if (currentList == null) {
                    _authorList.setValue(authors);
                } else {
                    List<Author> updatedList = new ArrayList<>(currentList);
                    updatedList.addAll(authors);
                    _authorList.setValue(updatedList);
                }
            }
            _isLoading.setValue(false);
        });

        return true;
    }

    public MutableLiveData<Author> addAuthor(AuthorRequest authorRequest) {
        return _authorRepository.addAuthor(authorRequest);
    }

    public Boolean deleteAuthor(Author author) {
        if (_authorRepository.deleteAuthor(author.getId())) {
            List<Author> currentList = _authorList.getValue();
            if (currentList != null) {
                currentList.remove(author);
                _authorList.setValue(currentList);
            }
            return true;
        } else {
            return false;
        }
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

    public LiveData<Boolean> isLoading() {
        return _isLoading;
    }
}
package com.example.p42_abc.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p42_abc.model.Author;
import com.example.p42_abc.retrofit.ApiService;
import com.example.p42_abc.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.p42_abc.retrofit.AuthorRepository;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Author> _selectedAuthor;
    private final MutableLiveData<List<Author>> _authorList;

    private final AuthorRepository _authorRepository;

    @ViewModelInject
    public HomeViewModel(AuthorRepository authorRepository) {
        _selectedAuthor = new MutableLiveData<>();
        _authorList = new MutableLiveData<>();
        _authorRepository = authorRepository;
        loadAuthors();
    }


    private void loadAuthors() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<List<Author>> call = apiService.getAuthors();
        call.enqueue(new Callback<List<Author>>() {
            @Override
            public void onResponse(@NonNull Call<List<Author>> call, @NonNull Response<List<Author>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _authorList.setValue(response.body());
                    Log.d("HomeViewModel", "Requête réussie : " + response.body().toString());
                } else {
                    Log.e("HomeViewModel", "Erreur dans la réponse API");
                }
            }

            @Override
            public void onFailure(Call<List<Author>> call, Throwable t) {
                Log.e("HomeViewModel", "Erreur lors de la récupération des auteurs", t);
            }
        });
    }

    public LiveData<Author> addAuthor(Author author) {
        return _authorRepository.addAuthor(author);
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
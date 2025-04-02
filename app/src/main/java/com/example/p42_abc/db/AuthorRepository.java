package com.example.p42_abc.db;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.p42_abc.model.Author;
import com.example.p42_abc.model.AuthorRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorRepository {
    private final ApiService _apiService;
    private final int PAGE_SIZE = 20;

    public AuthorRepository(ApiService apiService) {
        _apiService = apiService;
    }

    public LiveData<List<Author>> getAuthors(int page) {

        MutableLiveData<List<Author>> authorList = new MutableLiveData<>();

        Call<List<Author>> call = _apiService.getAuthors(page, PAGE_SIZE);
        call.enqueue(new Callback<List<Author>>() {
            @Override
            public void onResponse(@NonNull Call<List<Author>> call, @NonNull Response<List<Author>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<Author> newAuthors = response.body();

                    List<Author> currentList = authorList.getValue();
                    if (currentList == null) {
                        authorList.setValue(newAuthors);
                    } else {
                        List<Author> updatedList = new ArrayList<>(currentList);
                        updatedList.addAll(newAuthors);
                        authorList.setValue(updatedList);
                    }
                } else {
                    Log.e("AuthorRepository", "Erreur dans la réponse API");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Author>> call, @NonNull Throwable t) {
                Log.e("AuthorRepository", "Erreur lors de la récupération des auteurs", t);
            }
        });

        return authorList;
    }


    public MutableLiveData<Author> addAuthor(AuthorRequest author) {
        MutableLiveData<Author> result = new MutableLiveData<>();

        _apiService.addAuthor(author).enqueue(new Callback<Author>() {
            @Override
            public void onResponse(@NonNull Call<Author> call, @NonNull Response<Author> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                } else {
                    Log.e("AuthorRepository", "Échec de l'ajout : " + response.errorBody());
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Author> call, @NonNull Throwable t) {
                Log.e("AuthorRepository", "Erreur réseau : " + t.getMessage());
                result.setValue(null);
            }
        });

        return result;
    }

    public Boolean deleteAuthor(int authorId) {
        MutableLiveData<Author> result = new MutableLiveData<>();

        _apiService.deleteAuthor(authorId).enqueue(new Callback<Author>() {
            @Override
            public void onResponse(@NonNull Call<Author> call, @NonNull Response<Author> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                } else {
                    Log.e("AuthorRepository", "Échec de la suppression : " + response.errorBody());
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Author> call, @NonNull Throwable t) {
                Log.e("AuthorRepository", "Erreur réseau : " + t.getMessage());
                result.setValue(null);
            }
        });

        return true;
    }
}

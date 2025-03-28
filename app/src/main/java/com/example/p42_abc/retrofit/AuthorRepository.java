package com.example.p42_abc.retrofit;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.p42_abc.model.Author;
import com.example.p42_abc.retrofit.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorRepository {
    private final ApiService apiService;

    public AuthorRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Author> addAuthor(Author author) {
        MutableLiveData<Author> result = new MutableLiveData<>();

        apiService.addAuthor(author).enqueue(new Callback<Author>() {
            @Override
            public void onResponse(@NonNull Call<Author> call, @NonNull Response<Author> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                } else {
                    Log.e("AuthorRepository", "Échec de l'ajout : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Author> call, @NonNull Throwable t) {
                Log.e("AuthorRepository", "Erreur réseau : " + t.getMessage());
            }
        });

        return result;
    }
}

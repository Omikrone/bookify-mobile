package com.example.p42_abc.db;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.p42_abc.model.Author;
import com.example.p42_abc.model.AuthorRequest;
import com.example.p42_abc.model.Book;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class AuthorRepository {
    private final ApiService _apiService; // Service API Retrofit
    private final int PAGE_SIZE = 20; // Taille de pagination fixe

    public AuthorRepository(ApiService apiService) {
        _apiService = apiService;
    }

    /**
     * Récupère une liste paginée d'auteurs avec filtres optionnels
     * @param page Numéro de page (commence à 1)
     * @param firstname Filtre sur le prénom (optionnel)
     * @param lastname Filtre sur le nom (optionnel)
     * @return LiveData contenant la liste des auteurs
     */
    public LiveData<List<Author>> getAuthors(int page, String firstname, String lastname) {
        MutableLiveData<List<Author>> authorList = new MutableLiveData<>();

        Call<List<Author>> call = _apiService.getAuthors(page, PAGE_SIZE, firstname, lastname);
        call.enqueue(new Callback<List<Author>>() {
            @Override
            public void onResponse(@NonNull Call<List<Author>> call, @NonNull Response<List<Author>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Concaténation avec les données existantes pour le scroll infini
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
                    Log.e("AuthorRepository", "Erreur API: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Author>> call, @NonNull Throwable t) {
                Log.e("AuthorRepository", "Erreur réseau", t);
            }
        });

        return authorList;
    }

    /**
     * Récupère les livres d'un auteur spécifique
     * @param authorId ID de l'auteur
     * @return LiveData contenant la liste des livres
     */
    public LiveData<List<Book>> getBooksByAuthor(int authorId) {
        MutableLiveData<List<Book>> bookList = new MutableLiveData<>();

        _apiService.getBooksByAuthor(authorId).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call, @NonNull Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookList.setValue(response.body());
                } else {
                    Log.e("AuthorRepository", "Erreur API: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                Log.e("AuthorRepository", "Erreur réseau", t);
            }
        });

        return bookList;
    }

    /**
     * Ajoute un nouvel auteur
     * @param author Données de l'auteur à créer
     * @return LiveData contenant l'auteur créé ou null en cas d'erreur
     */
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

    /**
     * Supprime un auteur
     * @param authorId ID de l'auteur à supprimer
     * @return true si la requête a été envoyée (le résultat réel est asynchrone)
     */
    public Boolean deleteAuthor(int authorId) {
        final MutableLiveData<Boolean> result = new MutableLiveData<>();

        _apiService.deleteAuthor(authorId).enqueue(new Callback<Author>() {
            @Override
            public void onResponse(@NonNull Call<Author> call, @NonNull Response<Author> response) {
                result.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(@NonNull Call<Author> call, @NonNull Throwable t) {
                Log.e("AuthorRepository", "Erreur réseau", t);
                result.setValue(false);
            }
        });

        return true; // Retour immédiat indiquant que la requête a été lancée
    }
}
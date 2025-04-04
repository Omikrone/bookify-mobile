package com.example.p42_abc.db;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.p42_abc.model.Author;
import com.example.p42_abc.model.AuthorRequest;
import com.example.p42_abc.model.Book;
import com.example.p42_abc.model.BookRequest;
import com.example.p42_abc.model.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository {
    private final ApiService _apiService; // Service API Retrofit
    private final int PAGE_SIZE = 20; // Taille de pagination fixe

    /**
     * Constructeur avec injection de dépendance
     * @param apiService Instance du service API
     */
    public BookRepository(ApiService apiService) {
        _apiService = apiService;
    }

    /**
     * Récupère une liste paginée de livres avec filtre optionnel
     * @param page Numéro de page (commence à 1)
     * @param title Filtre sur le titre (optionnel)
     * @return LiveData contenant la liste des livres
     */
    public LiveData<List<Book>> getBooks(int page, String title) {
        MutableLiveData<List<Book>> bookList = new MutableLiveData<>();

        Call<List<Book>> call = _apiService.getBooks(page, PAGE_SIZE, title);
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call, @NonNull Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> newBooks = response.body();
                    Log.d("BookRepository", "Nombre de livres reçus : " + newBooks.size());

                    // Concaténation avec les données existantes pour le scroll infini
                    List<Book> currentList = bookList.getValue();
                    List<Book> updatedList = currentList != null ?
                            new ArrayList<>(currentList) : new ArrayList<>();
                    updatedList.addAll(newBooks);
                    bookList.setValue(updatedList);
                } else {
                    Log.e("BookRepository", "Erreur API - Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                Log.e("BookRepository", "Erreur réseau", t);
            }
        });

        return bookList;
    }

    /**
     * Ajoute un nouveau livre pour un auteur spécifique
     * @param authorId ID de l'auteur
     * @param book Données du livre à créer
     * @return LiveData contenant le livre créé ou null en cas d'erreur
     */
    public MutableLiveData<Book> addBook(int authorId, BookRequest book) {
        MutableLiveData<Book> result = new MutableLiveData<>();

        _apiService.addBook(authorId, book).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(@NonNull Call<Book> call, @NonNull Response<Book> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                    Log.d("BookRepository", "Livre ajouté avec ID: " + response.body().getId());
                } else {
                    Log.e("BookRepository", "Échec de l'ajout - Code: " + response.code());
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Book> call, @NonNull Throwable t) {
                Log.e("BookRepository", "Erreur réseau", t);
                result.setValue(null);
            }
        });

        return result;
    }

    /**
     * Supprime un livre
     * @param bookId ID du livre à supprimer
     * @return LiveData contenant le résultat de l'opération
     */
    public Boolean deleteBook(int bookId) {
        MutableLiveData<Book> result = new MutableLiveData<>();

        _apiService.deleteBook(bookId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Book> call, @NonNull Response<Book> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                } else {
                    Log.e("BookRepository", "Échec de la suppression : " + response.errorBody());
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Book> call, @NonNull Throwable t) {
                Log.e("BookRepository", "Erreur réseau : " + t.getMessage());
                result.setValue(null);
            }
        });

        return true;
    }

    /**
     * Récupère les tags associés à un livre
     * @param bookId ID du livre
     * @return LiveData contenant la liste des tags
     */
    public LiveData<List<Tag>> getTags(int bookId) {
        MutableLiveData<List<Tag>> tagList = new MutableLiveData<>();

        _apiService.getTags(bookId).enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(@NonNull Call<List<Tag>> call, @NonNull Response<List<Tag>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Tag> newTags = response.body();
                    Log.d("BookRepository", "Nombre de tags reçus : " + newTags.size());

                    // Concaténation avec les tags existants
                    List<Tag> currentList = tagList.getValue();
                    List<Tag> updatedList = currentList != null ?
                            new ArrayList<>(currentList) : new ArrayList<>();
                    updatedList.addAll(newTags);
                    tagList.setValue(updatedList);
                } else {
                    Log.e("BookRepository", "Erreur API - Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Tag>> call, @NonNull Throwable t) {
                Log.e("BookRepository", "Erreur réseau", t);
            }
        });

        return tagList;
    }
}
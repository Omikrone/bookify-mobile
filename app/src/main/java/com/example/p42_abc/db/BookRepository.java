package com.example.p42_abc.db;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.p42_abc.model.Author;
import com.example.p42_abc.model.Book;
import com.example.p42_abc.model.BookRequest;
import com.example.p42_abc.model.Tag;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository {
    private final ApiService _apiService;
    private final int PAGE_SIZE = 20;

    public BookRepository(ApiService apiService) {
        _apiService = apiService;
    }

    public LiveData<List<Book>> getBooks(int page) {

        MutableLiveData<List<Book>> bookList = new MutableLiveData<>();

        Call<List<Book>> call = _apiService.getBooks(page, PAGE_SIZE);
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call, @NonNull Response<List<Book>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<Book> newBooks = response.body();
                    Log.d("BookRepository", "Livres récupérés : " + response.body());

                    List<Book> currentList = bookList.getValue();
                    if (currentList == null) {
                        bookList.setValue(newBooks);
                    } else {
                        List<Book> updatedList = new ArrayList<>(currentList);
                        updatedList.addAll(newBooks);
                        bookList.setValue(updatedList);
                    }
                } else {
                    Log.e("HomeViewModel", "Erreur dans la réponse API");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                Log.e("HomeViewModel", "Erreur lors de la récupération des livres", t);
            }
        });

        return bookList;
    }


    public MutableLiveData<Book> addBook(BookRequest book, int authorId) {
        MutableLiveData<Book> result = new MutableLiveData<>();

        _apiService.addBook(authorId, book).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(@NonNull Call<Book> call, @NonNull Response<Book> response) {
                if (response.isSuccessful()) {
                    Log.d("BookRepository", "Livre ajouté avec succès : " + response.body());
                    result.setValue(response.body());
                } else {
                    Log.e("BookRepository", "Échec de l'ajout : " + response.errorBody());
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Book> call, @NonNull Throwable t) {
                Log.e("BookRepository", "Erreur réseau : " + t.getMessage());
                result.setValue(null);
            }
        });

        return result;
    }

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

    public LiveData<List<Tag>> getTags(int bookId) {

        MutableLiveData<List<Tag>> tagList = new MutableLiveData<>();

        Call<List<Tag>> call = _apiService.getTags(bookId);
        call.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(@NonNull Call<List<Tag>> call, @NonNull Response<List<Tag>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<Tag> newTags = response.body();
                    Log.d("BookRepository", "Livres récupérés : " + response.body());

                    List<Tag> currentList = tagList.getValue();
                    if (currentList == null) {
                        tagList.setValue(newTags);
                    } else {
                        List<Tag> updatedList = new ArrayList<>(currentList);
                        updatedList.addAll(newTags);
                        tagList.setValue(updatedList);
                    }
                } else {
                    Log.e("HomeViewModel", "Erreur dans la réponse API");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Tag>> call, @NonNull Throwable t) {
                Log.e("HomeViewModel", "Erreur lors de la récupération des livres", t);
            }
        });

        return tagList;
    }
}

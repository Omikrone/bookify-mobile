package com.example.p42_abc.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p42_abc.model.Book;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<List<Book>> _books;

    public DashboardViewModel() {
        _books = new MutableLiveData<>();
        loadBooks();
    }

    private void loadBooks() {
        List<Book> books = new ArrayList<>();
        for (int i=0; i<20; i++) {
            Book book = new Book("Mein Kampf", "Hitler's masterpiece");
            books.add(book);
        }
        _books.setValue(books);

    }

    public LiveData<List<Book>> getBookList() {
        return _books;
    }
}
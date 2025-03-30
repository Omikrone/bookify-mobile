package com.example.p42_abc.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p42_abc.model.Book;
import com.example.p42_abc.retrofit.ApiService;
import com.example.p42_abc.retrofit.BookRepository;
import com.example.p42_abc.retrofit.BookRequest;
import com.example.p42_abc.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<Book> _selectedBook;
    private final MutableLiveData<List<Book>> _bookList;

    private final BookRepository _bookRepository;
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);

    public DashboardViewModel() {
        _selectedBook = new MutableLiveData<>();
        _bookList = new MutableLiveData<>();
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        _bookRepository = RetrofitClient.provideBookRepository(apiService);
    }

    public boolean loadBooks(int page) {
        if (Boolean.TRUE.equals(_isLoading.getValue())) return false;
        _isLoading.setValue(true);

        _bookRepository.getBooks(page).observeForever(books -> {
            if (books != null) {
                List<Book> currentList = _bookList.getValue();
                if (currentList == null) {
                    _bookList.setValue(books);
                } else {
                    List<Book> updatedList = new ArrayList<>(currentList);
                    updatedList.addAll(books);
                    _bookList.setValue(updatedList);
                }
            }
            _isLoading.setValue(false);
        });

        return true;
    }

    public MutableLiveData<Book> addBook(BookRequest bookRequest) {
        return _bookRepository.addBook(bookRequest);
    }

    public void setSelectedBook(Book book) {
        _selectedBook.setValue(book);
    }

    public MutableLiveData<Book> getSelectedBook() {
        return _selectedBook;
    }

    public LiveData<List<Book>> getBookList() {
        return _bookList;
    }

    public LiveData<Boolean> isLoading() {
        return _isLoading;
    }
}
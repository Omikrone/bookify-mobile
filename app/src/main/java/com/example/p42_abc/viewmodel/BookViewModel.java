package com.example.p42_abc.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p42_abc.model.Book;
import com.example.p42_abc.db.ApiService;
import com.example.p42_abc.db.BookRepository;
import com.example.p42_abc.model.BookRequest;
import com.example.p42_abc.db.ServiceInstantiate;

import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends ViewModel {

    private final MutableLiveData<Book> _selectedBook;
    private final MutableLiveData<List<Book>> _bookList;

    private final BookRepository _bookRepository;
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);

    public BookViewModel() {
        _selectedBook = new MutableLiveData<>();
        _bookList = new MutableLiveData<>();
        ApiService apiService = ServiceInstantiate.getClient().create(ApiService.class);
        _bookRepository = ServiceInstantiate.provideBookRepository(apiService);
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

    public MutableLiveData<Book> addBook(BookRequest bookRequest, int authorId) {
        return _bookRepository.addBook(bookRequest, authorId);
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
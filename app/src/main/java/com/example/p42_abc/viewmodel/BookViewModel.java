package com.example.p42_abc.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p42_abc.model.Author;
import com.example.p42_abc.model.Book;
import com.example.p42_abc.db.ApiService;
import com.example.p42_abc.db.BookRepository;
import com.example.p42_abc.model.BookRequest;
import com.example.p42_abc.db.ServiceInstantiate;
import com.example.p42_abc.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends ViewModel {

    private final MutableLiveData<Book> _selectedBook;
    private final MutableLiveData<List<Book>> _bookList;
    private final MutableLiveData<List<Tag>> _bookTags;
    private final MutableLiveData<Book> _addedBook = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();

    private final BookRepository _bookRepository;
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);

    public BookViewModel() {
        _selectedBook = new MutableLiveData<>();
        _bookList = new MutableLiveData<>();
        _bookTags = new MutableLiveData<>();
        ApiService apiService = ServiceInstantiate.getClient().create(ApiService.class);
        _bookRepository = ServiceInstantiate.provideBookRepository(apiService);
    }

    public boolean loadBooks(int page, String title) {
        if (Boolean.TRUE.equals(_isLoading.getValue())) return false;
        _isLoading.setValue(true);

        _bookRepository.getBooks(page, title).observeForever(books -> {
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

    public Boolean deleteBook(Book book) {
        if (_bookRepository.deleteBook(book.getId())) {
            List<Book> currentList = _bookList.getValue();
            if (currentList != null) {
                currentList.remove(book);
                _bookList.setValue(currentList);
            }
            return true;
        } else {
            return false;
        }
    }

    public void loadTags(int bookId) {
        _bookRepository.getTags(bookId).observeForever(tags -> {

            if (tags != null) {
                List<Tag> currentTags = _bookTags.getValue();
                List<Tag> updatedTags = new ArrayList<>(currentTags);
                updatedTags.addAll(tags);
                _bookTags.setValue(updatedTags);

            }
        });
    }

    public LiveData<Book> addBook(BookRequest bookRequest, int authorId) {
        return _bookRepository.addBook(authorId, bookRequest);
    }

    public LiveData<Book> getAddedBook() {
        return _addedBook;
    }

    public LiveData<String> getError() {
        return _error;
    }

    public void clearBooks() {
        _bookList.setValue(new ArrayList<>());
        _bookTags.setValue(new ArrayList<>());
    }

    public void setSelectedBook(Book book) {
        _bookTags.setValue(new ArrayList<>());
        _selectedBook.setValue(book);

    }

    public MutableLiveData<Book> getSelectedBook() {
        return _selectedBook;
    }

    public LiveData<List<Book>> getBookList() {
        return _bookList;
    }

    public MutableLiveData<List<Tag>> getBookTags() {
        return _bookTags;
    }

    public LiveData<Boolean> isLoading() {
        return _isLoading;
    }
}
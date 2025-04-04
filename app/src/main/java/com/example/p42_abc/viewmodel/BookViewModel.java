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
    // LiveData pour la gestion des états
    private final MutableLiveData<Book> _selectedBook; // Livre sélectionné
    private final MutableLiveData<List<Book>> _bookList; // Liste paginée des livres
    private final MutableLiveData<List<Tag>> _bookTags; // Tags associés au livre
    private final MutableLiveData<Book> _addedBook = new MutableLiveData<>(); // Livre nouvellement ajouté
    private final MutableLiveData<String> _error = new MutableLiveData<>(); // Messages d'erreur
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false); // État de chargement

    // Couche d'accès aux données
    private final BookRepository _bookRepository;

    public BookViewModel() {
        // Initialisation des LiveData
        _selectedBook = new MutableLiveData<>();
        _bookList = new MutableLiveData<>();
        _bookTags = new MutableLiveData<>();

        // Initialisation du repository
        ApiService apiService = ServiceInstantiate.getClient().create(ApiService.class);
        _bookRepository = ServiceInstantiate.provideBookRepository(apiService);
    }

    /**
     * Charge la liste des livres avec pagination
     * @param page Numéro de page
     * @param title Filtre optionnel par titre
     * @return true si le chargement a été initié, false si déjà en cours
     */
    public boolean loadBooks(int page, String title) {
        if (Boolean.TRUE.equals(_isLoading.getValue())) return false;
        _isLoading.setValue(true);

        _bookRepository.getBooks(page, title).observeForever(books -> {
            if (books != null) {
                List<Book> currentList = _bookList.getValue();
                // Concaténation avec la liste existante pour la pagination
                List<Book> updatedList = currentList != null ?
                        new ArrayList<>(currentList) : new ArrayList<>();
                updatedList.addAll(books);
                _bookList.setValue(updatedList);
            }
            _isLoading.setValue(false);
        });
        return true;
    }

    /**
     * Supprime un livre
     * @param book Livre à supprimer
     * @return true si la suppression a réussi
     */
    public Boolean deleteBook(Book book) {
        if (_bookRepository.deleteBook(book.getId())) {
            // Mise à jour de la liste locale
            List<Book> currentList = _bookList.getValue();
            if (currentList != null) {
                currentList.remove(book);
                _bookList.setValue(currentList);
            }
            return true;
        }
        return false;
    }

    /**
     * Charge les tags associés à un livre
     * @param bookId ID du livre
     */
    public void loadTags(int bookId) {
        _bookRepository.getTags(bookId).observeForever(tags -> {
            if (tags != null) {
                // Concaténation avec les tags existants
                List<Tag> currentTags = _bookTags.getValue() != null ?
                        new ArrayList<>(_bookTags.getValue()) : new ArrayList<>();
                currentTags.addAll(tags);
                _bookTags.setValue(currentTags);
            }
        });
    }

    /**
     * Ajoute un nouveau livre
     * @param bookRequest Données du nouveau livre
     * @param authorId ID de l'auteur associé
     * @return LiveData contenant le livre créé
     */
    public LiveData<Book> addBook(BookRequest bookRequest, int authorId) {
        return _bookRepository.addBook(authorId, bookRequest);
    }

    /**
     * Réinitialise les données des livres et tags
     */
    public void clearBooks() {
        _bookList.setValue(new ArrayList<>());
        _bookTags.setValue(new ArrayList<>());
    }

    /**
     * Définit le livre sélectionné et réinitialise ses tags
     * @param book Livre sélectionné
     */
    public void setSelectedBook(Book book) {
        _bookTags.setValue(new ArrayList<>());
        _selectedBook.setValue(book);
    }

    // --- Getters ---
    public LiveData<Book> getAddedBook() {
        return _addedBook;
    }

    public LiveData<String> getError() {
        return _error;
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
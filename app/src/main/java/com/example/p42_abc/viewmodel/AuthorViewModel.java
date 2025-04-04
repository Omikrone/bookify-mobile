package com.example.p42_abc.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p42_abc.model.Author;
import com.example.p42_abc.db.ApiService;
import com.example.p42_abc.model.AuthorRequest;
import com.example.p42_abc.db.ServiceInstantiate;

import java.util.ArrayList;
import java.util.List;

import com.example.p42_abc.db.AuthorRepository;
import com.example.p42_abc.model.Book;

public class AuthorViewModel extends ViewModel {
    // LiveData pour la gestion des états
    private final MutableLiveData<Author> _selectedAuthor; // Auteur sélectionné
    private final MutableLiveData<List<Author>> _authorList; // Liste des auteurs
    private final MutableLiveData<List<Book>> _authorBooks; // Livres de l'auteur
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false); // État de chargement

    // Couche d'accès aux données
    private final AuthorRepository _authorRepository;

    public AuthorViewModel() {
        // Initialisation des LiveData
        _selectedAuthor = new MutableLiveData<>();
        _authorList = new MutableLiveData<>();
        _authorBooks = new MutableLiveData<>();

        // Initialisation du repository
        ApiService apiService = ServiceInstantiate.getClient().create(ApiService.class);
        _authorRepository = ServiceInstantiate.provideAuthorRepository(apiService);
    }

    /**
     * Charge la liste des auteurs avec pagination
     * @param page Numéro de page
     * @param firstname Filtre optionnel sur le prénom
     * @param lastname Filtre optionnel sur le nom
     * @return true si le chargement a été initié
     */
    public boolean loadAuthors(int page, String firstname, String lastname) {
        if (Boolean.TRUE.equals(_isLoading.getValue())) return false;
        _isLoading.setValue(true);

        _authorRepository.getAuthors(page, firstname, lastname).observeForever(authors -> {
            if (authors != null) {
                List<Author> currentList = _authorList.getValue();
                // Concaténation avec la liste existante pour la pagination
                List<Author> updatedList = currentList != null ?
                        new ArrayList<>(currentList) : new ArrayList<>();
                updatedList.addAll(authors);
                _authorList.setValue(updatedList);
            }
            _isLoading.setValue(false);
        });
        return true;
    }

    /**
     * Charge les livres de l'auteur sélectionné
     */
    public void loadAuthorBooks() {
        if (_selectedAuthor.getValue() == null) return;

        _authorRepository.getBooksByAuthor(_selectedAuthor.getValue().getId())
                .observeForever(books -> {
                    if (books != null) {
                        _authorBooks.setValue(books); // Remplace complètement la liste
                    }
                });
    }

    /**
     * Ajoute un nouvel auteur
     * @param authorRequest Données du nouvel auteur
     * @return LiveData contenant l'auteur créé
     */
    public MutableLiveData<Author> addAuthor(AuthorRequest authorRequest) {
        return _authorRepository.addAuthor(authorRequest);
    }

    /**
     * Supprime un auteur
     * @param author Auteur à supprimer
     * @return true si la suppression a réussi
     */
    public Boolean deleteAuthor(Author author) {
        if (_authorRepository.deleteAuthor(author.getId())) {
            // Mise à jour de la liste locale
            List<Author> currentList = _authorList.getValue();
            if (currentList != null) {
                currentList.remove(author);
                _authorList.setValue(currentList);
            }
            return true;
        }
        return false;
    }

    // Méthodes utilitaires de nettoyage
    public void clearBooks() {
        _authorBooks.setValue(new ArrayList<>());
    }

    public void clearAuthors() {
        _authorList.setValue(new ArrayList<>());
    }

    // Getters et Setters
    public void setSelectedAuthor(Author author) {
        _selectedAuthor.setValue(author);
    }

    public MutableLiveData<Author> getSelectedAuthor() {
        return _selectedAuthor;
    }

    public LiveData<List<Author>> getAuthorList() {
        return _authorList;
    }

    public LiveData<List<Book>> getAuthorBooks() {
        return _authorBooks;
    }

    public LiveData<Boolean> isLoading() {
        return _isLoading;
    }
}
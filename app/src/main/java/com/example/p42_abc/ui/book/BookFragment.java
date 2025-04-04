package com.example.p42_abc.ui.book;

import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.R;
import com.example.p42_abc.databinding.FragmentBookBinding;
import com.example.p42_abc.model.Author;
import com.example.p42_abc.model.Book;
import com.example.p42_abc.viewmodel.BookViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BookFragment extends Fragment {
    // Binding et ViewModel
    private FragmentBookBinding _binding;
    private BookViewModel _bookViewModel;

    // Adapter et gestion de pagination
    private BookAdapter _bookAdapter;
    private int _currentPage = 1;
    boolean isLoading = false;

    // Gestion du scroll infini
    private RecyclerView.OnScrollListener _currentScrollListener;
    private RecyclerView _recyclerView;

    // Gestion de la recherche avec debounce
    private final Handler searchHandler = new Handler();
    private static final int SEARCH_DELAY = 100; // ms

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Initialisation du ViewModel
        _bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
        _binding = FragmentBookBinding.inflate(inflater, container, false);

        // Configuration de la RecyclerView
        _recyclerView = _binding.recyclerViewDashboard;
        setupSearch();

        // Initialisation de l'adapter avec gestion du click
        _bookAdapter = new BookAdapter(new ArrayList<>(), book -> {
            _bookViewModel.setSelectedBook(book);
            navigateToBookDetails();
        });

        _recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _recyclerView.setAdapter(_bookAdapter);

        // Observation des données
        _bookViewModel.getBookList().observe(getViewLifecycleOwner(), books -> {
            _bookAdapter.updateList(books);
        });

        _bookViewModel.isLoading().observe(getViewLifecycleOwner(), loading -> {
            isLoading = loading;
        });

        // Chargement initial
        _bookViewModel.loadBooks(_currentPage++, null);
        setScrollListener(_recyclerView, null);

        return _binding.getRoot();
    }

    /**
     * Configure le scroll listener pour la pagination
     * @param title Filtre optionnel par titre
     */
    private void setScrollListener(RecyclerView recyclerView, String title) {
        if (_currentScrollListener != null) {
            recyclerView.removeOnScrollListener(_currentScrollListener);
        }

        _currentScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                // Chargement lorsque l'utilisatteur arrive près de la fin
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && lastVisibleItem >= totalItemCount - 2) {
                    boolean result = _bookViewModel.loadBooks(_currentPage, title);
                    if (result) {
                        _currentPage++;
                    }
                }
            }
        };
        recyclerView.addOnScrollListener(_currentScrollListener);
    }

    /**
     * Configure la recherche avec debounce
     */
    private void setupSearch() {
        _binding.searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchHandler.removeCallbacksAndMessages(null); // Annule la recherche précédente

                searchHandler.postDelayed(() -> {
                    if (getActivity() != null && _binding != null) {
                        _bookViewModel.clearBooks();
                        _currentPage = 1;

                        if (s.length() == 0) {
                            // Recherche vide = charger tous les livres
                            setScrollListener(_recyclerView, null);
                            _bookViewModel.loadBooks(_currentPage++, null);
                        } else {
                            // Recherche par titre
                            setScrollListener(_recyclerView, s.toString());
                            searchBooksByTitle(s.toString());
                        }
                    }
                }, SEARCH_DELAY); // Délai anti-rebond
            }
        });
    }

    /**
     * Effectue une recherche de livres par titre
     */
    private void searchBooksByTitle(String query) {
        _bookViewModel.loadBooks(1, query);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Réinitialisation à chaque retour sur le fragment
        _currentPage = 1;
        _bookViewModel.clearBooks();
        _bookViewModel.loadBooks(_currentPage++, null);

        // Configuration du FAB
        setupFloatingActionButton();
    }

    /**
     * Configure le bouton flottant pour l'ajout de livre
     */
    private void setupFloatingActionButton() {
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setOnClickListener(v -> navigateToAddBook());
        fab.setVisibility(VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _binding = null; // Nettoyage du binding
    }

    /**
     * Navigation vers les détails d'un livre
     */
    private void navigateToBookDetails() {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_navigation_dashboard_to_bookDetailsFragment);
    }

    /**
     * Navigation vers l'ajout de livre
     */
    private void navigateToAddBook() {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_navigation_dashboard_to_addBookFragment);
    }

    /**
     * Gestion de la suppression d'un livre
     */
    public void onButtonClick(Book book) {
        if (_bookViewModel.deleteBook(book)) {
            showToast("Livre supprimé!");
        } else {
            showToast("Erreur lors de la suppression");
        }
    }

    /**
     * Affiche un message Toast
     */
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}

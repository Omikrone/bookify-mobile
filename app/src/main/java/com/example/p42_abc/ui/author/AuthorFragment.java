package com.example.p42_abc.ui.author;

import static android.view.View.VISIBLE;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;
import static com.google.android.material.internal.ViewUtils.showKeyboard;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.R;
import com.example.p42_abc.databinding.FragmentAuthorBinding;
import com.example.p42_abc.model.Author;
import com.example.p42_abc.viewmodel.AuthorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AuthorFragment extends Fragment implements AuthorAdapter.ItemClickListener {
    private FragmentAuthorBinding _binding;
    private AuthorViewModel _authorViewModel;
    private AuthorAdapter _authorAdapter;
    private int _currentPage = 1;
    boolean isLoading = false;
    private RecyclerView.OnScrollListener _currentScrollListener;
    private RecyclerView _recyclerView;

    // Handler pour le debounce de recherche
    private final Handler searchHandler = new Handler();
    private static final int SEARCH_DELAY = 100; // Délai en ms pour la recherche

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _authorViewModel = new ViewModelProvider(requireActivity()).get(AuthorViewModel.class);
        _binding = FragmentAuthorBinding.inflate(inflater, container, false);

        // Configuration initiale de la RecyclerView
        _recyclerView = _binding.recyclerViewAuthor;
        _recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        setupSearch(); // Initialisation du système de recherche

        // Initialisation de l'adapter avec les click listeners
        _authorAdapter = new AuthorAdapter(new ArrayList<>(), this, this);
        _recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _recyclerView.setAdapter(_authorAdapter);

        // Observation de la liste des auteurs
        _authorViewModel.getAuthorList().observe(getViewLifecycleOwner(), authors -> {
            _authorAdapter.updateList(authors);
        });

        // Observation de l'état de chargement
        _authorViewModel.isLoading().observe(getViewLifecycleOwner(), loading -> {
            isLoading = loading;
        });

        // Chargement initial des auteurs
        _authorViewModel.loadAuthors(_currentPage++, null, null);
        setScrollListener(_recyclerView, null, null);

        return _binding.getRoot();
    }

    /**
     * Configure le scroll listener pour le chargement paginé
     * @param firstname Filtre optionnel sur le prénom
     * @param lastname Filtre optionnel sur le nom
     */
    public void setScrollListener(RecyclerView recyclerView, String firstname, String lastname) {
        if (_currentScrollListener != null) {
            recyclerView.removeOnScrollListener(_currentScrollListener);
        }

        _currentScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                // Déclenche le chargement quand on atteint l'avant-dernier élément
                if (!isLoading && lastVisibleItem >= totalItemCount - 2) {
                    boolean result = _authorViewModel.loadAuthors(_currentPage, firstname, lastname);
                    if (result) {
                        _currentPage++;
                    }
                }
            }
        };
        recyclerView.addOnScrollListener(_currentScrollListener);
    }

    /**
     * Configure le système de recherche avec debounce
     */
    private void setupSearch() {
        _binding.searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchHandler.removeCallbacksAndMessages(null); // Annule les recherches précédentes

                searchHandler.postDelayed(() -> {
                    if (getActivity() != null && _binding != null) {
                        _authorViewModel.clearAuthors();
                        if (s.length() == 0) {
                            // Réinitialisation si la recherche est vide
                            _currentPage = 1;
                            setScrollListener(_recyclerView, null, null);
                            _authorViewModel.loadAuthors(_currentPage++, null, null);
                        } else {
                            // Recherche par nom de famille
                            setScrollListener(_recyclerView, s.toString(), null);
                            searchAuthorsByLastname(s.toString());
                        }
                    }
                }, SEARCH_DELAY); // Délai pour éviter les recherches à chaque frappe
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * Effectue une recherche d'auteurs par nom
     */
    private void searchAuthorsByLastname(String query) {
        _authorViewModel.loadAuthors(1, null, query);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Réinitialisation à chaque retour sur le fragment
        _currentPage = 1;
        _authorViewModel.clearAuthors();
        _authorViewModel.loadAuthors(_currentPage++, null, null);

        // Configuration du FAB
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Navigation.findNavController(requireActivity(),
                            R.id.nav_host_fragment_activity_main)
                    .navigate(R.id.action_navigation_home_to_addAuthorFragment);
        });
        fab.setVisibility(VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _binding = null; // Nettoyage du binding
    }

    // Gestion des clicks sur un auteur
    @Override
    public void onAuthorClick(Author author) {
        _authorViewModel.clearBooks();
        _authorViewModel.setSelectedAuthor(author);
        Navigation.findNavController(requireActivity(),
                        R.id.nav_host_fragment_activity_main)
                .navigate(R.id.action_navigation_home_to_authorFragment);
    }

    // Gestion de la suppression d'un auteur
    @Override
    public void onButtonClick(Author author) {
        if (_authorViewModel.deleteAuthor(author)) {
            Toast.makeText(getContext(), "Auteur supprimé!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
        }
    }
}
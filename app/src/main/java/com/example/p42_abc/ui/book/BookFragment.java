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

    private FragmentBookBinding _binding;
    private BookViewModel _bookViewModel;
    private BookAdapter _bookAdapter;
    private int _currentPage = 1;
    boolean isLoading = false;

    private RecyclerView.OnScrollListener _currentScrollListener;
    private RecyclerView _recyclerView;
    private final Handler searchHandler = new Handler();
    private static final int SEARCH_DELAY = 100;

    public BookFragment() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        _binding = FragmentBookBinding.inflate(inflater, container, false);
        View root = _binding.getRoot();

        _recyclerView = _binding.recyclerViewDashboard;

        setupSearch();

        _bookAdapter = new BookAdapter(new ArrayList<>(), book -> {
            _bookViewModel.setSelectedBook(book);

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_navigation_dashboard_to_bookDetailsFragment);
        });

        _recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _recyclerView.setAdapter(_bookAdapter);

        _bookViewModel.getBookList().observe(getViewLifecycleOwner(), books -> {
            _bookAdapter.updateList(books);
        });

        _bookViewModel.isLoading().observe(getViewLifecycleOwner(), loading -> {
            isLoading = loading;
        });

        _bookViewModel.loadBooks(_currentPage++, null);
        Log.d("DashboardFragment", "Page " + _currentPage + " loaded");
        setScrollListener(_recyclerView, null);

        return root;
    }

    private void setScrollListener(RecyclerView recyclerView, String title) {
        if (_currentScrollListener != null) {
            _recyclerView.removeOnScrollListener(_currentScrollListener);
        }
        _currentScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && lastVisibleItem >= totalItemCount - 2) {
                    Log.d("BookFragment", "Loading more books...");
                    boolean result = _bookViewModel.loadBooks(_currentPage, title);
                    Log.d("BookFragment", "Title: " + title);
                    if (result) {
                        _currentPage++;
                        Log.d("DashboardFragment", "Page " + _currentPage + " loaded");
                    }
                }
            }
        };
        recyclerView.addOnScrollListener(_currentScrollListener);
    }

    private void setupSearch() {
        _binding.searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchHandler.removeCallbacksAndMessages(null);

                searchHandler.postDelayed(() -> {
                    if (getActivity() != null && _binding != null) {
                        _bookViewModel.clearBooks();
                        _currentPage = 1;
                        if (s.length() == 0) {
                            setScrollListener(_recyclerView, null);
                            _bookViewModel.loadBooks(_currentPage++, null);
                        } else {
                            setScrollListener(_recyclerView, s.toString());
                            searchBooksByTitle(s.toString());
                        }
                    }
                }, SEARCH_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void searchBooksByTitle(String query) {
        _bookViewModel.loadBooks(1, query);
        _bookViewModel.getBookList().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                _bookAdapter.updateList(books);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        _currentPage = 1;
        _bookViewModel.clearBooks();
        _bookViewModel.loadBooks(_currentPage++, null);

        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_navigation_dashboard_to_addBookFragment);
        });
        fab.setVisibility(VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _binding = null;
    }

    public void onButtonClick(Book book) {
        if (_bookViewModel.deleteBook(book)) {
            Toast.makeText(getContext(), "Livre supprimé!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Erreur lors de la suppression du livre", Toast.LENGTH_SHORT).show();
        }
    }
}

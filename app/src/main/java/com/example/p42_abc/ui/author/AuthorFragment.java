package com.example.p42_abc.ui.author;

import static android.view.View.VISIBLE;

import android.os.Bundle;
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
import com.example.p42_abc.databinding.FragmentHomeBinding;
import com.example.p42_abc.model.Author;
import com.example.p42_abc.viewmodel.AuthorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AuthorFragment extends Fragment implements AuthorAdapter.ItemClickListener {

    private FragmentHomeBinding _binding;
    private AuthorViewModel _homeViewModel;
    private AuthorAdapter _authorAdapter;
    private int _currentPage = 1;
    boolean isLoading = false;

    public AuthorFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _homeViewModel = new ViewModelProvider(requireActivity()).get(AuthorViewModel.class);

        _binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = _binding.getRoot();

        RecyclerView recyclerView = _binding.recyclerViewHome;
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        _authorAdapter = new AuthorAdapter(new ArrayList<>(), this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(_authorAdapter);

        _homeViewModel.getAuthorList().observe(getViewLifecycleOwner(), authors -> {
            _authorAdapter.updateList(authors);
        });

        _homeViewModel.isLoading().observe(getViewLifecycleOwner(), loading -> {
            isLoading = loading;
        });


        _homeViewModel.loadAuthors(_currentPage++);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && lastVisibleItem >= totalItemCount - 2) {
                    Log.d("HomeFragment", "Loading more authors...");
                    boolean result = _homeViewModel.loadAuthors(_currentPage);
                    if (result) {
                        _currentPage++;
                        Log.d("HomeFragment", "Page " + _currentPage + " loaded");
                    }
                }
            }
        });


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_navigation_home_to_addAuthorFragment);
        });
        fab.setVisibility(VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _binding = null;
    }

    @Override
    public void onAuthorClick(Author author) {
        _homeViewModel.setSelectedAuthor(author);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_navigation_home_to_authorFragment);
    }

    @Override
    public void onButtonClick(Author author) {
        if (_homeViewModel.deleteAuthor(author)) {
            Toast.makeText(getContext(), "Auteur supprimé!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Erreur lors de la suppression de l'auteur", Toast.LENGTH_SHORT).show();
        }
    }
}

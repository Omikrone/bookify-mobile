package com.example.p42_abc.ui.home;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.R;
import com.example.p42_abc.adapter.AuthorAdapter;
import com.example.p42_abc.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding _binding;
    NavController _navController;
    private HomeViewModel _homeViewModel;
    private AuthorAdapter _authorAdapter;
    private int _currentPage = 1;
    boolean isLoading = false;

    public HomeFragment() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        _binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = _binding.getRoot();

        //_navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);

        RecyclerView recyclerView = _binding.recyclerView;

        _authorAdapter = new AuthorAdapter(new ArrayList<>(), author -> {
            _homeViewModel.setSelectedAuthor(author);

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_navigation_home_to_authorFragment);
        });

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
}

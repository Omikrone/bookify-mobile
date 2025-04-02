package com.example.p42_abc.ui.book;

import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.R;
import com.example.p42_abc.databinding.FragmentDashboardBinding;
import com.example.p42_abc.viewmodel.BookViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BookFragment extends Fragment {

    private FragmentDashboardBinding _binding;
    private NavController _navController;
    private BookViewModel _dashboardViewModel;
    private BookAdapter _bookAdapter;
    private int _currentPage = 1;
    boolean isLoading = false;


    public BookFragment() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _dashboardViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        _binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = _binding.getRoot();

        RecyclerView recyclerView = _binding.recyclerViewDashboard;

        _bookAdapter = new BookAdapter(new ArrayList<>(), book -> {
            _dashboardViewModel.setSelectedBook(book);

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_navigation_dashboard_to_bookDetailsFragment);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(_bookAdapter);

        _dashboardViewModel.getBookList().observe(getViewLifecycleOwner(), books -> {
            _bookAdapter.updateList(books);
        });

        _dashboardViewModel.isLoading().observe(getViewLifecycleOwner(), loading -> {
            isLoading = loading;
        });

        _dashboardViewModel.loadBooks(_currentPage++);
        Log.d("DashboardFragment", "Page " + _currentPage + " loaded");

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && lastVisibleItem >= totalItemCount - 2) {
                    Log.d("DashboardFragment", "Loading more books...");
                    boolean result = _dashboardViewModel.loadBooks(_currentPage);
                    if (result) {
                        _currentPage++;
                        Log.d("DashboardFragment", "Page " + _currentPage + " loaded");
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
            navController.navigate(R.id.action_navigation_dashboard_to_addBookFragment);
        });
        fab.setVisibility(VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _binding = null;
    }
}

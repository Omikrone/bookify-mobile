package com.example.p42_abc.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.adapter.BookAdapter;
import com.example.p42_abc.databinding.FragmentDashboardBinding;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding _binding;
    private BookAdapter _bookAdapter;
    private DashboardViewModel _dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        _binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = _binding.getRoot();

        RecyclerView recyclerView = _binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        _bookAdapter = new BookAdapter(new ArrayList<>());
        recyclerView.setAdapter(_bookAdapter);

        _dashboardViewModel.getBookList().observe(getViewLifecycleOwner(), books -> _bookAdapter.updateList(books));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _binding = null;
    }
}
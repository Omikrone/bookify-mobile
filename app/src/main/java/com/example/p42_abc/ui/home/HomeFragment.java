package com.example.p42_abc.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.adapter.AuthorAdapter;
import com.example.p42_abc.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding _binding;
    private HomeViewModel _homeViewModel;
    private AuthorAdapter _authorAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        _binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = _binding.getRoot();

        RecyclerView recyclerView = _binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        _authorAdapter = new AuthorAdapter(new ArrayList<>());
        recyclerView.setAdapter(_authorAdapter);

        _homeViewModel.getAuthorList().observe(getViewLifecycleOwner(), authors -> _authorAdapter.updateList(authors));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _binding = null;
    }
}
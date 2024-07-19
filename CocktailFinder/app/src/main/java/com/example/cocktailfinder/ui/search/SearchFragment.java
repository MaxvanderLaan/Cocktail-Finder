package com.example.cocktailfinder.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocktailfinder.R;
import com.example.cocktailfinder.databinding.FragmentSearchBinding;

import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel searchViewModel;
    private CocktailAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter
        adapter = new CocktailAdapter(getContext(), null);
        recyclerView.setAdapter(adapter);

        // Observe the LiveData from the ViewModel
        searchViewModel.getCocktailNames().observe(getViewLifecycleOwner(), names -> {
            // Update the adapter's data
            adapter.updateData(names);
        });

        // Call loadCocktailNames from the ViewModel
        searchViewModel.loadCocktailNames(requireContext());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

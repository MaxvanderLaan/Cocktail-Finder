package com.maxvanderlaan.cocktailfinder.ui.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.maxvanderlaan.cocktailfinder.databinding.FragmentSavedBinding;

public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;
    private SavedViewModel savedViewModel;
    private SavedCocktailAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        savedViewModel = new ViewModelProvider(this).get(SavedViewModel.class);

        binding = FragmentSavedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new SavedCocktailAdapter(getContext(), null);
        recyclerView.setAdapter(adapter);

        savedViewModel.getCocktailList().observe(getViewLifecycleOwner(), cocktails -> {
            adapter.updateData(cocktails);
        });

        savedViewModel.loadSavedCocktails(requireContext());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

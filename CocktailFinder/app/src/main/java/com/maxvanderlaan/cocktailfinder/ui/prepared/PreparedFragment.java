package com.maxvanderlaan.cocktailfinder.ui.prepared;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maxvanderlaan.cocktailfinder.R;

public class PreparedFragment extends Fragment {

    private PreparedViewModel preparedViewModel;
    private RecyclerView recyclerView;
    private PreparedAdapter preparedAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preparedViewModel = new ViewModelProvider(this).get(PreparedViewModel.class);
        preparedAdapter = new PreparedAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_prepared, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_prepared);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(preparedAdapter);

        preparedViewModel.getPreparedList().observe(getViewLifecycleOwner(), preparedList -> {
            preparedAdapter.updatePreparedList(preparedList);
        });

        return root;
    }
}

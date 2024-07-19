package com.example.cocktailfinder.ui.search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocktailfinder.R;
import com.example.cocktailfinder.ui.CocktailDetail;

import java.util.List;

public class CocktailAdapter extends RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder> {

    private List<String> cocktailNames;
    private Context context;

    public CocktailAdapter(Context context, List<String> cocktailNames) {
        this.context = context;
        this.cocktailNames = cocktailNames;
    }

    @NonNull
    @Override
    public CocktailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new CocktailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CocktailViewHolder holder, int position) {
        String cocktailName = cocktailNames.get(position);
        holder.cocktailTextView.setText(cocktailName);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CocktailDetail.class);
            intent.putExtra("cocktail_name", cocktailName);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cocktailNames != null ? cocktailNames.size() : 0;
    }

    public void updateData(List<String> newCocktailNames) {
        this.cocktailNames = newCocktailNames;
        notifyDataSetChanged();
    }

    public static class CocktailViewHolder extends RecyclerView.ViewHolder {
        TextView cocktailTextView;

        public CocktailViewHolder(@NonNull View itemView) {
            super(itemView);
            cocktailTextView = itemView.findViewById(R.id.cocktail);
        }
    }
}

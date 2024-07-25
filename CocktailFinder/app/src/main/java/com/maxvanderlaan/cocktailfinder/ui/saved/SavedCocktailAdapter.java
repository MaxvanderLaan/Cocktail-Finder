package com.maxvanderlaan.cocktailfinder.ui.saved;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maxvanderlaan.cocktailfinder.R;
import com.maxvanderlaan.cocktailfinder.model.Cocktail;
import com.maxvanderlaan.cocktailfinder.ui.cocktail.CocktailDetail;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class SavedCocktailAdapter extends RecyclerView.Adapter<SavedCocktailAdapter.CocktailViewHolder> {

    private List<Cocktail> cocktailList;
    private Context context;

    public SavedCocktailAdapter(Context context, List<Cocktail> cocktailList) {
        this.context = context;
        this.cocktailList = cocktailList;
    }

    @NonNull
    @Override
    public CocktailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_saved, parent, false);
        return new CocktailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CocktailViewHolder holder, int position) {
        Cocktail cocktail = cocktailList.get(position);
        holder.cocktailName.setText(cocktail.getStrDrink());
        holder.cocktailCategory.setText(cocktail.getStrCategory());

        new LoadImageTask(holder.cocktailImage).execute(cocktail.getStrDrinkThumb());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CocktailDetail.class);
            intent.putExtra("cocktail", cocktail);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cocktailList != null ? cocktailList.size() : 0;
    }

    public void updateData(List<Cocktail> newCocktailList) {
        this.cocktailList = newCocktailList;
        notifyDataSetChanged();
    }

    public static class CocktailViewHolder extends RecyclerView.ViewHolder {
        TextView cocktailName;
        TextView cocktailCategory;
        ImageView cocktailImage;

        public CocktailViewHolder(@NonNull View itemView) {
            super(itemView);
            cocktailName = itemView.findViewById(R.id.cocktail_name);
            cocktailCategory = itemView.findViewById(R.id.cocktail_category);
            cocktailImage = itemView.findViewById(R.id.cocktail_image);
        }
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new URL(urlDisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }
}


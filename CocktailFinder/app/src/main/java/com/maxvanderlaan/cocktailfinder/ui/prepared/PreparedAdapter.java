package com.maxvanderlaan.cocktailfinder.ui.prepared;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maxvanderlaan.cocktailfinder.R;
import com.maxvanderlaan.cocktailfinder.model.Prepared;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PreparedAdapter extends RecyclerView.Adapter<PreparedAdapter.PreparedViewHolder> {

    private List<Prepared> preparedList;

    public PreparedAdapter() {
        this.preparedList = new ArrayList<>(); // Ensure the list is initialized
    }

    public void updatePreparedList(List<Prepared> newPreparedList) {
        if (newPreparedList != null) {
            preparedList.clear();
            preparedList.addAll(newPreparedList);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public PreparedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prepared, parent, false);
        return new PreparedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreparedViewHolder holder, int position) {
        Prepared prepared = preparedList.get(position);
        holder.nameTextView.setText(prepared.getCocktailName());

        // Format the rating with "/10.0"
        String formattedRating = String.format("%.1f/10.0", prepared.getRating());
        holder.ratingTextView.setText(formattedRating);

        // Load and rotate the image, then set it to the ImageView
        try {
            String imagePath = prepared.getImagePath();
            if (imagePath != null) {
                // Ensure the image path is correct and doesn't contain extra characters
                Uri imageUri = Uri.parse(imagePath.startsWith("file://") ? imagePath : "file://" + imagePath);

                Log.d("PreparedAdapter", "Loading image from URI: " + imageUri);

                Bitmap bitmap = loadAndRotateImage(holder.itemView.getContext().getContentResolver(), imageUri);
                if (bitmap != null) {
                    holder.imageView.setImageBitmap(bitmap);
                } else {
                    Log.e("PreparedAdapter", "Bitmap is null for URI: " + imageUri);
                    holder.imageView.setImageResource(R.drawable.placeholder); // Fallback placeholder
                }
            } else {
                Log.e("PreparedAdapter", "Image path is null for position: " + position);
                holder.imageView.setImageResource(R.drawable.placeholder); // Fallback placeholder
            }
        } catch (Exception e) {
            e.printStackTrace();
            holder.imageView.setImageResource(R.drawable.placeholder); // Fallback placeholder
        }
    }

    @Override
    public int getItemCount() {
        return preparedList.size();
    }

    static class PreparedViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView ratingTextView;
        ImageView imageView;

        public PreparedViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.cocktail_name);
            ratingTextView = itemView.findViewById(R.id.cocktail_rating);
            imageView = itemView.findViewById(R.id.cocktail_image);
        }
    }

    private Bitmap loadAndRotateImage(ContentResolver contentResolver, Uri imageUri) {
        try (InputStream imageStream = contentResolver.openInputStream(imageUri)) {
            if (imageStream == null) {
                Log.e("PreparedAdapter", "ImageStream is null for URI: " + imageUri);
                return null;
            }
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            if (bitmap != null) {
                return rotateImage(bitmap, 90); // Rotate image by 90 degrees
            } else {
                Log.e("PreparedAdapter", "Bitmap decoding failed for URI: " + imageUri);
            }
        } catch (Exception e) {
            Log.e("PreparedAdapter", "Error loading image from URI: " + imageUri, e);
        }
        return null; // Return null if there's an error or the image cannot be loaded
    }


    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}

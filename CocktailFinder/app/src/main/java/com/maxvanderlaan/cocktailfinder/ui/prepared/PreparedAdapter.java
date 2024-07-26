package com.maxvanderlaan.cocktailfinder.ui.prepared;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.collection.LruCache;

import com.maxvanderlaan.cocktailfinder.R;
import com.maxvanderlaan.cocktailfinder.model.Prepared;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PreparedAdapter extends RecyclerView.Adapter<PreparedAdapter.PreparedViewHolder> {

    private List<Prepared> preparedList;
    private LruCache<String, Bitmap> imageCache;

    public PreparedAdapter() {
        this.preparedList = new ArrayList<>();

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);
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

        String formattedRating = String.format("%.1f/10.0", prepared.getRating());
        holder.ratingTextView.setText(formattedRating);

        String imagePath = prepared.getImagePath();
        if (imagePath != null) {
            Bitmap cachedBitmap = imageCache.get(imagePath);
            if (cachedBitmap != null) {
                holder.imageView.setImageBitmap(cachedBitmap);
            } else {
                new LoadImageTask(holder.imageView, imagePath).execute();
            }
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder);
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

    private class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {
        private ImageView imageView;
        private String imagePath;

        public LoadImageTask(ImageView imageView, String imagePath) {
            this.imageView = imageView;
            this.imagePath = imagePath;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Uri imageUri = Uri.parse(imagePath.startsWith("file://") ? imagePath : "file://" + imagePath);
            return loadAndRotateImage(imageView.getContext().getContentResolver(), imageUri);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageCache.put(imagePath, bitmap); // Cache the loaded bitmap
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.placeholder);
            }
        }
    }

    private Bitmap loadAndRotateImage(ContentResolver contentResolver, Uri imageUri) {
        try (InputStream imageStream = contentResolver.openInputStream(imageUri)) {
            if (imageStream == null) {
                return null;
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(imageStream, null, options);

            options.inSampleSize = calculateInSampleSize(options, 100, 100);
            options.inJustDecodeBounds = false;

            imageStream.close();
            try (InputStream inputStream = contentResolver.openInputStream(imageUri)) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                return rotateImage(bitmap, 90); // Rotate image by 90 degrees
            }
        } catch (Exception e) {
            Log.e("PreparedAdapter", "Error loading image from URI: " + imageUri, e);
        }
        return null;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}

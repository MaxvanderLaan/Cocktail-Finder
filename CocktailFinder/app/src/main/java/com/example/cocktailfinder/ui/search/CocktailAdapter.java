    package com.example.cocktailfinder.ui.search;

    import android.content.Context;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.graphics.Canvas;
    import android.graphics.Paint;
    import android.graphics.Shader;
    import android.graphics.BitmapShader;
    import android.os.AsyncTask;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.cocktailfinder.R;
    import com.example.cocktailfinder.model.Cocktail;
    import com.example.cocktailfinder.ui.CocktailDetail;

    import java.io.InputStream;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.util.List;

    public class CocktailAdapter extends RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder> {

        private List<Cocktail> cocktailList;
        private Context context;

        public CocktailAdapter(Context context, List<Cocktail> cocktailList) {
            this.context = context;
            this.cocktailList = cocktailList;
        }

        @NonNull
        @Override
        public CocktailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
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
                    imageView.setImageBitmap(getCircularBitmap(result));
                }
            }
        }

        private Bitmap getCircularBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int size = Math.min(width, height);
            Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

            float radius = size / 2f;
            canvas.drawCircle(radius, radius, radius, paint);

            return output;
        }
    }

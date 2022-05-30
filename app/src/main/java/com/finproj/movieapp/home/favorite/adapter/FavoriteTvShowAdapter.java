package com.finproj.movieapp.home.favorite.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.finproj.movieapp.R;
import com.finproj.movieapp.helper.DataHolder;
import com.finproj.movieapp.localstorage.entity.FavoriteTvShow;
import com.finproj.movieapp.model.TvItem;
import com.finproj.movieapp.network.TmdbApiClient;
import com.finproj.movieapp.showsdetails.TvDetailsActivity;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class FavoriteTvShowAdapter extends RecyclerView.Adapter<FavoriteTvShowAdapter.ViewHolder> {

    private final Context context;
    private List<FavoriteTvShow> favoriteTvShowList;

    public FavoriteTvShowAdapter(Context context) {
        this.context = context;
    }

    public void setFavoriteTvShowList(List<FavoriteTvShow> favoriteTvShowList) {
        this.favoriteTvShowList = favoriteTvShowList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fav, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // read tv show id from database and pass it to DataHolder for api
        String tvshow_id = this.favoriteTvShowList.get(position).favTvShowId;
        DataHolder.getInstance().setData(tvshow_id);

        // get data from tmdb api
        TmdbApiClient api = new TmdbApiClient();
        api.apiInterface().getTvDetails().enqueue(new Callback<TvItem>() {

            @Override
            public void onResponse(@NonNull Call<TvItem> call, @NonNull Response<TvItem> response) {
                TvItem itemTvShow = response.body();
                if (itemTvShow != null) {
                    Picasso.with(context)
                            .load("https://image.tmdb.org/t/p/w500" + itemTvShow.tv_image)
                            .into(holder.image);

                    holder.title.setText(itemTvShow.tv_og_title);
                }
                else {
                    Toast toast = Toast.makeText(context, "Failed to retrieve from server.\nSwipe down to refresh.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvItem> call, @NonNull Throwable t) {
                Toast toast = Toast.makeText(context, "Failed to retrieve from server.\nSwipe down to refresh.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        // open tv show details for each tv show item
        holder.itemFav.setOnClickListener(v -> {
            Intent intent = new Intent(context, TvDetailsActivity.class);
            DataHolder.getInstance().setData(tvshow_id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return this.favoriteTvShowList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        RelativeLayout itemFav;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            itemFav = itemView.findViewById(R.id.item_fav);
        }
    }
}

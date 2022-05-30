package com.finproj.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.finproj.movieapp.helper.DataHolder;
import com.finproj.movieapp.R;
import com.finproj.movieapp.databinding.ItemTvBinding;
import com.finproj.movieapp.model.TVs;
import com.finproj.movieapp.showsdetails.TvDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TvAdapter extends RecyclerView.Adapter<TvAdapter.ViewHolder> {
    private final List<TVs.Result> tvs;
    private final Context context;

    public TvAdapter(List<TVs.Result> tvs, Context context) {
        this.tvs = tvs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        com.finproj.movieapp.databinding.ItemTvBinding binding = ItemTvBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(tvs.get(position));
    }

    @Override
    public int getItemCount() {
        return tvs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void onBind(TVs.Result result) {
            TextView tvTitle = itemView.findViewById(R.id.item_tv_title);
            ImageView tvImage = itemView.findViewById(R.id.item_tv_image);
            RelativeLayout itemTv = itemView.findViewById(R.id.item_tv);

            itemTv.setOnClickListener(view -> {
                Intent intent = new Intent(context, TvDetailsActivity.class);
                DataHolder.getInstance().setData(result.tv_id);
                context.startActivity(intent);
            });

            Picasso.with(context)
                    .load("https://image.tmdb.org/t/p/w500" + result.tv_image)
                    .into(tvImage);

            tvTitle.setText(result.tv_og_title);
        }
    }
}
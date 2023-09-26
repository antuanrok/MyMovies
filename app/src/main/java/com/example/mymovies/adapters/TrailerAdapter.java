package com.example.mymovies.adapters;


import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.R;
import com.example.mymovies.data.Review;
import com.example.mymovies.data.Trailer;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private ArrayList<Trailer> trailers;

    private OnTrailerClickListener onTrailerClickListener;

    public interface OnTrailerClickListener {
        void onTrailrerClick(String url);
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        holder.textViewName.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public void setOnTrailerClickListener(OnTrailerClickListener onTrailerClickListener) {
        this.onTrailerClickListener = onTrailerClickListener;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewTrailer;
        private TextView textViewName;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            //imageViewTrailer = itemView.findViewById(R.id.imageView)
            textViewName = itemView.findViewById(R.id.textViewName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onTrailerClickListener != null) {
                        String url = trailers.get(getAdapterPosition()).getUrl();
                        onTrailerClickListener.onTrailrerClick(url);
                    }
                }
            });
        }
    }
}

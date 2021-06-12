package com.example.projectgit.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectgit.R;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private ArrayList<String> mDataset;
    private Activity activtiy;
    public static class GalleryViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public GalleryViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }


    public GalleryAdapter(Activity activtiy, ArrayList<String> GalleryDataset) {
        mDataset = GalleryDataset;
        this.activtiy = activtiy;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {

        CardView cardView = ( CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        final GalleryViewHolder galleryViewHolder = new GalleryViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("profilePath", mDataset.get(galleryViewHolder.getAdapterPosition()));
                activtiy.setResult(Activity.RESULT_OK, resultIntent);
                activtiy.finish();
            }
        });
        return galleryViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final GalleryViewHolder holder, int position) {
       CardView cardView = holder.cardView;

        ImageView imageView = cardView.findViewById(R.id.LoginBackground);
        Glide.with(activtiy).load(mDataset.get(position)).centerCrop().override(500).into(imageView);


    }


    @Override
    public int getItemCount() {

        return mDataset.size();
    }
}

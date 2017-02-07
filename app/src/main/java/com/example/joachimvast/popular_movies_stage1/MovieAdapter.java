package com.example.joachimvast.popular_movies_stage1;

import android.app.Application;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by JoachimVAST on 06/02/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ImageView[] mThumbnails;

    @Override
    public int getItemCount() {
        return mThumbnails.length;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView mThumbnail;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            this.mThumbnail = (ImageView) itemView.findViewById(R.id.iv_thumb);
        }
    }

    @Override
    public MovieAdapter.MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.gridrecycleview;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId,parent,shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieAdapterViewHolder holder, int position) {
        ImageView image = mThumbnails[position];
        holder.mThumbnail = image;
    }

    public void setImages(ImageView[] images){
        mThumbnails = images;
        notifyDataSetChanged();
    }
}

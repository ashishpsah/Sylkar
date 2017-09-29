package com.example.ashish.sylkar;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by ashishpsah on 19.09.17.
 */

public class ShowDataViewHolder extends RecyclerView.ViewHolder {
    private final TextView textview_name;
    private final ImageView imageView;
    View mView;

    public ShowDataViewHolder(final View itemView) {
        super(itemView);
        mView = itemView;
        imageView = (ImageView) itemView.findViewById(R.id.fetch_image);
        textview_name = (TextView) itemView.findViewById(R.id.fetch_image_title);
    }

    public void setEtTitle(String etTitle) {
        textview_name.setText(etTitle);
    }

    public void setImageurl(String imageurl) {
        Picasso.with(mView.getContext())
                .load(imageurl)
                .into(imageView);
    }
}
package com.example.ashish.sylkar;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by ashishpsah on 21.09.17.
 */

public class ShowGangMember extends RecyclerView.ViewHolder {
    private final TextView name,job,phone,address,iban;
    private final ImageView imageView;
    View mView;


    public ShowGangMember(final View itemView) {
        super(itemView);
        mView = itemView;
        imageView = (ImageView) itemView.findViewById(R.id.fetch_image);
        name = (TextView) itemView.findViewById(R.id.fetch_image_title);
        job = (TextView) itemView.findViewById(R.id.fetch_job);
        phone = (TextView)itemView.findViewById(R.id.fetch_phone);
        address = (TextView)itemView.findViewById(R.id.fetch_address);
        iban = (TextView)itemView.findViewById(R.id.fetch_iban);
    }

    public void setEtTitle(String etTitle) {
        name.setText(etTitle);
    }
    public void setEtJob(String etJob) {job.setText(etJob);}
    public void setPhone(String etPhone){phone.setText(etPhone);}
    public void setAddress(String etAddress){address.setText(etAddress);}
    public void setIban(String etIBAN){iban.setText(etIBAN);}
    public void setImageurl(String imageurl) {

        Picasso.with(mView.getContext())
                .load(imageurl)
                .into(imageView);

    }


}
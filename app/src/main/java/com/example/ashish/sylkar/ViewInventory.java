package com.example.ashish.sylkar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewInventory.OnFragmentInteractionListener} interface
 * to handle interaction events.

 * create an instance of this fragment.
 */
public class ViewInventory extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference myref;

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle b)
    {
        View view=inflater.inflate(R.layout.main,group,false);
        getActivity().setTitle("Update Inventory");
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        myref= FirebaseDatabase.getInstance().getReference().child("Inventory");
        FirebaseRecyclerAdapter<Inventory,ShowDataViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<Inventory,ShowDataViewHolder>(
                Inventory.class,
                R.layout.show_data_single_item,
                ShowDataViewHolder.class,
                myref
        ) {
            @Override
            protected void populateViewHolder(ShowDataViewHolder viewHolder, Inventory model, int position) {
                viewHolder.setEtTitle(model.getEtTitle());
                viewHolder.setImageurl(model.getImageurl());

            }


        };
        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }
}

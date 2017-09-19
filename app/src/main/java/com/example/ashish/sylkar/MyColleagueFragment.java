package com.example.ashish.sylkar;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyColleagueFragment extends Fragment {


    private RecyclerView recyclerView;
    private DatabaseReference myref;
    static String UserTag;
    FirebaseRecyclerAdapter<Users, ShowDataViewHolder> recyclerAdapter;



    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle b) {
        View view = inflater.inflate(R.layout.main, group, false);
        getActivity().setTitle("My Colleagues");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        myref = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerAdapter = new FirebaseRecyclerAdapter<Users, ShowDataViewHolder>(
                Users.class,
                R.layout.show_data_single_item,
                ShowDataViewHolder.class,
                myref
        ) {
            @Override
            protected void populateViewHolder(ShowDataViewHolder viewHolder, Users model, final int position) {
                viewHolder.setEtTitle(model.getEtTitle());
                viewHolder.setImageurl(model.getImageurl());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Do you want to send Notification?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int selectedItems = position;

                                        UserTag = recyclerAdapter.getRef(selectedItems).getKey();


                                        Toast.makeText(getContext(), UserTag, Toast.LENGTH_LONG).show();


                                        recyclerAdapter.notifyItemRemoved(selectedItems);
                                        startActivity(new Intent(getContext(), SendNotification.class));
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.setTitle("Confirm");
                        dialog.show();
                    }
                });
            }
        };
        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }
}






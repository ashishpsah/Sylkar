package com.example.ashish.sylkar;


import android.content.DialogInterface;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyColleagueFragment extends Fragment {

    private FirebaseRecyclerAdapter<ShowDataItems, MyColleagueFragment.ShowDataViewHolder> mFirebaseAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference myref;
    FirebaseDatabase firebaseDatabase;

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle b)
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        View view=inflater.inflate(R.layout.show_data_layout,group,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.show_data_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        myref= FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseRecyclerAdapter<ShowDataItems,ShowDataViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<ShowDataItems,ShowDataViewHolder>(
                ShowDataItems.class,
                R.layout.show_data_single_item,
                ShowDataViewHolder.class,
                myref
        ) {

            public void populateViewHolder(final MyColleagueFragment.ShowDataViewHolder viewHolder, ShowDataItems model, final int position) {
                viewHolder.imageurl(model.getImageurl());
                viewHolder.name(model.getName());


                //OnClick Item
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Do you want to Delete this data ?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int selectedItems = position;
                                        mFirebaseAdapter.getRef(selectedItems).removeValue();
                                        mFirebaseAdapter.notifyItemRemoved(selectedItems);
                                        recyclerView.invalidate();
                                        onStart();
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
    public static class ShowDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView textview_name;
        private final ImageView imageView;




        public ShowDataViewHolder(final View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.fetch_image);
            textview_name = (TextView) itemView.findViewById(R.id.fetch_image_title);


        }

        private void name(String name) {
            textview_name.setText(name);
        }

        private void imageurl(String imageurl) {

            Picasso.with(itemView.getContext())
                    .load(imageurl)
                    .into(imageView);

        }



    }


}


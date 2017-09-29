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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class UpdateInventory extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference myref;
    static String UserTag,userid;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseRecyclerAdapter<Inventory,ShowDataViewHolder> recyclerAdapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle b)
    {
        View view=inflater.inflate(R.layout.main,group,false);
        getActivity().setTitle("Update Inventory");
        mAuth= FirebaseAuth.getInstance();
        user =mAuth.getCurrentUser();
        userid = user.getUid().toString();
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        myref= FirebaseDatabase.getInstance().getReference().child("Inventory");
        recyclerAdapter=new FirebaseRecyclerAdapter<Inventory,ShowDataViewHolder>(
                Inventory.class,
                R.layout.show_data_single_item,
                ShowDataViewHolder.class,
                myref
        ) {
            @Override
            protected void populateViewHolder(ShowDataViewHolder viewHolder, Inventory model, final int position) {
                 viewHolder.setEtTitle(model.getEtTitle()+":- "+ model.getEtQuant()+" Left");
                 viewHolder.setImageurl(model.getImageurl());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        if(userid.equals("YmFDwtw9ncMTaZyXTKQkqTpCutG3")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("What you want to do with this item?").setCancelable(true)
                                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int selectedItems = position;
                                            UserTag = recyclerAdapter.getRef(selectedItems).getKey();
                                            startActivity(new Intent(getContext(), InvenDataUpdate.class));
                                        }
                                    })
                                    .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                            builder.setMessage("Do you want to Delete this data ?").setCancelable(false)
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            int selectedItems = position;
                                                            recyclerAdapter.getRef(selectedItems).removeValue();
                                                            recyclerAdapter.notifyItemRemoved(selectedItems);
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
                                            AlertDialog dialog2 = builder.create();
                                            dialog2.setTitle("Confirm");
                                            dialog2.show();
                                        }
                                    });
                            AlertDialog dialog1 = builder.create();
                            dialog1.setTitle("Confirm");
                            dialog1.show();
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Do you want to Update this data ?").setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int selectedItems = position;
                                            UserTag = recyclerAdapter.getRef(selectedItems).getKey();
                                            startActivity(new Intent(getContext(), InvenDataUpdate.class));
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
                    }
                });
            }
        };
        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }
}

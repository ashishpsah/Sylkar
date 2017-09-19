package com.example.ashish.sylkar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class InvenDataView extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    static String UserTag;
    private FirebaseRecyclerAdapter<InventoryData, InvenDataView.ShowDataViewHolder> mFirebaseAdapter;
    private FirebaseAuth firebaseAuth;
    String CurrentUser;





    public InvenDataView() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_data_layout);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        CurrentUser = user.getEmail();




        myRef = FirebaseDatabase.getInstance().getReference("Inventory");


        recyclerView = (RecyclerView)findViewById(R.id.show_data_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(InvenDataView.this));





    }

    @Override
    public void onStart() {
        super.onStart();
        //Log.d("LOGGED", "IN onStart ");
        mFirebaseAdapter = new FirebaseRecyclerAdapter<InventoryData, InvenDataView.ShowDataViewHolder>(InventoryData.class, R.layout.show_data_single_item, InvenDataView.ShowDataViewHolder.class, myRef) {




            public void populateViewHolder(final InvenDataView.ShowDataViewHolder viewHolder, InventoryData model, final int position) {
                viewHolder.imageurl(model.getImageurl());
                viewHolder.name(model.getEtTitle()+":- "+ model.getEtQuant()+" Left");





                //OnClick Item
                String admin = "sylkewulff@gmail.com".toString().trim();
                if(CurrentUser.equals(admin)){

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(final View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(InvenDataView.this);
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
                else {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(final View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(InvenDataView.this);
                            builder.setMessage("What you want to do with this item?").setCancelable(false)
                                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int selectedItems = position;

                                            UserTag = mFirebaseAdapter.getRef(selectedItems).getKey();



                                            startActivity(new Intent(InvenDataView.this, InvenDataUpdate.class));
                                        }
                                    })
                                    .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int selectedItems = position;
                                            mFirebaseAdapter.getRef(selectedItems).removeValue();
                                            mFirebaseAdapter.notifyItemRemoved(selectedItems);
                                            recyclerView.invalidate();
                                            onStart();
                                            startActivity(new Intent(InvenDataView.this, Homepage.class));
                                            Toast.makeText(getApplicationContext(), "Item Deleted!!!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.setTitle("Confirm");
                            dialog.show();
                        }
                    });
                }


            }
        };


        recyclerView.setAdapter(mFirebaseAdapter);

    }





    //View Holder For Recycler View
    public static class ShowDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView textview_name;
        private final ImageView imageView;




        public ShowDataViewHolder(final View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.fetch_image);
            textview_name = (TextView) itemView.findViewById(R.id.fetch_image_title);


        }

        public void name(String name) {
            textview_name.setText(name);
        }

        public void imageurl(String imageurl) {

            Picasso.with(itemView.getContext())
                    .load(imageurl)
                    .into(imageView);

        }



    }
}

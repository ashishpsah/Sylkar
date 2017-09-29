package com.example.ashish.sylkar;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class MyColleagueFragment extends Fragment {
    private RecyclerView recyclerView;
    FirebaseAuth mAuth;
    private DatabaseReference myref;
    private FirebaseDatabase database;
    static String UserTag,userid,username;
    FirebaseRecyclerAdapter<Users, ShowDataViewHolder> recyclerAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle b) {
        View view = inflater.inflate(R.layout.main, group, false);
        getActivity().setTitle("THE GANG");
        mAuth= FirebaseAuth.getInstance();
        userid =mAuth.getCurrentUser().getUid().toString();
        database = FirebaseDatabase.getInstance();
        //get username from database
        myref = database.getReference();
        myref.child("Users").child(userid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            Users users = dataSnapshot.getValue(Users.class);
                            username = users.etTitle;
                        }
                        catch (Exception e) {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
                        UserTag = recyclerAdapter.getRef(position).getKey();
                        //User want to delete his profile and he is not admin
                        if((userid.equals(UserTag) && !(UserTag.equals("yvprnDUyyOTijsHOQmpg6a11luA3")))){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Are you sure you want to delete your pofile?").setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int selectedItems = position;
                                            recyclerAdapter.getRef(selectedItems).removeValue();
                                            recyclerAdapter.notifyItemRemoved(selectedItems);
                                            recyclerView.invalidate();
                                            onStart();
                                            startActivity(new Intent(getContext(), Homepage.class));
                                            Toast.makeText(getContext(), "Successfully deleted profile!!!", Toast.LENGTH_SHORT).show();
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
                        //User wants to send notification and he is not admin
                        else if (!(userid.equals(UserTag)) && !(userid.equals("yvprnDUyyOTijsHOQmpg6a11luA3"))) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Do you want to send Notification?").setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
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
                        //if user is admin
                        else if(userid.equals("yvprnDUyyOTijsHOQmpg6a11luA3")) {
                            AdminHomepage adminHomepage = new AdminHomepage();
                            if(adminHomepage.count == 2){
                                if(UserTag.equals("yvprnDUyyOTijsHOQmpg6a11luA3"))
                                {
                                    Toast.makeText(getContext(), "You cannot Delete yourself", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    adminHomepage.count = 0;
                                    DelAccount delAccount = new DelAccount();
                                    FragmentManager manager = getFragmentManager();
                                    manager.beginTransaction().replace(
                                            R.id.relativelayout_for_fragment,
                                            delAccount).commit();
                                }
                            }
                            else if (adminHomepage.count == 1) {
                                adminHomepage.count = 0;
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Do you want to see detais?").setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(getContext(), ViewGangMember.class));
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                startActivity(new Intent(getContext(), AdminHomepage.class));
                                            }
                                        });
                                AlertDialog dialog2 = builder.create();
                                dialog2.setTitle("Confirm");
                                dialog2.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Choose from options below").setCancelable(true)
                                        .setPositiveButton("Notify", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Admin cannot send notification to itself
                                                if (UserTag.equals("yvprnDUyyOTijsHOQmpg6a11luA3")) {
                                                    Toast.makeText(getContext(), "You cannot send Notification to yourself", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                    builder.setMessage("Do you want to send Notification?").setCancelable(false)
                                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                    startActivity(new Intent(getContext(), SendNotification.class));
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
                                            }
                                        })
                                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setMessage("Are you sure you want to delete this colleague?").setCancelable(false)
                                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                int selectedItems = position;
                                                                recyclerAdapter.getRef(selectedItems).removeValue();
                                                                recyclerAdapter.notifyItemRemoved(selectedItems);
                                                                recyclerView.invalidate();
                                                                onStart();
                                                                startActivity(new Intent(getContext(), AdminHomepage.class));
                                                                Toast.makeText(getContext(), "Successfully deleted profile!!!", Toast.LENGTH_SHORT).show();
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
                                dialog1.setTitle("Choose!!!");
                                dialog1.show();
                            }
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }
}






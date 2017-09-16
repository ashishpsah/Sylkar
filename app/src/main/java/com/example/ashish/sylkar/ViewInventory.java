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
import com.google.api.client.repackaged.com.google.common.base.Objects;
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

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    static String UserTag;
    private FirebaseRecyclerAdapter<ShowDataItems, ViewInventory.ShowDataViewHolder> mFirebaseAdapter;
    private FirebaseAuth firebaseAuth;
    String CurrentUser;


    private OnFragmentInteractionListener mListener;

    public ViewInventory() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        CurrentUser = user.getEmail();



        myRef = FirebaseDatabase.getInstance().getReference("Users");


        recyclerView = (RecyclerView)getView().findViewById(R.id.show_data_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFirebaseAdapter = new FirebaseRecyclerAdapter<ShowDataItems, ViewInventory.ShowDataViewHolder>(ShowDataItems.class, R.layout.show_data_single_item, ViewInventory.ShowDataViewHolder.class, myRef) {


            @Override
            protected void populateViewHolder(ShowDataViewHolder viewHolder, ShowDataItems model, int position) {

            }

            public void populateViewHolder(final InvenDataView.ShowDataViewHolder viewHolder, ShowDataItems model, final int position) {
                viewHolder.imageurl(model.getImageurl());
                viewHolder.name(model.getName());




                //OnClick Item
                String admin = "sylkewulff@gmail.com".toString().trim();
                if(Objects.equal(CurrentUser,admin)){

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
                else {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(final View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Do you want to send Notification?").setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int selectedItems = position;

                                            UserTag = mFirebaseAdapter.getRef(selectedItems).getKey();


                                            Toast.makeText(getContext(), UserTag, Toast.LENGTH_LONG).show();


                                            mFirebaseAdapter.notifyItemRemoved(selectedItems);
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


            }
        };


        recyclerView.setAdapter(mFirebaseAdapter);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_inventory, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

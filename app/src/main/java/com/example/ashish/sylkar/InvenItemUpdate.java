package com.example.ashish.sylkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;



public class InvenItemUpdate extends Fragment {
    private Firebase mRoofRef;
    UpdateInventory updateInventory = new UpdateInventory();
    private String Key = updateInventory.UserTag.toString();
    Button buttonUpdate;
    TextView etQuant;

    public InvenItemUpdate() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getContext());
        buttonUpdate = (Button)getView().findViewById(R.id.buttonUpdate);
        etQuant =(TextView)getView().findViewById(R.id.etQuant);
        buttonUpdate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String Quant = etQuant.getText().toString().trim();
                if(Quant.equals(""))

                    Toast.makeText(getContext(), "Fill Required fields", Toast.LENGTH_LONG).show();


                else{
                    mRoofRef = new Firebase("https://sylkar-4cbdc.firebaseio.com/").child("Inventory");
                    mRoofRef.child(Key).child("etQuant").setValue(Quant);
                    startActivity(new Intent(getContext(), Homepage.class));
                    Toast.makeText(getContext(), "INVENTORY UPDATED", Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inven_item_update, container, false);
    }





}

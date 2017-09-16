package com.example.ashish.sylkar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class InvenDataUpdate extends AppCompatActivity implements View.OnClickListener {
    private Firebase mRoofRef;
    InvenDataView invenDataView = new InvenDataView();
    private String Key = invenDataView.UserTag.toString();

    Button buttonUpdate;
    TextView etQuant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_inven_data_update);
        buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
        etQuant =(TextView)findViewById(R.id.etQuant);
        buttonUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonUpdate )
        {
           String Quant = etQuant.getText().toString().trim();
            if(Quant.equals(""))

                Toast.makeText(getApplicationContext(), "Fill Required fields", Toast.LENGTH_LONG).show();


            else{
                mRoofRef = new Firebase("https://sylkar-4cbdc.firebaseio.com/").child("Inventory");
                mRoofRef.child(Key).child("etQuant").setValue(Quant);
                startActivity(new Intent(InvenDataUpdate.this, Homepage.class));
                Toast.makeText(getApplicationContext(), "INVENTORY UPDATED", Toast.LENGTH_LONG).show();

            }



        }
    }


}

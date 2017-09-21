package com.example.ashish.sylkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InvenDataUpdate extends AppCompatActivity implements View.OnClickListener {
    private Firebase mRoofRef;
    UpdateInventory updateInventory = new UpdateInventory();
    private String Key = updateInventory.UserTag.toString();

    Button buttonUpdate;
    TextView etQuant;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    String userid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_inven_data_update);
        buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
        etQuant =(TextView)findViewById(R.id.etQuant);
        buttonUpdate.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userid = user.getUid().toString();
        if(getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();

        }
        return super.onOptionsItemSelected(item);
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
                Toast.makeText(getApplicationContext(), "INVENTORY UPDATED", Toast.LENGTH_LONG).show();

                if(userid.equals("YmFDwtw9ncMTaZyXTKQkqTpCutG3")){
                    startActivity(new Intent(InvenDataUpdate.this, AdminHomepage.class));

                }

                else{
                    startActivity(new Intent(InvenDataUpdate.this, Homepage.class));

                }


            }



        }
    }


}

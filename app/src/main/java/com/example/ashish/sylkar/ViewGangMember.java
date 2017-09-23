package com.example.ashish.sylkar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewGangMember extends AppCompatActivity {

    FirebaseAuth mAuth;
    private DatabaseReference myref;
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Users, ShowGangMember> recyclerAdapter;
    static String userid;
    private TextView name,job,address,iban,phone;
    private ImageView dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_gang_member);
        if(getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        MyColleagueFragment myColleagueFragment = new MyColleagueFragment();
        userid =myColleagueFragment.UserTag;
        database = FirebaseDatabase.getInstance();
        myref = database.getReference();
        name = (TextView)findViewById(R.id.fetch_image_title);
        job = (TextView)findViewById(R.id.fetch_job);
        address =(TextView)findViewById(R.id.fetch_address);
        iban = (TextView)findViewById(R.id.fetch_iban);
        phone = (TextView)findViewById(R.id.fetch_phone);
        dp = (ImageView)findViewById(R.id.fetch_image);
        myref.child("Users").child(userid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            Users users = dataSnapshot.getValue(Users.class);

                            name.setText(users.getEtTitle());
                            job.setText("Works as "+users.getEtJob());
                            address.setText("Lives at " +users.getEtAddress());
                            iban.setText("IBAN:- "+users.getEtIBAN());
                            phone.setText("Phone number:- "+users.getEtPhone());
                            Picasso.with(ViewGangMember.this)
                                    .load(users.getImageurl())
                                    .into(dp);


                        }
                        catch (Exception e) {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            AdminHomepage adminHomepage = new AdminHomepage();
            adminHomepage.count =1;
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
}

package com.example.ashish.sylkar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminHomepage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    FirebaseUser user;
    String userid,username,dp;
    ImageView userimage;
    TextView name,email;
    DatabaseReference myRef;
    private Firebase mRoofRef;
    FirebaseDatabase database;
    static int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userid = user.getUid().toString();
        database = FirebaseDatabase.getInstance();

        myRef = database.getReference();
        set_nav_header();





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    public void set_nav_header(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userid = user.getUid().toString();



            // User logged in
            //
            //email.setText("HI ALL");

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

            navigationView.setNavigationItemSelectedListener(this);
            View header=navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
            name = (TextView)header.findViewById(R.id.name);
            email = (TextView)header.findViewById(R.id.email);
            userimage = (ImageView)header.findViewById(R.id.imageView);
            String emailAddress = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            user = mAuth.getCurrentUser();
            userid = user.getUid().toString();
            mRoofRef = new Firebase("https://sylkar-4cbdc.firebaseio.com/").child("Users");
            // Read from the database
            myRef.child("Users").child(userid).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                Users users = dataSnapshot.getValue(Users.class);

                                username = users.etTitle; // "John Doe"
                                dp = users.imageurl.toString();
                                name.setText(username);
                                name.setTextColor(Color.parseColor("#000000"));
                                //Toast.makeText(Homepage.this,dp, Toast.LENGTH_LONG).show();
                                //if(!(TextUtils.isEmpty(dp)))
                                Picasso.with(AdminHomepage.this).load(dp).into(userimage);
                            }
                            catch (Exception e) {
                                Toast.makeText(AdminHomepage.this,
                                        "Please add your data from Edit Profile menu",
                                        Toast.LENGTH_LONG).show();


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            email.setText(emailAddress.toString());
            email.setTextColor(Color.parseColor("#000000"));
        }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                // No user is signed in
                startActivity(new Intent(this, LoginActivity.class));

            } else {
                // User logged in
                String emailAddress = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                mAuth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AdminHomepage.this, "Successfully send you response...Login Again After Updating your Password", Toast.LENGTH_LONG).show();
                                }

                            }

                        });
                startActivity(new Intent(this, LoginActivity.class));


            }

        }
        if (id == R.id.updateEmail){
            startActivity(new Intent(this, UpdateEmail.class));
        }

        if(id == R.id.sign_out){
            finish();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));

        }


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.adminhome) {
            HomeFragment homeFragment = new HomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.relativelayout_for_fragment,
                    homeFragment).commit();

        }  else if (id == R.id.mycoll) {
            count = 0;
            MyColleagueFragment myColleagueFragment = new MyColleagueFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.relativelayout_for_fragment,
                    myColleagueFragment).commit();


        }
        else if (id == R.id.gdetails) {
            MyColleagueFragment myColleagueFragment = new MyColleagueFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.relativelayout_for_fragment,
                    myColleagueFragment).commit();
            count =1;


        }
        else if (id == R.id.addcoll) {
            AddUserFragment addUserFragment = new AddUserFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.relativelayout_for_fragment,
                    addUserFragment).commit();
            count =0;

        } else if (id == R.id.addinventory) {
            AddInventory addInventory = new AddInventory();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.relativelayout_for_fragment,
                    addInventory).commit();
            count = 0;

        } else if (id == R.id.updateinventory) {
            UpdateInventory updateInventory = new UpdateInventory();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.relativelayout_for_fragment,
                    updateInventory).commit();

        } else if (id == R.id.admineditprofile) {
            EditProfileFragment editProfileFragment = new EditProfileFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.relativelayout_for_fragment,
                    editProfileFragment).commit();
            count = 0;

        }
        else if (id == R.id.delcoll) {
            MyColleagueFragment myColleagueFragment = new MyColleagueFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.relativelayout_for_fragment,
                    myColleagueFragment).commit();
            count = 2;

        }else if (id == R.id.logout) {
            finish();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

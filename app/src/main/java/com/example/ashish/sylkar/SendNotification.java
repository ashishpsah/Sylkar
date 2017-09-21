package com.example.ashish.sylkar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SendNotification extends AppCompatActivity {
    private Button buttonNotify;
    private EditText editTextNotify;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String Current,message,userid;
    static String username;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    final MyColleagueFragment model =new MyColleagueFragment();
    private static final String TAG= SendNotification.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        buttonNotify = (Button )findViewById(R.id.buttonNotify);
        editTextNotify = (EditText) findViewById(R.id.editTextNotify);
        mAuth= FirebaseAuth.getInstance();
        user =mAuth.getCurrentUser();
        Current =  model.UserTag;
        userid = user.getUid().toString();
        if(getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        database = FirebaseDatabase.getInstance();
        //get username from database
        myRef = database.getReference();
        myRef.child("Users").child(userid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            Users users = dataSnapshot.getValue(Users.class);

                            username = users.etTitle; // "John Doe"

                        }
                        catch (Exception e) {
                            Toast.makeText(SendNotification.this,
                                    "Please add your data from Edit Profile menu",
                                    Toast.LENGTH_LONG).show();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        buttonNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Sender = user.getUid().toString();



                   Toast.makeText(getApplicationContext(), "Notification Sent", Toast.LENGTH_SHORT).show();
                    sendNotification();

                if(userid.equals("YmFDwtw9ncMTaZyXTKQkqTpCutG3")){
                    startActivity(new Intent(SendNotification.this, AdminHomepage.class));
                }

                else{
                    startActivity(new Intent(SendNotification.this, Homepage.class));
                }




            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }







   private void sendNotification()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    message = username +" SAYS:" +
                            System.lineSeparator() + editTextNotify.getText().toString();


                   /* if (Current.equals("carstenwulff@gmail.com")) {
                        send_email = "harishcarbon@gmail.com";
                    } else {
                        send_email = "carstenwulff@gmail.com";
                    }*/

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic MjRjMDQ2ZDctNWE2Ny00ZDVmLWI3ZmMtZGMzNTU5ODE2YTM2");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"0bd2c3a8-c151-4bb8-b144-1374e1f6127f\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + Current + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\":\"" + message + "\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }
}

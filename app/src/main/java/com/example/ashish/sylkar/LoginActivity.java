package com.example.ashish.sylkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.api.client.repackaged.com.google.common.base.Objects;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onesignal.OneSignal;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button3;
    private EditText emailid;
    private EditText passsword;
    private TextView textView4;
    String mail,pass;
    //Firebase auth object
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        //initialize the views
        button3 =(Button) findViewById(R.id.button3);
        emailid =(EditText) findViewById(R.id.emailid);
        passsword =(EditText) findViewById(R.id.password);
        textView4 = (TextView) findViewById(R.id.textView4);
        //attach the listner to button
        button3.setOnClickListener(this);
        textView4.setOnClickListener(this);

    }



    private void user_login(){

        firebaseAuth.signInWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();//before starting another activity kill current activity
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String check_admin = user.getEmail().toString().trim();
                            String admin = "sylkewulff@gmail.com".toString().trim();
                            if(Objects.equal(check_admin,admin))
                                startActivity(new Intent(getApplicationContext(), AdminHomepage.class));

                            else
                                startActivity(new Intent(getApplicationContext(), Homepage.class));
                            user = firebaseAuth.getCurrentUser();
                            String User_id = user.getUid();
                            //set user tag
                            OneSignal.sendTag("User_ID", User_id);
                        }
                        else {

                                // If sign in fails, display a message to the user.

                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if(v == button3 )
        {
            mail = emailid.getText().toString().trim();
            pass = passsword.getText().toString().trim();
            if(mail.equals("") ||pass.equals("") )

                Toast.makeText(getApplicationContext(), "Fill Required fields", Toast.LENGTH_LONG).show();


            else

            user_login();
        }
        if(v == textView4)
        {
            startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
        }



    }




}

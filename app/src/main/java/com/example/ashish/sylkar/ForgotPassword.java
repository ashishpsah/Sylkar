package com.example.ashish.sylkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    EditText editTextEmail; //to get email address
    ProgressBar progressBar;    //show progress bar
    Button buttonForgotPassword;    //button for click
    FirebaseAuth firebaseAuth;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        buttonForgotPassword = (Button) findViewById(R.id.buttonForgotPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        buttonForgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == buttonForgotPassword)
        {
            email = editTextEmail.getText().toString();


            /*  check if email address is blank
            * */
            if (TextUtils.isEmpty(email)) {

                Toast.makeText(ForgotPassword.this, "Successfully send you response", Toast.LENGTH_LONG).show();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }



            /*
            *       send request for reset password
            * */
            else {


                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPassword.this, "Successfully send you response", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                            Toast.makeText(ForgotPassword.this, "Failed to Send", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        }
    }


}

package com.example.ashish.sylkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextEmail;
    private Button buttonForgotPassword;
    private FirebaseAuth mAuth;
    String email;
    int count = 0;
    public static final String TAG = "Sylkar logs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        buttonForgotPassword = (Button) findViewById(R.id.buttonForgotPassword);
        mAuth = FirebaseAuth.getInstance();
        buttonForgotPassword.setOnClickListener(this);
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
        if(v == buttonForgotPassword)
        {   count = 0;
            email = editTextEmail.getText().toString().toLowerCase();
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            //Email field is valid or not
            if (TextUtils.isEmpty(email) || !(email.matches(emailPattern))) {
                editTextEmail.setError("Valid Email Required.");
                editTextEmail.setHint("Please enter valid email id");
                Toast.makeText(ForgotPassword.this, "Enter valid email id", Toast.LENGTH_SHORT).show();
                count = 1;
            }
            //check if user is registered or not
            else {
                mAuth.signInWithEmailAndPassword(email, email)
                        .addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {


                                            try {
                                                throw task.getException();
                                            }
                                            // if user enters wrong email.
                                            catch (FirebaseAuthInvalidUserException invalidEmail) {

                                                Toast.makeText(ForgotPassword.this, "User with " + email +
                                                        " does not exists", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "user doesn't exists");
                                                count = 1;

                                                // TODO: take your actions!
                                            }
                                            catch (Exception e) {

                                            }
                                        }
                                    }
                                });
            }
            //Send the password reset email
            if(count == 0)

                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "successfully sent");
                            Toast.makeText(ForgotPassword.this,
                                    "Successfully send you response", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                            Log.d(TAG, "can't send");
                            Toast.makeText(ForgotPassword.this, "Failed to Send", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        }
    }




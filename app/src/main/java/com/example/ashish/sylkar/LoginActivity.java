package com.example.ashish.sylkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.onesignal.OneSignal;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button3;
    private EditText emailid;
    private EditText password;
    private TextView textView4;
    private Firebase mRef;
    int count =0;
    //Firebase auth object
    private FirebaseAuth mAuth;
    public static final String TAG = "Sylkar login logs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Set Context for Firebase before using it
        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://sylkar-4cbdc.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();
        //initialize the views
        button3 = (Button) findViewById(R.id.button3);
        emailid = (EditText) findViewById(R.id.emailid);
        password = (EditText) findViewById(R.id.password);
        textView4 = (TextView) findViewById(R.id.textView4);
        //attach the listner to button
        button3.setOnClickListener(this);
        textView4.setOnClickListener(this);
    }
    //Handle click events
    @Override
    public void onClick(View v) {
        if (v == button3) {
            SignInAccount(emailid.getText().toString().toLowerCase(), password.getText().toString());
        }

        if (v == textView4) {
            startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
        }

    }

    //Method for user sign in
    private void SignInAccount(final String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            Log.d(TAG, "Invalid form");
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    emailValidation(emailid.getText().toString().toLowerCase());
                                    if(count == 1) {
                                        Toast.makeText(LoginActivity.this, email +
                                                " is not a valid email address", Toast.LENGTH_SHORT).show();
                                    }
                                    else {

                                        try {
                                            throw task.getException();
                                        }
                                        // if user enters wrong email.
                                        catch (FirebaseAuthInvalidUserException invalidEmail) {
                                            Log.d(TAG, "onComplete: invalid_email");
                                            Toast.makeText(LoginActivity.this, "User with " + email +
                                                    " does not exists", Toast.LENGTH_SHORT).show();

                                            // TODO: take your actions!
                                        }
                                        // if user enters wrong password.
                                        catch (FirebaseAuthInvalidCredentialsException wrongPassword) {

                                            Log.d(TAG, "onComplete: wrong_password");
                                            Toast.makeText(LoginActivity.this, "Incorrect Password for " + email +
                                                    ". Please enter correct password", Toast.LENGTH_SHORT).show();


                                            // TODO: Take your action
                                        } catch (Exception e) {
                                            Log.d(TAG, "onComplete: " + e.getMessage());

                                        }
                                    }
                                } else {
                                    Log.d(TAG, "Login:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String User_id = user.getUid();
                                    //set user tag
                                    OneSignal.sendTag("User_ID", User_id);
                                    Toast.makeText(LoginActivity.this, "Welcome " + email + ".", Toast.LENGTH_SHORT).show();
                                    if(User_id.equals("yvprnDUyyOTijsHOQmpg6a11luA3"))
                                        startActivity(new Intent(LoginActivity.this, AdminHomepage.class));
                                    else
                                        startActivity(new Intent(LoginActivity.this, Homepage.class));

                                }
                            }
                        }
                );
    }

    //Method for form validations
    private boolean validateForm() {
        boolean valid = true;
        String email = emailid.getText().toString().toLowerCase();
        if (TextUtils.isEmpty(email)) {
            emailid.setError("Email Required.");
            emailid.setHint("Please enter email");
            Toast.makeText(LoginActivity.this, "Enter valid email id", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            emailid.setError(null);
        }
        String pass = password.getText().toString();
        if (pass.length() < 6) {
            password.setError("Valid Password required");
            password.setHint("Please enter valid password");
            Toast.makeText(LoginActivity.this, "Enter valid password", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    //Method for Email Validation
    private void emailValidation(String checkemail)
    {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (checkemail.matches(emailPattern))
        {
            count = 0;
        }
        else
        {
            count =1;
        }
    }
}


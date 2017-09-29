package com.example.ashish.sylkar;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddUserFragment extends Fragment implements OnCompleteListener {
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private FirebaseAuth mAuth;
    public static final String TAG = "Sylkar Add user logs";


    public AddUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add Colleague");
        buttonRegister = (Button) getView().findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) getView().findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) getView().findViewById(R.id.editTextPassword);
        buttonRegister.setOnClickListener((new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                createAccount(editTextEmail.getText().toString().toLowerCase(),
                        editTextPassword.getText().toString());
            }
        }));
        mAuth = FirebaseAuth.getInstance();
    }
    private void createAccount(final String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            Log.d(TAG, "Invalid form");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getContext(), "User with "+email +
                                    " created successfully. Please Login again",
                                    Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getContext(), LoginActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            try
                            {
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthWeakPasswordException weakPassword)
                            {
                                Toast.makeText(getContext(), "Weak Password Choose a strong one",
                                        Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onComplete: weak_password");


                                // TODO: take your actions!
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                            {
                                Log.d(TAG, "onComplete: malformed_email");
                                Toast.makeText(getContext(), "Enter Valid email id",
                                        Toast.LENGTH_SHORT).show();

                                // TODO: Take your action
                            }
                            catch (FirebaseAuthUserCollisionException existEmail)
                            {
                                Log.d(TAG, "onComplete: exist_email");
                                Toast.makeText(getContext(), "User already exists",
                                        Toast.LENGTH_SHORT).show();

                                // TODO: Take your action
                            }
                            catch (Exception e)
                            {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                            }
                        }
                    }
                });
    }

    //method to validate the filled form
    private boolean validateForm() {
        boolean valid = true;
        String email = editTextEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email Required.");
            editTextEmail.setHint("Please enter email");
            Toast.makeText(getContext(), "Invalid form", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            editTextEmail.setError(null);
        }
        String password = editTextPassword.getText().toString();
        if (password.length()<6) {
            editTextPassword.setError("Valid Password Required.");
            editTextPassword.setHint("Please enter valid password");
            Toast.makeText(getContext(), "Choose strong password (Atleast 6 Characters)",
                    Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            editTextPassword.setError(null);
        }
        return valid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_user, container, false);
    }

    @Override
    public void onComplete(@NonNull Task task) {


    }
}

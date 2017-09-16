package com.example.ashish.sylkar;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddUserFragment extends Fragment {
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseUsers;



    public AddUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonRegister = (Button) getView().findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) getView().findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) getView().findViewById(R.id.editTextPassword);
        databaseUsers = FirebaseDatabase.getInstance().getReference();
        buttonRegister.setOnClickListener((new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                registerColleague();
                startActivity(new Intent(getContext(), AdminHomepage.class));

            }
        }));
        firebaseAuth = FirebaseAuth.getInstance();
    }


    private void registerColleague()
    {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        databaseUsers = FirebaseDatabase.getInstance().getReference();
        firebaseAuth.createUserWithEmailAndPassword(email,password);
        Toast.makeText(getContext(),"Colleague Successfully Added",Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_user, container, false);
    }

}

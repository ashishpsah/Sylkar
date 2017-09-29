package com.example.ashish.sylkar;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class DelAccount extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser user;
    private DatabaseReference myref;
    private FirebaseDatabase database;
    FirebaseRecyclerAdapter<Users, ShowGangMember> recyclerAdapter;
    static String userid,uniqueid;
    private TextView name;
    private EditText email,password;
    private Button delete;
    private ImageView dp;
    int count;
    public DelAccount() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Delete Account");
        Firebase.setAndroidContext(getContext());
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userid = user.getUid().toString();
        name = (TextView)getView().findViewById(R.id.fetch_image_title);
        dp = (ImageView)getView().findViewById(R.id.fetch_image);
        email = (EditText)getView().findViewById(R.id.emailid);
        password = (EditText)getView().findViewById(R.id.password);
        delete = (Button)getView().findViewById(R.id.buttonDel);
        database = FirebaseDatabase.getInstance();
        myref = database.getReference();
        final MyColleagueFragment myColleagueFragment = new MyColleagueFragment();
        if(userid.equals("yvprnDUyyOTijsHOQmpg6a11luA3"))
        {
            uniqueid = myColleagueFragment.UserTag.toString();

        }
        else {
            uniqueid = userid.toString();
        }
        myref.child("Users").child(uniqueid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        try {
                            Users users = dataSnapshot.getValue(Users.class);

                            name.setText(users.getEtTitle());

                            Picasso.with(getContext())
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
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String emailid =email.getText().toString().toLowerCase(),
                        pass=password.getText().toString();

                if(userid.equals("yvprnDUyyOTijsHOQmpg6a11luA3"))
                {
                    DeleteAccount(emailid,pass);

                }
                else {
                    accountDelete(emailid,pass);
                }
            }
        });
    }

    private void accountDelete(final String email, final String password){
        if (!validateForm()) {
            return;
        }
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(email,password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(),
                                                        "Successfully deleted",
                                                        Toast.LENGTH_SHORT).show();
                                                getActivity().finish();
                                                FirebaseAuth.getInstance().signOut();
                                                DatabaseReference db_node =
                                                        FirebaseDatabase.getInstance()
                                                                .getReference()
                                                                .getRoot()
                                                                .child("Users").child(uniqueid);
                                                db_node.setValue(null);
                                                startActivity(new Intent(getContext(),
                                                        LoginActivity.class));
                                            }
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(getContext(), "Wrong credentials",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void DeleteAccount(final String email, final String password) {
        if (!validateForm()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    emailValidation(email);
                                    if(count == 1) {
                                        Toast.makeText(getContext(), email +
                                                " is not a valid email address",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else {

                                        try {
                                            throw task.getException();
                                        }
                                        // if user enters wrong email.
                                        catch (FirebaseAuthInvalidUserException invalidEmail) {

                                            Toast.makeText(getContext(), "User with " + email +
                                                    " does not exists", Toast.LENGTH_SHORT).show();
                                        }
                                        // if user enters wrong password.
                                        catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                                        {
                                            Toast.makeText(getContext(), "Incorrect Password for "
                                                    + email + ". Please enter correct password",
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {

                                        }
                                    }
                                }
                                else {
                                    final FirebaseUser user = FirebaseAuth.getInstance()
                                            .getCurrentUser();
                                    AuthCredential credential = EmailAuthProvider
                                            .getCredential(email,password);
                                    user.reauthenticate(credential)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()) {
                                                        user.delete()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(getContext(), "Account Successfully Deleted!!! Login Again", Toast.LENGTH_LONG).show();
                                                                            FirebaseAuth.getInstance().signOut();
                                                                            getActivity().finish();
                                                                            DatabaseReference db_node = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(uniqueid);
                                                                            db_node.setValue(null);


                                                                            startActivity(new Intent(getContext(), LoginActivity.class));
                                                                        } else {
                                                                            Toast.makeText(getContext(), "Cannot delete", Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                    else {
                                                        Toast.makeText(getContext(), "Wrong credentials", Toast.LENGTH_LONG).show();}

                                                }
                                            });
                                    database = FirebaseDatabase.getInstance();
                                    myref = database.getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                }
                            }
                        }
                );
    }

    //Method for form validations
    private boolean validateForm() {
        boolean valid = true;

        if (TextUtils.isEmpty(email.toString().toLowerCase())) {
            email.setError("Email Required.");
            email.setHint("Please enter email");
            Toast.makeText(getContext(), "Enter valid email id", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            email.setError(null);
        }
        String pass = password.getText().toString();
        if (pass.length() < 6) {
            password.setError("Valid Password required");
            password.setHint("Please enter valid password");
            Toast.makeText(getContext(), "Enter valid password", Toast.LENGTH_SHORT).show();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_del_account, container, false);
    }

}

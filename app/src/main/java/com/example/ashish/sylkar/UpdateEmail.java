package com.example.ashish.sylkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmail extends AppCompatActivity {
    Button UpdateEmail;
    TextView EmailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        UpdateEmail = (Button)findViewById(R.id.buttonUpdate);
        EmailAddress = (TextView)findViewById(R.id.editTextEmail);
        UpdateEmail.setOnClickListener((new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = EmailAddress.getText().toString().trim();
                if(email.equals(""))
                    Toast.makeText(getApplicationContext(), "Fill Required fields", Toast.LENGTH_LONG).show();
                else {
                    user.updateEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UpdateEmail.this, "Successfully send you response...Login Again After Updating your Password", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                    finish();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(UpdateEmail.this, LoginActivity.class));
                }
            }
        }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
}

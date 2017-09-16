package com.example.ashish.sylkar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadInfo extends AppCompatActivity {


    Button select_image,upload_button;
    ImageView user_image;
    TextView etTitle,etPhone,etAddress,etIBAN,etJob;
    public static final int READ_EXTERNAL_STORAGE = 0;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgressDialog;
    private Firebase mRoofRef,childRef_name;
    private Uri mImageUri = null;
    private DatabaseReference mdatabaseRef;
    private StorageReference mStorage;
    String imageurl;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_info);

        Firebase.setAndroidContext(this);

        select_image = (Button)findViewById(R.id.select_image);
        upload_button = (Button)findViewById(R.id.upload_bttn);
        user_image = (ImageView) findViewById(R.id.user_image);
        etTitle = (TextView) findViewById(R.id.etTitle);
        etPhone = (TextView) findViewById(R.id.etPhone);
        etAddress = (TextView) findViewById(R.id.etAddress);
        etIBAN = (TextView) findViewById(R.id.etIBAN);
        etJob = (TextView) findViewById(R.id.etJob);

        //Initialize the Progress Bar
        mProgressDialog = new ProgressDialog(UploadInfo.this);


        //Select image from External Storage...
        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Check for Runtime Permission
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getApplicationContext(), "Call for Permission", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
                    }
                }
                else
                {
                    callgalary();
                }
            }
        });

        //Initialize Firebase Database paths for database and Storage

        mdatabaseRef = FirebaseDatabase.getInstance().getReference();
        mRoofRef = new Firebase("https://sylkar-4cbdc.firebaseio.com/").child("Users");  // Push will create new child every time we upload data
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://sylkar-4cbdc.appspot.com/");


        //Click on Upload Button Title will upload to Database
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // editProfileData();
               /* final String mName = etTitle.getText().toString().trim();


                if(mName.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Fill all Field", Toast.LENGTH_SHORT).show();
                    return;
                }

                childRef_name = mRoofRef.child("Image_Title");
                //childRef_name.setValue("Hello");


                Toast.makeText(getApplicationContext(), "Updated Info", Toast.LENGTH_LONG).show();*/

                startActivity(new Intent(UploadInfo.this, ShowData.class));
            }
        });

    }




    //Check for Runtime Permissions for Storage Access
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    callgalary();
                return;
        }
        Toast.makeText(getApplicationContext(), "...", Toast.LENGTH_SHORT).show();
    }

    private void editProfileData()
    {
        String Name = etTitle.getText().toString().trim();
        String Phone = etPhone.getText().toString().trim();
        String Address = etAddress.getText().toString().trim();
        String IBAN = etIBAN.getText().toString().trim();
        String Job = etJob.getText().toString().trim();


        mdatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        Users users = new Users(Name,Address,Phone,IBAN,Job,imageurl);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mdatabaseRef.child(uid).setValue(users);
        Toast.makeText(getApplicationContext(), "Updated Info", Toast.LENGTH_LONG).show();
        // Toast.makeText(getContext(),"Data Added Successfully",Toast.LENGTH_LONG).show();
    }



    //If Access Granted gallery Will open
    private void callgalary() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }


    //After Selecting image from gallery image will directly uploaded to Firebase Database
    //and Image will Show in Image View
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            mImageUri = data.getData();
            user_image.setImageURI(mImageUri);
            StorageReference filePath = mStorage.child("User_Images").child(mImageUri.getLastPathSegment());
            mProgressDialog.setMessage("Uploading Image....");
            mProgressDialog.show();

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUri = taskSnapshot.getDownloadUrl();  //Ignore This error
                    imageurl= downloadUri.toString();

                    //ShowDataItems users = new ShowDataItems();
                   // users.setImageurl(imageurl);
                   // String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                   // mRoofRef.child(uid).setValue(users);
                    mProgressDialog.dismiss();



                }
            });
        }
    }


}

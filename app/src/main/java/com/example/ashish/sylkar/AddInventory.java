package com.example.ashish.sylkar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


public class AddInventory extends Fragment {
    Button select_image,upload_button;
    ImageView user_image;
    TextView etName,etQuant;
    public static final int READ_EXTERNAL_STORAGE = 0;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgressDialog;
    private Firebase mRoofRef,childRef_name;
    private Uri mImageUri = null;
    private DatabaseReference mdatabaseRef;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    String imageurl,userid;
    int x;



    public AddInventory() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add Inventory");
        Firebase.setAndroidContext(getContext());
        select_image = (Button)getView().findViewById(R.id.select_image);
        upload_button = (Button)getView().findViewById(R.id.upload_bttn);
        user_image = (ImageView) getView().findViewById(R.id.user_image);
        etName = (TextView)getView().findViewById(R.id.etName);
        etQuant = (TextView)getView().findViewById(R.id.etQuant);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userid = user.getUid().toString();



        //Initialize the Progress Bar
        mProgressDialog = new ProgressDialog(getContext());

        //Select image from External Storage...
        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Check for Runtime Permission
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getContext(), "Call for Permission", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
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
        mRoofRef = new Firebase("https://sylkar-4cbdc.firebaseio.com/").child("Inventory");  // Push will create new child every time we upload data
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://sylkar-4cbdc.appspot.com/");

        //Click on Upload Button Title will upload to Database
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addInventory();

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
        Toast.makeText(getContext(), "...", Toast.LENGTH_SHORT).show();
    }

    //action on click event
    private void addInventory()
    {
        String Name = etName.getText().toString().trim();
        String Quant = etQuant.getText().toString().trim();

        if(Name.equals("")|| Quant.equals("")) {

            Toast.makeText(getContext(), "Fill Required fields", Toast.LENGTH_LONG).show();
            return;
        }

        else {
            mdatabaseRef = FirebaseDatabase.getInstance().getReference();
            InventoryData data = new InventoryData(Name,Quant, imageurl);
            Firebase newRef = mRoofRef.push();
            newRef.setValue(data);
            Toast.makeText(getContext(), "Updated Info", Toast.LENGTH_LONG).show();

            if(userid.equals("yvprnDUyyOTijsHOQmpg6a11luA3")){
                startActivity(new Intent(getContext(), AdminHomepage.class));
            }

            else{
                startActivity(new Intent(getContext(), Homepage.class));
            }

        }

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
            StorageReference filePath = mStorage.child("Inventory_Images").child(mImageUri.getLastPathSegment());
            mProgressDialog.setMessage("Uploading Image....");
            mProgressDialog.show();

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUri = taskSnapshot.getDownloadUrl();  //Ignore This error
                    imageurl= downloadUri.toString();
                    mProgressDialog.dismiss();
                }
            });
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_inventory, container, false);
    }

}

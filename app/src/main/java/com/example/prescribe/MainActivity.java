package com.example.prescribe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class   MainActivity extends AppCompatActivity {
private ImageView imageViewAdd;
private EditText inputImageName, Price;
private TextView textViewProgess;
    public ProgressBar progressBar;
private Button btnUpload;

    private static final int REQUEST_CODE_IMAGE=101;

    Uri imageUri;
    boolean isImageAdded=false;

    DatabaseReference Dataref;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    imageViewAdd = findViewById(R.id.imageViewAdd);
    inputImageName= findViewById(R.id.inputImageName);
    textViewProgess=findViewById(R.id.textViewProgress);
    progressBar=findViewById(R.id.progressbar);
    btnUpload=findViewById(R.id.btnupload);
    Price=findViewById(R.id.price);

textViewProgess.setVisibility(View.GONE);
progressBar.setVisibility(View.GONE);

        Dataref= FirebaseDatabase.getInstance().getReference().child("Drug");


        mStorageRef = FirebaseStorage.getInstance().getReference().child("DrugImage");


    imageViewAdd.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,REQUEST_CODE_IMAGE);
        }
    });

    btnUpload.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           final String imageName= inputImageName.getText().toString();
           //final int PriceAdd = Integer.parseInt(Price.getText().toString());
            String PriceAdd=Price.getText().toString();
           if (isImageAdded!=false && imageName!=null);
           {
               uploadImage(imageName,PriceAdd);
           }
        }
    });



    }

    private void uploadImage(final String imageName, final String priceAdd) {
        textViewProgess.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        final String key=Dataref.push().getKey();
        mStorageRef.child(key+"*.jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mStorageRef.child(key+"*.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap hashMap= new HashMap();
                        hashMap.put("DrugName", imageName);
                        hashMap.put("Price", priceAdd);
                        hashMap.put("ImageUrl", uri.toString());

                        Dataref.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                //Toast.makeText(MainActivity.this,"Data inserted Succesfully Uploaded", Toast.LENGTH_LONG);
                            }
                        });
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress =(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int)progress);
                textViewProgess.setText(progress+"%");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_IMAGE && data!=null){
            imageUri=data.getData();
            isImageAdded=true;
            imageViewAdd.setImageURI(imageUri);
        }
    }
}
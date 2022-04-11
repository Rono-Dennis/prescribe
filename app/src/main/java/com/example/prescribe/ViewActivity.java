package com.example.prescribe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ViewActivity extends AppCompatActivity {

    private ImageView imageView;
    TextView textView, priceview;
    Button btnDelete;

    DatabaseReference databaseReference, dataref;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        imageView=findViewById(R.id.image_single_view_Activity);
        textView=findViewById(R.id.textView_single_view_Activity);
        priceview=findViewById(R.id.price_single_view);
        btnDelete=findViewById(R.id.btnDelete);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Drug");

        String DrugKey=getIntent().getStringExtra("DrugKey");
        dataref=FirebaseDatabase.getInstance().getReference().child("Drug").child("DrugKey");
        storageRef= FirebaseStorage.getInstance().getReference().child("DrugImage").child(DrugKey+".jpg");

        databaseReference.child(DrugKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    String DrugName=dataSnapshot.child("DrugName").getValue().toString();
                    String ImageUrl=dataSnapshot.child("ImageUrl").getValue().toString();
                    String Pricetag=dataSnapshot.child("Price").getValue().toString();

                    Picasso.get().load(ImageUrl).into(imageView);
                    textView.setText(DrugName);
                    priceview.setText(Pricetag);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ViewActivity.this,"Image Succesfully deleted", Toast.LENGTH_LONG);

                            }
                        });
                    }
                });
            }
        });

    }
}
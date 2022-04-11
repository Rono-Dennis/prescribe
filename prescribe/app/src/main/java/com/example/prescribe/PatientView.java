package com.example.prescribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class PatientView extends AppCompatActivity {
    EditText inputsearch;
    RecyclerView recyclerView;

    FirebaseRecyclerOptions<Drug> options;
    FirebaseRecyclerAdapter<Drug,MyViewHolder> adapter;

    DatabaseReference DataRef;




    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view);


        inputsearch=findViewById(R.id.inputsearch);
        recyclerView=findViewById(R.id.recycleView);
        DataRef= FirebaseDatabase.getInstance().getReference().child("Drug");
        fab = findViewById(R.id.fab);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);


        LoadData("");

        inputsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString()!=null){
                    LoadData(s.toString());
                }
                else
                {
                    LoadData("");
                }

            }
        });


    }

    private void LoadData(String data) {
        Query query=DataRef.orderByChild("DrugName").startAt(data).endAt(data+"\uf8ff");
        //Query query1= DataRef.orderByChild("Pricetag").startAt(data).endAt(data+"\uf8ff");

        options  = new FirebaseRecyclerOptions.Builder<Drug>().setQuery(query,Drug.class).build();
        //options =  new  FirebaseRecyclerOptions.Builder<Drug>().setQuery(query1,Drug.class).build();
        adapter= new FirebaseRecyclerAdapter<Drug, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i, @NonNull Drug drug) {
                myViewHolder.textView.setText(drug.getDrugName());
                myViewHolder.priceView.setText("price = Ksh " + drug.getPrice());
                Picasso.get().load(drug.getImageUrl()).into(myViewHolder.imageView);
                myViewHolder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PatientView.this,ViewActivity.class);
                        intent.putExtra("DrugKey", getRef(i).getKey());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view,parent, false);

                return new MyViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
package com.example.prescribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Login extends AppCompatActivity {
    private MaterialEditText email,password;
    private ProgressBar progressBar;
    private TextView forgetPassword;
    private FirebaseAuth firebaseAuth;
    Button reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button registerbtn = findViewById(R.id.register);
        Button loginbtn = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressbar);

        firebaseAuth = FirebaseAuth.getInstance();
        reg=(Button)findViewById(R.id.register);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  tex_email = email.getText().toString();
                String tex_password = password.getText().toString();

                if (tex_email.isEmpty()) {
                    email.setError("Email is required");
                    email.requestFocus();
                    return;
                }
                if (tex_password.isEmpty()) {
                    password.setError("Email is required");
                    password.requestFocus();
                    return;
                }

                if (tex_password.length() < 6) {
                    password.setError("Password should be more than 6 characters");
                    password.requestFocus();
                    return;
                }  else {
                    login(tex_email,tex_password);
                }


            }
        });

    }

    private void login(String tex_email, String tex_password) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(tex_email,tex_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(Login.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
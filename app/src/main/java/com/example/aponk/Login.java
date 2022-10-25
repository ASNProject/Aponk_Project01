package com.example.aponk;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aponk.Dashboard.Dashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private TextView register;
    private EditText email, password;
    private Button login;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);
        progressBar = (ProgressBar) findViewById(R.id.red_progressBar);

        login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.btn_login:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String nemail = email.getText().toString().trim();
        String npassword = password.getText().toString().trim();

        if (nemail.isEmpty()){
            email.setError("Masukkan email");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(nemail).matches()){
            email.setError("Format email salah");
            email.requestFocus();
            return;
        }
        if (npassword.isEmpty()){
            password.setError("Masukkan password");
            password.requestFocus();
            return;
        }
        if (npassword.length() < 6){
            password.setError("Password minimal 6 digit");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.GONE);

        mAuth.signInWithEmailAndPassword(nemail, npassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(Login.this, Dashboard.class));
                    progressBar.setVisibility(View.GONE);
                    finish();
                }
                else {
                    Toast.makeText(Login.this, "Gagagl Login! Periksa kembali email dan password", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}
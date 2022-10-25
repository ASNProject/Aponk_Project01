package com.example.aponk;

//import android.support.v7.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText nama, nik, email, username, password, alamat;
    private ProgressBar progressBar;
    private Button daftar;
    private Spinner spiner, spiner2, spiner3;
    private TextView jeniskelamin, kelumpokusia, statuswarganegara;
    private String[] list = {"-Pilih-", "Laki-Laki", "Perempuan"};
    private String[] list2 = {"-Pilih-", "Anak-anak", "Dewasa", "Lansia"};
    private String[] list3 = {"-Pilih-", "WNI", "WNA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        nama = (EditText) findViewById(R.id.reg_nama);
        nik = (EditText) findViewById(R.id.reg_nik);
        email = (EditText) findViewById(R.id.reg_email);
        username = (EditText) findViewById(R.id.reg_username);
        password = (EditText) findViewById(R.id.reg_password);
        alamat = (EditText) findViewById(R.id.reg_alamat);
        jeniskelamin = (TextView) findViewById(R.id.reg_jeniskelamin);
        kelumpokusia = (TextView) findViewById(R.id.reg_kelompokusia);
        statuswarganegara = (TextView) findViewById(R.id.reg_statuswarganegara);

        //Spiner 1
        spiner = (Spinner) findViewById(R.id.reg_spiner);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jeniskelamin.setText(list[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                jeniskelamin.setText("");
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spiner.setAdapter(adapter);


        //Spiner 2
        spiner2 = (Spinner) findViewById(R.id.reg_spiner2);
        spiner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kelumpokusia.setText(list2[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                kelumpokusia.setText("");
            }
        });
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list2);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spiner2.setAdapter(adapter2);

        //Spiner 3
        spiner3 = (Spinner) findViewById(R.id.reg_spiner3);
        spiner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statuswarganegara.setText(list3[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                statuswarganegara.setText("");
            }
        });
        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list3);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spiner3.setAdapter(adapter3);


        progressBar = (ProgressBar) findViewById(R.id.red_progressBar);

        daftar = (Button) findViewById(R.id.reg_btn_register);
        daftar.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reg_btn_register:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String nnama = nama.getText().toString().trim();
        String nnik = nik.getText().toString().trim();
        String nemail = email.getText().toString().trim();
        String nusername = username.getText().toString().trim();
        String npassword = password.getText().toString().trim();
        String nalamat = alamat.getText().toString().trim();
        String njeniskelamin = jeniskelamin.getText().toString().trim();
        String nkelumpokusia = kelumpokusia.getText().toString().trim();
        String nstatuswarnanegara = statuswarganegara.getText().toString().trim();

        if (nnama.isEmpty()){
            nama.setError("Isi nama dengan benar");
            nama.requestFocus();
            return;
        }
        if (nnik.isEmpty()){
            nik.setError("Isi nik dengan benar");
            nik.requestFocus();
            return;
        }
        if (nusername.isEmpty()){
            username.setError("Isi username dengan benar");
            username.requestFocus();
            return;
        }
        if (nemail.isEmpty()){
            email.setError("Isi email dengan benar");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(nemail).matches()){
            email.setError("Format email salah");
            email.requestFocus();
            return;
        }

        if (npassword.isEmpty()){
            password.setError("Isi password dengan benar");
            password.requestFocus();
            return;
        }
        if (npassword.length() < 6 ){
            password.setError("Password harus berjumlah minimal 6 digit");
            password.requestFocus();
            return;
        }
        if (nalamat.isEmpty()){
            alamat.setError("Isi alamat dengan benar");
            alamat.requestFocus();
            return;
        }
        if (njeniskelamin.isEmpty()){
            jeniskelamin.setError("Pilih jenis kelamin");
            jeniskelamin.requestFocus();
            return;
        }
        if (nkelumpokusia.isEmpty()){
            kelumpokusia.setError("Pilih kelompok usia");
            kelumpokusia.requestFocus();
            return;
        }
        if (nstatuswarnanegara.isEmpty()){
            statuswarganegara.setError("Pilih status warganegara");
            statuswarganegara.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(nemail, npassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        User user = new User(nnama, nnik, nusername, nemail, nalamat, njeniskelamin, nkelumpokusia, nstatuswarnanegara);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(Register.this, "Berhasil mendaftar", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(Register.this, "Gagal mendaftar! Silahkan coba kembali", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                    else {
                        Toast.makeText(Register.this, "Gagal mendaftar! silahkan coba lagi!", Toast.LENGTH_LONG).show();
                    }
                    }

                });


    }
}
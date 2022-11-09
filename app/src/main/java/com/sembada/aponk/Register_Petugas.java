package com.sembada.aponk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.aponk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Petugas extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText nama, nip, nik, email, username, password, alamat;
    private ProgressBar progressBar;
    private Button daftar;
    private Spinner spiner, spiner2, spiner3, spiner4;
    private TextView upt, jeniskelamin, kelumpokusia, statuswarganegara;
    private String[] list = {"-Pilih-", "Laki-Laki", "Perempuan"};
    private String[] list2 = {"-Pilih-", "Anak-anak", "Dewasa", "Lansia"};
    private String[] list3 = {"-Pilih-", "WNI", "WNA"};
    private String[] list4 = {"-Pilih UPT-", "Lembaga Pemasyarakatan Kelas I Batu Nusakambangan", "Lembaga Pemasyarkatan Kelas IIA Besi Nusakambangan", "Lembaga Pemasyarakatan Narkotika Kelas IIA Nusakambangan", "Lembaga Pemasyarakatan Kelas IIA Kembangkuning", "Lembaga Pemasyarakatan Kelas IIA Permisan Nusakambangan", "Lembaga Pemasyarakatan Kelas IIA Pasir Putih Nusakambangan", "Lembaga Pemasyarakatan Khusu Kelas IIA Karanganyar", "Lembaga Pemasyarakatan Terbuka Nusakambangan", "Balai Pemasyarakatan Kelas II Nusakambangan"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_petugas);

        mAuth = FirebaseAuth.getInstance();

        nama = (EditText) findViewById(R.id.reg_peg_nama);
        nip = (EditText) findViewById(R.id.reg_peg_nip);
        upt = (TextView) findViewById(R.id.reg_peg_upt);
        nik = (EditText) findViewById(R.id.reg_peg_nik);
        email = (EditText) findViewById(R.id.reg_peg_email);
        username = (EditText) findViewById(R.id.reg_peg_username);
        password = (EditText) findViewById(R.id.reg_peg_password);
        alamat = (EditText) findViewById(R.id.reg_peg_alamat);
        jeniskelamin = (TextView) findViewById(R.id.reg_peg_jeniskelamin);
        kelumpokusia = (TextView) findViewById(R.id.reg_peg_kelompokusia);
        statuswarganegara = (TextView) findViewById(R.id.reg_peg_statuswarganegara);

        //Spiner 1
        spiner = (Spinner) findViewById(R.id.reg_peg_spiner);
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
        spiner2 = (Spinner) findViewById(R.id.reg_peg_spiner2);
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
        spiner3 = (Spinner) findViewById(R.id.reg_peg_spiner3);
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

        //Spiner 3
        spiner4 = (Spinner) findViewById(R.id.reg_peg_spiner4);
        spiner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                upt.setText(list4[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                upt.setText("");
            }
        });
        ArrayAdapter adapter4 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list4);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spiner4.setAdapter(adapter4);

        progressBar = (ProgressBar) findViewById(R.id.red_progressBar);

        daftar = (Button) findViewById(R.id.reg_peg_btn_register);
        daftar.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reg_peg_btn_register:
                registerUser();
                break;
        }



    }
    private void registerUser() {
        String nnama = nama.getText().toString().trim();
        String nnik = nik.getText().toString().trim();
        String nnip = nip.getText().toString().trim();
        String nupt = upt.getText().toString().trim();
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
        if (nnip.isEmpty()){
            nip.setError("Isi nip dengan benar");
            nip.requestFocus();
            return;
        }
        if (nnik.isEmpty()){
            nik.setError("Isi nik dengan benar");
            nik.requestFocus();
            return;
        }
        if (nupt.isEmpty()){
            upt.setError("Pilih UPT terlebih dahulu");
            upt.requestFocus();
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
                            User_Pegawai user = new User_Pegawai(nnama, nnip, nnik, nupt, nusername, nemail, nalamat, njeniskelamin, nkelumpokusia, nstatuswarnanegara);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(Register_Petugas.this, "Berhasil mendaftar", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                showDialog();
                                            }
                                            else {
                                                Toast.makeText(Register_Petugas.this, "Gagal mendaftar! Silahkan coba kembali", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                            FirebaseDatabase.getInstance().getReference("Users").child("Petugas")
                                    .child(nusername).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){


                                            }
                                            else {

                                            }
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(Register_Petugas.this, "Gagal mendaftar! silahkan coba lagi!", Toast.LENGTH_LONG).show();
                        }
                    }

                });


    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Terimakasih sudah mendaftar");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Kembali ke halaman login?")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        Intent i = new Intent(Register_Petugas.this, Login.class);
                        startActivity(i);
                        finish();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }
}

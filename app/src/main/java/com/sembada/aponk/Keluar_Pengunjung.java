package com.sembada.aponk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aponk.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Keluar_Pengunjung extends AppCompatActivity {

    private EditText noIdentitas;
    private Button keluar;
    private DatabaseReference database, database2, database3, database4;
    private TextView jk, us, sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keluar_pengunjung);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat forday = new SimpleDateFormat("dd");
        SimpleDateFormat formonth = new SimpleDateFormat("MMMM");
        SimpleDateFormat foryear = new SimpleDateFormat("yyyy");
        SimpleDateFormat fmtTgl = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat fmtJam = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String day = forday.format(calendar.getTime());
        String month = formonth.format(calendar.getTime());
        String year = foryear.format(calendar.getTime());
        String tgl = fmtTgl.format(calendar.getTime());

        noIdentitas = (EditText) findViewById(R.id.keluar_noidentitas);
        keluar = (Button) findViewById(R.id.bnt_keluar);


        database = FirebaseDatabase.getInstance().getReference().child("DataKeluar").child(year).child(month).child(day);
        database2 = FirebaseDatabase.getInstance().getReference().child("Users").child("DataPengunjung");
        database3 = FirebaseDatabase.getInstance().getReference().child("DataKeluar").child("DataBulanan").child(year).child(month);
        database4 = FirebaseDatabase.getInstance().getReference().child("DataKeluar").child("DataTahunan").child(year);

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Keluar_Pengunjung.this);
                alertDialogBuilder.setTitle("Keluar Pengunjung");

                alertDialogBuilder
                        .setMessage("Anda yakin akan keluar?")
                        .setIcon(R.mipmap.ic_launcher_round)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                data();
                                startActivity(new Intent(Keluar_Pengunjung.this, Login.class));
                                finish();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void data() {
        String nIdentitas = noIdentitas.getText().toString();
        database2.child(nIdentitas).child("jeniskelamin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    String a = snapshot.getValue(String.class);
                    database2.child(nIdentitas).child("kelompokusia").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){

                                String b = snapshot.getValue(String.class);
                                database2.child(nIdentitas).child("statuswarganegara").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){

                                            String c = snapshot.getValue(String.class);
                                            database.child("Pengunjung").push().child("data").setValue(nIdentitas);
                                            database.child(a).push().child("data").setValue(nIdentitas);
                                            database.child(b).push().child("data").setValue(nIdentitas);
                                            database.child(c).push().child("data").setValue(nIdentitas);

                                            //Bulanan
                                            database3.child("Pengunjung").push().child("data").setValue(nIdentitas);
                                            database3.child(a).push().child("data").setValue(nIdentitas);
                                            database3.child(b).push().child("data").setValue(nIdentitas);
                                            database3.child(c).push().child("data").setValue(nIdentitas);

                                            //Tahunan
                                            database4.child("Pengunjung").push().child("data").setValue(nIdentitas);
                                            database4.child(a).push().child("data").setValue(nIdentitas);
                                            database4.child(b).push().child("data").setValue(nIdentitas);
                                            database4.child(c).push().child("data").setValue(nIdentitas);
                                            Toast.makeText(Keluar_Pengunjung.this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(Keluar_Pengunjung.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else {
                                Toast.makeText(Keluar_Pengunjung.this,"Tidak ada data", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    Toast.makeText(Keluar_Pengunjung.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}
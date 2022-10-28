package com.example.aponk.Dashboard;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.aponk.DataMasuk;
import com.example.aponk.Login;
import com.example.aponk.R;
import com.example.aponk.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Dashboard extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;

    private TextView logout, petugas, penduduk, pengunjung, lakilaki, perempuan, anakanak, dewasa, lansia, wni, wna, namadata, statusdata, jammasuk, jamkeluar;

    private Button scan, keluar;

    //Sharepreference
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private DatabaseReference database, totalref, total, total1, total2;
    private  int totalvalue = 0;
    SimpleDateFormat tanggal;
    DatePickerDialog tglDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        database = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.dash_maps);
        mapFragment.getMapAsync(this);

        //SET
        petugas = (TextView) findViewById(R.id.dash_jumlahpetugas);
        penduduk = (TextView) findViewById(R.id.dash_jumlahpenduduk);
        pengunjung = (TextView) findViewById(R.id.dash_jumlahpengunjung);
        lakilaki = (TextView) findViewById(R.id.dash_jumlahlakilaki);
        perempuan = (TextView) findViewById(R.id.dash_jumlahperempuan);
        anakanak = (TextView) findViewById(R.id.dash_jumlahanakanak);
        dewasa = (TextView) findViewById(R.id.dash_jumlahdewasa);
        lansia = (TextView) findViewById(R.id.jumlah_lansia);
        wni = (TextView) findViewById(R.id.dash_jumlahwni);
        wna = (TextView) findViewById(R.id.dash_jumlahwna);
        namadata = (TextView) findViewById(R.id.dash_NamaData);
        statusdata = (TextView) findViewById(R.id.dash_status);
        jammasuk = (TextView) findViewById(R.id.dash_jammasuk);
        jamkeluar = (TextView) findViewById(R.id.dash_jamkeluar);


//        Toast.makeText(Dashboard.this, month, Toast.LENGTH_LONG).show();

        //Scan Barcode Program
        scan = (Button) findViewById(R.id.dash_masuk);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

        keluar = (Button) findViewById(R.id.dash_keluar);
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnKeluar();
                sdialog();
            }
        });




        //Logout Program
        logout = (TextView) findViewById(R.id.dash_btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Dashboard.this, Login.class));
            }
        });

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

        statusdata.setText(tgl);

        final SharedPreferences pref = getSharedPreferences("uploadData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();



        totalref = FirebaseDatabase.getInstance().getReference().child("DataHarian")
                .child(year).child(month).child(day);
        //Jumlah Petugas
        totalref.child("Petugas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    totalvalue = (int) snapshot.getChildrenCount();
                    petugas.setText(Integer.toString(totalvalue) + " Orang");
                }
                else
                {
                    petugas.setText("0 Orang");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Jumlah Penduduk
        totalref.child("Penduduk").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    totalvalue = (int) snapshot.getChildrenCount();
                    penduduk.setText(Integer.toString(totalvalue) + " Orang");
                }
                else
                {
                    penduduk.setText("0 Orang");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Jumlah Pengunjung
        totalref.child("Pengunjung").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    totalvalue = (int) snapshot.getChildrenCount();
                    pengunjung.setText(Integer.toString(totalvalue) + " Orang");
                }
                else
                {
                    pengunjung.setText("0 Orang");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Jumlah Laki-laki
        totalref.child("Laki-Laki").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalvalue = (int) snapshot.getChildrenCount();
                lakilaki.setText(Integer.toString(totalvalue));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                lakilaki.setText("0");
            }
        });

        //Jumlah Perempuan
        totalref.child("Perempuan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalvalue = (int) snapshot.getChildrenCount();
                perempuan.setText(Integer.toString(totalvalue));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                perempuan.setText("0");
            }
        });

        //Jumlah Anak-anak
        totalref.child("Anak-Anak").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalvalue = (int) snapshot.getChildrenCount();
                anakanak.setText(Integer.toString(totalvalue));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                anakanak.setText("0");
            }
        });

        //Jumlah Dewasa
        totalref.child("Laki-Laki").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalvalue = (int) snapshot.getChildrenCount();
                dewasa.setText(Integer.toString(totalvalue));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dewasa.setText("0");
            }
        });

        //Jumlah Lansia
        totalref.child("Lansia").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalvalue = (int) snapshot.getChildrenCount();
                lansia.setText(Integer.toString(totalvalue));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                lansia.setText("0");
            }
        });

        //Jumlah WNI
        totalref.child("WNI").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalvalue = (int) snapshot.getChildrenCount();
                wni.setText(Integer.toString(totalvalue));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                wni.setText("0");
            }
        });

        //Jumlah WNA
        totalref.child("WNA").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalvalue = (int) snapshot.getChildrenCount();
                wna.setText(Integer.toString(totalvalue));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                wna.setText("0");
            }
        });

        //Button Keluar






        //Get user by ID
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView username = (TextView) findViewById(R.id.dash_nama);
        final TextView username2 = (TextView) findViewById(R.id.dash_NamaData);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){
                    String nusername = userProfile.username;
                    username.setText(nusername);
                    String nnama = userProfile.nama;
                    username2.setText(nnama);


                    final SharedPreferences pref = getSharedPreferences("uploadData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                   // Toast.makeText(Dashboard.this, "Anda masuk sebagai " + userProfile.nama, Toast.LENGTH_LONG).show();


                    editor.putString("nama", userProfile.nama);
                    editor.putString("nik", userProfile.nik);
                    editor.putString("username", userProfile.username);
                    editor.putString("email", userProfile.email);
                    editor.putString("alamat", userProfile.alamat);
                    editor.putString("jeniskelamin", userProfile.jeniskelamin);
                    editor.putString("kelompokusia", userProfile.kelompokusia);
                    editor.putString("statuswarganegara", userProfile.statuswarganegara);
                    editor.apply();


                    database.child("DataMasuk").child(userProfile.username).child(tgl).child("masuk").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                jammasuk.setText(snapshot.getValue(String.class));
                            }
                            else {
                                jammasuk.setText("Belum Masuk");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    database.child("DataMasuk").child(userProfile.username).child(tgl).child("keluar").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                jamkeluar.setText(snapshot.getValue(String.class));
                            }
                            else {
                                jamkeluar.setText("Belum Masuk");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dashboard.this, "Ada kesalahan laporkan kepada admin", Toast.LENGTH_LONG).show();
            }
        });
        String nusername = pref.getString("username", null);



    }

    private void sdialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setTitle("Anda Sudah Keluar");

        alertBuilder.setMessage("Terimakasih").setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }


    //Menampilkan Maps
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LatLng Nusakambangan = new LatLng(-7.742166, 108.968880);
        map.addMarker(new MarkerOptions().position(Nusakambangan).title("Nusakambangan"));
        map.moveCamera(CameraUpdateFactory.newLatLng(Nusakambangan));
    }

    private void btnKeluar() {
        final SharedPreferences pref = getSharedPreferences("uploadData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String nusername = pref.getString("username", null);
        database = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
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
        String jam = fmtJam.format(calendar.getTime());

        database.child("DataMasuk").child(nusername).child(tgl).child("keluar").setValue(jam);

    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Tekan volume atas untuk menyalakan flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {

        if(result.getContents() !=null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
            builder.setTitle("Hasil");
            builder.setMessage("Anda akan masuk sebagai " + result.getContents());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sharedPreferences = getSharedPreferences("uploadData", Context.MODE_PRIVATE);

                    Toast.makeText(Dashboard.this, "Anda masuk sebagai " + result.getContents(), Toast.LENGTH_LONG).show();
                    database = FirebaseDatabase.getInstance().getReference();
                    FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
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
                    String jam = fmtJam.format(calendar.getTime());

                    final SharedPreferences pref = getSharedPreferences("uploadData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    String nnama = pref.getString("username", null);
                    String nnik = pref.getString("nik", null);
                    String nusername = pref.getString("username", null);
                    String nemail = pref.getString("email", null);
                    String nalamat = pref.getString("ualamat", null);
                    String njeniskelamin = pref.getString("jeniskelamin", null);
                    String nkelumpokusia = pref.getString("kelompokusia", null);
                    String nstatuswarnanegara = pref.getString("statuswarganegara", null);
                    User user = new User(nnama, nnik, nusername, nemail, nalamat, njeniskelamin, nkelumpokusia, nstatuswarnanegara);

                    database.child("DataHarian").child(year).child(month).child(day).child(result.getContents())
                            .push().child("username")
                                    .setValue(user);
                    database.child("DataHarian").child(year).child(month).child(day).child(njeniskelamin).push().child("username").setValue(user);
                    database.child("DataHarian").child(year).child(month).child(day).child(nkelumpokusia).push().child("username").setValue(user);
                    database.child("DataHarian").child(year).child(month).child(day).child(nstatuswarnanegara).push().child("username").setValue(user);
                    database.child("DataMasuk").child(nusername).child(tgl).child("masuk").setValue(jam);


                    dialog.dismiss();
                }
            }).show();
        }
    });



}
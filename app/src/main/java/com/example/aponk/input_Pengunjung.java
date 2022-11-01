package com.example.aponk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class input_Pengunjung extends AppCompatActivity {

    ImageView captureimage;
    private static final int CAMERA_REQUEST = 100;
    Uri imageUri;

    private EditText nama, alamat, identitas, keperluan;
    private TextView instansi, kewarganegaraan, jeniskelamin, kelompokusia;

    private Spinner spinner, spinner2, spinner3, spinner4;
    private String[] list = {"-Pilih-", "TNI/POLRI", "PNS", "Umum"};
    private String[] list2 = {"-Pilih-", "WNI", "WNA"};
    private String[] list3 = {"-Pilih-", "Laki-laki", "Perempuan"};
    private String[] list4 = {"-Pilih-", "Anak-anak", "Dewasa", "Lansia"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pengunjung);

        nama = (EditText) findViewById(R.id.pengujung_nama);
        alamat = (EditText) findViewById(R.id.pengujung_alamat);
        identitas = (EditText) findViewById(R.id.pengunjung_nik);
        keperluan = (EditText) findViewById(R.id.pengunjung_keperluan);

        instansi = (TextView) findViewById(R.id.pengunjung_instasi);
        kewarganegaraan = (TextView) findViewById(R.id.pengujung_statuswarganegara);
        jeniskelamin = (TextView) findViewById(R.id.pengunjung_jeniskelamin);
        kelompokusia = (TextView) findViewById(R.id.pengunjung_kelompokusia);

        spinner = (Spinner) findViewById(R.id.pengujung_spiner1i);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                instansi.setText(list[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                instansi.setText("");
            }
        });
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        spinner2 = (Spinner) findViewById(R.id.pengunjung_spiner3);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                instansi.setText(list2[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                instansi.setText("");
            }
        });
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list2);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(adapter2);

        spinner3 = (Spinner) findViewById(R.id.pengunjung_spiner);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jeniskelamin.setText(list3[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                jeniskelamin.setText("");
            }
        });
        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list3);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner3.setAdapter(adapter3);

        spinner4 = (Spinner) findViewById(R.id.pengunjung_spiner2);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kelompokusia.setText(list4[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                kelompokusia.setText("");
            }
        });
        ArrayAdapter adapter4 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list4);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner4.setAdapter(adapter4);


        captureimage = (ImageView) this.findViewById(R.id.click_image);
        Button buttoncamera = (Button) findViewById(R.id.btn_camera);
        Button buttonmasuk = (Button) findViewById(R.id.pengunjung_btn_register);

        if (ContextCompat.checkSelfPermission(input_Pengunjung.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(input_Pengunjung.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        buttoncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 100);
            }
        });

        buttonmasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureimage.buildDrawingCache();
                Bitmap bmap = captureimage.getDrawingCache();

                imageUri = getImageUri(getApplicationContext(), bmap);
                setToFireStore(imageUri);


            }
        });
    }

    private void takeImageFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            captureimage.setImageBitmap(photo);
           // imageUri = getImageUri(getApplicationContext(), photo);
           // File finalfile = new File(getRealPathFromURI(imageUri));
        //    byte[] bitmapBytes = getBytesFromBitmap(bitmap, 100);
           // setToFireStore(imageUri);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private void setToFireStore(Uri imageUri){

        String nnama = nama.getText().toString().trim();
        if (nnama.isEmpty()){
            nama.setError("Masukkan nama terlebih dahulu");
            nama.requestFocus();
            return;
        }
        String nalamat = alamat.getText().toString().trim();
        if (nalamat.isEmpty()){
            alamat.setError("Isi alamat terlebih dahulu");
            alamat.requestFocus();
            return;
        }
        String ninstansi = instansi.getText().toString().trim();
        if (ninstansi.isEmpty()){
            instansi.setError("Pilih instansi terlebih dahulu");
            instansi.requestFocus();
            return;
        }
        String nindentitas = instansi.getText().toString().trim();
        if (nindentitas.isEmpty()){
            identitas.setError("Masukkan nomor identitas terlebih dahulu");
            identitas.requestFocus();
            return;
        }
        String nkewarganegaraan = kewarganegaraan.getText().toString().trim();
        if (nkewarganegaraan.isEmpty()){
            kewarganegaraan.setError("Pilih kewarganegaraan");
            kewarganegaraan.requestFocus();
            return;
        }
        String njeniskelamin = jeniskelamin.getText().toString().trim();
        if (njeniskelamin.isEmpty()){
            jeniskelamin.setError("Pilih jenis kelamin");
            jeniskelamin.requestFocus();
            return;
        }
        String nkelompokusia = kelompokusia.getText().toString().trim();
        if (nkelompokusia.isEmpty()){
            kelompokusia.setError("Pilih kelompok usia");
            kelompokusia.requestFocus();
            return;
        }
        String nkeperluan = keperluan.getText().toString().trim();
        if(nkeperluan.isEmpty()){
            keperluan.setError("Masukkan keperluan");
            keperluan.requestFocus();
            return;

        }

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



        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ImageFolder");
        final StorageReference ImageReference = storageReference.child("112233");
        ImageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ImageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("riddle_game").child("myImages").child("user1");
                        db.child("Pengunjung").setValue(uri.toString());

                        Toast.makeText(input_Pengunjung.this, "Successfully added to real time", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(input_Pengunjung.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(input_Pengunjung.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
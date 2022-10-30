package com.example.aponk;

public class User_Pegawai {

    public String nama, nip, nik, upt, username, email, password, alamat, jeniskelamin, kelompokusia, statuswarganegara;

    public User_Pegawai (){

    }

    public User_Pegawai(String nama, String nip, String nik, String upt, String username, String email, String alamat, String jeniskelamin, String kelompokusia, String statuswarganegara) {
        this.nama = nama;
        this.nip = nip;
        this.nik = nik;
        this.upt = upt;
        this.username = username;
        this.email = email;
        this.alamat = alamat;
        this.jeniskelamin = jeniskelamin;
        this.kelompokusia = kelompokusia;
        this.statuswarganegara = statuswarganegara;
    }
}

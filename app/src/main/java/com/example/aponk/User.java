package com.example.aponk;

public class User {

    public String nama, nik, username, email, password, alamat, jeniskelamin, kelompokusia, statuswarganegara;

    public User (){

    }

    public User(String nama, String nik, String username, String email, String alamat, String jeniskelamin, String kelompokusia, String statuswarganegara) {
        this.nama = nama;
        this.nik = nik;
        this.username = username;
        this.email = email;
        this.alamat = alamat;
        this.jeniskelamin = jeniskelamin;
        this.kelompokusia = kelompokusia;
        this.statuswarganegara = statuswarganegara;
    }

}

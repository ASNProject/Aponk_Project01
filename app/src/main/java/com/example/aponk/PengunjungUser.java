package com.example.aponk;

public class PengunjungUser {
    public String nama, alamat, instansi, identitas, kewarganegaraan, jeniskelamin, kelompokusia, keperluan;

    public PengunjungUser(){

    }

    public PengunjungUser(String nama, String alamat, String instansi, String identitas, String kewarganegaraan, String jeniskelamin, String kelompokusia, String keperluan){
        this.nama = nama;
        this.alamat = alamat;
        this.instansi = instansi;
        this.identitas = identitas;
        this.kewarganegaraan = kewarganegaraan;
        this.jeniskelamin = jeniskelamin;
        this.kelompokusia = kelompokusia;
        this.keperluan = keperluan;
    }
}

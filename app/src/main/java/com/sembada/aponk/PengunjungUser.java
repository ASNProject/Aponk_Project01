package com.sembada.aponk;

public class PengunjungUser {
    public String nama, alamat, instansi, identitas, jeniskelamin, kelompokusia, statuswarganegara, keperluan;

    public PengunjungUser() {

    }

    public PengunjungUser(String nama, String alamat, String instansi, String identitas, String jeniskelamin, String kelompokusia, String statuswarganegara, String keperluan) {
        this.nama = nama;
        this.alamat = alamat;
        this.instansi = instansi;
        this.identitas = identitas;
        this.jeniskelamin = jeniskelamin;
        this.kelompokusia = kelompokusia;
        this.statuswarganegara = statuswarganegara;
        this.keperluan = keperluan;
    }
}

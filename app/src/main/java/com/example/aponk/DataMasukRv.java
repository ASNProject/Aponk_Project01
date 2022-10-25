package com.example.aponk;

public class DataMasukRv {
    private String tanggal;
    private String masuk;
    private String status;
    private String keluar;

    public DataMasukRv(){

    }

    public DataMasukRv(String tanggal, String masuk, String status,
                       String keluar){
        this.tanggal = tanggal;
        this.masuk = masuk;
        this.status = status;
        this.keluar = keluar;
    }

    public String getTanggal(){
        return tanggal;
    }

    public void setTanggal(String tanggal){
        this.tanggal = tanggal;
    }

    public String getMasuk(){
        return masuk;
    }

    public void setMasuk(String masuk){
        this.masuk = masuk;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getKeluar(){
        return keluar;
    }

    public void setKeluar(String keluar){
        this.keluar = keluar;
    }
}

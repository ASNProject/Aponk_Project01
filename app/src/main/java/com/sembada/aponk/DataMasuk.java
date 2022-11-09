package com.sembada.aponk;

public class DataMasuk {
    private String waktu, status;

    public DataMasuk(){

    }

    public DataMasuk(String waktu, String status){
        this.waktu = waktu;
        this.status = status;
    }

    public String getWaktu(){
        return waktu;
    }

    public void setWaktu(String waktu){
        this.waktu = waktu;
    }

    public String getStatus(){
        return status;
    }

    public  void setStatus(String status){
        this.status = status;
    }
}

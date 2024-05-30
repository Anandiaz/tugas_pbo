package com.peliharaan;

public class Hewan {
    private String nama, ras, kelompok;
    private int umur;

    public Hewan (String nama, String kelompok, String ras, int umur){
        this.nama = nama;
        this.kelompok = kelompok;
        this.ras = ras;
        this.umur = umur;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
    public String getNama() {
        return nama;
    }
    public void setKelompok(String kelompok) {
        this.kelompok = kelompok;
    }
    public String getKelompok() {
        return kelompok;
    }
    public void setRas(String ras) {
        this.ras = ras;
    }
    public String getRas() {
        return ras;
    }
    public void setUmur(int umur) {
        this.umur = umur;
    }
    public int getUmur() {
        return umur;
    }
}

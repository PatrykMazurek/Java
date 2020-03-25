package com.company;

import java.io.Serializable;

public class Osoba implements Serializable {

    String imie;
    String nazwisko;
    Adres adresOsoby;

    public Osoba(String imieN, String nazwiskoN, Adres adresZameldowania){
        this.imie = imieN;
        this.nazwisko = nazwiskoN;
        this.adresOsoby = adresZameldowania;
        System.out.println("Dodano nową osobę");
    }

    @Override
    public String toString() {
        return "Adres z pamięci: " + super.toString() + "\n"+
                "Imie " + this.imie + " Nazwisko " + this.nazwisko + "\n"+
                "Adres " + this.adresOsoby.toString();
    }
}

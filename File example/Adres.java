package com.company;

import java.io.Serializable;

public class Adres implements Serializable {

    String ulicz;
    int numerBudyknu;
    int numerLok;
    String miasto;
    String kodPocztowy;

    public Adres(String ulicaN, int budynekNr, int lokal, String miastoN, String poczta){
        this.ulicz = ulicaN;
        this.numerBudyknu = budynekNr;
        this.numerLok = lokal;
        this.miasto = miastoN;
        this.kodPocztowy = poczta;

    }

    @Override
    public String toString() {
        return "Adres w pamięci:" + super.toString() + "\n"+
                "Ulica " + this.ulicz + " nr / lok " + this.numerBudyknu + " / " + this.numerBudyknu + "\n"+
                "Miasto " + this.miasto + " Kod pocztowy " + this.kodPocztowy;
    }
}

package com.pao.laboratory06.exercise3;

public abstract class Persoana {
    protected String nume;
    protected String prenume;
    protected String telefon; // Poate fi null sau gol

    public Persoana(String nume, String prenume, String telefon) {
        this.nume = nume;
        this.prenume = prenume;
        this.telefon = telefon;
    }
}
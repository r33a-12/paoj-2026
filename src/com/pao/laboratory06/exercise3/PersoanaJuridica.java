package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.List;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS {
    private List<String> smsTrimise = new ArrayList<>();
    private double sold = 100000.0;

    public PersoanaJuridica(String nume, String telefon) {
        super(nume, "SRL", telefon);
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || parola == null) throw new IllegalArgumentException();
        System.out.println("Compania " + nume + " s-a autentificat.");
    }

    @Override
    public double consultareSold() { return sold; }

    @Override
    public boolean efectuarePlata(double suma) {
        if (sold >= suma) { sold -= suma; return true; }
        return false;
    }

    @Override
    public boolean trimiteSMS(String mesaj) {
        if (mesaj == null || mesaj.isEmpty()) return false;
        if (telefon == null || telefon.isEmpty()) return false;

        smsTrimise.add(mesaj);
        System.out.println("SMS trimis de " + nume + ": " + mesaj);
        return true;
    }

    public List<String> getSmsTrimise() { return smsTrimise; }
}
package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends Colaborator implements PersoanaFizica {
    private double cheltuieliLunare;
    private static final double SALARIU_MINIM = 4050;

    @Override
    public void citeste(Scanner in) {
        super.citeste(in);
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNetInitial = (venitBrutLunar - cheltuieliLunare) * 12;
        double salMin = 3000;

        double impozit = venitNetInitial * 0.10;

        double cass = venitNetInitial * 0.10;

        double cas = 0;
        if (venitNetInitial >= 24 * salMin) {
            cas = 0.08 * venitNetInitial;
        } else {
            cas = 14400;
        }

        return venitNetInitial - impozit - cass - cas;
    }

    @Override
    public String tipContract() { return "PFA"; }
}
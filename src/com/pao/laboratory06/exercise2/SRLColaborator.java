package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator extends Colaborator implements PersoanaJuridica {
    private double cheltuieliLunare;

    @Override
    public void citeste(Scanner in) {
        super.citeste(in);
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double profitAnual = (venitBrutLunar - cheltuieliLunare) * 12;
        double coeficient = (profitAnual > 125000) ? 0.80 : 0.84;
        return profitAnual * coeficient;
    }
    @Override
    public String tipContract() { return "SRL"; }
}
package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends Colaborator implements PersoanaFizica {
    private boolean bonus;

    @Override
    public void citeste(Scanner in) {
        super.citeste(in);
        if (in.hasNext()) {
            String b = in.next();
            this.bonus = b.equalsIgnoreCase("DA");
        }
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double baza = venitBrutLunar * 12 * 0.55;
        return bonus ? baza * 1.10 : baza;
    }

    @Override
    public String tipContract() { return "CIM"; }
    @Override
    public boolean areBonus() { return bonus; }
}
package com.pao.laboratory06.exercise2;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in).useLocale(Locale.US);
        if (!sc.hasNextInt()) return;
        int n = sc.nextInt();

        List<Colaborator> lista = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String tipStr = sc.next();
            Colaborator c = switch (tipStr) {
                case "CIM" -> new CIMColaborator();
                case "PFA" -> new PFAColaborator();
                case "SRL" -> new SRLColaborator();
                default -> null;
            };
            if (c != null) {
                c.citeste(sc);
                lista.add(c);
            }
        }

        for (Colaborator c : lista) {
            c.afiseaza();
        }
        System.out.println();

        Colaborator max = lista.stream()
                .max(Comparator.comparingDouble(Colaborator::calculeazaVenitNetAnual))
                .orElse(null);
        if (max != null) {
            System.out.print("Colaborator cu venit net maxim: ");
            max.afiseaza();
        }
        System.out.println();

        System.out.println("Colaboratori persoane juridice:");
        for (Colaborator c : lista) {
            if (c instanceof PersoanaJuridica) {
                c.afiseaza();
            }
        }
        System.out.println();

        System.out.println("Sume și număr colaboratori pe tip:");
        for (TipColaborator t : TipColaborator.values()) {
            long count = lista.stream().filter(c -> c.tipContract().equals(t.name())).count();
            if (count > 0) {
                double suma = lista.stream()
                        .filter(c -> c.tipContract().equals(t.name()))
                        .mapToDouble(Colaborator::calculeazaVenitNetAnual).sum();
                System.out.printf("%s: suma = %.2f lei, număr = %d%n", t.name(), suma, (int)count);
            }
        }
    }
}
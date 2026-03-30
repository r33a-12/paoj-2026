package com.pao.laboratory06.exercise1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        if (!scanner.hasNext()) return;
        String optiune = scanner.next();

        if (!scanner.hasNextInt()) return;
        int numarAngajati = scanner.nextInt();

        Angajat[] angajati = new Angajat[numarAngajati];
        for (int i = 0; i < numarAngajati; i++) {
            angajati[i] = Angajat.citeste(scanner);
        }

        Comparator<Angajat> comparator = switch (optiune) {
            case "by_name" -> Comparator.comparing(Angajat::getNume);
            case "by_salary" -> Comparator.naturalOrder(); // Folosește Comparable din clasa Angajat
            case "by_salary_desc" -> Comparator.comparingDouble(Angajat::getSalariu).reversed();
            default -> null;
        };

        if (comparator != null) {
            Arrays.sort(angajati, comparator);
        }

        for (Angajat angajat : angajati) {
            System.out.println(angajat);
        }
    }
}
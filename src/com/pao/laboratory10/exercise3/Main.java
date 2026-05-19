package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Tranzactie> tranzactii = Arrays.asList(
            new Tranzactie(1, 150.0, "2024-01-10", TipTranzactie.CREDIT, "RO12INGB0001"),
            new Tranzactie(2, 50.0, "2024-01-15", TipTranzactie.DEBIT, "RO12INGB0001"),
            new Tranzactie(3, 2000.0, "2024-01-22", TipTranzactie.CREDIT, "RO99BCR0002"),
            new Tranzactie(4, 100.0, "2024-02-05", TipTranzactie.DEBIT, "RO12INGB0001"),
            new Tranzactie(5, 500.0, "2024-02-14", TipTranzactie.CREDIT, "RO88BT0003"),
            new Tranzactie(6, 120.0, "2024-02-28", TipTranzactie.DEBIT, "RO99BCR0002"),
            new Tranzactie(7, 300.0, "2024-03-01", TipTranzactie.DEBIT, "RO12INGB0001"),
            new Tranzactie(8, 4000.0, "2024-03-10", TipTranzactie.CREDIT, "RO12INGB0001"),
            new Tranzactie(9, 75.5, "2024-03-15", TipTranzactie.DEBIT, "RO88BT0003"),
            new Tranzactie(10, 20.0, "2024-03-20", TipTranzactie.DEBIT, "RO88BT0003")
        );

        System.out.println("=== 1. Filtrare (tip == CREDIT) ===");
        tranzactii.stream()
            .filter(t -> t.getTip() == TipTranzactie.CREDIT)
            .forEach(System.out::println);
            
        System.out.println("\n=== 2. Suma totala procesata ===");
        double sumTotal = tranzactii.stream()
            .mapToDouble(Tranzactie::getSuma)
            .sum();
        System.out.printf(Locale.US, "Total procesat: %.2f RON%n", sumTotal);

        System.out.println("\n=== 3. Suma per luna ===");
        Map<String, Double> sumaPeLuna = tranzactii.stream()
            .collect(Collectors.groupingBy(
                t -> t.getData().substring(0, 7),
                TreeMap::new,
                Collectors.summingDouble(Tranzactie::getSuma)
            ));
        sumaPeLuna.forEach((luna, suma) -> 
            System.out.printf(Locale.US, "%s: %.2f RON%n", luna, suma)
        );

        System.out.println("\n=== 4. Top 3 tranzactii (dupa suma descrescator) ===");
        System.out.println("Top 3 tranzactii:");
        tranzactii.stream()
            .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
            .limit(3)
            .forEach(System.out::println);

        System.out.println("\n=== 5. Conturi sursa unice ===");
        List<String> conturi = tranzactii.stream()
            .map(Tranzactie::getContSursa)
            .distinct()
            .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturi);

        System.out.println("\n=== 6. Suma medie ===");
        double medie = tranzactii.stream()
            .mapToDouble(Tranzactie::getSuma)
            .average()
            .orElse(0.0);
        System.out.printf(Locale.US, "Suma medie: %.2f RON%n", medie);

        System.out.println("\n=== 7. Extras de cont per luna ===");
        Map<String, List<Tranzactie>> tranzactiiPeLuna = tranzactii.stream()
            .collect(Collectors.groupingBy(
                t -> t.getData().substring(0, 7),
                TreeMap::new,
                Collectors.toList()
            ));
        tranzactiiPeLuna.forEach((luna, listaT) -> {
            double total = listaT.stream().mapToDouble(Tranzactie::getSuma).sum();
            System.out.printf(Locale.US, "EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                luna, listaT.size(), total);
        });
    }
}

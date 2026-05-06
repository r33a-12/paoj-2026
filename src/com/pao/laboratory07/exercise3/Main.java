package com.pao.laboratory07.exercise3;

import com.pao.laboratory07.exercise2.Comanda;
import com.pao.laboratory07.exercise2.ComandaGratuita;
import com.pao.laboratory07.exercise2.ComandaRedusa;
import com.pao.laboratory07.exercise2.ComandaStandard;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) return;

        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String line = sc.nextLine().trim();
            String[] tokens = line.split(" ");
            Comanda c = null;
            if (tokens[0].equals("STANDARD")) {
                String nume = tokens[1];
                double pret = Double.parseDouble(tokens[2]);
                String client = tokens[3];
                c = new ComandaStandard(nume, pret);
                c.setClient(client);
            } else if (tokens[0].equals("DISCOUNTED")) {
                String nume = tokens[1];
                double pret = Double.parseDouble(tokens[2]);
                int discount = Integer.parseInt(tokens[3]);
                String client = tokens[4];
                c = new ComandaRedusa(nume, pret, discount);
                c.setClient(client);
            } else if (tokens[0].equals("GIFT")) {
                String nume = tokens[1];
                String client = tokens[2];
                c = new ComandaGratuita(nume);
                c.setClient(client);
            }
            if (c != null) {
                comenzi.add(c);
                System.out.println(c.descriere() + " - client: " + c.getClient());
            }
        }

        System.out.println();

        while (sc.hasNextLine()) {
            String commandLine = sc.nextLine().trim();
            if (commandLine.isEmpty()) continue;

            String[] cmdTokens = commandLine.split(" ");
            String cmd = cmdTokens[0];

            if (cmd.equals("QUIT")) {
                break;
            } else if (cmd.equals("STATS")) {
                System.out.println("--- STATS ---");
                Map<Class<? extends Comanda>, Double> averages = comenzi.stream()
                        .collect(Collectors.groupingBy(Comanda::getClass, Collectors.averagingDouble(Comanda::pretFinal)));

                double avgStandard = averages.getOrDefault(ComandaStandard.class, 0.0);
                double avgDiscounted = averages.getOrDefault(ComandaRedusa.class, 0.0);
                double avgGift = averages.getOrDefault(ComandaGratuita.class, 0.0);

                System.out.printf(Locale.US, "STANDARD: medie = %.2f lei\n", avgStandard);
                System.out.printf(Locale.US, "DISCOUNTED: medie = %.2f lei\n", avgDiscounted);
                System.out.printf(Locale.US, "GIFT: medie = %.2f lei\n", avgGift);
                System.out.println();
            } else if (cmd.equals("FILTER")) {
                double threshold = Double.parseDouble(cmdTokens[1]);
                System.out.printf(Locale.US, "--- FILTER (>= %.2f) ---\n", threshold);

                comenzi.stream()
                        .filter(c -> c.pretFinal() >= threshold)
                        .forEach(c -> {
                            System.out.println(c.descriere().replaceAll(" \\[PLACED\\]", "") + " - client: " + c.getClient());
                        });
                System.out.println();
            } else if (cmd.equals("SORT")) {
                System.out.println("--- SORT (by client, then by pret) ---");

                comenzi.stream()
                        .sorted(Comparator.comparing(Comanda::getClient).thenComparing(Comanda::pretFinal))
                        .forEach(c -> {
                            System.out.println(c.descriere().replaceAll(" \\[PLACED\\]", "") + " - client: " + c.getClient());
                        });
                System.out.println();
            } else if (cmd.equals("SPECIAL")) {
                System.out.println("--- SPECIAL (discount > 15%) ---");

                comenzi.stream()
                        .filter(c -> c instanceof ComandaRedusa)
                        .map(c -> (ComandaRedusa) c)
                        .filter(c -> c.getDiscountProcent() > 15)
                        .forEach(c -> {
                            System.out.println(c.descriere().replaceAll(" \\[PLACED\\]", "") + " - client: " + c.getClient());
                        });
                System.out.println();
            }
        }
    }
}

package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();
        List<Tranzactie> lista = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = Double.parseDouble(scanner.next());
            String data = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
            lista.add(new Tranzactie(id, suma, data, tip));
        }

        while (scanner.hasNext()) {
            String cmd = scanner.next();
            if (cmd.equals("UNIQUE_IDS")) {
                LinkedHashSet<Integer> ids = new LinkedHashSet<>();
                for (Tranzactie t : lista) ids.add(t.getId());
                System.out.println("IDs unice (" + ids.size() + "): " + ids);
            } else if (cmd.equals("MONTHLY_REPORT")) {
                TreeMap<String, double[]> report = new TreeMap<>();
                for (Tranzactie t : lista) {
                    String month = t.getData().substring(0, 7);
                    report.putIfAbsent(month, new double[2]);
                    if (t.getTip() == TipTranzactie.CREDIT) {
                        report.get(month)[0] += t.getSuma();
                    } else {
                        report.get(month)[1] += t.getSuma();
                    }
                }
                for (Map.Entry<String, double[]> entry : report.entrySet()) {
                    System.out.printf(Locale.US, "%s: CREDIT %.2f RON, DEBIT %.2f RON%n", 
                        entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
                }
            } else if (cmd.equals("TOP")) {
                int count = scanner.nextInt();
                List<Tranzactie> copy = new ArrayList<>(lista);
                copy.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                System.out.println("Top " + count + ":");
                for (int i = 0; i < Math.min(count, copy.size()); i++) {
                    System.out.println(copy.get(i));
                }
            } else if (cmd.equals("SORT_ASC")) {
                lista.sort(Comparator.comparingDouble(Tranzactie::getSuma));
                for (Tranzactie t : lista) System.out.println(t);
            } else if (cmd.equals("SORT_DESC")) {
                lista.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                for (Tranzactie t : lista) System.out.println(t);
            } else if (cmd.equals("REVERSE")) {
                Collections.reverse(lista);
                for (Tranzactie t : lista) System.out.println(t);
            } else if (cmd.equals("MIN_MAX")) {
                if (!lista.isEmpty()) {
                    Tranzactie min = Collections.min(lista, Comparator.comparingDouble(Tranzactie::getSuma));
                    Tranzactie max = Collections.max(lista, Comparator.comparingDouble(Tranzactie::getSuma));
                    System.out.println("MIN: " + min);
                    System.out.println("MAX: " + max);
                }
            } else if (cmd.equals("CME_DEMO")) {
                try {
                    for (Tranzactie t : lista) {
                        lista.remove(t);
                    }
                } catch (ConcurrentModificationException e) {
                    System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                }
            }
        }
    }
}

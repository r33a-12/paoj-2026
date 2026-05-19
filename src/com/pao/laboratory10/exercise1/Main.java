package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LinkedList<Tranzactie> coada = new LinkedList<>();

        while (scanner.hasNext()) {
            String cmd = scanner.next();
            if (cmd.equals("ENQUEUE")) {
                int id = Integer.parseInt(scanner.next());
                double suma = Double.parseDouble(scanner.next());
                String data = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
                coada.addLast(new Tranzactie(id, suma, data, tip));
            } else if (cmd.equals("DEQUEUE")) {
                if (coada.isEmpty()) {
                    System.out.println("Coada goala.");
                } else {
                    System.out.println("Procesat: " + coada.removeFirst());
                }
            } else if (cmd.equals("PUSH")) {
                int id = Integer.parseInt(scanner.next());
                double suma = Double.parseDouble(scanner.next());
                String data = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
                coada.addFirst(new Tranzactie(id, suma, data, tip));
            } else if (cmd.equals("POP")) {
                if (coada.isEmpty()) {
                    System.out.println("Coada goala.");
                } else {
                    System.out.println("Extras: " + coada.removeFirst());
                }
            } else if (cmd.equals("REMOVE_DEBIT")) {
                Iterator<Tranzactie> itr = coada.iterator();
                int count = 0;
                while (itr.hasNext()) {
                    if (itr.next().getTip() == TipTranzactie.DEBIT) {
                        itr.remove();
                        count++;
                    }
                }
                System.out.println("Eliminat " + count + " tranzactii DEBIT.");
            } else if (cmd.equals("REMOVE_BELOW")) {
                double threshold = Double.parseDouble(scanner.next());
                Iterator<Tranzactie> itr = coada.iterator();
                int count = 0;
                while (itr.hasNext()) {
                    if (itr.next().getSuma() < threshold) {
                        itr.remove();
                        count++;
                    }
                }
                System.out.printf(Locale.US, "Eliminat %d tranzactii sub %.2f RON.%n", count, threshold);
            } else if (cmd.equals("PRINT")) {
                for (Tranzactie t : coada) {
                    System.out.println(t);
                }
            } else if (cmd.equals("SIZE")) {
                System.out.println("Dimensiune coada: " + coada.size());
            }
        }
    }
}

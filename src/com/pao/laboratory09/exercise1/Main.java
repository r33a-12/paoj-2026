package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);
        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();
        
        List<Tranzactie> tranzactii = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            String contSursa = scanner.next();
            String contDestinatie = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
            Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            t.note = "procesat";
            tranzactii.add(t);
        }
        
        File outFile = new File(OUTPUT_FILE);
        outFile.getParentFile().mkdirs();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outFile))) {
            oos.writeObject(tranzactii);
        }
        
        List<Tranzactie> readTranzactii;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(outFile))) {
            readTranzactii = (List<Tranzactie>) ois.readObject();
        }
        
        while (scanner.hasNext()) {
            String command = scanner.next();
            if (command.equals("LIST")) {
                for (Tranzactie t : readTranzactii) {
                    System.out.printf(Locale.US, "[%d] %s %s: %.2f RON | %s -> %s\n", 
                            t.id, t.data, t.tip, t.suma, t.contSursa, t.contDestinatie);
                }
            } else if (command.equals("FILTER")) {
                String prefix = scanner.next();
                boolean found = false;
                for (Tranzactie t : readTranzactii) {
                    if (t.data.startsWith(prefix)) {
                        System.out.printf(Locale.US, "[%d] %s %s: %.2f RON | %s -> %s\n", 
                                t.id, t.data, t.tip, t.suma, t.contSursa, t.contDestinatie);
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("Niciun rezultat.");
                }
            } else if (command.equals("NOTE")) {
                int id = scanner.nextInt();
                boolean found = false;
                for (Tranzactie t : readTranzactii) {
                    if (t.id == id) {
                        System.out.println("NOTE[" + id + "]: " + t.note);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("NOTE[" + id + "]: not found");
                }
            }
        }
    }
}

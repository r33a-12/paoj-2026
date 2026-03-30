package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== DEMONSTRAȚIE PLATFORMĂ PLĂȚI ONLINE ===\n");

        System.out.println(">>> 1. Constante Financiare:");
        System.out.println("Valoare TVA: " + (ConstanteFinanciare.TVA.getValoare() * 100) + "%");
        System.out.println("Salariu Minim: " + ConstanteFinanciare.SALARIU_MINIM.getValoare() + " lei");
        System.out.println();

        Inginer[] ingineri = {
                new Inginer("Ionescu", "Ana", "0721111222", 8500.0),
                new Inginer("Popescu", "Vlad", "0744333444", 9500.0),
                new Inginer("Ababei", "Costel", "0755000111", 7200.0)
        };

        System.out.println(">>> 2. Sortare Ingineri:");
        System.out.println("-- Înainte de sortare --");
        afiseazaIngineri(ingineri);

        System.out.println("\n-- Sortare Naturală (alfabetic după nume - Comparable) --");
        Arrays.sort(ingineri);
        afiseazaIngineri(ingineri);

        System.out.println("\n-- Sortare după Salariu (descrescător - Comparator) --");
        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        afiseazaIngineri(ingineri);
        System.out.println();

        System.out.println(">>> 3. Demonstrație Polimorfism (Interfață PlataOnline):");
        PlataOnline referintaPlata = ingineri[0];

        referintaPlata.autentificare("vpopescu", "parola123");
        System.out.println("Sold consultat prin interfață: " + referintaPlata.consultareSold());

        System.out.println("[INFO] Accesul este restricționat doar la metodele din PlataOnline.");
        System.out.println();

        System.out.println(">>> 4. Demonstrație Persoana Juridică & SMS:");
        PersoanaJuridica srlOk = new PersoanaJuridica("Tech Solutions SRL", "021444555");
        PlataOnlineSMS referintaSMS = srlOk;

        System.out.println("Trimitere SMS valid: " + referintaSMS.trimiteSMS("Plata confirmată pentru factura #101"));
        System.out.println("Mesaje stocate în SRL: " + srlOk.getSmsTrimise());
        System.out.println();

        System.out.println(">>> 5. Testare Edge Cases & Erori:");

        PersoanaJuridica srlFaraTel = new PersoanaJuridica("NoPhone SRL", "");
        System.out.println("Trimitere SMS către SRL fără telefon: " + srlFaraTel.trimiteSMS("Salut")); // Trebuie să fie false

        System.out.println("Trimitere SMS cu mesaj null: " + referintaSMS.trimiteSMS(null)); // Trebuie să fie false

        try {
            System.out.println("Încercare autentificare cu user null...");
            referintaPlata.autentificare(null, "1234");
        } catch (IllegalArgumentException e) {
            System.out.println("Capturat: " + e.getMessage());
        }

        try {
            System.out.println("Încercare trimitere SMS de pe un Inginer...");
            testareCapabilitateSMS(referintaPlata, "Mesaj spam");
        } catch (UnsupportedOperationException e) {
            System.out.println("Capturat: " + e.getMessage());
        }
    }

    private static void afiseazaIngineri(Inginer[] array) {
        for (Inginer i : array) {
            System.out.println(" - " + i);
        }
    }

    private static void testareCapabilitateSMS(PlataOnline entitate, String mesaj) {
        if (!(entitate instanceof PlataOnlineSMS)) {
            throw new UnsupportedOperationException("Eroare: Această entitate (Inginer) nu suportă operații SMS!");
        }
        ((PlataOnlineSMS) entitate).trimiteSMS(mesaj);
    }
}
package com.pao.proiect.elearning;

import com.pao.proiect.elearning.model.*;
import com.pao.proiect.elearning.service.*;
import com.pao.proiect.elearning.exception.ResursaNegasitaException;

public class Main {
    public static void main() {
        UtilizatorService uService = UtilizatorService.getInstance();
        CursService cService = CursService.getInstance();

        System.out.println("=====================================================");
        System.out.println("   POPULARE INITIALA SISTEM");
        System.out.println("=====================================================");
        uService.adauga(new Student(901, "Maria Georgescu", "2025-10-15"));
        uService.adauga(new Instructor(902, "Dr. Vasile", "2024-05-10", "Securitate Cibernetica"));
        Curs cursC = new Curs(801, "C++ pentru Incepatori");
        cursC.adaugaMaterial(new LectieVideo("Introducere C++", 60));
        cService.adauga(cursC);

        System.out.println("-> Sistemul contine initial 2 utilizatori si 1 curs.\n");
        uService.listeazaToti();

        System.out.println("\n=====================================================");
        System.out.println("   ACTIUNI DE VERIFICARE");
        System.out.println("=====================================================\n");

        // --- ACTIUNEA 1 ---
        System.out.println("1. Inregistrare Student nou...");
        uService.adauga(new Student(1, "Stefan Popescu", "2026-04-20"));
        System.out.println("VERIFICARE: Studentul a fost adaugat cu succes.");
        uService.listeazaToti();

        // --- ACTIUNEA 2 ---
        System.out.println("\n2. Inregistrare Instructor nou...");
        uService.adauga(new Instructor(2, "Prof. Andrei Ionescu", "2025-09-01", "Programare Java"));
        System.out.println("VERIFICARE: Instructorul a fost adaugat cu succes.");
        uService.listeazaToti();

        // --- ACTIUNEA 3 ---
        System.out.println("\n3. Creare Cursuri noi...");
        Curs c1 = new Curs(101, "Java Advanced");
        Curs c2 = new Curs(102, "Algoritmi de Sortare");
        Curs c3 = new Curs(103, "Baze de Date");
        cService.adauga(c1);
        cService.adauga(c2);
        cService.adauga(c3);
        System.out.println("VERIFICARE: Cursurile adaugate in sistem sunt:");
        cService.afiseazaToate(); // Va afisa cursurile noi + cel de C++

        // --- ACTIUNEA 4 ---
        System.out.println("\n4. Adaugare Material (Lectie Video) la cursul 'Java Advanced'...");
        LectieVideo video = new LectieVideo("Introducere in OOP", 45);
        c1.adaugaMaterial(video);
        System.out.println("VERIFICARE continut curs 101:");
        c1.afiseazaMateriale();

        // --- ACTIUNEA 5 ---
        System.out.println("\n5. Adaugare Material (Quiz) la cursul 'Java Advanced'...");
        Quiz quizOOP = new Quiz("Test Recapitulativ OOP", 10);
        c1.adaugaMaterial(quizOOP);
        System.out.println("VERIFICARE continut curs 101 (dupa update):");
        c1.afiseazaMateriale();

        // --- ACTIUNEA 6 ---
        System.out.println("\n6. Inregistrare Scor Quiz (Folosind Clasa Imutabila)...");
        ScorFinal scorStefan = new ScorFinal("REF-STEFAN-101", 95);
        cService.inregistreazaScor(101, scorStefan);
        System.out.println("VERIFICARE Catalog Scoruri pentru Cursul 101:");
        cService.afiseazaCatalog();

        // --- ACTIUNEA 7 ---
        System.out.println("\n7. Cautare Curs dupa Titlu (Gestioneaza Exceptii)...");
        try {
            System.out.print("VERIFICARE Cautam 'Java Advanced': ");
            Curs gasit = cService.cautaDupaNume("Java Advanced");
            System.out.println("Am gasit cu succes -> " + gasit.getTitlu());

            System.out.print("VERIFICARE Cautam 'Curs Inexistent': ");
            cService.cautaDupaNume("Curs Inexistent"); // Aici se declanseaza eroarea
        } catch (ResursaNegasitaException e) {
            System.out.println("\n   [EXCEPTIE PRINSĂ SI TRATATĂ] " + e.getMessage());
        }

        // --- ACTIUNEA 8 ---
        System.out.println("\n8. Listare Utilizatori (Polimorfism - Metode Override)...");
        System.out.println("VERIFICARE Toti utilizatorii (2 pre-existenti + 2 adaugati la pasii 1,2):");
        uService.listeazaToti();

        // --- ACTIUNEA 9 ---
        System.out.println("\n9. Listare Cursuri Sortate Alfabetic (Comparable + TreeSet)...");
        System.out.println("VERIFICARE Ordinea trebuie sa fie alfabetica:");
        cService.afiseazaToate();

        // --- ACTIUNEA 10 ---
        System.out.println("\n10. Stergere Curs 102 ('Algoritmi de Sortare')...");
        cService.sterge(102);

        System.out.println("VERIFICARE Lista cursuri dupa stergere (Cursul 102 nu mai trebuie sa apara):");
        cService.afiseazaToate();
    }
}
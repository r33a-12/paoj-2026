package com.pao.proiect.elearning.service;

import com.pao.proiect.elearning.model.Curs;
import com.pao.proiect.elearning.model.ScorFinal;
import com.pao.proiect.elearning.exception.ResursaNegasitaException;
import com.pao.proiect.elearning.exception.ActiuneInvalidaException;

import java.util.*;

public class CursService {
    private static CursService instance;
    private Set<Curs> cursuri = new TreeSet<>(); // Colectie sortată automat
    private Map<Integer, List<ScorFinal>> catalogScoruri = new HashMap<>(); // Map pentru indexare

    private CursService() {}

    public static CursService getInstance() {
        if (instance == null) instance = new CursService();
        return instance;
    }

    public void adauga(Curs c) {
        cursuri.add(c);
    }

    public void sterge(int id) {
        cursuri.removeIf(c -> c.getId() == id);
        catalogScoruri.remove(id);
    }

    public Curs cautaDupaNume(String nume) throws ResursaNegasitaException {
        return cursuri.stream()
                .filter(c -> c.getTitlu().equalsIgnoreCase(nume))
                .findFirst()
                .orElseThrow(() -> new ResursaNegasitaException("Cursul " + nume + " nu a fost gasit."));
    }

    public void inregistreazaScor(int idCurs, ScorFinal scor) {
        if (scor.getPunctaj() < 0) throw new ActiuneInvalidaException("Scorul nu poate fi negativ.");
        catalogScoruri.computeIfAbsent(idCurs, k -> new ArrayList<>()).add(scor);
    }

    public void afiseazaToate() {
        cursuri.forEach(System.out::println);
    }

    public void afiseazaCatalog() {
        catalogScoruri.forEach((id, scoruri) -> System.out.println("Curs ID " + id + " -> " + scoruri));
    }
}
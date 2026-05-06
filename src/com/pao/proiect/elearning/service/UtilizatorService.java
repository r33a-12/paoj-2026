package com.pao.proiect.elearning.service;

import com.pao.proiect.elearning.model.Utilizator;
import com.pao.proiect.elearning.exception.ResursaNegasitaException;
import java.util.ArrayList;
import java.util.List;

public class UtilizatorService {
    private static UtilizatorService instance;
    private List<Utilizator> utilizatori = new ArrayList<>();

    private UtilizatorService() {}

    public static UtilizatorService getInstance() {
        if (instance == null) instance = new UtilizatorService();
        return instance;
    }

    public void adauga(Utilizator u) {
        utilizatori.add(u);
    }

    public void sterge(int id) {
        utilizatori.removeIf(u -> u.getId() == id);
    }

    public Utilizator cautaDupaId(int id) throws ResursaNegasitaException {
        return utilizatori.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResursaNegasitaException("Utilizatorul cu ID " + id + " nu exista."));
    }

    public void listeazaToti() {
        utilizatori.forEach(System.out::println);
    }
}
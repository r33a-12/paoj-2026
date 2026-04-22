package com.pao.proiect.elearning.model;
import com.pao.proiect.elearning.model.Utilizator;
import java.util.*;

public class UtilizatorService {
    private static UtilizatorService instance;
    private List<Utilizator> utilizatori = new ArrayList<>();

    private UtilizatorService() {}

    public static UtilizatorService getInstance() {
        if (instance == null) instance = new UtilizatorService();
        return instance;
    }

    public void adauga(Utilizator u) { utilizatori.add(u); }
    public void listeazaToti() { utilizatori.forEach(System.out::println); }
}
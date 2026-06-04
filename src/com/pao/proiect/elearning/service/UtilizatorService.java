package com.pao.proiect.elearning.service;

import com.pao.proiect.elearning.model.Utilizator;
import com.pao.proiect.elearning.repository.UtilizatorRepository;
import com.pao.proiect.elearning.exception.ResursaNegasitaException;

import java.util.List;

/**
 * Serviciu Singleton pentru gestionarea utilizatorilor.
 * Refactorizat: folosește UtilizatorRepository (JDBC) în loc de List in-memory.
 * Toate acțiunile sunt loggate prin AuditService.
 */
public class UtilizatorService {
    private static UtilizatorService instance;
    private final UtilizatorRepository repository = new UtilizatorRepository();
    private final AuditService audit = AuditService.getInstance();

    private UtilizatorService() {}

    public static UtilizatorService getInstance() {
        if (instance == null) instance = new UtilizatorService();
        return instance;
    }

    public void adauga(Utilizator u) {
        repository.save(u);

        // Audit diferențiat: student vs instructor
        if ("STUDENT".equals(u.getRol())) {
            audit.logAction("inregistrare_student");
        } else {
            audit.logAction("inregistrare_instructor");
        }
    }

    public void sterge(int id) {
        repository.delete(id);
        audit.logAction("stergere_utilizator");
    }

    public Utilizator cautaDupaId(int id) throws ResursaNegasitaException {
        audit.logAction("cautare_utilizator");
        return repository.findById(id)
                .orElseThrow(() -> new ResursaNegasitaException("Utilizatorul cu ID " + id + " nu exista."));
    }

    public void listeazaToti() {
        List<Utilizator> toti = repository.findAll();
        toti.forEach(System.out::println);
        audit.logAction("listare_utilizatori");
    }
}
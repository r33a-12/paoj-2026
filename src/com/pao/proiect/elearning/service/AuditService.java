package com.pao.proiect.elearning.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton thread-safe pentru audit.
 * Loghează fiecare acțiune executată în fișierul audit.csv (mod append).
 * Format: nume_actiune,timestamp
 */
public class AuditService {
    private static AuditService instance;
    private static final String FISIER_AUDIT = "audit.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private AuditService() {
        // Constructorul privat previne instanțierea externă
    }

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    /**
     * Loghează o acțiune în audit.csv.
     * Metoda este synchronized pentru thread-safety.
     *
     * @param numeActiune numele acțiunii executate
     */
    public synchronized void logAction(String numeActiune) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String linie = numeActiune + "," + timestamp;

        // FileWriter cu append=true — nu suprascrie fișierul
        try (PrintWriter writer = new PrintWriter(new FileWriter(FISIER_AUDIT, true))) {
            writer.println(linie);
        } catch (IOException e) {
            System.err.println("[AUDIT] Eroare la scrierea in audit.csv: " + e.getMessage());
        }
    }
}

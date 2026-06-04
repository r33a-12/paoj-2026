package com.pao.proiect.elearning.service;

import com.pao.proiect.elearning.model.*;
import com.pao.proiect.elearning.repository.*;
import com.pao.proiect.elearning.exception.ResursaNegasitaException;
import com.pao.proiect.elearning.exception.ActiuneInvalidaException;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.sql.*;
import java.util.*;

/**
 * Serviciu Singleton pentru gestionarea cursurilor, materialelor și scorurilor.
 * Refactorizat: folosește Repository-uri JDBC, include tranzacții și interogări JOIN.
 * Toate acțiunile sunt loggate prin AuditService.
 */
public class CursService {
    private static CursService instance;
    private final CursRepository cursRepo = new CursRepository();
    private final MaterialRepository materialRepo = new MaterialRepository();
    private final ScorRepository scorRepo = new ScorRepository();
    private final AuditService audit = AuditService.getInstance();

    private CursService() {}

    public static CursService getInstance() {
        if (instance == null) instance = new CursService();
        return instance;
    }

    // ===================== OPERATII CRUD =====================

    public void adauga(Curs c) {
        cursRepo.save(c);
        audit.logAction("creare_curs");
    }

    public void sterge(int id) {
        // CASCADE pe FK-uri va sterge si materialele/scorurile asociate
        cursRepo.delete(id);
        audit.logAction("stergere_curs");
    }

    public Curs cautaDupaNume(String nume) throws ResursaNegasitaException {
        audit.logAction("cautare_curs");

        // Cautare prin toate cursurile
        List<Curs> toate = cursRepo.findAll();
        return toate.stream()
                .filter(c -> c.getTitlu().equalsIgnoreCase(nume))
                .findFirst()
                .orElseThrow(() -> new ResursaNegasitaException("Cursul " + nume + " nu a fost gasit."));
    }

    /**
     * Adaugă un material la un curs existent (persistă în BD).
     */
    public void adaugaMaterialLaCurs(int cursId, Material material) {
        materialRepo.saveByCursId(cursId, material);

        if (material instanceof LectieVideo) {
            audit.logAction("adaugare_lectie_video");
        } else if (material instanceof Quiz) {
            audit.logAction("adaugare_quiz");
        }
    }

    public void inregistreazaScor(int idCurs, ScorFinal scor) {
        if (scor.getPunctaj() < 0) throw new ActiuneInvalidaException("Scorul nu poate fi negativ.");
        scorRepo.saveByCursId(idCurs, scor);
        audit.logAction("inregistrare_scor");
    }

    public void afiseazaToate() {
        List<Curs> toate = cursRepo.findAll();
        toate.forEach(System.out::println);
        audit.logAction("listare_cursuri_sortate");
    }

    public void afiseazaCatalog() {
        List<ScorFinal> scoruri = scorRepo.findAll();
        scoruri.forEach(s -> System.out.println("   -> " + s));
    }

    // ===================== TRANZACTIE JDBC =====================

    /**
     * Adaugă un curs împreună cu toate materialele sale — într-o tranzacție atomică.
     * Dacă oricare INSERT eșuează, totul se anulează (rollback).
     */
    public void adaugaCursComplet(Curs curs, List<Material> materiale) {
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try {
            conn.setAutoCommit(false);

            // Operatia 1: inserează cursul
            String sqlCurs = "INSERT INTO cursuri (id, titlu) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlCurs)) {
                ps.setInt(1, curs.getId());
                ps.setString(2, curs.getTitlu());
                ps.executeUpdate();
            }

            // Operatia 2: inserează fiecare material
            String sqlMaterial = "INSERT INTO materiale (curs_id, tip, titlu, durata_minute, nr_intrebari) VALUES (?, ?, ?, ?, ?)";
            for (Material m : materiale) {
                try (PreparedStatement ps = conn.prepareStatement(sqlMaterial)) {
                    ps.setInt(1, curs.getId());
                    ps.setString(3, m.getTitlu());

                    if (m instanceof LectieVideo video) {
                        ps.setString(2, "VIDEO");
                        ps.setInt(4, video.getDurataMinute());
                        ps.setNull(5, Types.INTEGER);
                    } else if (m instanceof Quiz quiz) {
                        ps.setString(2, "QUIZ");
                        ps.setNull(4, Types.INTEGER);
                        ps.setInt(5, quiz.getNrIntrebari());
                    }

                    ps.executeUpdate();
                }
            }

            conn.commit();
            System.out.println("[TRANZACTIE] Curs + " + materiale.size() + " materiale adaugate cu succes (commit).");
            audit.logAction("adaugare_curs_complet_tranzactie");

        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("[TRANZACTIE] Eroare — rollback efectuat: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Eroare critica la rollback: " + rollbackEx.getMessage(), rollbackEx);
            }
            throw new RuntimeException("Tranzactia a esuat: " + e.getMessage(), e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("[DB] Eroare la resetarea autoCommit: " + e.getMessage());
            }
        }
    }

    // ===================== INTEROGARI JOIN =====================

    /**
     * JOIN 1: Listează toate cursurile cu numărul de materiale asociate.
     * JOIN între cursuri și materiale, cu GROUP BY.
     */
    public void listeazaCursuriCuNrMateriale() {
        String sql = """
                SELECT c.id, c.titlu, COUNT(m.id) AS nr_materiale
                FROM cursuri c
                LEFT JOIN materiale m ON c.id = m.curs_id
                GROUP BY c.id, c.titlu
                ORDER BY nr_materiale DESC
                """;

        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- Cursuri cu numar de materiale ---");
            System.out.printf("%-6s %-30s %s%n", "ID", "Titlu", "Nr. Materiale");
            System.out.println("-".repeat(50));

            while (rs.next()) {
                System.out.printf("%-6d %-30s %d%n",
                        rs.getInt("id"),
                        rs.getString("titlu"),
                        rs.getInt("nr_materiale"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea JOIN (cursuri + materiale): " + e.getMessage(), e);
        }
    }

    /**
     * JOIN 2: Listează cursurile cu scorul mediu al studenților.
     * JOIN între cursuri și scoruri, cu GROUP BY + AVG.
     */
    public void listeazaCursuriCuScorMediu() {
        String sql = """
                SELECT c.id, c.titlu, AVG(s.punctaj) AS scor_mediu, COUNT(s.id) AS nr_scoruri
                FROM cursuri c
                INNER JOIN scoruri s ON c.id = s.curs_id
                GROUP BY c.id, c.titlu
                ORDER BY scor_mediu DESC
                """;

        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- Cursuri cu scor mediu ---");
            System.out.printf("%-6s %-30s %-12s %s%n", "ID", "Titlu", "Scor Mediu", "Nr. Scoruri");
            System.out.println("-".repeat(60));

            while (rs.next()) {
                System.out.printf("%-6d %-30s %-12.2f %d%n",
                        rs.getInt("id"),
                        rs.getString("titlu"),
                        rs.getDouble("scor_mediu"),
                        rs.getInt("nr_scoruri"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea JOIN (cursuri + scoruri): " + e.getMessage(), e);
        }
    }

    /**
     * JOIN 3: Listează toate scorurile cu titlul cursului aferent.
     * JOIN între scoruri și cursuri.
     */
    public void listeazaScorDetaliatCuNumeCurs() {
        String sql = """
                SELECT s.id_referinta, s.punctaj, c.id AS curs_id, c.titlu AS curs_titlu
                FROM scoruri s
                INNER JOIN cursuri c ON s.curs_id = c.id
                ORDER BY c.titlu, s.punctaj DESC
                """;

        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- Scoruri detaliate cu numele cursului ---");
            System.out.printf("%-20s %-10s %-6s %s%n", "Referinta", "Punctaj", "Curs ID", "Curs Titlu");
            System.out.println("-".repeat(65));

            while (rs.next()) {
                System.out.printf("%-20s %-10d %-6d %s%n",
                        rs.getString("id_referinta"),
                        rs.getInt("punctaj"),
                        rs.getInt("curs_id"),
                        rs.getString("curs_titlu"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea JOIN (scoruri + cursuri): " + e.getMessage(), e);
        }
    }
}
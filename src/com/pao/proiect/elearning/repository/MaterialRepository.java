package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.LectieVideo;
import com.pao.proiect.elearning.model.Material;
import com.pao.proiect.elearning.model.Quiz;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository JDBC pentru entitatea Material (LectieVideo / Quiz).
 * Folosește coloana 'tip' pentru a diferenția tipul materialului.
 */
public class MaterialRepository implements Repository<Material, Integer> {

    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Material entity) {
        throw new UnsupportedOperationException(
                "Foloseste saveByCursId(int cursId, Material entity) pentru a salva un material.");
    }

    /**
     * Salvează un material asociat unui curs specific.
     */
    public void saveByCursId(int cursId, Material entity) {
        String sql = "INSERT INTO materiale (curs_id, tip, titlu, durata_minute, nr_intrebari) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, cursId);
            ps.setString(3, entity.getTitlu());

            if (entity instanceof LectieVideo video) {
                ps.setString(2, "VIDEO");
                ps.setInt(4, video.getDurataMinute());
                ps.setNull(5, Types.INTEGER);
            } else if (entity instanceof Quiz quiz) {
                ps.setString(2, "QUIZ");
                ps.setNull(4, Types.INTEGER);
                ps.setInt(5, quiz.getNrIntrebari());
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea materialului: " + e.getMessage(), e);
        }
    }

    /**
     * Returnează toate materialele unui curs.
     */
    public List<Material> findByCursId(int cursId) {
        String sql = "SELECT * FROM materiale WHERE curs_id = ?";
        List<Material> rezultat = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, cursId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rezultat.add(mapRowToMaterial(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea materialelor: " + e.getMessage(), e);
        }
        return rezultat;
    }

    @Override
    public Optional<Material> findById(Integer id) {
        String sql = "SELECT * FROM materiale WHERE id = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToMaterial(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea materialului: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Material> findAll() {
        String sql = "SELECT * FROM materiale";
        List<Material> rezultat = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rezultat.add(mapRowToMaterial(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea materialelor: " + e.getMessage(), e);
        }
        return rezultat;
    }

    @Override
    public void update(Material entity) {
        // Materialele nu se actualizează individual în acest context
        throw new UnsupportedOperationException("Update pe materiale nu este implementat.");
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM materiale WHERE id = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea materialului: " + e.getMessage(), e);
        }
    }

    /**
     * Reconstituie LectieVideo sau Quiz pe baza coloanei 'tip'.
     */
    private Material mapRowToMaterial(ResultSet rs) throws SQLException {
        String tip = rs.getString("tip");
        String titlu = rs.getString("titlu");

        if ("VIDEO".equals(tip)) {
            int durata = rs.getInt("durata_minute");
            return new LectieVideo(titlu, durata);
        } else {
            int nrIntrebari = rs.getInt("nr_intrebari");
            return new Quiz(titlu, nrIntrebari);
        }
    }
}

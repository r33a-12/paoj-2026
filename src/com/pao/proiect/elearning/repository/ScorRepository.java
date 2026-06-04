package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.ScorFinal;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository JDBC pentru entitatea ScorFinal.
 */
public class ScorRepository implements Repository<ScorFinal, Integer> {

    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(ScorFinal entity) {
        throw new UnsupportedOperationException(
                "Foloseste saveByCursId(int cursId, ScorFinal entity) pentru a salva un scor.");
    }

    /**
     * Salvează un scor asociat unui curs specific.
     */
    public void saveByCursId(int cursId, ScorFinal entity) {
        String sql = "INSERT INTO scoruri (curs_id, id_referinta, punctaj) VALUES (?, ?, ?)";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, cursId);
            ps.setString(2, entity.getIdReferinta());
            ps.setInt(3, entity.getPunctaj());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea scorului: " + e.getMessage(), e);
        }
    }

    /**
     * Returnează toate scorurile unui curs.
     */
    public List<ScorFinal> findByCursId(int cursId) {
        String sql = "SELECT * FROM scoruri WHERE curs_id = ?";
        List<ScorFinal> rezultat = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, cursId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rezultat.add(mapRowToScor(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea scorurilor: " + e.getMessage(), e);
        }
        return rezultat;
    }

    @Override
    public Optional<ScorFinal> findById(Integer id) {
        String sql = "SELECT * FROM scoruri WHERE id = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToScor(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea scorului: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<ScorFinal> findAll() {
        String sql = "SELECT * FROM scoruri";
        List<ScorFinal> rezultat = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rezultat.add(mapRowToScor(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea scorurilor: " + e.getMessage(), e);
        }
        return rezultat;
    }

    @Override
    public void update(ScorFinal entity) {
        // ScorFinal este imutabil — nu se actualizează
        throw new UnsupportedOperationException("ScorFinal este imutabil si nu poate fi actualizat.");
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM scoruri WHERE id = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea scorului: " + e.getMessage(), e);
        }
    }

    private ScorFinal mapRowToScor(ResultSet rs) throws SQLException {
        return new ScorFinal(rs.getString("id_referinta"), rs.getInt("punctaj"));
    }
}

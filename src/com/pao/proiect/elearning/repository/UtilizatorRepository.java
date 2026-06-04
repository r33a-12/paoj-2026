package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.*;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository JDBC pentru ierarhia Utilizator (Student / Instructor).
 * Folosește Single Table Inheritance — coloana 'rol' diferentiază tipul.
 */
public class UtilizatorRepository implements Repository<Utilizator, Integer> {

    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Utilizator entity) {
        String sql = "INSERT INTO utilizatori (id, nume, rol, data_inscriere, specializare) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getNume());
            ps.setString(3, entity.getRol());

            if (entity instanceof MembruPlatforma membru) {
                ps.setString(4, membru.getDataInscriere());
            } else {
                ps.setNull(4, Types.VARCHAR);
            }

            if (entity instanceof Instructor instructor) {
                ps.setString(5, instructor.getSpecializare());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea utilizatorului: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Utilizator> findById(Integer id) {
        String sql = "SELECT * FROM utilizatori WHERE id = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUtilizator(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea utilizatorului: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Utilizator> findAll() {
        String sql = "SELECT * FROM utilizatori";
        List<Utilizator> rezultat = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rezultat.add(mapRowToUtilizator(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea utilizatorilor: " + e.getMessage(), e);
        }
        return rezultat;
    }

    @Override
    public void update(Utilizator entity) {
        String sql = "UPDATE utilizatori SET nume = ?, rol = ?, data_inscriere = ?, specializare = ? WHERE id = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getRol());

            if (entity instanceof MembruPlatforma membru) {
                ps.setString(3, membru.getDataInscriere());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }

            if (entity instanceof Instructor instructor) {
                ps.setString(4, instructor.getSpecializare());
            } else {
                ps.setNull(4, Types.VARCHAR);
            }

            ps.setInt(5, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea utilizatorului: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM utilizatori WHERE id = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea utilizatorului: " + e.getMessage(), e);
        }
    }

    /**
     * Reconstituie obiectul corect (Student sau Instructor) pe baza coloanei 'rol'.
     */
    private Utilizator mapRowToUtilizator(ResultSet rs) throws SQLException {
        String rol = rs.getString("rol");
        int id = rs.getInt("id");
        String nume = rs.getString("nume");
        String dataInscriere = rs.getString("data_inscriere");

        if ("INSTRUCTOR".equals(rol)) {
            String specializare = rs.getString("specializare");
            return new Instructor(id, nume, dataInscriere, specializare);
        } else {
            return new Student(id, nume, dataInscriere);
        }
    }
}

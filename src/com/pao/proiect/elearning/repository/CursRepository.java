package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.Curs;
import com.pao.proiect.elearning.model.Material;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository JDBC pentru entitatea Curs.
 * findById încarcă și materialele asociate.
 */
public class CursRepository implements Repository<Curs, Integer> {

    private final MaterialRepository materialRepo = new MaterialRepository();

    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Curs entity) {
        String sql = "INSERT INTO cursuri (id, titlu) VALUES (?, ?)";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, entity.getId());
            ps.setString(2, entity.getTitlu());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea cursului: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Curs> findById(Integer id) {
        String sql = "SELECT * FROM cursuri WHERE id = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Curs curs = new Curs(rs.getInt("id"), rs.getString("titlu"));
                    // Încarcă și materialele asociate
                    List<Material> materiale = materialRepo.findByCursId(id);
                    for (Material m : materiale) {
                        curs.adaugaMaterial(m);
                    }
                    return Optional.of(curs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea cursului: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Curs> findAll() {
        String sql = "SELECT * FROM cursuri ORDER BY titlu";
        List<Curs> rezultat = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Curs curs = new Curs(rs.getInt("id"), rs.getString("titlu"));
                // Încarcă materialele pentru fiecare curs
                List<Material> materiale = materialRepo.findByCursId(curs.getId());
                for (Material m : materiale) {
                    curs.adaugaMaterial(m);
                }
                rezultat.add(curs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea cursurilor: " + e.getMessage(), e);
        }
        return rezultat;
    }

    @Override
    public void update(Curs entity) {
        String sql = "UPDATE cursuri SET titlu = ? WHERE id = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, entity.getTitlu());
            ps.setInt(2, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea cursului: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM cursuri WHERE id = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea cursului: " + e.getMessage(), e);
        }
    }
}

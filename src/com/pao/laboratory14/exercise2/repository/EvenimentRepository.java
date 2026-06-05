package com.pao.laboratory14.exercise2.repository;

import com.pao.laboratory14.exercise1.TipBilet;
import com.pao.laboratory14.exercise2.model.Eveniment;
import com.pao.laboratory14.exercise2.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EvenimentRepository implements Repository<Eveniment, Integer> {

    private Connection getConnection() {
        try {
            return DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void initSchema() throws SQLException {
        String drop = "DROP TABLE IF EXISTS evenimente";
        String create = "CREATE TABLE IF NOT EXISTS evenimente (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nume TEXT NOT NULL, " +
                "data TEXT NOT NULL, " +
                "capacitate INTEGER, " +
                "tip TEXT)";
        try (Statement st = getConnection().createStatement()) {
            st.execute(drop);
            st.execute(create);
        }
    }

    @Override
    public void save(Eveniment entity) throws SQLException {
        String sql = "INSERT INTO evenimente(nume, data, capacitate, tip) VALUES(?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getData());
            ps.setInt(3, entity.getCapacitate());
            ps.setString(4, entity.getTip() != null ? entity.getTip().name() : null);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Optional<Eveniment> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM evenimente WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEveniment(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Eveniment> findAll() throws SQLException {
        List<Eveniment> list = new ArrayList<>();
        String sql = "SELECT * FROM evenimente ORDER BY id";
        try (Statement st = getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEveniment(rs));
            }
        }
        return list;
    }

    @Override
    public void update(Eveniment entity) throws SQLException {
        String sql = "UPDATE evenimente SET nume=?, data=?, capacitate=?, tip=? WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getData());
            ps.setInt(3, entity.getCapacitate());
            ps.setString(4, entity.getTip() != null ? entity.getTip().name() : null);
            ps.setInt(5, entity.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        deleteImpl(id);
    }
    
    public int deleteImpl(int id) throws SQLException {
        String sql = "DELETE FROM evenimente WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM evenimente";
        try (Statement st = getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private Eveniment mapResultSetToEveniment(ResultSet rs) throws SQLException {
        TipBilet tip = null;
        if (rs.getString("tip") != null) {
            tip = TipBilet.valueOf(rs.getString("tip"));
        }
        return new Eveniment(
                rs.getInt("id"),
                rs.getString("nume"),
                rs.getString("data"),
                rs.getInt("capacitate"),
                tip
        );
    }
}

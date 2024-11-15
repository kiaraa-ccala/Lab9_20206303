package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Arbitro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoArbitros {
    public ArrayList<Arbitro> listaArbitros() {
        ArrayList<Arbitro> arbitros = new ArrayList<>();
        String sql = "SELECT idArbitro, nombre, pais FROM arbitro"; // Ajusta el nombre de tu tabla si es necesario

        try (Connection conn = DaoBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombre"));
                arbitro.setPais(rs.getString("pais"));
                arbitros.add(arbitro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return arbitros;
    }

    public boolean crearArbitro(Arbitro arbitro) {
        String sql = "INSERT INTO arbitro (nombre, pais) VALUES (?, ?)";
        try (Connection conn = DaoBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, arbitro.getNombre());
            stmt.setString(2, arbitro.getPais());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna true si se insertó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false si ocurrió algún error
        }
    }


    public ArrayList<Arbitro> busquedaPais(String pais) {

        ArrayList<Arbitro> arbitros = new ArrayList<>();
        String sql = "SELECT * FROM arbitro WHERE pais = ?";

        try (Connection conn = DaoBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pais);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombre"));
                arbitro.setPais(rs.getString("pais"));
                arbitros.add(arbitro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return arbitros;
    }

    public ArrayList<Arbitro> busquedaNombre(String nombre) {
        ArrayList<Arbitro> arbitros = new ArrayList<>();
        String sql = "SELECT * FROM arbitro WHERE nombre LIKE ?";

        try (Connection conn = DaoBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombre"));
                arbitro.setPais(rs.getString("pais"));
                arbitros.add(arbitro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return arbitros;
    }


    public boolean guardarArbitro(Arbitro nuevoArbitro) {
        String sql = "INSERT INTO arbitro (nombre, pais) VALUES (?, ?)";

        try (Connection conn = DaoBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asignar los valores a los parámetros
            stmt.setString(1, nuevoArbitro.getNombre());
            stmt.setString(2, nuevoArbitro.getPais());

            // Ejecutar la inserción
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna true si se insertó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false si ocurrió algún error
        }
    }
    public boolean existeNombreArbitro(String nombre) {
        String sql = "SELECT COUNT(*) FROM arbitro WHERE nombre = ?";
        try (Connection conn = DaoBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true si el nombre ya existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retorna false si no existe o si hay un error
    }

    public boolean eliminarArbitro(int idArbitro) {
        String sql = "DELETE FROM arbitro WHERE idArbitro = ?";
        try (Connection conn = DaoBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Asigna el idArbitro al primer parámetro de la consulta
            stmt.setInt(1, idArbitro);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna true si se eliminó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false si ocurrió algún error
        }
    }




    public void borrarArbitro(int id) {
        String sql = "DELETE FROM arbitro WHERE idArbitro = ?";

        try (Connection conn = DaoBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

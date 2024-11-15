package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Bean.Estadio;
import com.example.lab9_base.Bean.Seleccion;
import com.example.lab9_base.Bean.Partido;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoPartidos {

    public ArrayList<Partido> listaDePartidos() {
        ArrayList<Partido> partidos = new ArrayList<>();
        String sql = "SELECT p.idPartido, p.fecha, p.numeroJornada, " +
                "s1.nombre AS nombreLocal, s2.nombre AS nombreVisitante, " +
                "e.nombre AS estadio, a.nombre AS arbitro " +
                "FROM partido p " +
                "JOIN seleccion s1 ON p.seleccionLocal = s1.idSeleccion " +
                "JOIN seleccion s2 ON p.seleccionVisitante = s2.idSeleccion " +
                "JOIN estadio e ON s1.estadio_idEstadio = e.idEstadio " +
                "JOIN arbitro a ON p.arbitro = a.idArbitro";

        try (Connection conn = DaoBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Partido partido = new Partido();
                partido.setIdPartido(rs.getInt("idPartido"));
                partido.setFecha(rs.getDate("fecha"));
                partido.setNumeroJornada(rs.getInt("numeroJornada"));

                // Configurar la Selección Local
                Seleccion seleccionLocal = new Seleccion();
                seleccionLocal.setNombre(rs.getString("nombreLocal"));
                Estadio estadio = new Estadio();
                estadio.setNombre(rs.getString("estadio"));
                seleccionLocal.setEstadio(estadio);
                partido.setSeleccionLocal(seleccionLocal);

                // Configurar la Selección Visitante
                Seleccion seleccionVisitante = new Seleccion();
                seleccionVisitante.setNombre(rs.getString("nombreVisitante"));
                partido.setSeleccionVisitante(seleccionVisitante);

                // Configurar el Arbitro
                Arbitro arbitro = new Arbitro();
                arbitro.setNombre(rs.getString("arbitro"));
                partido.setArbitro(arbitro);

                partidos.add(partido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return partidos;
    }

    public boolean crearPartido(Partido partido) {
        boolean success = false;
        String sql = "INSERT INTO partido (seleccionLocal, seleccionVisitante, arbitro, fecha, numeroJornada) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DaoBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, partido.getSeleccionLocal().getIdSeleccion());
            stmt.setInt(2, partido.getSeleccionVisitante().getIdSeleccion());
            stmt.setInt(3, partido.getArbitro().getIdArbitro());
            stmt.setDate(4, new java.sql.Date(partido.getFecha().getTime()));
            stmt.setInt(5, partido.getNumeroJornada());

            // Ejecutar la consulta y comprobar si se afectó alguna fila
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean existePartido(Partido partido) {
        String sql = "SELECT COUNT(*) FROM partido WHERE seleccionLocal = ? AND seleccionVisitante = ? AND fecha = ?";
        try (Connection conn = DaoBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, partido.getSeleccionLocal().getIdSeleccion());
            stmt.setInt(2, partido.getSeleccionVisitante().getIdSeleccion());
            stmt.setDate(3, new java.sql.Date(partido.getFecha().getTime()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}

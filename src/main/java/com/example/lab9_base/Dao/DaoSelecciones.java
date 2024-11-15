package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Seleccion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoSelecciones {

    public ArrayList<Seleccion> listarSelecciones() {
        ArrayList<Seleccion> selecciones = new ArrayList<>();
        String sql = "SELECT idSeleccion, nombre FROM seleccion"; // Cambia 'seleccion' por el nombre correcto de tu tabla

        try (Connection conn = DaoBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Seleccion seleccion = new Seleccion();
                seleccion.setIdSeleccion(rs.getInt("idSeleccion"));
                seleccion.setNombre(rs.getString("nombre"));
                selecciones.add(seleccion);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return selecciones;
    }
}

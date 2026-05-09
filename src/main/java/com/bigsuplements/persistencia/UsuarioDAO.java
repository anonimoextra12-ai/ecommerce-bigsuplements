package com.bigsuplements.persistencia;

import com.bigsuplements.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    private final Connection conexion;

    public UsuarioDAO() {

        conexion =
                ConexionDB.getInstance()
                        .getConexion();
    }

    public Usuario login(
            String email,
            String password
    ) {

        String sql =
                "SELECT * FROM usuarios WHERE email=? AND password=?";

        try {

            PreparedStatement ps =
                    conexion.prepareStatement(sql);

            ps.setString(1, email);

            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("rol")
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Error login: "
                    + e.getMessage()
            );
        }

        return null;
    }
}
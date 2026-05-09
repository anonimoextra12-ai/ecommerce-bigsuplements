package com.bigsuplements.vista.ventanas;

import com.bigsuplements.modelo.Usuario;
import com.bigsuplements.persistencia.ConexionDB;
import com.bigsuplements.persistencia.UsuarioDAO;
import com.bigsuplements.vista.PanelAdmin;
import com.bigsuplements.vista.VentanaTienda;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {

    private JTextField txtEmail;

    private JPasswordField txtPassword;

    private final UsuarioDAO usuarioDAO =
            new UsuarioDAO();

    public VentanaLogin() {

        setTitle("BigSuplements - Login");

        setSize(420, 320);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        getContentPane().setBackground(
                new Color(18,18,24)
        );

        // =====================================
        // TITULO
        // =====================================
        JLabel titulo = new JLabel(
                "BIGSUPLEMENTS",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("SansSerif",
                        Font.BOLD,
                        24)
        );

        titulo.setForeground(
                new Color(255,87,34)
        );

        add(titulo, BorderLayout.NORTH);

        // =====================================
        // FORMULARIO
        // =====================================
        JPanel form = new JPanel();

        form.setLayout(
                new GridLayout(5,1,10,10)
        );

        form.setBorder(
                BorderFactory.createEmptyBorder(
                        30,40,30,40
                )
        );

        form.setBackground(
                new Color(18,18,24)
        );

        JLabel lblEmail =
                new JLabel("Correo");

        lblEmail.setForeground(Color.WHITE);

        txtEmail = new JTextField();

        JLabel lblPass =
                new JLabel("Contraseña");

        lblPass.setForeground(Color.WHITE);

        txtPassword =
                new JPasswordField();

        JButton btnLogin =
                new JButton("INGRESAR");

        btnLogin.setBackground(
                new Color(255,87,34)
        );

        btnLogin.setForeground(Color.WHITE);

        btnLogin.setFocusPainted(false);

        btnLogin.addActionListener(
                e -> iniciarSesion()
        );

        form.add(lblEmail);

        form.add(txtEmail);

        form.add(lblPass);

        form.add(txtPassword);

        form.add(btnLogin);

        add(form, BorderLayout.CENTER);
    }

    // =====================================
    // LOGIN
    // =====================================
    private void iniciarSesion() {

        String email =
                txtEmail.getText();

        String password =
                new String(
                        txtPassword.getPassword()
                );

        Usuario usuario =
                usuarioDAO.login(
                        email,
                        password
                );

        if (usuario == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Correo o contraseña incorrectos"
            );

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Bienvenido " +
                        usuario.getNombre()
        );

        dispose();

        // =================================
        // ABRIR SEGUN ROL
        // =================================
        if (usuario.getRol()
                .equalsIgnoreCase("ADMIN")) {

            new PanelAdmin()
                    .setVisible(true);

        } else {

            new VentanaTienda()
                    .setVisible(true);
        }
    }

    // =====================================
    // MAIN
    // =====================================
    public static void main(String[] args) {

        ConexionDB.getInstance();

        SwingUtilities.invokeLater(() -> {

            new VentanaLogin()
                    .setVisible(true);
        });
    }
}
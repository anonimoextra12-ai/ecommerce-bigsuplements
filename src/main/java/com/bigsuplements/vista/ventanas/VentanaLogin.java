package com.bigsuplements.vista.ventanas;

import com.bigsuplements.modelo.Usuario;
import com.bigsuplements.persistencia.ConexionDB;
import com.bigsuplements.persistencia.UsuarioDAO;
import com.bigsuplements.vista.VentanaRegistro;
import com.bigsuplements.vista.VentanaTienda;
import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public VentanaLogin() {
        setTitle("BigSuplements - Login");

        setSize(420, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(18, 18, 24));

        // TITULO
        JLabel titulo = new JLabel(
                "BIGSUPLEMENTS",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(new Color(255, 87, 34));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        add(titulo, BorderLayout.NORTH);

        // FORMULARIO
        JPanel form = new JPanel();

        form.setLayout(new GridLayout(6, 1, 10, 10));

        form.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40));
        form.setBackground(new Color(18, 18, 24));

        JLabel lblEmail = new JLabel("Correo");
        lblEmail.setForeground(Color.WHITE);
        txtEmail = new JTextField();

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setForeground(Color.WHITE);
        txtPassword = new JPasswordField();

        // BOTÓN INGRESAR
        JButton btnLogin = new JButton("INGRESAR");
        btnLogin.setBackground(new Color(255, 87, 34));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnLogin.addActionListener(e -> iniciarSesion());

        // BOTÓN REGISTRARSE
        JButton btnRegister = new JButton("REGISTRARSE");

        btnRegister.setBackground(new Color(45, 45, 60));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnRegister.addActionListener(e -> {

            new VentanaRegistro().setVisible(true);
        });

        form.add(lblEmail);
        form.add(txtEmail);
        form.add(lblPass);
        form.add(txtPassword);
        form.add(btnLogin);
        form.add(btnRegister);

        add(form, BorderLayout.CENTER);
    }

    // LOGIN
    private void iniciarSesion() {

        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        Usuario usuario = usuarioDAO.login(email, password);

        if (usuario == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Correo o contraseña incorrectos",
                    "Error de acceso",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Bienvenido " + usuario.getNombre()
        );

        dispose();

        new VentanaTienda(usuario).setVisible(true);
    }

    // MAIN
    public static void main(String[] args) {
        ConexionDB.getInstance();
        SwingUtilities.invokeLater(() -> {
            new VentanaLogin().setVisible(true);
        });
    }
}

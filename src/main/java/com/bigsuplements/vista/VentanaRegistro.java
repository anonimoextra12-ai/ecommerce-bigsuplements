package com.bigsuplements.vista;

import com.bigsuplements.modelo.Usuario;
import com.bigsuplements.persistencia.UsuarioDAO;
import javax.swing.*;
import java.awt.*;

public class VentanaRegistro extends JFrame {

    private JTextField txtNombre, txtEmail;
    private JPasswordField txtPassword;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public VentanaRegistro() {
        setTitle("BigSuplements - Registro");
        setSize(420, 450); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(18, 18, 24));

        // TITULO
        JLabel titulo = new JLabel("CREAR CUENTA", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(new Color(255, 87, 34));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        add(titulo, BorderLayout.NORTH);

        // El formulario
        JPanel form = new JPanel(new GridLayout(7, 1, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        form.setBackground(new Color(18, 18, 24));

        
        Color colorTexto = Color.WHITE;

        JLabel lblNombre = new JLabel("Nombre Completo");
        lblNombre.setForeground(colorTexto);
        txtNombre = new JTextField();

        JLabel lblEmail = new JLabel("Correo Electrónico");
        lblEmail.setForeground(colorTexto);
        txtEmail = new JTextField();

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setForeground(colorTexto);
        txtPassword = new JPasswordField();

        JButton btnRegistrar = new JButton("REGISTRARSE");
        btnRegistrar.setBackground(new Color(0, 200, 150)); // Un verde azulado para diferenciar del login
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRegistrar.setFocusPainted(false);
        
        btnRegistrar.addActionListener(e -> ejecutarRegistro());

        form.add(lblNombre);
        form.add(txtNombre);
        form.add(lblEmail);
        form.add(txtEmail);
        form.add(lblPass);
        form.add(txtPassword);
        form.add(new JLabel("")); // Espacio
        form.add(btnRegistrar);

        add(form, BorderLayout.CENTER);
    }

    private void ejecutarRegistro() {
        String nombre = txtNombre.getText();
        String email = txtEmail.getText();
        String pass = new String(txtPassword.getPassword());

        if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, llena todos los campos.");
            return;
        }

        
        Usuario nuevo = new Usuario(0, nombre, email, pass, "CLIENTE");

        if (usuarioDAO.registrar(nuevo)) {
            JOptionPane.showMessageDialog(this, "¡Registro exitoso! Ya puedes iniciar sesión.");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error: El correo ya está registrado.");
        }
    }
}
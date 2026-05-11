package com.bigsuplements.vista;

import com.bigsuplements.modelo.Usuario;
import com.bigsuplements.persistencia.UsuarioDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaGestionUsuarios extends JFrame {

    private JTable tablaUsuarios;
    private DefaultTableModel modelo;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario adminActual;

    public VentanaGestionUsuarios(Usuario admin) {
        this.adminActual = admin;
        setTitle("Administración - Usuarios Registrados");
        setSize(700, 550);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(18, 18, 24));
        setLayout(new BorderLayout());

        // TITULO
        JLabel titulo = new JLabel("👥 GESTIÓN DE USUARIOS", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(new Color(0, 200, 150));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        // TABLA
        String[] columnas = {"ID", "Nombre", "Correo", "Rol"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tablaUsuarios = new JTable(modelo);
        estilizarTabla(tablaUsuarios);

        JScrollPane scroll = new JScrollPane(tablaUsuarios);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scroll.getViewport().setBackground(new Color(24, 24, 34));
        add(scroll, BorderLayout.CENTER);

        // PANEL DE ACCIONES
        JPanel panelAcciones = new JPanel();
        panelAcciones.setBackground(new Color(18, 18, 24));
        panelAcciones.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton btnEliminar = new JButton("ELIMINAR USUARIO SELECCIONADO");
        btnEliminar.setBackground(new Color(211, 47, 47));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setFont(new Font("SansSerif", Font.BOLD, 12));

        btnEliminar.addActionListener(e -> ejecutarEliminacion());

        panelAcciones.add(btnEliminar);
        add(panelAcciones, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        ArrayList<Usuario> usuarios = usuarioDAO.listarTodos();
        for (Usuario u : usuarios) {
            modelo.addRow(new Object[]{u.getId(), u.getNombre(), u.getEmail(), u.getRol()});
        }
    }

    private void ejecutarEliminacion() {
        int fila = tablaUsuarios.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un usuario de la tabla.");
            return;
        }

        int idSeleccionado = (int) modelo.getValueAt(fila, 0);
        String nombre = (String) modelo.getValueAt(fila, 1);

        // que el admin no se mate solo
        if (idSeleccionado == adminActual.getId()) {
            JOptionPane.showMessageDialog(this,
                    "No puedes eliminar tu propia cuenta mientras estás en uso.",
                    "Acción denegada",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de eliminar a " + nombre + "?\nEsta acción no se puede deshacer.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            if (usuarioDAO.eliminar(idSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error: No se pudo eliminar.");
            }
        }
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setBackground(new Color(28, 28, 38));
        tabla.setForeground(Color.WHITE);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setBackground(new Color(40, 40, 55));
        tabla.getTableHeader().setForeground(new Color(0, 200, 150));
        tabla.setGridColor(new Color(45, 45, 60));
        tabla.setSelectionBackground(new Color(255, 87, 34, 100));
    }
}

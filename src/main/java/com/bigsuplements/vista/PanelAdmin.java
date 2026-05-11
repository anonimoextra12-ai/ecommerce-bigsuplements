package com.bigsuplements.vista;

import com.bigsuplements.modelo.Creatina;
import com.bigsuplements.modelo.Producto;
import com.bigsuplements.modelo.Proteina;
import com.bigsuplements.modelo.Usuario;
import com.bigsuplements.persistencia.ProductoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PanelAdmin extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private ProductoDAO dao = new ProductoDAO();
    private ArrayList<Producto> productos;
    private Usuario usuarioLogueado;

    public PanelAdmin(Usuario admin) {
        this.usuarioLogueado = admin;

        setTitle("Panel Administrador - BigSuplements");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // TABLA 
        String[] columnas = {"ID", "Tipo", "Nombre", "Marca", "Precio", "Stock"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        //  PANEL DE BOTONES 
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(30, 30, 30));
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        JButton btnAgregar = crearBoton("Agregar", new Color(255, 87, 34));
        JButton btnEditar = crearBoton("Editar", new Color(255, 152, 0));
        JButton btnEliminar = crearBoton("Eliminar", new Color(211, 47, 47));
        JButton btnRecargar = crearBoton("Recargar", new Color(33, 150, 243));

        // BOTÓN GESTIONAR USUARIOS
        JButton btnUsuarios = crearBoton("Gestionar Usuarios", new Color(0, 150, 136));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRecargar);
        panelBotones.add(new JSeparator(SwingConstants.VERTICAL));
        panelBotones.add(btnUsuarios);

        add(panelBotones, BorderLayout.SOUTH);

        //  EVENTOS 
        btnAgregar.addActionListener(e -> agregarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnRecargar.addActionListener(e -> cargarProductos());

        btnUsuarios.addActionListener(e -> {
            new VentanaGestionUsuarios(usuarioLogueado).setVisible(true);
        });

        cargarProductos();
    }

    private JButton crearBoton(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        return btn;
    }

    private void cargarProductos() {
        modelo.setRowCount(0);
        productos = dao.listarTodo();
        for (Producto p : productos) {
            modelo.addRow(new Object[]{
                p.getId(), p.getTipo(), p.getNombre(), p.getMarca(), p.getPrecio(), p.getStock()
            });
        }
    }

    private void agregarProducto() {
        String[] opciones = {"PROTEINA", "CREATINA"};
        String tipo = (String) JOptionPane.showInputDialog(this, "Seleccione tipo:", "Agregar Producto",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (tipo == null) {
            return;
        }

        try {
            String nombre = JOptionPane.showInputDialog("Nombre:");
            String marca = JOptionPane.showInputDialog("Marca:");
            double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio:"));
            int stock = Integer.parseInt(JOptionPane.showInputDialog("Stock:"));

            Producto producto;
            if (tipo.equals("PROTEINA")) {
                String sabor = JOptionPane.showInputDialog("Sabor:");
                producto = new Proteina(0, nombre, marca, precio, stock, sabor, Proteina.TipoProteina.WHEY);
            } else {
                int servicios = Integer.parseInt(JOptionPane.showInputDialog("Servicios:"));
                producto = new Creatina(0, nombre, marca, precio, stock, servicios, true);
            }

            if (dao.agregarProducto(producto)) {
                JOptionPane.showMessageDialog(this, "Producto agregado.");
                cargarProductos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en los datos.");
        }
    }

    private void editarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }

        Producto p = productos.get(fila);
        try {
            String nombre = JOptionPane.showInputDialog("Nuevo nombre:", p.getNombre());
            String marca = JOptionPane.showInputDialog("Nueva marca:", p.getMarca());
            double precio = Double.parseDouble(JOptionPane.showInputDialog("Nuevo precio:", p.getPrecio()));
            int stock = Integer.parseInt(JOptionPane.showInputDialog("Nuevo stock:", p.getStock()));

            p.setNombre(nombre);
            p.setMarca(marca);
            p.setPrecio(precio);
            p.setStock(stock);

            if (dao.actualizarProducto(p)) {
                JOptionPane.showMessageDialog(this, "Producto actualizado.");
                cargarProductos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar.");
        }
    }

    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }

        Producto p = productos.get(fila);
        int op = JOptionPane.showConfirmDialog(this, "¿Eliminar " + p.getNombre() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (op == JOptionPane.YES_OPTION) {
            if (dao.eliminarProducto(p.getId())) {
                JOptionPane.showMessageDialog(this, "Producto eliminado.");
                cargarProductos();
            }
        }
    }
}

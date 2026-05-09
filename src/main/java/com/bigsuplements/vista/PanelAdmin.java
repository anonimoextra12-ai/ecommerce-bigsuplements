package com.bigsuplements.vista;

import com.bigsuplements.modelo.Creatina;
import com.bigsuplements.modelo.Producto;
import com.bigsuplements.modelo.Proteina;
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

    public PanelAdmin() {

        setTitle("Panel Administrador");

        setSize(1000, 600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // TABLA
        String[] columnas = {
            "ID",
            "Tipo",
            "Nombre",
            "Marca",
            "Precio",
            "Stock"
        };

        modelo = new DefaultTableModel(columnas, 0);

        tabla = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabla);

        add(scroll, BorderLayout.CENTER);

        // BOTONES
        JPanel panelBotones = new JPanel();

        JButton btnAgregar = new JButton("Agregar");

        JButton btnEditar = new JButton("Editar");

        JButton btnEliminar = new JButton("Eliminar");

        JButton btnRecargar = new JButton("Recargar");

        panelBotones.add(btnAgregar);

        panelBotones.add(btnEditar);

        panelBotones.add(btnEliminar);

        panelBotones.add(btnRecargar);

        add(panelBotones, BorderLayout.SOUTH);

        // EVENTOS
        btnAgregar.addActionListener(e -> agregarProducto());

        btnEditar.addActionListener(e -> editarProducto());

        btnEliminar.addActionListener(e -> eliminarProducto());

        btnRecargar.addActionListener(e -> cargarProductos());

        cargarProductos();
    }

    // =========================
    // CARGAR PRODUCTOS
    // =========================
    private void cargarProductos() {

        modelo.setRowCount(0);

        productos = dao.listarTodo();

        for (Producto p : productos) {

            modelo.addRow(new Object[]{
                p.getId(),
                p.getTipo(),
                p.getNombre(),
                p.getMarca(),
                p.getPrecio(),
                p.getStock()
            });
        }
    }

    // =========================
    // AGREGAR PRODUCTO
    // =========================
    private void agregarProducto() {

        String[] opciones = {
            "PROTEINA",
            "CREATINA"
        };

        String tipo = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione tipo:",
                "Agregar Producto",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (tipo == null) return;

        String nombre = JOptionPane.showInputDialog("Nombre:");

        String marca = JOptionPane.showInputDialog("Marca:");

        double precio = Double.parseDouble(
                JOptionPane.showInputDialog("Precio:")
        );

        int stock = Integer.parseInt(
                JOptionPane.showInputDialog("Stock:")
        );

        Producto producto;

        if (tipo.equals("PROTEINA")) {

            String sabor = JOptionPane.showInputDialog("Sabor:");

            producto = new Proteina(
                    0,
                    nombre,
                    marca,
                    precio,
                    stock,
                    sabor,
                    Proteina.TipoProteina.WHEY
            );

        } else {

            int servicios = Integer.parseInt(
                    JOptionPane.showInputDialog("Servicios:")
            );

            producto = new Creatina(
                    0,
                    nombre,
                    marca,
                    precio,
                    stock,
                    servicios,
                    true
            );
        }

        boolean ok = dao.agregarProducto(producto);

        if (ok) {

            JOptionPane.showMessageDialog(
                    this,
                    "Producto agregado."
            );

            cargarProductos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al agregar."
            );
        }
    }

    // =========================
    // EDITAR PRODUCTO
    // =========================
    private void editarProducto() {

        int fila = tabla.getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un producto."
            );

            return;
        }

        Producto p = productos.get(fila);

        String nombre = JOptionPane.showInputDialog(
                "Nuevo nombre:",
                p.getNombre()
        );

        String marca = JOptionPane.showInputDialog(
                "Nueva marca:",
                p.getMarca()
        );

        double precio = Double.parseDouble(
                JOptionPane.showInputDialog(
                        "Nuevo precio:",
                        p.getPrecio()
                )
        );

        int stock = Integer.parseInt(
                JOptionPane.showInputDialog(
                        "Nuevo stock:",
                        p.getStock()
                )
        );

        p.setNombre(nombre);

        p.setMarca(marca);

        p.setPrecio(precio);

        p.setStock(stock);

        boolean ok = dao.actualizarProducto(p);

        if (ok) {

            JOptionPane.showMessageDialog(
                    this,
                    "Producto actualizado."
            );

            cargarProductos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al actualizar."
            );
        }
    }

    // =========================
    // ELIMINAR PRODUCTO
    // =========================
    private void eliminarProducto() {

        int fila = tabla.getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un producto."
            );

            return;
        }

        Producto p = productos.get(fila);

        int op = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar producto?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (op != JOptionPane.YES_OPTION) return;

        boolean ok = dao.eliminarProducto(
                p.getId()
        );

        if (ok) {

            JOptionPane.showMessageDialog(
                    this,
                    "Producto eliminado."
            );

            cargarProductos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al eliminar."
            );
        }
    }
}
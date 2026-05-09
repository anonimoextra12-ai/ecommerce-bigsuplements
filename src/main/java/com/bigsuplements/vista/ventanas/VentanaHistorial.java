package com.bigsuplements.vista.ventanas;

import com.bigsuplements.persistencia.ProductoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaHistorial extends JFrame {

    private JTable tablaHistorial;
    private DefaultTableModel modeloHistorial;

    public VentanaHistorial() {

        setTitle("Historial de Ventas");

        setSize(700, 500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        String[] columnas = {
            "ID",
            "Fecha",
            "Producto",
            "Cantidad",
            "Subtotal"
        };

        modeloHistorial = new DefaultTableModel(columnas, 0);

        tablaHistorial = new JTable(modeloHistorial);

        JScrollPane scroll = new JScrollPane(tablaHistorial);

        add(scroll, BorderLayout.CENTER);

        cargarHistorial();
    }

    private void cargarHistorial() {

        ProductoDAO dao = new ProductoDAO();

        ArrayList<Object[]> historial =
                dao.listarHistorialVentas();

        modeloHistorial.setRowCount(0);

        for (Object[] fila : historial) {

            modeloHistorial.addRow(fila);
        }
    }
}
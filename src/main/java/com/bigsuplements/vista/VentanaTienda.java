package com.bigsuplements.vista;

import com.bigsuplements.modelo.Carrito;
import com.bigsuplements.modelo.ItemCarrito;
import com.bigsuplements.modelo.Producto;
import com.bigsuplements.modelo.Usuario;
import com.bigsuplements.persistencia.ProductoDAO;
import com.bigsuplements.vista.ventanas.VentanaHistorial;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaTienda extends JFrame {

    private static final Color COLOR_FONDO = new Color(18, 18, 24);
    private static final Color COLOR_PANEL = new Color(28, 28, 38);
    private static final Color COLOR_ACENTO = new Color(255, 87, 34);
    private static final Color COLOR_ACENTO2 = new Color(0, 200, 150);
    private static final Color COLOR_TEXTO = new Color(230, 230, 240);
    private static final Color COLOR_TEXTO_GRIS = new Color(140, 140, 160);
    private static final Color COLOR_HEADER_TABLA = new Color(40, 40, 55);
    private static final Color COLOR_FILA_PAR = new Color(24, 24, 34);
    private static final Color COLOR_FILA_IMPAR = new Color(30, 30, 42);

    private static final Color COLOR_BOTON_COMPRAR = new Color(255, 87, 34);
    private static final Color COLOR_BOTON_CARRITO = new Color(0, 170, 120);
    private static final Color COLOR_BOTON_LIMPIAR = new Color(100, 60, 60);

    private static final Font FONT_TITULO = new Font("SansSerif", Font.BOLD, 22);
    private static final Font FONT_SUBTITULO = new Font("SansSerif", Font.BOLD, 13);
    private static final Font FONT_TABLA = new Font("Monospaced", Font.PLAIN, 12);
    private static final Font FONT_BOTON = new Font("SansSerif", Font.BOLD, 13);
    private static final Font FONT_LABEL = new Font("SansSerif", Font.PLAIN, 12);

    private JTable tablaCatalogo;
    private DefaultTableModel modeloCatalogo;
    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;
    private JLabel lblSubtotal;
    private JLabel lblIva;
    private JLabel lblTotal;
    private JSpinner spinnerCantidad;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final Carrito carrito = new Carrito();
    private ArrayList<Producto> productos = new ArrayList<>();

    private Usuario usuarioLogueado;

    public VentanaTienda(Usuario usuario) {

        this.usuarioLogueado = usuario;

        setTitle("BigSuplements - Catálogo");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1250, 720);

        setMinimumSize(new Dimension(950, 650));

        setLocationRelativeTo(null);

        getContentPane().setBackground(COLOR_FONDO);

        setLayout(new BorderLayout());

        add(crearHeader(), BorderLayout.NORTH);

        add(crearPanelCentral(), BorderLayout.CENTER);

        add(crearStatusBar(), BorderLayout.SOUTH);

        cargarProductos();
    }

    private JPanel crearHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(COLOR_PANEL);

        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_ACENTO),
                new EmptyBorder(12, 20, 12, 20)
        ));

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        izquierda.setOpaque(false);

        JLabel logo = new JLabel("💪 ");

        logo.setFont(new Font("SansSerif", Font.PLAIN, 26));

        JLabel titulo = new JLabel("BigSuplements");

        titulo.setFont(FONT_TITULO);

        titulo.setForeground(COLOR_ACENTO);

        JLabel subtitulo = new JLabel("  —  Catálogo de Suplementos");

        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        subtitulo.setForeground(COLOR_TEXTO_GRIS);

        izquierda.add(logo);

        izquierda.add(titulo);

        izquierda.add(subtitulo);

        JButton btnRecargar = crearBoton(
                "↻ Recargar",
                COLOR_HEADER_TABLA
        );

        btnRecargar.addActionListener(e -> cargarProductos());

        JButton btnHistorial = crearBoton(
                "📜 Historial",
                COLOR_ACENTO2
        );

        btnHistorial.addActionListener(e -> {

            VentanaHistorial historial = new VentanaHistorial();

            historial.setVisible(true);
        });

        // BOTÓN ADMIN
        JButton btnAdmin = crearBoton(
                "⚙ Panel Admin",
                new Color(70, 70, 90)
        );

        // LÓGICA DE VISIBILIDAD 
        if (usuarioLogueado != null && !usuarioLogueado.getRol().equalsIgnoreCase("ADMIN")) {
            btnAdmin.setVisible(false);
        }

        btnAdmin.addActionListener(e -> {

            PanelAdmin admin = new PanelAdmin(usuarioLogueado);
            admin.setVisible(true);
        });

        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );

        panelBotones.setOpaque(false);
        panelBotones.add(btnAdmin);
        panelBotones.add(btnHistorial);
        panelBotones.add(btnRecargar);
        header.add(izquierda, BorderLayout.WEST);
        header.add(panelBotones, BorderLayout.EAST);

        return header;
    }

    private JSplitPane crearPanelCentral() {
        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                crearPanelCatalogo(),
                crearPanelCarrito()
        );
        split.setDividerLocation(730);
        split.setDividerSize(3);
        split.setBorder(null);
        split.setBackground(COLOR_FONDO);
        split.setResizeWeight(0.65);
        return split;
    }

    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(12, 12, 10, 6));
        JLabel lbl = new JLabel("📦 Catálogo de Productos");
        lbl.setFont(FONT_SUBTITULO);
        lbl.setForeground(COLOR_ACENTO2);
        String[] columnas = {"ID", "Tipo", "Nombre", "Marca", "Precio ($)", "Stock", "Detalles"};
        modeloCatalogo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tablaCatalogo = new JTable(modeloCatalogo);
        estilizarTabla(tablaCatalogo);
        int[] anchos = {40, 90, 220, 160, 100, 60, 220};
        for (int i = 0; i < anchos.length; i++) {
            tablaCatalogo.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        JScrollPane scroll = new JScrollPane(tablaCatalogo);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_HEADER_TABLA));
        scroll.getViewport().setBackground(COLOR_FILA_PAR);
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBotones.setBackground(COLOR_FONDO);
        JLabel lblCant = new JLabel("Cantidad:");
        lblCant.setForeground(COLOR_TEXTO_GRIS);
        lblCant.setFont(FONT_LABEL);
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        spinnerCantidad.setPreferredSize(new Dimension(60, 32));
        JButton btnAgregar = crearBoton("🛒 Agregar al Carrito", COLOR_BOTON_CARRITO);
        btnAgregar.addActionListener(e -> agregarAlCarrito());
        panelBotones.add(lblCant);
        panelBotones.add(spinnerCantidad);
        panelBotones.add(btnAgregar);
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelCarrito() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(12, 6, 10, 12));
        JLabel lbl = new JLabel("🛒 Mi Carrito");
        lbl.setFont(FONT_SUBTITULO);
        lbl.setForeground(COLOR_ACENTO);
        String[] cols = {"Producto", "Cant.", "P. Unit", "Subtotal"};
        modeloCarrito = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tablaCarrito = new JTable(modeloCarrito);
        estilizarTabla(tablaCarrito);
        int[] anchos = {180, 60, 90, 100};
        for (int i = 0; i < anchos.length; i++) {
            tablaCarrito.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        JScrollPane scroll = new JScrollPane(tablaCarrito);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_HEADER_TABLA));
        scroll.getViewport().setBackground(COLOR_FILA_PAR);
        JPanel resumen = new JPanel(new GridLayout(3, 2, 5, 5));
        resumen.setBackground(COLOR_PANEL);
        resumen.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(COLOR_ACENTO), new EmptyBorder(10, 12, 10, 12)));
        lblSubtotal = new JLabel("$ 0,00");
        lblIva = new JLabel("$ 0,00");
        lblTotal = new JLabel("$ 0,00");
        lblTotal.setForeground(COLOR_ACENTO);
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
        resumen.add(etiquetaResumen("Subtotal:"));
        resumen.add(lblSubtotal);
        resumen.add(etiquetaResumen("IVA (19%):"));
        resumen.add(lblIva);
        resumen.add(etiquetaResumen("TOTAL:"));
        resumen.add(lblTotal);
        JPanel acciones = new JPanel(new GridLayout(1, 2, 10, 0));
        acciones.setBackground(COLOR_FONDO);
        JButton btnComprar = crearBoton("✔ Confirmar Compra", COLOR_BOTON_COMPRAR);
        JButton btnVaciar = crearBoton("✕ Vaciar Carrito", COLOR_BOTON_LIMPIAR);
        btnComprar.addActionListener(e -> confirmarCompra());
        btnVaciar.addActionListener(e -> vaciarCarrito());
        acciones.add(btnComprar);
        acciones.add(btnVaciar);
        JPanel sur = new JPanel(new BorderLayout(0, 8));
        sur.setBackground(COLOR_FONDO);
        sur.add(resumen, BorderLayout.CENTER);
        sur.add(acciones, BorderLayout.SOUTH);
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setBackground(new Color(14, 14, 20));
        JLabel lbl = new JLabel("BigSuplements © 2025 | SQLite conectado");
        lbl.setForeground(COLOR_TEXTO_GRIS);
        bar.add(lbl);
        return bar;
    }

    private void cargarProductos() {
        modeloCatalogo.setRowCount(0);
        productos = productoDAO.listarTodo();
        for (Producto p : productos) {
            modeloCatalogo.addRow(new Object[]{p.getId(), p.getTipo(), p.getNombre(), p.getMarca(), p.getPrecio(), p.getStock(), p.getDetalles()});
        }
    }

    private void agregarAlCarrito() {
        int fila = tablaCatalogo.getSelectedRow();
        if (fila < 0) {
            mostrarError("Selecciona un producto.");
            return;
        }
        Producto producto = productos.get(fila);
        int cantidad = (int) spinnerCantidad.getValue();
        if (cantidad > producto.getStock()) {
            mostrarError("Stock insuficiente. Disponible: " + producto.getStock());
            return;
        }
        carrito.agregar(producto, cantidad);
        actualizarTablaCarrito();
        JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
    }

    private void actualizarTablaCarrito() {
        modeloCarrito.setRowCount(0);
        for (ItemCarrito item : carrito.getItems()) {
            modeloCarrito.addRow(new Object[]{item.getProducto().getNombre(), item.getCantidad(), "$ " + item.getProducto().getPrecio(), "$ " + item.getSubtotal()});
        }
        lblSubtotal.setText("$ " + carrito.getSubtotal());
        lblIva.setText("$ " + carrito.getIva());
        lblTotal.setText("$ " + carrito.getTotal());
    }

    private void confirmarCompra() {

        if (carrito.estaVacio()) {

            mostrarError("El carrito está vacío.");

            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Confirmar compra?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        int idVenta = productoDAO.registrarVenta(carrito, usuarioLogueado.getId());

        if (idVenta > 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "✅ Compra registrada.\nID Venta: #" + idVenta
            );

            carrito.vaciar();

            actualizarTablaCarrito();

            cargarProductos();

        } else {

            mostrarError("Error al registrar compra.");
        }
    }

    private void vaciarCarrito() {
        carrito.vaciar();
        actualizarTablaCarrito();
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setBackground(COLOR_FILA_PAR);
        tabla.setForeground(COLOR_TEXTO);
        tabla.setFont(FONT_TABLA);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.setRowHeight(25);
        tabla.setGridColor(new Color(45, 45, 60));
        tabla.setSelectionBackground(new Color(255, 87, 34, 80));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.getTableHeader().setBackground(COLOR_HEADER_TABLA);
        tabla.getTableHeader().setForeground(COLOR_ACENTO2);
        tabla.getTableHeader().setFont(FONT_SUBTITULO);
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? COLOR_FILA_PAR : COLOR_FILA_IMPAR);
                    c.setForeground(COLOR_TEXTO);
                }
                return c;
            }
        });
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BOTON);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }

    private JLabel etiquetaResumen(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(COLOR_TEXTO_GRIS);
        lbl.setFont(FONT_SUBTITULO);
        return lbl;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}

package com.bigsuplements.persistencia;

import com.bigsuplements.modelo.Carrito;
import com.bigsuplements.modelo.Creatina;
import com.bigsuplements.modelo.ItemCarrito;
import com.bigsuplements.modelo.Producto;
import com.bigsuplements.modelo.Proteina;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

public class ProductoDAO {

    private final Connection conexion;

    public ProductoDAO() {

        this.conexion = ConexionDB.getInstance().getConexion();
    }

    /**
     * LISTAR TODOS LOS PRODUCTOS
     */
    public ArrayList<Producto> listarTodo() {

        ArrayList<Producto> lista = new ArrayList<>();

        String sql = "SELECT * FROM productos ORDER BY nombre";

        try (
                Statement stmt = conexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {

            while (rs.next()) {

                String tipo = rs.getString("tipo");

                if ("PROTEINA".equalsIgnoreCase(tipo)) {

                    Proteina p = new Proteina(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("marca"),
                            rs.getDouble("precio"),
                            rs.getInt("stock"),
                            rs.getString("sabor"),
                            Proteina.TipoProteina.valueOf(
                                    rs.getString("tipo_proteina")
                            )
                    );

                    lista.add(p);

                } else if ("CREATINA".equalsIgnoreCase(tipo)) {

                    Creatina c = new Creatina(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("marca"),
                            rs.getDouble("precio"),
                            rs.getInt("stock"),
                            rs.getInt("servicios"),
                            rs.getInt("es_monohidrato") == 1
                    );

                    lista.add(c);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "[DAO] Error al listar productos: "
                    + e.getMessage()
            );
        }

        return lista;
    }

    /**
     * ACTUALIZAR STOCK
     */
    public boolean actualizarStock(int id, int cantidad) {

        String sql = """
                     UPDATE productos
                     SET stock = stock - ?
                     WHERE id = ?
                     AND stock >= ?
                     """;

        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {

            pstmt.setInt(1, cantidad);

            pstmt.setInt(2, id);

            pstmt.setInt(3, cantidad);

            int filas = pstmt.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.err.println(
                    "[DAO] Error al actualizar stock: "
                    + e.getMessage()
            );

            return false;
        }
    }

    /**
     * REGISTRAR VENTA
     */
    public int registrarVenta(Carrito carrito, int idUsuario) {

        String sqlVenta = """
                           INSERT INTO ventas
                           (id_usuario, fecha, subtotal, iva, total)
                           VALUES (?, datetime('now'), ?, ?, ?)
                           """;

        String sqlDetalle = """
                             INSERT INTO detalle_ventas
                             (id_venta, id_producto, cantidad,
                              precio_unit, subtotal)
                             VALUES (?, ?, ?, ?, ?)
                             """;

        try {

            conexion.setAutoCommit(false);

            int idVenta;

            try (
                    PreparedStatement ps = conexion.prepareStatement(
                            sqlVenta,
                            Statement.RETURN_GENERATED_KEYS
                    )
            ) {

                ps.setInt(1, idUsuario);

                ps.setDouble(2, carrito.getSubtotal());

                ps.setDouble(3, carrito.getIva());

                ps.setDouble(4, carrito.getTotal());

                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();

                idVenta = keys.next()
                        ? keys.getInt(1)
                        : -1;
            }

            if (idVenta == -1) {

                conexion.rollback();

                return -1;
            }

            try (PreparedStatement ps = conexion.prepareStatement(sqlDetalle)) {

                for (ItemCarrito item : carrito.getItems()) {

                    ps.setInt(1, idVenta);

                    ps.setInt(2, item.getProducto().getId());

                    ps.setInt(3, item.getCantidad());

                    ps.setDouble(4,
                            item.getProducto().getPrecio());

                    ps.setDouble(5,
                            item.getSubtotal());

                    ps.addBatch();

                    actualizarStock(
                            item.getProducto().getId(),
                            item.getCantidad()
                    );
                }

                ps.executeBatch();
            }

            conexion.commit();

            conexion.setAutoCommit(true);

            return idVenta;

        } catch (SQLException e) {

            System.err.println(
                    "[DAO] Error al registrar venta: "
                    + e.getMessage()
            );

            try {

                conexion.rollback();

                conexion.setAutoCommit(true);

            } catch (SQLException ex) {
            }

            return -1;
        }
    }

    /**
     * HISTORIAL DE VENTAS
     */
    public ArrayList<Object[]> listarHistorialVentas() {

        ArrayList<Object[]> historial = new ArrayList<>();

        String sql = """
                     SELECT
                        dv.id,
                        v.fecha,
                        p.nombre,
                        dv.cantidad,
                        dv.subtotal
                     FROM detalle_ventas dv
                     JOIN ventas v
                        ON dv.id_venta = v.id
                     JOIN productos p
                        ON dv.id_producto = p.id
                     ORDER BY v.fecha DESC
                     """;

        try (
                Statement stmt = conexion.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {

            while (rs.next()) {

                Object[] fila = {
                    rs.getInt("id"),
                    rs.getString("fecha"),
                    rs.getString("nombre"),
                    rs.getInt("cantidad"),
                    "$ " + rs.getDouble("subtotal")
                };

                historial.add(fila);
            }

        } catch (SQLException e) {

            System.err.println(
                    "[DAO] Error al obtener historial: "
                    + e.getMessage()
            );
        }

        return historial;
    }

    /**
     * AGREGAR PRODUCTO
     */
    public boolean agregarProducto(Producto p) {

        String sql = """
                     INSERT INTO productos
                     (
                        tipo,
                        nombre,
                        marca,
                        precio,
                        stock,
                        sabor,
                        tipo_proteina,
                        servicios,
                        es_monohidrato
                     )
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                     """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, p.getTipo());

            ps.setString(2, p.getNombre());

            ps.setString(3, p.getMarca());

            ps.setDouble(4, p.getPrecio());

            ps.setInt(5, p.getStock());

            if (p instanceof Proteina proteina) {

                ps.setString(6, proteina.getSabor());

                ps.setString(7,
                        proteina.getTipoProteina().name());

                ps.setNull(8, java.sql.Types.INTEGER);

                ps.setNull(9, java.sql.Types.INTEGER);

            } else if (p instanceof Creatina creatina) {

                ps.setNull(6, java.sql.Types.VARCHAR);

                ps.setNull(7, java.sql.Types.VARCHAR);

                ps.setInt(8, creatina.getServicios());

                ps.setInt(9,
                        creatina.isEsMonohidrato() ? 1 : 0);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "[DAO] Error al agregar producto: "
                    + e.getMessage()
            );

            return false;
        }
    }

    /**
     * ELIMINAR PRODUCTO
     */
    public boolean eliminarProducto(int id) {

        String sql = "DELETE FROM productos WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "[DAO] Error al eliminar producto: "
                    + e.getMessage()
            );

            return false;
        }
    }

    /**
     * ACTUALIZAR PRODUCTO
     */
    public boolean actualizarProducto(Producto p) {

        String sql = """
                     UPDATE productos
                     SET
                        nombre = ?,
                        marca = ?,
                        precio = ?,
                        stock = ?
                     WHERE id = ?
                     """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());

            ps.setString(2, p.getMarca());

            ps.setDouble(3, p.getPrecio());

            ps.setInt(4, p.getStock());

            ps.setInt(5, p.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "[DAO] Error al actualizar producto: "
                    + e.getMessage()
            );

            return false;
        }
    }
}
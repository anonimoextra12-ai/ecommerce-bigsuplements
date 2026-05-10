package com.bigsuplements.persistencia;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class ConexionDB {
    
    
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String URL = "jdbc:sqlite:bigsuplements.db";
    private static ConexionDB instancia;
    private Connection conexion;
    
    private ConexionDB() {
        try {
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(URL);
            conexion.createStatement().execute("PRAGMA foreign_keys = ON");
            crearTablas();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error DB: " + e.getMessage());
        }
    }
    public static ConexionDB getInstance() {
        if (instancia == null) instancia = new ConexionDB();
        return instancia;
    }
    public Connection getConexion() { return conexion; }
    private void crearTablas() throws SQLException {
        try (Statement stmt = conexion.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, email TEXT UNIQUE, password TEXT, rol TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS productos (id INTEGER PRIMARY KEY AUTOINCREMENT, tipo TEXT, nombre TEXT, marca TEXT, precio REAL, stock INTEGER, sabor TEXT, tipo_proteina TEXT, servicios INTEGER, es_monohidrato INTEGER)");
            stmt.execute("CREATE TABLE IF NOT EXISTS ventas (id INTEGER PRIMARY KEY AUTOINCREMENT, id_usuario INTEGER, fecha TEXT, subtotal REAL, iva REAL, total REAL, FOREIGN KEY (id_usuario) REFERENCES usuarios(id))");
            stmt.execute("CREATE TABLE IF NOT EXISTS detalle_ventas (id INTEGER PRIMARY KEY AUTOINCREMENT, id_venta INTEGER, id_producto INTEGER, cantidad INTEGER, precio_unit REAL, subtotal REAL, FOREIGN KEY (id_venta) REFERENCES ventas(id), FOREIGN KEY (id_producto) REFERENCES productos(id))");
            insertarDatosDemo(stmt);
        }
    }
    private void insertarDatosDemo(Statement stmt) throws SQLException {
        var rsU = stmt.executeQuery("SELECT COUNT(*) FROM usuarios");
        if (rsU.getInt(1) == 0) {
            stmt.execute("INSERT INTO usuarios (nombre, email, password, rol) VALUES ('Admin', 'admin@bigsuplements.com', '123', 'ADMIN')");
            stmt.execute("INSERT INTO usuarios (nombre, email, password, rol) VALUES ('Cliente', 'cliente@mail.com', '123', 'CLIENTE')");
        }
        var rsP = stmt.executeQuery("SELECT COUNT(*) FROM productos");
        if (rsP.getInt(1) == 0) {
            stmt.execute("INSERT INTO productos (tipo,nombre,marca,precio,stock,sabor,tipo_proteina) VALUES ('PROTEINA','Whey Gold','Optimum',129900,50,'Chocolate','WHEY')");
            stmt.execute("INSERT INTO productos (tipo,nombre,marca,precio,stock,servicios,es_monohidrato) VALUES ('CREATINA','Creatine Mono','Optimum',69900,60,83,1)");
        }
    }
}
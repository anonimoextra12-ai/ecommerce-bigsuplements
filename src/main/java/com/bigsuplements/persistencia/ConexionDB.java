package com.bigsuplements.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {

    private static final String DRIVER = "org.sqlite.JDBC";

    private static final String URL
            = "jdbc:sqlite:bigsuplements.db";

    private static ConexionDB instancia;

    private Connection conexion;

    // Constructorcito
    private ConexionDB() {

        try {

            Class.forName(DRIVER);

            conexion = DriverManager.getConnection(URL);

            conexion.createStatement().execute(
                    "PRAGMA foreign_keys = ON"
            );

            crearTablas();

            System.out.println(
                    "[DB] Conexión establecida correctamente."
            );

        } catch (ClassNotFoundException e) {

            System.err.println(
                    "[DB] Driver SQLite no encontrado: "
                    + e.getMessage()
            );

        } catch (SQLException e) {

            System.err.println(
                    "[DB] Error al conectar: "
                    + e.getMessage()
            );

        }

    }

    // el singleton es esto para que no se te olvide oiste!!!!
    public static ConexionDB getInstance() {

        if (instancia == null) {

            instancia = new ConexionDB();

        }

        return instancia;

    }

    // el get de conexion
    public Connection getConexion() {

        return conexion;

    }

    // las tablas
    private void crearTablas() throws SQLException {

        try (Statement stmt = conexion.createStatement()) {

            // TABLA USUARIOS
            stmt.execute("""

                CREATE TABLE IF NOT EXISTS usuarios (

                    id INTEGER PRIMARY KEY AUTOINCREMENT,

                    nombre TEXT NOT NULL,

                    email TEXT NOT NULL UNIQUE,

                    password TEXT NOT NULL,

                    rol TEXT NOT NULL

                )

            """);

            // TaBla productos
            stmt.execute("""

                CREATE TABLE IF NOT EXISTS productos (

                    id INTEGER PRIMARY KEY AUTOINCREMENT,

                    tipo TEXT NOT NULL,

                    nombre TEXT NOT NULL,

                    marca TEXT NOT NULL,

                    precio REAL NOT NULL,

                    stock INTEGER NOT NULL,



                    sabor TEXT,

                    tipo_proteina TEXT,



                    servicios INTEGER,

                    es_monohidrato INTEGER

                )

            """);

            // Tabla ventas
            stmt.execute("""

                CREATE TABLE IF NOT EXISTS ventas (

                    id INTEGER PRIMARY KEY AUTOINCREMENT,

                    id_usuario INTEGER,

                    fecha TEXT NOT NULL,

                    subtotal REAL NOT NULL,

                    iva REAL NOT NULL,

                    total REAL NOT NULL,



                    FOREIGN KEY (id_usuario)

                    REFERENCES usuarios(id)

                )

            """);

            // Tabla de los detalles de la venta
            stmt.execute("""

                CREATE TABLE IF NOT EXISTS detalle_ventas (

                    id INTEGER PRIMARY KEY AUTOINCREMENT,

                    id_venta INTEGER NOT NULL,

                    id_producto INTEGER NOT NULL,

                    cantidad INTEGER NOT NULL,

                    precio_unit REAL NOT NULL,

                    subtotal REAL NOT NULL,



                    FOREIGN KEY (id_venta)

                    REFERENCES ventas(id),



                    FOREIGN KEY (id_producto)

                    REFERENCES productos(id)

                )

            """);

            insertarDatosDemoSiVacio(stmt);

        }

    }

    // Insertar los datos demo
    private void insertarDatosDemoSiVacio(
            Statement stmt
    ) throws SQLException {

        // usuarios demo
        try (var rsU = stmt.executeQuery(
                "SELECT COUNT(*) AS cnt FROM usuarios"
        )) {

            if (rsU.getInt("cnt") == 0) {

                // ADMIN
                stmt.execute("""

                    INSERT INTO usuarios

                    (nombre, email, password, rol)

                    VALUES

                    ('Administrador',

                     'admin@bigsuplements.com',

                     '123',

                     'ADMIN')

                """);

                // CLIENTE
                stmt.execute("""

                    INSERT INTO usuarios

                    (nombre, email, password, rol)

                    VALUES

                    ('Cliente General',

                     'cliente@mail.com',

                     '123',

                     'CLIENTE')

                """);

                System.out.println(
                        "[DB] Usuarios demo creados."
                );

            }

        }

        // Productos DEMO
        try (var rs = stmt.executeQuery(
                "SELECT COUNT(*) AS cnt FROM productos"
        )) {

            if (rs.getInt("cnt") > 0) {

                return;

            }

        }

        String[] demos = {
            """

            INSERT INTO productos

            (tipo,nombre,marca,precio,stock,sabor,tipo_proteina)

            VALUES

            ('PROTEINA',

             'Whey Gold Standard',

             'Optimum Nutrition',

             129900,

             50,

             'Chocolate',

             'WHEY')

            """,
            """

            INSERT INTO productos

            (tipo,nombre,marca,precio,stock,sabor,tipo_proteina)

            VALUES

            ('PROTEINA',

             'Iso-100 Hydrolyzed',

             'Dymatize',

             159900,

             30,

             'Vainilla',

             'ISOLATE')

            """,
            """

            INSERT INTO productos

            (tipo,nombre,marca,precio,stock,sabor,tipo_proteina)

            VALUES

            ('PROTEINA',

             'Combat Protein',

             'MusclePharm',

             99900,

             40,

             'Fresa',

             'WHEY')

            """,
            """

            INSERT INTO productos

            (tipo,nombre,marca,precio,stock,servicios,es_monohidrato)

            VALUES

            ('CREATINA',

             'Creatine Monohydrate',

             'Optimum Nutrition',

             69900,

             60,

             83,

             1)

            """,
            """

            INSERT INTO productos

            (tipo,nombre,marca,precio,stock,servicios,es_monohidrato)

            VALUES

            ('CREATINA',

             'Creatine HCL',

             'Kaged Muscle',

             89900,

             45,

             75,

             0)

            """

        };

        for (String sql : demos) {

            stmt.execute(sql);

        }

        System.out.println(
                "[DB] Productos demo insertados."
        );

    }

    // CERRAR LA CONEXION!!!!!
    public void cerrar() {

        try {

            if (conexion != null
                    && !conexion.isClosed()) {

                conexion.close();

                System.out.println(
                        "[DB] Conexión cerrada."
                );

            }

        } catch (SQLException e) {

            System.err.println(
                    "[DB] Error al cerrar conexión: "
                    + e.getMessage()
            );

        }

    }

}

# 🛒 [Bigsuplements]

> Proyecto final — Java POO · [Curso:1603] · [2026]

## 👥 Integrantes

| Nombre | GitHub |
|--------|--------|
| [Reinel Pupo Morelo] | [@usuario](https://github.com/usuario) |
| [Emiliano Pérez Meza] | [@usuario](https://github.com/usuario) |
| [Alexander Polo Narvaez] | [@usuario](https://github.com/usuario) |
  [Melanie Polo Beltran] | [@usuario](https://github.com/usuario) |
---

## 📋 Descripción
Venderemos suplementación deportiva de alto rendimiento, pero segmentada para facilitar la navegación en el codigo
Línea de Fuerza: Creatinas y Pre-entrenos (foco en energía y potencia)
Línea de Recuperación: Proteínas (Whey, Vegana, Isolatada) y Aminoácidos.
El sistema no solo vende; te pregunta tu objetivo (ej: "Subir masa muscular") y tu peso, y automáticamente te sugiere un Combo Personalizado.

---

## 🚀 Cómo ejecutar

### Requisitos
- Java JDK 17+
- SQLite JDBC Driver
- IDE: NetBeans

### Pasos
bash
# 1. Clonar el repositorio
git clone https://github.com/Antomaker/competencia.git
# 2. Abrir el proyecto en NetBeans.
# 3. Asegurarse de que la librería sqlite-jdbc esté en el Classpath.
# 4. Ejecutar la clase com.bigsuplements.vista.VentanaTienda


---

## 🏗️ Tecnologías usadas

| Categoría | Tecnología elegida |
|-----------|-------------------|
| Lenguaje | Java |
| UI / Framework | Swing | <!-- Swing / JavaFX / Spring Boot / otro --> |
| Persistencia | SQLite |  <!-- Archivos / SQLite / MySQL / H2 / otro --> |
| IDE | NetBeans | <!-- NetBeans / IntelliJ / Eclipse --> |

---

## 🧩 Diagrama de clases UML

![Diagrama de clases](docs/uml/diagrama-clases.png)

---

## 📐 Diagrama de casos de uso

![Casos de uso](docs/uml/casos-de-uso.png)

---

## 🎯 Funcionalidades implementadas

- [ x ] Gestión de productos
- [ x ] Gestión de usuarios / clientes
- [ x ] Carrito de compras
- [ x ] Flujo de pedido y pago
- [ x ] Historial de pedidos
- [ x ] Interfaz gráfica o web funcional
- [ x ] Persistencia de datos
- [ ] <!-- opcional: búsqueda, reportes, descuentos... -->

---

## 📐 Conceptos POO aplicados

| Concepto | Clase / método donde se aplica |
|----------|-------------------------------|
| Herencia | Proteina y Creatina extienden de la clase base Producto|
| Encapsulación |Uso de modificadores private y métodos Getters/Setters en el paquete modelo. |
| Polimorfismo |El método listarTodo() maneja una lista de Producto pero instancia objetos específicos según el tipo en la BD. |
| Abstracción |Clase Producto define la estructura base necesaria para cualquier suplemento. |
| Colecciones |Uso intensivo de ArrayList<ItemCarrito> y ArrayList<Producto>. |
| Excepciones |Manejo de SQLException con bloques try-with-resources y Rollbacks en transacciones |

---

## 🖼️ Capturas

<!-- Agrega screenshots en la carpeta assets/ -->

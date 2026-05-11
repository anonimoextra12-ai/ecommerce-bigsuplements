# 🛒 [Bigsuplements]

> Proyecto final — Java POO · [Curso:1603] · [2026]

## 👥 Integrantes

| Nombre | GitHub |
|--------|--------|
| [Reinel Pupo Morelo] | [(https://github.com/Puupoox)] |
| [Emiliano Pérez Meza] | [(https://github.com/anonimoextra12-ai)] |
| [Alexander Polo Narvaez] | [(https://github.com/melaniepolo0214-alt)] |
  [Melanie Polo Beltran] | [(https://github.com/alexanderpolonarvaez24-art)] |
---

## 📋 Descripción
Venderemos suplementación deportiva de alto rendimiento, pero segmentada para facilitar la navegación en el codigo
Línea de Fuerza: Creatinas (foco en energía y potencia),
Línea de Recuperación: Proteínas (Whey, Vegana, Isolatada).


---

## 🚀 Cómo ejecutar

### Requisitos
- Java JDK 17+
- SQLite JDBC Driver
- IDE: NetBeans

### Pasos
- #1 Clonar el repositorio
git clone https://github.com/Antomaker/competencia.git
- #2 Abrir el proyecto en netbeans
- #3 Asegurarse de que la libreria sqlite-jdbc esta en Classpath
  (En el panel izquierdo, abre la carpeta Dependencies.
Asegúrate de que aparezca el archivo sqlite-jdbc-3.45.3.0.jar.
Si aparece con un icono de advertencia, haz clic derecho sobre el proyecto y selecciona "Clean and Build" para que Maven descargue la librería automáticamente.)

- #4 Click derecho en  la clase com.bigsuplements.vista.ventanas.VentanaLogin, ejecutar run file.
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

![(<img width="3655" height="3799" alt="diagrama-uml (1)" src="https://github.com/user-attachments/assets/9f237704-7502-4af4-a37d-ff8d8ecdd598" />)]


---

## 📐 Diagrama de casos de uso

![<img width="1649" height="1157" alt="casos-de-uso" src="https://github.com/user-attachments/assets/23755d31-504b-45d4-8c6a-d46ac1eeea93" />]


---

## 🎯 Funcionalidades implementadas

- [x] Gestión de productos
- [x] Gestión de usuarios / clientes
- [x] Carrito de compras
- [x] Flujo de pedido y pago
- [x] Historial de pedidos
- [x] Interfaz gráfica o web funcional
- [x] Persistencia de datos
- [ ] <!-- opcional: búsqueda, reportes, descuentos... -->

---

## 📐 Conceptos POO aplicados

| Concepto | Clase / método donde se aplica |
|----------|-------------------------------|
| Herencia | VentanaLogin y VentanaHistorial extienden de JFrame. También Proteina y Creatina extienden de Producto.|
| Encapsulación |Uso de modificadores private en atributos como txtEmail y txtPassword, accediendo a datos mediante getters/setters en la clase Usuario. |
| Polimorfismo |El método listarTodo() en los DAO maneja objetos de tipo Producto, permitiendo tratar Proteina y Creatina de forma genérica. |
| Abstracción |Clase Producto define la estructura base necesaria para cualquier suplemento. |
| Colecciones |Uso de ArrayList<Object[]> en VentanaHistorial para manejar dinámicamente los datos provenientes de listarHistorialVentas(). |
| Excepciones |Manejo de errores de base de datos en los DAO mediante bloques try-catch para capturar SQLException. |

---

## 🖼️ Capturas
<img width="400" height="371" alt="image" src="https://github.com/user-attachments/assets/af63b7c3-8971-4002-a00b-2b058c191e42" />
<img width="1229" height="706" alt="image" src="https://github.com/user-attachments/assets/b86327a0-dcb1-44cc-9f91-08ec28450079" />
<img width="983" height="593" alt="image" src="https://github.com/user-attachments/assets/70d4da4a-cabd-4cd3-9935-b6263f114c0a" />
<img width="681" height="534" alt="image" src="https://github.com/user-attachments/assets/43217a7e-1924-493b-b08e-bab97bc8f9f6" />



<!-- Agrega screenshots en la carpeta assets/ -->

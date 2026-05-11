package com.bigsuplements.modelo;

public abstract class Producto {

    private int id;
    private String nombre;
    private String marca;
    private double precio;
    private int stock;

    public Producto() {
    }

    public Producto(int id, String nombre, String marca, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMarca() {
        return marca;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String n) {
        this.nombre = n;
    }

    public void setMarca(String m) {
        this.marca = m;
    }

    public void setPrecio(double p) {
        this.precio = p;
    }

    public void setStock(int s) {
        this.stock = s;
    }

    public abstract String getTipo();

    public abstract String getDetalles();

    @Override

    public String toString() {
        return String.format("[%d] %s - %s | $%.2f | Stock: %d", id, nombre, marca, precio, stock);
    }
}

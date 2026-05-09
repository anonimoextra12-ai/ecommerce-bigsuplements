package com.bigsuplements.modelo;
public class Creatina extends Producto {
    private int servicios;
    private boolean esMonohidrato;
    public Creatina() { 
        super(); 
    }
    public Creatina(int id, String nombre, String marca, double precio, int stock, int servicios, boolean esMonohidrato) {
        super(id, nombre, marca, precio, stock);
        this.servicios = servicios;
        this.esMonohidrato = esMonohidrato;
    }
    public int getServicios() { 
        return servicios; 
    }
    public boolean isEsMonohidrato() { 
        return esMonohidrato; 
    }
    public void setServicios(int s) { 
        this.servicios = s; 
    }
    public void setEsMonohidrato(boolean e) { 
        this.esMonohidrato = e; 
    }
    @Override public String getTipo() { 
        return "Creatina"; 
    }
    @Override public String getDetalles() {
        return "Servicios: " + servicios + " | " + (esMonohidrato ? "Monohidrato" : "Otra fórmula");
    }
}
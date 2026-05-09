
package com.bigsuplements.modelo;


public class Proteina extends Producto {
 
    public enum TipoProteina { WHEY, ISOLATE }
 
    private String       sabor;
    private TipoProteina tipoProteina;
 
    public Proteina() { super(); }
 
    public Proteina(int id, String nombre, String marca, double precio, int stock,
                    String sabor, TipoProteina tipoProteina) {
        super(id, nombre, marca, precio, stock);
        this.sabor        = sabor;
        this.tipoProteina = tipoProteina;
    }
 
    // ── Getters ──
    public String       getSabor()        { return sabor; }
    public TipoProteina getTipoProteina() { return tipoProteina; }
 
    // ── Setters ──
    public void setSabor(String s)               { this.sabor = s; }
    public void setTipoProteina(TipoProteina tp) { this.tipoProteina = tp; }
 
    @Override public String getTipo()     { return "Proteína"; }
    @Override public String getDetalles() {
        return "Sabor: " + sabor + " | Tipo: " + tipoProteina;
    }
}
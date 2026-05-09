package com.bigsuplements.modelo;
import java.util.ArrayList;
public class Carrito {
    private static final double IVA = 0.19;
    private ArrayList<ItemCarrito> items = new ArrayList<>();
    public void agregar(Producto producto, int cantidad) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getId() == producto.getId()) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        items.add(new ItemCarrito(producto, cantidad));
    }
    public void eliminar(int idProducto) {
        items.removeIf(i -> i.getProducto().getId() == idProducto);
    }
    public void vaciar() { 
        items.clear(); 
    }
    public ArrayList<ItemCarrito> getItems() { 
        return items; 
    }
    public double getSubtotal() {
        double total = 0;
        for (ItemCarrito i : items) {
            total += i.getSubtotal();
        }
        return total;
    }
    public double getIva() { 
        return getSubtotal() * IVA; 
    }
    public double getTotal() { 
        return getSubtotal() + getIva(); 
    }
    public boolean estaVacio() { 
        return items.isEmpty(); 
    }
}
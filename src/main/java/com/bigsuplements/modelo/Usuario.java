package com.bigsuplements.modelo;
public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String password;
    private String rol;
    public Usuario(int id, String nombre, String email, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }
}
package com.ivn.lamejortienda.clases;

public class Usuario {
    private String urlfoto;
    private String usuario;
    private String contraseña;

    public Usuario(String urlfoto, String usuario, String contraseña) {
        this.urlfoto = urlfoto;
        this.usuario = usuario;
        this.contraseña = contraseña;
    }

    public String getUsuario() { return usuario; }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getUrlfoto() { return urlfoto; }

    public void setUrlfoto(String urlfoto) { this.urlfoto = urlfoto; }
}

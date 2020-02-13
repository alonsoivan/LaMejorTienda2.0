package com.ivn.lamejortienda.clases;

public class Usuario {

    private int id;
    private String usuario;
    private String contraseña;
    private String cesta;
    private String pedido;

    public Usuario(){}

    public Usuario(String usuario, String contraseña) {
        this.usuario = usuario;
        this.contraseña = contraseña;
    }

    public Usuario(int id, String usuario, String contraseña, String cesta, String pedido) {
        this.id = id;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.cesta = cesta;
        this.pedido = pedido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCesta() {
        return cesta;
    }

    public void setCesta(String cesta) {
        this.cesta = cesta;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
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

}

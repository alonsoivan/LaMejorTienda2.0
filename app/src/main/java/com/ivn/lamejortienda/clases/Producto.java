package com.ivn.lamejortienda.clases;

import android.graphics.Bitmap;

public class Producto {
    private long id_producto;
    private Bitmap imagen;
    private String nombre;
    private String descripcion;
    private float precio;
    private String marca;
    private int popularidad;            // 1-5
    private int ventas;
    private int cantidad;
    private boolean cesta;

    //private boolean oferta = false;   Quizá para LMT 2.0

    public Producto(long id_producto, Bitmap imagen, String nombre, String descripcion, float precio, String marca, int popularidad, int ventas, int cantidad, boolean cesta) {
        this.id_producto = id_producto;
        this.imagen = imagen;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.marca = marca;
        this.popularidad = popularidad;
        this.ventas = ventas;
        this.cantidad = cantidad;
        this.cesta = cesta;
    }

    public Producto(){};

    public long getId_producto() {
        return id_producto;
    }

    public void setId_producto(long id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getPopularidad() {
        return popularidad;
    }

    public void setPopularidad(int popularidad) {
        this.popularidad = popularidad;
    }

    public String toString(){
        return nombre;
    }

    public void setImagen(Bitmap img){imagen = img;}

    public Bitmap getImagen(){ return imagen; }

    public String getMarca() { return marca; }

    public void setMarca(String marca) { this.marca = marca; }

    public int getVentas() { return ventas; }

    public void setVentas(int ventas) { this.ventas = ventas; }

    public int getCantidad() { return cantidad; }

    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public boolean isInCesta() { return cesta; }

    public void setCesta(boolean cesta) { this.cesta = cesta; }

    public void anadirCesta(){
        cesta = true;
    }
}

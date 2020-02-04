package com.ivn.lamejortienda.clases;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Modelo {

    private int id;
    private String nombre;
    private String descripcion;
    private byte[] foto;
    private float precio;
    private String marca;
    private int popularidad;
    private int ventas;
    private int cantidad;
    private boolean cesta;

    public Modelo() {}

    public Modelo(int id, String nombre, String descripcion, byte[] foto, float precio, String marca, int popularidad, int ventas, int cantidad, boolean cesta) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
        this.precio = precio;
        this.marca = marca;
        this.popularidad = popularidad;
        this.ventas = ventas;
        this.cantidad = cantidad;
        this.cesta = cesta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getPopularidad() {
        return popularidad;
    }

    public void setPopularidad(int popularidad) {
        this.popularidad = popularidad;
    }

    public int getVentas() {
        return ventas;
    }

    public void setVentas(int ventas) {
        this.ventas = ventas;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public boolean isCesta() {
        return cesta;
    }

    public void setCesta(boolean cesta) {
        this.cesta = cesta;
    }

    public Bitmap getBitmap(){
        try {
            return  BitmapFactory.decodeByteArray( foto, 0,
                    foto.length);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

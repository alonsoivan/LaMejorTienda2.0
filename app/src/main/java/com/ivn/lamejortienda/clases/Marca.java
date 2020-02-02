package com.ivn.lamejortienda.clases;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Marca {
    private String nombre;
    private byte[] logo;

    public Marca(String nombre, byte[] logo) {
        this.nombre = nombre;
        this.logo = logo;
    }

    public Marca(){}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public Bitmap getBitmap(){
        try {
            return  BitmapFactory.decodeByteArray( logo, 0,
                    logo.length);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

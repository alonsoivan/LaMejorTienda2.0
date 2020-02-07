package com.ivn.lamejortienda.clases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Objetos {

    // prueba server web
    public static List<Modelo> listaModelos = new ArrayList<>();
    public static List<Marca> listaMarcas = new ArrayList<>();
    public static HashMap<Integer , Modelo> diccionarioModelos = new HashMap<Integer, Modelo>();
    public static Modelo modelo;

    // revisar lo q sobra
    public static final String URL_SERVIDOR = "http://192.168.1.128:8080";
    public static final String URL_MARCAS = "/marcas";
    public static final String URL_MODELOS = "/modelos";
    public static final String URL_MODELO = "/modelo?id=";  //
    public static final String URL_MODELOS_POR_MARCA = "/modelos_por_marca?marca=";
    public static final String URL_USUARIOS = "/usuarios";

}

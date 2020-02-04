package com.ivn.lamejortienda.clases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Objetos {

    public static Semaphore sem = new Semaphore(0);
    // prueba server web
    public static List<Marca> listaMarcas = new ArrayList<>();
    public static List<Modelo> listaModelos = new ArrayList<>();
    public static List<Modelo> listaModelosPorMarca = new ArrayList<>();
    public static HashMap<Integer , Modelo> diccionarioModelos = new HashMap<Integer, Modelo>();
    public static Modelo modelo;

    //
    public static final String URL_SERVIDOR = "http://192.168.1.128:8080";
    public static final String URL_MARCAS = "/marcas";
    public static final String URL_MODELOS = "/modelos";
    public static final String URL_MODELO = "/modelo?id=";  //
    public static final String URL_MODELOS_POR_MARCA = "/modelos_por_marca?marca=";
    public static final String URL_USUARIOS = "/usuarios";


    public static void cargarMarcas(String urlMarcas) {
        TareaDescarga t = new TareaDescarga();
        t.execute(urlMarcas);
    }

    public static void cargarModelos(String urlModelos) {
        TareaDescarga t = new TareaDescarga();
        t.execute(urlModelos);
    }

    public static void cargarUsuarios(String urlUsuarios) {
        TareaDescarga t = new TareaDescarga();
        t.execute(urlUsuarios);
    }

    public static void cargarModeloPorId(String urlModelo, int id) {
        TareaDescarga t = new TareaDescarga();
        t.execute(urlModelo,""+id);
    }

    public static void cargarModelosPorMarca(String urlModelosPorMarca, String marca) {
        TareaDescarga t = new TareaDescarga();
        t.execute(urlModelosPorMarca,""+marca);
    }
}

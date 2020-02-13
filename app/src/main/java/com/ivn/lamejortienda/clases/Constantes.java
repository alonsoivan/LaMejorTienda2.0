package com.ivn.lamejortienda.clases;

public class Constantes {
    public static final String BASE_DATOS = "laMejorTiendaDB.db";

    // TABLAS
    public static final String TABLA_USUARIOS = "USUARIOS";

    // TABLA USUARIOS
    public static final String USUARIO = "USUARIO";
    public static final String CONTRASEÑA = "CONTRASEÑA";

    // SERVIDOR
    public static final String URL_SERVIDOR = "http://192.168.1.128:8080";
    public static final String URL_MARCAS = "/marcas";
    public static final String URL_MODELOS = "/modelos";
    public static final String URL_MODELO = "/modelo?id=";  //
    public static final String URL_MODELOS_POR_MARCA = "/modelos_por_marca?marca=";
    public static final String URL_USUARIOS = "/usuarios";
    public static final String URL_COMPROBAR_USUARIO = "/comprobar_usuario?usuario=";
    public static final String URL_NUEVO_USUARIO = "/nuevo_usuario?usuario=";
    public static final String URL_AÑADIR_CESTA = "/añadir_cesta?usuario=";
    public static final String URL_ESTA_EN_CESTA = "/esta_en_cesta?usuario=";
    public static final String URL_CESTA_USUARIO = "/cesta_usuario?usuario=";

}

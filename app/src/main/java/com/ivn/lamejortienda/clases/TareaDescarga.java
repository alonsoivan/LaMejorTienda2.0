package com.ivn.lamejortienda.clases;

import android.os.AsyncTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TareaDescarga extends AsyncTask<String, Void, Void> {

    // prueba server web
    public static List<Marca> listaMarcas = new ArrayList<>();
    public static final String URL_SERVIDOR = "http://192.168.1.128:8080";

    /*
     * En este método se debe escribir el código de la tarea que se desea
     * realizar en segundo plano.
     * Hay que tener en cuenta que Android no nos permitirá acceder a
     * ningún componente de la GUI desde este método
     */
    @Override
    protected Void doInBackground(String... params) {
        System.out.println("-2----+++-++-+-+-+-+-+-+-+-+-+-++--2-4--4-4-4-4-4-4--4-4-4-4-44");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        Marca[] marcasArray = restTemplate.getForObject(URL_SERVIDOR + "/marcas", Marca[].class);
        listaMarcas.addAll(Arrays.asList(marcasArray));
        System.out.println(listaMarcas.size()+"E-E-E-E-E--E-E-E+E+E+E++E+E+E+E++E+E+E");
        return null;
    }

    /*
     * Este método se ejecuta cuando se cancela la tarea
     * Permite interactuar con la GUI
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    /*
     * Este método se ejecuta a medida que avanza la tarea
     * Permite, por ejemplo, actualizar parte de la GUI para
     * que el usuario pueda ver el avance de la misma
     */
    @Override
    protected void onProgressUpdate(Void... progreso) {
        super.onProgressUpdate(progreso);
    }

    /*
     * Este método se ejecuta automáticamente cuando la tarea
     * termina (cuando termina el método ''doInBackground'')
     * Permite interactuar con la GUI con lo que podemos comunicar
     * al usuario la finalización de la tarea o mensajes de error
     * si proceden
     */
    @Override
    protected void onPostExecute(Void resultado) {
        super.onPostExecute(resultado);
    }
}

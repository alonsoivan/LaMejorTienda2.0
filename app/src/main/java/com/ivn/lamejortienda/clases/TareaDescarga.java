package com.ivn.lamejortienda.clases;

import android.os.AsyncTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static com.ivn.lamejortienda.clases.Objetos.URL_MARCAS;
import static com.ivn.lamejortienda.clases.Objetos.URL_MODELO;
import static com.ivn.lamejortienda.clases.Objetos.URL_MODELOS;
import static com.ivn.lamejortienda.clases.Objetos.URL_MODELOS_POR_MARCA;
import static com.ivn.lamejortienda.clases.Objetos.URL_SERVIDOR;
import static com.ivn.lamejortienda.clases.Objetos.URL_USUARIOS;
import static com.ivn.lamejortienda.clases.Objetos.diccionarioModelos;
import static com.ivn.lamejortienda.clases.Objetos.listaMarcas;
import static com.ivn.lamejortienda.clases.Objetos.listaModelos;
import static com.ivn.lamejortienda.clases.Objetos.listaModelosPorMarca;
import static com.ivn.lamejortienda.clases.Objetos.modelo;
import static com.ivn.lamejortienda.clases.Objetos.sem;


public class TareaDescarga extends AsyncTask<String, Void, Void> {

    /*
     * En este método se debe escribir el código de la tarea que se desea
     * realizar en segundo plano.
     * Hay que tener en cuenta que Android no nos permitirá acceder a
     * ningún componente de la GUI desde este método
     */
    @Override
    protected Void doInBackground(String... params) {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {

            switch(params[0]){
                case URL_MARCAS:
                    listaMarcas.clear();
                    listaMarcas.addAll(Arrays.asList(restTemplate.getForObject(URL_SERVIDOR + params[0], Marca[].class)));
                    System.out.println(listaMarcas.size()+" = E-E-E-E-E--E-E-E+E+E+E++E+E+E+E++E+E+E= TAMAÑO ARRAYLIST MARCAS");
                    break;
                case URL_MODELOS:
                    listaModelos.clear();
                    listaModelos.addAll(Arrays.asList(restTemplate.getForObject(URL_SERVIDOR + params[0], Modelo[].class)));

                    for(Modelo m : listaModelos)
                        diccionarioModelos.put(m.getId(),m);

                    System.out.println(listaModelos.size()+" = E-E-E-E-E--E-E-E+E+E+E++E+E+E+E++E+E+E= TAMAÑO ARRAYLIST MODELOS");
                    break;
                case URL_USUARIOS:
                    //listaUsuarios.addAll(Arrays.asList(restTemplate.getForObject(URL_SERVIDOR + params[0], Usuario[].class)));
                    break;
                case URL_MODELO:   // quitar?
                    modelo = restTemplate.getForObject(URL_SERVIDOR + params[0] + params[1], Modelo.class);
                    System.out.println(modelo.getNombre());
                    break;
                case URL_MODELOS_POR_MARCA:
                    listaModelosPorMarca.clear();
                    listaModelosPorMarca.addAll(Arrays.asList(restTemplate.getForObject(URL_SERVIDOR + params[0] + params[1], Modelo[].class)));
                    System.out.println(listaModelosPorMarca.size()+" = E-E-E-E-E--E-E-E+E+E+E++E+E+E+E++E+E+E= TAMAÑO ARRAYLIST MODELOS POR RMARCA");
                    sem.release();
                    break;

            }

        }catch (Exception e){
            e.printStackTrace();
        }

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

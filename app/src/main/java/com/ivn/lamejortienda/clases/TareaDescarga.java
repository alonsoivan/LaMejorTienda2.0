package com.ivn.lamejortienda.clases;

import android.os.AsyncTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static com.ivn.lamejortienda.clases.Constantes.URL_MARCAS;
import static com.ivn.lamejortienda.clases.Constantes.URL_MODELO;
import static com.ivn.lamejortienda.clases.Constantes.URL_MODELOS;
import static com.ivn.lamejortienda.clases.Constantes.URL_SERVIDOR;
import static com.ivn.lamejortienda.clases.Constantes.URL_USUARIOS;
import static com.ivn.lamejortienda.clases.Objetos.diccionarioModelos;
import static com.ivn.lamejortienda.clases.Objetos.listaMarcas;
import static com.ivn.lamejortienda.clases.Objetos.listaModelos;
import static com.ivn.lamejortienda.clases.Objetos.modelo;


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

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onProgressUpdate(Void... progreso) {
        super.onProgressUpdate(progreso);
    }

    @Override
    protected void onPostExecute(Void resultado) {
        super.onPostExecute(resultado);
    }
}

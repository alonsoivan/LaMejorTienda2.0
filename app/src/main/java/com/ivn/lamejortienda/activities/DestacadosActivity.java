package com.ivn.lamejortienda.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ivn.lamejortienda.R;
import com.ivn.lamejortienda.clases.Marca;
import com.ivn.lamejortienda.clases.Modelo;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Comparator;

import static com.ivn.lamejortienda.clases.Constantes.URL_MARCAS;
import static com.ivn.lamejortienda.clases.Constantes.URL_MODELOS;
import static com.ivn.lamejortienda.clases.Constantes.URL_SERVIDOR;
import static com.ivn.lamejortienda.clases.Objetos.listaMarcas;
import static com.ivn.lamejortienda.clases.Objetos.listaModelos;


public class DestacadosActivity extends AppCompatActivity implements View.OnClickListener {

    private String usr;

    ImageView marca1;
    ImageView marca2;
    ImageView marca3;
    ImageView marca4;

    ImageView modelo1;
    ImageView modelo2;
    ImageView modelo3;
    ImageView modelo4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destacados);

        usr = getIntent().getStringExtra("usr");

        marca1 = findViewById(R.id.marca1);
        marca2 = findViewById(R.id.marca2);
        marca3 = findViewById(R.id.marca3);
        marca4 = findViewById(R.id.marca4);

        modelo1 = findViewById(R.id.modelo1);
        modelo2 = findViewById(R.id.modelo2);
        modelo3 = findViewById(R.id.modelo3);
        modelo4 = findViewById(R.id.modelo4);

        if(listaModelos.size() == 0 || listaMarcas.size() == 0)
            new TareaDescargaMarcasYModelos().execute();
        else
            ponerFotos();
    }

    public void ponerFotos(){

        if(listaMarcas.size()>4) {
            marca1.setImageBitmap(listaMarcas.get(0).getBitmap());
            marca2.setImageBitmap(listaMarcas.get(1).getBitmap());
            marca3.setImageBitmap(listaMarcas.get(2).getBitmap());
            marca4.setImageBitmap(listaMarcas.get(4).getBitmap());
        }else
            Toast.makeText(this,R.string.conexion_fallida,Toast.LENGTH_SHORT).show();

        if(listaModelos.size()>3){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                listaModelos.sort(Comparator.comparing(Modelo::getPopularidad).reversed());
            }
            modelo1.setImageBitmap(listaModelos.get(0).getBitmap());
            modelo1.setContentDescription(String.valueOf(listaModelos.get(0).getId()));
            modelo2.setImageBitmap(listaModelos.get(1).getBitmap());
            modelo2.setContentDescription(String.valueOf(listaModelos.get(1).getId()));
            modelo3.setImageBitmap(listaModelos.get(2).getBitmap());
            modelo3.setContentDescription(String.valueOf(listaModelos.get(2).getId()));
            modelo4.setImageBitmap(listaModelos.get(3).getBitmap());
            modelo4.setContentDescription(String.valueOf(listaModelos.get(3).getId()));
        }else
            Toast.makeText(this, R.string.conexion_fallida,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        Intent intentListado = new Intent(this, ListadoActivity.class);
        Intent intentModelo = new Intent(this, ProductoActivity.class);
        intentListado.putExtra("usr", usr);
        intentModelo.putExtra("usr", usr);

        switch (v.getId()) {
            case R.id.marca1:
            case R.id.marca2:
            case R.id.marca3:
            case R.id.marca4:

                // pasas la desc de la marca que haya pulsado para filtrar por ella en el spinner

                intentListado.putExtra("marca", Integer.parseInt(findViewById(v.getId()).getContentDescription().toString()));

            case R.id.btVerTodo:

                startActivity(intentListado);
                break;

            case R.id.modelo1:
            case R.id.modelo2:
            case R.id.modelo3:
            case R.id.modelo4:

                String id = findViewById(v.getId()).getContentDescription().toString();

                intentModelo.putExtra("idModelo",id );
                startActivity(intentModelo);

                break;

            default:
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_bar_preferencias:

                Intent in = new Intent(this, PreferenciasActivity.class);
                startActivity(in);

                break;
            case R.id.action_bar_acerca_de:

                AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogStyle);
                builder.setTitle(R.string.acerca_de );
                builder.setMessage(R.string.acerca_de_mensaje);
                builder.setNeutralButton(R.string.volver ,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){dialog.dismiss();}});

                builder.create().show();

                break;
            case R.id.action_bar_carrito:
                Intent carrito = new Intent(this,CestaActivity.class);
                carrito.putExtra("usr",usr);
                startActivity(carrito);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class TareaDescargaMarcasYModelos extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            listaMarcas.clear();
            listaModelos.clear();

            listaMarcas.addAll(Arrays.asList(restTemplate.getForObject(URL_SERVIDOR + URL_MARCAS, Marca[].class)));
            listaModelos.addAll(Arrays.asList(restTemplate.getForObject(URL_SERVIDOR + URL_MODELOS, Modelo[].class)));

            System.out.println(listaMarcas.get(0).getNombre());
            System.out.println(listaModelos.get(0).getNombre());

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

            ponerFotos();
        }
    }
}
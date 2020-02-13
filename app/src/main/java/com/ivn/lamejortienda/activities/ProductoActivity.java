package com.ivn.lamejortienda.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.ivn.lamejortienda.R;
import com.ivn.lamejortienda.clases.Database;
import com.ivn.lamejortienda.clases.Modelo;
import com.ivn.lamejortienda.clases.Usuario;
import com.ivn.lamejortienda.clases.Util;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Comparator;

import static com.ivn.lamejortienda.clases.Constantes.URL_AÑADIR_CESTA;
import static com.ivn.lamejortienda.clases.Constantes.URL_ESTA_EN_CESTA;
import static com.ivn.lamejortienda.clases.Constantes.URL_MODELOS_POR_MARCA;
import static com.ivn.lamejortienda.clases.Constantes.URL_SERVIDOR;
import static com.ivn.lamejortienda.clases.Objetos.diccionarioModelos;
import static com.ivn.lamejortienda.clases.Objetos.listaModelos;
import static com.ivn.lamejortienda.clases.Objetos.modelo;


public class ProductoActivity extends AppCompatActivity implements View.OnClickListener {
    private String usr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        // quitar?
        Database db = new Database(this);
        //Producto producto = db.getProducto(getIntent().getLongExtra("idModelo",1));
        //getIntent().getIntExtra("idModelo",1)

        modelo = diccionarioModelos.get(getIntent().getIntExtra("idModelo",1));

        TextView tvNombre = findViewById(R.id.tvNombre);
        tvNombre.setText(modelo.getNombre());

        TextView tvPrecio = findViewById(R.id.tvPrecio);
        tvPrecio.setText(Util.format(modelo.getPrecio()));


        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        boolean info = preferencias.getBoolean("opcion_ver_informacion",false);
        if(info) {
            TextView tvDes = findViewById(R.id.tvDes);
            tvDes.setText(modelo.getDescripcion());
        }else {
            TextView tvTituloDes = findViewById(R.id.tvTituloDes);
            tvTituloDes.setText("");
        }

        ImageView ivProducto = findViewById(R.id.ivProducto);
        ivProducto.setImageBitmap(modelo.getBitmap());


        Button btComprar = findViewById(R.id.btComprar);
        Button btAddCesta = findViewById(R.id.btAddCesta);


        btComprar.setOnClickListener(this);
        btAddCesta.setOnClickListener(this);


        usr = getIntent().getStringExtra("usr");

        // Usuario
        TextView tvUsr = findViewById(R.id.tvUsr);
        tvUsr.setOnClickListener(this);

        if(usr != null)
            tvUsr.setText(usr);



    }

    @Override // Arreglar
    public void onClick(View v) {

        int index = listaModelos.indexOf(modelo);
        switch (v.getId()){
            case R.id.btComprar:

                new TareaComprobarCesta().execute(URL_SERVIDOR,usr,String.valueOf(modelo.getId()));

                listaModelos.set(index,modelo);


                Intent carrito = new Intent(getApplicationContext(), CestaActivity.class);
                carrito.putExtra("usr", usr);
                startActivity(carrito);

                break;
            case R.id.btAddCesta:

                new TareaComprobarCesta().execute(URL_SERVIDOR,usr,String.valueOf(modelo.getId()));

                listaModelos.set(index,modelo);

                break;
            case R.id.tvUsr:
                Util.login(this);
                break;
        }
    }

    class TareaComprobarCesta extends AsyncTask<String, Void, Void> {
        int res;

        @Override
        protected Void doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            res = restTemplate.getForObject(URL_SERVIDOR + URL_ESTA_EN_CESTA + params[1] + "&modelo=" + params[2], Integer.class);

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onProgressUpdate(Void... progreso) { super.onProgressUpdate(progreso); }

        @Override
        protected void onPostExecute(Void resultado) {
            super.onPostExecute(resultado);
            if (res == 1)
                Toast.makeText(getApplicationContext(),R.string.producto_ya_en_cesta,Toast.LENGTH_SHORT).show();
            else {
                new TareaAñadirCesta().execute(URL_SERVIDOR, usr, String.valueOf(modelo.getId()));
            }
        }
    }


    class TareaAñadirCesta extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            restTemplate.getForObject(URL_SERVIDOR + URL_AÑADIR_CESTA + params[1] + "&modelo=" + params[2], Modelo[].class);

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
            Toast.makeText(getApplicationContext(), R.string.producto_añadido_cesta , Toast.LENGTH_SHORT).show();
        }
    }

}

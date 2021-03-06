package com.ivn.lamejortienda.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ivn.lamejortienda.R;
import com.ivn.lamejortienda.clases.Modelo;
import com.ivn.lamejortienda.clases.ModeloAdapterListado;
import com.ivn.lamejortienda.clases.Objetos;
import com.ivn.lamejortienda.clases.Util;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.ivn.lamejortienda.clases.Constantes.URL_MODELOS_POR_MARCA;
import static com.ivn.lamejortienda.clases.Constantes.URL_SERVIDOR;


public class ListadoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private String usr;

    private ArrayList<Modelo> modelos;  // Todos
    private List<Modelo> listaModelosPorMarca = new ArrayList<>();

    private ModeloAdapterListado adaptador;
    private Spinner spinnerOrden;
    private Spinner spinnerFiltro;
    private ListView listViewModelos;
    private ProgressBar pbLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        usr = getIntent().getStringExtra("usr");


        TextView tvUsr = findViewById(R.id.tvUsr);
        tvUsr.setOnClickListener(this);
        // Usuario
        if(usr != null)
            tvUsr.setText(usr);


        modelos = new ArrayList<>();

        listViewModelos = findViewById(R.id.lvListaProductos);

        listViewModelos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentModelo = new Intent(getApplicationContext(), ProductoActivity.class);
                intentModelo.putExtra("usr",usr);
                intentModelo.putExtra("idModelo",String.valueOf(modelos.get(position).getId()));
                startActivity(intentModelo);
            }
        });

        adaptador = new ModeloAdapterListado(this,modelos);

        listViewModelos.setAdapter(adaptador);

        // Spinners
        spinnerOrden = findViewById(R.id.spinner_orden);
        spinnerFiltro = findViewById(R.id.spinner_filtro);

        ArrayAdapter adaptadorSpinnerFiltro = ArrayAdapter.createFromResource(this,R.array.filtroMarcas,R.layout.spinner_item);
        ArrayAdapter adaptadorSpinnerOrden = ArrayAdapter.createFromResource(this,R.array.ordenLista,R.layout.spinner_item);

        spinnerFiltro.setAdapter(adaptadorSpinnerFiltro);
        spinnerFiltro.setOnItemSelectedListener(this);
        spinnerOrden.setAdapter(adaptadorSpinnerOrden);
        spinnerOrden.setOnItemSelectedListener(this);

        // PROGRESSBAR
        pbLista = findViewById(R.id.pbLista);

        // boton flotante para ir al carrito rápidamente
        FloatingActionButton carrito = findViewById(R.id.btCarrito);
        carrito.setOnClickListener(this);

        // poner al spinner la marca destacada que hayan seleccionado
        spinnerFiltro.setSelection(getIntent().getIntExtra("marca",0));

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // filtrar la lista por la marca que selecionen y
        // ordenar por lo que seleccionen, dos spinners

        switch (parent.getId()) {
            case R.id.spinner_filtro:

                String marca = spinnerFiltro.getSelectedItem().toString();

                modelos.clear();

                if (marca.equals("TODAS")) {
                    modelos.addAll(Objetos.listaModelos);

                    String orden = spinnerOrden.getSelectedItem().toString();

                    switch (orden) {
                        case "PRECIO ASCENDENTE":
                            modelos.sort(Comparator.comparing(Modelo::getPrecio));
                            break;
                        case "PRECIO DESCENDENTE":
                            modelos.sort(Comparator.comparing(Modelo::getPrecio).reversed());
                            break;
                        case "POPULARIDAD":
                            modelos.sort(Comparator.comparing(Modelo::getPopularidad).reversed());
                            break;
                        case "VENTAS":
                            modelos.sort(Comparator.comparing(Modelo::getVentas).reversed());
                            break;
                    }

                    adaptador.notifyDataSetChanged();
                } else {
                    adaptador.notifyDataSetChanged();
                    pbLista.setVisibility(View.VISIBLE);
                    TareaDescarga t = new TareaDescarga();
                    t.execute(URL_MODELOS_POR_MARCA, marca);
                }
                break;
            case R.id.spinner_orden:

                String orden = spinnerOrden.getSelectedItem().toString();

                switch (orden) {
                    case "PRECIO ASCENDENTE":
                        modelos.sort(Comparator.comparing(Modelo::getPrecio));
                        break;
                    case "PRECIO DESCENDENTE":
                        modelos.sort(Comparator.comparing(Modelo::getPrecio).reversed());
                        break;
                    case "POPULARIDAD":
                        modelos.sort(Comparator.comparing(Modelo::getPopularidad).reversed());
                        break;
                    case "VENTAS":
                        modelos.sort(Comparator.comparing(Modelo::getVentas).reversed());
                        break;
                }
                adaptador.notifyDataSetChanged();
                break;
        }
    }

    class TareaDescarga extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            listaModelosPorMarca.clear();
            listaModelosPorMarca.addAll(Arrays.asList(restTemplate.getForObject(URL_SERVIDOR + URL_MODELOS_POR_MARCA + params[1], Modelo[].class)));

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

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void resultado) {
            super.onPostExecute(resultado);
            modelos.addAll(listaModelosPorMarca);

            String orden = spinnerOrden.getSelectedItem().toString();

            switch (orden) {
                case "PRECIO ASCENDENTE":
                    modelos.sort(Comparator.comparing(Modelo::getPrecio));
                    break;
                case "PRECIO DESCENDENTE":
                    modelos.sort(Comparator.comparing(Modelo::getPrecio).reversed());
                    break;
                case "POPULARIDAD":
                    modelos.sort(Comparator.comparing(Modelo::getPopularidad).reversed());
                    break;
                case "VENTAS":
                    modelos.sort(Comparator.comparing(Modelo::getVentas).reversed());
                    break;
            }

            adaptador.notifyDataSetChanged();
            pbLista.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tvUsr:
                Util.login(this,usr);
                break;
            case R.id.btCarrito:
                Intent carrito = new Intent(this,CestaActivity.class);
                carrito.putExtra("usr",usr);
                startActivity(carrito);
                break;
            default:
                break;
        }
    }
}

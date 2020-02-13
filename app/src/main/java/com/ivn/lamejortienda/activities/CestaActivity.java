package com.ivn.lamejortienda.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ivn.lamejortienda.R;
import com.ivn.lamejortienda.clases.Modelo;
import com.ivn.lamejortienda.clases.ModeloAdapterCesta;
import com.ivn.lamejortienda.clases.Util;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ivn.lamejortienda.clases.Constantes.URL_CESTA_USUARIO;
import static com.ivn.lamejortienda.clases.Constantes.URL_ELIMINAR_MODELO_CESTA;
import static com.ivn.lamejortienda.clases.Constantes.URL_REALIZAR_PEDIDO;
import static com.ivn.lamejortienda.clases.Constantes.URL_SERVIDOR;

public class CestaActivity extends AppCompatActivity implements View.OnClickListener {

    private String usr;

    public static ArrayList<Modelo> modelosCesta;
    public static ModeloAdapterCesta adaptador;
    public static float total = 0;
    public static TextView tvTotal;
    public ProgressBar pbCesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cesta);

        usr = getIntent().getStringExtra("usr");

        pbCesta = findViewById(R.id.pbCesta);
        pbCesta.setVisibility(View.VISIBLE);
        new TareaGetCestaUsuario().execute(URL_CESTA_USUARIO,usr);

        // Usuario
        TextView tvUsr = findViewById(R.id.tvUsr);
        tvUsr.setOnClickListener(this);
        if(usr != null)
            tvUsr.setText(usr);


        Button btCancelar = findViewById(R.id.btCancelar);
        Button btFinalizarYComprar = findViewById(R.id.btFinalizarYComprar);

        btCancelar.setOnClickListener(this);
        btFinalizarYComprar.setOnClickListener(this);


        // LISTVIEW
        modelosCesta = new ArrayList<>();
        ListView lvModelos = findViewById(R.id.lvListaCesta);
        registerForContextMenu(lvModelos);
        adaptador = new ModeloAdapterCesta(this,modelosCesta);
        lvModelos.setAdapter(adaptador);


        // Obtener el precio total de la cesta
        tvTotal = findViewById(R.id.tvTotal);
        total = 0;
        for (Modelo modelo: modelosCesta)
            total += (modelo.getPrecio()*modelo.getCantidad());

        tvTotal.setText(Util.format(total));
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvTotal = findViewById(R.id.tvTotal);
        total = 0;
        for (Modelo modelo: modelosCesta)
            total += (modelo.getPrecio()*modelo.getCantidad());

        tvTotal.setText(Util.format(total));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btCancelar:
                finish();
                break;
            case R.id.btFinalizarYComprar:
                if(modelosCesta.size()>0){

                    if(usr != null) {
                        // Dialogo
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle(R.string.finalizar_y_pagar);
                        builder.setMessage(R.string.pregunta_finalizar_compra);
                        builder.setPositiveButton("Recogida en tienda", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(getApplicationContext(), MapaActivity.class));

                                modelosCesta.clear();
                                adaptador.notifyDataSetChanged();

                                ((TextView) findViewById(R.id.tvTotal)).setText(Util.format(0));
                            }
                        });
                        builder.setNegativeButton("Envio a domicilio", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), R.string.compra_exitosa, Toast.LENGTH_SHORT).show();

                                modelosCesta.clear();
                                adaptador.notifyDataSetChanged();

                                ((TextView) findViewById(R.id.tvTotal)).setText(Util.format(0));
                            }

                        });
                        builder.create().show();

                        // Realizamos el pedido
                        new TareaRealizarPedido().execute(URL_REALIZAR_PEDIDO,usr);

                    }else
                        Toast.makeText(this, R.string.debe_iniciar_sesion ,Toast.LENGTH_SHORT).show();

                }else
                    Toast.makeText(this, R.string.cesta_vacia ,Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvUsr:
                Util.login(this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.context_menu_cesta, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int itemSeleccionado = info.position;
        final TextView tvTotal = findViewById(R.id.tvTotal);

        final Modelo modelo = modelosCesta.get(itemSeleccionado);
        switch (item.getItemId()) {
            case R.id.context_eliminar:

                // quito el producto de la cesta y de la lsitview
                // Dialogo
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.cuidado );
                builder.setMessage(R.string.pregunta_eliminar_producto_cesta );
                builder.setPositiveButton(R.string.eliminar ,   new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        modelosCesta.remove(modelo);

                        new TareaEliminarModeloCesta().execute(URL_ELIMINAR_MODELO_CESTA,usr,String.valueOf(modelo.getId()));
                    }
                });
                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

                break;
            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }

    public class TareaGetCestaUsuario extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            modelosCesta.addAll(Arrays.asList(restTemplate.getForObject(URL_SERVIDOR + URL_CESTA_USUARIO + params[1] , Modelo[].class)));

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
            adaptador.notifyDataSetChanged();
            for (Modelo modelo: modelosCesta)
                total += (modelo.getPrecio()*modelo.getCantidad());

            tvTotal.setText(Util.format(total));

            pbCesta.setVisibility(View.INVISIBLE);
        }
    }

    public class TareaRealizarPedido extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            restTemplate.getForObject(URL_SERVIDOR + URL_REALIZAR_PEDIDO + params[1] , Modelo[].class);

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

    public class TareaEliminarModeloCesta extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            restTemplate.getForObject(URL_SERVIDOR + URL_ELIMINAR_MODELO_CESTA + params[1] + "&modelo=" + params[2] , Modelo[].class);

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
            adaptador.notifyDataSetChanged();
            total = 0;
            for (Modelo modelo: modelosCesta)
                total += (modelo.getPrecio()*modelo.getCantidad());

            tvTotal.setText(Util.format(total));
        }
    }
}

package com.ivn.lamejortienda.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ivn.lamejortienda.R;
import com.ivn.lamejortienda.clases.Database;
import com.ivn.lamejortienda.clases.Modelo;
import com.ivn.lamejortienda.clases.ModeloAdapterListado;
import com.ivn.lamejortienda.clases.Objetos;
import com.ivn.lamejortienda.clases.Producto;
import com.ivn.lamejortienda.clases.ProductoAdapterListado;
import com.ivn.lamejortienda.clases.Usuario;
import com.ivn.lamejortienda.clases.Util;

import java.util.ArrayList;
import java.util.Comparator;

import static com.ivn.lamejortienda.clases.Objetos.URL_MODELOS_POR_MARCA;
import static com.ivn.lamejortienda.clases.Objetos.cargarModelosPorMarca;
import static com.ivn.lamejortienda.clases.Objetos.listaModelosPorMarca;
import static com.ivn.lamejortienda.clases.Objetos.sem;


public class ListadoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private String usr;

    private ArrayList<Modelo> modelos;

    private ModeloAdapterListado adaptador;
    private Spinner spinnerOrden;
    private Spinner spinnerFiltro;
    private ListView listaModelos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        usr = getIntent().getStringExtra("usr");

        // Usuario
        TextView tvUsr = findViewById(R.id.tvUsr);

        if(usr != null) {
            Database db = new Database(this);
            ImageView ivUsr = findViewById(R.id.ivUsr);

            Usuario usuario = db.getUsuario(usr);
            ivUsr.setImageBitmap(BitmapFactory.decodeFile(usuario.getUrlfoto()));
            tvUsr.setText(usuario.getUsuario());
        }
        tvUsr.setOnClickListener(this);

        modelos = new ArrayList<>();

        listaModelos = findViewById(R.id.lvListaProductos);

        // CAMBIAR //ª

        listaModelos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // PROVISIONALLL --------
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentModelo = new Intent(getApplicationContext(), ProductoActivity.class);
                intentModelo.putExtra("usr",usr);
                intentModelo.putExtra("idModelo",modelos.get(position).getId());
                startActivity(intentModelo);
            }
        });


        adaptador = new ModeloAdapterListado(this,modelos);

        listaModelos.setAdapter(adaptador);

        // Spinners
        spinnerOrden = findViewById(R.id.spinner_orden);
        spinnerFiltro = findViewById(R.id.spinner_filtro);

        ArrayAdapter adaptadorSpinnerFiltro = ArrayAdapter.createFromResource(this,R.array.filtroMarcas,android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter adaptadorSpinnerOrden = ArrayAdapter.createFromResource(this,R.array.ordenLista,android.R.layout.simple_spinner_dropdown_item);

        spinnerFiltro.setAdapter(adaptadorSpinnerFiltro);
        spinnerFiltro.setOnItemSelectedListener(this);
        spinnerOrden.setAdapter(adaptadorSpinnerOrden);
        //spinnerOrden.setOnItemSelectedListener(this);


        // boton flotante para ir al carrito rápidamente
        FloatingActionButton carrito = findViewById(R.id.btCarrito);
        carrito.setOnClickListener(this);

        // poner al spinner la marca destacada que hayan seleccionado  //
        spinnerFiltro.setSelection(getIntent().getIntExtra("marca",0));

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // filtrar la lista por la marca que selecionen y
        // ordenar por lo que seleccionen, dos spinners

        String marca = spinnerFiltro.getSelectedItem().toString();

        // FALTA ORDENAR




        modelos.clear();

        try {
            if(marca.equals("TODAS"))
                modelos.addAll(Objetos.listaModelos);
            else {
                cargarModelosPorMarca(URL_MODELOS_POR_MARCA, marca);
                sem.acquire();
                modelos.addAll(listaModelosPorMarca);
            }

            // ORDENAR, orden ascendente
            //modelos.sort(Comparator.comparing(Modelo::getVentas));

            adaptador.notifyDataSetChanged();
        } catch (InterruptedException e) { e.printStackTrace(); }



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // PROVISIONAL 4
        // revisar si se puede NO selecionar algo
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tvUsr:
                Util.login(this);
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

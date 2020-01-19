package com.ivn.lamejortienda.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ivn.lamejortienda.R;
import com.ivn.lamejortienda.clases.Database;
import com.ivn.lamejortienda.clases.Producto;
import com.ivn.lamejortienda.clases.ProductoAdapterListado;
import com.ivn.lamejortienda.clases.Usuario;
import com.ivn.lamejortienda.clases.Util;

import java.util.ArrayList;


public class ListadoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private String usr;

    private ArrayList<Producto> productos;
    private ProductoAdapterListado adaptador;
    private Spinner spinnerOrden;
    private Spinner spinnerFiltro;
    private ListView listaProductos;

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


        // ListView
        productos = new ArrayList<>();

        listaProductos = findViewById(R.id.lvListaProductos);

        // hacer la lsitview clickable para ir al producto que selecciones
        listaProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // PROVISIONALLL --------
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent carrito = new Intent(getApplicationContext(),ProductoActivity.class);
                carrito.putExtra("usr",usr);
                carrito.putExtra("producto",productos.get(position).getId_producto());
                startActivity(carrito);
            }
        });

        adaptador = new ProductoAdapterListado(this,productos);

        listaProductos.setAdapter(adaptador);

        // Spinners
        spinnerOrden = findViewById(R.id.spinner_orden);
        spinnerFiltro = findViewById(R.id.spinner_filtro);

        ArrayAdapter adaptadorSpinnerFiltro = ArrayAdapter.createFromResource(this,R.array.filtroMarcas,android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter adaptadorSpinnerOrden = ArrayAdapter.createFromResource(this,R.array.ordenLista,android.R.layout.simple_spinner_dropdown_item);

        spinnerFiltro.setAdapter(adaptadorSpinnerFiltro);
        spinnerFiltro.setOnItemSelectedListener(this);
        spinnerOrden.setAdapter(adaptadorSpinnerOrden);
        spinnerOrden.setOnItemSelectedListener(this);


        // boton flotante para ir al carrito rápidamente
        FloatingActionButton carrito = findViewById(R.id.btCarrito);
        carrito.setOnClickListener(this);

        // poner al spinner la marca destacada que hayan seleccionado
        spinnerFiltro.setSelection(getIntent().getIntExtra("marca",0));

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // filtrar la lista por la marca que selecionen y
        // ordenar por lo que seleccionen, dos spinners

        int posOrden = spinnerOrden.getSelectedItemPosition();
        int posMarca = spinnerFiltro.getSelectedItemPosition();

        Database db = new Database(this);
        productos.clear();
        productos.addAll(db.getProductosPorMarcaYOrden("Marca "+ posMarca,posOrden + 1 ) );
        adaptador.notifyDataSetChanged();
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

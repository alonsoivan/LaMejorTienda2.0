package com.ivn.lamejortienda.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ivn.lamejortienda.R;
import com.ivn.lamejortienda.clases.Database;
import com.ivn.lamejortienda.clases.Usuario;
import com.ivn.lamejortienda.clases.Util;

import static com.ivn.lamejortienda.clases.Objetos.diccionarioModelos;
import static com.ivn.lamejortienda.clases.Objetos.listaModelos;
import static com.ivn.lamejortienda.clases.Objetos.modelo;


public class ProductoActivity extends AppCompatActivity implements View.OnClickListener {
    private String usr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        Database db = new Database(this);
        //Producto producto = db.getProducto(getIntent().getLongExtra("idModelo",1));
        //getIntent().getIntExtra("idModelo",1)

        modelo = diccionarioModelos.get(getIntent().getIntExtra("idModelo",1));

        TextView tvNombre = findViewById(R.id.tvNombre);
        tvNombre.setText(modelo.getNombre());

        TextView tvPrecio = findViewById(R.id.tvPrecio);
        tvPrecio.setText(Util.format(modelo.getPrecio()));

        TextView tvDes = findViewById(R.id.tvDes);
        tvDes.setText(modelo.getDescripcion());

        ImageView ivProducto = findViewById(R.id.ivProducto);
        ivProducto.setImageBitmap(modelo.getBitmap());


        Button btComprar = findViewById(R.id.btComprar);
        Button btAddCesta = findViewById(R.id.btAddCesta);


        btComprar.setOnClickListener(this);
        btAddCesta.setOnClickListener(this);

        // Usuario  //
        usr = getIntent().getStringExtra("usr");

        TextView tvUsr = findViewById(R.id.tvUsr);

        if(usr != null) {
            Usuario usuario = db.getUsuario(usr);

            ImageView ivUsr = findViewById(R.id.ivUsr);

            ivUsr.setImageBitmap(BitmapFactory.decodeFile(usuario.getUrlfoto()));
            tvUsr.setText(usuario.getUsuario());
        }
        tvUsr.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        int index = listaModelos.indexOf(modelo);
        switch (v.getId()){
            case R.id.btComprar:
                Intent carrito = new Intent(this, CestaActivity.class);
                carrito.putExtra("usr",usr);

                if (!modelo.isCesta()) {
                    modelo.setCesta(true);
                }

                listaModelos.set(index,modelo);

                startActivity(carrito);

                break;
            case R.id.btAddCesta:
                if (modelo.isCesta())
                    Toast.makeText(this,R.string.producto_ya_en_cesta,Toast.LENGTH_SHORT).show();
                else {
                    modelo.setCesta(true);
                    listaModelos.set(index,modelo);
                    Toast.makeText(this, R.string.producto_añadido_cesta , Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tvUsr:
                Util.login(this);
                break;
        }
    }
}

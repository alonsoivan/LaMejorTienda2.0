package com.ivn.lamejortienda.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ivn.lamejortienda.R;


public class DestacadosActivity extends AppCompatActivity implements View.OnClickListener {

    private String usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destacados);

        usr = getIntent().getStringExtra("usr");
    }

    @Override
    public void onClick(View v) {
        Intent intentListado = new Intent(this, ListadoActivity.class);
        Intent intentProducto = new Intent(this, ProductoActivity.class);
        intentListado.putExtra("usr", usr);
        intentProducto.putExtra("usr", usr);

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

            case R.id.producto1:
            case R.id.producto2:
            case R.id.producto3:
            case R.id.producto4:

                // pasas la desc del producto sobre el que haya pulsado para ir a el

                intentProducto.putExtra("producto", Long.parseLong(findViewById(v.getId()).getContentDescription().toString()));
                startActivity(intentProducto);
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
            case R.id.action_bar_acerca_de:
                /*
                Intent acercaDe = new Intent(this,AcercaDeActivity.class);
                acercaDe.putExtra("usr",usr);
                startActivity(acercaDe);
                */
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
}
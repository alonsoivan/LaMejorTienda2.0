package com.ivn.lamejortienda.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ivn.lamejortienda.R;

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

        ponerFotos();
    }

    public void ponerFotos(){
        if(listaMarcas.size()>0) {
            marca1.setImageBitmap(listaMarcas.get(0).getBitmap());
            marca2.setImageBitmap(listaMarcas.get(1).getBitmap());
            marca3.setImageBitmap(listaMarcas.get(2).getBitmap());
            marca4.setImageBitmap(listaMarcas.get(3).getBitmap());
        }else
            Toast.makeText(this,"Conexión fallida con el servidor.",Toast.LENGTH_SHORT).show();

        if(listaModelos.size()>0){
            modelo1.setImageBitmap(listaModelos.get(0).getBitmap());
            modelo2.setImageBitmap(listaModelos.get(1).getBitmap());
            modelo3.setImageBitmap(listaModelos.get(2).getBitmap());
            modelo4.setImageBitmap(listaModelos.get(3).getBitmap());
        }else
            Toast.makeText(this,"Conexión fallida con el servidor.",Toast.LENGTH_SHORT).show();
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

                int id = Integer.parseInt(findViewById(v.getId()).getContentDescription().toString());

                // pasas la desc del producto sobre el que haya pulsado para ir a el

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
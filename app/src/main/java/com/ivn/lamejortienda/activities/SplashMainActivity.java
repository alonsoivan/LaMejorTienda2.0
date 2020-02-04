package com.ivn.lamejortienda.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.ivn.lamejortienda.R;

import static com.ivn.lamejortienda.clases.Objetos.URL_MARCAS;
import static com.ivn.lamejortienda.clases.Objetos.URL_MODELO;
import static com.ivn.lamejortienda.clases.Objetos.URL_MODELOS;
import static com.ivn.lamejortienda.clases.Objetos.URL_USUARIOS;
import static com.ivn.lamejortienda.clases.Objetos.cargarMarcas;
import static com.ivn.lamejortienda.clases.Objetos.cargarModelos;
import static com.ivn.lamejortienda.clases.Objetos.cargarUsuarios;

public class SplashMainActivity extends AppCompatActivity {

    private final int DURACION_SPLASH = 400;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_main);

        cargarMarcas(URL_MARCAS);
        cargarModelos(URL_MODELOS);
        cargarUsuarios(URL_USUARIOS);
        //cargarModeloPorId(URL_MODELO,3);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(SplashMainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, DURACION_SPLASH);
    }


}

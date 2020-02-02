package com.ivn.lamejortienda.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.ivn.lamejortienda.R;
import com.ivn.lamejortienda.clases.TareaDescarga;

import static com.ivn.lamejortienda.clases.TareaDescarga.URL_SERVIDOR;


public class SplashMainActivity extends AppCompatActivity {

    private final int DURACION_SPLASH = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_main);

        cargarMarcas();

        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(SplashMainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, DURACION_SPLASH);
    }

    public void cargarMarcas() {
        TareaDescarga t = new TareaDescarga();
        t.execute(URL_SERVIDOR);
    }


}

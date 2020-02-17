package com.ivn.lamejortienda.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.ivn.lamejortienda.R;
import com.ivn.lamejortienda.clases.Marca;
import com.ivn.lamejortienda.clases.Modelo;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static com.ivn.lamejortienda.clases.Constantes.URL_MARCAS;
import static com.ivn.lamejortienda.clases.Constantes.URL_MODELOS;
import static com.ivn.lamejortienda.clases.Constantes.URL_SERVIDOR;
import static com.ivn.lamejortienda.clases.Objetos.listaMarcas;
import static com.ivn.lamejortienda.clases.Objetos.listaModelos;

public class SplashMainActivity extends AppCompatActivity {

    private final int DURACION_SPLASH = 500;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_main);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {

            TareaDescarga t = new TareaDescarga();
            t.execute();

        }else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(SplashMainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, DURACION_SPLASH);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public class TareaDescarga extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            listaMarcas.clear();
            listaModelos.clear();

            listaMarcas.addAll(Arrays.asList(restTemplate.getForObject(URL_SERVIDOR + URL_MARCAS, Marca[].class)));
            listaModelos.addAll(Arrays.asList(restTemplate.getForObject(URL_SERVIDOR + URL_MODELOS, Modelo[].class)));

            System.out.println(listaMarcas.get(0).getLogo());
            System.out.println();

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
            Intent intent = new Intent(SplashMainActivity.this, LoginActivity.class);
            startActivity(intent);

            finish();
        }
    }

}

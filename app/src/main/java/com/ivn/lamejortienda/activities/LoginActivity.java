package com.ivn.lamejortienda.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.ivn.lamejortienda.R;
import com.ivn.lamejortienda.clases.Database;
import com.ivn.lamejortienda.clases.Modelo;
import com.ivn.lamejortienda.clases.NotificationWorker;
import com.ivn.lamejortienda.clases.Usuario;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import static androidx.work.PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS;
import static com.ivn.lamejortienda.activities.CestaActivity.modelosCesta;
import static com.ivn.lamejortienda.clases.Constantes.URL_AÑADIR_CESTA;
import static com.ivn.lamejortienda.clases.Constantes.URL_COMPROBAR_USUARIO;
import static com.ivn.lamejortienda.clases.Constantes.URL_ESTA_EN_CESTA;
import static com.ivn.lamejortienda.clases.Constantes.URL_SERVIDOR;
import static com.ivn.lamejortienda.clases.Objetos.modelo;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    CheckBox cb;
    EditText etUser;
    EditText etPass;
    ProgressBar pbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        /*
        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();
        */
        // Pruebas servicio notis
        PeriodicWorkRequest saveRequest =
                new PeriodicWorkRequest.Builder(NotificationWorker.class, MIN_PERIODIC_INTERVAL_MILLIS , TimeUnit.MILLISECONDS)
                        //.setConstraints(constraints)
                        .build();

        WorkManager.getInstance(this).enqueue(saveRequest);


        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);

        cb = findViewById(R.id.checkBox);

        pbLogin = findViewById(R.id.pbLogin);

        // Recordar datos
        Database db = new Database(this);
        Usuario usr = db.getUsuario();
        if(usr != null) {
            etUser.setText(usr.getUsuario());
            etPass.setText(usr.getContraseña());
            cb.setChecked(true);
        }

        Button btAcceder = findViewById(R.id.btAcceder);
        Button btAccederSinRegistrar = findViewById(R.id.btAccederSinRegistrar);
        Button btRegistrar = findViewById(R.id.btRegistrar);
        
        btAcceder.setOnClickListener(this);
        btAccederSinRegistrar.setOnClickListener(this);
        btRegistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btRegistrar:

                Intent intentRegistrar = new Intent(this,RegistroActivity.class);
                startActivity(intentRegistrar);

                break;

            case R.id.btAcceder:
                String usuario = etUser.getText().toString();
                String contraseña = etPass.getText().toString();

                // Recordar datos
                if(!usuario.equals("") & !contraseña.equals("")) {
                    Database db = new Database(this);
                    if (cb.isChecked()) {
                        db.drop();
                        db.recordarUsiario(new Usuario(usuario, contraseña));
                    } else
                        db.drop();

                    pbLogin.setVisibility(View.VISIBLE);
                    new TareaDescarga().execute(URL_COMPROBAR_USUARIO, usuario, contraseña);
                }else
                    Toast.makeText(this,"Campos vacios.",Toast.LENGTH_SHORT).show();

                break;

            case R.id.btAccederSinRegistrar:

                Intent intentDestacados = new Intent(this, DestacadosActivity.class);
                startActivity(intentDestacados);
                finish();
                break;

            default:
                    break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    public class TareaDescarga extends AsyncTask<String, Void, Void> {
        private int res;

        @Override
        protected Void doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            res = restTemplate.getForObject(URL_SERVIDOR + URL_COMPROBAR_USUARIO + params[1] + "&contraseña="+ params[2] , Integer.class);

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

            if(res == 1) {

                if(getIntent().getStringExtra("usr") == null)
                    for(Modelo m: modelosCesta)
                        new TareaComprobarCesta().execute(URL_SERVIDOR,etUser.getText().toString(),String.valueOf(m.getId()),"false");



                Intent intentDestacados = new Intent(getApplicationContext(), DestacadosActivity.class);
                intentDestacados.putExtra("usr", etUser.getText().toString());
                startActivity(intentDestacados);
                finish();
            }else if(res == 0)
                Toast.makeText(getApplicationContext(),"Contraseña incorrecta",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(),"Usuario incorrecto",Toast.LENGTH_SHORT).show();


            pbLogin.setVisibility(View.INVISIBLE);
        }
    }

    class TareaComprobarCesta extends AsyncTask<String, Void, Void> {
        int res;
        boolean cesta;

        @Override
        protected Void doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            cesta = Boolean.parseBoolean(params[3]);
            res = restTemplate.getForObject(URL_SERVIDOR + URL_ESTA_EN_CESTA + params[1] + "&modelo=" + params[2], Integer.class);

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onProgressUpdate(Void... progreso) { super.onProgressUpdate(progreso); }

        @Override
        protected void onPostExecute(Void resultado) {
            super.onPostExecute(resultado);
            if (res == 1){
                Toast.makeText(getApplicationContext(),R.string.producto_ya_en_cesta,Toast.LENGTH_SHORT).show();

                if(cesta){
                    Intent carrito = new Intent(getApplicationContext(), CestaActivity.class);
                    carrito.putExtra("usr", etUser.getText().toString());
                    startActivity(carrito);
                }
            }
            else {
                new TareaAñadirCesta().execute(URL_SERVIDOR, etUser.getText().toString(), String.valueOf(modelo.getId()),String.valueOf(cesta));
            }
        }
    }


    class TareaAñadirCesta extends AsyncTask<String, Void, Void> {
        boolean cesta;

        @Override
        protected Void doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            cesta = Boolean.parseBoolean(params[3]);
            restTemplate.getForObject(URL_SERVIDOR + URL_AÑADIR_CESTA + params[1] + "&modelo=" + params[2], Modelo[].class);

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
            Toast.makeText(getApplicationContext(), R.string.producto_añadido_cesta , Toast.LENGTH_SHORT).show();

            if(cesta) {
                Intent carrito = new Intent(getApplicationContext(), CestaActivity.class);
                carrito.putExtra("usr", etUser.getText().toString());
                startActivity(carrito);
            }
        }
    }
}

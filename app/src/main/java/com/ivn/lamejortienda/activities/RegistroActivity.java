package com.ivn.lamejortienda.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ivn.lamejortienda.R;
import com.ivn.lamejortienda.clases.Usuario;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static com.ivn.lamejortienda.clases.Constantes.URL_COMPROBAR_USUARIO;
import static com.ivn.lamejortienda.clases.Constantes.URL_NUEVO_USUARIO;
import static com.ivn.lamejortienda.clases.Constantes.URL_SERVIDOR;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUser;
    EditText etPass;
    EditText etRepetirPass;
    Button btRegistrar;
    ProgressBar pbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        etRepetirPass = findViewById(R.id.etRepetirPass);
        btRegistrar = findViewById(R.id.btRegistrar);
        btRegistrar.setOnClickListener(this);
        pbLogin = findViewById(R.id.pbLogin);
    }

    @Override
    public void onClick(View v) {
        String pass = etPass.getText().toString();
        String repetirPass = etRepetirPass.getText().toString();
        String user = etUser.getText().toString();

        if(pass.equals("") || repetirPass.equals("") || user.equals("")) {
            Toast t = Toast.makeText(this, R.string.campos_vacios, Toast.LENGTH_SHORT);
            t.setGravity(Gravity.TOP | Gravity.LEFT, 345, 1480);
            t.show();
        }
        else {
            pbLogin.setVisibility(View.VISIBLE);
            if (pass.equals(repetirPass)) {
                new TareaComprobarUsuario().execute(URL_COMPROBAR_USUARIO, user);
            } else {
                Toast t = Toast.makeText(this, R.string.contraseñas_no_coinciden , Toast.LENGTH_SHORT);
                t.setGravity(Gravity.TOP | Gravity.LEFT, 240, 1480);
                t.show();

                pbLogin.setVisibility(View.INVISIBLE);
            }
        }
    }

    public class TareaNuevoUsuario extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            if(params[0].equals(URL_NUEVO_USUARIO))
                restTemplate.getForObject(URL_SERVIDOR + URL_NUEVO_USUARIO + params[1] + "&contraseña="+ params[2] , Usuario[].class);

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

            Intent intentDestacados = new Intent(getApplicationContext(), DestacadosActivity.class);
            intentDestacados.putExtra("usr",etUser.getText().toString());
            startActivity(intentDestacados);
            finish();
        }
    }

    public class TareaComprobarUsuario extends AsyncTask<String, Void, Void> {
        private int res;

        @Override
        protected Void doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            res = restTemplate.getForObject(URL_SERVIDOR + URL_COMPROBAR_USUARIO + params[1] + "&contraseña=*" , Integer.class);

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

            if(res >= 0) {
                Toast.makeText(getApplicationContext(), R.string.usuario_ya_existe, Toast.LENGTH_SHORT).show();
                pbLogin.setVisibility(View.INVISIBLE);
            }else
                new TareaNuevoUsuario().execute(URL_NUEVO_USUARIO,etUser.getText().toString(),etPass.getText().toString());

        }
    }
}

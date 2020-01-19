package com.ivn.lamejortienda.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ivn.lamejortienda.R;
import com.ivn.lamejortienda.clases.Database;
import com.ivn.lamejortienda.clases.Usuario;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private int RESULTADO_CARGA_IMAGEN = 1;
    private String urlFotoUsuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CheckBox cb = findViewById(R.id.checkBox);
        Button btAcceder = findViewById(R.id.btAcceder);
        Button btAccederSinRegistrar = findViewById(R.id.btAccederSinRegistrar);
        Button btRegistrar = findViewById(R.id.btRegistrar);
        ImageView ivLogo = findViewById(R.id.ivLogo);
        ivLogo.setOnClickListener(this);
        cb.setOnClickListener(this);

        btAcceder.setOnClickListener(this);
        btAccederSinRegistrar.setOnClickListener(this);
        btRegistrar.setOnClickListener(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == RESULTADO_CARGA_IMAGEN) && (resultCode == RESULT_OK) && (data != null)) {
            // Obtiene el Uri de la imagen seleccionada por el usuario
            Uri imagenSeleccionada = data.getData();
            String[] ruta = {MediaStore.Images.Media.DATA };

            // Realiza una consulta a la galería de imágenes solicitando la imagen seleccionada
            Cursor cursor = getContentResolver().query(imagenSeleccionada, ruta, null, null, null);
            cursor.moveToFirst();

            // Obtiene la ruta a la imagen
            int indice = cursor.getColumnIndex(ruta[0]);
            String picturePath = cursor.getString(indice);
            cursor.close();

            urlFotoUsuario = picturePath;

            // Carga la imagen en una vista ImageView que se encuentra en
            // en layout de la Activity actual
            ImageView imageView = findViewById(R.id.ivLogo);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    public void onClick(View v) {
        Database db = new Database(this);
        EditText etUser = findViewById(R.id.etUser);
        EditText etPass = findViewById(R.id.etPass);

        Usuario usr = new Usuario(urlFotoUsuario,etUser.getText().toString(),etPass.getText().toString());

        Intent intentDestacados = new Intent(this, DestacadosActivity.class);
        switch (v.getId()){
            case R.id.btRegistrar:

                db.registrarUsiario(usr);
                db.close();

                break;

            case R.id.btAcceder:

                if (db.comprobarAcceso(usr)){
                    intentDestacados.putExtra("usr", etUser.getText().toString());
                    startActivity(intentDestacados);
                    finish();
                }

                break;
            case R.id.btAccederSinRegistrar:
                startActivity(intentDestacados);
                finish();
                break;

            case R.id.ivLogo:

                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULTADO_CARGA_IMAGEN);
                }
                break;

            case R.id.checkBox:
                Toast.makeText(this, R.string.proximamente ,Toast.LENGTH_SHORT).show();
                break;
            default:
                    break;
        }

        db.close();
        etPass.setText("");
        etUser.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }
}

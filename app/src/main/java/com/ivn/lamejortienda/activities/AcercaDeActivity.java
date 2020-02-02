package com.ivn.lamejortienda.activities;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ivn.lamejortienda.R;
import com.ivn.lamejortienda.clases.Database;

public class AcercaDeActivity extends AppCompatActivity {

    private String usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        usr = getIntent().getStringExtra("usr");
        if(usr != null) {
            Database db = new Database(this);
            ImageView ivPrueba = findViewById(R.id.ivPrueba);
            ivPrueba.setImageBitmap(BitmapFactory.decodeFile(db.getUsuario(usr).getUrlfoto()));
        }
    }
}

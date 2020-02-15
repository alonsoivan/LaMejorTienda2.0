package com.ivn.lamejortienda.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.ivn.lamejortienda.R;

public class PreferenciasActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setTheme(android.R.style.Theme_Material);
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.layout.preferencias);

        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);

        Preference modoPref = findPreference("opcion_modo");

        modoPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {

                boolean modo = preferencias.getBoolean("opcion_modo",false);

                if(modo)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                return true;
            }
        });

        Preference contactoPref = findPreference("opcion_contacto");

        contactoPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {

                // pruebas tlf

                String posted_by = "123456789";

                String uri = "tel:" + posted_by.trim() ;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);

                return true;
            }
        });


    }
}
package com.ivn.lamejortienda.clases;

import android.content.Context;
import android.content.Intent;

import com.ivn.lamejortienda.activities.LoginActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Util {

    public static String format(float d){
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator(',');
        DecimalFormat f = new DecimalFormat("#,##0.00",simbolos);
        return f.format(d)+" €";
    }

    public static void login(Context context, String usr){
        Intent loginIntent = new Intent(context, LoginActivity.class);
        loginIntent.putExtra("usr",usr);
        context.startActivity(loginIntent);
    }
}

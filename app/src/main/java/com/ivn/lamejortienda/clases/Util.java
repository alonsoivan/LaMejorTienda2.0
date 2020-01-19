package com.ivn.lamejortienda.clases;

import android.content.Context;
import android.content.Intent;

import com.ivn.lamejortienda.activities.LoginActivity;

import java.text.DecimalFormat;


public class Util {

    public static String format(float d){
        DecimalFormat f = new DecimalFormat("#,##0.00");
        return f.format(d)+" €";
    }

    public static void login(Context context){
        Intent carrito = new Intent(context, LoginActivity.class);
        context.startActivity(carrito);
    }
}

package com.ivn.lamejortienda.clases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.ivn.lamejortienda.clases.Constantes.BASE_DATOS;
import static com.ivn.lamejortienda.clases.Constantes.CONTRASEÑA;
import static com.ivn.lamejortienda.clases.Constantes.TABLA_USUARIOS;
import static com.ivn.lamejortienda.clases.Constantes.USUARIO;

public class Database extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    public Database(Context contexto) {
        super(contexto, BASE_DATOS, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLA_USUARIOS + "(" +
                    USUARIO + " TEXT PRIMARY KEY, "+
                    CONTRASEÑA + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO
    }

    // MÉTODOS USUARIOS

    public void drop() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLA_USUARIOS,  null, null);
    }

    public void recordarUsiario(Usuario usr) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USUARIO, usr.getUsuario());
        values.put(CONTRASEÑA, usr.getContraseña());

        db.insertOrThrow(TABLA_USUARIOS, null, values);
        db.close();
    }

    public Usuario getUsuario(){
        final String[] SELECT = {USUARIO,CONTRASEÑA};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_USUARIOS, SELECT, null, null, null, null,null);

        cursor.moveToNext();

        if (cursor.getCount()==1)
            return new Usuario(cursor.getString(0),cursor.getString(1));

        return null;
    }
}

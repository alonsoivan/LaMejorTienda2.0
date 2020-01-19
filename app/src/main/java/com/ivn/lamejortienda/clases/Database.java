package com.ivn.lamejortienda.clases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.ivn.lamejortienda.R;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.ivn.lamejortienda.clases.Constantes.BASE_DATOS;
import static com.ivn.lamejortienda.clases.Constantes.CANTIDAD;
import static com.ivn.lamejortienda.clases.Constantes.CESTA;
import static com.ivn.lamejortienda.clases.Constantes.CONTRASEÑA;
import static com.ivn.lamejortienda.clases.Constantes.DESCRIPCION;
import static com.ivn.lamejortienda.clases.Constantes.IMAGEN;
import static com.ivn.lamejortienda.clases.Constantes.MARCA;
import static com.ivn.lamejortienda.clases.Constantes.NOMBRE;
import static com.ivn.lamejortienda.clases.Constantes.POPULARIDAD;
import static com.ivn.lamejortienda.clases.Constantes.PRECIO;
import static com.ivn.lamejortienda.clases.Constantes.TABLA_PRODUCTOS;
import static com.ivn.lamejortienda.clases.Constantes.TABLA_USUARIOS;
import static com.ivn.lamejortienda.clases.Constantes.URLFOTOUSUARIO;
import static com.ivn.lamejortienda.clases.Constantes.USUARIO;
import static com.ivn.lamejortienda.clases.Constantes.VENTAS;

public class Database extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static Context context;

    public Database(Context contexto) {
        super(contexto, BASE_DATOS, null, VERSION); context = contexto;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLA_USUARIOS + "(" +
                    URLFOTOUSUARIO + " TEXT, " +
                    USUARIO + " TEXT PRIMARY KEY, "+
                    CONTRASEÑA + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLA_PRODUCTOS + "(" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    IMAGEN + " TEXT, " +
                    NOMBRE + " TEXT, " +
                    DESCRIPCION + " TEXT, " +
                    PRECIO + " REAL, " +
                    MARCA + " TEXT, " +
                    POPULARIDAD + " INTEGER, " +    // Entero del 1-5
                    VENTAS + " INTEGER, " +
                    CANTIDAD + " INTEGER, " +       // Para saber cuantos compra el usuario al hacer el pedido. Por defecto , 1
                    CESTA + " INTEGER )");          // Para saber si lo has añadido a la cesta

        poblarTablaProductos(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO
    }

    public void poblarTablaProductos(SQLiteDatabase db){
        for (int i = 0 ; i<40 ; i++) {

            ContentValues values = new ContentValues();
            values.put(IMAGEN, "logo");   ////////////////   todos los objetos se guardan con la misma foto para probar
            values.put(NOMBRE, "Producto "+(i+1));

            String desc = "Producto "+(i+1)+" ";    //////// llenar la desc de forma chapucera
            for (int j = 0 ; j<7 ; j++)
                desc += desc;

            values.put(DESCRIPCION, desc);
            values.put(PRECIO,(Math.random()*50)+5 );
            values.put(MARCA, "Marca "+ (int)(Math.floor(Math.random() * ((6 - 1) + 1) + 1)));
            values.put(POPULARIDAD, Math.floor(Math.random() * ((5 - 1) + 1) + 1) );
            values.put(VENTAS, Math.floor(Math.random() * ((500 - 1) + 1) + 1));
            values.put(CANTIDAD, 1);
            values.put(CESTA, 0);

            db.insertOrThrow(TABLA_PRODUCTOS, null, values);
        }

    }

    // MÉTODOS PRODUCTOS

    public Producto getProducto(long id){
        final String[] SELECT = {_ID, IMAGEN, NOMBRE, DESCRIPCION, PRECIO, MARCA, POPULARIDAD, VENTAS, CANTIDAD, CESTA};

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_PRODUCTOS, SELECT, _ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        return getProductos(cursor).get(0);
    }

    public void modificarProducto(Producto producto){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IMAGEN, "logo");
        values.put(NOMBRE, producto.getNombre());
        values.put(DESCRIPCION, producto.getDescripcion());
        values.put(PRECIO,producto.getPrecio());
        values.put(MARCA, producto.getMarca());
        values.put(POPULARIDAD, producto.getPopularidad() );
        values.put(VENTAS, producto.getVentas());
        values.put(CANTIDAD, producto.getCantidad());
        values.put(CESTA, producto.isInCesta());

        String[] argumentos = new String[]{String.valueOf(producto.getId_producto())};
        db.update(TABLA_PRODUCTOS, values, _ID+" = ?", argumentos);
        db.close();
    }

    public List<Producto> getProductos(Cursor cursor){
        ArrayList<Producto> listaProductos = new ArrayList<>();

        Producto producto = null;
        while (cursor.moveToNext()) {

            producto = new Producto();

            producto.setId_producto(cursor.getLong(0));
            producto.setImagen( BitmapFactory.decodeResource( context.getResources(), context.getResources().getIdentifier(cursor.getString(1), "drawable", context.getPackageName())));
            producto.setNombre(cursor.getString(2));
            producto.setDescripcion(cursor.getString(3));
            producto.setPrecio(cursor.getFloat(4));
            producto.setMarca(cursor.getString(5));
            producto.setPopularidad(cursor.getInt(6));
            producto.setVentas(cursor.getInt(7));
            producto.setCantidad(cursor.getInt(8));
            producto.setCesta(cursor.getInt(9) == 1);

            listaProductos.add(producto);
        }
        cursor.close();

        return listaProductos;
    }

    public List<Producto> getProductosPorMarcaYOrden(String marca, int orden){

        final String[] SELECT = {_ID, IMAGEN, NOMBRE, DESCRIPCION, PRECIO, MARCA, POPULARIDAD, VENTAS, CANTIDAD, CESTA};

        String ORDER_BY = PRECIO;
        switch (orden){
            case 1:
                ORDER_BY = PRECIO;
                break;
            case 2:
                ORDER_BY = PRECIO + " DESC";
                break;
            case 3:
                ORDER_BY = POPULARIDAD + " DESC";
                break;
            case 4:
                ORDER_BY = VENTAS + " DESC";
                break;
            default:
                break;
        }

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        if (marca.equals("Marca 0")) // cuando seleccione mostrar todas
            cursor = db.query(TABLA_PRODUCTOS, SELECT, null, null, null, null, ORDER_BY);
        else
            cursor = db.query(TABLA_PRODUCTOS, SELECT, MARCA + "=?", new String[]{marca}, null, null, ORDER_BY);

        return getProductos(cursor);

    }

    public List<Producto> getCesta(){
        final String[] SELECT = {_ID, IMAGEN, NOMBRE, DESCRIPCION, PRECIO, MARCA, POPULARIDAD, VENTAS, CANTIDAD, CESTA};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_PRODUCTOS, SELECT, CESTA + "=?", new String[]{"1"}, null, null, null);

        return getProductos(cursor);
    }

    // MÉTODOS USUARIOS

    public boolean existe(Usuario usr){
        final String[] SELECT = {USUARIO};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_USUARIOS, SELECT,USUARIO + "=?", new String[]{usr.getUsuario()}, null, null, null);

        if (cursor.getCount()>0) {
            db.close();
            return true;
        }
        else{
            db.close();
            return false;}

    }

    public void registrarUsiario(Usuario usr) {

        if(existe(usr))
            Toast.makeText(context, R.string.usuario_ya_existe, Toast.LENGTH_SHORT).show();
        else
            if(!usr.getUsuario().isEmpty() && !usr.getContraseña().isEmpty()) {
                if(!usr.getUrlfoto().isEmpty()) {
                    SQLiteDatabase db = getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(URLFOTOUSUARIO, usr.getUrlfoto());
                    values.put(USUARIO, usr.getUsuario());
                    values.put(CONTRASEÑA, usr.getContraseña());

                    db.insertOrThrow(TABLA_USUARIOS, null, values);
                    db.close();

                    Toast.makeText(context, R.string.registrado_correcto , Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(context, R.string.seleccione_foto , Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(context, R.string.campos_vacios , Toast.LENGTH_SHORT).show();
    }

    public boolean comprobarAcceso(Usuario usr) {

        if (existe(usr)) {

            final String[] SELECT = {CONTRASEÑA};
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(TABLA_USUARIOS, SELECT, USUARIO + "=?", new String[]{usr.getUsuario()}, null, null,null);

            cursor.moveToNext();

            String contraseña = cursor.getString(0);

            cursor.close();
            db.close();

            if(contraseña.equals(usr.getContraseña())){
                Toast.makeText(context, R.string.contraseña_correcta , Toast.LENGTH_SHORT).show();
                return true;
            }else
                Toast.makeText(context, R.string.contraseña_incorrecta , Toast.LENGTH_SHORT).show();

        }else
            Toast.makeText(context, R.string.usuario_incorrecto , Toast.LENGTH_SHORT).show();

        return false;
    }

    public Usuario getUsuario(String usr){
        final String[] SELECT = {URLFOTOUSUARIO,USUARIO,CONTRASEÑA};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_USUARIOS, SELECT, USUARIO + "=?", new String[]{usr}, null, null,null);

        cursor.moveToNext();

        return new Usuario(cursor.getString(0),cursor.getString(1),cursor.getString(2));
    }
}

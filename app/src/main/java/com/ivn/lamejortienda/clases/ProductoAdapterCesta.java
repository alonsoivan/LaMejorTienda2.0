package com.ivn.lamejortienda.clases;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;

import com.ivn.lamejortienda.R;

import java.util.ArrayList;

import static com.ivn.lamejortienda.activities.CestaActivity.adaptador;
import static com.ivn.lamejortienda.activities.CestaActivity.productos;
import static com.ivn.lamejortienda.activities.CestaActivity.total;
import static com.ivn.lamejortienda.activities.CestaActivity.tvTotal;

public class ProductoAdapterCesta extends BaseAdapter{

    private Context context;
    private ArrayList<Producto> listaProductos;
    private LayoutInflater inflater;

    public ProductoAdapterCesta(Context context, ArrayList<Producto> listaProductos) {
        this.context = context;
        this.listaProductos = listaProductos;
        inflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        ImageView foto;
        TextView nombre;
        TextView cantidad;
        TextView precio;
        Button botonMenos;
        Button botonMas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductoAdapterCesta.ViewHolder holder = null;

        // Si la View es null se crea de nuevo
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fila_cesta, null);

            holder = new ProductoAdapterCesta.ViewHolder();
            holder.foto = convertView.findViewById(R.id.ivFoto);
            holder.nombre = convertView.findViewById(R.id.tvNombre);
            holder.cantidad = convertView.findViewById(R.id.tvCantidad);
            holder.precio = convertView.findViewById(R.id.tvPrecio);
            holder.botonMenos = convertView.findViewById(R.id.btMenos);
            holder.botonMas = convertView.findViewById(R.id.btMas);

            convertView.setTag(holder);
        }
        /*
         * En caso de que la View no sea null se reutilizará con los
         * nuevos valores
         */
        else {
            holder = (ProductoAdapterCesta.ViewHolder) convertView.getTag();
        }

        final Producto producto = listaProductos.get(position);
        final Database db = new Database(context);
        final ViewHolder finalHolder = holder;

        holder.foto.setImageBitmap(producto.getImagen()); // EL bueno cuando tenga imgs
        //holder.foto.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo)); // Para poner 1 foto a todos
        holder.nombre.setText(producto.getNombre());
        holder.cantidad.setText(String.valueOf(producto.getCantidad()));
        holder.precio.setText(Util.format(producto.getPrecio()));

        if (producto.getCantidad() > 1)
            ViewCompat.setBackgroundTintList(finalHolder.botonMenos, ColorStateList.valueOf(Color.parseColor("#FFEB3B")));
        else {
            ViewCompat.setBackgroundTintList(finalHolder.botonMenos, ColorStateList.valueOf(Color.parseColor("#d1d1d1")));
            finalHolder.botonMenos.setTextColor(Color.parseColor("#A5A5A5"));
        }
        holder.botonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(producto.getCantidad()>1){
                    producto.setCantidad(producto.getCantidad()-1);
                    db.modificarProducto(producto);
                    adaptador.notifyDataSetChanged();

                    //Actualziar precio total
                    total = 0;
                    for (Producto producto1: productos)
                        total += (producto1.getPrecio()*producto1.getCantidad());

                    tvTotal.setText(Util.format(total));
                }else
                    Toast.makeText(context, R.string.eliminar_producto ,Toast.LENGTH_SHORT).show();

                if(producto.getCantidad()==1) {
                    ViewCompat.setBackgroundTintList(finalHolder.botonMenos, ColorStateList.valueOf(Color.parseColor("#d1d1d1")));
                    finalHolder.botonMenos.setTextColor(Color.parseColor("#A5A5A5"));
                }

            }
        });
        holder.botonMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                producto.setCantidad(producto.getCantidad()+1);
                db.modificarProducto(producto);
                adaptador.notifyDataSetChanged();

                //Actualziar precio total
                total = 0;
                for (Producto producto1: productos)
                    total += (producto1.getPrecio()*producto1.getCantidad());

                tvTotal.setText(Util.format(total));

                ViewCompat.setBackgroundTintList(finalHolder.botonMenos, ColorStateList.valueOf(Color.parseColor("#FFEB3B")));
                finalHolder.botonMenos.setTextColor(Color.parseColor("#000000"));
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return listaProductos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaProductos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
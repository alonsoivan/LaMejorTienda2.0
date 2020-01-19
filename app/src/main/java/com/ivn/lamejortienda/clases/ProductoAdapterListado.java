package com.ivn.lamejortienda.clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivn.lamejortienda.R;

import java.util.ArrayList;

public class ProductoAdapterListado extends BaseAdapter {
    private Context context;
    private ArrayList<Producto> listaProductos;
    private LayoutInflater inflater;

    public ProductoAdapterListado(Context context, ArrayList<Producto> listaProductos) {
        this.context = context;
        this.listaProductos = listaProductos;
        inflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        ImageView foto;
        TextView nombre;
        TextView precio;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        // Si la View es null se crea de nuevo
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fila_listado, null);

            holder = new ViewHolder();
            holder.foto = (ImageView) convertView.findViewById(R.id.ivFoto);
            holder.nombre = (TextView) convertView.findViewById(R.id.tvNombre);
            holder.precio = (TextView) convertView.findViewById(R.id.tvP);

            convertView.setTag(holder);
        }
        /*
         * En caso de que la View no sea null se reutilizará con los
         * nuevos valores
         */
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Producto producto = listaProductos.get(position);
        holder.foto.setImageBitmap(producto.getImagen()); // EL bueno cuando tenga imgs
        //holder.foto.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo)); // Para poner 1 foto a todos
        holder.nombre.setText(producto.getNombre());
        holder.precio.setText(Util.format(producto.getPrecio()));

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

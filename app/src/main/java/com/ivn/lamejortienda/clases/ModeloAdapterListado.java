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

public class ModeloAdapterListado extends BaseAdapter {
    private Context context;
    private ArrayList<Modelo> listaModelos;
    private LayoutInflater inflater;

    public ModeloAdapterListado(Context context, ArrayList<Modelo> listaModelos) {
        this.context = context;
        this.listaModelos = listaModelos;
        inflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        ImageView foto;
        TextView nombre;
        TextView precio;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ModeloAdapterListado.ViewHolder holder = null;

        // Si la View es null se crea de nuevo
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fila_listado, null);

            holder = new ModeloAdapterListado.ViewHolder();
            holder.foto = convertView.findViewById(R.id.ivFoto);
            holder.nombre = convertView.findViewById(R.id.tvNombre);
            holder.precio = convertView.findViewById(R.id.tvP);

            convertView.setTag(holder);
        }
        /*
         * En caso de que la View no sea null se reutilizará con los
         * nuevos valores
         */
        else {
            holder = (ModeloAdapterListado.ViewHolder) convertView.getTag();
        }

        Modelo modelo = listaModelos.get(position);
        holder.foto.setImageBitmap(modelo.getBitmap()); // EL bueno cuando tenga imgs
        holder.nombre.setText(modelo.getNombre());
        holder.precio.setText(Util.format(modelo.getPrecio()));

        return convertView;
    }

    @Override
    public int getCount() {
        return listaModelos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaModelos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
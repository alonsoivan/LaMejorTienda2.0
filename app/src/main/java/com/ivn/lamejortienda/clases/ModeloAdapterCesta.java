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
import static com.ivn.lamejortienda.activities.CestaActivity.total;
import static com.ivn.lamejortienda.activities.CestaActivity.tvTotal;

public class ModeloAdapterCesta extends BaseAdapter {

    private Context context;
    private ArrayList<Modelo> listaModelos;
    private LayoutInflater inflater;

    public ModeloAdapterCesta(Context context, ArrayList<Modelo> listaModelos) {
        this.context = context;
        this.listaModelos = listaModelos;
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
        ModeloAdapterCesta.ViewHolder holder = null;

        // Si la View es null se crea de nuevo
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fila_cesta, null);

            holder = new ModeloAdapterCesta.ViewHolder();
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
            holder = (ModeloAdapterCesta.ViewHolder) convertView.getTag();
        }

        final Modelo modelo = listaModelos.get(position);
        final ModeloAdapterCesta.ViewHolder finalHolder = holder;

        holder.foto.setImageBitmap(modelo.getBitmap()); // EL bueno cuando tenga imgs
        holder.nombre.setText(modelo.getNombre());
        holder.cantidad.setText(String.valueOf(modelo.getCantidad()));
        holder.precio.setText(Util.format(modelo.getPrecio()));

        if (modelo.getCantidad() > 1)
            ViewCompat.setBackgroundTintList(finalHolder.botonMenos, ColorStateList.valueOf(Color.parseColor("#FFEB3B")));
        else {
            ViewCompat.setBackgroundTintList(finalHolder.botonMenos, ColorStateList.valueOf(Color.parseColor("#d1d1d1")));
            finalHolder.botonMenos.setTextColor(Color.parseColor("#A5A5A5"));
        }
        holder.botonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modelo.getCantidad()>1){
                    listaModelos.get(listaModelos.indexOf(modelo)).setCantidad(modelo.getCantidad()-1);

                    adaptador.notifyDataSetChanged();

                    //Actualziar precio total
                    total = 0;
                    for (Modelo modelo: listaModelos)
                        total += (modelo.getPrecio()*modelo.getCantidad());

                    tvTotal.setText(Util.format(total));
                }else
                    Toast.makeText(context, R.string.eliminar_producto ,Toast.LENGTH_SHORT).show();

                if(modelo.getCantidad()==1) {
                    ViewCompat.setBackgroundTintList(finalHolder.botonMenos, ColorStateList.valueOf(Color.parseColor("#d1d1d1")));
                    finalHolder.botonMenos.setTextColor(Color.parseColor("#A5A5A5"));
                }

            }
        });
        holder.botonMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaModelos.get(listaModelos.indexOf(modelo)).setCantidad(modelo.getCantidad()+1);
                adaptador.notifyDataSetChanged();

                //Actualziar precio total
                total = 0;
                for (Modelo modelo: listaModelos)
                    total += (modelo.getPrecio()*modelo.getCantidad());

                tvTotal.setText(Util.format(total));

                ViewCompat.setBackgroundTintList(finalHolder.botonMenos, ColorStateList.valueOf(Color.parseColor("#FFEB3B")));
                finalHolder.botonMenos.setTextColor(Color.parseColor("#000000"));
            }
        });

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
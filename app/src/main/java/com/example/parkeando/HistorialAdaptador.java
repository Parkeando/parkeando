package com.example.parkeando;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistorialAdaptador  extends RecyclerView.Adapter<HistorialAdaptador.ViewHolder> {

    List<HistorialModelo> historialModelo;


    public HistorialAdaptador(List<HistorialModelo> historialModelo) {
        this.historialModelo = historialModelo;

    }


    @NonNull
    @Override
    public HistorialAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.datoshistorial, parent, false);
        ViewHolder viewHolder = new ViewHolder (v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistorialAdaptador.ViewHolder holder, int position) {
        String lugar = historialModelo.get(position).getLugar ();
        holder.lugar.setText(lugar);
        String fecha = historialModelo.get(position).getFecha ();
        holder.fecha.setText(fecha);
    }

    @Override
    public int getItemCount() {
        return historialModelo.size();
    }


    public static class ViewHolder  extends RecyclerView.ViewHolder{
        private TextView lugar;
        private TextView fecha;
        public ViewHolder(View v) {
            super(v);
            lugar = (TextView) v.findViewById(R.id.textViewLugar);
            fecha = (TextView) v.findViewById(R.id.textViewFecha);
        }


    }
}

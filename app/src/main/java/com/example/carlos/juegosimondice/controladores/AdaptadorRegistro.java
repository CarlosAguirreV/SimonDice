package com.example.carlos.juegosimondice.controladores;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carlos.juegosimondice.R;
import com.example.carlos.juegosimondice.modelos.POJOindividual;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorRegistro extends RecyclerView.Adapter<AdaptadorRegistro.VistaRegistro> {

    private ArrayList<POJOindividual> listaEquipos;

    public AdaptadorRegistro(ArrayList<POJOindividual> listaEquipos) {
        this.listaEquipos = listaEquipos;
    }

    @NonNull
    @Override
    public AdaptadorRegistro.VistaRegistro onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vistaFila = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.registro, viewGroup, false);
        return new VistaRegistro(vistaFila);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorRegistro.VistaRegistro vistaRegistro, int i) {
    }

    @Override
    public void onBindViewHolder(@NonNull VistaRegistro vistaRegistro, int position, @NonNull List<Object> payloads) {
        final POJOindividual registroIndividual = listaEquipos.get(position);

        // Rellenar datos.
        vistaRegistro.lblRecord.setText(Integer.toString(registroIndividual.getRecord()));
        vistaRegistro.lblFecha.setText(registroIndividual.getFecha());
    }

    @Override
    public int getItemCount() {
        return this.listaEquipos.size();
    }

    public class VistaRegistro extends RecyclerView.ViewHolder {
        public TextView lblId, lblRecord, lblFecha;

        public VistaRegistro(@NonNull View itemView) {
            super(itemView);
            this.lblRecord = itemView.findViewById(R.id.lblRegistroRecord);
            this.lblFecha = itemView.findViewById(R.id.lblRegistroFecha);
        }
    }

}

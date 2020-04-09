package com.example.carlos.juegosimondice.vistas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.carlos.juegosimondice.R;
import com.example.carlos.juegosimondice.controladores.AdaptadorRegistro;
import com.example.carlos.juegosimondice.controladores.BDSQLiteRegistros;
import com.example.carlos.juegosimondice.modelos.POJOindividual;

import java.util.ArrayList;

public class VistaRegistros extends AppCompatActivity {

    private BDSQLiteRegistros controlBD;
    private Button btnVaciar, btnEliminar, btnVolver;
    private View pnlNoHayRegistros;
    private TextView titulo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_registros);

        // Inflar (incluir) la barra de accion personalizada.
        super.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        this.getSupportActionBar().setCustomView(R.layout.barra_superior);

        // Obtener todos los elementos.
        this.btnVaciar = findViewById(R.id.btnVaciar);
        this.btnEliminar = findViewById(R.id.btnEliminar);
        this.btnVolver = findViewById(R.id.btnVolver);
        this.pnlNoHayRegistros = findViewById(R.id.pnlNoHayRegistros);
        this.titulo = findViewById(R.id.tituloBarra);
        this.titulo.setText("Ver registros");

        // Cargar los records de la BD.
        this.controlBD = new BDSQLiteRegistros(this);

        // Mostrar todos los registros.
        this.mostrarRegistros(this.controlBD.getRegistrosIndividual());

        // Controlar eventos.
        this.eventos();
    }

    private void eventos() {
        this.btnVaciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarMensajeVaciarRegistros();
            }
        });

        this.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarMensajeEliminarRegistros();
            }
        });

        this.btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Muestra los equipos en el RecyclerView y controla sus eventos.
    private void mostrarRegistros(final ArrayList<POJOindividual> registros) {
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        AdaptadorRegistro adaptadorRegistro = new AdaptadorRegistro(registros);

        RecyclerView.LayoutManager controlLayout = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(controlLayout);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adaptadorRegistro);

        // Mostrar u ocultar los botones si hay o no registros. Si no los hay se muestra un panel especificando que no hay registros.
        this.btnEliminar.setVisibility(registros.size() > 0 ? View.VISIBLE : View.GONE);
        this.btnVaciar.setVisibility(registros.size() > 1 ? View.VISIBLE : View.GONE);
        this.pnlNoHayRegistros.setVisibility(registros.size() == 0 ? View.VISIBLE : View.GONE);
    }

    private void mostrarMensajeVaciarRegistros() {
        // Crear el AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Definir el contenido del cuerpo.
        builder.setTitle(R.string.seguro).setMessage(R.string.advertencia1);
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                controlBD.vaciarRegistros(false);
                mostrarRegistros(controlBD.getRegistrosIndividual());
            }
        });
        builder.setNegativeButton(R.string.no, null);

        builder.setCancelable(false);

        // Obtener y mostrar el cuadro de dialogo de alerta.
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mostrarMensajeEliminarRegistros() {
        // Crear el AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Definir el contenido del cuerpo.
        builder.setTitle(R.string.seguro).setMessage(R.string.advertencia2);
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                controlBD.vaciarRegistros(true);
                mostrarRegistros(controlBD.getRegistrosIndividual());
            }
        });
        builder.setNegativeButton(R.string.no, null);

        builder.setCancelable(false);

        // Obtener y mostrar el cuadro de dialogo de alerta.
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

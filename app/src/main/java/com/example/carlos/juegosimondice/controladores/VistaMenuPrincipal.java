package com.example.carlos.juegosimondice.controladores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carlos.juegosimondice.R;
import com.example.carlos.juegosimondice.modelos.BDSQLiteRegistros;

import java.util.Locale;
import java.util.Objects;

public class VistaMenuPrincipal extends AppCompatActivity {

    private Button btnIndividual, btnRegistros, btnAcerca, btnSalir;
    private ImageView imagenPortada;
    private TextView lblRecordIndividual;
    private BDSQLiteRegistros controlBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // En cuanto cargue esperar un poco y quitar el Splash Screen
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Log.e("ERROR", "No se ha podido hacer sleep en el metodo onCreate de VistaMenuPrincipal");
        }
        setTheme(R.style.AppTheme);

        // Del onCreate
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.vista_menu_principal);

        // Ocultar la barra superior
        Objects.requireNonNull(super.getSupportActionBar()).hide();


        // Obtener todos los elementos.
        this.btnIndividual = findViewById(R.id.btnMnuIndividual);
        this.btnRegistros = findViewById(R.id.btnMnuRegistros);
        this.btnAcerca = findViewById(R.id.btnMnuAcerca);
        this.btnSalir = findViewById(R.id.btnMnuSalir);
        this.lblRecordIndividual = findViewById(R.id.lblMnuRecordIndividual);
        this.imagenPortada = findViewById(R.id.imgPortada);

        // Obtener el idioma para poner la imagen de titulo con el idioma correspondiente
        String idioma = Locale.getDefault().getLanguage();

        if (idioma.equals("es")) {
            this.imagenPortada.setImageResource(R.drawable.ic_tituloaplicacion);
        } else {
            this.imagenPortada.setImageResource(R.drawable.ic_tituloaplicacion_eng);
        }

        // Controlar eventos.
        this.eventos();

        // Cargar los records de la BD.
        this.controlBD = new BDSQLiteRegistros(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Desbloquear botones.
        btnIndividual.setEnabled(true);
        btnRegistros.setEnabled(true);

        // Mostrar el ultimo record.
        String record = Integer.toString(this.controlBD.getUltimoRegistroIndividual().getRecord());
        this.lblRecordIndividual.setText(record);
    }

    // Controla los eventos de todos los botones del menu.
    private void eventos() {
        this.btnIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnIndividual.setEnabled(false);
                animarBoton(btnIndividual);

                Intent motivo = new Intent(VistaMenuPrincipal.this, VistaIndividual.class);
                startActivity(motivo);
            }
        });

        this.btnRegistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRegistros.setEnabled(false);
                animarBoton(btnRegistros);

                Intent motivo = new Intent(VistaMenuPrincipal.this, VistaRegistros.class);
                startActivity(motivo);
            }
        });

        this.btnAcerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animarBoton(btnAcerca);

                Intent motivo = new Intent(VistaMenuPrincipal.this, VistaAcerca.class);
                startActivity(motivo);
            }
        });

        this.btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animarBoton(btnSalir);
                VistaMenuPrincipal.this.finish();
            }
        });
    }

    private void animarBoton(final Button boton) {
        boton.animate().scaleY(0.90f).scaleX(0.90f).setDuration(70).withEndAction(new Runnable() {
            @Override
            public void run() {
                boton.animate().scaleY(1f).scaleX(1f).setDuration(70);
            }
        });
    }

}





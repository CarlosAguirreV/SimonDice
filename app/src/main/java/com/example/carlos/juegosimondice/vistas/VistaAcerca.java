package com.example.carlos.juegosimondice.vistas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carlos.juegosimondice.R;

public class VistaAcerca extends AppCompatActivity {

    private Button btnVolver, btnCodigo;
    private ImageView imgInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_acerca);

        // Inflar (incluir) la barra de accion personalizada.
        super.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        this.getSupportActionBar().setCustomView(R.layout.barra_superior);

        // Obtener todos los elementos.
        this.btnVolver = findViewById(R.id.btnVolver);
        this.btnCodigo = findViewById(R.id.btnCodigo);
        this.imgInfo = findViewById(R.id.imgInfo);
        TextView titulo = findViewById(R.id.tituloBarra);
        titulo.setText(R.string.acerca);

        // Controlar eventos.
        this.eventos();
    }

    private void eventos() {
        this.btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VistaAcerca.this.finish();
            }
        });

        this.btnCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarWeb();
            }
        });

        this.imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imgInfo.animate().rotationX(-180f).setDuration(370).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        imgInfo.animate().rotationX(1f).setDuration(370);
                    }
                });

            }
        });
    }

    private void mostrarWeb(){
        String paginaWeb = "https://github.com/CarlosAguirreV/SimonDice";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(paginaWeb));
        startActivity(intent);
    }
}

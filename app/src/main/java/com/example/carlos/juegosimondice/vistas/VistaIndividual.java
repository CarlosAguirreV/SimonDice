package com.example.carlos.juegosimondice.vistas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.carlos.juegosimondice.R;
import com.example.carlos.juegosimondice.controladores.JuegoIndividual;

public class VistaIndividual extends AppCompatActivity implements InterfazVista {

    private Button btnSalir, btnVerde, btnRojo, btnAmarillo, btnAzul;
    private Button[] coleccionBotones;
    private TextView lblRecord, lblPuntos, lblToquesRestantes;
    private GridLayout pnlCentral;
    private JuegoIndividual CONTROLADOR;
    private SoundPool soundPool;

    private int[] coleccionAudios = new int[5];
    private boolean aplicacionActiva;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.vista_individual);
        this.aplicacionActiva = true;


        // Para los sonidos
        AudioAttributes atributosAudio = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        this.soundPool = new SoundPool.Builder().setMaxStreams(6).setAudioAttributes(atributosAudio).build();

        // Almacenar los sonidos en un Array
        coleccionAudios[0] = soundPool.load(this, R.raw.nota1, 1);
        coleccionAudios[1] = soundPool.load(this, R.raw.nota2, 1);
        coleccionAudios[2] = soundPool.load(this, R.raw.nota3, 1);
        coleccionAudios[3] = soundPool.load(this, R.raw.nota4, 1);
        coleccionAudios[4] = soundPool.load(this, R.raw.fin, 1);


        // Ocultar la barra superior.
        super.getSupportActionBar().hide();

        // Obtener todos los elementos.
        this.lblRecord = findViewById(R.id.lblRecord);
        this.lblPuntos = findViewById(R.id.lblPuntos);
        this.lblToquesRestantes = findViewById(R.id.lblToquesRestantes);

        this.btnSalir = findViewById(R.id.btnIndividualSalir);
        this.btnVerde = findViewById(R.id.btnVerde);
        this.btnRojo = findViewById(R.id.btnRojo);
        this.btnAmarillo = findViewById(R.id.btnAmarillo);
        this.btnAzul = findViewById(R.id.btnAzul);

        this.pnlCentral = findViewById(R.id.pnlCentral);

        this.coleccionBotones = new Button[4];
        this.coleccionBotones[0] = this.btnVerde;
        this.coleccionBotones[1] = this.btnRojo;
        this.coleccionBotones[2] = this.btnAmarillo;
        this.coleccionBotones[3] = this.btnAzul;


        // Crear el controlador de JuegoIndividual.
        this.CONTROLADOR = new JuegoIndividual(this);

        // Controlar eventos.
        this.eventos();

        // Bloquear los botones la primera vez.
        this.bloquearBotones(true, true);

        // Empezar
        this.CONTROLADOR.reiniciar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.aplicacionActiva = false;
        this.soundPool.release();
    }

    // Controla los eventos de todos los botones del juego.
    private void eventos() {
        this.btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearDialogoSalir();
            }
        });

        this.btnVerde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animarBoton(btnVerde);
                CONTROLADOR.jugar((byte) 0);
                tocarNota((byte) 0);
            }
        });

        this.btnRojo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animarBoton(btnRojo);
                CONTROLADOR.jugar((byte) 1);
                tocarNota((byte) 1);
            }
        });

        this.btnAmarillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animarBoton(btnAmarillo);
                CONTROLADOR.jugar((byte) 2);
                tocarNota((byte) 2);
            }
        });

        this.btnAzul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animarBoton(btnAzul);
                CONTROLADOR.jugar((byte) 3);
                tocarNota((byte) 3);
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

    private void tocarNota(byte nota) {
        soundPool.play(coleccionAudios[nota], 1, 1, 0, 0, 1f);
    }

    // Crea y muestra un cuadro de dialogo que pide confirmación para pulsadoSalir.
    private void crearDialogoSalir() {
        // Crear el AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Definir el contenido del cuerpo.
        builder.setTitle(R.string.volver).setMessage(R.string.volverMensaje);
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                volverAlMenu();
            }
        });
        builder.setNegativeButton(R.string.no, null);

        // Obtener y mostrar el cuadro de dialogo de alerta.
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void mostrarMensajeConfirmacion(String mensaje) {
        this.bloquearBotones(true, true);

        // Sonido fin.
        this.tocarNota((byte) 4);

        // Crear el AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Definir el contenido del cuerpo.
        builder.setTitle(R.string.finJuego).setMessage(mensaje);
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CONTROLADOR.reiniciar();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                volverAlMenu();
            }
        });

        builder.setCancelable(false);

        // Obtener y mostrar el cuadro de dialogo de alerta.
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void mostrarRecord(int record) {
        String cadena = this.getString(R.string.record) + " " + record;
        this.lblRecord.setText(cadena);
    }

    @Override
    public void mostrarPuntos(int puntos) {
        String cadena = this.getString(R.string.puntos) + " " + puntos;
        this.lblPuntos.setText(cadena);
    }

    @Override
    public void mostrarToquesRestantes(int numToques) {
        String cadena = this.getString(R.string.toquesRestantes) + " " + numToques;
        this.lblToquesRestantes.setText(cadena);
    }

    @Override
    public synchronized void bloquearBotones(boolean bloqueado, boolean pintarFondo) {
        this.btnVerde.setEnabled(!bloqueado);
        this.btnRojo.setEnabled(!bloqueado);
        this.btnAmarillo.setEnabled(!bloqueado);
        this.btnAzul.setEnabled(!bloqueado);
        this.btnSalir.setEnabled(!bloqueado);

        if (pintarFondo) {
            this.pnlCentral.setBackgroundResource(bloqueado ? R.color.colorAzulOscuro : R.color.colorWhite);
        }
    }

    @Override
    public synchronized void mostrarValorSerie(byte valorActual) {
        if (valorActual < this.coleccionBotones.length) {
            for (int i = 0; i < coleccionBotones.length; i++) {
                if (i == valorActual) {
                    this.coleccionBotones[i].setPressed(true);
                    this.tocarNota((byte) i);
                } else {
                    this.coleccionBotones[i].setPressed(false);
                }
            }
        }
    }

    @Override
    public void mostrarMensajeCorrecto(String mensaje) {
        this.bloquearBotones(true, true);

        // Crear el AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Definir el contenido del cuerpo.
        builder.setTitle(R.string.hasAcertado).setMessage(mensaje);
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CONTROLADOR.mostrarSerie();
            }
        });

        // El cuadro de dialogo no se puede cancelar.
        builder.setCancelable(false);

        // Obtener y mostrar el cuadro de dialogo de alerta.
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Crea la actividad del menu principal y quita esta.
    private void volverAlMenu() {
        this.finish();
    }

    public void mostrarSerie() {
        final int TIEMPO_ESPERA = CONTROLADOR.getSerie().size() > 7 ? 350 : 500;
        CONTROLADOR.setMostrandoValores(true);

        new Thread() {

            public void run() {
                // Tiempo de espera inicial.
                if (CONTROLADOR.getSerie().size() == 1) {
                    SystemClock.sleep(500);
                }

                for (final byte valor : CONTROLADOR.getSerie()) {
                    try {
                        Thread.sleep(100);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (aplicacionActiva) {
                                    mostrarValorSerie(valor);
                                }
                            }
                        });

                        Thread.sleep(TIEMPO_ESPERA);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mostrarValorSerie((byte) -1);
                            }
                        });

                        Thread.sleep(TIEMPO_ESPERA);


                    } catch (InterruptedException e) {
                        Log.d("ERROR", e.getMessage());
                    }

                    // Parar el hilo si se cierra la actividad.
                    if (!aplicacionActiva) {
                        break;
                    }

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mostrarValorSerie((byte) 120); // Para habilitar todos.

                        // Indicar al controlador que se ha terminado de mostrar la serie.
                        CONTROLADOR.setMostrandoValores(false);
                        bloquearBotones(false, true);
                    }
                });

            }
        }.start();


    }

}

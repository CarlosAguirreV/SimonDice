package com.example.carlos.juegosimondice.controladores;

import com.example.carlos.juegosimondice.R;
import com.example.carlos.juegosimondice.modelos.BDSQLiteRegistros;
import com.example.carlos.juegosimondice.modelos.POJOindividual;

import java.util.Random;

/**
 * Clase preparada para jugar al juego de Simon un solo jugador.
 *
 * @author Carlos Aguirre Vozmediano
 */
public class JuegoIndividual extends Juego {

    private Random rand;
    private VistaIndividual vista;
    private String[] colores;

    /**
     * Constructor de la clase.
     *
     * @param vista Vista asociada a este juego.
     */
    public JuegoIndividual(VistaIndividual vista) {
        colores = new String[4];
        colores[0] = vista.getString(R.string.verde);
        colores[1] = vista.getString(R.string.rojo);
        colores[2] = vista.getString(R.string.amarilo);
        colores[3] = vista.getString(R.string.azul);
        this.vista = vista;
        this.rand = new Random();
        this.cargarDatos();
    }

    /**
     * Permite jugar al juego de Simon.
     *
     * @param valorPulsado El valor que se haya pulsado.
     * @return true si la tecla pulsada es correcta, false si no lo es, en cuyo
     * caso se debera reiniciar todo.
     */
    public boolean jugar(byte valorPulsado) {
        if (valorPulsado == serie.get(super.contadorToques)) {
            // Si aciertas.
            this.puntosActuales += PUNTOS_POR_ACIERTO;

            if (super.serie.size() == ++super.contadorToques) {
                this.aniadirValorAleatorio();

                // Mostrar el un mensaje desde la vista.
                this.vista.mostrarMensajeCorrecto(vista.getString(R.string.prestaAtencion));
            }

            // Refrescar valores de la vista.
            this.refrescarValoresVista();

            return true;

        } else {
            // Si fallas.
            // Mostrar el un mensaje desde la vista.
            this.vista.mostrarMensajeConfirmacion(vista.getString(R.string.tocaba)
                    + " " + colores[serie.get(super.contadorToques)]
                    + " " + vista.getString(R.string.peroPulsaste) + " " + colores[valorPulsado] + "."
                    + (this.isNuevoRecord() ? "\n" + vista.getString(R.string.nuevoRecord) + " " + this.puntosActuales + " " + vista.getString(R.string.puntos) + "." : "")
                    + "\n" + vista.getString(R.string.jugarDeNuevo));

            // Si has batido un nuevo record.
            if (isNuevoRecord()) {
                this.guardarDatos();
                this.recordAnterior = this.puntosActuales;
            }

            return false;
        }
    }

    // Aniade un valor nuevo a la serie.
    // Se asegura de que un valor nunca pueda ser repetido mas de dos veces.
    private void aniadirValorAleatorio() {

        int tamanioSerie = super.serie.size();
        byte valorAleatorio = (byte) rand.nextInt(NUMERO_TECLAS);

        if (tamanioSerie >= 2) {
            // Si la serie tiene mas de dos valores comprueba que no se repitan, si lo hace se genera un valor nuevo, si no se deja el que esta.
            if (valorAleatorio == super.serie.get(tamanioSerie - 1) && valorAleatorio == super.serie.get(tamanioSerie - 2)) {
                byte valorNuevo = this.generarValorNuevoAleatorio(valorAleatorio);
                super.serie.add(valorNuevo);

            } else {
                // Como no se repite no pasa nada.
                super.serie.add(valorAleatorio);
            }

        } else {
            // Como son los primeros dos valores se puede aniadir el valor aleatorio a la serie sin problemas.
            super.serie.add(valorAleatorio);
        }

        // Definir el contador de toques a 0.
        super.contadorToques = 0;

        // Refrescar valores de la vista.
        this.refrescarValoresVista();

        // Muestra por primera vez la serie al jugador.
        if (super.serie.size() < 2) {
            this.vista.mostrarSerie();
        }

    }

    public void mostrarSerie() {
        // Crea un hilo para mostrar la serie al jugador.
        this.vista.mostrarSerie();
    }


    // En el caso en que se repitan los dos ultimos valores se llamara a este metodo el cual retorna un valor distinto. Evita mas de dos repeticiones seguidas.
    private byte generarValorNuevoAleatorio(byte valorAleatorioRepetido) {
        byte[] valoresNuevosPosibles = new byte[NUMERO_TECLAS - 1];

        byte indice = 0;
        for (byte i = 0; i < NUMERO_TECLAS; i++) {
            if (i == valorAleatorioRepetido) {
                continue;
            } else {
                valoresNuevosPosibles[indice++] = i;
            }
        }

        return valoresNuevosPosibles[this.rand.nextInt(NUMERO_TECLAS - 1)];
    }

    @Override
    public void reiniciar() {
        super.puntosActuales = 0;
        super.serie.clear();
        this.aniadirValorAleatorio();

        // Muestra los valores en la vista.
        this.refrescarValoresVista();
    }

    @Override
    protected void guardarDatos() {
        BDSQLiteRegistros controlBD = new BDSQLiteRegistros(this.vista);
        POJOindividual datos = new POJOindividual(this.puntosActuales);
        controlBD.guardarRegistroIndividual(datos);
    }

    @Override
    protected void cargarDatos() {
        BDSQLiteRegistros controlBD = new BDSQLiteRegistros(this.vista);
        POJOindividual datos = controlBD.getUltimoRegistroIndividual();
        this.recordAnterior = datos.getRecord();
    }

    // Refresca los valores del panel superior de la vistaIndividual.
    private void refrescarValoresVista() {
        this.vista.mostrarRecord(this.recordAnterior);
        this.vista.mostrarPuntos(super.getPuntosActuales());
        this.vista.mostrarToquesRestantes(super.getToquesRestantes());
    }

}

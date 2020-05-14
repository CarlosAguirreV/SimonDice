package com.example.carlos.juegosimondice.controladores;

/**
 * Interfaz que recoge los metodos comunes que han de tener las vistas de los juegos.
 * Excluyendo los basicos.
 *
 * @author Carlos Aguirre Vozmediano
 */
public interface InterfazVista {

    void mostrarRecord(int record);

    void mostrarPuntos(int puntos);

    void mostrarToquesRestantes(int numToques);

    void bloquearBotones(boolean bloqueado, boolean pintarFondo);

    void mostrarValorSerie(byte valorActual);

    void mostrarMensajeCorrecto(String mensaje);
}

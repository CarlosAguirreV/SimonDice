package com.example.carlos.juegosimondice.modelos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Esta clase se encarga de la manipulacion de la BD.
 */
public class BDSQLiteRegistros extends SQLiteOpenHelper {

    private SQLiteDatabase baseDatos;
    private Context contexto;

    /**
     * Constructor de la clase.
     *
     * @param context Contexto, la actividad en la que esta.
     */
    public BDSQLiteRegistros(Context context) {
        super(context, "registros", null, 1);
        this.contexto = context;
    }

    /**
     * Esto ocurre al actualizar la BD de SQLite.
     *
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    /**
     * Al crear la BD, si no existe la crea, si existe no hace nada.
     *
     * @param sqLiteDatabase La BD con la que se trabaja.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.baseDatos = sqLiteDatabase;
        this.crearTablas();
    }

    // Metodo que crea las tablas.
    private void crearTablas() {
        String sentenciaCreacion1 = "CREATE TABLE individual(id integer PRIMARY KEY autoincrement, record integer, fecha text)";
        this.baseDatos.execSQL(sentenciaCreacion1);
    }

    // Ejecuta la sentencia y devuelve true o false en funcion de si ha habido algun problema.
    private boolean ejecutarSQL(String sentenciaSQL) {
        boolean correcto = false;
        SQLiteDatabase escritorBD = getWritableDatabase();
        try {
            escritorBD.execSQL(sentenciaSQL);
            correcto = true;
        } catch (Exception ex) {
            correcto = false;
        } finally {
            escritorBD.close();
        }
        return correcto;
    }

    // ----------------------------------- INDIVIDUAL -----------------------------------

    public ArrayList<POJOindividual> getRegistrosIndividual() {
        ArrayList<POJOindividual> coleccionRegistros = new ArrayList();

        String sentenciaSQL = "SELECT * FROM individual;";

        SQLiteDatabase lectorBD = this.getReadableDatabase();
        Cursor cursor = lectorBD.rawQuery(sentenciaSQL, null);

        if (cursor.moveToFirst()) {
            do {
                POJOindividual registro = new POJOindividual();

                registro.setId(cursor.getInt(0));
                registro.setRecord(cursor.getInt(1));
                registro.setFecha(cursor.getString(2));

                coleccionRegistros.add(registro);

            } while (cursor.moveToNext());
        }

        lectorBD.close();
        return coleccionRegistros;
    }

    /**
     * @return
     */
    public POJOindividual getUltimoRegistroIndividual() {
        String sentenciaSQL = "SELECT * FROM individual WHERE id = (SELECT MAX(id) FROM individual);";

        SQLiteDatabase lectorBD = this.getReadableDatabase();

        Cursor cursor = lectorBD.rawQuery(sentenciaSQL, null);

        if (cursor.moveToFirst()) {
            POJOindividual registro = new POJOindividual();
            registro.setId(cursor.getInt(0));
            registro.setRecord(cursor.getInt(1));
            registro.setFecha(cursor.getString(2));

            return registro;
        }

        lectorBD.close();
        return new POJOindividual();
    }

    /**
     * @param objIndividual
     * @return
     */
    public boolean guardarRegistroIndividual(POJOindividual objIndividual) {
        String sentenciaSQL = "INSERT INTO individual(record, fecha) VALUES (" + objIndividual.getRecord() + ",\'" + objIndividual.getFecha() + "\');";
        return this.ejecutarSQL(sentenciaSQL);
    }

    public boolean vaciarRegistros(boolean borrarTodo) {
        String sentenciaBorrado;
        String sentenciaActualizarId = "UPDATE individual SET id = 0 WHERE id = (SELECT MAX(id) FROM individual)";
        String sentenciaReiniciarIndice = "UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='individual'";
        if (borrarTodo) {
            sentenciaBorrado = "DELETE FROM individual";
        } else {
            sentenciaBorrado = "DELETE FROM individual WHERE id < (SELECT MAX(id) FROM individual)";
        }

        this.ejecutarSQL(sentenciaBorrado);
        this.ejecutarSQL(sentenciaActualizarId);
        this.ejecutarSQL(sentenciaReiniciarIndice);

        return true;
    }
}

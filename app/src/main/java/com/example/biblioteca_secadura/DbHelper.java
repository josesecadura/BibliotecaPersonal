package com.example.biblioteca_secadura;

import static com.example.biblioteca_secadura.Contrato.Entradas.COLUMN_NAME_AUTOR;
import static com.example.biblioteca_secadura.Contrato.Entradas.COLUMN_NAME_CATEGORIA;
import static com.example.biblioteca_secadura.Contrato.Entradas.COLUMN_NAME_FECHA_LECTURA_FIN;
import static com.example.biblioteca_secadura.Contrato.Entradas.COLUMN_NAME_FECHA_LECTURA_INI;
import static com.example.biblioteca_secadura.Contrato.Entradas.COLUMN_NAME_FORMATO;
import static com.example.biblioteca_secadura.Contrato.Entradas.COLUMN_NAME_ID;
import static com.example.biblioteca_secadura.Contrato.Entradas.COLUMN_NAME_IDIOMA;
import static com.example.biblioteca_secadura.Contrato.Entradas.COLUMN_NAME_NOTAS;
import static com.example.biblioteca_secadura.Contrato.Entradas.COLUMN_NAME_PRESTADO_A;
import static com.example.biblioteca_secadura.Contrato.Entradas.COLUMN_NAME_TITULO;
import static com.example.biblioteca_secadura.Contrato.Entradas.COLUMN_NAME_VALORACION;
import static com.example.biblioteca_secadura.Contrato.Entradas.TABLE_NAME;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla de Usuarios
    String sqlCreate="CREATE TABLE "+ TABLE_NAME+" ("+COLUMN_NAME_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME_CATEGORIA+" TEXT NOT NULL, "+COLUMN_NAME_TITULO+" TEXT NOT NULL, "+COLUMN_NAME_AUTOR+" " +
            "TEXT NOT NULL, "+COLUMN_NAME_IDIOMA+" TEXT, " +
            ""+COLUMN_NAME_FECHA_LECTURA_INI+" LONG, " +
            ""+COLUMN_NAME_FECHA_LECTURA_FIN+" LONG, "+COLUMN_NAME_PRESTADO_A+" TEXT, " +
            ""+COLUMN_NAME_VALORACION+" FLOAT, "+COLUMN_NAME_FORMATO+" TEXT, "+COLUMN_NAME_NOTAS+" TEXT);";


    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Se elimina la versión anterior de la tabla y se crea la nueva
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        db.execSQL(sqlCreate);
    }
}

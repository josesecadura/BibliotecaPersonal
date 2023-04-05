package com.example.biblioteca_secadura;

import android.provider.BaseColumns;

public class Contrato {
    private Contrato(){
    }
    public static class Entradas implements BaseColumns {

        //Clase contrato en la que se definen las constantes que se utilizaran en la base de datos
        public static final String TABLE_NAME = "libros";
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_CATEGORIA = "categoria";
        public static final String COLUMN_NAME_TITULO = "titulo";
        public static final String COLUMN_NAME_AUTOR = "autor";
        public static final String COLUMN_NAME_IDIOMA = "idioma";
        public static final String COLUMN_NAME_FECHA_LECTURA_INI = "fecha_lectura_ini";
        public static final String COLUMN_NAME_FECHA_LECTURA_FIN = "fecha_lectura_fin";
        public static final String COLUMN_NAME_PRESTADO_A = "prestado_a";
        public static final String COLUMN_NAME_VALORACION = "valoracion";
        public static final String COLUMN_NAME_FORMATO = "formato";
        public static final String COLUMN_NAME_NOTAS = "notas";

    }
}

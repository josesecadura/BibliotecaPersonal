package com.example.biblioteca_secadura;

import java.io.Serializable;

public class Libro implements Serializable {
    private int id;
    private String titulo;
    private String autor;
    private String categoria;
    private String idioma;
    private String formato;
    private long fecha_ini;
    private long fecha_fin;
    private float valoracion;
    private String prestado;
    private String notas;

    public Libro(int id, String titulo, String autor, String categoria, String idioma, String formato,
                 long fecha_ini, long fecha_fin, float valoracion, String prestado, String notas) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.idioma = idioma;
        this.formato = formato;
        this.fecha_ini = fecha_ini;
        this.fecha_fin = fecha_fin;
        this.valoracion = valoracion;
        this.prestado = prestado;
        this.notas = notas;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getCategoria() {
        return categoria;
    }

    //Debere saber la posicion del spinner para poder ponerlo en la posicion correcta
    public int getCategoriaPos() {
        int pos = 0;
        switch (categoria){
            case "Novela":
                pos = 0;
                break;
            case "Poesia":
                pos = 1;
                break;
            case "Teatro":
                pos = 2;
                break;
            case "Ensayo":
                pos = 3;
                break;
            case "Biografia":
                pos = 4;
                break;
            case "Infantil":
                pos = 5;
                break;
            case "Otros":
                pos = 6;
                break;
        }
        return pos;
    }


    public String getIdioma() {
        return idioma;
    }

    //Debere saber la posicion del spinner idioma para poder ponerlo en la posicion correcta
    public int getIdiomaPos() {
        int pos = 0;
        switch (idioma){
            case "Castellano":
                pos = 0;
                break;
            case "Ingles":
                pos = 1;
                break;
            case "Frances":
                pos = 2;
                break;
            case "Aleman":
                pos = 3;
                break;
            case "Italiano":
                pos = 4;
                break;
            case "Otro":
                pos = 5;
                break;
        }
        return pos;
    }


    public String getFormato() {
        return formato;
    }

    //Debere saber la posicion del spinner formato para poder ponerlo en la posicion correcta
    public int getFormatoPos() {
        int pos = 0;
        switch (formato){
            case "Fisico":
                pos = 0;
                break;
            case "Digital":
                pos = 1;
                break;
        }
        return pos;
    }


    public long getFecha_ini() {
        return fecha_ini;
    }

    public long getFecha_fin() {
        return fecha_fin;
    }

    public float getValoracion() {
        return valoracion;
    }

    public String getPrestado() {
        return prestado;
    }

    public String getNotas() {
        return notas;
    }

}

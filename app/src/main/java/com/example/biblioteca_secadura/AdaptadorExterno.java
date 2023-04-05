package com.example.biblioteca_secadura;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdaptadorExterno extends RecyclerView.Adapter<AdaptadorExterno.ContendorVistas> implements View.OnClickListener{

    Cursor cursor;
    private View.OnClickListener listener;
    public AdaptadorExterno(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public ContendorVistas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        //Le asignamos el listener a la vista
        view.setOnClickListener(this);
        return new ContendorVistas(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdaptadorExterno.ContendorVistas holder, int position) {
        cursor.moveToPosition(position);

        @SuppressLint("Range") String titulo = cursor.getString(cursor.getColumnIndex(Contrato.Entradas.COLUMN_NAME_TITULO));
        @SuppressLint("Range") String autor = cursor.getString(cursor.getColumnIndex(Contrato.Entradas.COLUMN_NAME_AUTOR));
        @SuppressLint("Range") long fecha = cursor.getLong(cursor.getColumnIndex(Contrato.Entradas.COLUMN_NAME_FECHA_LECTURA_INI));
        @SuppressLint("Range") String formato = cursor.getString(cursor.getColumnIndex(Contrato.Entradas.COLUMN_NAME_FORMATO));
        @SuppressLint("Range") String prestado = cursor.getString(cursor.getColumnIndex(Contrato.Entradas.COLUMN_NAME_PRESTADO_A));
        @SuppressLint("Range") long finalizado = cursor.getLong(cursor.getColumnIndex(Contrato.Entradas.COLUMN_NAME_FECHA_LECTURA_FIN));
        @SuppressLint("Range") String notas = cursor.getString(cursor.getColumnIndex(Contrato.Entradas.COLUMN_NAME_NOTAS));
        @SuppressLint("Range") String idioma = cursor.getString(cursor.getColumnIndex(Contrato.Entradas.COLUMN_NAME_IDIOMA));
        @SuppressLint("Range") float valoracion = cursor.getFloat(cursor.getColumnIndex(Contrato.Entradas.COLUMN_NAME_VALORACION));
        //La categoria la cojo por si tuvieramos que hacer algo en un futuro
        //@SuppressLint("Range") String categoria = cursor.getString(cursor.getColumnIndex(Contrato.Entradas.COLUMN_NAME_CATEGORIA));

        //Asignamos los valores a los elementos que no necesiten condiciones
        holder.titulo.setText(titulo);
        holder.autor.setText(autor);
        holder.valoracion.setRating(valoracion);

        //Condiciones para poner la imagen checked si la fecha de finalización es distinta de 0 y la imagen unchecked si es 0
        if (finalizado != 0)
            //Si no es 0 debo poner una imagen a la izquierda del texto en el textview
            holder.finalizado.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.checked, 0, 0, 0);
        else
            holder.finalizado.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.unchecked, 0, 0, 0);

        // Crear un objeto Calendar y establecer su fecha a partir del valor guardado en el long
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fecha);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechaString = dateFormat.format(calendar.getTime());

        //Si la fecha de inicio es 0 debo poner un texto en el textview de que no se ha empezado a leer
        if(fecha==0)
            holder.fecha.setText("No empezado");
        else
            //Si no es 0 debo poner la fecha en el textview con el string que he creado
            holder.fecha.setText(fechaString);

        //Condiciones para poner la imagen checked si el prestado no está vacío y la imagen unchecked si está vacío
        if (!prestado.equalsIgnoreCase(""))
            holder.prestado.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.checked, 0, 0, 0);
        else
            holder.prestado.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.unchecked, 0, 0, 0);

        //Condiciones para poner la imagen checked si las notas no están vacías y la imagen unchecked si están vacías
        if (!notas.equalsIgnoreCase(""))
            holder.notas.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.checked, 0, 0, 0);
        else
            holder.notas.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.unchecked, 0, 0, 0);

        //Condiciones para poner la imagen dependiendo del formato seleccionado
        if (formato.equalsIgnoreCase("Libro")) {
            holder.formato.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.book, 0);
        } else if (formato.equalsIgnoreCase("ebook")) {
            holder.formato.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ebook, 0);
        } else if (formato.equalsIgnoreCase("Audio")) {
            holder.formato.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.audio, 0);
        }

        //Condiciones para poner la imagen dependiendo del idioma seleccionado
        if (idioma.equalsIgnoreCase("Español")) {
            holder.img.setImageResource(R.drawable.libroesp);
        } else if (idioma.equalsIgnoreCase("Vasco")) {
            holder.img.setImageResource(R.drawable.librovas);
        } else if (idioma.equalsIgnoreCase("Inglés")) {
            holder.img.setImageResource(R.drawable.libroeng);
        } else if (idioma.equalsIgnoreCase("Francés")) {
            holder.img.setImageResource(R.drawable.librofra);
        } else if (idioma.equalsIgnoreCase("Alemán")) {
            holder.img.setImageResource(R.drawable.libroale);
        } else if (idioma.equalsIgnoreCase("Italiano")) {
            holder.img.setImageResource(R.drawable.libroita);
        } else if (idioma.equalsIgnoreCase("Catalan")) {
            holder.img.setImageResource(R.drawable.librocat);
        } else if (idioma.equalsIgnoreCase("Gallego")) {
            holder.img.setImageResource(R.drawable.librogal);
        } else if (idioma.equalsIgnoreCase("Chino")) {
            holder.img.setImageResource(R.drawable.librochi);
        } else {
            holder.img.setImageResource(R.drawable.libro);
        }

    }

    //Establecemos el listener para el click
    public void setOnCLickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    //Método para el click le pasaremos la vista que ha sido pulsada para mover el cursor
    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    //Devuelve el número de elementos que tiene el cursor para llevar la cuenta
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    //Método para actualizar el cursor
    @SuppressLint("NotifyDataSetChanged")
    public void swapCursor(Cursor cursor2) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = cursor2;
        if (cursor2 != null) {
            notifyDataSetChanged();
        }
    }


    public static class ContendorVistas extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView titulo, autor, fecha, formato, prestado, finalizado, notas;
        RatingBar valoracion;
        ImageView img;

        public ContendorVistas(@NonNull View itemView) {
            super(itemView);
            //Asignamos los elementos del layout item_layout a las variables
            titulo = itemView.findViewById(R.id.text_titulo);
            autor = itemView.findViewById(R.id.text_autor);
            fecha = itemView.findViewById(R.id.text_fecha);
            formato = itemView.findViewById(R.id.text_formato);
            prestado = itemView.findViewById(R.id.text_prestado);
            finalizado = itemView.findViewById(R.id.text_finalizado);
            notas = itemView.findViewById(R.id.text_notas);
            img = itemView.findViewById(R.id.imageView);
            valoracion = itemView.findViewById(R.id.calificacion);
            //Establecemos el listener para el menu contextual
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //Menu contextual podriamos crear mas elementos
            final int BORRAR = 122;
            menu.setHeaderTitle("Selecciona una opción");
            menu.add(getAdapterPosition(), BORRAR, 0, "Borrar");

        }

    }
}

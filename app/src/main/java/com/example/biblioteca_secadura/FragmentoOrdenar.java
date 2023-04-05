package com.example.biblioteca_secadura;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FragmentoOrdenar extends DialogFragment {
    Orden orden;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        orden = (Orden) getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final String[] items = {"Id","Titulo", "Autor", "Categoria","Pais","Formato","Valoracion","Fecha de lectura"};
        AlertDialog.Builder ventana = new AlertDialog.Builder(getActivity());
        ventana.setIcon(R.drawable.libro);
        ventana.setTitle("Elige el orden: ");

        ventana.setItems(items, (dialogInterface, i) -> {
            Toast.makeText(getActivity(), i + " = " + items[i], Toast.LENGTH_SHORT).show();
            orden.ordenSeleccionado(i);
        });

        return ventana.create();
    }

    public interface Orden {
         void ordenSeleccionado(int posicion);

    }

}
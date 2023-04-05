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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity implements FragmentoOrdenar.Orden, View.OnClickListener {

    private static final String NOMBRE = "bdlibros";
    DbHelper usuariosHelper;
    SQLiteDatabase bdEscritura, bdLectura;
    RecyclerView recyclerView;
    AdaptadorExterno adaptadorLista;
    Cursor cursor;
    Boolean seEditaUnLibro = false;

    TextView cantidad, ordenado;
    ActivityResultLauncher<Intent> lanzadorAlta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_libros);
        cantidad = findViewById(R.id.tv_cabecera);
        ordenado = findViewById(R.id.tv_orden);
        usuariosHelper = new DbHelper(this, "bdlibros", null, 7);
        cursor = getAllItems();
        cantidad.setText("Nº de registros: " + cursor.getCount() + "         - ");
        ordenado.setText("Ordenado por: ID");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptadorLista = new AdaptadorExterno(cursor);
        adaptadorLista.setOnCLickListener(this);
        recyclerView.setAdapter(adaptadorLista);

        lanzadorAlta = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    bdEscritura = usuariosHelper.getWritableDatabase();
                    Intent intent = result.getData();
                    Libro resultado = (Libro) intent.getSerializableExtra("libro");

                    //Lo añadimos a la base de datos
                    ContentValues nuevoRegistro = new ContentValues();
                    nuevoRegistro.put(Contrato.Entradas.COLUMN_NAME_TITULO, resultado.getTitulo());
                    nuevoRegistro.put(Contrato.Entradas.COLUMN_NAME_AUTOR, resultado.getAutor());
                    nuevoRegistro.put(Contrato.Entradas.COLUMN_NAME_CATEGORIA, resultado.getCategoria());
                    nuevoRegistro.put(Contrato.Entradas.COLUMN_NAME_IDIOMA, resultado.getIdioma());
                    nuevoRegistro.put(Contrato.Entradas.COLUMN_NAME_FECHA_LECTURA_INI, resultado.getFecha_ini());
                    nuevoRegistro.put(Contrato.Entradas.COLUMN_NAME_FECHA_LECTURA_FIN, resultado.getFecha_fin());
                    nuevoRegistro.put(Contrato.Entradas.COLUMN_NAME_FORMATO, resultado.getFormato());
                    nuevoRegistro.put(Contrato.Entradas.COLUMN_NAME_VALORACION, resultado.getValoracion());
                    nuevoRegistro.put(Contrato.Entradas.COLUMN_NAME_NOTAS, resultado.getNotas());
                    nuevoRegistro.put(Contrato.Entradas.COLUMN_NAME_PRESTADO_A, resultado.getPrestado());
                    if (!seEditaUnLibro) {
                        bdEscritura.insert(TABLE_NAME, null, nuevoRegistro);
                    } else {
                        //Debere actualizar el registro que se ha editado con los nuevos datos
                        bdEscritura.update(TABLE_NAME, nuevoRegistro, COLUMN_NAME_ID + " = " + resultado.getId(), null);
                        seEditaUnLibro = false;
                        cantidad.setText("Nº de registros: " + cursor.getCount() + " - ");
                        ordenado.setText("Orden: ID");
                    }
                    //Actualizamos el cursor
                    cursor = getAllItems();
                    adaptadorLista.swapCursor(cursor);
                    //Cerramos la base de datos
                    bdEscritura.close();
                } else if (result.getResultCode() == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "No se ha podido añadir el elemento", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Cursor getAllItems() {
        bdLectura = usuariosHelper.getReadableDatabase();
        Cursor c = bdLectura.query(TABLE_NAME, null, null, null, null, null, null, null);
        Toast.makeText(this, c.getCount() + "", Toast.LENGTH_SHORT).show();
        bdLectura.close();
        return c;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.alta:
                lanzadorAlta.launch(new Intent(this, SecondActivity.class));
                return true;
            case R.id.info:
                mostrarInformacionApp();
                return true;
            case R.id.importar:
                importarBaseDatos();
                return true;
            case R.id.exportar:
                exportarBaseDatos();
                return true;
            case R.id.ordenar:
                mostrarFragmentoOrdenar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public boolean onContextItemSelected(@NonNull MenuItem item) {
        final int BORRAR = 122;
        switch (item.getItemId()) {

            case BORRAR:
                // Borramos el elemento seleccionado
                bdLectura = usuariosHelper.getReadableDatabase();
                cursor.moveToPosition(item.getGroupId());
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID));
                Toast.makeText(this, "ID: " + id, Toast.LENGTH_SHORT).show();
                bdLectura.delete(TABLE_NAME, COLUMN_NAME_ID + " = " + id, null);
                cursor = getAllItems(); // Actualizamos el cursor
                adaptadorLista.swapCursor(cursor);
                bdLectura.close();
                cantidad.setText("Nº de registros: " + cursor.getCount() + " - ");
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public void ordenSeleccionado(int posicion) {
        String orden = "";
        switch (posicion) {
            case 0:
                //Ordenamos por id
                orden = COLUMN_NAME_ID + " ASC";
                ordenado.setText("Orden: ID");
                break;
            case 1:
                //Ordenamos por titulo
                orden = Contrato.Entradas.COLUMN_NAME_TITULO + " ASC";
                ordenado.setText("Orden: TITULO");
                break;
            case 2:
                //Ordenamos por autor
                orden = Contrato.Entradas.COLUMN_NAME_AUTOR + " ASC";
                ordenado.setText("Orden: AUTOR");
                break;
            case 3:
                //Ordenamos por categoria
                orden = Contrato.Entradas.COLUMN_NAME_CATEGORIA + " ASC";
                ordenado.setText("Orden: CATEGORIA");
                break;
            case 4:
                //Ordenamos por idioma
                orden = Contrato.Entradas.COLUMN_NAME_IDIOMA + " ASC";
                ordenado.setText("Orden: IDIOMA");
                break;
            case 5:
                //Ordenamos por formato
                orden = Contrato.Entradas.COLUMN_NAME_FORMATO + " ASC";
                ordenado.setText("Orden: FORMATO");
                break;
            case 6:
                //Ordenamos por valoracion
                orden = Contrato.Entradas.COLUMN_NAME_VALORACION + " DESC";
                ordenado.setText("Orden: VALORACION");
                break;
            case 7:
                //Ordenamos por fecha de lectura
                orden = Contrato.Entradas.COLUMN_NAME_FECHA_LECTURA_INI + " ASC";
                ordenado.setText("Orden: FECHA INICIO DE LECTURA");
                break;
        }
        bdLectura = usuariosHelper.getReadableDatabase();
        cursor = bdLectura.query(TABLE_NAME, null, null, null, null, null, orden);
        adaptadorLista.swapCursor(cursor);
    }


    @SuppressLint("Range")
    @Override
    public void onClick(View v) {
        //Lanzaremos la actividad pasandole los datos del elemento seleccionado
        Intent intent = new Intent(this, SecondActivity.class);
        //Obtenemos la posicion del elemento seleccionado mediante la vista que nos mandan
        cursor.moveToPosition(recyclerView.getChildAdapterPosition(v));
        //Se lo pasaremos como un libro
        Toast.makeText(this, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID))+"", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITULO)), Toast.LENGTH_SHORT).show();
        Libro libro = new Libro(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITULO)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME_AUTOR)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CATEGORIA)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME_IDIOMA)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FORMATO)),
                cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_FECHA_LECTURA_INI)),
                cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_FECHA_LECTURA_FIN)),
                cursor.getFloat(cursor.getColumnIndex(COLUMN_NAME_VALORACION)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PRESTADO_A)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NOTAS)));
        intent.putExtra("libroP", libro);

        seEditaUnLibro = true;
        lanzadorAlta.launch(intent);
    }

    public void exportarBaseDatos() {
        try {

            String currentDBPath = "/data/data/"+getPackageName()+"/databases/";
            String backupDBPath = "copia.db";
            File actual = new File(currentDBPath, NOMBRE);
            File copia = new File(currentDBPath, backupDBPath);
            if (actual.exists()) {
                FileChannel fis = new FileInputStream(actual).getChannel();
                FileChannel fos = new FileOutputStream(copia).getChannel();
                fos.transferFrom(fis, 0, fis.size());
                fis.close();
                fos.close();
                Toast.makeText(getApplicationContext(),"Exportado!",Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(),"Error al exportar!",Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"Error al exportar!",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            Log.e("ERROR",e.getMessage());
        }
    }

    public void importarBaseDatos() {

        String currentDBPath = "/data/data/"+getPackageName()+"/databases/";
        String backupDBPath = "copia.db";
        File actual = new File(currentDBPath, NOMBRE);
        File copia = new File(currentDBPath, backupDBPath);

        if (actual.exists()) {
            actual.delete();
        }

        try {
            FileChannel fis = new FileInputStream(copia).getChannel();
            FileChannel fos = new FileOutputStream(actual).getChannel();
            fos.transferFrom(fis, 0, fis.size());
            fis.close();
            fos.close();
            Toast.makeText(getApplicationContext(),"Importado!",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"Error al importar!",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            Log.e("ERROR",e.getMessage());
        }

        cursor =getAllItems();
        adaptadorLista.swapCursor(cursor);
    }

    private void mostrarInformacionApp() {
        String info = "Aplicación creada por: \n" +
                "Jose Secadura Del Olmo \n" +
                "Se trata de una aplicación que permite gestionar una biblioteca personal, \n" +
                "pudiendo añadir, editar, borrar y ordenar los libros que tengamos en ella. \n" +
                "También permite exportar e importar los datos de la biblioteca a un archivo .csv";
        //Creo un dialog para mostrar la informacion
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Información");
        builder.setMessage(info);
        builder.setPositiveButton("Aceptar", null);
        builder.create().show();
    }

    private void mostrarFragmentoOrdenar() {
        //Ordenaremos dependiendo lo que elijamos podra ser por titulo, autor, id o fecha de lectura
        Toast.makeText(this, "Boton seleccion pulsado", Toast.LENGTH_SHORT).show();
        FragmentManager items = getSupportFragmentManager();
        FragmentoOrdenar fragmento = new FragmentoOrdenar();
        fragmento.show(items, "xxx");
    }

}
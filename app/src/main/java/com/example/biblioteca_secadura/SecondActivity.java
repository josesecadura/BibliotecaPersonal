package com.example.biblioteca_secadura;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SecondActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView tv_fechaInicio, tv_fechaFin;
    Spinner spinnerCategoria, spinnerIdioma, spinnerFormato;
    RatingBar ratingValoracion;
    EditText titulo, autor, prestado, notas;
    Button btn_FechaInicio, btn_FechaFin;
    long fechaDeInicio_long, fechaDeFin_long;
    private Boolean esFechaDeInicio = true;
    int id;

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        spinnerCategoria = findViewById(R.id.spinnerTipo);
        spinnerIdioma = findViewById(R.id.spinnerIdioma);
        spinnerFormato = findViewById(R.id.spinnerFormato);
        ratingValoracion = findViewById(R.id.calificacion);
        titulo = findViewById(R.id.et_titulo);
        autor = findViewById(R.id.et_autor);
        prestado = findViewById(R.id.et_prestado);
        notas = findViewById(R.id.et_notas);
        btn_FechaInicio = findViewById(R.id.btn_in);
        btn_FechaFin = findViewById(R.id.btn_fin);
        tv_fechaInicio = findViewById(R.id.tvFechaInicio);
        tv_fechaFin = findViewById(R.id.tvFechaFin);

        //Creo los spinners para que el usuario elija
        crearSpinners();

        //Recogeremos el intent que nos manda el usuario y rellenaremos con los datos
        Intent intent = getIntent();
        Libro resultado = (Libro) intent.getSerializableExtra("libroP");
        //Si el resultado no es null es que es un libro que se quiere modificar
        if (resultado != null) {
            //Rellenamos los campos con los datos del libro
            rellenarCamposLibroEditado(resultado);
        }
    }

    private void rellenarCamposLibroEditado(Libro resultado) {
        id=resultado.getId();
        titulo.setText(resultado.getTitulo());
        autor.setText(resultado.getAutor());

        //Si el prestado esta vacio pondre un hint en el et para que el usuario lo sepa
        if(resultado.getPrestado().equalsIgnoreCase(""))
            prestado.setHint("No ha sido prestado");
        else
            //Si no esta vacio pondre en el et el prestado
            prestado.setText(resultado.getPrestado());

        //Si las notas estan vacias pondre un hint en el et para que el usuario lo sepa
        if(resultado.getNotas().equalsIgnoreCase(""))
            notas.setHint("No hay notas");
        else
            //Si no estan vacias pondre en el et las notas
            notas.setText(resultado.getNotas());

        ratingValoracion.setRating(resultado.getValoracion());

        //Creo un objeto calendar para poder formatear la fecha y poder ponerla en el textview
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        //Le hago el setTimeInMillis porque la guardo en un long asi la puedo obtener
        calendar.setTimeInMillis(resultado.getFecha_ini());
        calendar1.setTimeInMillis(resultado.getFecha_fin());


        //Las guardo en Strings para poder ponerlas en el textview
        String fechaString = dateFormat.format(calendar.getTime());
        String fechaString1 = dateFormat.format(calendar1.getTime());

        //Si la fecha de fin es distinta a 0 pongo la fecha de fin en el textview
        if(resultado.getFecha_fin()!=0)
            tv_fechaFin.setText(fechaString1);
        else
            //Si es 0 pongo un textview vacio porque no interesa
            tv_fechaFin.setText("");

        //Hago lo mismo con la fecha de inicio
        if(resultado.getFecha_ini()!=0)
            tv_fechaInicio.setText(fechaString);
        else
            tv_fechaFin.setText("");

        //Guardo las fechas en long para poder modificarlas
        fechaDeInicio_long =resultado.getFecha_ini();
        fechaDeFin_long =resultado.getFecha_fin();

        //Tendre que poner el spinner en la posicion que corresponda
        spinnerCategoria.setSelection(resultado.getCategoriaPos());
        spinnerIdioma.setSelection(resultado.getIdiomaPos());
        spinnerFormato.setSelection(resultado.getFormatoPos());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendario = Calendar.getInstance();
        //Obtengo los datos del long si la fecha ya esta intruducida
        calendario.set(Calendar.YEAR, year);
        calendario.set(Calendar.MONTH, month);
        calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = dateFormat.format(calendario.getTime());

        if (esFechaDeInicio) {
            fechaDeInicio_long = calendario.getTimeInMillis();
            tv_fechaInicio.setText(currentDateString);
        } else {
            fechaDeFin_long = calendario.getTimeInMillis();
            tv_fechaFin.setText(currentDateString);
        }

    }

    public void click_alta(View v) {
        //Recogeremos los datos y los devolveremos a la actividad principal
        String titulo = this.titulo.getText().toString();
        String autor = this.autor.getText().toString();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String idioma = spinnerIdioma.getSelectedItem().toString();
        String formato = spinnerFormato.getSelectedItem().toString();
        float valoracion = ratingValoracion.getRating();
        String prestado = this.prestado.getText().toString();
        String notas = this.notas.getText().toString();
        long fecha_in = fechaDeInicio_long;
        long fecha_fin = fechaDeFin_long;

        //Se lo mandamos por intent
        Intent intent = new Intent();

        //Aqui debere hacer lo mismo si la fecha de fin es menor que la de inicio
        //Si es menor, pondre la fecha de fin a 0
        if(fecha_fin<fecha_in && fecha_fin!=0) {
            fecha_fin = 0;
            Toast.makeText(this, "La fecha de fin no puede ser menor que la de inicio", Toast.LENGTH_SHORT).show();
        }

        Libro a = new Libro(id,titulo, autor, categoria, idioma, formato, fecha_in, fecha_fin, valoracion, prestado, notas);
        intent.putExtra("libro", a);

        //Solo puedo mandarlo por intent si estan todos los campos rellenados
        if (titulo.length()>0 && autor.length()>0) {
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }
        finish();
    }

    public void modificar_fecha_ini(View v){
        esFechaDeInicio =true;
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(SecondActivity.this, SecondActivity.this, year, month, day);
        datePickerDialog.show();
    }

    public void modificar_fecha_fin(View v){
        esFechaDeInicio =false;
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(SecondActivity.this, SecondActivity.this, year, month, day);
        datePickerDialog.show();
    }

    private void crearSpinners() {
        //Voy a hacer el adaptador para el spinner de idiomas
        String[] idiomas = {"Español", "Inglés", "Francés", "Alemán", "Italiano", "Vasco", "Catalan", "Gallego", "Chino"};
        ArrayAdapter<String> adaptadorIdiomas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, idiomas);
        spinnerIdioma.setAdapter(adaptadorIdiomas);

        //Voy a hacer el adaptador para el spinner de formato
        String[] formatos = {"Libro", "Ebook", "Audio"};
        ArrayAdapter<String> adaptadorFormatos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, formatos);
        spinnerFormato.setAdapter(adaptadorFormatos);

        //Voy a hacer el adaptador para el spinner de categorias
        String[] categorias = {"Literatura", "Novela", "Poesía", "Teatro", "Cuento", "Ensayo", "Autoayuda", "Infantil", "Juvenil", "Otros"};
        ArrayAdapter<String> adaptadorCategorias = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorias);
        spinnerCategoria.setAdapter(adaptadorCategorias);
    }
}

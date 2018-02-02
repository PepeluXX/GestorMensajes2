package com.example.lourdes.gestormensajes;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

/*
* Clase que se encarga de confirmar el borrado de un mensaje.
*
* @author  Jose Luis
* @version 1.0
*/

public class ConfirmarBorradoMensaje extends AppCompatActivity {

    //Botones para aceptar o cancelar la operación
    Button borrado_mensaje_definitivo, cancela;
    //Para conectar con la BBDD
    private final BDDHelper miHelper = new BDDHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_borrado_mensaje);

        //Traer botones desde el layout
        borrado_mensaje_definitivo = (Button)findViewById(R.id.boton_borrado_definitivo);
        cancela = (Button)findViewById(R.id.cancela);

        //Recoger datos del intento que ha llamado a esta actividad
        final Bundle datos = getIntent().getExtras();

        //Poner el botón de borrado a la escucha
        borrado_mensaje_definitivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Para escribir o borrar datos en la BBDD
                SQLiteDatabase db = miHelper.getWritableDatabase();

                //Definir WHERE de la consulta
                String selection = "id LIKE ?";
                //Definir parámetros de la consulta
                String []selectionArgs = {String.valueOf(datos.getInt("id_mensaje"))};

                //Ejecutar consulta
                db.delete(datos.getString("nombre_tabla"),selection,selectionArgs);
                //Cerrar la conexión con la BBDD
                db.close();
              ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                //Crear intento para iniciar una nueva actividad
                Intent intent = new Intent(getApplicationContext(), ParaFragmentos.class);
                //Añadir datos al intento para que los use la actividad que se va a iniciar

                intent.putExtra("fragmento", datos.getString("fragmento"));
                intent.putExtra("categoria",datos.getString("categoria"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //Comenzamos la nueva actividad
                startActivity(intent);
                //Finalizamos la actividad actual
                finish();


             /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            }
        });

        //Poner el botón de cancelar operación a la escucha
        cancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Crear intento para iniciar nueva actividad
                Intent intent = new Intent(getApplicationContext(), MuestraMensaje.class);
                //Poner datos en el intento para que los use la actividad que se va a iniciar
                intent.putExtra("nombre_tabla",datos.getString("nombre_tabla"));
                intent.putExtra("id_mensaje",datos.getInt("id_mensaje"));
                intent.putExtra("titulo",datos.getString("titulo"));
                intent.putExtra("fragmento",datos.getString("fragmento"));
                //Comenzar nueva actividad
                startActivity(intent);
                //Finalizar actividad actual
                finish();
            }
        });

    }

}//end of class


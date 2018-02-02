package com.example.lourdes.gestormensajes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/*
* Clase que se encarga de confirmar el borrado de una categoría o de cancelar el proceso de borrado.
*
* @author  Jose Luis
* @version 1.0
*/

public class ConfirmarBorradoCategoria extends Activity {

    //Para mostrar el texto de confirmación de borrado
    TextView texto_alerta;
    //Botones para proceder o cancelar la operación
    Button aceptar,cancelar;
    //Para recoger el nombre de la categoría pendiente de ser borrada
    String categoria = "";
    //Para conectar con la BBDD
    private final BDDHelper miHelper = new BDDHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_borrado_categoria);

        //Recoger los datos del intento procedentes de la actividad previa
        Bundle datos = getIntent().getExtras();
        //Recoger nombre de la categoría a borrar
        categoria = datos.getString("nombre_categoria");
        //Traer TextView desde el layout
        texto_alerta = (TextView)findViewById(R.id.textView4);
        //Traer botones desde el layout
        aceptar = (Button)findViewById(R.id.button4);
        cancelar = (Button)findViewById(R.id.button5);
        //Configuramos texto de alerta
        texto_alerta.setText("¿Seguro que desea borrar la categoría \""+categoria+"\" ?");

        //Poner botón de confirmado a la escucha
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Para poder manejar datos en la BBDD, tanto escribir como borrar
                SQLiteDatabase db = miHelper.getWritableDatabase();

                //Definir parte WHERE de la consulta
                String selection =  "nombre LIKE ?";
                //Definir argumentos de la consulta
                String []selectionArgs = {categoria};

                //Ejecutar consulta
                db.delete("categorias",selection,selectionArgs);

                //y desvinculamos los mensajes asociados a esa categoría
                String RAW_QUERY = "SELECT name FROM sqlite_master WHERE type='table' AND name NOT IN('android_metadata','tokens','categorias')";

                Cursor cursor = db.rawQuery(RAW_QUERY,null);
                cursor.moveToFirst();
                ContentValues values = new ContentValues();
                values.putNull("categoria");

                for(int i=0;i<cursor.getCount();i++){

                    //Definir parte WHERE de la consulta
                    String selection2 =  "categoria LIKE ?";
                    //Definir argumentos de la consulta
                    String []selectionArgs2 = {categoria};
                    //Ejecutar la consulta
                    int count = db.update(
                            "'"+cursor.getString(0)+"'",
                            values,
                            selection2,
                            selectionArgs2);




                   /* String nombre_tabla = cursor.getString(0);
                    String RAW_QUERY2 = "update '"+ nombre_tabla+"' set categoria = null WHERE categoria ='"+categoria+"'";
                    db.rawQuery(RAW_QUERY2,null);*/
                    cursor.moveToNext();

                }


                //Crear intento para iniciar nueva actividad
                Intent intent = new Intent(getApplicationContext(),BorrarCategorias.class);
                //Cerrar la base de datos
                db.close();
                //Iniciar nueva actividad
                startActivity(intent);
                //Finalizar actividad actual
                finish();
            }
        });

        //Poner el botón de cancelar borrado a la escucha de ser pulsado
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Crear intento para iniciar nueva actividad
                Intent intent = new Intent(getApplicationContext(),BorrarCategorias.class);
                //Iniciar nueva actividad
                startActivity(intent);
                //Finalizar actividad actual
                finish();
            }
        });

    }
}//end of class

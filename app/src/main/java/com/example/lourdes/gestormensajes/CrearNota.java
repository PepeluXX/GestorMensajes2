package com.example.lourdes.gestormensajes;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CrearNota extends AppCompatActivity {

     EditText texto_nota;
     Button crear_nota;
     Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nota);

        texto_nota = (EditText)findViewById(R.id.texto_nota);
        crear_nota = (Button)findViewById(R.id.crear_nota);
        datos = getIntent().getExtras();

        crear_nota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(texto_nota.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Añada alguna nota sobre este mensaje.",Toast.LENGTH_SHORT).show();
                }
                else{
                    String nota = texto_nota.getText().toString();
                    final BDDHelper mDbHelper = new BDDHelper(getApplicationContext());
                    //Para poder escribir datos en la BBDD
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();



                    //Crear un nuevo mapa de valores, donde los nombres de las columnas son las keys
                    ContentValues values = new ContentValues();
                    //values.put("id",1);
                    values.put("nota",nota);


                    //Indicar la fila en la que se van a modificar datos
                    String selection = " id LIKE ?";
                    String[] selectionArgs = {datos.getString("id")};
                    //Ejecutar la consulta
                    int count = db.update(
                            datos.getString("nombre_tabla"),
                            values,
                            selection,
                            selectionArgs);

                    if(count!=0){
                        Toast.makeText(getApplicationContext(),"Se ha añadido la nota al mensaje",Toast.LENGTH_SHORT).show();
                        finish();

                    }

                }
            }
        });
    }
}

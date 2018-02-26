package com.example.lourdes.gestormensajes;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Lourdes on 26/02/2018.
 */

public class CrearCategoriaFragment extends Fragment {


    //Botones para crear o borrar categorías
    Button boton_crear, boton_borrar;
    //Campo de texto para darle nombre a la categoría que se desea crear
    EditText nombre_categoria;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Para conectar con la BBDD
        final BDDHelper miHelper = new BDDHelper(getActivity());
        //Traer elementos desde el layout
        boton_crear = (Button) getActivity().findViewById(R.id.boton_crear_categoria);
        boton_borrar = (Button)getActivity().findViewById(R.id.boton_borrar_categorias);
        nombre_categoria = (EditText)getActivity(). findViewById(R.id.nombre_categoria);

        //Poner a la escucha el botón de creación de categorías
        boton_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Comprobar que se ha insertado algún nombre para la categoría
                if(nombre_categoria.getText().toString().trim().equals("")){
                    Toast.makeText(getActivity(),"Inserte algún nombre para la categoría",Toast.LENGTH_SHORT).show();
                }
                //Si se ha insertado algún nombre, se comienza con la creación de la categoría
                else{
                    //Para leer datos de la BBDD
                    SQLiteDatabase db1 = miHelper.getReadableDatabase();

                    // Definir las columnas de la tabla de las que queremos leer datos
                    String[] projection = {
                            "id"
                    };

                    // Filtrar resultados: WHERE nombre = ?
                    String selection = "nombre = ?";
                    String[] selectionArgs = {nombre_categoria.getText().toString()};

                    // Definir orden de los datos que se van a mostrar
            /* String sortOrder =
                EstructuraBD.NOMBRE_COLUMNA_3 + " DESC";*/

                    //Ejecutar la consulta
                    try {
                        Cursor cursor = db1.query(
                                "categorias",                        // La tabla a consultar
                                projection,                                // Las columnas a devolver
                                selection,                                 // Las columnas en las que ejecutar el WHERE
                                selectionArgs,                             // Los valores para la clausula WHERE
                                null,                             // No agrupar las filas
                                null,                              // No filtrar por grupos de filas
                                null                              // No ordenar
                        );

                        cursor.moveToFirst();

                        //Comprobar que la categoría no existe y si true, se crea la nueva categoría
                        if (cursor.getCount() == 0) {

                            //Para poder escribir en la base de datos
                            SQLiteDatabase db = miHelper.getWritableDatabase();

                            // Crear nuevo mapa de valores donde los nombres de columna son la key
                            ContentValues values = new ContentValues();
                            values.put("nombre", nombre_categoria.getText().toString());


                            //Toast.makeText(getApplicationContext(), "Antes de guardar: ", Toast.LENGTH_LONG).show();

                            // Insertar el nuevo valor en la tabla, se devuelve la id de la nueva fila creada
                            long newRowId = db.insert("categorias", null, values);

                            Toast.makeText(getActivity(), "Se Creó la categoria: " +
                                    nombre_categoria.getText().toString(), Toast.LENGTH_LONG).show();

                            //TODO:  HACER QUE VUELVA A ELEGIRCATEGORIASFRAGMENT
                        } else {
                            Toast.makeText(getActivity(), "La categoría ya existe", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "No existe el registro", Toast.LENGTH_SHORT).show();
                    }
                    //Si la categoría ya existe se limpia el campo de texto
                    nombre_categoria.setText("");
                }

            }
        });

        //Poner el botón de borrado de categoría a la escucha de ser pulsado
        boton_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Crear intento para comenzar una nueva actividad
              //  Intent intent = new Intent(getApplicationContext(),BorrarCategorias.class);
                //Comenzar nueva actividad
                //startActivity(intent);


                Fragment fragment = new BorrarCategoriasFragment();
               /* Bundle datos = new Bundle();
                datos.putString("categoria",categoria);
                fragment.setArguments(datos);*/


                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    //fragmentManager.popBackStack("root_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.screen_area, fragment).addToBackStack("root_fragment");
                    ft.commit();
                }


            }

        });

    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_crear_categorias,null);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        int pxx = Math.round(px);
        return pxx;
    }






}//end of class


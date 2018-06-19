package com.example.lourdes.gestormensajes;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Lourdes on 26/02/2018.
 */

public class GuardarEnCategoriaFragment extends Fragment {





    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Para conectar con la BBDD
         final BDDHelper miHelper = new BDDHelper(getActivity());

        //Texto para indicar que no existen categorías creadas, si fuese el caso
        TextView texto_no_hay_categorias;
        //Para recoger el resultado de la consulta
        Cursor cursor;

        //Layout principal de la actividad
        final LinearLayout lm = (LinearLayout) getActivity().findViewById(R.id.linearLayout);
        lm.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.fondo));

        //Crear los parámetos para definir el layout de los botones
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        //Para leer datos de la BBDD
        SQLiteDatabase db = miHelper.getReadableDatabase();
        //Crear la consulta
        String RAW_QUERY = "SELECT nombre FROM categorias";
        //Ejecutar la consulta
        cursor = db.rawQuery(RAW_QUERY,null);

        cursor.moveToFirst();

        //Si existen categorías creadas, se crea un botón para cada una de ellas
        if(cursor.getCount()!=0) {

            for (int j = 0; j < cursor.getCount(); j++) {

                // Crear un linear layout para cada botón
                LinearLayout ll = new LinearLayout(getActivity());
                //ll.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.fondo));
                ll.setOrientation(LinearLayout.HORIZONTAL);
                //ll.setBackgroundColor(12);

                //Crear el botón y definir parámetros del mismo
                final Button boton = new Button(getActivity());
                boton.setText(cursor.getString(0));
                boton.setId(j);
                //boton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorSecondary));

                //Agregar layout definido para el boton
                boton.setLayoutParams(params);
                //Recoger el nombre de la categoría
                final String valor = cursor.getString(0);
                //Poner el botón a la escucha se ser pulsado
                boton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Para escribir datos en la BBDD
                        SQLiteDatabase db = miHelper.getWritableDatabase();
                        //Para insertar diferentes valores
                        ContentValues values = new ContentValues();
                        values.put("categoria",valor );


                        //Definir que fila se va a actualizar
                        String selection = "id LIKE ?";
                        String[] selectionArgs = {String.valueOf(getArguments().getInt("id")) };

                        //Ejecutar la consulta
                        int count = db.update(
                                getArguments().getString("tabla"),
                                values,
                                selection,
                                selectionArgs);
                        //Si la inserción es correcta, se comunica al usuario
                        if(count !=0){
                            Toast.makeText(getActivity(),"Se guardó el mensaje como perteneciente a la categoría "+valor,Toast.LENGTH_SHORT).show();
                        }
                        //Terminar actividad actual
                        //finish();
                    }
                });
                //Avanzar el cursor para la creación del siguiente botón
                cursor.moveToNext();
                //Añadir botón al layout del botón
                ll.addView(boton);
                //Añadir layout del botón al layout principal
                lm.addView(ll);

            }
            db.close();
        }
        //Si aún no existen categorías creadas
        else{
            texto_no_hay_categorias = (TextView)getActivity().findViewById(R.id.texto_confirma_guardado);
            texto_no_hay_categorias.setText("No hay categorías creadas.");
          /* Toast.makeText(getApplicationContext(),"Aún no se ha creado ninguna categoría",Toast.LENGTH_SHORT).show();
           finish();*/
        }







    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_guardar_en_categoria,null);
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
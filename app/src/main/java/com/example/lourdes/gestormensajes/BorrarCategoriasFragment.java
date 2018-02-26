package com.example.lourdes.gestormensajes;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Lourdes on 26/02/2018.
 */

public class BorrarCategoriasFragment extends Fragment {





    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Para conectar con la BBDD
         final BDDHelper miHelper = new BDDHelper(getActivity());
        //Para mostrar texto que indica aún no hay categorías creadas
         TextView no_categorias;


        //Layout principal de la actividad
        final LinearLayout lm = (LinearLayout) getActivity().findViewById(R.id.linearLayout);

        //Parámetros del layout para definir la apariencia de los botones
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);



        //Para poder leer datos de la BBDD
        SQLiteDatabase db = miHelper.getReadableDatabase();

        //Construir la consulta
        String RAW_QUERY = "SELECT nombre FROM categorias";
        //Ejecutar la consulta
        final Cursor cursor = db.rawQuery(RAW_QUERY,null);

        cursor.moveToFirst();

        //Si aún no existen categorías creadas se muestra un TextView indicándolo
        if(cursor.getCount()==0){
            no_categorias = (TextView)getActivity().findViewById(R.id.texto_no_cat);
            no_categorias.setText(R.string.no_categorias);
        }

        //Crear botones dinámicamente,uno para cada categoría

        for(int j=0;j<cursor.getCount();j++) {
            // Crear un LinearLayout para cada botón
            LinearLayout ll = new LinearLayout(getActivity());
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setBackgroundColor(12);

           /* GradientDrawable border = new GradientDrawable();
            border.setColor(ContextCompat.getColor(this, R.color.colorBordeLayoutListaMensajes)); //white background
            border.setStroke(2, ContextCompat.getColor(this, R.color.colorBordeSeparacion)); //black border with full opacity
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                ll.setBackgroundDrawable(border);
            } else {
                ll.setBackground(border);
            }*/


            //Crear el boton que reperesenta a una categoría
            final Button boton = new Button(getActivity());
            //Darle nombre de la categoría correspondiente
            boton.setText(cursor.getString(0));
            //Darle una id
            boton.setId(j);
            //Establecer parámetros del Layout
            boton.setLayoutParams(params);

            //Log.d("idsboton",""+boton.getId());

            //Poner el botón a la escucha de ser pulsado
            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getApplicationContext(),"comenzar actividad",Toast.LENGTH_SHORT).show();
                    /*
                    //Crear intento para comenzar nueva actividad
                    Intent intent = new Intent(getApplicationContext(),ConfirmarBorradoCategoria.class);
                    //Pasarle datos al intento, en concreto el nombre de la categoría a borrar
                    intent.putExtra("nombre_categoria",boton.getText().toString());
                    //Iniciar nueva actividad
                    startActivity(intent);
                    //Terminar actividad actual
                    finish();
                    */


                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    // Add the buttons

                    builder.setMessage("¿Seguro que quiere borrar la categoria "+boton.getText()+"?")
                            .setTitle("Borrar");

                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            borrarCategoria(boton.getText().toString());


                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    // Set other dialog properties


                    // Create the AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

/*
            Resources resources = getApplicationContext().getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            float dp = 25 / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
            float dp_boton = 200 / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

            boton.setTextSize(dp);
            boton.setGravity(0);
            boton.setHeight((int) dp_boton);

            boton.setLayoutParams(params);*/

            //Añadir el botón a la vista
            ll.addView(boton);
            //Avanzar a la siguiente fila del resultset, para obtener el siguiente nombre de categoría
            cursor.moveToNext();
            //Añadir el layout que contiene el botón al layout principal
            lm.addView(ll);

        }

        //Cerrar conexión con la BBDD
        db.close();


    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_borrar_categorias,null);
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


    public void borrarCategoria(String categoria){

        BDDHelper miHelper = new BDDHelper(getActivity());
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

            cursor.moveToNext();

        }

        Fragment fragment = new BorrarCategoriasFragment();



        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            //fragmentManager.popBackStack("root_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.screen_area, fragment).addToBackStack("root_fragment");
            ft.commit();
        }
    }






}//end of class
package com.example.lourdes.gestormensajes;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Lourdes on 02/02/2018.
 */

public class FragmentMuestraMensaje2 extends Fragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Toolbar toolbar;
        //Para conectar con la BBDD
        final BDDHelper miHelper = new BDDHelper(getActivity());
        //Campos de texto para presentar datos sobre el mensaje
        TextView texto_autor, texto_fecha, texto_titulo, texto_mensaje;
        //Botones con imagen que representan las opciones a ejecutar sobre un mensaje
        ImageButton boton_borrar_mensaje, boton_guardar_mensaje, boton_crear_categoria, boton_crear_nota;

        final int id = getArguments().getInt("id");
        final String titulo = getArguments().getString("titulo");
        final String tabla = getArguments().getString("tabla");


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setImageResource(R.mipmap.ic_leer_nota);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String RAW_QUERY = "SELECT nota FROM " + tabla +
                        " WHERE titulo = '" + titulo + "' AND id = " + id;

                //Para leer datos de la BBDD
                SQLiteDatabase db = miHelper.getReadableDatabase();
                //Ejecutar la consulta
                Cursor cursor = db.rawQuery(RAW_QUERY, null);
                cursor.moveToFirst();
                String nota = cursor.getString(0);
                //Toast.makeText(getApplicationContext(),""+nota,Toast.LENGTH_SHORT).show();


                if (nota == null) {
                    Snackbar.make(view, "No hay notas sobre este mensaje.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else {
                    Snackbar.make(view, cursor.getString(0), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                db.close();
            }
        });


        //Traer elementos desde el layout
        texto_autor = (TextView) view.findViewById(R.id.texto_autor);
        texto_fecha = (TextView) view.findViewById(R.id.texto_fecha);
        texto_titulo = (TextView) view.findViewById(R.id.texto_titulo);
        texto_mensaje = (TextView) view.findViewById(R.id.texto_mensaje);


        boton_borrar_mensaje = (ImageButton) view.findViewById(R.id.boton_borrar_mensaje);
        boton_guardar_mensaje = (ImageButton) view.findViewById(R.id.boton_guardar_mensaje);
        boton_crear_categoria = (ImageButton) view.findViewById(R.id.boton_crear_categoria);
        boton_crear_nota = (ImageButton) view.findViewById(R.id.boton_crear_nota);


        //Construir la consulta
        String RAW_QUERY = "SELECT autor,fecha,titulo,mensaje,id,categoria FROM " + tabla +
                " WHERE titulo = '" + titulo + "' AND id = " + id;


        //Para leer datos de la BBDD
        SQLiteDatabase db2 = miHelper.getReadableDatabase();
        //Ejecutar la consulta
        Cursor cursor = db2.rawQuery(RAW_QUERY, null);
        cursor.moveToFirst();

        //Log.d("IMPORTANTE",cursor.getString(0)+cursor.getString(1)+cursor.getString(2)+cursor.getString(3)+cursor.getInt(4));
        //Configurar los campos de texto para mostrar el mensaje (el for sobra, ya que solo se muestra un mensaje)
        //for (int i = 0; i < cursor.getCount(); i++) {
            texto_autor.setText("Enviado por: " + cursor.getString(0));
            texto_fecha.setText("Recibido el: " + cursor.getString(1));
            texto_titulo.setText("Asunto:\n" + cursor.getString(2));
            texto_mensaje.setText("Mensaje:\n\n" + cursor.getString(3));
            //int id=cursor.getInt(4);
            final String categoria = cursor.getString(5);

            cursor.moveToNext();
       // }


        //Cerrar conexión con la BBDD
        db2.close();


        //Nueva conexión con la BBDD para marcar el mensaje como leído
        SQLiteDatabase db3 = miHelper.getWritableDatabase();

        //Definir valores a insertar en la tabla
        ContentValues values = new ContentValues();
        values.put("leido", 1);


        //Indicar la fila en la que se van a modificar datos
        String selection = " id LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};
        //Ejecutar la consulta
        int count = db3.update(
                tabla,
                values,
                selection,
                selectionArgs);
        //Cerrar conexión con la BBDD
        db3.close();

        //Poner el botón de borrar a la escucha de ser pulsado


        boton_borrar_mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                // Add the buttons

                builder.setMessage("¿Seguro que quiere borrar este mensaje?")
                        .setTitle("Borrar");

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        if(categoria == null) {
                            borraMensaje(tabla, getArguments().getInt("id"), getArguments().getString("desde"));
                        }else{
                            borraMensaje2(tabla, getArguments().getInt("id"), categoria);
                        }
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

                /*
                //Definir intento para iniciar una nueva actividad
                Intent intent = new Intent(getActivity(),ConfirmarBorradoMensaje.class);
                //Insertar datos en el intento para que los use la actividad a iniciar
                intent.putExtra("id_mensaje",id);
                intent.putExtra("nombre_tabla",tabla);
                intent.putExtra("titulo",titulo);
                //intent.putExtra("fragmento",datos.getString("fragmento"));
                //Iniciar nueva actividad
                startActivity(intent);
                //Finalizar actividad actual
                //finish();
                */
            }
        });


        //Poner el botón de guardar mensaje(en categoría) a la escucha de ser pulsado

        boton_guardar_mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                //Crear intento para iniciar nueva actividad
                Intent intent = new Intent(getActivity(), GuardarEnCategoria.class);
                //Insertar datos en el intento
                intent.putExtra("nombre_tabla", tabla);
                intent.putExtra("id", String.valueOf(id));

                //Comenzar nueva actividad
                startActivity(intent);

                //finish();
                */
                Fragment fragment = new GuardarEnCategoriaFragment();
                Bundle datos = new Bundle();
                datos.putInt("id",getArguments().getInt("id"));
                datos.putString("tabla",getArguments().getString("tabla"));
                fragment.setArguments(datos);


                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    //fragmentManager.popBackStack("root_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.screen_area, fragment).addToBackStack("root_fragment");
                    ft.commit();
                }



            }
        });

        //Poner el botón a la escucha de ser pulsado. Si se pulsa se accede a la actividad "CrearCategorias"
        boton_crear_categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent intent2 = new Intent(getActivity(), CrearCategoria.class);
                startActivity(intent2);
                */
                Fragment fragment = new CrearCategoriaFragment();
                //Bundle datos = new Bundle();
                //datos.putString("categoria",categoria);
                // fragment.setArguments(datos);


                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    //fragmentManager.popBackStack("root_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.screen_area, fragment).addToBackStack("root_fragment");
                    ft.commit();
                }
            }
        });
        //Poner el botón a la escucha de ser pulsado. Si se pulsa se accede a la actividad "CrearCategorias"
        boton_crear_nota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent(getActivity(), CrearNota.class);
                //Insertar datos en el intento
                intent2.putExtra("nombre_tabla", tabla);
                intent2.putExtra("id", String.valueOf(id));
                startActivity(intent2);

            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_muestra_mensaje2, null);
    }

    public void borraMensaje(String nombre_tabla, int id, String desde) {


        //Para escribir o borrar datos en la BBDD
        BDDHelper miOtroHelper = new BDDHelper(getActivity());
        SQLiteDatabase db = miOtroHelper.getWritableDatabase();

        //Definir WHERE de la consulta
        String selection = "id LIKE ?";
        //Definir parámetros de la consulta
        String[] selectionArgs = {String.valueOf(id)};

        //Ejecutar consulta
        db.delete(nombre_tabla, selection, selectionArgs);
        //Cerrar la conexión con la BBDD
        db.close();

        Fragment fragment = null;

        if (desde.equals("curso")) {
            fragment = new PorCursosFragment();

        } else if (desde.equals("hijos")) {
            fragment = new PorHijosFragment();
        } else if (desde.equals("sin_leer")) {
            fragment = new SinLeerFragment();
        } else if (desde.equals("leidos")) {
            fragment = new TodosLeidosFragment();
        } else if (desde.equals("categorizados")) {
            fragment = new CategorizadosFragment();

        } else if (desde.equals("gen")) {
            fragment = new GeneralFragment();
        }


        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack("root_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.screen_area, fragment).addToBackStack("root_fragment");
            ft.commit();
        }
    }


    public void borraMensaje2(String nombre_tabla, int id, String categoria) {


        //Para escribir o borrar datos en la BBDD
        BDDHelper miOtroHelper = new BDDHelper(getActivity());
        SQLiteDatabase db = miOtroHelper.getWritableDatabase();

        //Definir WHERE de la consulta
        String selection = "id LIKE ?";
        //Definir parámetros de la consulta
        String[] selectionArgs = {String.valueOf(id)};

        //Ejecutar consulta
        db.delete(nombre_tabla, selection, selectionArgs);
        //Cerrar la conexión con la BBDD
        db.close();

        Fragment fragment = new CategorizadosFragment();
        Bundle datos = new Bundle();
        datos.putString("categoria",categoria);
        fragment.setArguments(datos);


        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack("root_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.screen_area, fragment).addToBackStack("root_fragment");
            ft.commit();
        }
    }



}//end of class
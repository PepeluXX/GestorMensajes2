package com.example.lourdes.gestormensajes;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMuestraMensaje#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMuestraMensaje extends DialogFragment {

    private Toolbar toolbar;
    //Para conectar con la BBDD
    final BDDHelper miHelper = new BDDHelper(getActivity());
    //Campos de texto para presentar datos sobre el mensaje
    TextView texto_autor,texto_fecha,texto_titulo,texto_mensaje;
    //Botones con imagen que representan las opciones a ejecutar sobre un mensaje
    ImageButton boton_borrar_mensaje,boton_guardar_mensaje,boton_crear_categoria,boton_crear_nota;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "titulo";
    private static final String ARG_PARAM3 = "tabla";

    // TODO: Rename and change types of parameters
    private int id;
    private String titulo;
    private String tabla;


    public FragmentMuestraMensaje() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @param titulo Parameter 2.
     * @param tabla
     * @return A new instance of fragment FragmentMuestraMensaje.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMuestraMensaje newInstance(int id, String titulo,String tabla) {
        FragmentMuestraMensaje fragment = new FragmentMuestraMensaje();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, id);
        args.putString(ARG_PARAM2, titulo);
        args.putString(ARG_PARAM3,tabla);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id= getArguments().getInt(ARG_PARAM1);
            titulo = getArguments().getString(ARG_PARAM2);
            tabla = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
                //return inflater.inflate(R.layout.fragment_muestra_mensaje, container, false);

        id= getArguments().getInt(ARG_PARAM1);
        titulo = getArguments().getString(ARG_PARAM2);
        tabla = getArguments().getString(ARG_PARAM3);
        //Para conectar con la BBDD
        final BDDHelper miHelper = new BDDHelper(getDialog().getContext());



        View view = inflater.inflate(R.layout.fragment_muestra_mensaje, container, false);

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setImageResource(R.mipmap.leer_nota);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String RAW_QUERY = "SELECT nota FROM "+tabla+
                        " WHERE titulo = '"+titulo+"' AND id = "+id;

                //Para leer datos de la BBDD
                SQLiteDatabase db = miHelper.getReadableDatabase();
                //Ejecutar la consulta
                Cursor cursor = db.rawQuery(RAW_QUERY,null);
                cursor.moveToFirst();
                String nota = cursor.getString(0);
                //Toast.makeText(getApplicationContext(),""+nota,Toast.LENGTH_SHORT).show();


                if(nota == null){
                    Snackbar.make(view, "No hay notas sobre este mensaje.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
                else{
                    Snackbar.make(view, cursor.getString(0), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });



        //Traer elementos desde el layout
        texto_autor = (TextView)view.findViewById(R.id.texto_autor);
        texto_fecha = (TextView)view.findViewById(R.id.texto_fecha);
        texto_titulo = (TextView)view.findViewById(R.id.texto_titulo);
        texto_mensaje = (TextView)view.findViewById(R.id.texto_mensaje);

        boton_borrar_mensaje = (ImageButton)view.findViewById(R.id.boton_borrar_mensaje);
        boton_guardar_mensaje = (ImageButton)view.findViewById(R.id.boton_guardar_mensaje);
        boton_crear_categoria = (ImageButton)view.findViewById(R.id.boton_crear_categoria);
        boton_crear_nota = (ImageButton)view.findViewById(R.id.boton_crear_nota);



        //Construir la consulta
        String RAW_QUERY = "SELECT autor,fecha,titulo,mensaje,id FROM "+tabla+
                " WHERE titulo = '"+titulo+"' AND id = "+id;


        //Para leer datos de la BBDD
        SQLiteDatabase db = miHelper.getReadableDatabase();
        //Ejecutar la consulta
        Cursor cursor = db.rawQuery(RAW_QUERY,null);
        cursor.moveToFirst();

        //Log.d("IMPORTANTE",cursor.getString(0)+cursor.getString(1)+cursor.getString(2)+cursor.getString(3)+cursor.getInt(4));
        //Configurar los campos de texto para mostrar el mensaje (el for sobra, ya que solo se muestra un mensaje)
        for(int i=0;i<cursor.getCount();i++){
            texto_autor.setText("Enviado por: "+cursor.getString(0));
            texto_fecha.setText("Recibido el: " +cursor.getString(1));
            texto_titulo.setText("Asunto:\n"+cursor.getString(2));
            texto_mensaje.setText("Mensaje:\n"+cursor.getString(3));
            id=cursor.getInt(4);

            cursor.moveToNext();
        }

        //Cerrar conexión con la BBDD
        db.close();


        //Nueva conexión con la BBDD para marcar el mensaje como leído
        SQLiteDatabase db2=miHelper.getWritableDatabase();

        //Definir valores a insertar en la tabla
        ContentValues values = new ContentValues();
        values.put("leido", 1);


        //Indicar la fila en la que se van a modificar datos
        String selection = " id LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};
        //Ejecutar la consulta
        int count = db2.update(
                tabla,
                values,
                selection,
                selectionArgs);
        //Cerrar conexión con la BBDD
        db2.close();

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
                        // User clicked OK button
                        //Para escribir o borrar datos en la BBDD
                        SQLiteDatabase db = miHelper.getWritableDatabase();

                        //Definir WHERE de la consulta
                        String selection = "id LIKE ?";
                        //Definir parámetros de la consulta
                        String []selectionArgs = {String.valueOf(id)};

                        //Ejecutar consulta
                        db.delete(tabla,selection,selectionArgs);
                        //Cerrar la conexión con la BBDD
                        db.close();

                        Fragment fragment = null;
                        fragment= new TodosLeidosFragment();

                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.popBackStack("root_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.screen_area,fragment).addToBackStack("root_fragment");
                        ft.commit();

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










            /*    //Definir intento para iniciar una nueva actividad
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
                //Crear intento para iniciar nueva actividad
                Intent intent = new Intent(getActivity(),GuardarEnCategoria.class);
                //Insertar datos en el intento
                intent.putExtra("nombre_tabla",tabla);
                intent.putExtra("id",String.valueOf(id));

                //Comenzar nueva actividad
                startActivity(intent);

                //finish();

            }
        });

        //Poner el botón a la escucha de ser pulsado. Si se pulsa se accede a la actividad "CrearCategorias"
        boton_crear_categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent(getActivity(),CrearCategoria.class);
                startActivity(intent2);

            }
        });
        //Poner el botón a la escucha de ser pulsado. Si se pulsa se accede a la actividad "CrearCategorias"
        boton_crear_nota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent(getActivity(),CrearNota.class);
                //Insertar datos en el intento
                intent2.putExtra("nombre_tabla",tabla);
                intent2.putExtra("id",String.valueOf(id));
                startActivity(intent2);

            }
        });

        return view;
    }

}

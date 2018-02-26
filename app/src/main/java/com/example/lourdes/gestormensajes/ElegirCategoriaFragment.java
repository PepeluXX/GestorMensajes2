package com.example.lourdes.gestormensajes;

import android.content.Context;
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
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Lourdes on 25/01/2018.
 */

public class ElegirCategoriaFragment extends Fragment {

    boolean categoria = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Para conectar con la BBDD
        BDDHelper miHelper = new BDDHelper(getActivity());

        //Definir layout principal, , y definir parámetros del layout
        final LinearLayout layout_principal = (LinearLayout) view.findViewById(R.id.linearLayout);
        layout_principal.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.fondo));
        //Definir parámetros del layout principal
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        //Para leer datos de la BBDD
        SQLiteDatabase db = miHelper.getReadableDatabase();
        //Recoger los datos del intento que inició esta actividad
        // Bundle datos = getIntent().getExtras();
        //Recoger el nombre de la tabla que contiene los mensajes que se van a mostrar
        //final String nombre_tabla = datos.getString("nombre_tabla");


        //Definir la consulta

        String RAW_QUERY = "SELECT nombre FROM 'categorias'";
        //Ejecutar consulta
        Cursor cursor = db.rawQuery(RAW_QUERY, null);

        cursor.moveToFirst();

        if(cursor.getCount()>0){
            categoria=true;
        }

        if(categoria) {

            for (int i = 0; i < cursor.getCount(); i++) {

                // Crear LinearLayout layout_fila que albergará los elementos
                LinearLayout layout_fila = new LinearLayout(getActivity());
                //parámetros del layout fila
                LinearLayout.LayoutParams param_layout_fila = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                param_layout_fila.height = convertDpToPixel(230, getActivity());
                layout_fila.setOrientation(LinearLayout.HORIZONTAL);
                layout_fila.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.fondo));


                //CREAMOS EL BOTÓN
                final Button boton = new Button(getActivity());
                //le damos parámetros comunes a todos los botones, independientes del tipo de mensaje
                LinearLayout.LayoutParams param_layout_boton = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                param_layout_boton.weight = 4;
                param_layout_boton.width = convertDpToPixel(300, getActivity());
                boton.setLayoutParams(param_layout_boton);
                boton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                //boton.setGravity(0);
                boton.setPadding(20, 20, 0, 20);
                boton.setTypeface(null, Typeface.ITALIC);
                boton.setId(i);

                boton.setText(cursor.getString(0));
               // boton.setBackgroundColor(getResources().getColor(R.color.fondo_layout_fila_curso));

                // Se pone el boton principal a la escucha de ser pulsado
                boton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Fragment fragment = new CategorizadosFragment();
                        if(fragment!=null) {
                            Bundle datos = new Bundle();
                            datos.putString("categoria",boton.getText().toString());
                            fragment.setArguments(datos);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction ft = fragmentManager.beginTransaction();
                            ft.replace(R.id.screen_area, fragment).addToBackStack("root_fragment");
                            ft.commit();
                        }
                        //Finalizamos la actividad actual
                        //getActivity().finish();

                    }
                });

                //Añadimos a los layout

                //elementos al layout_fila
                layout_fila.addView(boton);

                //y la fila al layout principal
                layout_principal.addView(layout_fila);

                cursor.moveToNext();

            }//end for

        }else
        {
            TextView textView = (TextView)getActivity().findViewById(R.id.texto_no_categorias);
            textView.setText("No hay categorías creadas.");
            textView.setPadding(20,20,0,0);
            textView.setTextSize(20);

        }


    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_tres_puntos,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();

        if(id==R.id.action_settings){
           /* Intent intent2 = new Intent(getActivity().getApplicationContext(),CrearCategoria.class);
            startActivity(intent2);*/

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
            return true;
        }

        return false;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_elegir_categoria_fragment,null);
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

}

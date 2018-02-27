package com.example.lourdes.gestormensajes;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Lourdes on 26/02/2018.
 */

public class CrearNotaFragment extends Fragment {

    EditText texto_nota;
    Button crear_nota;
    Bundle datos;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        texto_nota = (EditText)view.findViewById(R.id.texto_nota);
        crear_nota = (Button)view.findViewById(R.id.crear_nota);
        //datos = getIntent().getExtras();

        crear_nota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(texto_nota.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Añada alguna nota sobre este mensaje.",Toast.LENGTH_SHORT).show();
                }
                else{
                    String nota = texto_nota.getText().toString();
                    final BDDHelper mDbHelper = new BDDHelper(getActivity());
                    //Para poder escribir datos en la BBDD
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();



                    //Crear un nuevo mapa de valores, donde los nombres de las columnas son las keys
                    ContentValues values = new ContentValues();
                    //values.put("id",1);
                    values.put("nota",nota);


                    //Indicar la fila en la que se van a modificar datos
                    String selection = " id LIKE ?";
                    String[] selectionArgs = {String.valueOf(getArguments().getInt("id"))};
                    //Ejecutar la consulta
                    int count = db.update(
                            getArguments().getString("tabla"),
                            values,
                            selection,
                            selectionArgs);

                    if(count!=0){
                        Toast.makeText(getActivity(),"Se ha añadido la nota al mensaje",Toast.LENGTH_SHORT).show();
                        texto_nota.setText("");
                        //finish();

                    }

                    /*
                    Fragment fragment = new FragmentMuestraMensaje2();
                    Bundle datos = new Bundle();
                    datos.putInt("id",getArguments().getInt("id"));
                    datos.putString("tabla",getArguments().getString("tabla"));
                    datos.putString("titulo",getArguments().getString("titulo"));
                    fragment.setArguments(datos);


                    if (fragment != null) {
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.popBackStack("root_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.screen_area, fragment).addToBackStack("root_fragment");
                        ft.commit();
                    }
                    */

                }
            }
        });



    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_crear_nota,null);
    }


}

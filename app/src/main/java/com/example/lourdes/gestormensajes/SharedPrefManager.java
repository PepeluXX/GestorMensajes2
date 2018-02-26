package com.example.lourdes.gestormensajes;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


/*
* Gestiona el almacenamiento de datos de manera persistente y el acceso a los mismos.
*
* @author  Jose Luis
* @version 1.0
* @since 20//11/2017
*
*/
public class SharedPrefManager {

    //Definir este contexto
    private static Context mCtx;
    //Definir objeto de esta clase
    private static SharedPrefManager mInstance;
    //Darle un nombre al conjunto de los datos
    private static final String SHARED_PREF_NAME="mi_caja_de_datos";
    //Darle nombre a uno de los datos que se van almacenar
    private static final String KEY_TOKEN = "token";

    //para controlar si se cerró o no sesion, para mostrar o no el formulario
    private static final String SESION ="sesion";

    //para comprobar si el registro inicial se realizó o no
    private static final String REGISTRADO = "registrado";


    //Constructor de la clase
    private SharedPrefManager(Context context)
    {
        mCtx = context;
    }

    //Método de la clase, es necesario implementarlo siempre como se indica en la API Android
    public static synchronized SharedPrefManager getInstance(Context context){
        if(mInstance==null)
            mInstance = new SharedPrefManager(context);
        return mInstance;
    }

    //crea la variable con la que se contralará si la sesión se cierra o no
    public int creaVariableRegistrado(){
        //Para acceder a los datos
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        //Para editar los datos
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Guardar el token con key 'KEY_TOKEN'
        editor.putInt(REGISTRADO,0);
        //Aplicar para guardar datos
        editor.apply();
        return 0;

    }

    public int getRegistrado(){
        //Crear objeto de la clase SharedPreferences para acceder a sus datos
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        //Tomar el dato guardado identificándolo con su key y devolverlo
        //si la variable sesion no se ha creado aún, devuelve 0
        return sharedPreferences.getInt(REGISTRADO,2);
    }

    public boolean switchRegistrado(int registrado){
        //Para acceder a los datos
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        //Para editar los datos
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Guardar el token con key 'KEY_TOKEN'
        editor.putInt(REGISTRADO,registrado);
        //Aplicar para guardar datos
        editor.apply();
        return true;

    }



    //crea la variable con la que se contralará si la sesión se cierra o no
    public int creaVariableSesion(){
        //Para acceder a los datos
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        //Para editar los datos
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Guardar el token con key 'KEY_TOKEN'
        editor.putInt(SESION,0);
        //Aplicar para guardar datos
        editor.apply();
        return 0;

    }

    public int getSesion(){
        //Crear objeto de la clase SharedPreferences para acceder a sus datos
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        //Tomar el dato guardado identificándolo con su key y devolverlo
        //si la variable sesion no se ha creado aún, devuelve 0
        return sharedPreferences.getInt(SESION,2);
    }


    public boolean switchSesion(int sesion){
        //Para acceder a los datos
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        //Para editar los datos
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Guardar el token con key 'KEY_TOKEN'
        editor.putInt(SESION,sesion);
        //Aplicar para guardar datos
        editor.apply();
        return true;

    }



    /*
   * Guarda el token recibido desde los servidores de FCM, se ejecuta por tanto una vez con cada nueva instalación
   * de la aplicación.
   *
   * @param token el token a guardar
   *
   * */

    public boolean storeToken(String token){
        //Para acceder a los datos
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        //Para editar los datos
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Guardar el token con key 'KEY_TOKEN'
        editor.putString(KEY_TOKEN,token);
        //Aplicar para guardar datos
        editor.apply();
        return true;

    }

    /*
   * Devuelve el token guardado en SharedPreferences
   *
   * @return token el token guardado o null si este no existe
   *
   * */

    public String getToken(){
        //Crear objeto de la clase SharedPreferences para acceder a sus datos
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        //Tomar el dato guardado identificándolo con su key y devolverlo
        return sharedPreferences.getString(KEY_TOKEN,null);
    }

}//end of class
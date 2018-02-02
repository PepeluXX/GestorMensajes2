package com.example.lourdes.gestormensajes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Scope;

import java.lang.reflect.Type;
import java.util.ArrayList;


/*
* Se encarga de mostrar una lista con todos los mensajes contenidos en la tabla indicada, que será una tabla
* correspondiente a mensajes para hijos o para cursos.
*
* @author  Jose Luis
* @version 1.0
*
*/

public class ListaMensajes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Para conectar con la BBDD
    public BDDHelper miHelper = new BDDHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_mensajes);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
       /* TextView texto5 = (TextView)findViewById(R.id.texto_tiene_mensajes);
        texto5.setText("dinga??");*/
        TextView texto = (TextView)findViewById(R.id.texto_tiene_mensajes);
        TextView texto2 = (TextView)findViewById(R.id.texto_tiene_mensajes_continuacion);
        texto.setPadding(20,20,5,5);
        texto2.setPadding(20,20,5,5);
        //Para conectar con la BBDD
        BDDHelper miHelper = new BDDHelper(this);

        //Comprobar si hay mensajes del tipo seleccionado

        SQLiteDatabase db = miHelper.getReadableDatabase();
        String RAW_QUERY = "SELECT name FROM sqlite_master WHERE type='table' and name not in('tokens','categorias','android_metadata')";
        Cursor cursor = db.rawQuery(RAW_QUERY, null);
        cursor.moveToFirst();

        ArrayList<String> nombres_tablas = new ArrayList<>();

        for(int i=0;i<cursor.getCount();i++){

            String RAW_QUERY_2= "SELECT leido FROM '"+cursor.getString(0)+"' ";
            Cursor cursor2 = db.rawQuery(RAW_QUERY_2,null);
            cursor2.moveToFirst();

            String nombre_tabla="";

            for(int j=0;j<cursor2.getCount();j++){
                Log.d("getInt",""+cursor2.getInt(0));
                if(cursor2.getInt(0)==0){
                    if(cursor.getString(0).startsWith("curso")){
                       nombre_tabla = cursor.getString(0).substring(6,cursor.getString(0).length()).replace("_"," ".replace("!",""));
                    }else if(cursor.getString(0).startsWith("hijo")){
                        nombre_tabla = cursor.getString(0).substring(5,cursor.getString(0).length()).replace("_"," ").replace("!","");
                    }else{
                         nombre_tabla="General";
                    }
                    nombres_tablas.add(nombre_tabla);
                    break;
                }
                cursor2.moveToNext();
            }
            cursor.moveToNext();
        }


        if(nombres_tablas.size()>0){

            texto.setText("\n-----Tiene mensajes sin leer de:----");


            String para = "";
            for(int ii=0;ii<nombres_tablas.size();ii++){
                para = para +nombres_tablas.get(ii)+".\n\n ";
            }
            para=para.substring(0,para.length()-1);
            para=para.replace("_"," ");
            para=para.replace("!","");
            texto2.setText(para);

        }
        db.close();


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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


  /* GradientDrawable border = new GradientDrawable();
            border.setColor(ContextCompat.getColor(this,R.color.colorBordeLayoutListaMensajes)); //white background
            border.setStroke(2, ContextCompat.getColor(this,R.color.colorBordeSeparacion)); //black border with full opacity


          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                layout_fila.setBackground(border);
            }*/
  @Override
  public void onBackPressed() {
      DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      if (drawer.isDrawerOpen(GravityCompat.START)) {
          drawer.closeDrawer(GravityCompat.START);
      } else {
          super.onBackPressed();
      }

  }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        Fragment fragment = null;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cursos) {
            fragment= new PorCursosFragment();
        } else if (id == R.id.nav_hijos) {
            fragment= new PorHijosFragment();
        } else if (id == R.id.nav_sin_leer) {
            fragment= new SinLeerFragment();
        } else if (id == R.id.nav_todos_leidos) {
            fragment= new TodosLeidosFragment();
        } else if (id == R.id.nav_categorizados) {
            fragment= new ElegirCategoriaFragment();
        }else if(id==R.id.nav_general){
            fragment = new GeneralFragment();
        }else if(id==R.id.nav_acerca_de){
            fragment = new AcercaDeFragment();
        }

        if(fragment!=null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack("root_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.screen_area,fragment).addToBackStack("root_fragment");
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }





}//end of class
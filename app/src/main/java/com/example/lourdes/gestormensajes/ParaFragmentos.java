package com.example.lourdes.gestormensajes;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

public class ParaFragmentos extends ListaMensajes {

    Bundle datos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_para_fragmentos);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        LinearLayout sideNavLayout = (LinearLayout)header.findViewById(R.id.sideNavLayout);
        sideNavLayout.setBackgroundResource(R.drawable.botones);

        navigationView.setNavigationItemSelectedListener(this);



        Bundle datos = getIntent().getExtras();

        String fragmento = datos.getString("fragmento");

        Fragment fragment = null;

        if(fragmento.equals("curso")){
            fragment= new PorCursosFragment();

        }
        else if(fragmento.equals("hijos")){
            fragment= new PorHijosFragment();
        }
        else if(fragmento.equals("sin_leer")){
            fragment = new SinLeerFragment();
        }
        else if(fragmento.equals("leidos")){
            fragment = new TodosLeidosFragment();
        }
        else if(fragmento.equals("categ")){
            fragment = new CategorizadosFragment();
            Bundle datos2 = new Bundle();
            datos2.putString("categoria",datos.getString("categoria"));
            fragment.setArguments(datos2);
        }
        else if(fragmento.equals("gen")){
            fragment = new GeneralFragment();
        }
        //dingueando
        else if(fragmento.equals("muestra_mensaje")){
            fragment = new FragmentMuestraMensaje2();
            Bundle datos2 = new Bundle();
            datos2.putInt("id",datos.getInt("id_mensaje"));
            datos2.putString("titulo",datos.getString("titulo"));
            datos2.putString("tabla",datos.getString("nombre_tabla"));
            fragment.setArguments(datos2);
        }
        // fin dingueando

        if(fragment!=null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack("root_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.screen_area, fragment).addToBackStack("root_fragment");
            ft.commit();
        }

    }

 @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }



}
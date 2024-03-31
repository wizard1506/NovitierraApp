package com.example.novitierraapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novitierraapp.entidades.Global;
import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class navMenu extends AppCompatActivity {
    TextView nombreSesion,userSesion,grupoSesion;

//    ImageView casita;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.formularios,R.id.formCoSolicitante,R.id.mapFragment,R.id.encuesta,R.id.formularioMapa,R.id.cargarFormulario,R.id.misFormularios,R.id.prospectos,R.id.prospectosHoy,R.id.perfil,R.id.updatePassword,R.id.prospectosLista,R.id.titulares,R.id.informacion)
                .setDrawerLayout(drawer)
                .build();

//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.formularios,R.id.formCoSolicitante,R.id.mapFragment)
//                .setDrawerLayout(drawer)
//                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Global.userSesion="";
//        Global.nombreSesion="";
//        Global.apellidoSesion="";
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        nombreSesion = findViewById(R.id.nombreSesion);
        userSesion = findViewById(R.id.usuarioSesion);
        grupoSesion = findViewById(R.id.grupoSesion);

        nombreSesion.setText(Global.nombreSesion+" "+Global.apellidoSesion);
        userSesion.setText(Global.userSesion);
        if(Global.grupo.contains("NINGUNO")){
            grupoSesion.setVisibility(View.GONE);
        }else {
            grupoSesion.setText(Global.grupo);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onBackPressed() {
        // Aquí puedes poner la lógica para manejar el botón de atrás
        // Obtén el NavController de tu actividad
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

// Verifica si el fragmento actual es nav_home
        if (navController.getCurrentDestination().getId() == R.id.nav_home) {
            // Por ejemplo, si quieres mostrar un diálogo de confirmación antes de cerrar la actividad
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Estás seguro de que quieres salir?");
            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish(); // Cierra la actividad actual
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); // Descarta el diálogo sin cerrar la actividad
                }
            });
            builder.show();
        } else {
            super.onBackPressed();
        }


    }




}
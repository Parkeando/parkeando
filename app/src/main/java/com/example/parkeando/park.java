package com.example.parkeando;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class park extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;


    private TextView txtPerfil, txtCorreo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_park2 );

       // txtCorreo = (TextView) findViewById ( R.id.txtCorreo ) ;
        // :::SESION INICIADA::::
        final String usuario = PreferencesSesion.obtenerPreferenceString(this,PreferencesSesion.PREFERENCE_ESTADO_LOGIN);
        Toast.makeText(this, "Bienvenido: " +  usuario, Toast.LENGTH_SHORT).show();

        final String correo = PreferencesSesion.obtenerPreferenceString ( this,PreferencesSesion.PREFERENCE_ESTADO_CORREO );

/////


        Toolbar toolbar = findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );
        FloatingActionButton fab = findViewById ( R.id.fab );
        fab.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Snackbar.make ( view, "Replace with your own action", Snackbar.LENGTH_LONG )
                        .setAction ( "Action", null ).show ();
            }
        } );
        DrawerLayout drawer = findViewById ( R.id.drawer_layout );
         NavigationView navigationView = findViewById ( R.id.nav_view );

        View hView = navigationView.getHeaderView(0);
        txtPerfil = (TextView) hView.findViewById ( R.id.txtPerfilNombre );
        txtPerfil.setText ( usuario );

        txtCorreo = (TextView) hView.findViewById ( R.id.txtCorreo ) ;
        txtCorreo.setText ( correo );
      navigationView.setNavigationItemSelectedListener(this );



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder (
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send )
                .setDrawerLayout ( drawer )
                .build ();
        NavController navController = Navigation.findNavController ( this, R.id.nav_host_fragment );
        NavigationUI.setupActionBarWithNavController ( this, navController, mAppBarConfiguration );
        NavigationUI.setupWithNavController ( navigationView, navController );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ().inflate ( R.menu.park, menu );
        return true;
    }
//Control de seleccion de item menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Selecion de item de cerrar sesion
        switch (item.getItemId ()) {
            case R.id.cerrarSesion:
               PreferencesSesion ps = new PreferencesSesion ();
                ps.CambiarSesion ( getApplicationContext (), false );
                Intent i = new Intent ( getApplicationContext (), MainActivity.class );
                startActivity ( i );
                finish ();



                return true;
            default:

                return super.onOptionsItemSelected ( item );
        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController ( this, R.id.nav_host_fragment );
        return NavigationUI.navigateUp ( navController, mAppBarConfiguration )
                || super.onSupportNavigateUp ();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}

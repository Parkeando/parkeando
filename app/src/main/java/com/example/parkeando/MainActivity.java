package com.example.parkeando;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView txtRegistrar;
    private Button btnLoguear;

    private EditText etNombre, etPassword;

    private String nombre, password;


    private boolean isActivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        // SESION
        PreferencesSesion w = new PreferencesSesion();
        isActivate= false;// Mantener apagada la sesion

        if(w.obtenerEstado(this)) {
            //Toast.makeText(this, "estado true ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, park.class);
            startActivity(intent);
        }else{
            //    Toast.makeText(this, "estado false", Toast.LENGTH_SHORT).show();
        }


        ///


        txtRegistrar = (TextView) findViewById ( R.id.txtRegistrar );
        btnLoguear = (Button) findViewById ( R.id.btnLoguear );

        etNombre = (EditText) findViewById(R.id.etNombreLog);
        etPassword = (EditText) findViewById(R.id.etPasswordLog);
        txtRegistrar.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                    abrirRegistrar ();
            }
        } );

        btnLoguear.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                prepararDatosLogueo ();
            }
        } );
    }

    private void abrirRegistrar(){
        Intent i = new Intent (this, Registro.class );
        startActivity ( i );
    }



    private void prepararDatosLogueo() {
        nombre = etNombre.getText().toString();
        password = etPassword.getText().toString();

        WebServiceLoginRegistro wslr = new WebServiceLoginRegistro();
        wslr.loguear(nombre,password,this);
    }



}

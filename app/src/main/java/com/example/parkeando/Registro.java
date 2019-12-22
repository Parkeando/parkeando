package com.example.parkeando;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {
    private TextInputLayout tilNombre, tilCorreo,tilPassword;
    private EditText etNombre, etCorreo, etPassword;
    private Button btnRegistrar;
    private String nombre, correo, password;

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_registro );

        tilNombre = (TextInputLayout) findViewById(R.id.tilnombre);
        tilCorreo = (TextInputLayout) findViewById(R.id.tilcorreo);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);

        etNombre= (EditText) findViewById(R.id.etNombre);
        etCorreo= (EditText) findViewById(R.id.etCorreo);
        etPassword= (EditText) findViewById(R.id.etPassword);

        image = (ImageView) findViewById ( R.id.trazado_105 );

        image.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                regresarLogin ();
            }
        } );

        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);


        btnRegistrar.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                validacion ();
            }
        } );



    }
    private void regresarLogin(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void prepararDatos(){
        nombre = etNombre.getText().toString();
        correo = etCorreo.getText().toString();
        password = etPassword.getText().toString();

        WebServiceLoginRegistro wslr = new WebServiceLoginRegistro();

        wslr.insertar(nombre, correo, password, this);
    }
    private void validacion() {
        String nombret = tilNombre.getEditText().getText().toString();
        String correot = tilCorreo.getEditText().getText().toString();

        boolean a = esNombreValido(nombret);
        boolean c = esCorreoValido(correot);

        if (a && c) {

            prepararDatos();
            // Toast.makeText(this, "Se guarda el registro", Toast.LENGTH_LONG).show();
        }

    }

    private boolean esNombreValido(String nombre) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if (!patron.matcher(nombre).matches() || nombre.length() > 45) {
            tilNombre.setError("Nombre inválido");
            return false;
        } else {
            tilNombre.setError(null);
        }

        return true;
    }
    private boolean esCorreoValido(String correo) {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            tilCorreo.setError("Correo electrónico inválido");
            return false;
        } else {
            tilCorreo.setError(null);
        }

        return true;
    }

}

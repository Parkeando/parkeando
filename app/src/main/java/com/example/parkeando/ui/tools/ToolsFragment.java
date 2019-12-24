package com.example.parkeando.ui.tools;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parkeando.PreferencesSesion;
import com.example.parkeando.R;
import com.example.parkeando.WebServiceLoginRegistro;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;
    private TextInputLayout tilNombrePerfil, tilCorreoPerfil;
    public static EditText etNombrePerfil, etCorreoPerfil, etPasswordPerfil, etPasswordNuevoPerfi;

    private Button btnActualizarPerfil, btnElimnarCuenta;

    WebServiceLoginRegistro wslR;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final String usuario = PreferencesSesion.obtenerPreferenceString(getContext (),PreferencesSesion.PREFERENCE_ESTADO_LOGIN);


        toolsViewModel =
                ViewModelProviders.of ( this ).get ( ToolsViewModel.class );
        View root = inflater.inflate ( R.layout.fragment_tools, container, false );
        tilNombrePerfil = (TextInputLayout) root.findViewById ( R.id.tilNombrePerfil );
        tilCorreoPerfil = (TextInputLayout) root.findViewById ( R.id. tilCorreoPerfil);
        etNombrePerfil = (EditText) root.findViewById ( R.id.etNombrePerfil );
        etCorreoPerfil = (EditText) root.findViewById ( R.id.etCorreoPerfil );
        etPasswordPerfil = (EditText) root.findViewById ( R.id.etPasswordPerfil );
        etPasswordNuevoPerfi = (EditText) root.findViewById ( R.id.etPasswordNuevoPerfil );
        btnActualizarPerfil = (Button) root.findViewById ( R.id.btnPerfilActualizar );
        btnElimnarCuenta = (Button) root.findViewById ( R.id.btnEliminarCuenta );

        wslR = new WebServiceLoginRegistro ();
        //LLenar los campos
        wslR.consultardatosPerfil ( usuario, getContext () );

        disabledEditTexts ();
//Testeo si el habilitar los edittext
        if(disabledEditTexts ()==false){
          dialogActualizacion ();
        }




        btnActualizarPerfil.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {

             validacion ();

            }
        } );

        btnElimnarCuenta.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                dialogEliminacion ();
            }
        } );



       /* final TextView textView = root.findViewById ( R.id.text_tools );
        toolsViewModel.getText ().observe ( this, new Observer<String> () {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText ( s );
            }
        } );*/
        return root;
    }
    public void dialogActualizacion(){
        AlertDialog.Builder dialogAct = new AlertDialog.Builder ( getContext () );
        dialogAct.setTitle ( "Aviso!" );
        dialogAct.setMessage ( "¿Deséa actualizar sus datos de cuenta? " );
        dialogAct.setCancelable ( false );
        dialogAct.setPositiveButton ( "Confirmar", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                enabledEditTexts ();
            }
        } );
        dialogAct.setNegativeButton ( "Cancelar", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                disabledEditTexts ();
            }
        } );
        dialogAct.show ();
    }

    public void dialogEliminacion(){
        AlertDialog.Builder dialogAct = new AlertDialog.Builder ( getContext () );
        dialogAct.setTitle ( "Advertencia !" );
        dialogAct.setMessage ( "¿Realmente desea eliminar su cuenta ? " );
        dialogAct.setCancelable ( false );
        dialogAct.setPositiveButton ( "Confirmar", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    dobleDialogEliminacion ();
            }
        } );
        dialogAct.setNegativeButton ( "Cancelar", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                disabledEditTexts ();
            }
        } );
        dialogAct.show ();
    }
    public void dobleDialogEliminacion(){
        AlertDialog.Builder dialogAct = new AlertDialog.Builder ( getContext () );
        dialogAct.setTitle ( "Advertencia !" );
        dialogAct.setMessage ( "Al eliminar su cuenta perdera su saldo. ¿Realmente desea eliminar su cuenta de parkiando?" );
        dialogAct.setCancelable ( false );
        dialogAct.setPositiveButton ( "Aceptar", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String usuario = PreferencesSesion.obtenerPreferenceString(getContext (),PreferencesSesion.PREFERENCE_ESTADO_LOGIN);
                wslR = new WebServiceLoginRegistro ();
                wslR.eliminarPerfil ( usuario, getContext () );
            }
        } );
        dialogAct.setNegativeButton ( "Cancelar", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                disabledEditTexts ();
            }
        } );
        dialogAct.show ();
    }

    public boolean disabledEditTexts(){
        etNombrePerfil.setEnabled ( false );
        etCorreoPerfil.setEnabled ( false );
        etPasswordPerfil.setEnabled ( false );
        etPasswordNuevoPerfi.setEnabled ( false );
        return  false;
    }

    public boolean enabledEditTexts(){
        etNombrePerfil.setEnabled ( true );
        etCorreoPerfil.setEnabled ( true );
        etPasswordPerfil.setEnabled ( true);
        etPasswordNuevoPerfi.setEnabled ( true );
        return true;
    }


    private void validacion() {


        String nombret = tilNombrePerfil.getEditText().getText().toString();
        String correot = tilCorreoPerfil.getEditText().getText().toString();

        boolean a = esNombreValido(nombret);
        boolean c = esCorreoValido(correot);

        if (a && c) {

            prepararDatosActualizar();
            // Toast.makeText(this, "Se guarda el registro", Toast.LENGTH_LONG).show();
        }

    }

    private void prepararDatosActualizar() {
        final String usuarioIndex = PreferencesSesion.obtenerPreferenceString(getContext (),PreferencesSesion.PREFERENCE_ESTADO_LOGIN);

        final  String nombre = etNombrePerfil.getText().toString();
        final  String correo = etCorreoPerfil.getText().toString();
        final String viejaPassword = etPasswordPerfil.getText ().toString ();

        final String  password = etPasswordNuevoPerfi.getText().toString();

        wslR = new WebServiceLoginRegistro ();
        wslR.actualizarPerfil ( usuarioIndex,nombre, correo,viejaPassword,password,getContext () );


    }


    private boolean esNombreValido(String nombre) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if (!patron.matcher(nombre).matches() || nombre.length() > 45) {
            tilNombrePerfil.setError("Nombre inválido");
            return false;
        } else {
            tilNombrePerfil.setError(null);
        }

        return true;
    }
    private boolean esCorreoValido(String correo) {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            tilCorreoPerfil.setError("Correo electrónico inválido");
            return false;
        } else {
            tilCorreoPerfil.setError(null);
        }

        return true;
    }
}
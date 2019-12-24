package com.example.parkeando;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.parkeando.Config.Config;
import com.example.parkeando.ui.tools.ToolsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WebServiceLoginRegistro {
    private boolean estadoSesion;


    RequestQueue requestQueue;

/*
    Si se requiere cambiar la ip del servidor entrar al apartado de Config y la clase config
  */
   private final String url=  Config.IP_SERVER + "parkeando/Usuarios.php";
    public void insertar(final String nombre, final String correo, final String password, final Context context){
        //Diseño y conecte al webservice

        StringRequest respuesta= new StringRequest( Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String, String> ();

                parametros.put("nombre",nombre);
                parametros.put("email", correo);
                parametros.put("password", password);
                parametros.put("opcion", "register");
                return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(respuesta);


    }

    public void loguear(final String nombre, final String password, final Context context){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject obj = new  JSONObject(response);
                    boolean error = obj.getBoolean("error");
                    String mensaje = obj.getString("mensaje");
                    String correo = obj.getString ( "correo" );
                    //     String mensaje2 = obj.getString("usuario");

                    if (error == true) {
                        Toast.makeText(context, "Error: "+ mensaje, Toast.LENGTH_SHORT).show();



                    } else {

                        Intent i2;
                        i2 = new Intent(context.getApplicationContext(), park.class);
                        i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.getApplicationContext().startActivity(i2);

                        PreferencesSesion ps = new PreferencesSesion();
                        ps.guardarSesion(context);
                      //  Toast.makeText ( context, correo, Toast.LENGTH_SHORT ).show ();
                        ps.savePreferenceString(context, obj.getString("nombre"),PreferencesSesion.PREFERENCE_ESTADO_LOGIN);
                        Toast.makeText(context,  mensaje, Toast.LENGTH_SHORT).show();
                        ps.savePreferenceStringEmail ( context, correo, PreferencesSesion.PREFERENCE_ESTADO_CORREO );



                        //   Toast.makeText(getApplicationContext(), mensaje2, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){

            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("nombre", nombre);
                params.put("password", password);
                params.put("opcion", "login");
                return params;
            }
        };
        requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }



    public void actualizarPerfil (final String indexNombre, final String nombre,
                                  final String correo, final String viejaPassword, final String password, final Context context ){

        final StringRequest respuesta= new StringRequest( Request.Method.POST,
                url,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject ( response );
                            boolean error = obj.getBoolean ( "error" );
                            String mensaje = obj.getString ( "mensaje" );

                            //  Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();


                            if (error== true){
                                Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();

                            }else{
                                Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();
                                PreferencesSesion ps = new PreferencesSesion ();
                                ps.CambiarSesion ( context, false );
                                Intent i2;
                                i2 = new Intent(context.getApplicationContext(), MainActivity.class);
                                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.getApplicationContext().startActivity(i2);
                            }



                        }catch (JSONException e){

                            Toast.makeText ( context, e.getMessage (), Toast.LENGTH_SHORT ).show ();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String, String> ();

                parametros.put("indexNombre", indexNombre );
                parametros.put("nombre", nombre);
                parametros.put("correo", correo);
                parametros.put("viejaPassword", viejaPassword);
                parametros.put("password", password);
                parametros.put("opcion", "actualizar");
                return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(respuesta);
    }

    public void consultardatosPerfil(final String usuario, final Context context){
        final StringRequest respuesta= new StringRequest( Request.Method.POST,
                url,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject ( response );
                            boolean error = obj.getBoolean ( "error" );
                            String mensaje = obj.getString ( "mensaje" );

                            String getNombre = obj.getString ( "nombre" );
                            String getEmail = obj.getString ( "email" );

                            //  Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();


                            if (error== true){
                                Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();

                            }else{
                                //Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();
                            //Llamo al editText del fragment de perfil (TOOLS)
                                ToolsFragment.etNombrePerfil = ((Activity)context).findViewById ( R.id.etNombrePerfil );
                                ToolsFragment.etCorreoPerfil = ((Activity)context).findViewById ( R.id.etCorreoPerfil);
                                ToolsFragment.etNombrePerfil.setText ( getNombre );
                                ToolsFragment.etCorreoPerfil.setText ( getEmail );


                            }



                        }catch (JSONException e){

                            Toast.makeText ( context, "Fallo en jSOn"+ e.getMessage (), Toast.LENGTH_SHORT ).show ();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String, String> ();

                parametros.put("user", usuario);
                parametros.put("opcion", "completarPerfil");
                return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(respuesta);
    }

    public void eliminarPerfil(final String usuario, final Context context){
       // Toast.makeText ( context, usuario, Toast.LENGTH_SHORT ).show ();
        final StringRequest respuesta= new StringRequest( Request.Method.POST,
                url,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject ( response );
                            boolean error = obj.getBoolean ( "error" );
                            String mensaje = obj.getString ( "mensaje" );



                            //  Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();


                            if (error== true){
                                Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();

                            }else{
                                Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();

                                PreferencesSesion ps = new PreferencesSesion ();
                                ps.CambiarSesion ( context, false );
                                Intent i2;
                                i2 = new Intent(context.getApplicationContext(), MainActivity.class);
                                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.getApplicationContext().startActivity(i2);


                            }



                        }catch (JSONException e){

                            Toast.makeText ( context, "Fallo en jSOn"+ e.getMessage (), Toast.LENGTH_SHORT ).show ();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String, String> ();

                parametros.put("user", usuario);
                parametros.put("opcion", "eliminarPerfil");
                return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(respuesta);
    }
}

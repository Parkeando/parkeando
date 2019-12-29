package com.example.parkeando;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.parkeando.Config.Config;
import com.example.parkeando.ui.slideshow.SlideshowFragment;
import com.example.parkeando.ui.tools.ToolsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WebServiceEstacionamiento {
    RequestQueue requestQueue;

    private final String url=  Config.IP_SERVER + "parkeando/estacionamiento.php";

    public void consultarEstacionamiento(final String estacionamiento, final Context context){

        final StringRequest respuesta= new StringRequest( Request.Method.POST,
                url,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject ( response );
                            boolean error = obj.getBoolean ( "error" );
                            String mensaje = obj.getString ( "mensaje" );
                            String lugar = obj.getString ( "lugar" );



                            //  Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();


                            if (error== true){
                                Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();

                            }else{
                                //QR Exitoso o que existe el estacionamiento en el sistema
                             //   Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();

                               switch (mensaje){
                                   case "Usted esta Entrando":
                                       Toast.makeText ( context,"Entrar", Toast.LENGTH_SHORT ).show ();
                                       final String usuario = PreferencesSesion.obtenerPreferenceString(context,PreferencesSesion.PREFERENCE_ESTADO_LOGIN);
                                      // Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();
                                       Toast.makeText ( context, lugar, Toast.LENGTH_SHORT ).show ();
                                       AlertDialog.Builder dialogAct = new AlertDialog.Builder ( context );
                                       dialogAct.setTitle ( "Esta por ingresar a " + lugar );
                                       dialogAct.setNegativeButton ( "Cancelar", new DialogInterface.OnClickListener () {
                                           @Override
                                           public void onClick(DialogInterface dialogInterface, int i) {

                                           }
                                       } );
                                       dialogAct.setMessage ( "¿Desea ingresar?" );
                                       dialogAct.setCancelable ( false );
                                       dialogAct.setPositiveButton ( "Aceptar", new DialogInterface.OnClickListener () {
                                           @Override
                                           public void onClick(DialogInterface dialogInterface, int i) {
                                               //Si el usuario acepta ingresar al estacionamiento procede a descontar dinero la app

                                            Calendar calendar = Calendar.getInstance ();
                                            int hora,min,seg;

                                            hora = calendar.get(Calendar.HOUR_OF_DAY);
                                            min = calendar.get(Calendar.MINUTE);
                                            seg = calendar.get(Calendar.SECOND);

                                            String tiempoActual = hora + ":" + min + ":" + seg;

                                            int horaS = hora + 1;

                                            String tiempoSalida = horaS + ":" + min + ":" + seg;
                                              // Toast.makeText ( context, tiempoActual, Toast.LENGTH_SHORT ).show ();

                                                //llamamos al metodo que se comunica con Webservice para descontar la primera hora
                                              descontarSaldo(usuario, estacionamiento, tiempoActual, tiempoSalida, context);

                                           }
                                       } );
                                       dialogAct.setNegativeButton ( "Cancelar", new DialogInterface.OnClickListener () {
                                           @Override
                                           public void onClick(DialogInterface dialogInterface, int i) {
                                               Toast.makeText ( context, "Acceso no concedido.", Toast.LENGTH_LONG ).show ();

                                               Intent i2 = new Intent ( context, park.class );
                                               context.startActivity ( i2 );
                                           }
                                       } );
                                       dialogAct.show ();







                                       break;
                                   case  "Usted esta Saliendo":
                                      // Toast.makeText ( context,"Usted esta Saliendo de " + estacionamiento, Toast.LENGTH_SHORT ).show ();

                                       final String usuario2 = PreferencesSesion.obtenerPreferenceString(context,PreferencesSesion.PREFERENCE_ESTADO_LOGIN);
                                       Calendar calendar = Calendar.getInstance ();
                                       int hora,min,seg;

                                       hora = calendar.get(Calendar.HOUR_OF_DAY);
                                       min = calendar.get(Calendar.MINUTE);
                                       seg = calendar.get(Calendar.SECOND);
//Tiempo en que salio
                                       String tiempoActual = hora + ":" + min + ":" + seg;


                                       descontarSaldoSalida ( usuario2, estacionamiento, tiempoActual,context );




                                       break;
                                       default:

                                           break;

                               }

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

                parametros.put("estacionamiento", estacionamiento);
                parametros.put("opcion", "consultarEstacionamiento");
                return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(respuesta);
    }

    private void descontarSaldo(final String usuario, final String estacionamiento, final String horaEntrada, final String horaSalida,
                                final Context context) {

        final StringRequest respuesta= new StringRequest( Request.Method.POST,
                url,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject ( response );
                            boolean error = obj.getBoolean ( "error" );
                            String mensaje = obj.getString ( "mensaje" );

                            String entrada = obj.getString ( "horaEntrada" );
                            String salida = obj.getString ( "horaSalida" );




                            //  Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();


                            if (error== true){
                                Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();

                            }else{
                                //QR Exitoso y posterior descuento de saldo apartir de aqui
                                // COLOCAR LA FUNCION DE LEVANTAR PLUMILLA DE ENTRADA AL ESTACIONAMIENTO
                                /////////////////////////////////////////////////////////////
                                   Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();

                                Toast.makeText ( context, "Tu hora de entrada es: "+entrada, Toast.LENGTH_SHORT ).show ();

                                Toast.makeText ( context, "Tu hora de salida estimada es: " +  salida, Toast.LENGTH_SHORT ).show ();




/*
                                //impresion de la hora de entrada.
                                TextView txtTiempo = ((Activity)context).findViewById ( R.id.txtTiempo );

                                TextView txtTiempoSalida = ((Activity)context).findViewById ( R.id.txtTiempoSalida );

                                txtTiempo.setText ( entrada );
                                txtTiempoSalida.setText ( salida );*/
/*
                                Intent i = new Intent ( context, park.class );
                               context.startActivity ( i );*/





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

                parametros.put("usuario", usuario);
                parametros.put("estacionamiento", estacionamiento);
                parametros.put("horaEntrada", horaEntrada);
              parametros.put("horaSalida", horaSalida);
                parametros.put("opcion", "descontarSaldo");
                return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(respuesta);
    }





    public void descontarSaldoSalida(final String usuario, final String estacionamineto, final String horaSalida,
                                     final Context context){

        final StringRequest respuesta= new StringRequest( Request.Method.POST,
                url,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject ( response );
                            boolean error = obj.getBoolean ( "error" );
                            String mensaje = obj.getString ( "mensaje" );
                            //          String lugar = obj.getString ( "lugar" );




                            //  Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();


                            if (error== true){


                                switch (mensaje){
                                    case "Saldo insuficiente":
                                        //no COLOCAR LA FUNCION DE LA PLUMILLA
                                        Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();
                                        break;

                                    case "Saldo actualizado":
                                        // Aqui colocar la funcion de la plumilla
                                        // puesto que ya hizo el descuento del dinero

                                        String status = obj.getString ( "status" );
                                       // Toast.makeText ( context, status, Toast.LENGTH_LONG ).show ();
                                        String tiempo = obj.getString ( "tiempo" );

                                        final AlertDialog.Builder dialogAct = new AlertDialog.Builder ( context );
                                        dialogAct.setTitle ( "Información de cobro: ");
                                        dialogAct.setMessage ( status + " pesos "+" y " + tiempo + " horas");
                                        dialogAct.setCancelable ( false );
                                        dialogAct.setPositiveButton ( "OK", new DialogInterface.OnClickListener () {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel ();
                                                Intent i3 = new Intent (context, park.class);
                                                context.startActivity ( i3 );

                                            }
                                        } );
                                        dialogAct.show ();

                                }

                            }else{
                                //QR Exitoso o que existe el estacionamiento en el sistema
                                Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();
                            //Aqui colocar la funcion de salida plumilla


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

                parametros.put("usuario", usuario);
                parametros.put("estacionamiento", estacionamineto);
                parametros.put("horaSalida", horaSalida);

                parametros.put("opcion", "descontarSaldoSalida");
                return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(respuesta);


    }












    public void consultarEstacionamiento2(final String usuario, final Context context){

        final StringRequest respuesta= new StringRequest( Request.Method.POST,
                url,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject ( response );
                            boolean error = obj.getBoolean ( "error" );
                            String mensaje = obj.getString ( "mensaje" );
                  //          String lugar = obj.getString ( "lugar" );

                                String horaEntrada = obj.getString ( "horaEntrada" );
                             String horaSalida = obj.getString ( "horaSalida" );



                            //  Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();


                            if (error== true){
                                Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();

                            }else{
                                //QR Exitoso o que existe el estacionamiento en el sistema
                                Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();
                               TextView txtTiempo = ((Activity)context).findViewById ( R.id.txtTiempo );

                                TextView txtTiempoSalida = ((Activity)context).findViewById ( R.id.txtTiempoSalida );




                                if (horaEntrada.equals ( "null" ) && horaSalida.equals ( "null" )){
                                    txtTiempo.setText ( "00.00" );
                                    txtTiempoSalida.setText ("00.00");
                                }else{
                                    txtTiempo.setText ( horaEntrada );
                                    txtTiempoSalida.setText ( horaSalida );
                                }



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

                parametros.put("usuario", usuario);
                parametros.put("opcion", "consultarTiempo");
                return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(respuesta);
    }




}

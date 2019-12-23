package com.example.parkeando;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
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
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class WebServiceSaldo {

    RequestQueue requestQueue;
    private final String url= Config.IP_SERVER +"parkeando/cuenta.php";

  private  Context mcontext;

  public WebServiceSaldo(Context contex){
this.mcontext = contex;
  }

    public void abonarSaldo(final float saldo, final String usuario, final String tarjeta, final String opcion, final Context context){
        //Diseño y conecte al webservice



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

                parametros.put("saldo", String.valueOf ( saldo ) );
                parametros.put("usuario", usuario);
                parametros.put("tarjeta", tarjeta);
                parametros.put("opcion", opcion);
                return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(respuesta);

    }

    public void consultarSaldo(final String usuario, final Context context){


        final StringRequest respuesta= new StringRequest( Request.Method.POST,
                url,
                new Response.Listener<String>() {


                    @Override
                    public  void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject ( response );
                            boolean error = obj.getBoolean ( "error" );
                            String mensaje = obj.getString ( "mensaje" );

                            //  Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();


                            if (error== true){
                                Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();

                            }else{
                              //

                            //   s = new Saldos(context);
                               // s.setSaldo ( mensaje, context );

                                Toast.makeText ( context, mensaje, Toast.LENGTH_SHORT ).show ();
                       NavigationView navigationView = ((Activity)context).findViewById ( R.id.nav_view );

                                View hView = navigationView.getHeaderView(0);
                        TextView txtSaldo = hView.findViewById ( R.id.txtSaldo );
                        txtSaldo.setText ("Su saldo es de: " + "$ " +  mensaje );
                                  navigationView.setNavigationItemSelectedListener( (NavigationView.OnNavigationItemSelectedListener) context);

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


                parametros.put("usuario", usuario);
                parametros.put("opcion", "consultarSaldo");
                return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(respuesta);
    }



}

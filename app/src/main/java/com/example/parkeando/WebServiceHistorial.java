package com.example.parkeando;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.parkeando.Config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebServiceHistorial {

    RequestQueue requestQueue;

    private final String url=  Config.IP_SERVER + "parkeando/historial.php";

    List<HistorialModelo> userModels = new ArrayList<> ();

    private HistorialAdaptador historialAdaptador;


    public void consultarHistorial(final String usuario, final Context context){


        StringRequest respuesta= new StringRequest( Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Toast.makeText(context, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jsonArray = new JSONArray ( response );


                            JSONObject jsonObject = null ;
                            for (int i=0; i<jsonArray.length (); i++){



                                jsonObject =jsonArray.getJSONObject ( i );
                            String lugar = jsonObject.getString ( "lugar");

                                String fecha = jsonObject.getString (  "fecha");


                                userModels.add ( new HistorialModelo ("Lugar: "+ lugar, "Fecha: "+ fecha));



                            }
                            RecyclerView recyclerView = (RecyclerView) ((Activity)context).findViewById ( R.id.historialRecicler );
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager (context));

                            historialAdaptador = new HistorialAdaptador ( userModels );
                            recyclerView.setAdapter (  historialAdaptador);

                        } catch (JSONException ex) {
                            ex.printStackTrace ();
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

                parametros.put("usuario",usuario);
                return parametros;
            }
        };

        requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(respuesta);

    }

}

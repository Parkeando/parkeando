package com.example.parkeando.ui.slideshow;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parkeando.PreferencesSesion;
import com.example.parkeando.R;
import com.example.parkeando.Temporizador;
import com.example.parkeando.WebServiceEstacionamiento;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.Locale;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of ( this ).get ( SlideshowViewModel.class );
        View root = inflater.inflate ( R.layout.fragment_slideshow, container, false );
        final String usuario = PreferencesSesion.obtenerPreferenceString(getContext (),PreferencesSesion.PREFERENCE_ESTADO_LOGIN);

       /* final TextView textView = root.findViewById ( R.id.text_slideshow );
        slideshowViewModel.getText ().observe ( this, new Observer<String> () {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText ( s );
            }
        } );*/


        WebServiceEstacionamiento wsE = new WebServiceEstacionamiento ();
        wsE.consultarEstacionamiento2 ( usuario, getContext () );

        TextView txtTiempo = root.findViewById ( R.id.txtTiempo );

        TextView txtTiempoSalida = root.findViewById ( R.id.txtTiempoSalida );





        return root;
    }



    public void obtenerFecha(){

    }
}
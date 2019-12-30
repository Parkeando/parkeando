package com.example.parkeando.ui.share;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkeando.HistorialAdaptador;
import com.example.parkeando.HistorialModelo;
import com.example.parkeando.PreferencesSesion;
import com.example.parkeando.R;
import com.example.parkeando.WebServiceHistorial;

import java.util.ArrayList;
import java.util.List;

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;

    private HistorialAdaptador historialAdaptador;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of ( this ).get ( ShareViewModel.class );
        View root = inflater.inflate ( R.layout.fragment_share, container, false );

        final String usuario = PreferencesSesion.obtenerPreferenceString(getContext (),PreferencesSesion.PREFERENCE_ESTADO_LOGIN);

        WebServiceHistorial wsH = new WebServiceHistorial ();

        wsH.consultarHistorial ( usuario, getContext () );


       /* final TextView textView = root.findViewById ( R.id.text_share );
        shareViewModel.getText ().observe ( this, new Observer<String> () {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText ( s );
            }
        } );*/

    /*    RecyclerView recyclerView = (RecyclerView) root.findViewById ( R.id.historialRecicler );
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager (getContext ()));

        historialAdaptador = new HistorialAdaptador (getData());
        recyclerView.setAdapter(historialAdaptador);*/

       
        return root;
    }


}
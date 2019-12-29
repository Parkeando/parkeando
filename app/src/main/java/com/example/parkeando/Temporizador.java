package com.example.parkeando;

import android.content.Context;
import android.widget.Toast;

public class Temporizador extends Thread{
Context mcontext;

public Temporizador(Context c){
    this.mcontext=c;
    }


    @Override
    public void run() {
        try {
            Thread.sleep(2000);

            Toast.makeText ( mcontext, "Termino proceso", Toast.LENGTH_SHORT ).show ();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

package com.example.parkeando;

public class HistorialModelo {

    private String lugar;
    private String fecha;




    public HistorialModelo(String lugar, String fecha) {
        this.lugar = lugar;
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }
    public String getFecha() {
        return fecha;
    }



}

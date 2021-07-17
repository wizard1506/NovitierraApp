package com.example.novitierraapp;

import androidx.annotation.NonNull;

public class Proyectos {

    private Integer codigo;
    private String urbanizacion;

    public Proyectos() {
    }


    public Proyectos(Integer codigo, String urbanizacion) {
        this.codigo = codigo;
        this.urbanizacion = urbanizacion;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getUrbanizacion() {
        return urbanizacion;
    }

    public void setUrbanizacion(String urbanizacion) {
        this.urbanizacion = urbanizacion;
    }

    @NonNull
    @Override
    public String toString() {
        return getUrbanizacion();
        //return super.toString();
    }
}

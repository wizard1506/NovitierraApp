package com.example.novitierraapp.entidades;

public class TerrenoReferidos {
    Integer id_venta_referido ;
    String urbanizacion;
    String fecha;

    public TerrenoReferidos(){

    }

    public TerrenoReferidos(Integer id_venta_referido, String urbanizacion, String fecha) {
        this.id_venta_referido = id_venta_referido;
        this.urbanizacion = urbanizacion;
        this.fecha = fecha;
    }

    public Integer getId_venta_referido() {
        return id_venta_referido;
    }

    public void setId_venta_referido(Integer id_venta_referido) {
        this.id_venta_referido = id_venta_referido;
    }

    public String getUrbanizacion() {
        return urbanizacion;
    }

    public void setUrbanizacion(String urbanizacion) {
        this.urbanizacion = urbanizacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}

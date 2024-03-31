package com.example.novitierraapp.entidades;

import java.math.BigInteger;
import java.util.Date;

public class Prospectos {
    Integer id_prospectos ;
    String nombre_completo;
    String telefono;
    String llamada;
    String zona;
    String lugar;
    String urbanizacion;
    String observacion;
    String asesor;
    Integer codigo;
    String vigencia;
    String fecha;


    public Prospectos() {
    }

    public Prospectos(Integer id_prospectos, String nombre_completo, String telefono, String llamada, String zona, String lugar, String urbanizacion, String observacion, String asesor, Integer codigo, String vigencia, String fecha) {
        this.id_prospectos = id_prospectos;
        this.nombre_completo = nombre_completo;
        this.telefono = telefono;
        this.llamada = llamada;
        this.zona = zona;
        this.lugar = lugar;
        this.urbanizacion = urbanizacion;
        this.observacion = observacion;
        this.asesor = asesor;
        this.codigo = codigo;
        this.vigencia = vigencia;
        this.fecha = fecha;
    }

    public Integer getId_prospectos() {
        return id_prospectos;
    }

    public void setId_prospectos(Integer id_prospectos) {
        this.id_prospectos = id_prospectos;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getLlamada() {
        return llamada;
    }

    public void setLlamada(String llamada) {
        this.llamada = llamada;
    }

    public String getUrbanizacion() {
        return urbanizacion;
    }

    public void setUrbanizacion(String urbanizacion) {
        this.urbanizacion = urbanizacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getAsesor() {
        return asesor;
    }

    public void setAsesor(String asesor) {
        this.asesor = asesor;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getVigencia() {
        return vigencia;
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }
}

package com.example.novitierraapp.entidades;

public class Referidos {
    Integer id_referido ;
    String nombres;
    String apellidos;
    Integer telf;

    public Referidos(){

    }

    public Referidos(Integer id_referido, String nombres, String apellidos, Integer telf) {
        this.id_referido = id_referido;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telf = telf;
    }

    public Integer getId_referido() {
        return id_referido;
    }

    public void setId_referido(Integer id_referido) {
        this.id_referido = id_referido;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Integer getTelf() {
        return telf;
    }

    public void setTelf(Integer telf) {
        this.telf = telf;
    }
}

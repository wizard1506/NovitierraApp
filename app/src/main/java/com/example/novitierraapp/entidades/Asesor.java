package com.example.novitierraapp.entidades;

public class Asesor {
    Integer id;
    String nombre;
    Integer codigo;

    public Asesor (){

    }

    public Asesor(Integer id, String nombre, Integer codigo) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String toString(){
        return nombre;
    }
}

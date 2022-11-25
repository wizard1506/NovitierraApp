package com.example.novitierraapp.entidades;

public class Proyectos2 {
    String codigo;
    String proyecto;

    public Proyectos2(){

    }

    public Proyectos2(String codigo, String proyecto) {
        this.codigo = codigo;
        this.proyecto = proyecto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }
    public String toString(){
        return proyecto;
    }
}

package com.example.novitierraapp.entidades;

import java.sql.Date;

public class usuarios {

    private Integer id_usuario;
    private String nombre;
    private String apellido;
    private String ci;
    private Integer telefono;
    private Integer codigo;
    private String usuario;
    private String password;
    //private Date fechaIngreso;

    public usuarios() {
    }

    public usuarios(Integer id_usuario, String nombre, String apellido, String ci, Integer telefono, Integer codigo, String usuario, String password) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.ci = ci;
        this.telefono = telefono;
        this.codigo = codigo;
        this.usuario = usuario;
        this.password = password;
        //this.fechaIngreso = fechaIngreso;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


//    public Date getFechaIngreso() {
//        return fechaIngreso;
//    }
//
//    public void setFechaIngreso(Date fechaIngreso) {
//        this.fechaIngreso = fechaIngreso;
//    }
}

package com.example.novitierraapp.entidades;

import java.sql.Date;

public class Titular {
    Integer id_titular;
    String nombres;
    String apellidoP;
    String apellidoM;
    String apellidoC;
    String prefijo;
    String tipo_identificacion;
    String nro_documento;
    String extension;
    String nacionalidad;
    String fecha_nacimiento;
    String estado_civil;
    String sexo;
    String nivel_estudio;
    String profesion_ocupacion;
    String telf_fijo;
    String telf_movil;
    String telf_fijoOficina;
    String telf_movilOficina;
    String correo;
    String referencia1;
    String relacion1;
    String telf_referencia1;
    String referencia2;
    String relacion2;
    String telf_referencia2;
    String tipo_vivienda;
    String tenencia;
    String costo_vivienda;
    String moneda_costoVivienda;
    String propietario_vivienda;
    String telf_propietario;
    String pais_vivienda;
    String departamento;
    String zona;
    String ciudad;
    String barrio;
    String avenida;
    String calle;
    String numero;
    String nombre_empresa;
    String direccion_empresa;
    String rubro;
    String ingresos;
    String moneda_ingresos;
    Integer proyecto;
    String urbanizacion;
    String uv;
    String mz;
    Integer lt;
    String cat;
    String metros2;
    String tipo_venta;
    String cuotas;
    String asesor;
    Integer codigo_asesor;
    String observacion;
    String observacion2;
    String latitud;
    String longitud;
    String ubicacion;

    public Titular(){

    }

    public Titular(Integer id_titular, String nombres, String apellidoP, String apellidoM, String apellidoC, String prefijo, String tipo_identificacion, String nro_documento, String extension, String nacionalidad, String fecha_nacimiento, String estado_civil, String sexo, String nivel_estudio, String profesion_ocupacion, String telf_fijo, String telf_movil, String telf_fijoOficina, String telf_movilOficina, String correo, String referencia1, String relacion1, String telf_referencia1, String referencia2, String relacion2, String telf_referencia2, String tipo_vivienda, String tenencia, String costo_vivienda, String moneda_costoVivienda, String propietario_vivienda, String telf_propietario, String pais_vivienda, String departamento, String zona, String ciudad, String barrio, String avenida, String calle, String numero, String nombre_empresa, String direccion_empresa, String rubro, String ingresos, String moneda_ingresos, Integer proyecto, String urbanizacion, String uv, String mz, Integer lt, String cat, String metros2, String tipo_venta, String cuotas, String asesor, Integer codigo_asesor, String observacion, String observacion2, String latitud, String longitud, String ubicacion) {
        this.id_titular = id_titular;
        this.nombres = nombres;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.apellidoC = apellidoC;
        this.prefijo = prefijo;
        this.tipo_identificacion = tipo_identificacion;
        this.nro_documento = nro_documento;
        this.extension = extension;
        this.nacionalidad = nacionalidad;
        this.fecha_nacimiento = fecha_nacimiento;
        this.estado_civil = estado_civil;
        this.sexo = sexo;
        this.nivel_estudio = nivel_estudio;
        this.profesion_ocupacion = profesion_ocupacion;
        this.telf_fijo = telf_fijo;
        this.telf_movil = telf_movil;
        this.telf_fijoOficina = telf_fijoOficina;
        this.telf_movilOficina = telf_movilOficina;
        this.correo = correo;
        this.referencia1 = referencia1;
        this.relacion1 = relacion1;
        this.telf_referencia1 = telf_referencia1;
        this.referencia2 = referencia2;
        this.relacion2 = relacion2;
        this.telf_referencia2 = telf_referencia2;
        this.tipo_vivienda = tipo_vivienda;
        this.tenencia = tenencia;
        this.costo_vivienda = costo_vivienda;
        this.moneda_costoVivienda = moneda_costoVivienda;
        this.propietario_vivienda = propietario_vivienda;
        this.telf_propietario = telf_propietario;
        this.pais_vivienda = pais_vivienda;
        this.departamento = departamento;
        this.zona = zona;
        this.ciudad = ciudad;
        this.barrio = barrio;
        this.avenida = avenida;
        this.calle = calle;
        this.numero = numero;
        this.nombre_empresa = nombre_empresa;
        this.direccion_empresa = direccion_empresa;
        this.rubro = rubro;
        this.ingresos = ingresos;
        this.moneda_ingresos = moneda_ingresos;
        this.proyecto = proyecto;
        this.urbanizacion = urbanizacion;
        this.uv = uv;
        this.mz = mz;
        this.lt = lt;
        this.cat = cat;
        this.metros2 = metros2;
        this.tipo_venta = tipo_venta;
        this.cuotas = cuotas;
        this.asesor = asesor;
        this.codigo_asesor = codigo_asesor;
        this.observacion = observacion;
        this.observacion2 = observacion2;
        this.latitud = latitud;
        this.longitud = longitud;
        this.ubicacion = ubicacion;
    }

    public Integer getId_titular() {
        return id_titular;
    }

    public void setId_titular(Integer id_titular) {
        this.id_titular = id_titular;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public String getApellidoC() {
        return apellidoC;
    }

    public void setApellidoC(String apellidoC) {
        this.apellidoC = apellidoC;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public String getTipo_identificacion() {
        return tipo_identificacion;
    }

    public void setTipo_identificacion(String tipo_identificacion) {
        this.tipo_identificacion = tipo_identificacion;
    }

    public String getNro_documento() {
        return nro_documento;
    }

    public void setNro_documento(String nro_documento) {
        this.nro_documento = nro_documento;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getEstado_civil() {
        return estado_civil;
    }

    public void setEstado_civil(String estado_civil) {
        this.estado_civil = estado_civil;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNivel_estudio() {
        return nivel_estudio;
    }

    public void setNivel_estudio(String nivel_estudio) {
        this.nivel_estudio = nivel_estudio;
    }

    public String getProfesion_ocupacion() {
        return profesion_ocupacion;
    }

    public void setProfesion_ocupacion(String profesion_ocupacion) {
        this.profesion_ocupacion = profesion_ocupacion;
    }

    public String getTelf_fijo() {
        return telf_fijo;
    }

    public void setTelf_fijo(String telf_fijo) {
        this.telf_fijo = telf_fijo;
    }

    public String getTelf_movil() {
        return telf_movil;
    }

    public void setTelf_movil(String telf_movil) {
        this.telf_movil = telf_movil;
    }

    public String getTelf_fijoOficina() {
        return telf_fijoOficina;
    }

    public void setTelf_fijoOficina(String telf_fijoOficina) {
        this.telf_fijoOficina = telf_fijoOficina;
    }

    public String getTelf_movilOficina() {
        return telf_movilOficina;
    }

    public void setTelf_movilOficina(String telf_movilOficina) {
        this.telf_movilOficina = telf_movilOficina;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getReferencia1() {
        return referencia1;
    }

    public void setReferencia1(String referencia1) {
        this.referencia1 = referencia1;
    }

    public String getRelacion1() {
        return relacion1;
    }

    public void setRelacion1(String relacion1) {
        this.relacion1 = relacion1;
    }

    public String getTelf_referencia1() {
        return telf_referencia1;
    }

    public void setTelf_referencia1(String telf_referencia1) {
        this.telf_referencia1 = telf_referencia1;
    }

    public String getReferencia2() {
        return referencia2;
    }

    public void setReferencia2(String referencia2) {
        this.referencia2 = referencia2;
    }

    public String getRelacion2() {
        return relacion2;
    }

    public void setRelacion2(String relacion2) {
        this.relacion2 = relacion2;
    }

    public String getTelf_referencia2() {
        return telf_referencia2;
    }

    public void setTelf_referencia2(String telf_referencia2) {
        this.telf_referencia2 = telf_referencia2;
    }

    public String getTipo_vivienda() {
        return tipo_vivienda;
    }

    public void setTipo_vivienda(String tipo_vivienda) {
        this.tipo_vivienda = tipo_vivienda;
    }

    public String getTenencia() {
        return tenencia;
    }

    public void setTenencia(String tenencia) {
        this.tenencia = tenencia;
    }

    public String getCosto_vivienda() {
        return costo_vivienda;
    }

    public void setCosto_vivienda(String costo_vivienda) {
        this.costo_vivienda = costo_vivienda;
    }

    public String getMoneda_costoVivienda() {
        return moneda_costoVivienda;
    }

    public void setMoneda_costoVivienda(String moneda_costoVivienda) {
        this.moneda_costoVivienda = moneda_costoVivienda;
    }

    public String getPropietario_vivienda() {
        return propietario_vivienda;
    }

    public void setPropietario_vivienda(String propietario_vivienda) {
        this.propietario_vivienda = propietario_vivienda;
    }

    public String getTelf_propietario() {
        return telf_propietario;
    }

    public void setTelf_propietario(String telf_propietario) {
        this.telf_propietario = telf_propietario;
    }

    public String getPais_vivienda() {
        return pais_vivienda;
    }

    public void setPais_vivienda(String pais_vivienda) {
        this.pais_vivienda = pais_vivienda;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getAvenida() {
        return avenida;
    }

    public void setAvenida(String avenida) {
        this.avenida = avenida;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombre_empresa() {
        return nombre_empresa;
    }

    public void setNombre_empresa(String nombre_empresa) {
        this.nombre_empresa = nombre_empresa;
    }

    public String getDireccion_empresa() {
        return direccion_empresa;
    }

    public void setDireccion_empresa(String direccion_empresa) {
        this.direccion_empresa = direccion_empresa;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getIngresos() {
        return ingresos;
    }

    public void setIngresos(String ingresos) {
        this.ingresos = ingresos;
    }

    public String getMoneda_ingresos() {
        return moneda_ingresos;
    }

    public void setMoneda_ingresos(String moneda_ingresos) {
        this.moneda_ingresos = moneda_ingresos;
    }

    public Integer getProyecto() {
        return proyecto;
    }

    public void setProyecto(Integer proyecto) {
        this.proyecto = proyecto;
    }

    public String getUrbanizacion() {
        return urbanizacion;
    }

    public void setUrbanizacion(String urbanizacion) {
        this.urbanizacion = urbanizacion;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public Integer getLt() {
        return lt;
    }

    public void setLt(Integer lt) {
        this.lt = lt;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getMetros2() {
        return metros2;
    }

    public void setMetros2(String metros2) {
        this.metros2 = metros2;
    }

    public String getTipo_venta() {
        return tipo_venta;
    }

    public void setTipo_venta(String tipo_venta) {
        this.tipo_venta = tipo_venta;
    }

    public String getCuotas() {
        return cuotas;
    }

    public void setCuotas(String cuotas) {
        this.cuotas = cuotas;
    }

    public String getAsesor() {
        return asesor;
    }

    public void setAsesor(String asesor) {
        this.asesor = asesor;
    }

    public Integer getCodigo_asesor() {
        return codigo_asesor;
    }

    public void setCodigo_asesor(Integer codigo_asesor) {
        this.codigo_asesor = codigo_asesor;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getObservacion2() {
        return observacion2;
    }

    public void setObservacion2(String observacion2) {
        this.observacion2 = observacion2;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}

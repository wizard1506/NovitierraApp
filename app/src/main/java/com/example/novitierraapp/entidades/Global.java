package com.example.novitierraapp.entidades;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

public class Global {
    public static String useridSesion = "";
    public static String userSesion = "";
    public static String upass = "";
    public static String nombreSesion = "";
    public static String apellidoSesion = "";
    public static String ciSesion ="";
    public static String telefonoSesion = "";
    public static String grupo = "";
    public static Integer codigo = 0;
    public static String nombreReferidor = "";
    public static String apellidoReferidor = "";
    public static String telfReferidor = "";
    public static String ciReferidor = "";
    public static String passReferidor = "";
    public static String asesorReferidor = "";
    public static String idReferidor = "";



    ///otros datos
    public static Double gLat=0.0;
    public  static  Double gLong=0.0;
    public static Bitmap ubicacion;

    public static String getUseridSesion() {
        return useridSesion;
    }

    public static void setUseridSesion(String useridSesion) {
        Global.useridSesion = useridSesion;
    }

    public static String getUserSesion() {
        return userSesion;
    }

    public static void setUserSesion(String userSesion) {
        Global.userSesion = userSesion;
    }

    public static String getUpass() {
        return upass;
    }

    public static void setUpass(String upass) {
        Global.upass = upass;
    }

    public static String getNombreSesion() {
        return nombreSesion;
    }

    public static void setNombreSesion(String nombreSesion) {
        Global.nombreSesion = nombreSesion;
    }

    public static String getApellidoSesion() {
        return apellidoSesion;
    }

    public static void setApellidoSesion(String apellidoSesion) {
        Global.apellidoSesion = apellidoSesion;
    }

    public static String getCiSesion() {
        return ciSesion;
    }

    public static void setCiSesion(String ciSesion) {
        Global.ciSesion = ciSesion;
    }

    public static String getTelefonoSesion() {
        return telefonoSesion;
    }

    public static void setTelefonoSesion(String telefonoSesion) {
        Global.telefonoSesion = telefonoSesion;
    }

    public static String getGrupo() {
        return grupo;
    }

    public static void setGrupo(String grupo) {
        Global.grupo = grupo;
    }

    public static Integer getCodigo() {
        return codigo;
    }

    public static void setCodigo(Integer codigo) {
        Global.codigo = codigo;
    }

    public static String getNombreReferidor() {
        return nombreReferidor;
    }

    public static void setNombreReferidor(String nombreReferidor) {
        Global.nombreReferidor = nombreReferidor;
    }

    public static String getApellidoReferidor() {
        return apellidoReferidor;
    }

    public static void setApellidoReferidor(String apellidoReferidor) {
        Global.apellidoReferidor = apellidoReferidor;
    }

    public static String getTelfReferidor() {
        return telfReferidor;
    }

    public static void setTelfReferidor(String telfReferidor) {
        Global.telfReferidor = telfReferidor;
    }

    public static String getCiReferidor() {
        return ciReferidor;
    }

    public static void setCiReferidor(String ciReferidor) {
        Global.ciReferidor = ciReferidor;
    }

    public static String getPassReferidor() {
        return passReferidor;
    }

    public static void setPassReferidor(String passReferidor) {
        Global.passReferidor = passReferidor;
    }

    public static String getAsesorReferidor() {
        return asesorReferidor;
    }

    public static void setAsesorReferidor(String asesorReferidor) {
        Global.asesorReferidor = asesorReferidor;
    }

    public static String getIdReferidor() {
        return idReferidor;
    }

    public static void setIdReferidor(String idReferidor) {
        Global.idReferidor = idReferidor;
    }

    public static Double getgLat() {
        return gLat;
    }

    public static void setgLat(Double gLat) {
        Global.gLat = gLat;
    }

    public static Double getgLong() {
        return gLong;
    }

    public static void setgLong(Double gLong) {
        Global.gLong = gLong;
    }

    public static Bitmap getUbicacion() {
        return ubicacion;
    }

    public static void setUbicacion(Bitmap ubicacion) {
        Global.ubicacion = ubicacion;
    }
    public static boolean verificarDatosSesion(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("sesion", Context.MODE_PRIVATE);

        // Verificar si los datos requeridos están presentes en SharedPreferences
        return preferences.contains("userID") &&
                preferences.contains("nombre") &&
                preferences.contains("apellido") &&
                preferences.contains("ci") &&
                preferences.contains("telefono") &&
                preferences.contains("codigo") &&
                preferences.contains("grupo") &&
                preferences.contains("usuario") &&
                preferences.contains("upassword");
    }

}

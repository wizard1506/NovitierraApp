package com.example.novitierraapp;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.novitierraapp.entidades.Proyectos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class metodos {

    public ArrayList<String> cargarUrbanizacion(ArrayList<String> lista){
        lista.add("LA ENCONADA II");
        lista.add("LA PASCANA DE COTOCA");
        lista.add("LA PASCANA DE COTOCA II");
        lista.add("LA PASCANA DE COTOCA III");
        lista.add("LA PASCANA DE COTOCA IV");
        lista.add("LA PASCANA DE COTOCA V");
        lista.add("LA PASCANA DE COTOCA VI");
        lista.add("LA PASCANA DE COTOCA VII");
        lista.add("LA TIERRA PROMETIDA");
        lista.add("AME TAUNA");
        lista.add("AME TAUNA I");

        return lista;
    }
    public ArrayList<String> cargarUrbanizacionProspectos(ArrayList<String> lista){
        lista.add("NINGUNO");
        lista.add("LA ENCONADA II");
        lista.add("LA PASCANA DE COTOCA");
        lista.add("LA PASCANA DE COTOCA II");
        lista.add("LA PASCANA DE COTOCA III");
        lista.add("LA PASCANA DE COTOCA IV");
        lista.add("LA PASCANA DE COTOCA V");
        lista.add("LA PASCANA DE COTOCA VI");
        lista.add("LA PASCANA DE COTOCA VII");
        lista.add("LA TIERRA PROMETIDA");
        lista.add("AME TAUNA");
        lista.add("AME TAUNA I");

        return lista;
    }
    public ArrayList<String>cargarLlamada(ArrayList<String> lista){
        lista.add("No");
        lista.add("Si");
        return lista;
    }
    public ArrayList<String>cargarExtensiones(ArrayList<String> lista){
        lista.add("--");
        lista.add("SC");
        lista.add("LP");
        lista.add("CB");
        lista.add("PO");
        lista.add("CH");
        lista.add("TJ");
        lista.add("OR");
        lista.add("BE");
        lista.add("PD");
        return lista;
    }
    public ArrayList<String>cargarMoneda(ArrayList<String> lista){
        lista.add("Ninguno");
        lista.add("Bs");
        lista.add("$us");
        return lista;
    }
    public ArrayList<String>cargarListaPlazo(ArrayList<String> lista){
        lista.add("--");
        lista.add("12");
        lista.add("24");
        lista.add("36");
        lista.add("48");
        lista.add("60");
        lista.add("72");
        return lista;
    }
    public ArrayList<String>cargarTipoIdentificacion(ArrayList<String> lista){
        lista.add("Carnet de Identidad");
        lista.add("Pasaporte");
        lista.add("Carnet Extranjero");
        return lista;
    }
    public ArrayList<String>cargarEstadoCivil(ArrayList<String> lista){
        lista.add("Soltero(a)");
        lista.add("Casado(a)");
        lista.add("Divorciado(a)");
        lista.add("Viudo(a)");
        return lista;
    }
    public ArrayList<String>cargarNivelEstudio(ArrayList<String> lista){
        lista.add("Primaria");
        lista.add("Secundaria");
        lista.add("Tecnico Medio");
        lista.add("Tecnico Superior");
        lista.add("Licenciatura");
        lista.add("Ninguno");
        return lista;
    }
    public ArrayList<String>cargarTipoVivienda(ArrayList<String> lista){
        lista.add("Dpto");
        lista.add("Casa");
        lista.add("Cuarto");
        lista.add("Terreno");
        lista.add("Otro");
        return lista;
    }
    public ArrayList<String>cargarDpto(ArrayList<String> lista){
        lista.add("Santa Cruz");
        lista.add("La Paz");
        lista.add("Cochabamba");
        lista.add("Beni");
        lista.add("Pando");
        lista.add("Oruro");
        lista.add("Potosi");
        lista.add("Tarija");
        lista.add("Chuquisaca");
        lista.add("Ninguno");
        return lista;
    }
    public ArrayList<String>cargarTenencia(ArrayList<String> lista){
        lista.add("Propia");
        lista.add("Alquiler");
        lista.add("Anticretico");
        lista.add("Otro");
        return lista;
    }
    public ArrayList<String>cargarPrefijo(ArrayList<String> lista){
        lista.add("Ninguno");
        lista.add("VDA DE");
        lista.add("DE");
        return lista;
    }


}

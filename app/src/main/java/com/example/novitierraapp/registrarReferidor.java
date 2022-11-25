package com.example.novitierraapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.novitierraapp.entidades.Asesor;
import com.example.novitierraapp.entidades.Global;
import com.example.novitierraapp.entidades.Referidos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.AdapterReferidos;

public class registrarReferidor extends AppCompatActivity {

    EditText nombreref, apellidoref,ci,telfref,passref1,passref2;
    Spinner spinnerAsesor;
    Integer codigoasesor;
    Button btregistrarRef;
    ArrayList<Asesor> listAsesor = new ArrayList<>();
    private String urlAddReferidor="http://wizardapps.xyz/novitierra/api/addReferidor.php";
    private String URL_ListaAsesor="http://wizardapps.xyz/novitierra/api/listaReferidor.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_referidor);
        nombreref = findViewById(R.id.nombreReferidor);
        apellidoref = findViewById(R.id.apellidoReferidor);
        ci = findViewById(R.id.ciReferidor);
        telfref = findViewById(R.id.telefonoReferidor);
        passref1 = findViewById(R.id.passreferidor1);
        passref2 = findViewById(R.id.passreferidor2);
        btregistrarRef = findViewById(R.id.btRegistrarReferidor);
        spinnerAsesor = findViewById(R.id.spinnerAsesor);

        llenarSpinner();

        spinnerAsesor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codigoasesor=listAsesor.get(position).getCodigo();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btregistrarRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarref();
            }
        });
    }

    private void llenarSpinner(){
        RequestQueue requestQueue;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ListaAsesor, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject respuesta = array.getJSONObject(i);
                            Asesor asesor = new Asesor();
                            asesor.setNombre(respuesta.getString("nombre"));
                            asesor.setCodigo(respuesta.getInt("codigo"));
                            listAsesor.add(asesor);
                        }
                        ArrayAdapter<Asesor> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line,listAsesor);
                        spinnerAsesor.setAdapter(adapter);
                    }catch(JSONException e) {
                        Toast.makeText(getApplicationContext(), "No se cargaron los referidores o no existen aun", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Ocurrio algun error", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
//                parametros.put("id", Global.idReferidor);
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void registrarref() {
        String pass1,pass2;
        pass1 = passref1.getText().toString();
        pass2=passref2.getText().toString();
        if (!validarCampos()){
            if (pass1.equals(pass2)){
                StringRequest request = new StringRequest(Request.Method.POST, urlAddReferidor, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Referidor registrado correctamente",Toast.LENGTH_LONG).show();
                            limpiarEdits();
                            Intent intent = new Intent(getApplicationContext(),loginReferidor.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "Error: CI ya fue registrado antes", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parametros = new HashMap<String, String>();
                        parametros.put("nombres",nombreref.getText().toString().toUpperCase());
                        parametros.put("apellidos",apellidoref.getText().toString().toUpperCase());
                        parametros.put("ci",ci.getText().toString().toUpperCase());
                        parametros.put("telefono",telfref.getText().toString());
                        parametros.put("asesor",spinnerAsesor.getSelectedItem().toString());
                        parametros.put("codigo",codigoasesor.toString());
                        parametros.put("pass",passref1.getText().toString());
                        return parametros;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(request);
            }else {
                Toast.makeText(getApplicationContext(),"Contraseñas no coinciden",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Llenar todo los campos.",Toast.LENGTH_SHORT).show();
        }
    }
    public Boolean validarCampos(){
        Boolean result = true;
        if(nombreref.getText().toString().equals("") || apellidoref.getText().toString().equals("") || ci.getText().toString().equals("")|| telfref.getText().toString().equals("")
                || passref1.getText().toString().equals("")|| passref2.getText().toString().equals("")){
            return result;
        }else {
            result=false;
            return result;
        }

    }
    private void limpiarEdits() {
        nombreref.setText("");
        apellidoref.setText("");
        ci.setText("");
        telfref.setText("");
        passref1.setText("");
        passref2.setText("");
    }
}
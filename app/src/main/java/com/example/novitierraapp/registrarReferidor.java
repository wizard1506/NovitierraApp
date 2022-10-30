package com.example.novitierraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

public class registrarReferidor extends AppCompatActivity {

    EditText nombreref, apellidoref,ci,telfref,passref1,passref2;
    Spinner spinnerAsesor;
    Button btregistrarRef;
    private String urlAddReferidor="http://wizardapps.xyz/novitierra/api/addReferidor.php";

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

        btregistrarRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarref();
            }
        });
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
                            Toast.makeText(getApplicationContext(), "Se ha producido un error de sistema o el CI ya fue registrado antes", Toast.LENGTH_LONG).show();
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
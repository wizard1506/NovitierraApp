package com.example.novitierraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegistrarseLogin extends AppCompatActivity {

    EditText nombres,apellidos,ci,telf,codigo,usuario,password,passwordRepeat,grupo;
    Button registrarUsuario;
    private String url="http://wizardapps.xyz/novitierra/api/addUser.php";
//    private String url="https://novitierra.000webhostapp.com/api/addUser.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse_login);

        nombres = findViewById(R.id.nombreRegistro);
        apellidos=findViewById(R.id.apellidoRegistro);
        ci=findViewById(R.id.ciRegistro);
        telf=findViewById(R.id.telefonoRegistro);
        codigo=findViewById(R.id.codigoRegistro);
        grupo=findViewById(R.id.grupo);
        usuario=findViewById(R.id.usuarioRegistro);
        password=findViewById(R.id.passwordRegistro);
        passwordRepeat=findViewById(R.id.passwordRepeatRegistro);
        registrarUsuario=findViewById(R.id.btRegistrarRegistro);

        Toast.makeText(getApplicationContext(),"Llenar todo los campos para proceder al registro",Toast.LENGTH_LONG).show();

        registrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!RegistrosVacios()){
                    registrar();
                }else {
                    Toast.makeText(getApplicationContext(),"LLenar todo los campos para proceder a registrar",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public Boolean RegistrosVacios(){
        Boolean result = true;
        if(nombres.getText().toString().equals("") || apellidos.getText().toString().equals("") || ci.getText().toString().equals("")|| telf.getText().toString().equals("")
        || codigo.getText().toString().equals("")||usuario.getText().toString().equals("")|| password.getText().toString().equals("")|| passwordRepeat.getText().toString().equals("")){
            return result;
        }else {
            result=false;
            return result;
        }

    }

    private void registrar() {

            String pass1,pass2;
            pass1 = password.getText().toString();
            pass2=passwordRepeat.getText().toString();
            if (pass1.equals(pass2)){
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Usuario registrado correctamente",Toast.LENGTH_LONG).show();
                            limpiarEdits();
                        }else{
                            Toast.makeText(RegistrarseLogin.this, "Se ha producido un error", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegistrarseLogin.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parametros = new HashMap<String, String>();
                        parametros.put("nombres",nombres.getText().toString());
                        parametros.put("apellidos",apellidos.getText().toString());
                        parametros.put("ci",ci.getText().toString());
                        parametros.put("telefono",telf.getText().toString());
                        parametros.put("codigo",codigo.getText().toString());
                        parametros.put("grupo",grupo.getText().toString());
                        parametros.put("usuario",usuario.getText().toString());
                        parametros.put("password",password.getText().toString());
                        return parametros;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(request);
            }else {
                Toast.makeText(getApplicationContext(),"Contraseñas no coinciden",Toast.LENGTH_SHORT).show();
            }


            }

    private void limpiarEdits() {
        nombres.setText("");
        apellidos.setText("");
        ci.setText("");
        telf.setText("");
        codigo.setText("");
        grupo.setText("");
        usuario.setText("");
        password.setText("");
        passwordRepeat.setText("");
    }
}
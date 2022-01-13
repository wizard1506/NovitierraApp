package com.example.novitierraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.novitierraapp.entidades.Global;
import com.example.novitierraapp.entidades.usuarios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView tvRegistro;
    EditText userLogin,passwordLogin;
    Button login;
    List<usuarios> userList;
    RequestQueue requestQueue;
    String usu,usu2;
    String pass,pass2;
    private static final String URL = "http://wizardapps.xyz/novitierra/validar_usuario.php" ;
    private static final String URL2 = "http://wizardapps.xyz/novitierra/userLogged.php" ;
//    private static final String URL = "https://novitierra.000webhostapp.com/validar_usuario.php" ;
//    private static final String URL2 = "https://novitierra.000webhostapp.com/userLogged.php" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userList = new ArrayList<>();
        setContentView(R.layout.activity_main);
        userLogin=findViewById(R.id.userLogin);
        passwordLogin=findViewById(R.id.passwordLogin);
        login = findViewById(R.id.btlogin);
        tvRegistro=findViewById(R.id.tvRegistrarme);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (campoVacio()!=true){
                    traerUsuario();
                }else {
                    Toast.makeText(MainActivity.this, "Ingrese sus datos correctamente.", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegistrarseLogin.class);
                startActivity(intent);

            }
        });
    }


    public boolean campoVacio(){
        if (userLogin.getText().toString().isEmpty()|| passwordLogin.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }

    private void validarUsuario(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(),navMenu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Bienvenido(a)"+" "+Global.nombreSesion.toString(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Usuario o Contraseña incorrecta", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"No se pudo conectar al servidor", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("usuario",userLogin.getText().toString());
                parametros.put("password",passwordLogin.getText().toString());

                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void traerUsuario(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject respuesta = array.getJSONObject(i);
                            Global.nombreSesion=respuesta.getString("nombres");
                            Global.apellidoSesion=respuesta.getString("apellidos");
                            Global.userSesion=respuesta.getString("usuario");
                            Global.codigo=respuesta.getInt("codigo");
                            usu=respuesta.getString("usuario");
                            pass=respuesta.getString("upassword");
                            usu2=userLogin.getText().toString();
                            pass2=passwordLogin.getText().toString();
                            if(usu2.equals(usu)&&pass2.equals(pass)){
                                validarUsuario(URL);
                            }else {
                                Toast.makeText(MainActivity.this, "Usuario o Contraseña incorrecta", Toast.LENGTH_LONG).show();
                            }
                        }
                    }catch(JSONException e) {
                        Toast.makeText(MainActivity.this, "Usuario no registrado", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Ocurrio algun error", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("usuario",userLogin.getText().toString());
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    }


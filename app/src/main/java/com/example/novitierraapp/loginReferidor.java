package com.example.novitierraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class loginReferidor extends AppCompatActivity {

    EditText ciReferidor, passwordReferidor;
    TextView tvRegistroReferidor;
    Button btLoginReferidor;
    String ci,pass,ci2,pass2;
    RequestQueue requestQueue;

    private static final String URL_login_referidor = "http://wizardapps.xyz/novitierra/userReferidor.php" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_referidor);

        ciReferidor = findViewById(R.id.ciReferidor);
        passwordReferidor = findViewById(R.id.passReferidor);
        btLoginReferidor = findViewById(R.id.btloginReferidor);
        tvRegistroReferidor = findViewById(R.id.tvRegistroReferidor);

        tvRegistroReferidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),registrarReferidor.class);
                startActivity(intent);
            }
        });
        btLoginReferidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (campoVacio()!=true){
                    entrar();
                }else {
                    Toast.makeText(getApplicationContext(), "Ingrese sus datos correctamente.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public boolean campoVacio(){
        if (ciReferidor.getText().toString().isEmpty()|| passwordReferidor.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }
    public void entrar(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_login_referidor, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject respuesta = array.getJSONObject(i);
//                            Global.id_user=respuesta.getInt("id_usuario");
                            Global.idReferidor = respuesta.getString("id_referidor");
                            Global.nombreReferidor=respuesta.getString("nombres");
                            Global.apellidoReferidor=respuesta.getString("apellidos");
                            Global.ciReferidor=respuesta.getString("ci");
                            Global.telfReferidor = respuesta.getString("telefono");
                            Global.asesorReferidor = respuesta.getString("asesor");
                            Global.passReferidor=respuesta.getString("password");
                            ci=respuesta.getString("ci");
                            pass=respuesta.getString("password");
                            ci2=ciReferidor.getText().toString();
                            pass2=passwordReferidor.getText().toString();
                            if(ci2.equals(ci)&&pass2.equals(pass)){
                                Intent intent = new Intent(getApplicationContext(),userReferidor.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Bienvenido(a)"+" "+Global.nombreReferidor.toString(), Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrecta", Toast.LENGTH_LONG).show();
                            }
                        }
                    }catch(JSONException e) {
                        Toast.makeText(getApplicationContext(), "Referidor no registrado", Toast.LENGTH_LONG).show();
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("ci",ciReferidor.getText().toString());
//                parametros.put("pass",passwordReferidor.getText().toString());
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
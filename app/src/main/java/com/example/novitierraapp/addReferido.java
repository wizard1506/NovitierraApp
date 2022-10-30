package com.example.novitierraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.example.novitierraapp.entidades.Global;

import java.util.HashMap;
import java.util.Map;

public class addReferido extends AppCompatActivity {
    EditText nombresReferido, apellidosReferido,telefonoReferido;
    Button btregistrarReferido;
    private String urlAddReferido="http://wizardapps.xyz/novitierra/api/addReferido.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_referido);
        nombresReferido =  findViewById(R.id.referidoNombres);
        apellidosReferido = findViewById(R.id.referidoApellidos);
        telefonoReferido = findViewById(R.id.referidoTelefono);
        btregistrarReferido = findViewById(R.id.referidoAdd);

        btregistrarReferido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarReferido();
            }
        });

    }
    public void registrarReferido(){
            StringRequest request = new StringRequest(Request.Method.POST, urlAddReferido, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (!response.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Referido registrado correctamente",Toast.LENGTH_LONG).show();
                        limpiarEdits();
//                        Intent intent = new Intent(getApplicationContext(),loginReferidor.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Se ha producido un error o Telefono ya registrado", Toast.LENGTH_LONG).show();
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
                    parametros.put("idreferidor", Global.idReferidor);
                    parametros.put("nombres",nombresReferido.getText().toString().toUpperCase());
                    parametros.put("apellidos",apellidosReferido.getText().toString().toUpperCase());
                    parametros.put("telefono",telefonoReferido.getText().toString());
                    return parametros;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(request);

    }
    private void limpiarEdits() {
        nombresReferido.setText("");
        apellidosReferido.setText("");
        telefonoReferido.setText("");
    }
}
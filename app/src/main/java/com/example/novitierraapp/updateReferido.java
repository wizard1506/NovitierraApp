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

import java.util.HashMap;
import java.util.Map;

public class updateReferido extends AppCompatActivity {

    EditText nombre, apellido;
    Button btupdateReferido;
    Integer id = 0 ;
    private static  String URL_updateReferido="http://wizardapps.xyz/novitierra/api/updateReferido.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_referido);
        nombre = findViewById(R.id.updateReferidoNombres);
        apellido = findViewById(R.id.updateReferidoApellidos);
        btupdateReferido = findViewById(R.id.referidoUpdate);
        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
            id= getIntent().getExtras().getInt("id");
            nombre.setText(getIntent().getExtras().getString("nombre"));
            apellido.setText(getIntent().getExtras().getString("apellido"));
        }
        btupdateReferido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReferido(v);
                Intent intent = new Intent(v.getContext(), misReferidos.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void updateReferido(View v){
        StringRequest request = new StringRequest(Request.Method.POST, URL_updateReferido, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    if(response.contains("algo salio mal")){
                        Toast.makeText(v.getContext(),"No se pudo modificar el registro debido a un error",Toast.LENGTH_LONG).show();
                    }
                    else{Toast.makeText(v.getContext(),"Referido modificado",Toast.LENGTH_LONG).show();}

                }else{
                    Toast.makeText(v.getContext(), "No se ha podido modificar el referido", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(v.getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("id",(id.toString()));
                parametros.put("nombre",(nombre.getText().toString()));
                parametros.put("apellido",(apellido.getText().toString()));
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(request);
//        Navigation.findNavController(v).navigate(R.id.prospectosHoy);

    }

}
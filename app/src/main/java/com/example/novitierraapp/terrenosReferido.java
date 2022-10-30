package com.example.novitierraapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
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
import com.example.novitierraapp.entidades.Referidos;
import com.example.novitierraapp.entidades.TerrenoReferidos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.AdapterReferidos;
import Adapters.AdapterTerrenoReferidos;

public class terrenosReferido extends AppCompatActivity {
    ArrayList<TerrenoReferidos> listTerrenoReferidos = new ArrayList<>();
    RecyclerView recycler ;
    Integer id_referido = 0;
    String nombre="nombre",apellido="apellido";
    TextView tvnombrecompleto;
    private  static String URL_Terrenoreferidos="http://wizardapps.xyz/novitierra/api/cargarTerrenosReferido.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terrenos_referido);
        recycler = findViewById(R.id.recyclerTerrenosReferido);
        tvnombrecompleto = findViewById(R.id.tvnombreReferido);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
            id_referido= getIntent().getExtras().getInt("id");
            nombre = getIntent().getExtras().getString("nombre");
            apellido = getIntent().getExtras().getString("apellido");
            tvnombrecompleto.setText(nombre+" "+apellido);
        }

        cargarTerrenos();
    }

    public void cargarTerrenos() {
        RequestQueue requestQueue;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Terrenoreferidos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject respuesta = array.getJSONObject(i);
                            listTerrenoReferidos.add(new TerrenoReferidos
                                    (respuesta.getInt("id_venta_referido")
                                            ,respuesta.getString("urbanizacion")
                                            ,respuesta.getString("fecha")));
                        }
                        AdapterTerrenoReferidos adapterTerrenoReferidos = new AdapterTerrenoReferidos(listTerrenoReferidos);
                        recycler.setAdapter(adapterTerrenoReferidos);
                    }catch(JSONException e) {
                        Toast.makeText(getApplicationContext(), "No se cargaron los datos o no existen aun", Toast.LENGTH_LONG).show();
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
                parametros.put("id", String.valueOf(id_referido));
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}
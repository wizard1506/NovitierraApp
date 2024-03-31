package com.example.novitierraapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.novitierraapp.entidades.Prospectos;
import com.example.novitierraapp.entidades.Referidos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.AdapterProspectos;
import Adapters.AdapterReferidos;

public class misReferidos extends AppCompatActivity {
    ArrayList<Referidos> listReferidos = new ArrayList<>();
    RecyclerView recycler ;
    EditText etbuscarReferidos;
    AdapterReferidos madapter;

    private  static String URL_referidos="http://wizardapps.xyz/novitierra/api/cargarReferidos.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_referidos);
        etbuscarReferidos = findViewById(R.id.etbuscarReferidos);
        etbuscarReferidos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        recycler = findViewById(R.id.recyclerReferidos);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        cargarReferidos();

    }

    private void filter(String text){
        ArrayList<Referidos> filteredListReferidos = new ArrayList<>();

        for (Referidos item: listReferidos ){
            if (item.getNombres().toLowerCase().contains(text.toLowerCase()) || item.getApellidos().toLowerCase().contains(text.toLowerCase()) || item.getTelf().toString().toLowerCase().contains(text.toLowerCase()) ){
                filteredListReferidos.add(item);
            }
        }
        madapter.filterListReferidos(filteredListReferidos);
        recycler.setAdapter(madapter);
    }

    public void cargarReferidos(){
        RequestQueue requestQueue;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_referidos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject respuesta = array.getJSONObject(i);
                            listReferidos.add(new Referidos
                                            (respuesta.getInt("id_referido")
                                            ,respuesta.getString("nombres")
                                            ,respuesta.getString("apellidos")
                                            ,respuesta.getInt("telf")));
                        }
                        AdapterReferidos adapterReferidos = new AdapterReferidos(listReferidos);
                        madapter=new AdapterReferidos(listReferidos); ///importante
                        recycler.setAdapter(adapterReferidos);
                    }catch(JSONException e) {
                        Toast.makeText(getApplicationContext(), "No se cargaron los referidos o no existen aun", Toast.LENGTH_LONG).show();
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
                parametros.put("id", Global.idReferidor);
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
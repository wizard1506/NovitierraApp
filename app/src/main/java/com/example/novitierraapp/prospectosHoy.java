package com.example.novitierraapp;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.service.autofill.DateTransformation;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.novitierraapp.entidades.Prospectos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Adapters.AdapterProspectos;

public class prospectosHoy extends Fragment {

    ArrayList<Prospectos> listProspectos = new ArrayList<>();

    TextView fechaHoy;
    EditText etbuscarProspecto;
    RecyclerView recycler;
    AdapterProspectos madapter;
    private  static String URL_prospectosHoy="http://wizardapps.xyz/novitierra/api/cargarProspectoFechaHoy.php";


    private ProspectosHoyViewModel mViewModel;

    public static prospectosHoy newInstance() {
        return new prospectosHoy();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prospectos_hoy_fragment, container, false);
        recycler = view.findViewById(R.id.recyclerProspectosHoyID);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        madapter=new AdapterProspectos(listProspectos);
        recycler.setAdapter(madapter);
        fechaHoy = view.findViewById(R.id.prospectoFechaHoy);
        fechaHoy.setText(LocalDate.now().toString());
        cargarProspectosHoy();
        etbuscarProspecto = view.findViewById(R.id.etbuscarProspecto);
        etbuscarProspecto.addTextChangedListener(new TextWatcher() {
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

        return view;
    }

    private void filter(String text){

        ArrayList<Prospectos> filteredList = new ArrayList<>();
        for (Prospectos item: listProspectos ){
            if (item.getId_prospectos().toString().contains(text.toLowerCase()) || item.getNombre_completo().toLowerCase().contains(text.toLowerCase()) || item.getTelefono().toString().contains(text.toLowerCase()) ){
                filteredList.add(item);
            }
        }
        madapter.filterList(filteredList);
        recycler.setAdapter(madapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProspectosHoyViewModel.class);
        // TODO: Use the ViewModel
    }


    public void cargarProspectosHoy(){
        RequestQueue requestQueue;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_prospectosHoy, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject respuesta = array.getJSONObject(i);
                            listProspectos.add(new Prospectos
                                    (respuesta.getInt("id_prospecto")
                                            ,respuesta.getString("nombre_completo")
                                            ,respuesta.getString("telefono")
                                            ,respuesta.getString("llamada")
                                            ,respuesta.getString("zona")
                                            ,respuesta.getString("lugar")
                                            ,respuesta.getString("urbanizacion")
                                            ,respuesta.getString("observacion")
                                            ,respuesta.getString("asesor")
                                            ,respuesta.getInt("codigo")
                                            ,respuesta.getString("vigencia")
                                            ,respuesta.getString("fecha")));
                        }
                        AdapterProspectos adapterprospectos = new AdapterProspectos(listProspectos);
                        madapter=new AdapterProspectos(listProspectos); ///importante
                        recycler.setAdapter(adapterprospectos);
                    }catch(JSONException e) {
                        Toast.makeText(getContext(), "No se cargaron los prospectos o no existen de esta fecha", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getContext(), "Ocurrio algun error", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("fecha",fechaHoy.getText().toString());
                parametros.put("codigo",Global.codigo.toString());
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
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
import com.example.novitierraapp.entidades.Titular;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.AdapterProspectos;
import Adapters.AdapterTitulares;

public class titulares extends Fragment {
   ArrayList<Titular> listTitulares = new ArrayList<>();
   RecyclerView recycler;
   TextView fechaHoy;
   EditText etbuscarTitular;
    AdapterTitulares madapter;
    private  static String urltitular="http://wizardapps.xyz/novitierra/api/cargarTitularFechaHoy.php";


    private TitularesViewModel mViewModel;

    public static titulares newInstance() {
        return new titulares();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.titulares_fragment, container, false);
        fechaHoy = view.findViewById(R.id.fechatitularHoy);
        fechaHoy.setText(LocalDate.now().toString());
        etbuscarTitular = view.findViewById(R.id.etbuscarTitular);
        etbuscarTitular.addTextChangedListener(new TextWatcher() {
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
        recycler = view.findViewById(R.id.recyclerTitular);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        cargarTitularesHoy();
        return view;
    }

    private void filter(String text){
        ArrayList<Titular> filteredListTitular = new ArrayList<>();

        for (Titular item: listTitulares ){
            if (item.getId_titular().toString().contains(text.toLowerCase()) || item.getNombres().toLowerCase().contains(text.toLowerCase()) || item.getNro_documento().contains(text.toLowerCase()) || item.getApellidoP().toLowerCase().contains(text.toLowerCase()) || item.getApellidoM().toLowerCase().contains(text.toLowerCase()) ){
                filteredListTitular.add(item);
            }
        }
        madapter.filterListTitular(filteredListTitular);
        recycler.setAdapter(madapter);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TitularesViewModel.class);
        // TODO: Use the ViewModel
    }

    private void cargarTitularesHoy() {
        RequestQueue requestQueue;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urltitular, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject respuesta = array.getJSONObject(i);
                            listTitulares.add(new Titular
                                             (respuesta.getInt("id_titular")
                                            ,respuesta.getString("nombres")
                                            ,respuesta.getString("apellidoP")
                                            ,respuesta.getString("apellidoM")
                                            ,respuesta.getString("apellidoC")
                                            ,respuesta.getString("prefijo")
                                            ,respuesta.getString("tipo_identificacion")
                                            ,respuesta.getString("nro_documento")
                                            ,respuesta.getString("extension")
                                                     ,respuesta.getString("nacionalidad")
                                                     ,respuesta.getString("fecha_nacimiento")
                                                     ,respuesta.getString("estado_civil")
                                                     ,respuesta.getString("sexo")
                                                     ,respuesta.getString("nivel_estudio")
                                                     ,respuesta.getString("profesion_ocupacion")
                                                     ,respuesta.getString("telf_fijo")
                                                     ,respuesta.getString("telf_movil")
                                                     ,respuesta.getString("telf_fijoOficina")
                                                     ,respuesta.getString("telf_movilOficina")
                                                     ,respuesta.getString("correo")
                                                     ,respuesta.getString("referencia1")
                                                     ,respuesta.getString("relacion1")
                                                     ,respuesta.getString("telf_referencia1")
                                                     ,respuesta.getString("referencia2")
                                                     ,respuesta.getString("relacion2")
                                                     ,respuesta.getString("telf_referencia2")
                                                     ,respuesta.getString("tipo_vivienda")
                                                     ,respuesta.getString("tenencia")
                                                     ,respuesta.getString("costo_vivienda")
                                                     ,respuesta.getString("moneda_costoVivienda")
                                                     ,respuesta.getString("propietario_vivienda")
                                                     ,respuesta.getString("telf_propietario")
                                                     ,respuesta.getString("pais_vivienda")
                                                     ,respuesta.getString("departamento")
                                                     ,respuesta.getString("zona")
                                                     ,respuesta.getString("ciudad")
                                                     ,respuesta.getString("barrio")
                                                     ,respuesta.getString("avenida")
                                                     ,respuesta.getString("calle")
                                                     ,respuesta.getString("numero")
                                                     ,respuesta.getString("nombre_empresa")
                                                     ,respuesta.getString("direccion_empresa")
                                                     ,respuesta.getString("rubro")
                                                     ,respuesta.getString("ingresos")
                                                     ,respuesta.getString("moneda_ingresos")
                                                     ,respuesta.getInt("proyecto")
                                                     ,respuesta.getString("urbanizacion")
                                                     ,respuesta.getString("uv")
                                                     ,respuesta.getString("mz")
                                                     ,respuesta.getInt("lt")
                                                     ,respuesta.getString("cat")
                                                     ,respuesta.getString("metros2")
                                                     ,respuesta.getString("tipo_venta")
                                                     ,respuesta.getString("cuotas")
                                                     ,respuesta.getString("asesor")
                                                     ,respuesta.getInt("codigo_asesor")
                                                     ,respuesta.getString("observacion")
                                                     ,respuesta.getString("observacion2")
                                                     ,respuesta.getString("latitud")
                                            ,respuesta.getString("longitud")
                                            ,respuesta.getString("ubicacion")));
                        }
                        AdapterTitulares adaptertitulares = new AdapterTitulares(listTitulares);
                        madapter=new AdapterTitulares(listTitulares); ///importante
                        recycler.setAdapter(adaptertitulares);
                    }catch(JSONException e) {
                        Toast.makeText(getContext(), "No se cargaron los datos o no existen de esta fecha", Toast.LENGTH_LONG).show();
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
                parametros.put("codigo", Global.codigo.toString());
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
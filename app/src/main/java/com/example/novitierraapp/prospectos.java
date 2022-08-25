package com.example.novitierraapp;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.novitierraapp.entidades.Proyectos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.AdapterProspectos;

public class prospectos extends Fragment {
    String hoy="";
    TextView fecha,usuario,grupo;
    EditText nombre,telefono,observacion,lugar,zona;
    Spinner spinnerLlamada,spinnerUrb;
    Button btRegistrarProspecto;
    ArrayList<String> listaUrbanizacion = new ArrayList<>();
    ArrayList<String> listaLlamada = new ArrayList<>();
    metodos metodos = new metodos();
    private String URL_Prospecto="http://wizardapps.xyz/novitierra/api/addProspecto.php";

    private ProspectosViewModel mViewModel;

    public static prospectos newInstance() {
        return new prospectos();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.prospectos_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProspectosViewModel.class);
        // TODO: Use the ViewModel
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fecha = view.findViewById(R.id.prospectoFecha);
        usuario= view.findViewById(R.id.prospectoUser);
        nombre=view.findViewById(R.id.prospectoNombre);
        telefono=view.findViewById(R.id.prospectoTelefono);
        observacion=view.findViewById(R.id.prospectoObservacion);
        lugar=view.findViewById(R.id.prospectoLugar);
        zona=view.findViewById(R.id.prospectoZona);
        spinnerLlamada=view.findViewById(R.id.spinnerLlamada);
        spinnerUrb=view.findViewById(R.id.spinnerProspectoUrb);
        grupo =  view.findViewById(R.id.prospectoGrupo);
        btRegistrarProspecto = view.findViewById(R.id.prospectoRegistrar);

        cargarComponentes();
        fecha.setText("Fecha: "+LocalDate.now().toString());
        usuario.setText(Global.nombreSesion.toUpperCase().toString()+" "+Global.apellidoSesion.toUpperCase().toString());
        grupo.setText(Global.grupo.toUpperCase());

        btRegistrarProspecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(camposVacios()){
                    if(telefono.getText().length()>9){
                        mensaje("Hubo un problema con el telefono, excede el numero de digitos o se ha copiado/pegado");
                    }else{
                        registrarProspecto();
                    }

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            vaciarCampos();
//                        }
//                    },500);
                }else {
                    mensaje("Nombre , Telefono y Proyecto de interes son obligatorios");
                }
            }
        });
    }
    public Boolean camposVacios(){
        if(nombre.length()==0 || telefono.length()==0 || spinnerUrb.getSelectedItemPosition()==0){
            return false;
        }else {
            return true;
        }
    }
    public void mensaje(String mensaje){
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
    }
    public void vaciarCampos(){
        nombre.setText("");
        telefono.setText("");
        observacion.setText("");
        zona.setText("");
        lugar.setText("");
        spinnerLlamada.setSelection(0);
        spinnerUrb.setSelection(0);
    }

    public void cargarComponentes(){
        metodos.cargarUrbanizacionProspectos(listaUrbanizacion);
        ArrayAdapter<String> adapterurb = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaUrbanizacion);
        spinnerUrb.setAdapter(adapterurb);

        metodos.cargarLlamada(listaLlamada);
        ArrayAdapter<String> adapterLlamada = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaLlamada);
        spinnerLlamada.setAdapter(adapterLlamada);
    }

    private void registrarProspecto() {
        StringRequest request = new StringRequest(Request.Method.POST, URL_Prospecto, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    if(response.contains("algo salio mal")){
                        Toast.makeText(getContext(),"No se pudo completar el registro debido a un error",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(response.contains("se inserto correctamente")){
                            mensaje("Datos Registrados");
                            vaciarCampos();
                            return;
                    }
                    if(response.contains("vencida")){
                            mensaje("puede prospectar");
                            return;
                    }
                    else{
                        mensaje("Telefono prospectado y tiene vigencia hasta la fecha "+response);
                        return;
                    }
                }else{
                    Toast.makeText(getContext(), "No se ha registrado a la base de datos", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("cliente",nombre.getText().toString().toUpperCase());
                parametros.put("telefono",telefono.getText().toString());
                parametros.put("llamada",spinnerLlamada.getSelectedItem().toString());
                parametros.put("zona",zona.getText().toString().toUpperCase());
                parametros.put("lugar",lugar.getText().toString().toUpperCase());
                parametros.put("urbanizacion",spinnerUrb.getSelectedItem().toString());
                parametros.put("observacion",observacion.getText().toString().toUpperCase());
                parametros.put("asesor",Global.nombreSesion.toUpperCase().toString()+" "+Global.apellidoSesion.toUpperCase().toString());
                parametros.put("codigo",Global.codigo.toString());
                parametros.put("grupo",Global.grupo.toUpperCase());
                parametros.put("fecha", LocalDateTime.now().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

}
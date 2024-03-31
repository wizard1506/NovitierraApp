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
import com.example.novitierraapp.entidades.Proyectos2;

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
    TextView fecha,etAsesor,etGrupo,etCodigo, tvErrorProspecto;
//    TextView usuario,grupo;
    EditText nombre,telefono,observacion,lugar,zona;
    Spinner spinnerLlamada;
    Spinner spinnerProyectos;
    Button btRegistrarProspecto;
    ArrayList<String> listaUrbanizacion = new ArrayList<>();
    ArrayList<String> listaLlamada = new ArrayList<>();
    ArrayList<Proyectos2> listProyectos = new ArrayList<>();
    metodos metodos = new metodos();
    private String URL_Prospecto="http://wizardapps.xyz/novitierra/api/addProspecto2.php";
    private String URL_proyectos = "http://wizardapps.xyz/novitierra/api/getProyectos.php" ;

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
//        usuario= view.findViewById(R.id.prospectoUser);
//        grupo =  view.findViewById(R.id.prospectoGrupo);
        etAsesor = view.findViewById(R.id.prospectoAsesor);
        etGrupo = view.findViewById(R.id.prospectoGrupo);
        etCodigo = view.findViewById(R.id.prospectoCodigo);
        tvErrorProspecto = view.findViewById(R.id.errorProspecto);
        nombre=view.findViewById(R.id.prospectoNombre);
        telefono=view.findViewById(R.id.prospectoTelefono);
        observacion=view.findViewById(R.id.prospectoObservacion);
        lugar=view.findViewById(R.id.prospectoLugar);
        zona=view.findViewById(R.id.prospectoZona);
        spinnerLlamada=view.findViewById(R.id.spinnerLlamada);
//        spinnerUrb=view.findViewById(R.id.spinnerProspectoUrb);
        spinnerProyectos = view.findViewById(R.id.spinnerProspectoUrb2);

        btRegistrarProspecto = view.findViewById(R.id.prospectoRegistrar);

        cargarComponentes();
        datosIniciales();
        fecha.setText("Fecha: "+LocalDate.now().toString());
//        usuario.setText(Global.nombreSesion.toUpperCase().toString()+" "+Global.apellidoSesion.toUpperCase().toString());
//        grupo.setText(Global.grupo.toUpperCase());

        btRegistrarProspecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Dentro de tu fragmento
                if (Global.verificarDatosSesion(requireContext())) {
                    if(camposVacios()){
//                    if(telefono.getText().length()>20){
//                        mensaje("Hubo un problema con el telefono, excede el numero de digitos o se ha copiado/pegado");
//                    }else{
                        registrarProspecto();
//                    }

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            vaciarCampos();
//                        }
//                    },500);
                    }else {
                        mensaje("Nombre , Telefono y Proyecto de interes son obligatorios ; Datos Asesor obligatorios");
                    }
                    // Los datos de sesión están disponibles, puedes continuar con el registro
                    // Escribe aquí el código para realizar el proceso de registro
                } else {
                    // Los datos de sesión no están disponibles, muestra un mensaje al usuario o realiza alguna otra acción
                    Toast.makeText(requireContext(), "Sesion expirada, por favor inicia sesión nuevamente", Toast.LENGTH_SHORT).show();
                    // También puedes redirigir al usuario a la pantalla de inicio de sesión, por ejemplo:
                    // Navigation.findNavController(requireView()).navigate(R.id.action_fragmento_registro_to_fragmento_inicio_sesion);
                }


            }
        });
    }

    private void datosIniciales() {
// Dentro de tu fragmento
        if (Global.verificarDatosSesion(requireContext())) {
            etGrupo.setText(Global.getGrupo());
            etCodigo.setText(Global.getCodigo().toString());
            etAsesor.setText(Global.getNombreSesion().concat(" ").concat(Global.getApellidoSesion()));
        } else {
            // Los datos de sesión no están disponibles, muestra un mensaje al usuario o realiza alguna otra acción
            Toast.makeText(requireContext(), "Datos de sesion expirados inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            // También puedes redirigir al usuario a la pantalla de inicio de sesión, por ejemplo:
            // Navigation.findNavController(requireView()).navigate(R.id.action_fragmento_registro_to_fragmento_inicio_sesion);
        }

    }

    public void getUrbanizacionProyectos(){
        RequestQueue requestQueue;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_proyectos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject respuesta = array.getJSONObject(i);
                            Proyectos2 proyectos2 = new Proyectos2();
                            proyectos2.setCodigo(respuesta.getString("codigo"));
                            proyectos2.setProyecto(respuesta.getString("proyecto"));
                            listProyectos.add(proyectos2);
                        }
                        ArrayAdapter<Proyectos2> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line,listProyectos);
                        spinnerProyectos.setAdapter(adapter);
                    }catch(JSONException e) {
                        Toast.makeText(getContext(), "No se cargaron los referidores o no existen aun", Toast.LENGTH_LONG).show();
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
//                parametros.put("id", Global.idReferidor);
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public Boolean camposVacios(){
        if(nombre.length()==0 || telefono.length()==0 || etAsesor.length()==0 || etCodigo.length()==0 || etGrupo.length()==0){
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

    }

    public void cargarComponentes(){
        getUrbanizacionProyectos();
        metodos.cargarUrbanizacionProspectos(listaUrbanizacion);
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
                            tvErrorProspecto.setVisibility(View.GONE);
                            return;
                    }
                    if(response.contains("vencida")){
                            mensaje("puede prospectar");
                            return;
                    }
                    else{
                        mensaje("Telefono Prospectado por: "+response);
                        tvErrorProspecto.setText("Telefono Prospectado por: "+response );
                        tvErrorProspecto.setVisibility(View.VISIBLE);
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
                parametros.put("urbanizacion",spinnerProyectos.getSelectedItem().toString());
                parametros.put("observacion",observacion.getText().toString().toUpperCase());
                parametros.put("asesor",etAsesor.getText().toString().toUpperCase());
                parametros.put("codigo",etCodigo.getText().toString().toUpperCase());
                parametros.put("grupo",etGrupo.getText().toString().toUpperCase());
                parametros.put("fecha", LocalDateTime.now().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

}
package com.example.novitierraapp;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
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
import com.example.novitierraapp.entidades.Proyectos2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class updateProspecto extends Fragment {
    TextView fecha;
//    TextView usuario;
    EditText nombre,telefono,observacion,zona,lugar;
    Spinner llamada;
    Spinner spinnerProyectos;
    ArrayList<String> listaLlamada;
    ArrayList<Proyectos2> listProyectos;
    metodos metodos = new metodos();
    Integer id = 0;
    Button modificar;
    String datoURB="";

    private UpdateProspectoViewModel mViewModel;

//    String urb;
    private static  String URL_updateprospecto="http://wizardapps.xyz/novitierra/api/updateProspecto.php";
    private String URL_proyectos = "http://wizardapps.xyz/novitierra/api/getProyectos.php";

    public static updateProspecto newInstance() {
        return new updateProspecto();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nombre = view.findViewById(R.id.updprospectoNombre);
        telefono = view.findViewById(R.id.updprospectoTelefono);
        observacion = view.findViewById(R.id.updprospectoObservacion);
        llamada = view.findViewById(R.id.updspinnerLlamada);
        zona = view.findViewById(R.id.updprospectoZona);
        lugar = view.findViewById(R.id.updprospectoLugar);

        spinnerProyectos = view.findViewById(R.id.updspinnerProspectoUrb2);
        fecha = view.findViewById(R.id.updprospectoFecha);
//        usuario = view.findViewById(R.id.updprospectoUser);
        modificar = view.findViewById(R.id.btmodificarProspecto);
        listaLlamada = new ArrayList<>();
        listProyectos = new ArrayList<>();

        if(getArguments()!=null){
            datoURB = getArguments().getString("urbanizacion");
        }

        fecha.setText("Fecha: "+ LocalDate.now().toString());
//        usuario.setText(Global.nombreSesion.toUpperCase().toString()+" "+Global.apellidoSesion.toUpperCase().toString());
        cargarListas();
        getUrbanizacionProyectos();

        id=getArguments().getInt("id");
        nombre.setText(getArguments().getString("nombre"));
        telefono.setText(String.valueOf(getArguments().getInt("telefono")));
        zona.setText(getArguments().getString("zona"));
        lugar.setText(getArguments().getString("lugar"));
        observacion.setText(getArguments().getString("observacion"));
        elegirSpinnerLlamada(getArguments().getString("llamada"));
//        datoURB= getArguments().getString("urbanizacion");


        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    updateProspecto();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Navigation.findNavController(v).navigate(R.id.nav_home);
                    }
                },500);
            }
        });

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.update_prospecto_fragment, container, false);
    }
    public void cargarListas(){

        metodos.cargarLlamada(listaLlamada);
        ArrayAdapter<String> adapterLLamada = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaLlamada);
        llamada.setAdapter(adapterLLamada);

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
//                            proyectos2.setCodigo(respuesta.getString("codigo"));
                            proyectos2.setProyecto(respuesta.getString("proyecto"));
                            listProyectos.add(proyectos2);
                        }
                        ArrayAdapter<Proyectos2> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line,listProyectos);
                        spinnerProyectos.setAdapter(adapter);


                        Integer j = spinnerProyectos.getCount();
                        for(Integer i=0;i<=j;i++){
                            spinnerProyectos.setSelection(i);
                            if(spinnerProyectos.getItemAtPosition(i).toString().contains(datoURB)){
                                spinnerProyectos.setSelection(i);
                                break;
                            }
                        }

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UpdateProspectoViewModel.class);
        // TODO: Use the ViewModel
    }

    public void elegirSpinnerLlamada(String valor){
        Integer j = llamada.getCount();
        for(int i=0;i<=j;i++){
            llamada.setSelection(i);
            if(llamada.getItemAtPosition(i).toString().contains(valor)){
                llamada.setSelection(i);
                break;
            }
        }
    }

    public void updateProspecto(){
        StringRequest request = new StringRequest(Request.Method.POST, URL_updateprospecto, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    if(response.contains("algo salio mal")){
                        Toast.makeText(getContext(),"No se pudo modificar el registro debido a un error",Toast.LENGTH_LONG).show();
                    }
                    else{Toast.makeText(getContext(),"Datos modificados",Toast.LENGTH_LONG).show();}

                }else{
                    Toast.makeText(getContext(), "No se ha podido modificar el prospecto", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("id",(id.toString()));
                parametros.put("nombre_completo",nombre.getText().toString());
                parametros.put("telefono",telefono.getText().toString());
                parametros.put("llamada",llamada.getSelectedItem().toString());
                parametros.put("zona",zona.getText().toString());
                parametros.put("lugar",lugar.getText().toString());
                parametros.put("urbanizacion",spinnerProyectos.getSelectedItem().toString());
                parametros.put("observacion",observacion.getText().toString());
//                parametros.put("asesor",Global.userSesion);
//                parametros.put("codigo",Global.codigo.toString());

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }



}
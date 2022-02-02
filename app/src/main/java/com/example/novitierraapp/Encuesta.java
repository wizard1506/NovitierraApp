package com.example.novitierraapp;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Encuesta extends Fragment {

    private EncuestaViewModel mViewModel;
    ArrayList<String> pregunta1 = new ArrayList<>();
    Spinner respuesta1 ;
    RadioGroup rgRedesSociales;
    RadioButton facebook,instagram,tiktok,paginaWeb,paginaAsesor, rbRedSocial;
    Button btEnviarRespuesta;
    String result="";
    private String URL_voto="http://wizardapps.xyz/novitierra/api/updateEncuesta.php";
    public static Encuesta newInstance() {
        return new Encuesta();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.encuesta_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EncuestaViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        respuesta1 = view.findViewById(R.id.spinnerRespuesta1);
        cargarRespuestas1();
        rgRedesSociales = view.findViewById(R.id.rgRedesSociales1);

        facebook = view.findViewById(R.id.rbFacebook);
        tiktok = view.findViewById(R.id.rbTiktok);
        instagram = view.findViewById(R.id.rbInstagram);
        paginaWeb = view.findViewById(R.id.rbPaginaWeb);
        paginaAsesor = view.findViewById(R.id.rbPaginaAsesor);
        btEnviarRespuesta = view.findViewById(R.id.btEnviarRespuesta);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionRedSocial(v);
            }
        });
        tiktok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionRedSocial(v);
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionRedSocial(v);
            }
        });
        paginaWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionRedSocial(v);
            }
        });
        paginaAsesor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionRedSocial(v);
            }
        });
        btEnviarRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultados();
                enviarEncuesta();
            }
        });
        respuesta1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(respuesta1.getSelectedItem().toString().contains("Redes Sociales")) {
                    rgRedesSociales.setVisibility(View.VISIBLE);
                }else {
                    rgRedesSociales.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void cargarRespuestas1(){
        pregunta1.add("Radio");
        pregunta1.add("Panfleteo");
        pregunta1.add("Referidor");
        pregunta1.add("Redes Sociales");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,pregunta1);
        respuesta1.setAdapter(adapter);
    }
    public void seleccionRedSocial(View v){
        int radiobtid = rgRedesSociales.getCheckedRadioButtonId();
        rbRedSocial = v.findViewById(radiobtid);
    }
    public String resultados(){
        if(respuesta1.getSelectedItem().toString().contains("Redes Sociales")){
            if(validarRedSocial()){
                result=rbRedSocial.getText().toString();
            }
        }else {
            result=respuesta1.getSelectedItem().toString();
        }
        return result;
    }
    public Boolean validarRedSocial(){
        if(facebook.isChecked() || tiktok.isChecked() || instagram.isChecked() || paginaWeb.isChecked() || paginaAsesor.isChecked()){
           return true;
        }else {
            Toast.makeText(getContext(),"Seleccionar una red social",Toast.LENGTH_LONG).show();
            return false;
        }

    }

    private void enviarEncuesta() {
        StringRequest request = new StringRequest(Request.Method.POST, URL_voto, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    if(response.contains("algo salio mal")){
                        Toast.makeText(getContext(),"No se pudo enviar el resultado",Toast.LENGTH_LONG).show();
                    }
                    else{Toast.makeText(getContext(),"Datos registrados",Toast.LENGTH_LONG).show();
                          btEnviarRespuesta.setEnabled(false);
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("resultado",result);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

}
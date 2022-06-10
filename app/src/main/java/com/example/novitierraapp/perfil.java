package com.example.novitierraapp;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class perfil extends Fragment {

    EditText nombre,apellido,ci,telefono,codigo,grupo,usuario;
    Button btguardarPerfil;
    private String URL_Perfil="http://wizardapps.xyz/novitierra/api/updateappUser.php";

    private PerfilViewModel mViewModel;

    public static perfil newInstance() {
        return new perfil();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.perfil_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nombre = view.findViewById(R.id.perfilNombre);
        apellido = view.findViewById(R.id.perfilApellido);
        ci= view.findViewById(R.id.perfilCi);
        telefono=view.findViewById(R.id.perfilTelefono);
        codigo=view.findViewById(R.id.perfilCodigo);
        grupo=view.findViewById(R.id.perfilGrupo);
        usuario=view.findViewById(R.id.perfilUsuario);
        btguardarPerfil=view.findViewById(R.id.btGuardarPerfil);

        nombre.setText(Global.nombreSesion);
        apellido.setText(Global.apellidoSesion);
        ci.setText(Global.ciSesion);
        telefono.setText(Global.telefonoSesion);
        codigo.setText(Global.codigo.toString());
        grupo.setText(Global.grupo);
        usuario.setText(Global.userSesion);

        btguardarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
                getActivity().finish();
            }
        });


    }

    private void guardar() {
        StringRequest request = new StringRequest(Request.Method.POST, URL_Perfil, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    if(response.contains("error")){
                        Toast.makeText(getContext(),"No se pudo completar el registro debido a un error",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(response.contains("correcto")){
                        mensaje("Datos Modificados vuelva a iniciar sesion");
                        Global.nombreSesion= nombre.getText().toString();
                        Global.apellidoSesion= apellido.getText().toString();
                        Global.ciSesion=ci.getText().toString();
                        Global.telefonoSesion=telefono.getText().toString();
                        Global.codigo = Integer.valueOf(codigo.getText().toString());
                        Global.grupo = grupo.getText().toString();
                        Global.userSesion=usuario.getText().toString();
                        return;
                    }
                }else{
                    Toast.makeText(getContext(), "No se ha completado la operacion", Toast.LENGTH_LONG).show();
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
                parametros.put("id_usuario",Global.useridSesion);
                parametros.put("nombres",nombre.getText().toString());
                parametros.put("apellidos",apellido.getText().toString());
                parametros.put("ci",ci.getText().toString());
                parametros.put("telefono",telefono.getText().toString());
                parametros.put("codigo",codigo.getText().toString());
                parametros.put("grupo",grupo.getText().toString().toUpperCase());
                parametros.put("usuario",usuario.getText().toString());

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void mensaje(String mensaje) {
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        // TODO: Use the ViewModel
    }

}
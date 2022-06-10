package com.example.novitierraapp;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
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

import java.util.HashMap;
import java.util.Map;

public class updatePassword extends Fragment {
    EditText pass,pass2;
    Button btguardarPassword;
    private String URL_Pass="http://wizardapps.xyz/novitierra/api/updateappPassword.php";

    private UpdatePasswordViewModel mViewModel;

    public static updatePassword newInstance() {
        return new updatePassword();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.update_password_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pass = view.findViewById(R.id.perfilPassword);
        pass2 = view.findViewById(R.id.perfilPassword2);
        btguardarPassword = view.findViewById(R.id.btGuardarPassword);

        btguardarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(compararPass()){
                    guardar();
                }else {
                    mensaje("Contraseña antigua incorrecta");
                }

            }
        });
    }

    public Boolean compararPass(){
        if(pass.getText().toString().equals(Global.upass)){
            return true;
        }else return false;
    }

    private void guardar() {
        StringRequest request = new StringRequest(Request.Method.POST, URL_Pass, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    if(response.contains("error")){
                        Toast.makeText(getContext(),"No se pudo completar el registro debido a un error",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(response.contains("correcto")){
                        mensaje("Datos Modificados vuelva a iniciar sesion");
                        getActivity().finish();
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
                parametros.put("pass",pass2.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void mensaje(String s) {
        Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UpdatePasswordViewModel.class);
        // TODO: Use the ViewModel
    }

}
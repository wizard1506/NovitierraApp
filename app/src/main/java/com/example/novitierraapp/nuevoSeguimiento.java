package com.example.novitierraapp;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

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
import com.example.novitierraapp.ui.home.HomeFragment;

import java.util.HashMap;
import java.util.Map;

public class nuevoSeguimiento extends Fragment {

    EditText txtSeguimiento;
    Button btaddSeguimiento;
    Integer idProspecto;
    String nombreProspecto;
    private Context mContext;
    private NavController navController;

    private static  String URL_addSeguimiento="http://wizardapps.xyz/novitierra/api/addSeguimiento.php";

    private NuevoSeguimientoViewModel mViewModel;

    public static nuevoSeguimiento newInstance() {
        return new nuevoSeguimiento();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nuevo_seguimiento, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NuevoSeguimientoViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = requireContext();

        // Obtener el NavController
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);


        txtSeguimiento = view.findViewById(R.id.txtaddSeguimiento);
        btaddSeguimiento = view.findViewById(R.id.btAddSeguimiento);

        if(getArguments()!=null){
            idProspecto = getArguments().getInt("id");
            nombreProspecto = getArguments().getString("nombre");
        }

        btaddSeguimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarSeguimiento();

                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nuevoSeguimiento, true)
                        .build();
                navController.navigate(R.id.prospectosHoy, getArguments(), navOptions);

            }
        });

    }

    private void registrarSeguimiento() {
        StringRequest request = new StringRequest(Request.Method.POST, URL_addSeguimiento, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    if(response.contains("algo salio mal")){
                        Toast.makeText(mContext,"No se pudo modificar el registro debido a un error",Toast.LENGTH_LONG).show();
                    }
                    else{Toast.makeText(mContext,"Datos registrados",Toast.LENGTH_LONG).show();}

                }else{
                    Toast.makeText(mContext, "No se ha podido completar el registro", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("id",(idProspecto.toString()));
                parametros.put("seguimiento",txtSeguimiento.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(request);

    }




}
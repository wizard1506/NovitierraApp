package com.example.novitierraapp;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.novitierraapp.entidades.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CargarFormulario extends Fragment {

    EditText cnombre, capellidoP,capellidoM,capellidoC,cprefijo,ctipoident,ccicliente,cextension,cnacionalidad,cfechanac,cestadocivil,csexo,cnivestudios,cprofesion,ctipovivienda,ctenencia,ccostovivienda,cmonedavivienda,cnombrepropietario,ctelfpropietario,cpais,cdpto,czona,cbarrio,cavenida,ccalle,cnumerocasa,cfijo,cmovil,coficfijo,coficmovil,ccorreo,cempresa,cdirecempresa,crubro,cingresos,cmonedaingresos,creferencia1,crelacion1,ctelfref1,creferencia2,crelacion2,ctelfref2,ctipoventa,ccuotas,curbanizacion,ccodproyecto,cuv,cmz,clt,ccat,cmts2,casesor,ccodigoasesor,cobservacion,cobservacion2;
    private static final String URL = "http://wizardapps.xyz/novitierra/api/cargarFormulario.php" ;

    private CargarFormularioViewModel mViewModel;

    public static CargarFormulario newInstance() {
        return new CargarFormulario();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cargar_formulario_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CargarFormularioViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cnombre = view.findViewById(R.id.cnombreCliente);
        capellidoP = view.findViewById(R.id.capellidoPCliente);
        capellidoM = view.findViewById(R.id.capellidoMCliente);
        capellidoC = view.findViewById(R.id.capellidoCasada);
        cprefijo= view.findViewById(R.id.ctipoPrefijo);
        ctipoident = view.findViewById(R.id.ctipoIdentificacion);
        ccicliente = view.findViewById(R.id.cciCliente);
        cextension = view.findViewById(R.id.cExtension);
        cnacionalidad = view.findViewById(R.id.cnacionalidad);
        cfechanac = view.findViewById(R.id.cfechaNacimiento);
        cestadocivil = view.findViewById(R.id.cestadoCivil);
        csexo = view.findViewById(R.id.csexo);
        cnivestudios = view.findViewById(R.id.cnivelEstudio);
        cprofesion = view.findViewById(R.id.cprofesion);
        ctipovivienda = view.findViewById(R.id.ctipoVivienda);
        ctenencia = view.findViewById(R.id.ctenencia);
        ccostovivienda = view.findViewById(R.id.ccostoAprox);
        cmonedavivienda = view.findViewById(R.id.cMonedaCostoAprox);
        cnombrepropietario = view.findViewById(R.id.cpropietarioVivienda);
        ctelfpropietario = view.findViewById(R.id.ctelefonoPropietario);
        cpais= view.findViewById(R.id.cpaisVivienda);
        cdpto = view.findViewById(R.id.cdptoBolivia);
        czona = view.findViewById(R.id.czonaVivienda);
        cbarrio = view.findViewById(R.id.cbarrioVivienda);
        cavenida = view.findViewById(R.id.cavenidadVivienda);
        ccalle = view.findViewById(R.id.ccalleVivienda);
        cnumerocasa = view.findViewById(R.id.cnumeroVivienda);
        cfijo = view.findViewById(R.id.ctelfFijoCliente);
        cmovil = view.findViewById(R.id.ctelfMovilCliente);
        coficfijo = view.findViewById(R.id.ctelfFijoOficina);
        coficmovil = view.findViewById(R.id.ctelfMovilOficina);
        ccorreo = view.findViewById(R.id.ccorreoPersonal);
        cempresa = view.findViewById(R.id.cempresaNombre);
        cdirecempresa = view.findViewById(R.id.cdireccionEmpresa);
        crubro = view.findViewById(R.id.crubroEmpresa);
        cingresos = view.findViewById(R.id.cingresos);
        cmonedaingresos = view.findViewById(R.id.cmonedaIngresos);
        creferencia1 = view.findViewById(R.id.cReferencia1);
        crelacion1 = view.findViewById(R.id.cRelacion1);
        ctelfref1 = view.findViewById(R.id.ctelfReferencia1);
        creferencia2 = view.findViewById(R.id.cReferencia2);
        crelacion2 = view.findViewById(R.id.cRelacion2);
        ctelfref2 = view.findViewById(R.id.ctelfReferencia2);
        ctipoventa = view.findViewById(R.id.cPlazoContado);
        ccuotas = view.findViewById(R.id.ccuotasplazo);
        curbanizacion = view.findViewById(R.id.curbanizacion);
        ccodproyecto = view.findViewById(R.id.cidProyecto);
        cuv = view.findViewById(R.id.cuv);
        cmz = view.findViewById(R.id.cmz);
        clt = view.findViewById(R.id.clt);
        ccat = view.findViewById(R.id.ccat);
        cmts2 = view.findViewById(R.id.cmts2);
        casesor = view.findViewById(R.id.ccodigoAsesor);
        ccodigoasesor = view.findViewById(R.id.ccodigoAsesor);
        cobservacion = view.findViewById(R.id.cobservacion1);
        cobservacion2 = view.findViewById(R.id.cobservacion2);

    }

//    public void traerFormulario(){
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if (!response.isEmpty()){
//                    try {
//                        JSONArray array = new JSONArray(response);
//                        for (int i = 0; i <array.length() ; i++) {
//                            JSONObject respuesta = array.getJSONObject(i);
//                            Global.nombreSesion=respuesta.getString("nombres");
//                            Global.apellidoSesion=respuesta.getString("apellidos");
//                            Global.userSesion=respuesta.getString("usuario");
//                            Global.codigo=respuesta.getInt("codigo");
//                            usu=respuesta.getString("usuario");
//                            pass=respuesta.getString("upassword");
//                            usu2=userLogin.getText().toString();
//                            pass2=passwordLogin.getText().toString();
//                            if(usu2.equals(usu)&&pass2.equals(pass)){
//                                validarUsuario(URL);
//                            }else {
//                                Toast.makeText(MainActivity.this, "Usuario o Contraseña incorrecta", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    }catch(JSONException e) {
//                        Toast.makeText(MainActivity.this, "Usuario no registrado", Toast.LENGTH_LONG).show();
//                        e.printStackTrace();
//                    }
//                }else{
//                    Toast.makeText(MainActivity.this, "Ocurrio algun error", Toast.LENGTH_LONG).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MainActivity.this,error.toString(), Toast.LENGTH_LONG).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> parametros = new HashMap<String, String>();
//                parametros.put("usuario",userLogin.getText().toString());
//                return parametros;
//            }
//        };
//        requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }
}
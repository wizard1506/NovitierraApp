package com.example.novitierraapp;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.system.ErrnoException;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.novitierraapp.entidades.Asesor;
import com.example.novitierraapp.entidades.Global;
import com.example.novitierraapp.entidades.Proyectos;
import com.example.novitierraapp.entidades.Proyectos2;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Formularios extends Fragment {

    private FormulariosViewModel mViewModel;
    ///text views fechas
    TextView day,month,year;
    String fechaNacBD="";
    EditText nombre_cliente, apellidoPaterno, apellidoMaterno, ci_cliente,uv,mz,lt,cat,asesor,codigo_asesor, apellidoCasada,nacionalidad,profesion,costoAprox;
    EditText propietarioVivienta,telefonoPropietario,pais,ciudad,barrio,avenida,calle,numero,telFijo,telMovil,telFijoOfc,telMovOfc,correoPersonal,expedido,mts2;
    EditText nombreEmpresa, rubroEmpresa, direccionEmpresa, ingresosEmpresa,primerReferencia,segundaReferencia,telfReferencia1,telfReferencia2,parentesco,relacion,zona;
    EditText observacion1, observacion2, codigoFormulario;
    RadioGroup radioGroup, radioGroupGenero, radioGroupVivienda, radioGroupIngresos, radioGroupIndependienteDependiente;
    RadioButton rb_plazo, rb_contado, rbSelected, rbMasculino, rbFemenino, rbSelectedGenero, rbIngresosBs, rbIngresosDolar,rbSelectedIngresos, rbDependiende, rbIndependiente, rbSelectedIndependienteDependiente;
//    rbSelectedMonedaVivienda
//    rbViviendaBs,rbViviendaDolar,
    RadioGroup radioGroupSinConUbicacion;
    RadioButton rbconUbicacion, rbsinUbicacion, rbSelectedSinConUbicacion;
    Spinner spinnerIdentificacion,spinnerEstadoCivil,spinnerNivelEstudio, spinnerTipoVivienda, spinnerDpto, spinnerTenencia, spinnerPrefijo, spinnerExtension, spinnerMoneda, spinnerPlazo, spinnerReserva;
//    Spinner spinner_urbanizacion;   // ya no usado;
    Spinner spinnerProyectos;
    TextView codigo_proyecto,tvfechaNacimiento,tvplazo;
    Button guardar, btFechaNac, cargar; //registrarForm;
//    Proyectos proyectos;
    DatePickerDialog datePickerDialog;
    ArrayList<Proyectos> listaProyectos = new ArrayList<>();
    ArrayList<String> listaExtension = new ArrayList<>();
    ArrayList<String> listaIdentificacion = new ArrayList<>();
    ArrayList<String> listaEstadoCivil = new ArrayList<>();
    ArrayList<String> listaNivelEstudio = new ArrayList<>();
    ArrayList<String> listaTipoVivienda = new ArrayList<>();
    ArrayList<String> listaDpto = new ArrayList<>();
    ArrayList<String> listaTenencia = new ArrayList<>();
    ArrayList<String> listaPrefijo = new ArrayList<>();
    ArrayList<String> listaMoneda = new ArrayList<>();
    ArrayList<String> listaPlazo = new ArrayList<>();
    ArrayList<String> listaReserva = new ArrayList<>();
    metodos metodos = new metodos();

    String URL_proyectos = "http://wizardapps.xyz/novitierra/api/getProyectos.php" ;
    ArrayList<Proyectos2> listProyectos = new ArrayList<>();

    Bitmap imagen,scaled;

    private String URL_addtitular="http://wizardapps.xyz/novitierra/api/addTitular.php";
    private String URL_formulario="http://wizardapps.xyz/novitierra/api/cargarFormulario.php";
    private String ubicacion = "https://www.google.es/maps?q=";
//    private String URL_addtitular="https://novitierra.000webhostapp.com/api/addTitular.php";

    //***PARA PDF****
    private String path = Environment.getExternalStorageDirectory().getPath() + "/Download/FormularioNovitierra.pdf";
    private File file = new File(path);

    public static Formularios newInstance() {
        return new Formularios();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ///necesario para poder compartir el pdf
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//        builder.detectFileUriExposure();
        /////
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        day = view.findViewById(R.id.formDay);
        month = view.findViewById(R.id.formMonth);
        year = view.findViewById(R.id.formYear);
        nombre_cliente = view.findViewById(R.id.nombreCliente);
        //nombre_cliente.setRawInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        apellidoPaterno= view.findViewById(R.id.apellidoPCliente);
        apellidoMaterno= view.findViewById(R.id.apellidoMCliente);
        apellidoCasada = view.findViewById(R.id.apellidoCasada);
        ci_cliente = view.findViewById(R.id.ciCliente);
        nacionalidad = view.findViewById(R.id.nacionalidad);
        //fechaNac = view.findViewById(R.id.fechaNacimiento);
        profesion = view.findViewById(R.id.profesion);
        costoAprox = view.findViewById(R.id.costoAprox);
        propietarioVivienta=view.findViewById(R.id.propietarioVivienda);
        telefonoPropietario=view.findViewById(R.id.telefonoPropietario);
        pais=view.findViewById(R.id.paisVivienda);
        ciudad=view.findViewById(R.id.ciudadVivienda);
        zona=view.findViewById(R.id.zonaVivienda);
        barrio=view.findViewById(R.id.barrioVivienda);
        avenida=view.findViewById(R.id.avenidadVivienda);
        calle=view.findViewById(R.id.calleVivienda);
        numero=view.findViewById(R.id.numeroVivienda);
        telFijo= view.findViewById(R.id.telfFijoCliente);
        telMovil=view.findViewById(R.id.telfMovilCliente);
        telFijoOfc=view.findViewById(R.id.telfFijoOficina);
        telMovOfc=view.findViewById(R.id.telfMovilOficina);
        correoPersonal = view.findViewById(R.id.correoPersonal);
        nombreEmpresa = view.findViewById(R.id.empresaNombre);
        rubroEmpresa=view.findViewById(R.id.rubroEmpresa);
        direccionEmpresa=view.findViewById(R.id.direccionEmpresa);
        ingresosEmpresa=view.findViewById(R.id.ingresos);
        primerReferencia=view.findViewById(R.id.nombreFamiliarCercano);
        segundaReferencia=view.findViewById(R.id.otraReferencia);
        telfReferencia1=view.findViewById(R.id.telfFamiliarCercano);
        telfReferencia2=view.findViewById(R.id.telfOtraReferencia);
        parentesco=view.findViewById(R.id.parentescoFamiliar);
        relacion=view.findViewById(R.id.relacionReferencia);
        uv= view.findViewById(R.id.uv);
        mz=view.findViewById(R.id.mz);
        lt= view.findViewById(R.id.lt);
        cat=view.findViewById(R.id.cat);
        mts2=view.findViewById(R.id.mts2);
        asesor=view.findViewById(R.id.fullnameAsesor);
        codigo_asesor= view.findViewById(R.id.codigoAsesor);
        observacion1 = view.findViewById(R.id.observacion1);
        observacion2 = view.findViewById(R.id.observacion2);
        codigoFormulario = view.findViewById(R.id.codigoformulario);
        ////radioButtons y RadioGroups
        radioGroup = view.findViewById(R.id.radiogroup);
        radioGroupGenero=view.findViewById(R.id.radiogroupGenero);
//        radioGroupVivienda = view.findViewById(R.id.radiogroupVivienda);
        radioGroupIngresos = view.findViewById(R.id.radiogroupIngresos);
        radioGroupSinConUbicacion=view.findViewById(R.id.radiogroupElegirUbicacion);
        radioGroupIndependienteDependiente = view.findViewById(R.id.rgDependienteIndependiente);

        rb_plazo = view.findViewById(R.id.aPlazo);
        rb_contado= view.findViewById(R.id.aContado);
        rbMasculino= view.findViewById(R.id.masculino);
        rbFemenino= view.findViewById(R.id.femenino);
//        rbViviendaBs=view.findViewById(R.id.viviendaBs);
//        rbViviendaDolar=view.findViewById(R.id.viviendaDolar);
        rbIngresosBs=view.findViewById(R.id.ingresosBs);
        rbIngresosDolar=view.findViewById(R.id.ingresosDolar);
        rbconUbicacion = view.findViewById(R.id.conUbicacion);
        rbsinUbicacion=view.findViewById(R.id.sinUbicacion);
        rbIndependiente=view.findViewById(R.id.independiente);
        rbDependiende=view.findViewById(R.id.dependiente);



//        spinner_urbanizacion= view.findViewById(R.id.urbanizacion);
        spinnerIdentificacion= view.findViewById(R.id.tipoIdentificacion);
        spinnerEstadoCivil= view.findViewById(R.id.estadoCivil);
        spinnerNivelEstudio= view.findViewById(R.id.nivelEstudio);
        spinnerTipoVivienda= view.findViewById(R.id.tipoVivienda);
        spinnerDpto= view.findViewById(R.id.dptoBolivia);
        spinnerTenencia= view.findViewById(R.id.tenencia);
        spinnerPrefijo=view.findViewById(R.id.tipoPrefijo);
        spinnerExtension=view.findViewById(R.id.spinnerExtension);
        spinnerMoneda=view.findViewById(R.id.spinnerMoneda);
        spinnerPlazo= view.findViewById(R.id.cuotasplazo);
        spinnerReserva = view.findViewById(R.id.spinnerReserva);
        spinnerProyectos = view.findViewById(R.id.urbanizacionproyecto);

        codigo_proyecto = view.findViewById(R.id.idProyecto);
        tvfechaNacimiento = view.findViewById(R.id.fechaNacimiento);
        tvplazo = view.findViewById(R.id.tvplazo);
        guardar = view.findViewById(R.id.btguardar);
        btFechaNac = view.findViewById(R.id.btDatePickerFechaNac);
        cargar = view.findViewById(R.id.btcargarformulario);
//        registrarForm = view.findViewById((R.id.btRegistrarDatosForm));
        //fechaNac.setText(fechaHoy());

        spinnerProyectos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codigo_proyecto.setText(listProyectos.get(position).getCodigo());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        codigo_asesor.setText(Global.codigo.toString());
        asesor.setText(Global.nombreSesion+" "+Global.apellidoSesion);

        /////cargamos los spinners
        cargarComponentes();
        cargarFechaFormulario();
//        cargarListaUrbanizacion();

        ////boton fecha nacimiento
        btFechaNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
            }
        });
        iniciarDatePicker();

        ////funcion de los Radiobutton y radioGroups
        rb_plazo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvplazo.setVisibility(View.VISIBLE);
                spinnerPlazo.setVisibility(View.VISIBLE);
                RBseleccionado(v);
            }
        });
        rb_contado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPlazo.setSelection(0);
                tvplazo.setVisibility(View.GONE);
                spinnerPlazo.setVisibility(View.GONE);
                RBseleccionado(v);
            }
        });
        rbMasculino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbSeleccionGenero(v);
            }

        });
        rbFemenino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbSeleccionGenero(v);
            }
        });
//        rbViviendaBs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rbMonedaVivienda(v);
//            }
//        });
//        rbViviendaDolar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rbMonedaVivienda(v);
//            }
//        });
        rbIngresosBs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbSeleccionIngresos(v);
            }
        });
        rbIngresosDolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbSeleccionIngresos(v);
            }
        });
        rbconUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeleccionSinConUbicacion(v);
            }
        });
        rbsinUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeleccionSinConUbicacion(v);
            }
        });
        rbDependiende.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeleccionIndependienteDependiente(view);
            }
        });
        rbIndependiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeleccionIndependienteDependiente(view);
            }
        });

//        spinner_urbanizacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                codigo_proyecto.setText(listaProyectos.get(position).getCodigo().toString());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validarCamposObligatorios()){
                    if(validarForm()){
                        if(EsDependiente()){
                            DeshabilitarBoton();
                            new Handler().postDelayed(new Runnable(){
                                public void run(){
                                    if(registrarDatos()){
                                        generarPDF();
                                    }else {
                                        Toast.makeText(getContext(), "Error, revisar mis formularios", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, 1000); //1000 millisegundos = 1 segundo.
                            Toast.makeText(getContext(), "Generando Formularios verifique bien todo los datos...", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getContext(), "Rellene Nombre Empresa y Direccion Empresa.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
//                        Toast.makeText(getContext(), "Rellene los campos marcados en *.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if(!Utils.isPermissionGranted(getContext())){
//            new AlertDialog.Builder(getContext()).setTitle("Permiso de aplicacion").setMessage("Debido a la version de android es necesario otorgar permisos").setPositiveButton("Permitir", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    otorgarPermisos();
//                }
//            }).setNegativeButton("Denegar", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            }).setIcon(R.drawable.casita_sola).show();
//        }
//        else{
//            mensaje("Permisos de aplicacion ya otorgados");
//        }
//    }

//    private void otorgarPermisos(){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
//            try {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                intent.addCategory("android.intent.category.DEFAULT");
//                Uri uri = Uri.fromParts("package",getActivity().getPackageName(),null);
//                intent.setData(uri);
//                startActivityForResult(intent,101);
//
//            }catch (Exception e){
//                e.printStackTrace();
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                startActivityForResult(intent,101);
//
//            }
//        }else  {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{
//                    Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
//            },101);
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(grantResults.length>0){
//
//            if(requestCode==101){
//
//                boolean readExt  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                if(!readExt){
//                    otorgarPermisos();
//                }
//            }
//
//
//
//        }
//    }

    public Boolean registrarDatos(){
        registrarTitular();
        return true;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.formularios_fragment, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FormulariosViewModel.class);
        // TODO: Use the ViewModel
    }

    public void cargarFechaFormulario(){
        Calendar cal = Calendar.getInstance();
        Integer year2 = cal.get(Calendar.YEAR);
        year.setText(year2.toString());
        Integer month2 = cal.get(Calendar.MONTH);
        month2=month2+1;
        month.setText(month2.toString());
        Integer day2= cal.get(Calendar.DAY_OF_MONTH);
        day.setText(day2.toString());
    }

    public void cargarComponentes(){

        getUrbanizacionProyectos();

        metodos.cargarExtensiones(listaExtension);
        ArrayAdapter<String> adapterExtension = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaExtension);
        spinnerExtension.setAdapter(adapterExtension);

        metodos.cargarMoneda(listaMoneda);
        ArrayAdapter<String> adapterMoneda = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaMoneda);
        spinnerMoneda.setAdapter(adapterMoneda);

        metodos.cargarListaPlazo(listaPlazo);
        ArrayAdapter<String> adapterPlazo = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaPlazo);
        spinnerPlazo.setAdapter(adapterPlazo);

        metodos.cargarTipoIdentificacion(listaIdentificacion);
        ArrayAdapter<String> adapterTipoIdent = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaIdentificacion);
        spinnerIdentificacion.setAdapter(adapterTipoIdent);

        metodos.cargarEstadoCivil(listaEstadoCivil);
        ArrayAdapter<String> adapterEstadoCivil = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaEstadoCivil);
        spinnerEstadoCivil.setAdapter(adapterEstadoCivil);

        metodos.cargarNivelEstudio(listaNivelEstudio);
        ArrayAdapter<String> adapterNivelEstudio = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaNivelEstudio);
        spinnerNivelEstudio.setAdapter(adapterNivelEstudio);

        metodos.cargarTipoVivienda(listaTipoVivienda);
        ArrayAdapter<String> adapterTipoVivienda = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaTipoVivienda);
        spinnerTipoVivienda.setAdapter(adapterTipoVivienda);

        metodos.cargarDpto(listaDpto);
        ArrayAdapter<String> adapterDpto = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaDpto);
        spinnerDpto.setAdapter(adapterDpto);

        metodos.cargarTenencia(listaTenencia);
        ArrayAdapter<String> adapterTenencia = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaTenencia);
        spinnerTenencia.setAdapter(adapterTenencia);

        metodos.cargarPrefijo(listaPrefijo);
        ArrayAdapter<String> adapterPrefijo = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaPrefijo);
        spinnerPrefijo.setAdapter(adapterPrefijo);

        metodos.cargarReserva(listaReserva);
        ArrayAdapter<String> adapterReserva = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaReserva);
        spinnerReserva.setAdapter(adapterReserva);

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

    public void DeshabilitarBoton(){
        guardar.setEnabled(false);
    }
    public void habilitarBoton(){
        guardar.setEnabled(true);
    }

    private void registrarTitular() {
        StringRequest request = new StringRequest(Request.Method.POST, URL_addtitular, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (!response.isEmpty()){
                        if(response.contains("algo salio mal")){
                            Toast.makeText(getContext(),"No se pudo completar el registro debido a un error",Toast.LENGTH_LONG).show();
                        }
                        else{Toast.makeText(getContext(),"Datos registrados",Toast.LENGTH_LONG).show();}

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
                    parametros.put("nombres",nombre_cliente.getText().toString());
                    parametros.put("apellidoP",apellidoPaterno.getText().toString());
                    parametros.put("apellidoM",apellidoMaterno.getText().toString());
                    parametros.put("apellidoC",apellidoCasada.getText().toString());
                    parametros.put("prefijo",spinnerPrefijo.getSelectedItem().toString());
                    parametros.put("tipo_identificacion",spinnerIdentificacion.getSelectedItem().toString());
                    parametros.put("nro_documento",ci_cliente.getText().toString());
                    parametros.put("extension",spinnerExtension.getSelectedItem().toString());
                    parametros.put("nacionalidad",nacionalidad.getText().toString());
                    parametros.put("fecha_nacimiento",fechaNacBD);
                    parametros.put("estado_civil",spinnerEstadoCivil.getSelectedItem().toString());
                    parametros.put("sexo",rbSelectedGenero.getText().toString());
                    parametros.put("nivel_estudio",spinnerNivelEstudio.getSelectedItem().toString());
                    parametros.put("profesion_ocupacion",profesion.getText().toString());
                    parametros.put("telf_fijo",telFijo.getText().toString());
                    parametros.put("telf_movil",telMovil.getText().toString());
                    parametros.put("telf_fijoOficina",telFijoOfc.getText().toString());
                    parametros.put("telf_movilOficina",telMovOfc.getText().toString());
                    parametros.put("correo",correoPersonal.getText().toString());
                    parametros.put("referencia1",primerReferencia.getText().toString());
                    parametros.put("relacion1",parentesco.getText().toString());
                    parametros.put("telf_referencia1",telfReferencia1.getText().toString());
                    parametros.put("referencia2",segundaReferencia.getText().toString());
                    parametros.put("relacion2",relacion.getText().toString());
                    parametros.put("telf_referencia2",telfReferencia2.getText().toString());
                    parametros.put("tipo_vivienda",spinnerTipoVivienda.getSelectedItem().toString());
                    parametros.put("tenencia",spinnerTenencia.getSelectedItem().toString());
                    parametros.put("costo_vivienda",costoAprox.getText().toString());
                    parametros.put("moneda_costoVivienda",spinnerMoneda.getSelectedItem().toString());
                    parametros.put("propietario_vivienda",propietarioVivienta.getText().toString());
                    parametros.put("telf_propietario",telefonoPropietario.getText().toString());
                    parametros.put("pais_vivienda",pais.getText().toString());
                    parametros.put("departamento",spinnerDpto.getSelectedItem().toString());
                    parametros.put("zona",zona.getText().toString());
                    parametros.put("ciudad",ciudad.getText().toString());
                    parametros.put("barrio",barrio.getText().toString());
                    parametros.put("avenida",avenida.getText().toString());
                    parametros.put("calle",calle.getText().toString());
                    parametros.put("numero",numero.getText().toString());
                    parametros.put("nombre_empresa",nombreEmpresa.getText().toString());
                    parametros.put("direccion_empresa",direccionEmpresa.getText().toString());
                    parametros.put("rubro",rubroEmpresa.getText().toString());
                    parametros.put("ingresos",ingresosEmpresa.getText().toString());
                    parametros.put("moneda_ingresos",rbSelectedIngresos.getText().toString());
                    parametros.put("proyecto",codigo_proyecto.getText().toString());
                    parametros.put("urbanizacion",spinnerProyectos.getSelectedItem().toString());
                    parametros.put("uv",uv.getText().toString());
                    parametros.put("mz",mz.getText().toString());
                    parametros.put("lt",lt.getText().toString());
                    parametros.put("cat",cat.getText().toString());
                    parametros.put("metros2",mts2.getText().toString());
                    parametros.put("tipo_venta",rbSelected.getText().toString());
                    parametros.put("cuotas",spinnerPlazo.getSelectedItem().toString());
                    parametros.put("asesor",asesor.getText().toString());
                    parametros.put("codigo_asesor",codigo_asesor.getText().toString());
                    parametros.put("observacion",observacion1.getText().toString());
                    parametros.put("observacion2",observacion2.getText().toString());
                    parametros.put("latitud",Global.gLat.toString());
                    parametros.put("longitud",Global.gLong.toString());
                    if(Global.gLong==0.0 && Global.gLat==0.0){
                        parametros.put("ubicacion","");
                    }else{
                        parametros.put("ubicacion",ubicacion+Global.gLat.toString()+","+Global.gLong.toString());
                    }
                    parametros.put("fecha",fechaHoyBase());

                    return parametros;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(request);
            habilitarBoton();
    }

    public String tresDigitos(String numero){
        int digitos = numero.length();
        String result="";
        switch (digitos){
            case 1:
                result="00"+numero;
                break;
            case 2:
                result="0"+numero;
                break;
            case 3:
                result=numero;
                break;
            case 4:
                result=numero;
                break;
        }
        return result;
    }

    public Boolean validarForm(){
        Boolean valor;
        valor=false;
        if (rbconUbicacion.isChecked()||rbsinUbicacion.isChecked()) {
            if (rb_contado.isChecked() || rb_plazo.isChecked()) {
                if (rbMasculino.isChecked() || rbFemenino.isChecked()) {
                    if (rbIngresosBs.isChecked() || rbIngresosDolar.isChecked()) {
                            if(rbIndependiente.isChecked()|| rbDependiende.isChecked()){
                                if(rb_plazo.isChecked()){
                                    if(spinnerPlazo.getSelectedItem().toString().contains("--")){
                                        mensaje("Seleccione un plazo para venta");
                                        return false;
                                    }else {
                                        return true;
                                    }
                                }else{
                                    return true;
                                }
                            }else {
                                Toast.makeText(getContext(), "Falta seleccionar cliente Independiente o Dependiente", Toast.LENGTH_SHORT).show();
                            }
                    } else {
                        Toast.makeText(getContext(), "Falta seleccionar Ingresos en Bs o $us.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Falta seleccionar Masculino o Femenino.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Falta seleccionar plazo o contado.", Toast.LENGTH_SHORT).show();
            }
        }else { Toast.makeText(getContext(), "Falta seleccionar formulario con Ubicacion o Sin ubicacion.", Toast.LENGTH_SHORT).show();}
        return valor;
    }
    public void mensaje(String mensaje){
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
    }

    public Boolean validarCamposObligatorios() {
        if (nombre_cliente.getText().toString().length() == 0) {
            mensaje("Falta nombre cliente");
            return false;
        } else {
            if (ci_cliente.getText().toString().length() == 0) {
                mensaje("Falta documento de identidad");
                return false;
            } else {
                if (nacionalidad.getText().toString().length() == 0) {
                    mensaje("Falta Nacionalidad");
                    return false;
                } else {
                    if (profesion.getText().toString().length() == 0) {
                        mensaje("Falta profesion");
                        return false;
                    } else {
                        if (pais.getText().toString().length() == 0) {
                            mensaje("Falta pais");
                            return false;
                        } else {
                            if (ciudad.getText().toString().length() == 0) {
                                mensaje("Falta ciudad");
                                return false;
                            } else {
                                if (barrio.getText().toString().length() == 0) {
                                    mensaje("Falta rellenar barrio");
                                    return false;
                                } else {
                                    if (telMovil.getText().toString().length() == 0) {
                                        mensaje("Falta Telefono Movil");
                                        return false;
                                    } else {
                                        if (rubroEmpresa.getText().toString().length() == 0) {
                                            mensaje("Falta rubro");
                                            return false;
                                        } else {
                                            if (ingresosEmpresa.getText().toString().length() == 0) {
                                                mensaje("Falta ingresos");
                                                return false;
                                            } else {
                                                if (primerReferencia.getText().toString().length() == 0) {
                                                    mensaje("Falta primer referencia");
                                                    return false;
                                                } else {
                                                    if (relacion.getText().toString().length() == 0) {
                                                        mensaje("Falta relacion de referencia");
                                                        return false;
                                                    } else {
                                                        if (telfReferencia1.getText().toString().length() == 0) {
                                                            mensaje("Falta telefono referencia 1");
                                                            return false;
                                                        } else {
                                                            if (segundaReferencia.getText().toString().length() == 0) {
                                                                mensaje("Falta segunda referencia");
                                                                return false;
                                                            } else {
                                                                if (parentesco.getText().toString().length() == 0) {
                                                                    mensaje("Falta parentesco de referencia ");
                                                                    return false;
                                                                } else {
                                                                    if (telfReferencia2.getText().toString().length() == 0) {
                                                                        mensaje("Falta telefono referencia 2");
                                                                        return false;
                                                                    } else {
                                                                        if (uv.getText().toString().length() == 0) {
                                                                            mensaje("Falta UV");
                                                                            return false;
                                                                        } else {
                                                                            if (mz.getText().toString().length() == 0) {
                                                                                mensaje("Falta MZ");
                                                                                return false;
                                                                            } else {
                                                                                if (lt.getText().toString().length() == 0) {
                                                                                    mensaje("Falta LT");
                                                                                    return false;
                                                                                } else {
                                                                                    if (cat.getText().toString().length() == 0) {
                                                                                        mensaje("Falta Categoria");
                                                                                        return false;
                                                                                    } else {
                                                                                        if (asesor.getText().toString().length() == 0) {
                                                                                            mensaje("Falta Asesor");
                                                                                            return false;
                                                                                        } else {
                                                                                            if (codigo_asesor.getText().toString().length() == 0) {
                                                                                                mensaje("Falta codigo asesor");
                                                                                                return false;
                                                                                            } else {
                                                                                                if (mts2.getText().toString().length() == 0) {
                                                                                                    mensaje("Falta ingresar superficie metros2");
                                                                                                    return false;
                                                                                                } else {
                                                                                                    if(tvfechaNacimiento.getText().toString().contains("Presionar boton Fecha")){
                                                                                                        mensaje("Colocar fecha de nacimiento del cliente");
                                                                                                        return false;
                                                                                                    }else{
                                                                                                        return true;
                                                                                                    }

                                                                                                }

                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }

                }
            }

        }
    }

    public Boolean EsDependiente(){
        if (rbSelectedIndependienteDependiente.getText().toString().contains("Dependiente")){
            if (nombreEmpresa.getText().toString().length()==0||
                    direccionEmpresa.getText().toString().length()==0){
                return false;
            }else {
                return true;
            }
        }else {
            return true;
        }
    }

    private String fechaHoyBase(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month=month+1;
        int day= cal.get(Calendar.DAY_OF_MONTH);
        String now = makeDateStringBase(day,month,year);
        return now;
    }
    private String makeDateString(Integer day, Integer month, Integer year) {
        String day2=day.toString();
        String month2=month.toString();
        if(day.toString().length()==1){
            day2="0"+day;
        }
        if(month.toString().length()==1){
            month2="0"+month;
        }
        return day2+"/"+month2+"/" + year;
    }
    private String makeDateStringBase(int day, int month, int year) {
        return year+"-"+month+"-"+day;
    }

    private void iniciarDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = makeDateString(day,month,year);
                tvfechaNacimiento.setText(date);
                fechaNacBD=makeDateStringBase(day,month,year);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day= cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getContext(),style,dateSetListener,year,month,day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }


    public void openDatePicker(View v){
        datePickerDialog.show();
    }


    public void RBseleccionado(View v){
        int radiobtid = radioGroup.getCheckedRadioButtonId();
        rbSelected = v.findViewById(radiobtid);
    }

    private void rbSeleccionGenero(View v) {
        int radiobtid = radioGroupGenero.getCheckedRadioButtonId();
        rbSelectedGenero = v.findViewById(radiobtid);
    }

//    private void rbMonedaVivienda(View v) {
//        int radiobtid = radioGroupVivienda.getCheckedRadioButtonId();
//        rbSelectedMonedaVivienda = v.findViewById(radiobtid);
//    }
    private void rbSeleccionIngresos(View v) {
        int radiobtid = radioGroupIngresos.getCheckedRadioButtonId();
        rbSelectedIngresos = v.findViewById(radiobtid);
    }
    private void SeleccionSinConUbicacion(View v) {
        int radiobtid = radioGroupSinConUbicacion.getCheckedRadioButtonId();
        rbSelectedSinConUbicacion = v.findViewById(radiobtid);
    }
    private void SeleccionIndependienteDependiente(View v) {
        int radiobtid = radioGroupIndependienteDependiente.getCheckedRadioButtonId();
        rbSelectedIndependienteDependiente = v.findViewById(radiobtid);
    }

//    public void cargarListaUrbanizacion(){
//        listaProyectos.add(new Proyectos(100,"LA ENCONADA II"));
//        listaProyectos.add(new Proyectos(101,"LA PASCANA DE COTOCA"));
//        listaProyectos.add(new Proyectos(200,"LA PASCANA DE COTOCA II"));
//        listaProyectos.add(new Proyectos(201,"LA TIERRA PROMETIDA"));
//        listaProyectos.add(new Proyectos(204,"AME TAUNA"));
//        listaProyectos.add(new Proyectos(205,"AME TAUNA I"));
//        listaProyectos.add(new Proyectos(206,"LA PASCANA DE COTOCA III"));
//        listaProyectos.add(new Proyectos(207,"LA PASCANA DE COTOCA IV"));
//        listaProyectos.add(new Proyectos(208,"LA PASCANA DE COTOCA V"));
//        listaProyectos.add(new Proyectos(209,"LA PASCANA DE COTOCA VI"));
//        listaProyectos.add(new Proyectos(210,"LA PASCANA DE COTOCA VII"));
//        listaProyectos.add(new Proyectos(212,"LA PASCANA DE COTOCA I"));
//        ArrayAdapter<Proyectos> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaProyectos);
//        spinner_urbanizacion.setAdapter(adapter);
//    }


    public String mesLiteral(Integer mes){
        String result="";
        switch (mes){
            case 1:
                result="Enero";
                break;
            case 2:
                result="Febrero";
                break;
            case 3:
                result="Marzo";
                break;
            case 4:
                result="Abril";
                break;
            case 5:
                result="Mayo";
                break;
            case 6:
                result="Junio";
                break;
            case 7:
                result="Julio";
                break;
            case 8:
                result="Agosto";
                break;
            case 9:
                result="Septiembre";
                break;
            case 10:
                result="Octubre";
                break;
            case 11:
                result="Noviembre";
                break;
            case 12:
                result="Diciembre";
                break;
        }
        return result;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /////boton generador de pdf/////
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void generarPDF (){
        String prefijoObtenido,monedaCostoAproximado, extensionObtenida;
        String plazoContado = rbSelected.getText().toString();
//        Calendar cal = Calendar.getInstance();
//        Integer year = cal.get(Calendar.YEAR);
//        Integer month = cal.get(Calendar.MONTH);
//        month=month+1;
//        Integer day= cal.get(Calendar.DAY_OF_MONTH);
        String fechaActual = makeDateString(Integer.valueOf(day.getText().toString()),Integer.valueOf(month.getText().toString()),Integer.valueOf(year.getText().toString()));
//        String fechaActual = makeDateString(day,month,year);
//            String fechaActual= day.toString()+"/"+month.toString()+"/"+year.toString();

        PdfDocument myPDF = new PdfDocument();
        Paint myPaint = new Paint();
        Paint titlePaint = new Paint();

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(20f);
        myPaint.setColor(Color.BLACK);
        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));

        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTextSize(20f);
        titlePaint.setColor(Color.BLACK);

        if(spinnerPrefijo.getSelectedItem().toString().contains("Ninguno")){
            prefijoObtenido="";
        }else {
            prefijoObtenido=spinnerPrefijo.getSelectedItem().toString();
        }
        if(spinnerMoneda.getSelectedItem().toString().contains("Ninguno")){
            monedaCostoAproximado="";
        }else{
            monedaCostoAproximado=spinnerMoneda.getSelectedItem().toString();
        }
        if(spinnerExtension.getSelectedItem().toString().contains("--")){
            extensionObtenida="";
        }else{
            extensionObtenida=spinnerExtension.getSelectedItem().toString();
        }
        //verificamos los campos numericos si estan vacios se pondran 0
        //numerosVacios();

        ////definimos pagina 1
//        PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(2539,3874,1).create();
        PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(1200,1927,1).create();
        PdfDocument.Page myPage1 = myPDF.startPage(myPageInfo1);
        Canvas canvas = myPage1.getCanvas();

//        imagen = BitmapFactory.decodeResource(getResources(),R.drawable.f1png);
        imagen = decodeSampledBitmapFromResource(getResources(),R.drawable.f1png,1200,1927);
        scaled = Bitmap.createScaledBitmap(imagen,1200,1927,false);
//        scaled = Bitmap.createScaledBitmap(imagen,2539,3874,false);

        canvas.drawBitmap(scaled,0,0,myPaint);

        Bitmap check,scaledcheck;
        check = BitmapFactory.decodeResource(getResources(),R.drawable.check1);
        scaledcheck = Bitmap.createScaledBitmap(check,45,45,false);

        canvas.drawText(spinnerProyectos.getSelectedItem().toString(),450,154,titlePaint);
        canvas.drawText(nombre_cliente.getText().toString().toUpperCase()+" "+apellidoPaterno.getText().toString().toUpperCase()+" "+apellidoMaterno.getText().toString().toUpperCase()+" "+prefijoObtenido.toUpperCase()+" "+apellidoCasada.getText().toString().toUpperCase(),270,229,titlePaint);
        canvas.drawText(ci_cliente.getText().toString(),352,289,titlePaint);
        canvas.drawText(extensionObtenida,705,289,titlePaint);

        if(plazoContado.contains("A plazo")){
            canvas.drawBitmap(scaledcheck,480,319,myPaint);
        }else {
            canvas.drawBitmap(scaledcheck,690,319,myPaint);
        }
        canvas.drawText(codigo_proyecto.getText().toString(),195,412,titlePaint);
        canvas.drawText(tresDigitos(uv.getText().toString()),402,412,titlePaint);
        canvas.drawText(tresDigitos(mz.getText().toString()),630,412,titlePaint);
        canvas.drawText(tresDigitos(lt.getText().toString()),825,412,titlePaint);
        canvas.drawText(cat.getText().toString(),1074,412,titlePaint);
        canvas.drawText(asesor.getText().toString().toUpperCase(),120,495,titlePaint);
        canvas.drawText(codigo_asesor.getText().toString(),825,495,titlePaint);

        myPDF.finishPage(myPage1);
        //// FIN PAGINA 1/////

        //////PAGINA 2
//        PdfDocument.PageInfo myPageInfo2 = new PdfDocument.PageInfo.Builder(2539,3874,1).create();
        PdfDocument.PageInfo myPageInfo2 = new PdfDocument.PageInfo.Builder(1200,1927,1).create();
        PdfDocument.Page myPage2 = myPDF.startPage(myPageInfo2);
        Canvas canvas2 = myPage2.getCanvas();

//        imagen = BitmapFactory.decodeResource(getResources(),R.drawable.f4png);
        imagen = decodeSampledBitmapFromResource(getResources(),R.drawable.f4png,1200,1927);
        scaled = Bitmap.createScaledBitmap(imagen,1200,1927,false);
//        scaled = Bitmap.createScaledBitmap(imagen,2539,3874,false);

        canvas2.drawBitmap(scaled,0,0,myPaint);

        canvas2.drawText(apellidoPaterno.getText().toString().toUpperCase(),120,255,titlePaint);
        canvas2.drawText(apellidoMaterno.getText().toString().toUpperCase(),675,255,titlePaint);
        canvas2.drawText(nombre_cliente.getText().toString().toUpperCase(),120,333,titlePaint);
        canvas2.drawText(apellidoCasada.getText().toString().toUpperCase(),675,333,titlePaint);
        canvas2.drawText(prefijoObtenido,975,333,titlePaint);
        canvas2.drawText(ci_cliente.getText().toString(),675,415,titlePaint);
        canvas2.drawText(extensionObtenida,982,415,titlePaint);
//            if(spinnerPrefijo.getSelectedItem().toString().contains("Ninguno")){
//                canvas2.drawText("",2060,750,titlePaint);
//            }else {
//                canvas2.drawText(spinnerPrefijo.getSelectedItem().toString(),2060,750,titlePaint);
//            }

        canvas2.drawText(spinnerIdentificacion.getSelectedItem().toString(),292,399,titlePaint);
        canvas2.drawText(nacionalidad.getText().toString(),292,423,titlePaint);
        canvas2.drawText(tvfechaNacimiento.getText().toString(),292,448,titlePaint);
        canvas2.drawText(spinnerEstadoCivil.getSelectedItem().toString(),292,472,titlePaint);
        canvas2.drawText(rbSelectedGenero.getText().toString(),292,495,titlePaint);
        canvas2.drawText(spinnerNivelEstudio.getSelectedItem().toString(),292,519,titlePaint);
        canvas2.drawText(profesion.getText().toString(),292,544,titlePaint);
        canvas2.drawText(spinnerTipoVivienda.getSelectedItem().toString(),525,571,titlePaint);
        canvas2.drawText(spinnerTenencia.getSelectedItem().toString(),810,571,titlePaint);
        canvas2.drawText(costoAprox.getText().toString()+" "+monedaCostoAproximado,120,633,titlePaint);
        canvas2.drawText(propietarioVivienta.getText().toString().toUpperCase(),345,633,titlePaint);
        canvas2.drawText(telefonoPropietario.getText().toString(),930,633,titlePaint);

        canvas2.drawText(pais.getText().toString(),120,699,titlePaint);
        if(spinnerDpto.getSelectedItem().toString().contains("Ninguno")){
            canvas2.drawText("",412,699,titlePaint);
        }else {
            canvas2.drawText(spinnerDpto.getSelectedItem().toString(),412,699,titlePaint);
        }
        canvas2.drawText(ciudad.getText().toString(),120,759,titlePaint);
        canvas2.drawText(barrio.getText().toString(),600,759,titlePaint);
        canvas2.drawText(avenida.getText().toString(),120,834,titlePaint);
        canvas2.drawText(calle.getText().toString(),600,834,titlePaint);
        canvas2.drawText(numero.getText().toString(),990,834,titlePaint);

        canvas2.drawText(telFijo.getText().toString(),105,933,titlePaint);
        canvas2.drawText(telFijoOfc.getText().toString(),105,1008,titlePaint);
        canvas2.drawText(telMovil.getText().toString(),337,933,titlePaint);
        canvas2.drawText(telMovOfc.getText().toString(),337,1008,titlePaint);
        canvas2.drawText(correoPersonal.getText().toString(),105,1084,titlePaint);

        canvas2.drawText(nombreEmpresa.getText().toString(),588,930,titlePaint);
        canvas2.drawText(direccionEmpresa.getText().toString(),588,1008,titlePaint);
        canvas2.drawText(rubroEmpresa.getText().toString(),588,1084,titlePaint);
        canvas2.drawText(ingresosEmpresa.getText().toString()+" "+rbSelectedIngresos.getText().toString(),975,1084,titlePaint);

        canvas2.drawText(primerReferencia.getText().toString().toUpperCase(),88,1180,titlePaint);
        canvas2.drawText(segundaReferencia.getText().toString().toUpperCase(),88,1252,titlePaint);
        canvas2.drawText(parentesco.getText().toString(),592,1180,titlePaint);
        canvas2.drawText(relacion.getText().toString(),592,1252,titlePaint);
        canvas2.drawText(telfReferencia1.getText().toString(),975,1180,titlePaint);
        canvas2.drawText(telfReferencia2.getText().toString(),975,1252,titlePaint);
        titlePaint.setTextSize(18f);
        canvas2.drawText(observacion1.getText().toString().toUpperCase(),225,1630,titlePaint);
        canvas2.drawText(observacion2.getText().toString().toUpperCase(),225,1657,titlePaint);
        titlePaint.setTextSize(15f);
        canvas2.drawText(nombre_cliente.getText().toString().toUpperCase()+" "
                        +apellidoPaterno.getText().toString().toUpperCase()+" "
                        +apellidoMaterno.getText().toString().toUpperCase()+" "+prefijoObtenido.toUpperCase()+" "+apellidoCasada.getText().toString().toUpperCase()
                ,184,1808,titlePaint);
        canvas2.drawText(asesor.getText().toString().toUpperCase(),835,1808,titlePaint);
        titlePaint.setTextSize(20f);

        myPDF.finishPage(myPage2);
        //////FIN PAGINA 2 /////
//
//
//        ////// INICIA PAGINA 3 ///////
        PdfDocument.PageInfo myPageInfo3 = new PdfDocument.PageInfo.Builder(1200,1927,1).create();
//        PdfDocument.PageInfo myPageInfo3 = new PdfDocument.PageInfo.Builder(2539,3874,1).create();
        PdfDocument.Page myPage3 = myPDF.startPage(myPageInfo3);
        Canvas canvas3 = myPage3.getCanvas();
//        imagen = BitmapFactory.decodeResource(getResources(),R.drawable.f3png);
        imagen = decodeSampledBitmapFromResource(getResources(),R.drawable.f3png,1200,1927);
        //        scaled = Bitmap.createScaledBitmap(imagen,2539,3874,false);
        scaled = Bitmap.createScaledBitmap(imagen,1200,1927,false);


//          bmp = BitmapFactory.decodeResource(getResources(),R.drawable.newentrega);

        canvas3.drawBitmap(scaled,0,0,myPaint);
        myPaint.setTextAlign(Paint.Align.CENTER);
        myPaint.setTextSize(30f);
        myPaint.setColor(Color.BLACK);
        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        canvas3.drawText(spinnerProyectos.getSelectedItem().toString(),330,450,myPaint);
        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(28f);
        canvas3.drawText(day.getText().toString(),108,285,myPaint);
        canvas3.drawText(month.getText().toString(),225,285,myPaint);
        canvas3.drawText(year.getText().toString(),313,285,myPaint);
        titlePaint.setTextSize(26f);
        canvas3.drawText(codigo_proyecto.getText().toString(),135,592,titlePaint);
        canvas3.drawText(tresDigitos(uv.getText().toString()),360,592,titlePaint);
        canvas3.drawText(tresDigitos(mz.getText().toString()),570,592,titlePaint);
        canvas3.drawText(tresDigitos(lt.getText().toString()),787,592,titlePaint);
        canvas3.drawText(cat.getText().toString().toUpperCase(),1027,592,titlePaint);
        canvas3.drawText(rbSelected.getText().toString(),120,780,titlePaint);
        canvas3.drawText(mts2.getText().toString(),345,780,titlePaint);
        if(plazoContado.contains("Contado")){
            canvas3.drawText("",790,780,titlePaint);
        }else {
            canvas3.drawText(spinnerPlazo.getSelectedItem().toString(),790,780,titlePaint);
        }
        titlePaint.setTextSize(16f);
//        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        canvas3.drawText(nombre_cliente.getText().toString().toUpperCase()+" "+apellidoPaterno.getText().toString().toUpperCase()+" "+
                apellidoMaterno.getText().toString().toUpperCase()+" "+prefijoObtenido.toUpperCase()+" "+apellidoCasada.getText().toString().toUpperCase(),107,867,titlePaint);
        titlePaint.setTextSize(17f);
        canvas3.drawText(ci_cliente.getText().toString(),945,867,titlePaint);
        canvas3.drawText(extensionObtenida,132,895,titlePaint);
        titlePaint.setTextSize(16f);
        canvas3.drawText(nombre_cliente.getText().toString().toUpperCase()+" "
                        +apellidoPaterno.getText().toString().toUpperCase()+" "
                        +apellidoMaterno.getText().toString().toUpperCase()+" "+prefijoObtenido.toUpperCase()+" "+apellidoCasada.getText().toString().toUpperCase(),
                202,1470,titlePaint);
//            canvas3.drawText(asesor.getText().toString(),1910,2960,titlePaint);

        myPDF.finishPage(myPage3);
        /////FIN PAGINA 3/////////

        ///INICIO DE PAGINA 4 ////

        if(rbSelectedSinConUbicacion.getText().toString().contains("Si")){
            if(Global.ubicacion==null){
                titlePaint.setTextSize(20f);
                mensaje("No se definio una ubicacion");
            }else{
                //        PdfDocument.PageInfo myPageInfo4 = new PdfDocument.PageInfo.Builder(2539,3874,1).create();
                titlePaint.setTextSize(20f);
                PdfDocument.PageInfo myPageInfo4 = new PdfDocument.PageInfo.Builder(1200,1927,1).create();
                PdfDocument.Page myPage4 = myPDF.startPage(myPageInfo4);
                Canvas canvas4 = myPage4.getCanvas();
//        imagen = BitmapFactory.decodeResource(getResources(),R.drawable.mapapng);
                imagen = decodeSampledBitmapFromResource(getResources(),R.drawable.mapapng,1200,1927);

//        scaled = Bitmap.createScaledBitmap(imagen,2539,3874,false);
                scaled = Bitmap.createScaledBitmap(imagen,1200,1927,false);
//            Bitmap imagen4,scaled4;
//            imagen4 = BitmapFactory.decodeResource(getResources(),R.drawable.nuevoformmapa);
//            scaled4 = Bitmap.createScaledBitmap(imagen4,2539,3874,false);
                canvas4.drawBitmap(scaled,0,0,myPaint);
                canvas4.drawText(tresDigitos(uv.getText().toString()),255,228,titlePaint);
                canvas4.drawText(tresDigitos(mz.getText().toString()),555,228,titlePaint);
                canvas4.drawText(tresDigitos(lt.getText().toString()),817,228,titlePaint);
                canvas4.drawText(cat.getText().toString(),1050,228,titlePaint);
                canvas4.drawText(nombre_cliente.getText().toString().toUpperCase()+" "+apellidoPaterno.getText().toString().toUpperCase()+" "+apellidoMaterno.getText().toString().toUpperCase()+" "+prefijoObtenido.toUpperCase()+" "+apellidoCasada.getText().toString().toUpperCase(),375,282,titlePaint);
                canvas4.drawText(ci_cliente.getText().toString(),375,315,titlePaint);
                canvas4.drawText(telMovil.getText().toString()+" - "+telFijo.getText().toString(),780,315,titlePaint);
                canvas4.drawText("Barrio:"+" "+barrio.getText().toString()+" Avenida: "+avenida.getText().toString()+" Calle: "+calle.getText().toString()+" Numero: "+numero.getText().toString(),375,348,titlePaint);
                canvas4.drawText(primerReferencia.getText().toString()+" "+telfReferencia1.getText().toString()+" - "+segundaReferencia.getText().toString()+" "+telfReferencia2.getText().toString(),375,382,titlePaint);
                canvas4.drawText(zona.getText().toString(),375,420,titlePaint);
                canvas4.drawText(observacion1.getText().toString().toUpperCase(),120,1725,titlePaint);
                canvas4.drawText(observacion2.getText().toString().toUpperCase(),120,1747,titlePaint);
                scaled = Bitmap.createScaledBitmap(Global.ubicacion,1080,1252,false);
                canvas4.drawBitmap(scaled,67,439,myPaint);
                myPDF.finishPage(myPage4);
            }
        }else{
            titlePaint.setTextSize(20f);
            mensaje("Se eligio sin ubicacion");
        }

        ///FIN DE PAGINA 4/////

        /////pagina 5 reserva/////

        if(spinnerReserva.getSelectedItem().toString().contains("SI")){

//        PdfDocument.PageInfo myPageInfo5 = new PdfDocument.PageInfo.Builder(2539,3874,1).create();
            PdfDocument.PageInfo myPageInfo5 = new PdfDocument.PageInfo.Builder(1200,1927,1).create();
            PdfDocument.Page myPage5 = myPDF.startPage(myPageInfo5);
            Canvas canvas5 = myPage5.getCanvas();
            titlePaint.setTextSize(20f);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
//        imagen = BitmapFactory.decodeResource(getResources(),R.drawable.reservapng);
            imagen = decodeSampledBitmapFromResource(getResources(),R.drawable.reservapng,1200,1927);

//        scaled = Bitmap.createScaledBitmap(imagen,2539,3874,false);
            scaled = Bitmap.createScaledBitmap(imagen,1200,1927,false);

            canvas5.drawBitmap(scaled,0,0,myPaint);

            canvas5.drawText(nombre_cliente.getText().toString().toUpperCase()+" "+apellidoPaterno.getText().toString().toUpperCase()+" "+apellidoMaterno.getText().toString().toUpperCase()+" "+prefijoObtenido.toUpperCase()+" "+apellidoCasada.getText().toString().toUpperCase(),480,393,titlePaint);
            canvas5.drawText(ci_cliente.getText().toString().toUpperCase(),435,429,titlePaint);
            canvas5.drawText(extensionObtenida,780,429,titlePaint);
            canvas5.drawText(spinnerProyectos.getSelectedItem().toString(),120,498,titlePaint);
            canvas5.drawText(codigo_proyecto.getText().toString(),180,532,titlePaint);
            canvas5.drawText(tresDigitos(uv.getText().toString().toUpperCase()),390,534,titlePaint);
            canvas5.drawText(tresDigitos(mz.getText().toString().toUpperCase()),570,534,titlePaint);
            canvas5.drawText(tresDigitos(lt.getText().toString()),742,534,titlePaint);
            canvas5.drawText(cat.getText().toString().toUpperCase(),892,534,titlePaint);
            canvas5.drawText(mts2.getText().toString(),1035,534,titlePaint);
            canvas5.drawText(fechaActual,195,730,titlePaint);
            canvas5.drawText(nombre_cliente.getText().toString().toUpperCase()+" "+apellidoPaterno.getText().toString().toUpperCase()+" "+apellidoMaterno.getText().toString().toUpperCase()+" "+prefijoObtenido.toUpperCase()+" "+apellidoCasada.getText().toString().toUpperCase(),150,1158,titlePaint);
            canvas5.drawText(extensionObtenida,825,1195,titlePaint);
            canvas5.drawText(ci_cliente.getText().toString().toUpperCase(),435,1195,titlePaint);
            canvas5.drawText(day.getText().toString(),585,1263,titlePaint);
            canvas5.drawText(mesLiteral(Integer.valueOf(month.getText().toString())),900,1263,titlePaint);
            canvas5.drawText(year.getText().toString(),195,1297,titlePaint);
            canvas5.drawText(ci_cliente.getText().toString()+" "+extensionObtenida,570,1677,titlePaint);

            myPDF.finishPage(myPage5);
        }else {
            mensaje("Selecciono sin Reserva");
        }

        ////FIN PAGINA 5 ////

        try {
            myPDF.writeTo(new FileOutputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
        }
        myPDF.close();

        Uri uri = FileProvider.getUriForFile(getContext(),"com.example.novitierraapp",file);

        Intent share = new Intent();
        share.setAction(Intent.ACTION_VIEW);
        share.setDataAndType(uri,"application/pdf");
        share.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(share);
        habilitarBoton();



/// ****** este es el bueno para android 9 inferior************
//        String path = Environment.getExternalStorageDirectory()+"/FormularioSolicitante.pdf";
//        File pdf = new File(path);
//        Intent share = new Intent();
//        share.setAction(Intent.ACTION_VIEW);
//        share.setDataAndType(Uri.fromFile(pdf),"application/pdf");
//        share.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


//        share.setAction(Intent.ACTION_SEND);
//        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdf));
//        share.setType("application/pdf");


//        startActivity(share);

    }




}
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
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.os.StrictMode;
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
import com.example.novitierraapp.entidades.Global;
import com.example.novitierraapp.entidades.Proyectos;
import com.google.android.gms.maps.SupportMapFragment;

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
    EditText nombre_cliente, apellidoPaterno, apellidoMaterno, ci_cliente,uv,mz,lt,cat,asesor,codigo_asesor,fechaNac, apellidoCasada,nacionalidad,profesion,costoAprox;
    EditText propietarioVivienta,telefonoPropietario,pais,ciudad,barrio,avenida,calle,numero,telFijo,telMovil,telFijoOfc,telMovOfc,correoPersonal,expedido,mts2;
    EditText nombreEmpresa, rubroEmpresa, direccionEmpresa, ingresosEmpresa,primerReferencia,segundaReferencia,telfReferencia1,telfReferencia2,parentesco,relacion,zona;
    RadioGroup radioGroup, radioGroupGenero, radioGroupVivienda, radioGroupIngresos, radioGroupIndependienteDependiente;
    RadioButton rb_plazo, rb_contado, rbSelected, rbMasculino, rbFemenino, rbSelectedGenero, rbIngresosBs, rbIngresosDolar,rbSelectedIngresos, rbDependiende, rbIndependiente, rbSelectedIndependienteDependiente;
//    rbSelectedMonedaVivienda
//    rbViviendaBs,rbViviendaDolar,
    RadioGroup radioGroupSinConUbicacion;
    RadioButton rbconUbicacion, rbsinUbicacion, rbSelectedSinConUbicacion;
    Spinner spinner_urbanizacion, spinnerIdentificacion,spinnerEstadoCivil,spinnerNivelEstudio, spinnerTipoVivienda, spinnerDpto, spinnerTenencia, spinnerPrefijo, spinnerExtension, spinnerMoneda;
    TextView codigo_proyecto;
    Button guardar, btFechaNac, registrarForm;
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

    Bitmap bmp, scaledbmp;

    private String URL_addtitular="https://novitierra.000webhostapp.com/api/addTitular.php";
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

        nombre_cliente = view.findViewById(R.id.nombreCliente);
        //nombre_cliente.setRawInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        apellidoPaterno= view.findViewById(R.id.apellidoPCliente);
        apellidoMaterno= view.findViewById(R.id.apellidoMCliente);
        apellidoCasada = view.findViewById(R.id.apellidoCasada);
        ci_cliente = view.findViewById(R.id.ciCliente);
        nacionalidad = view.findViewById(R.id.nacionalidad);
        fechaNac = view.findViewById(R.id.fechaNacimiento);
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



        spinner_urbanizacion= view.findViewById(R.id.urbanizacion);
        spinnerIdentificacion= view.findViewById(R.id.tipoIdentificacion);
        spinnerEstadoCivil= view.findViewById(R.id.estadoCivil);
        spinnerNivelEstudio= view.findViewById(R.id.nivelEstudio);
        spinnerTipoVivienda= view.findViewById(R.id.tipoVivienda);
        spinnerDpto= view.findViewById(R.id.dptoBolivia);
        spinnerTenencia= view.findViewById(R.id.tenencia);
        spinnerPrefijo=view.findViewById(R.id.tipoPrefijo);
        spinnerExtension=view.findViewById(R.id.spinnerExtension);
        spinnerMoneda=view.findViewById(R.id.spinnerMoneda);

        codigo_proyecto = view.findViewById(R.id.idProyecto);
        guardar = view.findViewById(R.id.btguardar);
        btFechaNac = view.findViewById(R.id.btDatePickerFechaNac);
        registrarForm = view.findViewById((R.id.btRegistrarDatosForm));
        //fechaNac.setText(fechaHoy());

        codigo_asesor.setText(Global.codigo.toString());
        asesor.setText(Global.nombreSesion+" "+Global.apellidoSesion);

        ///fragment mapa
//        Fragment fragment = new MapFragment();
//        getChildFragmentManager().beginTransaction().replace(R.id.mapContainer,fragment).commit();

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
                RBseleccionado(v);
            }
        });
        rb_contado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        /////cargamos los spinners
        cargarListaTipoIdentificacion(view);
        cargarListaEstadoCivil(view);
        cargarListaUrbanizacion(view);
        cargarListaNivelEstudio(view);
        cargarListaTipoVivienda(view);
        cargarListaDpto(view);
        cargarListaTenencia(view);
        cargarListaPrefijo(view);
        cargarListaExtensiones(view);
        cargarListaMoneda(view);

        spinner_urbanizacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codigo_proyecto.setText(listaProyectos.get(position).getCodigo().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCamposObligatorios()){
                    if(validarForm()){
                        if(EsDependiente()){
                            generarPDF(v);
                            Toast.makeText(getContext(), "Generando Formularios espere un momento...", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "Rellene Nombre Empresa y Direccion Empresa.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
//                        Toast.makeText(getContext(), "Rellene los campos marcados en *.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        registrarForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCamposObligatorios()) {
                    if(validarForm()){
                        if(EsDependiente()){
                            registrarTitular();
                            Toast.makeText(getContext(),"Registrando Datos espere un momento...", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "Rellene Nombre Empresa y Direccion Empresa.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                    }
                }
            }
        });

    }


    private void registrarTitular() {
        StringRequest request = new StringRequest(Request.Method.POST, URL_addtitular, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (!response.isEmpty()){
                        if(response.contains("algo salio mal")){
                            Toast.makeText(getContext(),"No se pudo completar el registro debido a un error",Toast.LENGTH_LONG).show();
                        }
                        else{Toast.makeText(getContext(),"Titular registrado correctamente",Toast.LENGTH_LONG).show();}

                    }else{
                        Toast.makeText(getContext(), "Se ha producido un error", Toast.LENGTH_LONG).show();
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
                    parametros.put("fecha_nacimiento",fechaNac.getText().toString());
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
                    parametros.put("uv",uv.getText().toString());
                    parametros.put("mz",mz.getText().toString());
                    parametros.put("lt",lt.getText().toString());
                    parametros.put("cat",cat.getText().toString());
                    parametros.put("asesor",asesor.getText().toString());
                    parametros.put("codigo_asesor",codigo_asesor.getText().toString());
                    parametros.put("fecha",fechaHoy());

                    parametros.put("urbanizacion",spinner_urbanizacion.getSelectedItem().toString());
                    parametros.put("metros2",mts2.getText().toString());
                    parametros.put("tipo_venta",rbSelected.getText().toString());


                    return parametros;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(request);

    }


    private String fechaHoy(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month=month+1;
        int day= cal.get(Calendar.DAY_OF_MONTH);
        Integer hora = cal.get(Calendar.HOUR_OF_DAY);
        Integer min = cal.get(Calendar.MINUTE);
        Integer seg = cal.get(Calendar.SECOND);
        String now = makeDateString(day,month,year);
        now = now+" "+hora.toString()+":"+min.toString()+":"+seg.toString();
        return now;
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
                                valor=true;
                                return valor;
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
            if (apellidoPaterno.getText().toString().length() == 0) {
                mensaje("Falta apellido paterno");
                return false;
            } else {
                if (apellidoMaterno.getText().toString().length() == 0) {
                    mensaje("Falta apellido materno");
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
                            if (fechaNac.getText().toString().length() == 0) {
                                mensaje("Seleccione su fecha nacimiento");
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

    private void iniciarDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = makeDateString(day,month,year);
                fechaNac.setText(date);
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

    private String makeDateString(int day, int month, int year) {
        return day+"/"+month+"/" + year;
    }
    public void openDatePicker(View v){
        datePickerDialog.show();
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

    public void cargarListaUrbanizacion(View v){
        listaProyectos.add(new Proyectos(100,"LA ENCONADA II"));
        listaProyectos.add(new Proyectos(101,"LA PASCANA DE COTOCA"));
        listaProyectos.add(new Proyectos(200,"LA PASCANA DE COTOCA II"));
        listaProyectos.add(new Proyectos(201,"LA TIERRA PROMETIDA"));
        listaProyectos.add(new Proyectos(204,"AME TAUNA"));
        listaProyectos.add(new Proyectos(205,"AME TAUNA I"));
        ArrayAdapter<Proyectos> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaProyectos);
        spinner_urbanizacion.setAdapter(adapter);
    }

    public void cargarListaExtensiones(View v){

        listaExtension.add("SC");
        listaExtension.add("LP");
        listaExtension.add("CB");
        listaExtension.add("PO");
        listaExtension.add("CH");
        listaExtension.add("TJ");
        listaExtension.add("OR");
        listaExtension.add("BE");
        listaExtension.add("PD");
//        listaExtension.add("NINGUNO");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaExtension);
        spinnerExtension.setAdapter(adapter);
    }
    public void cargarListaMoneda(View v){
        listaMoneda.add("-");
        listaMoneda.add("Bs");
        listaMoneda.add("$us");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaMoneda);
        spinnerMoneda.setAdapter(adapter);
    }


    public void cargarListaTipoIdentificacion(View v){
        listaIdentificacion.add("Carnet de Identidad");
        listaIdentificacion.add("Pasaporte");
        listaIdentificacion.add("Carnet Extranjero");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaIdentificacion);
        spinnerIdentificacion.setAdapter(adapter);
    }

    public void cargarListaEstadoCivil(View v){
        listaEstadoCivil.add("Casado");
        listaEstadoCivil.add("Soltero");
        listaEstadoCivil.add("Divorciado");
        listaEstadoCivil.add("Viudo");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaEstadoCivil);
        spinnerEstadoCivil.setAdapter(adapter);
    }

    public void cargarListaNivelEstudio(View v){
        listaNivelEstudio.add("Primaria");
        listaNivelEstudio.add("Secundaria");
        listaNivelEstudio.add("Tecnico Medio");
        listaNivelEstudio.add("Tecnico Superior");
        listaNivelEstudio.add("Licenciatura");
        listaNivelEstudio.add("Ninguno");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaNivelEstudio);
        spinnerNivelEstudio.setAdapter(adapter);
    }

    public void cargarListaTipoVivienda(View v){
        listaTipoVivienda.add("Dpto");
        listaTipoVivienda.add("Casa");
        listaTipoVivienda.add("Cuarto");
        listaTipoVivienda.add("Terreno");
        listaTipoVivienda.add("Otro");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaTipoVivienda);
        spinnerTipoVivienda.setAdapter(adapter);
    }

    public void cargarListaDpto(View v){
        listaDpto.add("Santa Cruz");
        listaDpto.add("La Paz");
        listaDpto.add("Cochabamba");
        listaDpto.add("Beni");
        listaDpto.add("Pando");
        listaDpto.add("Oruro");
        listaDpto.add("Potosi");
        listaDpto.add("Tarija");
        listaDpto.add("Chuquisaca");
        listaDpto.add("Ninguno");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaDpto);
        spinnerDpto.setAdapter(adapter);
    }

    public void cargarListaTenencia(View view) {
        listaTenencia.add("Propia");
        listaTenencia.add("Alquiler");
        listaTenencia.add("Anticretico");
        listaTenencia.add("Otro");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaTenencia);
        spinnerTenencia.setAdapter(adapter);
    }
    public void cargarListaPrefijo(View view) {
        listaPrefijo.add("Ninguno");
        listaPrefijo.add("Vda de");
        listaPrefijo.add("de");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaPrefijo);
        spinnerPrefijo.setAdapter(adapter);
    }
//    public void numerosVacios(){
//        if (costoAprox.getText().toString().length()==0){
//            costoAprox.setText("0");
//        }
//        if(telefonoPropietario.getText().toString().length()==0){
//            telefonoPropietario.setText("0");
//        }
//        if(numero.getText().toString().length()==0){
//            numero.setText("0");
//        }
//        if(telFijo.getText().toString().length()==0){
//            telFijo.setText("0");
//        }
//        if(telFijoOfc.getText().toString().length()==0){
//            telFijoOfc.setText("0");
//        }
//        if(telMovil.getText().toString().length()==0){
//            telMovil.setText("0");
//        }
//        if(telMovOfc.getText().toString().length()==0){
//           telMovOfc.setText("0");
//        }
//        if(ingresosEmpresa.getText().toString().length()==0){
//            ingresosEmpresa.setText("0");
//        }
//        if(telfReferencia1.getText().toString().length()==0){
//            telfReferencia1.setText("0");
//        }
//        if(telfReferencia2.getText().toString().length()==0){
//            telfReferencia2.setText("0");
//        }
//        if(mts2.getText().toString().length()==0){
//            mts2.setText("0");
//        }
//    }


/////boton generador de pdf/////
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void generarPDF (View v){
            String prefijoObtenido;
            PdfDocument myPDF = new PdfDocument();
            Paint myPaint = new Paint();
            Paint titlePaint = new Paint();

            myPaint.setTextAlign(Paint.Align.LEFT);
            myPaint.setTextSize(20f);
            myPaint.setColor(Color.BLACK);
            myPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));

            titlePaint.setTextAlign(Paint.Align.LEFT);
            titlePaint.setTextSize(50f);
            titlePaint.setColor(Color.BLACK);

            if(spinnerPrefijo.getSelectedItem().toString().contains("Ninguno")){
                prefijoObtenido="";
            }else {
                prefijoObtenido=spinnerPrefijo.getSelectedItem().toString();
            }
            //verificamos los campos numericos si estan vacios se pondran 0
            //numerosVacios();

            ////definimos pagina 1
            PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(2539,3874,1).create();
            PdfDocument.Page myPage1 = myPDF.startPage(myPageInfo1);
            Canvas canvas = myPage1.getCanvas();
            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.form1);
            scaledbmp = Bitmap.createScaledBitmap(bmp,2539,3874,false);
            canvas.drawBitmap(scaledbmp,0,0,myPaint);

            Bitmap check,scaled2;
            check = BitmapFactory.decodeResource(getResources(),R.drawable.check1);
            scaled2 = Bitmap.createScaledBitmap(check,100,100,false);

            canvas.drawText(spinner_urbanizacion.getSelectedItem().toString(),920,305,titlePaint);
            canvas.drawText(nombre_cliente.getText().toString().toUpperCase()+" "+apellidoPaterno.getText().toString().toUpperCase()+" "+apellidoMaterno.getText().toString().toUpperCase(),600,460,titlePaint);
            canvas.drawText(ci_cliente.getText().toString(),680,585,titlePaint);
            canvas.drawText(spinnerExtension.getSelectedItem().toString(),1480,585,titlePaint);
            String plazoContado = rbSelected.getText().toString();
            if(plazoContado.contains("A plazo")){
                canvas.drawBitmap(scaled2,980,640,myPaint);
            }else {
                canvas.drawBitmap(scaled2,1430,640,myPaint);
            }
            canvas.drawText(codigo_proyecto.getText().toString(),400,835,titlePaint);
            canvas.drawText(tresDigitos(uv.getText().toString()),880,835,titlePaint);
            canvas.drawText(tresDigitos(mz.getText().toString()),1310,835,titlePaint);
            canvas.drawText(tresDigitos(lt.getText().toString()),1730,835,titlePaint);
            canvas.drawText(cat.getText().toString(),2230,835,titlePaint);
            canvas.drawText(asesor.getText().toString(),250,995,titlePaint);
            canvas.drawText(codigo_asesor.getText().toString(),1730,995,titlePaint);

            myPDF.finishPage(myPage1);
            //// FIN PAGINA 1/////

            //////PAGINA 2
            PdfDocument.PageInfo myPageInfo2 = new PdfDocument.PageInfo.Builder(2539,3874,1).create();
            PdfDocument.Page myPage2 = myPDF.startPage(myPageInfo2);
            Canvas canvas2 = myPage2.getCanvas();

            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.nuevoform4);
            scaledbmp = Bitmap.createScaledBitmap(bmp,2539,3874,false);
            canvas2.drawBitmap(scaledbmp,0,0,myPaint);

            canvas2.drawText(apellidoPaterno.getText().toString().toUpperCase(),450,520,titlePaint);
            canvas2.drawText(apellidoMaterno.getText().toString().toUpperCase(),1450,520,titlePaint);
            canvas2.drawText(nombre_cliente.getText().toString().toUpperCase(),450,685,titlePaint);
            canvas2.drawText(apellidoCasada.getText().toString().toUpperCase(),1450,685,titlePaint);
            canvas2.drawText(prefijoObtenido,2060,685,titlePaint);
//            if(spinnerPrefijo.getSelectedItem().toString().contains("Ninguno")){
//                canvas2.drawText("",2060,750,titlePaint);
//            }else {
//                canvas2.drawText(spinnerPrefijo.getSelectedItem().toString(),2060,750,titlePaint);
//            }

            canvas2.drawText(ci_cliente.getText().toString(),1450,840,titlePaint);
            canvas2.drawText(spinnerExtension.getSelectedItem().toString(),2060,840,titlePaint);

            canvas2.drawText(spinnerIdentificacion.getSelectedItem().toString(),650,803,titlePaint);
            canvas2.drawText(nacionalidad.getText().toString(),650,850,titlePaint);
            canvas2.drawText(fechaNac.getText().toString(),650,903,titlePaint);
            canvas2.drawText(spinnerEstadoCivil.getSelectedItem().toString(),650,950,titlePaint);
            canvas2.drawText(rbSelectedGenero.getText().toString(),650,990,titlePaint);
            canvas2.drawText(spinnerNivelEstudio.getSelectedItem().toString(),650,1042,titlePaint);
            canvas2.drawText(profesion.getText().toString(),650,1093,titlePaint);
            canvas2.drawText(spinnerTipoVivienda.getSelectedItem().toString(),1120,1153,titlePaint);
            canvas2.drawText(spinnerTenencia.getSelectedItem().toString(),1740,1153,titlePaint);
            canvas2.drawText(costoAprox.getText().toString()+" "+spinnerMoneda.getSelectedItem().toString(),250,1280,titlePaint);
            canvas2.drawText(propietarioVivienta.getText().toString().toUpperCase(),780,1280,titlePaint);
            canvas2.drawText(telefonoPropietario.getText().toString(),1950,1280,titlePaint);

            canvas2.drawText(pais.getText().toString(),270,1400,titlePaint);
            if(spinnerDpto.getSelectedItem().toString().contains("Ninguno")){
                canvas2.drawText("",820,1400,titlePaint);
            }else {
                canvas2.drawText(spinnerDpto.getSelectedItem().toString(),820,1400,titlePaint);
            }
            canvas2.drawText(ciudad.getText().toString(),300,1530,titlePaint);
            canvas2.drawText(barrio.getText().toString(),1300,1530,titlePaint);
            canvas2.drawText(avenida.getText().toString(),300,1680,titlePaint);
            canvas2.drawText(calle.getText().toString(),1300,1680,titlePaint);
            canvas2.drawText(numero.getText().toString(),2060,1680,titlePaint);

            canvas2.drawText(telFijo.getText().toString(),250,1875,titlePaint);
            canvas2.drawText(telFijoOfc.getText().toString(),250,2030,titlePaint);
            canvas2.drawText(telMovil.getText().toString(),750,1875,titlePaint);
            canvas2.drawText(telMovOfc.getText().toString(),750,2030,titlePaint);
            canvas2.drawText(correoPersonal.getText().toString(),250,2180,titlePaint);

            canvas2.drawText(nombreEmpresa.getText().toString(),1300,1875,titlePaint);
            canvas2.drawText(direccionEmpresa.getText().toString(),1300,2030,titlePaint);
            canvas2.drawText(rubroEmpresa.getText().toString(),1270,2180,titlePaint);
            canvas2.drawText(ingresosEmpresa.getText().toString()+" "+rbSelectedIngresos.getText().toString(),2060,2180,titlePaint);

            canvas2.drawText(primerReferencia.getText().toString().toUpperCase(),250,2380,titlePaint);
            canvas2.drawText(segundaReferencia.getText().toString().toUpperCase(),250,2530,titlePaint);
            canvas2.drawText(parentesco.getText().toString(),1300,2380,titlePaint);
            canvas2.drawText(relacion.getText().toString(),1300,2530,titlePaint);
            canvas2.drawText(telfReferencia1.getText().toString(),2060,2380,titlePaint);
            canvas2.drawText(telfReferencia2.getText().toString(),2060,2530,titlePaint);
            titlePaint.setTextSize(40f);
            canvas2.drawText(nombre_cliente.getText().toString().toUpperCase()+" "
                    +apellidoPaterno.getText().toString().toUpperCase()+" "
                    +apellidoMaterno.getText().toString().toUpperCase()+" "+prefijoObtenido+" "+apellidoCasada.getText().toString().toUpperCase()
                    ,500,3635,titlePaint);
            canvas2.drawText(asesor.getText().toString().toUpperCase(),1800,3635,titlePaint);
            titlePaint.setTextSize(50f);

            myPDF.finishPage(myPage2);
              //////FIN PAGINA 2 /////
//
//
//        ////// INICIA PAGINA 3 ///////
            PdfDocument.PageInfo myPageInfo3 = new PdfDocument.PageInfo.Builder(2539,3874,1).create();
            PdfDocument.Page myPage3 = myPDF.startPage(myPageInfo3);
            Canvas canvas3 = myPage3.getCanvas();

            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.form3legal);
//          bmp = BitmapFactory.decodeResource(getResources(),R.drawable.newentrega);
            scaledbmp = Bitmap.createScaledBitmap(bmp,2539,3874,false);
            canvas3.drawBitmap(scaledbmp,0,0,myPaint);

            Calendar cal = Calendar.getInstance();
            Integer year = cal.get(Calendar.YEAR);
            Integer month = cal.get(Calendar.MONTH);
            month=month+1;
            Integer day= cal.get(Calendar.DAY_OF_MONTH);

            myPaint.setTextAlign(Paint.Align.CENTER);
            myPaint.setTextSize(70f);
            myPaint.setColor(Color.BLACK);
            myPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
            canvas3.drawText(spinner_urbanizacion.getSelectedItem().toString(),700,900,myPaint);
            myPaint.setTextAlign(Paint.Align.LEFT);
            canvas3.drawText(day.toString(),235,590,myPaint);
            canvas3.drawText(month.toString(),470,590,myPaint);
            canvas3.drawText(year.toString(),660,590,myPaint);
            titlePaint.setTextSize(60f);
            canvas3.drawText(codigo_proyecto.getText().toString(),270,1200,titlePaint);
            canvas3.drawText(tresDigitos(uv.getText().toString()),740,1200,titlePaint);
            canvas3.drawText(tresDigitos(mz.getText().toString()),1200,1200,titlePaint);
            canvas3.drawText(tresDigitos(lt.getText().toString()),1680,1200,titlePaint);
            canvas3.drawText(cat.getText().toString(),2180,1200,titlePaint);

            canvas3.drawText(rbSelected.getText().toString(),270,1555,titlePaint);
            canvas3.drawText(mts2.getText().toString(),740,1555,titlePaint);
            titlePaint.setTextSize(45f);
            canvas3.drawText(nombre_cliente.getText().toString().toUpperCase()+" "+apellidoPaterno.getText().toString().toUpperCase()+" "+
                    apellidoMaterno.getText().toString().toUpperCase()+" "+prefijoObtenido+" "+apellidoCasada.getText().toString().toUpperCase(),245,1750,titlePaint);
            canvas3.drawText(ci_cliente.getText().toString(),1950,1750,titlePaint);
            canvas3.drawText(spinnerExtension.getSelectedItem().toString(),270,1808,titlePaint);

            canvas3.drawText(nombre_cliente.getText().toString().toUpperCase()+" "
                        +apellidoPaterno.getText().toString().toUpperCase()+" "
                        +apellidoMaterno.getText().toString().toUpperCase()+" "+prefijoObtenido+" "+apellidoCasada.getText().toString().toUpperCase(),570,2960,titlePaint);
//            canvas3.drawText(asesor.getText().toString(),1910,2960,titlePaint);

            myPDF.finishPage(myPage3);
        /////FIN PAGINA 3/////////

        ///INICIO DE PAGINA 4 ////
            PdfDocument.PageInfo myPageInfo4 = new PdfDocument.PageInfo.Builder(2539,3874,1).create();
            PdfDocument.Page myPage4 = myPDF.startPage(myPageInfo3);
            Canvas canvas4 = myPage4.getCanvas();
            if(Global.ubicacion==null){
                Global.ubicacion=BitmapFactory.decodeResource(getResources(),R.drawable.nomap);
            }
            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.nuevoformmapa);
            scaledbmp = Bitmap.createScaledBitmap(bmp,2539,3874,false);
            canvas4.drawBitmap(scaledbmp,0,0,myPaint);

            canvas4.drawText(tresDigitos(uv.getText().toString()),600,460,titlePaint);
            canvas4.drawText(tresDigitos(mz.getText().toString()),1190,460,titlePaint);
            canvas4.drawText(tresDigitos(lt.getText().toString()),1780,460,titlePaint);
            canvas4.drawText(cat.getText().toString(),2250,460,titlePaint);
            canvas4.drawText(nombre_cliente.getText().toString()+" "+apellidoPaterno.getText().toString()+" "+apellidoMaterno.getText().toString()+" "+prefijoObtenido+" "+apellidoCasada.getText().toString(),800,575,titlePaint);
            canvas4.drawText(ci_cliente.getText().toString(),800,637,titlePaint);
            canvas4.drawText(telMovil.getText().toString()+" - "+telFijo.getText().toString(),1700,637,titlePaint);
            canvas4.drawText("Barrio:"+" "+barrio.getText().toString()+" Avenida: "+avenida.getText().toString()+" Calle: "+calle.getText().toString()+" Numero: "+numero.getText().toString(),800,705,titlePaint);
            canvas4.drawText(primerReferencia.getText().toString()+" "+telfReferencia1.getText().toString()+" - "+segundaReferencia.getText().toString()+" "+telfReferencia2.getText().toString(),800,775,titlePaint);
            canvas4.drawText(zona.getText().toString(),800,850,titlePaint);

        if(rbSelectedSinConUbicacion.getText().toString().contains("Si")){
            scaledbmp = Bitmap.createScaledBitmap(Global.ubicacion,2280,2500,false);
            canvas4.drawBitmap(scaledbmp,150,900,myPaint);
            myPDF.finishPage(myPage4);
        }else {
            myPDF.finishPage(myPage4);
        }



        ///FIN DE PAGINA 4/////

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
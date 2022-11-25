package com.example.novitierraapp;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
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
import android.os.Handler;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class formulario4 extends Fragment {

    EditText nombre_cliente, apellidoPaterno, apellidoMaterno, ci_cliente,uv,mz,lt,cat,asesor,codigo_asesor, apellidoCasada,nacionalidad,profesion,costoAprox;
    EditText propietarioVivienta,telefonoPropietario,pais,ciudad,barrio,avenida,calle,numero,telFijo,telMovil,telFijoOfc,telMovOfc,correoPersonal,expedido,mts2;
    EditText nombreEmpresa, rubroEmpresa, direccionEmpresa, ingresosEmpresa,primerReferencia,segundaReferencia,telfReferencia1,telfReferencia2,parentesco,relacion,zona;
    EditText observacion1, observacion2;
    RadioGroup radioGroup, radioGroupGenero, radioGroupVivienda, radioGroupIngresos, radioGroupIndependienteDependiente;
    RadioButton rb_plazo, rb_contado, rbSelected, rbMasculino, rbFemenino, rbSelectedGenero, rbIngresosBs, rbIngresosDolar,rbSelectedIngresos, rbDependiende, rbIndependiente, rbSelectedIndependienteDependiente;
    //    rbSelectedMonedaVivienda
//    rbViviendaBs,rbViviendaDolar,
    RadioGroup radioGroupSinConUbicacion;
    RadioButton rbconUbicacion, rbsinUbicacion, rbSelectedSinConUbicacion;
    Spinner spinner_urbanizacion, spinnerIdentificacion,spinnerEstadoCivil,spinnerNivelEstudio, spinnerTipoVivienda, spinnerDpto, spinnerTenencia, spinnerPrefijo, spinnerExtension, spinnerMoneda, spinnerPlazo;
    TextView codigo_proyecto,tvfechaNacimiento,tvplazo;
    Button guardar, btFechaNac; //registrarForm;
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

    Bitmap imagen,scaled;

//    private String URL_addtitular="http://wizardapps.xyz/novitierra/api/addTitular.php";
//    private String URL_addtitular="https://novitierra.000webhostapp.com/api/addTitular.php";

    //***PARA PDF****
    private String path = Environment.getExternalStorageDirectory().getPath() + "/Download/Formulario4.pdf";
    private File file = new File(path);

    private Formulario4ViewModel mViewModel;

    public static formulario4 newInstance() {
        return new formulario4();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.formulario4_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(Formulario4ViewModel.class);
        // TODO: Use the ViewModel
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
        asesor=view.findViewById(R.id.fullnameAsesor);
        codigo_asesor= view.findViewById(R.id.codigoAsesor);
        observacion1 = view.findViewById(R.id.observacion1);
        observacion2 = view.findViewById(R.id.observacion2);
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

        codigo_proyecto = view.findViewById(R.id.idProyecto);
        tvfechaNacimiento = view.findViewById(R.id.fechaNacimiento);
        tvplazo = view.findViewById(R.id.tvplazo);
        guardar = view.findViewById(R.id.btguardar);
        btFechaNac = view.findViewById(R.id.btDatePickerFechaNac);
//        registrarForm = view.findViewById((R.id.btRegistrarDatosForm));
        //fechaNac.setText(fechaHoy());

        codigo_asesor.setText(Global.codigo.toString());
        asesor.setText(Global.nombreSesion+" "+Global.apellidoSesion);

        /////cargamos los spinners
        cargarListaTipoIdentificacion();
        cargarListaEstadoCivil();
        cargarListaNivelEstudio();
        cargarListaTipoVivienda();
        cargarListaDpto();
        cargarListaTenencia();
        cargarListaPrefijo();
        cargarListaExtensiones();
        cargarListaMoneda();

        ////boton fecha nacimiento
        btFechaNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
            }
        });
        iniciarDatePicker();

        ////funcion de los Radiobutton y radioGroups

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

        rbIndependiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeleccionIndependienteDependiente(v);
            }
        });
        rbDependiende.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeleccionIndependienteDependiente(v);
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validarCamposObligatorios()){
                    if(validarForm()){
                        if(validarDependienteIndependiente()){
                            if(EsDependiente()){
                                DeshabilitarBoton();
                                new Handler().postDelayed(new Runnable(){
                                    public void run(){
                                        generarPDF();
                                    }
                                }, 1000); //1000 millisegundos = 1 segundo.
                                Toast.makeText(getContext(), "Generando Formularios verifique bien todo los datos...", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getContext(), "Rellene Nombre Empresa y Direccion Empresa.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            mensaje("Seleccionar Independiente o Dependiente");
                        }
                    }else {
//                        Toast.makeText(getContext(), "Rellene los campos marcados en *.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//        registrarForm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (validarCamposObligatorios()) {
//                    if(validarForm()){
//                        if(EsDependiente()){
//                            registrarTitular();
//                            Toast.makeText(getContext(),"Registrando Datos espere un momento...", Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(getContext(), "Rellene Nombre Empresa y Direccion Empresa.", Toast.LENGTH_SHORT).show();
//                        }
//                    }else {
//                    }
//                }
//            }
//        });

    }

    public void DeshabilitarBoton(){
        guardar.setEnabled(false);
    }
    public void habilitarBoton(){
        guardar.setEnabled(true);
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

    public Boolean validarForm(){
                if (rbMasculino.isChecked() || rbFemenino.isChecked()) {
                    if (rbIngresosBs.isChecked() || rbIngresosDolar.isChecked()) {
                        return true;
                    } else {
                        Toast.makeText(getContext(), "Falta seleccionar Ingresos en Bs o $us.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(getContext(), "Falta seleccionar Masculino o Femenino.", Toast.LENGTH_SHORT).show();
                    return false;
                }

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
                                                                        if (asesor.getText().toString().length() == 0) {
                                                                            mensaje("Falta Asesor");
                                                                            return false;
                                                                            } else {
                                                                            if (codigo_asesor.getText().toString().length() == 0) {
                                                                                mensaje("Falta codigo asesor");
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

    public Boolean validarDependienteIndependiente(){
        if(rbDependiende.isChecked() || rbIndependiente.isChecked()){
            return true;
        }else {
            return false;
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
                tvfechaNacimiento.setText(date);
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

    private String makeDateString(Integer day, Integer month, int year) {
        String day2=day.toString(),month2=month.toString();
        if(day.toString().length()==1){
            day2="0"+day;
        }
        if(month.toString().length()==1){
            month2="0"+month;
        }
        return day2+"/"+month2+"/" + year;
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

    private void rbSeleccionIngresos(View v) {
        int radiobtid = radioGroupIngresos.getCheckedRadioButtonId();
        rbSelectedIngresos = v.findViewById(radiobtid);
    }

    private void SeleccionIndependienteDependiente(View v) {
        int radiobtid = radioGroupIndependienteDependiente.getCheckedRadioButtonId();
        rbSelectedIndependienteDependiente = v.findViewById(radiobtid);
    }

    public void cargarListaExtensiones(){
        listaExtension.add("--");
        listaExtension.add("SC");
        listaExtension.add("LP");
        listaExtension.add("CB");
        listaExtension.add("PO");
        listaExtension.add("CH");
        listaExtension.add("TJ");
        listaExtension.add("OR");
        listaExtension.add("BE");
        listaExtension.add("PD");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaExtension);
        spinnerExtension.setAdapter(adapter);
    }
    public void cargarListaMoneda(){
        listaMoneda.add("Ninguno");
        listaMoneda.add("Bs");
        listaMoneda.add("$us");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaMoneda);
        spinnerMoneda.setAdapter(adapter);
    }




    public void cargarListaTipoIdentificacion(){
        listaIdentificacion.add("Carnet de Identidad");
        listaIdentificacion.add("Pasaporte");
        listaIdentificacion.add("Carnet Extranjero");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaIdentificacion);
        spinnerIdentificacion.setAdapter(adapter);
    }

    public void cargarListaEstadoCivil(){
        listaEstadoCivil.add("Casado(a)");
        listaEstadoCivil.add("Soltero(a)");
        listaEstadoCivil.add("Divorciado(a)");
        listaEstadoCivil.add("Viudo(a)");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaEstadoCivil);
        spinnerEstadoCivil.setAdapter(adapter);
    }

    public void cargarListaNivelEstudio(){
        listaNivelEstudio.add("Primaria");
        listaNivelEstudio.add("Secundaria");
        listaNivelEstudio.add("Tecnico Medio");
        listaNivelEstudio.add("Tecnico Superior");
        listaNivelEstudio.add("Licenciatura");
        listaNivelEstudio.add("Ninguno");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaNivelEstudio);
        spinnerNivelEstudio.setAdapter(adapter);
    }

    public void cargarListaTipoVivienda(){
        listaTipoVivienda.add("Dpto");
        listaTipoVivienda.add("Casa");
        listaTipoVivienda.add("Cuarto");
        listaTipoVivienda.add("Terreno");
        listaTipoVivienda.add("Otro");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaTipoVivienda);
        spinnerTipoVivienda.setAdapter(adapter);
    }

    public void cargarListaDpto(){
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

    public void cargarListaTenencia() {
        listaTenencia.add("Propia");
        listaTenencia.add("Alquiler");
        listaTenencia.add("Anticretico");
        listaTenencia.add("Otro");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaTenencia);
        spinnerTenencia.setAdapter(adapter);
    }
    public void cargarListaPrefijo() {
        listaPrefijo.add("Ninguno");
        listaPrefijo.add("VDA DE");
        listaPrefijo.add("DE");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaPrefijo);
        spinnerPrefijo.setAdapter(adapter);
    }



    /////boton generador de pdf/////
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void generarPDF (){
        String prefijoObtenido,monedaCostoAproximado;

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
        if(spinnerMoneda.getSelectedItem().toString().contains("Ninguno")){
            monedaCostoAproximado="";
        }else{
            monedaCostoAproximado=spinnerMoneda.getSelectedItem().toString();
        }
        //verificamos los campos numericos si estan vacios se pondran 0
        //numerosVacios();

        //////PAGINA 2
        PdfDocument.PageInfo myPageInfo2 = new PdfDocument.PageInfo.Builder(2539,3874,1).create();
        PdfDocument.Page myPage2 = myPDF.startPage(myPageInfo2);
        Canvas canvas2 = myPage2.getCanvas();
        imagen = BitmapFactory.decodeResource(getResources(),R.drawable.f4png);
        scaled = Bitmap.createScaledBitmap(imagen,2539,3874,false);
//            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.nuevoform4);
//            scaledbmp = Bitmap.createScaledBitmap(bmp,2539,3874,false);
        canvas2.drawBitmap(scaled,0,0,myPaint);

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
        canvas2.drawText(tvfechaNacimiento.getText().toString(),650,903,titlePaint);
        canvas2.drawText(spinnerEstadoCivil.getSelectedItem().toString(),650,950,titlePaint);
        canvas2.drawText(rbSelectedGenero.getText().toString(),650,990,titlePaint);
        canvas2.drawText(spinnerNivelEstudio.getSelectedItem().toString(),650,1042,titlePaint);
        canvas2.drawText(profesion.getText().toString(),650,1093,titlePaint);
        canvas2.drawText(spinnerTipoVivienda.getSelectedItem().toString(),1120,1153,titlePaint);
        canvas2.drawText(spinnerTenencia.getSelectedItem().toString(),1740,1153,titlePaint);
        canvas2.drawText(costoAprox.getText().toString()+" "+monedaCostoAproximado,250,1280,titlePaint);
        canvas2.drawText(propietarioVivienta.getText().toString().toUpperCase(),780,1280,titlePaint);
        canvas2.drawText(telefonoPropietario.getText().toString(),1950,1280,titlePaint);

        canvas2.drawText(pais.getText().toString(),270,1410,titlePaint);
        if(spinnerDpto.getSelectedItem().toString().contains("Ninguno")){
            canvas2.drawText("",820,1410,titlePaint);
        }else {
            canvas2.drawText(spinnerDpto.getSelectedItem().toString(),820,1410,titlePaint);
        }
        canvas2.drawText(ciudad.getText().toString(),200,1530,titlePaint);
        canvas2.drawText(barrio.getText().toString(),1250,1530,titlePaint);
        canvas2.drawText(avenida.getText().toString(),200,1680,titlePaint);
        canvas2.drawText(calle.getText().toString(),1250,1680,titlePaint);
        canvas2.drawText(numero.getText().toString(),2060,1680,titlePaint);

        canvas2.drawText(telFijo.getText().toString(),250,1875,titlePaint);
        canvas2.drawText(telFijoOfc.getText().toString(),250,2030,titlePaint);
        canvas2.drawText(telMovil.getText().toString(),750,1875,titlePaint);
        canvas2.drawText(telMovOfc.getText().toString(),750,2030,titlePaint);
        canvas2.drawText(correoPersonal.getText().toString(),250,2180,titlePaint);

        canvas2.drawText(nombreEmpresa.getText().toString(),1250,1875,titlePaint);
        canvas2.drawText(direccionEmpresa.getText().toString(),1250,2030,titlePaint);
        canvas2.drawText(rubroEmpresa.getText().toString(),1250,2180,titlePaint);
        canvas2.drawText(ingresosEmpresa.getText().toString()+" "+rbSelectedIngresos.getText().toString(),2060,2180,titlePaint);

        canvas2.drawText(primerReferencia.getText().toString().toUpperCase(),200,2380,titlePaint);
        canvas2.drawText(segundaReferencia.getText().toString().toUpperCase(),200,2530,titlePaint);
        canvas2.drawText(parentesco.getText().toString(),1300,2380,titlePaint);
        canvas2.drawText(relacion.getText().toString(),1300,2530,titlePaint);
        canvas2.drawText(telfReferencia1.getText().toString(),2060,2380,titlePaint);
        canvas2.drawText(telfReferencia2.getText().toString(),2060,2530,titlePaint);
        titlePaint.setTextSize(40f);
        canvas2.drawText(observacion1.getText().toString(),485,3280,titlePaint);
        canvas2.drawText(observacion2.getText().toString(),485,3330,titlePaint);
        canvas2.drawText(nombre_cliente.getText().toString().toUpperCase()+" "
                        +apellidoPaterno.getText().toString().toUpperCase()+" "
                        +apellidoMaterno.getText().toString().toUpperCase()+" "+prefijoObtenido.toUpperCase()+" "+apellidoCasada.getText().toString().toUpperCase()
                ,390,3635,titlePaint);
        canvas2.drawText(asesor.getText().toString().toUpperCase(),1750,3635,titlePaint);
        titlePaint.setTextSize(50f);

        myPDF.finishPage(myPage2);
        //////FIN PAGINA 2 /////

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
/// ***** este es el bueno para android 9 inferior************
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
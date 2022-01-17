package com.example.novitierraapp;

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
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.novitierraapp.entidades.Global;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FormCoSolicitante extends Fragment {

    EditText relacionCoSol,nombre,apellidoP,apellidoM,apellidoCasada,ci,profesion,nacionalidad,telFijo,telMovil,fijoOfi,movilOfi,correo,empresa,direccionEmpresa,rubro,etFechaNac,asesor,pais,ingresos;
    Spinner spinnerTipoIdent, spinnerEstadoCivil, spinnerNivelEstudio, spinnerDptos,spinnerExtension;
    DatePickerDialog datePickerDialog;
    RadioGroup radioGroupGenero, radioGroupIngresos;
    RadioButton rbSelectedGenero, rbMasculinoCoSol,rbFemeninoCoSol, rbingresoBs, rbingresoDolar, rbSelectedIngreso;
    Button btGenerarPdf, btFechaNac; //btRegistrarCoSol;
    ArrayList<String> listaIdentificacion = new ArrayList<>();
    ArrayList<String> listaEstadoCivil = new ArrayList<>();
    ArrayList<String> listaNivelEstudio = new ArrayList<>();
    ArrayList<String> listaExtension = new ArrayList<>();

//    ArrayList<String> listaTipoVivienda = new ArrayList<>();
    ArrayList<String> listaDpto = new ArrayList<>();


    Bitmap bmp, scaledbmp;
    private String URL_add_cosol = "http://wizardapps.xyz/novitierra/api/addCoSol.php";
//    private String URL_add_cosol = "https://novitierra.000webhostapp.com/api/addCoSol.php";


    //***PARA PDF****
    private String path = Environment.getExternalStorageDirectory().getPath() + "/Download/FormCoSolicitante.pdf";
    private File file = new File(path);

    private FormCoSolicitanteViewModel mViewModel;

    public static FormCoSolicitante newInstance() {
        return new FormCoSolicitante();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        ///necesario para poder compartir el pdf
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//        builder.detectFileUriExposure();
//        /////

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        relacionCoSol=view.findViewById(R.id.relacionCoSolTitular);
        nombre = view.findViewById(R.id.nombreCoSol);
        apellidoP = view.findViewById(R.id.apellidoPCoSol);
        apellidoM = view.findViewById(R.id.apellidoMCoSol);
        apellidoCasada = view.findViewById(R.id.apellidoCasadaCoSol);
        ci = view.findViewById(R.id.ciCoSol);
        profesion=view.findViewById(R.id.profesionCoSol);
        nacionalidad = view.findViewById(R.id.nacionalidadCoSol);
        pais = view.findViewById(R.id.direccionPaisCoSol);
        telFijo=view.findViewById(R.id.telfFijoCoSol);
        telMovil=view.findViewById(R.id.telfMovilCoSol);
        fijoOfi=view.findViewById(R.id.telfFijoOficinaCoSol);
        movilOfi=view.findViewById(R.id.telfMovilOficinaCoSol);
        correo=view.findViewById(R.id.correoCoSol);
        empresa=view.findViewById(R.id.empresaNombreCoSol);
        direccionEmpresa=view.findViewById(R.id.direccionEmpresaCoSol);
        rubro=view.findViewById(R.id.rubroEmpresaCoSol);
        ingresos=view.findViewById(R.id.ingresosCoSol);
        etFechaNac=view.findViewById(R.id.fechaNacimientoCoSol);
        asesor=view.findViewById(R.id.fullnameAsesor);
        spinnerTipoIdent=view.findViewById(R.id.tipoIdentificacionCoSol);
        spinnerEstadoCivil=view.findViewById(R.id.estadoCivilCoSol);
        spinnerNivelEstudio=view.findViewById(R.id.nivelEstudioCoSol);
        spinnerDptos=view.findViewById(R.id.dptoBoliviaCoSol);
        spinnerExtension=view.findViewById(R.id.spinnerExtensionCoSol);
        rbMasculinoCoSol=view.findViewById(R.id.masculinoCoSol);
        rbFemeninoCoSol=view.findViewById(R.id.femeninoCoSol);
        rbingresoBs=view.findViewById(R.id.ingresosCoSolBs);
        rbingresoDolar=view.findViewById(R.id.ingresosCoSolDolar);
        radioGroupGenero=view.findViewById(R.id.radiogroupGeneroCoSol);
        radioGroupIngresos=view.findViewById(R.id.radiogroupIngresosCoSol);
        btFechaNac=view.findViewById(R.id.btDatePickerFechaNacCoSol);
        btGenerarPdf=view.findViewById(R.id.btguardarCoSol);
//        btRegistrarCoSol=view.findViewById(R.id.btRegistrarCosol);

        rbMasculinoCoSol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbSeleccionGenero(v);
            }
        });
        rbFemeninoCoSol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbSeleccionGenero(v);
            }
        });
        rbingresoDolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbSeleccionIngresos(v);
            }
        });
        rbingresoBs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbSeleccionIngresos(v);
            }
        });

//        etFechaNac.setText(fechaHoy());
        btFechaNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
            }
        });
        iniciarDatePicker();
        cargarListaDpto(view);
        cargarListaEstadoCivil(view);
        cargarListaNivelEstudio(view);
        cargarListaTipoIdentificacion(view);
        cargarListaExtensiones(view);

        asesor.setText(Global.nombreSesion+" "+Global.apellidoSesion);

        ////para el boton del pdf
        ActivityCompat.requestPermissions(getActivity(),new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        btGenerarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbingresoBs.isChecked()|| rbingresoDolar.isChecked()){
                    if(rbMasculinoCoSol.isChecked()|| rbFemeninoCoSol.isChecked()){
                        if(validarCamposObligatorios()){
                            //        verificamos las variables con numeros si se encuentran vacias
//                            numerosVacios();
                            deshabilitar();
                            new Handler().postDelayed(new Runnable(){
                                public void run(){
                                    generarPDF();
                                    registrarCoSol();
                                }
                            }, 1000); //1000 millisegundos = 1 segundo.
                            Toast.makeText(getContext(),"Generando Formulario espere un momento..",Toast.LENGTH_SHORT).show();

                        }else {
//                            Toast.makeText(getContext(),"Rellene los campos marcados en *.",Toast.LENGTH_SHORT).show();
                        }

                    }else{Toast.makeText(getContext(),"Falta seleccionar Masculino o Femenino.",Toast.LENGTH_SHORT).show();}
                }else {Toast.makeText(getContext(),"Falta seleccionar Ingresos Bs o Dolar.",Toast.LENGTH_SHORT).show(); }
            }
        });

//        btRegistrarCoSol.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (rbingresoBs.isChecked()|| rbingresoDolar.isChecked()){
//                    if(rbMasculinoCoSol.isChecked()|| rbFemeninoCoSol.isChecked()){
//                        if(validarCamposObligatorios()){
//                            //        verificamos las variables con numeros si se encuentran vacias
////                            numerosVacios();
//                            registrarCoSol();
//                            Toast.makeText(getContext(),"Registrando Co-Solicitante.......espere un momento",Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(getContext(),"Rellene los campos marcados en *.",Toast.LENGTH_SHORT).show();
//                        }
//
//                    }else{Toast.makeText(getContext(),"Falta seleccionar Masculino o Femenino.",Toast.LENGTH_SHORT).show();}
//                }else {Toast.makeText(getContext(),"Falta seleccionar Ingresos Bs o Dolar.",Toast.LENGTH_SHORT).show(); }
//            }
//        });
    }

    public void mensaje(String mensaje){
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
    }
    public void deshabilitar(){
        btGenerarPdf.setEnabled(false);
    }
    public void habilitar(){
        btGenerarPdf.setEnabled(true);
    }


    public Boolean validarCamposObligatorios(){
        if(nombre.getText().toString().length()==0){
            mensaje("Falta nombre");
        }else{
            if(apellidoP.getText().toString().length()==0){
                mensaje("Falta apellido paterno");
            }else {
                if(apellidoM.getText().toString().length()==0){mensaje("Falta apellido materno");}else{
                    if(ci.getText().toString().length()==0){mensaje("Falta CI");}else{
                        if(nacionalidad.getText().toString().length()==0){mensaje("Falta nacionalidad");}else{
                            if(etFechaNac.getText().toString().length()==0){mensaje("Falta fecha nacimiento");}else{
                              if(profesion.getText().toString().length()==0){mensaje("Falta profesion");}else{
                                  if(nacionalidad.getText().toString().length()==0){mensaje("Falta nacionalidad");}else{
                                        if(telMovil.getText().toString().length()==0){mensaje("Falta telefono movil");}else{
                                            if(asesor.getText().toString().length()==0){mensaje("Falta asesor");}else{
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
            return false;
        }
        return false;
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


    private void iniciarDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = makeDateString(day,month,year);
                etFechaNac.setText(date);
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

    private void rbSeleccionGenero(View v) {
        int radiobtid = radioGroupGenero.getCheckedRadioButtonId();
        rbSelectedGenero = v.findViewById(radiobtid);
    }
    private void rbSeleccionIngresos(View v) {
        int radiobtid = radioGroupIngresos.getCheckedRadioButtonId();
        rbSelectedIngreso = v.findViewById(radiobtid);
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
        return inflater.inflate(R.layout.form_co_solicitante_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FormCoSolicitanteViewModel.class);
        // TODO: Use the ViewModel
    }

    public void cargarListaTipoIdentificacion(View v){
        listaIdentificacion.add("Carnet de Identidad");
        listaIdentificacion.add("Pasaporte");
        listaIdentificacion.add("Carnet Extranjero");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaIdentificacion);
        spinnerTipoIdent.setAdapter(adapter);
    }

    public void cargarListaEstadoCivil(View v){
        listaEstadoCivil.add("Casado(a)");
        listaEstadoCivil.add("Soltero(a)");
        listaEstadoCivil.add("Divorciado(a)");
        listaEstadoCivil.add("Viudo(a)");
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
        spinnerDptos.setAdapter(adapter);
    }
    public void cargarListaExtensiones(View v){
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

    private void registrarCoSol() {
        StringRequest request = new StringRequest(Request.Method.POST, URL_add_cosol, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    if(response.contains("algo salio mal")){
                        Toast.makeText(getContext(),"No se pudo completar el registro debido a un error",Toast.LENGTH_LONG).show();
                    }
                    else{Toast.makeText(getContext(),"Co-Solicitante registrado correctamente",Toast.LENGTH_LONG).show();}

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
                parametros.put("relacion",relacionCoSol.getText().toString());
                parametros.put("nombres",nombre.getText().toString());
                parametros.put("apellidoP",apellidoP.getText().toString());
                parametros.put("apellidoM",apellidoM.getText().toString());
                parametros.put("apellidoC",apellidoCasada.getText().toString());
                parametros.put("tipo_identificacion",spinnerTipoIdent.getSelectedItem().toString());
                parametros.put("nro_documento",ci.getText().toString());
                parametros.put("extension",spinnerExtension.getSelectedItem().toString());
                parametros.put("fecha_nacimiento",etFechaNac.getText().toString());
                parametros.put("sexo",rbSelectedGenero.getText().toString());
                parametros.put("estado_civil",spinnerEstadoCivil.getSelectedItem().toString());
                parametros.put("nivel_estudio",spinnerNivelEstudio.getSelectedItem().toString());
                parametros.put("profesion_ocupacion",profesion.getText().toString());
                parametros.put("nacionalidad",nacionalidad.getText().toString());
                parametros.put("pais",pais.getText().toString());
                parametros.put("departamento",spinnerDptos.getSelectedItem().toString());
                parametros.put("telf_fijo",telFijo.getText().toString());
                parametros.put("telf_movil",telMovil.getText().toString());
                parametros.put("fijo_oficina",fijoOfi.getText().toString());
                parametros.put("movil_oficina",movilOfi.getText().toString());
                parametros.put("correo",correo.getText().toString());
                parametros.put("nombre_empresa",empresa.getText().toString());
                parametros.put("direccion_empresa",direccionEmpresa.getText().toString());
                parametros.put("rubro",rubro.getText().toString());
                parametros.put("ingresos",ingresos.getText().toString());
                parametros.put("moneda_ingresos",rbSelectedIngreso.getText().toString());
                parametros.put("asesor",asesor.getText().toString());
                parametros.put("fecha",fechaHoy());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
        habilitar();
    }

    private void generarPDF() {

        PdfDocument myPDF = new PdfDocument();
        Paint myPaint = new Paint();
        Paint titlePaint = new Paint();

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(40f);
        myPaint.setColor(Color.BLACK);
        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));

        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTextSize(35f);
        titlePaint.setColor(Color.BLACK);
        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));

        ////definimos pagina 1
        PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(2539,3874,1).create();
        PdfDocument.Page myPage1 = myPDF.startPage(myPageInfo1);
        Canvas canvas = myPage1.getCanvas();

        //dibujamos el formulario
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.form4a);
        scaledbmp = Bitmap.createScaledBitmap(bmp,2539,3874,false);
        canvas.drawBitmap(scaledbmp,0,0,myPaint);

        canvas.drawText(relacionCoSol.getText().toString(),850,620,myPaint);
        canvas.drawText(apellidoP.getText().toString().toUpperCase(),400,810,myPaint);
        canvas.drawText(apellidoM.getText().toString().toUpperCase(),1600,810,myPaint);
        canvas.drawText(nombre.getText().toString().toUpperCase(),400,970,myPaint);
        canvas.drawText(apellidoCasada.getText().toString().toUpperCase(),1600,970,myPaint);
        canvas.drawText(ci.getText().toString(),1600,1150,myPaint);
        canvas.drawText(spinnerExtension.getSelectedItem().toString(),2080,1150,myPaint);
        canvas.drawText(spinnerTipoIdent.getSelectedItem().toString(),650,1135,myPaint);
        canvas.drawText(etFechaNac.getText().toString(),650,1185,myPaint);
        canvas.drawText(rbSelectedGenero.getText().toString(),650,1235,myPaint);
        canvas.drawText(spinnerEstadoCivil.getSelectedItem().toString(),650,1285,myPaint);
        canvas.drawText(spinnerNivelEstudio.getSelectedItem().toString(),650,1335,myPaint);
        canvas.drawText(profesion.getText().toString(),650,1395,myPaint);
        canvas.drawText(nacionalidad.getText().toString(),650,1445,myPaint);

        canvas.drawText(pais.getText().toString(),650,1560,myPaint);
        if(spinnerDptos.getSelectedItem().toString().contains("Ninguno")){
            canvas.drawText("",650,1605,myPaint);
        }else {
            canvas.drawText(spinnerDptos.getSelectedItem().toString(),650,1605,myPaint);
        }

        canvas.drawText(telFijo.getText().toString(),250,1805,myPaint);
        canvas.drawText(telMovil.getText().toString(),750,1805,myPaint);
        canvas.drawText(fijoOfi.getText().toString(),250,1960,myPaint);
        canvas.drawText(movilOfi.getText().toString(),750,1960,myPaint);
        canvas.drawText(correo.getText().toString(),250,2110,myPaint);

        canvas.drawText(empresa.getText().toString(),1350,1805,myPaint);
        canvas.drawText(direccionEmpresa.getText().toString(),1350,1960,myPaint);
        canvas.drawText(rubro.getText().toString(),1350,2110,myPaint);
        canvas.drawText(ingresos.getText().toString()+" "+rbSelectedIngreso.getText().toString(),2080,2110,myPaint);

        canvas.drawText(nombre.getText().toString().toUpperCase()+" "+apellidoP.getText().toString().toUpperCase()+" "+
                apellidoM.getText().toString().toUpperCase(),550,3000,titlePaint);
        canvas.drawText(asesor.getText().toString().toUpperCase(),1870,3000,titlePaint);

        myPDF.finishPage(myPage1);
        /// fin pagina 1

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
    }

    ///fin del fragment
}
package com.example.novitierraapp;

import androidx.core.app.ActivityCompat;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class FormCoSolicitante extends Fragment {

    EditText relacionCoSol,nombre,apellidoP,apellidoM,apellidoCasada,ci,profesion,extension,nacionalidad,telFijo,telMovil,fijoOfi,movilOfi,correo,empresa,direccionEmpresa,rubro,etFechaNac,asesor;
    Spinner spinnerTipoIdent, spinnerEstadoCivil, spinnerNivelEstudio, spinnerDptos;
    DatePickerDialog datePickerDialog;
    RadioGroup radioGroupGenero;
    RadioButton rbSelectedGenero, rbMasculinoCoSol,rbFemeninoCoSol;
    Button btGenerarPdf, btFechaNac;
    ArrayList<String> listaIdentificacion = new ArrayList<>();
    ArrayList<String> listaEstadoCivil = new ArrayList<>();
    ArrayList<String> listaNivelEstudio = new ArrayList<>();
    ArrayList<String> listaTipoVivienda = new ArrayList<>();
    ArrayList<String> listaDpto = new ArrayList<>();

    Bitmap bmp, scaledbmp;
    int pageWidth=1200;

    private FormCoSolicitanteViewModel mViewModel;

    public static FormCoSolicitante newInstance() {
        return new FormCoSolicitante();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ///necesario para poder compartir el pdf
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        /////

        relacionCoSol=view.findViewById(R.id.relacionCoSolTitular);
        nombre = view.findViewById(R.id.nombreCoSol);
        apellidoP = view.findViewById(R.id.apellidoPCoSol);
        apellidoM = view.findViewById(R.id.apellidoMCoSol);
        apellidoCasada = view.findViewById(R.id.apellidoCasadaCoSol);
        ci = view.findViewById(R.id.ciCoSol);
        extension = view.findViewById(R.id.extensionCoSol);
        profesion=view.findViewById(R.id.profesionCoSol);
        nacionalidad = view.findViewById(R.id.nacionalidadCoSol);
        telFijo=view.findViewById(R.id.telfFijoCoSol);
        telMovil=view.findViewById(R.id.telfMovilCoSol);
        fijoOfi=view.findViewById(R.id.telfFijoOficinaCoSol);
        movilOfi=view.findViewById(R.id.telfMovilOficinaCoSol);
        correo=view.findViewById(R.id.correoCoSol);
        empresa=view.findViewById(R.id.empresaNombreCoSol);
        direccionEmpresa=view.findViewById(R.id.direccionEmpresaCoSol);
        rubro=view.findViewById(R.id.rubroEmpresaCoSol);
        etFechaNac=view.findViewById(R.id.fechaNacimientoCoSol);
        asesor=view.findViewById(R.id.fullnameAsesor);
        spinnerTipoIdent=view.findViewById(R.id.tipoIdentificacionCoSol);
        spinnerEstadoCivil=view.findViewById(R.id.estadoCivilCoSol);
        spinnerNivelEstudio=view.findViewById(R.id.nivelEstudioCoSol);
        spinnerDptos=view.findViewById(R.id.dptoBoliviaCoSol);
        rbMasculinoCoSol=view.findViewById(R.id.masculinoCoSol);
        rbFemeninoCoSol=view.findViewById(R.id.femeninoCoSol);
        radioGroupGenero=view.findViewById(R.id.radiogroupGeneroCoSol);
        btFechaNac=view.findViewById(R.id.btDatePickerFechaNacCoSol);
        btGenerarPdf=view.findViewById(R.id.btguardarCoSol);

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

        etFechaNac.setText(fechaHoy());
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

        ////para el boton del pdf
        ActivityCompat.requestPermissions(getActivity(),new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        btGenerarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rbMasculinoCoSol.isChecked()|| rbFemeninoCoSol.isChecked()){
                    generarPDF(v);
                    Toast.makeText(getContext(),"PDF Generado",Toast.LENGTH_SHORT).show();
                }else{Toast.makeText(getContext(),"Falta seleccionar Masculino o Femenino.",Toast.LENGTH_SHORT).show();}
            }
        });

    }


    private String fechaHoy(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month=month+1;
        int day= cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaDpto);
        spinnerDptos.setAdapter(adapter);
    }

    private void generarPDF(View v) {
        PdfDocument myPDF = new PdfDocument();
        Paint myPaint = new Paint();
        Paint titlePaint = new Paint();
        Paint titulos = new Paint();
        Paint centros = new Paint();
        Paint formas = new Paint();
        Paint formas2 = new Paint();

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(20f);
        myPaint.setColor(Color.BLACK);
        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));

        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTextSize(20f);
        titlePaint.setColor(Color.BLACK);

        titulos.setTextAlign(Paint.Align.CENTER);
        titulos.setTextSize(45f);
        titulos.setColor(Color.BLACK);
        titulos.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));

        centros.setTextAlign(Paint.Align.CENTER);
        centros.setTextSize(20f);
        centros.setColor(Color.BLACK);
        centros.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));

        formas.setColor(Color.BLACK);
        formas.setStyle(Paint.Style.STROKE);
        formas.setStrokeWidth(2);

        formas2.setColor(Color.BLACK);
        formas2.setStyle(Paint.Style.STROKE);
        formas2.setStrokeWidth(2);

        ////definimos pagina 1
        PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
        PdfDocument.Page myPage1 = myPDF.startPage(myPageInfo1);
        Canvas canvas = myPage1.getCanvas();

        //logo e imagen final
        ////dibujamos el logo principal
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        scaledbmp = Bitmap.createScaledBitmap(bmp,400,200,false);
        canvas.drawBitmap(scaledbmp,20,20,myPaint);

        canvas.drawText("DATOS DEL CO-SOLICITANTE",600,250,titulos);
        canvas.drawText("Codigo Cliente:",720,300,myPaint);
        canvas.drawRoundRect(720,320,1100,370,10,10,formas);

        canvas.drawText("Tipo de Relacion con el Titular:",100,420,myPaint);
        canvas.drawText(relacionCoSol.getText().toString(),440,420,myPaint);
        canvas.drawRoundRect(400,390,1100,440,10,10,formas2);

        canvas.drawText("Apellido Paterno:",100,480,myPaint);
        canvas.drawText(apellidoP.getText().toString(),250,530,myPaint);
        canvas.drawRoundRect(100,500,550,550,10,10,formas);
        canvas.drawText("Apellido Materno:",600,480,myPaint);
        canvas.drawText(apellidoM.getText().toString(),750,530,myPaint);
        canvas.drawRoundRect(600,500,1100,550,10,10,formas2);

        canvas.drawText("Nombres:",100,580,myPaint);
        canvas.drawText(nombre.getText().toString(),250,630,myPaint);
        canvas.drawRoundRect(100,600,550,650,10,10,formas);
        canvas.drawText("Apellido Casada:",600,580,myPaint);
        canvas.drawText(apellidoCasada.getText().toString(),750,630,myPaint);
        canvas.drawRoundRect(600,600,1100,650,10,10,formas2);

        canvas.drawText("Tipo de Identificacion:",100,680,myPaint);
        canvas.drawText(spinnerTipoIdent.getSelectedItem().toString(),320,680,titlePaint);
        canvas.drawText("N° de Documento:",100,710,myPaint);
        canvas.drawText(ci.getText().toString(),320,710,titlePaint);
        canvas.drawText("Fecha de Nacimiento:",100,740,myPaint);
        canvas.drawText(etFechaNac.getText().toString(),320,740,titlePaint);
        canvas.drawText("Sexo:",100,770,myPaint);
        canvas.drawText(rbSelectedGenero.getText().toString(),320,770,titlePaint);

        canvas.drawText("Estado Civil:",600,680,myPaint);
        canvas.drawText(spinnerEstadoCivil.getSelectedItem().toString(),820,680,titlePaint);
        canvas.drawText("Nivel de Estudio:",600,710,myPaint);
        canvas.drawText(spinnerNivelEstudio.getSelectedItem().toString(),820,710,titlePaint);
        canvas.drawText("Profesion/Ocupacion:",600,740,myPaint);
        canvas.drawText(profesion.getText().toString(),820,740,titlePaint);
        canvas.drawText("Nacionalidad:",600,770,myPaint);
        canvas.drawText(nacionalidad.getText().toString(),820,770,titlePaint);
        canvas.drawText("Departamento:",600,800,myPaint);
        canvas.drawText(spinnerDptos.getSelectedItem().toString(),820,800,titlePaint);

        canvas.drawText("Datos Telefonicos:",100,850,myPaint);
        canvas.drawText("Telefono Fijo:",100,870,myPaint);
        canvas.drawText(telFijo.getText().toString(),130,910,titlePaint);
        canvas.drawRoundRect(100,880,250,930,10,10,formas2);
        canvas.drawText("Telefono Movil:",300,870,myPaint);
        canvas.drawText(telMovil.getText().toString(),330,910,titlePaint);
        canvas.drawRoundRect(300,880,450,930,10,10,formas2);
        canvas.drawText("Oficina Fijo:",100,960,myPaint);
        canvas.drawText(fijoOfi.getText().toString(),130,1000,titlePaint);
        canvas.drawRoundRect(100,970,250,1020,10,10,formas2);
        canvas.drawText("Oficina Movil:",300,960,myPaint);
        canvas.drawText(movilOfi.getText().toString(),330,1000,titlePaint);
        canvas.drawRoundRect(300,970,450,1020,10,10,formas2);
        canvas.drawText("Correo:",100,1040,myPaint);
        canvas.drawText(correo.getText().toString(),150,1090,titlePaint);
        canvas.drawRoundRect(100,1060,450,1110,10,10,formas);

        canvas.drawText("Datos Laborales (Empresa):",600,870,myPaint);
        canvas.drawText(empresa.getText().toString(),650,910,titlePaint);
        canvas.drawRoundRect(600,880,1100,930,10,10,formas);
        canvas.drawText("Direccion:",600,960,myPaint);
        canvas.drawText(direccionEmpresa.getText().toString(),650,1000,titlePaint);
        canvas.drawRoundRect(600,970,1100,1020,10,10,formas2);
        canvas.drawText("Rubro:",600,1040,myPaint);
        canvas.drawText(rubro.getText().toString(),650,1090,titlePaint);
        canvas.drawRoundRect(600,1060,1100,1110,10,10,formas);

        canvas.drawText("Autorizo a Novitierra a confirmar los datos declarados en el presente formulario, recabando mis antecedentes ",100,1230,myPaint);
        canvas.drawText("personales y crediticios tales como, el informe de la central de riesgos de la autoridad de supervision del ",100,1250,myPaint);
        canvas.drawText("Sistema Financiero ASFI, y otros que estime necesarios por si misma y/o terceras personas.",100,1270,myPaint);

        canvas.drawText("Firma Cliente....................................................",100,1450,myPaint);
        canvas.drawText("Firma..................................................................",600,1450,myPaint);
        canvas.drawText(nombre.getText().toString()+" "+apellidoP.getText().toString()+" "+apellidoM.getText().toString(),300,1490,centros);
        canvas.drawText(asesor.getText().toString(),820,1490,centros);



        ////dibujamos la parte inferior
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.form1_parte_inferior);
        scaledbmp = Bitmap.createScaledBitmap(bmp,pageWidth,50,false);
        canvas.drawBitmap(scaledbmp,0,1800,myPaint);


        myPDF.finishPage(myPage1);
        /// fin pagina 1

        File file = new File(Environment.getExternalStorageDirectory(),"/Formulario_Co_Solicitante.pdf");
        try {
            myPDF.writeTo(new FileOutputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
        }
        myPDF.close();
///desde aca
        String path = Environment.getExternalStorageDirectory()+"/Formulario_Co_Solicitante.pdf";
        File pdf = new File(path);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdf));
        share.setType("application/pdf");
        startActivity(share);
    ///hasta aca
    }

    ///fin del fragment
}
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

import com.example.novitierraapp.entidades.Global;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class FormCoSolicitante extends Fragment {

    EditText relacionCoSol,nombre,apellidoP,apellidoM,apellidoCasada,ci,profesion,extension,nacionalidad,telFijo,telMovil,fijoOfi,movilOfi,correo,empresa,direccionEmpresa,rubro,etFechaNac,asesor,pais,ingresos;
    Spinner spinnerTipoIdent, spinnerEstadoCivil, spinnerNivelEstudio, spinnerDptos;
    DatePickerDialog datePickerDialog;
    RadioGroup radioGroupGenero, radioGroupIngresos;
    RadioButton rbSelectedGenero, rbMasculinoCoSol,rbFemeninoCoSol, rbingresoBs, rbingresoDolar, rbSelectedIngreso;
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
        rbMasculinoCoSol=view.findViewById(R.id.masculinoCoSol);
        rbFemeninoCoSol=view.findViewById(R.id.femeninoCoSol);
        rbingresoBs=view.findViewById(R.id.ingresosCoSolBs);
        rbingresoDolar=view.findViewById(R.id.ingresosCoSolDolar);
        radioGroupGenero=view.findViewById(R.id.radiogroupGeneroCoSol);
        radioGroupIngresos=view.findViewById(R.id.radiogroupIngresosCoSol);
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
        asesor.setText(Global.nombreSesion+" "+Global.apellidoSesion);

        ////para el boton del pdf
        ActivityCompat.requestPermissions(getActivity(),new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        btGenerarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbingresoBs.isChecked()|| rbingresoDolar.isChecked()){
                    if(rbMasculinoCoSol.isChecked()|| rbFemeninoCoSol.isChecked()){
                        if(!validarCamposObligatorios()){
                            generarPDF(v);
                            Toast.makeText(getContext(),"Generando PDF.......espere un momento",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(),"Rellene los campos marcados en *.",Toast.LENGTH_SHORT).show();
                        }

                    }else{Toast.makeText(getContext(),"Falta seleccionar Masculino o Femenino.",Toast.LENGTH_SHORT).show();}
                }else {Toast.makeText(getContext(),"Falta seleccionar Ingresos Bs o Dolar.",Toast.LENGTH_SHORT).show(); }
            }
        });

    }

    public Boolean validarCamposObligatorios(){
        if(nombre.getText().toString().length()==0 ||
                apellidoP.getText().toString().length()==0 ||
                apellidoM.getText().toString().length()==0 ||
                ci.getText().toString().length()==0 ||
                extension.getText().toString().length()==0 ||
                nacionalidad.getText().toString().length()==0 ||
                etFechaNac.getText().toString().length()==0 ||
                profesion.getText().toString().length()==0 ||
                nacionalidad.getText().toString().length()==0 ||
                telMovil.getText().toString().length()==0||
                asesor.getText().toString().length()==0
                ){

            return true;
        }else {
            return false;
        }

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
        listaDpto.add("Ninguno");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaDpto);
        spinnerDptos.setAdapter(adapter);
    }

    private void generarPDF(View v) {
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
        PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(2550,4200,1).create();
        PdfDocument.Page myPage1 = myPDF.startPage(myPageInfo1);
        Canvas canvas = myPage1.getCanvas();

        //dibujamos el formulario
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.form4a);
        scaledbmp = Bitmap.createScaledBitmap(bmp,2550,4200,false);
        canvas.drawBitmap(scaledbmp,0,0,myPaint);

        canvas.drawText(relacionCoSol.getText().toString(),850,670,myPaint);
        canvas.drawText(apellidoP.getText().toString(),400,870,myPaint);
        canvas.drawText(apellidoM.getText().toString(),1600,870,myPaint);
        canvas.drawText(nombre.getText().toString(),400,1050,myPaint);
        canvas.drawText(apellidoCasada.getText().toString(),1600,1050,myPaint);
        canvas.drawText(ci.getText().toString(),1600,1250,myPaint);
        canvas.drawText(extension.getText().toString(),2080,1250,myPaint);

        canvas.drawText(spinnerTipoIdent.getSelectedItem().toString(),650,1165,myPaint);
        canvas.drawText(etFechaNac.getText().toString(),650,1285,myPaint);
        canvas.drawText(rbSelectedGenero.getText().toString(),650,1340,myPaint);
        canvas.drawText(spinnerEstadoCivil.getSelectedItem().toString(),650,1390,myPaint);
        canvas.drawText(spinnerNivelEstudio.getSelectedItem().toString(),650,1450,myPaint);
        canvas.drawText(profesion.getText().toString(),650,1505,myPaint);
        canvas.drawText(nacionalidad.getText().toString(),650,1560,myPaint);

        canvas.drawText(pais.getText().toString(),650,1690,myPaint);
        if(spinnerDptos.getSelectedItem().toString().contains("Ninguno")){
            canvas.drawText("",650,1740,myPaint);
        }else {
            canvas.drawText(spinnerDptos.getSelectedItem().toString(),650,1740,myPaint);
        }


        canvas.drawText(telFijo.getText().toString(),250,1955,myPaint);
        canvas.drawText(telMovil.getText().toString(),750,1955,myPaint);
        canvas.drawText(fijoOfi.getText().toString(),250,2120,myPaint);
        canvas.drawText(movilOfi.getText().toString(),750,2120,myPaint);
        canvas.drawText(correo.getText().toString(),250,2280,myPaint);

        canvas.drawText(empresa.getText().toString(),1350,1955,myPaint);
        canvas.drawText(direccionEmpresa.getText().toString(),1350,2120,myPaint);
        canvas.drawText(rubro.getText().toString(),1350,2280,myPaint);
        canvas.drawText(ingresos.getText().toString()+" "+rbSelectedIngreso.getText().toString(),2080,2280,myPaint);

        canvas.drawText(nombre.getText().toString()+" "+apellidoP.getText().toString()+" "+
                apellidoM.getText().toString(),550,3250,titlePaint);
        canvas.drawText(asesor.getText().toString(),1870,3250,titlePaint);

        myPDF.finishPage(myPage1);
        /// fin pagina 1

        File file = new File(Environment.getExternalStorageDirectory(),"/Form_CoSolicitante.pdf");
        try {
            myPDF.writeTo(new FileOutputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
        }
        myPDF.close();
///desde aca
        String path = Environment.getExternalStorageDirectory()+"/Form_CoSolicitante.pdf";
        File pdf = new File(path);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_VIEW);
        share.setDataAndType(Uri.fromFile(pdf),"application/pdf");
        share.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        share.setAction(Intent.ACTION_SEND);
//        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdf));
//        share.setType("application/pdf");
        startActivity(share);
    ///hasta aca
    }

    ///fin del fragment
}
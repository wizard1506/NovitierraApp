package com.example.novitierraapp;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
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

    EditText nombre,apellidoP,apellidoM,apellidoCasada,ci,extension,nacionalidad,telFijo,telMovil,fijoOfi,movilOfi,correo,empresa,rubro,etFechaNac;
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
        nombre = view.findViewById(R.id.nombreCoSol);
        apellidoP = view.findViewById(R.id.apellidoPCoSol);
        apellidoM = view.findViewById(R.id.apellidoMCoSol);
        apellidoCasada = view.findViewById(R.id.apellidoCasadaCoSol);
        ci = view.findViewById(R.id.ciCoSol);
        extension = view.findViewById(R.id.extensionCoSol);
        nacionalidad = view.findViewById(R.id.nacionalidadCoSol);
        telFijo=view.findViewById(R.id.telfFijoCoSol);
        telMovil=view.findViewById(R.id.telfMovilCoSol);
        fijoOfi=view.findViewById(R.id.telfFijoOficinaCoSol);
        movilOfi=view.findViewById(R.id.telfMovilOficinaCoSol);
        correo=view.findViewById(R.id.correoCoSol);
        empresa=view.findViewById(R.id.empresaNombreCoSol);
        rubro=view.findViewById(R.id.rubroEmpresaCoSol);
        etFechaNac=view.findViewById(R.id.fechaNacimientoCoSol);
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
        Paint formas = new Paint();
        Paint formas2 = new Paint();

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(20f);
        myPaint.setColor(Color.BLACK);
        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));

        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTextSize(20f);
        titlePaint.setColor(Color.BLACK);

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
        ////dibujamos la parte inferior
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.form1_parte_inferior);
        scaledbmp = Bitmap.createScaledBitmap(bmp,pageWidth,50,false);
        canvas.drawBitmap(scaledbmp,0,600,myPaint);


        myPDF.finishPage(myPage1);
        /// fin pagina 1

        File file = new File(Environment.getExternalStorageDirectory(),"/Formulario_Co_Solicitante.pdf");
        try {
            myPDF.writeTo(new FileOutputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
        }
        myPDF.close();

    }

    ///fin del fragment
}
package com.example.novitierraapp;

import androidx.annotation.RequiresApi;
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
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StrictMode;
import android.system.ErrnoException;
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

import com.example.novitierraapp.entidades.Proyectos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;

public class Formularios extends Fragment {

    private FormulariosViewModel mViewModel;
    EditText nombre_cliente, apellidoPaterno, apellidoMaterno, ci_cliente, extension_cliente,uv,mz,lt,cat,asesor,codigo_asesor,fechaNac, apellidoCasada,prefijo,nacionalidad,profesion,costoAprox;
    EditText propietarioVivienta,telefonoPropietario,pais,ciudad,barrio,avenida,calle,numero,telFijo,telMovil,telFijoOfc,telMovOfc,correoPersonal,expedido,mts2;
    EditText nombreEmpresa, rubroEmpresa, direccionEmpresa, ingresosEmpresa,primerReferencia,segundaReferencia,telfReferencia1,telfReferencia2,parentesco,relacion;
    RadioGroup radioGroup, radioGroupGenero, radioGroupVivienda, radioGroupIngresos;
    RadioButton rb_plazo, rb_contado, rbSelected, rbMasculino, rbFemenino, rbSelectedGenero,rbSelectedMonedaVivienda,rbViviendaBs,rbViviendaDolar, rbIngresosBs, rbIngresosDolar,rbSelectedIngresos;
    Spinner spinner_urbanizacion, spinnerIdentificacion,spinnerEstadoCivil,spinnerNivelEstudio, spinnerTipoVivienda, spinnerDpto, spinnerTenencia;
    TextView codigo_proyecto;
    Button guardar, btFechaNac;
    Proyectos proyectos;
    DatePickerDialog datePickerDialog;
    ArrayList<Proyectos> listaProyectos = new ArrayList<>();
    ArrayList<String> listaIdentificacion = new ArrayList<>();
    ArrayList<String> listaEstadoCivil = new ArrayList<>();
    ArrayList<String> listaNivelEstudio = new ArrayList<>();
    ArrayList<String> listaTipoVivienda = new ArrayList<>();
    ArrayList<String> listaDpto = new ArrayList<>();
    ArrayList<String> listaTenencia = new ArrayList<>();
    Bitmap bmp, scaledbmp;

    int pageWidth=1200;

    public static Formularios newInstance() {
        return new Formularios();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ///necesario para poder compartir el pdf
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        /////


        nombre_cliente = view.findViewById(R.id.nombreCliente);
        apellidoPaterno= view.findViewById(R.id.apellidoPCliente);
        apellidoMaterno= view.findViewById(R.id.apellidoMCliente);
        apellidoCasada = view.findViewById(R.id.apellidoCasada);
        prefijo=view.findViewById(R.id.prefijo);
        ci_cliente = view.findViewById(R.id.ciCliente);
        expedido=view.findViewById(R.id.expedido);
        extension_cliente = view.findViewById(R.id.extensionCliente);
        nacionalidad = view.findViewById(R.id.nacionalidad);
        fechaNac = view.findViewById(R.id.fechaNacimiento);
        profesion = view.findViewById(R.id.profesion);
        costoAprox = view.findViewById(R.id.costoAprox);
        propietarioVivienta=view.findViewById(R.id.propietarioVivienda);
        telefonoPropietario=view.findViewById(R.id.telefonoPropietario);
        pais=view.findViewById(R.id.paisVivienda);
        ciudad=view.findViewById(R.id.ciudadVivienda);
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
        radioGroupVivienda = view.findViewById(R.id.radiogroupVivienda);
        radioGroupIngresos = view.findViewById(R.id.radiogroupIngresos);

        rb_plazo = view.findViewById(R.id.aPlazo);
        rb_contado= view.findViewById(R.id.aContado);
        rbMasculino= view.findViewById(R.id.masculino);
        rbFemenino= view.findViewById(R.id.femenino);
        rbViviendaBs=view.findViewById(R.id.viviendaBs);
        rbViviendaDolar=view.findViewById(R.id.viviendaDolar);
        rbIngresosBs=view.findViewById(R.id.ingresosBs);
        rbIngresosDolar=view.findViewById(R.id.ingresosDolar);


        spinner_urbanizacion= view.findViewById(R.id.urbanizacion);
        spinnerIdentificacion= view.findViewById(R.id.tipoIdentificacion);
        spinnerEstadoCivil= view.findViewById(R.id.estadoCivil);
        spinnerNivelEstudio= view.findViewById(R.id.nivelEstudio);
        spinnerTipoVivienda= view.findViewById(R.id.tipoVivienda);
        spinnerDpto= view.findViewById(R.id.dptoBolivia);
        spinnerTenencia= view.findViewById(R.id.tenencia);

        codigo_proyecto = view.findViewById(R.id.idProyecto);
        guardar = view.findViewById(R.id.btguardar);
        btFechaNac = view.findViewById(R.id.btDatePickerFechaNac);
        fechaNac.setText(fechaHoy());
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
        rbViviendaBs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbMonedaVivienda(v);
            }
        });
        rbViviendaDolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbMonedaVivienda(v);
            }
        });
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
        /////cargamos los spinners
        cargarListaTipoIdentificacion(view);
        cargarListaEstadoCivil(view);
        cargarListaUrbanizacion(view);
        cargarListaNivelEstudio(view);
        cargarListaTipoVivienda(view);
        cargarListaDpto(view);
        cargarListaTenencia(view);
        spinner_urbanizacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codigo_proyecto.setText(listaProyectos.get(position).getCodigo().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ////para el boton del pdf
        ActivityCompat.requestPermissions(getActivity(),new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_contado.isChecked()||rb_plazo.isChecked()){
                    if (rbMasculino.isChecked()||rbFemenino.isChecked()){
                        if ( rbIngresosBs.isChecked()||rbIngresosDolar.isChecked()   ){
                            if(rbViviendaBs.isChecked()|| rbViviendaDolar.isChecked()){
                                generarPDF(v);
                                Toast.makeText(getContext(),"PDF Generado",Toast.LENGTH_SHORT).show();
                            }else{Toast.makeText(getContext(),"Falta seleccionar costo de vivienda en Bs o $us.",Toast.LENGTH_SHORT).show();}
                        }else{Toast.makeText(getContext(),"Falta seleccionar Ingresos en Bs o $us.",Toast.LENGTH_SHORT).show();}
                    }else{Toast.makeText(getContext(),"Falta seleccionar Masculino o Femenino.",Toast.LENGTH_SHORT).show();}
                }else{
                Toast.makeText(getContext(),"Falta seleccionar plazo o contado.",Toast.LENGTH_SHORT).show();
                }
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

    private void rbMonedaVivienda(View v) {
        int radiobtid = radioGroupVivienda.getCheckedRadioButtonId();
        rbSelectedMonedaVivienda = v.findViewById(radiobtid);
    }
    private void rbSeleccionIngresos(View v) {
        int radiobtid = radioGroupIngresos.getCheckedRadioButtonId();
        rbSelectedIngresos = v.findViewById(radiobtid);
    }


    public void cargarListaUrbanizacion(View v){

        listaProyectos.add(new Proyectos(100,"La Enconada II"));
        listaProyectos.add(new Proyectos(101,"La Pascana de Cotoca"));
        listaProyectos.add(new Proyectos(200,"La Pascana de Cotoca II"));
        listaProyectos.add(new Proyectos(201,"La Tierra Prometida"));
        listaProyectos.add(new Proyectos(204,"AME TAUNA"));
        listaProyectos.add(new Proyectos(205,"AME TAUNA I"));
        ArrayAdapter<Proyectos> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaProyectos);
        spinner_urbanizacion.setAdapter(adapter);
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

/////boton generador de pdf/////
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void generarPDF (View v){

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

            ////PAGINA 1 //////

            //logo e imagen final
            ////dibujamos el logo principal
            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
            scaledbmp = Bitmap.createScaledBitmap(bmp,400,200,false);
            canvas.drawBitmap(scaledbmp,20,20,myPaint);
            ////dibujamos la parte inferior
            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.form1_parte_inferior);
            scaledbmp = Bitmap.createScaledBitmap(bmp,pageWidth,50,false);
            canvas.drawBitmap(scaledbmp,0,600,myPaint);

            canvas.drawText("Urbanizacion: ",350,190,myPaint);
            canvas.drawText(spinner_urbanizacion.getSelectedItem().toString(),500,190,titlePaint);
            canvas.drawRoundRect(480,160,1100,200,10,10,formas);
            canvas.drawText("Nombre del Cliente: ",100,250,myPaint);
            canvas.drawText(nombre_cliente.getText().toString()+" "+apellidoPaterno.getText().toString()+" "+apellidoMaterno.getText().toString(),300,250,titlePaint);
            canvas.drawRoundRect(290,220,1100,260,10,10,formas2);
            canvas.drawText("Documento de Identidad: ",100,310,myPaint);
            canvas.drawText(ci_cliente.getText().toString(),360,310,titlePaint);
            canvas.drawRoundRect(340,280,500,320,10,10,formas);
            canvas.drawText("Extension: ",550,310,myPaint);
            canvas.drawText(extension_cliente.getText().toString(),660,310,titlePaint);
            canvas.drawRoundRect(650,280,770,320,10,10,formas2);
            canvas.drawText("N° de Reserva: ",800,310,myPaint);
            canvas.drawRoundRect(950,280,1100,320,10,10,formas);
            canvas.drawText("Codigo del Cliente: ",100,370,myPaint);
            canvas.drawRoundRect(290,340,390,380,10,10,formas2);
            canvas.drawText("Pago a: ",400,370,myPaint);
            canvas.drawText(rbSelected.getText().toString(),500,370,titlePaint);
            canvas.drawRoundRect(480,340,590,380,10,10,formas);
            canvas.drawText("N° de Contrato: ",630,370,myPaint);
            canvas.drawRoundRect(790,340,1100,380,10,10,formas2);
            canvas.drawText("Proyecto: ",100,430,myPaint);
            canvas.drawText(codigo_proyecto.getText().toString(),220,430,titlePaint);
            canvas.drawRoundRect(200,400,280,440,10,10,formas);
            canvas.drawText("UV: ",300,430,myPaint);
            canvas.drawText(uv.getText().toString(),360,430,titlePaint);
            canvas.drawRoundRect(340,400,420,440,10,10,formas);
            canvas.drawText("Mz: ",460,430,myPaint);
            canvas.drawText(mz.getText().toString(),540,430,titlePaint);
            canvas.drawRoundRect(520,400,600,440,10,10,formas);
            canvas.drawText("Lote: ",640,430,myPaint);
            canvas.drawText(lt.getText().toString(),720,430,titlePaint);
            canvas.drawRoundRect(700,400,780,440,10,10,formas);
            canvas.drawText("Categoria: ",820,430,myPaint);
            canvas.drawText(cat.getText().toString(),940,430,titlePaint);
            canvas.drawRoundRect(920,400,1100,440,10,10,formas);
            canvas.drawText("Nombre y Apellido del Asesor de Inversion: ",100,490,myPaint);
            canvas.drawText(asesor.getText().toString(),520,490,titlePaint);
            canvas.drawRoundRect(510,460,1100,500,10,10,formas);
            canvas.drawText("Codigo Asesor: ",100,550,myPaint);
            canvas.drawText(codigo_asesor.getText().toString(),260,550,titlePaint);
            canvas.drawRoundRect(240,520,400,560,10,10,formas2);
            myPDF.finishPage(myPage1);
            //// FIN PAGINA 1/////

            //////PAGINA 2
        ////definimos pagina 2
            PdfDocument.PageInfo myPageInfo2 = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
            PdfDocument.Page myPage2 = myPDF.startPage(myPageInfo2);
            Canvas canvas2 = myPage2.getCanvas();

            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
            scaledbmp = Bitmap.createScaledBitmap(bmp,400,200,false);
            canvas2.drawBitmap(scaledbmp,20,20,myPaint);

            titlePaint.setTextAlign(Paint.Align.CENTER);
            titlePaint.setTextSize(40f);
            titlePaint.setColor(Color.BLACK);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
            canvas2.drawText("DATOS PERSONALES DEL CLIENTE",600,200,titlePaint);

            titlePaint.setTextAlign(Paint.Align.LEFT);
            titlePaint.setTextSize(20f);
            titlePaint.setColor(Color.BLACK);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));

            canvas2.drawText("Codigo Cliente",850,250,myPaint);
            canvas2.drawRoundRect(1100,260,850,310,10,10,formas2);
            canvas2.drawText("Apellido Paterno",100,250,myPaint);
            canvas2.drawText(apellidoPaterno.getText().toString(),210,285,titlePaint);
            canvas2.drawRoundRect(100,260,400,310,10,10,formas);
            canvas2.drawText("Apellido Materno",500,250,myPaint);
            canvas2.drawText(apellidoMaterno.getText().toString(),610,285,titlePaint);
            canvas2.drawRoundRect(500,260,800,310,10,10,formas2);
            canvas2.drawText("Nombres",100,330,myPaint);
            canvas2.drawText(nombre_cliente.getText().toString(),210,375,titlePaint);
            canvas2.drawRoundRect(100,340,400,390,10,10,formas);
            canvas2.drawText("Apellido de Casada",500,330,myPaint);
            canvas2.drawText(apellidoCasada.getText().toString(),610,375,titlePaint);
            canvas2.drawRoundRect(500,340,800,390,10,10,formas2);
            canvas2.drawText("Prefijo",850,330,myPaint);
            canvas2.drawText(prefijo.getText().toString(),960,375,titlePaint);
            canvas2.drawRoundRect(850,340,1100,390,10,10,formas);

            canvas2.drawText("Tipo de Identificacion:",100,420,myPaint);
            canvas2.drawText(spinnerIdentificacion.getSelectedItem().toString(),320,420,titlePaint);
            canvas2.drawText("N° de Documento:",100,440,myPaint);
            canvas2.drawText(ci_cliente.getText().toString(),320,440,titlePaint);
            canvas2.drawText("Extension:",100,460,myPaint);
            canvas2.drawText(extension_cliente.getText().toString(),320,460,titlePaint);
            canvas2.drawText("Nacionalidad:",100,480,myPaint);
            canvas2.drawText(nacionalidad.getText().toString(),320,480,titlePaint);
            canvas2.drawText("Fecha de Nacimiento:",100,500,myPaint);
            canvas2.drawText(fechaNac.getText().toString(),320,500,titlePaint);
            canvas2.drawText("Estado Civil:",100,520,myPaint);
            canvas2.drawText(spinnerEstadoCivil.getSelectedItem().toString(),320,520,titlePaint);
            canvas2.drawText("Sexo:",100,540,myPaint);
            canvas2.drawText(rbSelectedGenero.getText().toString(),320,540,titlePaint);
            canvas2.drawText("Nivel de Estudio:",600,440,myPaint);
            canvas2.drawText(spinnerNivelEstudio.getSelectedItem().toString(),820,440,titlePaint);
            canvas2.drawText("Profesion/Ocupacion:",600,460,myPaint);
            canvas2.drawText(profesion.getText().toString(),820,460,titlePaint);
            canvas2.drawText("Datos de Vivienda",600,480,myPaint);
            canvas2.drawText("Tipo de Vivienda:",600,500,myPaint);
            canvas2.drawText(spinnerTipoVivienda.getSelectedItem().toString(),820,500,titlePaint);
            canvas2.drawText("Tenencia:",600,520,myPaint);
            canvas2.drawText(spinnerTenencia.getSelectedItem().toString(),820,520,titlePaint);
            canvas2.drawRoundRect(95,400,1100,550,10,10,formas);

            canvas2.drawText("Costo Aproximado:",100,570,myPaint);
            canvas2.drawText(costoAprox.getText().toString()+" "+rbSelectedMonedaVivienda.getText().toString(),210,610,titlePaint);
            canvas2.drawRoundRect(100,580,400,620,10,10,formas2);
            canvas2.drawText("Nombre Propietario:",500,570,myPaint);
            canvas2.drawText(propietarioVivienta.getText().toString(),550,610,titlePaint);
            canvas2.drawRoundRect(500,580,800,620,10,10,formas);
            canvas2.drawText("Telefono:",850,570,myPaint);
            canvas2.drawText(telefonoPropietario.getText().toString(),935,610,titlePaint);
            canvas2.drawRoundRect(850,580,1100,620,10,10,formas2);

            canvas2.drawText("Pais:",100,650,myPaint);
            canvas2.drawText(pais.getText().toString(),200,650,titlePaint);
            canvas2.drawText("Dpto:",400,650,myPaint);
            canvas2.drawText(spinnerDpto.getSelectedItem().toString(),480,650,titlePaint);
            canvas2.drawText("Ciudad:",700,650,myPaint);
            canvas2.drawText(ciudad.getText().toString(),780,650,titlePaint);
            canvas2.drawText("Barrio:",100,670,myPaint);
            canvas2.drawText(barrio.getText().toString(),200,670,titlePaint);
            canvas2.drawText("Avenida:",100,690,myPaint);
            canvas2.drawText(avenida.getText().toString(),200,690,titlePaint);
            canvas2.drawText("Calle:",100,710,myPaint);
            canvas2.drawText(calle.getText().toString(),200,710,titlePaint);
            canvas2.drawText("Numero:",100,730,myPaint);
            canvas2.drawText(numero.getText().toString(),200,730,titlePaint);
            canvas2.drawRoundRect(95,630,1100,740,10,10,formas2);

            canvas2.drawText("Telefono Fijo:",100,770,myPaint);
            canvas2.drawText(telFijo.getText().toString(),320,770,titlePaint);
            canvas2.drawText("Telefono Movil:",600,770,myPaint);
            canvas2.drawText(telMovil.getText().toString(),840,770,titlePaint);
            canvas2.drawText("Telefono Fijo-Oficina:",100,800,myPaint);
            canvas2.drawText(telFijoOfc.getText().toString(),320,800,titlePaint);
            canvas2.drawText("Telefono Movil-Oficina:",600,800,myPaint);
            canvas2.drawText(telMovOfc.getText().toString(),840,800,titlePaint);
            canvas2.drawText("Correo:",100,830,myPaint);
            canvas2.drawText(correoPersonal.getText().toString(),180,830,titlePaint);
            canvas2.drawRoundRect(95,750,1100,840,10,10,formas);

            canvas2.drawText("Datos Laborales (Empresa):",100,870,myPaint);
            canvas2.drawText(nombreEmpresa.getText().toString(),380,870,titlePaint);
            canvas2.drawText("Direccion:",100,900,myPaint);
            canvas2.drawText(direccionEmpresa.getText().toString(),380,900,titlePaint);
            canvas2.drawText("Rubro:",100,930,myPaint);
            canvas2.drawText(rubroEmpresa.getText().toString(),380,930,titlePaint);
            canvas2.drawText("Ingresos:",100,960,myPaint);
            canvas2.drawText(ingresosEmpresa.getText().toString()+" "+rbSelectedIngresos.getText().toString(),380,960,titlePaint);
            canvas2.drawRoundRect(95,850,1100,970,10,10,formas);

            canvas2.drawText("Referencias Personales",100,990,myPaint);
            canvas2.drawText("Nombre Conyuge/Familiar Cercano:",100,1010,myPaint);
            canvas2.drawText(primerReferencia.getText().toString(),140,1045,titlePaint);
            canvas2.drawRoundRect(100,1020,490,1060,10,10,formas2);
            canvas2.drawText("Parentesco:",500,1010,myPaint);
            canvas2.drawText(parentesco.getText().toString(),540,1045,titlePaint);
            canvas2.drawRoundRect(500,1020,690,1060,10,10,formas2);
            canvas2.drawText("Telefono:",700,1010,myPaint);
            canvas2.drawText(telfReferencia1.getText().toString(),740,1045,titlePaint);
            canvas2.drawRoundRect(700,1020,1100,1060,10,10,formas2);
            canvas2.drawText("Nombre otra referencia:",100,1090,myPaint);
            canvas2.drawText(segundaReferencia.getText().toString(),140,1125,titlePaint);
            canvas2.drawRoundRect(100,1100,490,1140,10,10,formas2);
            canvas2.drawText("Relacion:",500,1090,myPaint);
            canvas2.drawText(relacion.getText().toString(),540,1125,titlePaint);
            canvas2.drawRoundRect(500,1100,690,1140,10,10,formas2);
            canvas2.drawText("Telefono:",700,1090,myPaint);
            canvas2.drawText(telfReferencia2.getText().toString(),740,1125,titlePaint);
            canvas2.drawRoundRect(700,1100,1100,1140,10,10,formas2);

            canvas2.drawText("Croquis de Ubicacion:",500,1170,myPaint);
            ////croquis///
            canvas2.drawRoundRect(100,1190,200,1240,10,10,formas);
            canvas2.drawRoundRect(250,1190,350,1240,10,10,formas2);
            canvas2.drawRoundRect(400,1190,500,1240,10,10,formas);
            canvas2.drawRoundRect(550,1190,650,1240,10,10,formas2);
            canvas2.drawRoundRect(700,1190,800,1240,10,10,formas);
            canvas2.drawRoundRect(850,1190,950,1240,10,10,formas2);
            canvas2.drawRoundRect(1000,1190,1100,1240,10,10,formas);

            canvas2.drawRoundRect(100,1260,200,1310,10,10,formas);
            canvas2.drawRoundRect(250,1260,350,1310,10,10,formas2);
            canvas2.drawRoundRect(400,1260,500,1310,10,10,formas);
            canvas2.drawRoundRect(550,1260,650,1310,10,10,formas2);
            canvas2.drawRoundRect(700,1260,800,1310,10,10,formas);
            canvas2.drawRoundRect(850,1260,950,1310,10,10,formas2);
            canvas2.drawRoundRect(1000,1260,1100,1310,10,10,formas);

            canvas2.drawRoundRect(100,1330,200,1380,10,10,formas);
            canvas2.drawRoundRect(250,1330,350,1380,10,10,formas2);
            canvas2.drawRoundRect(400,1330,500,1380,10,10,formas);
            canvas2.drawRoundRect(550,1330,650,1380,10,10,formas2);
            canvas2.drawRoundRect(700,1330,800,1380,10,10,formas);
            canvas2.drawRoundRect(850,1330,950,1380,10,10,formas2);
            canvas2.drawRoundRect(1000,1330,1100,1380,10,10,formas);

            canvas2.drawRoundRect(100,1400,200,1450,10,10,formas);
            canvas2.drawRoundRect(250,1400,350,1450,10,10,formas2);
            canvas2.drawRoundRect(400,1400,500,1450,10,10,formas);
            canvas2.drawRoundRect(550,1400,650,1450,10,10,formas2);
            canvas2.drawRoundRect(700,1400,800,1450,10,10,formas);
            canvas2.drawRoundRect(850,1400,950,1450,10,10,formas2);
            canvas2.drawRoundRect(1000,1400,1100,1450,10,10,formas);

            canvas2.drawText("OBSERVACIONES:",100,1480,myPaint);
            canvas2.drawRoundRect(95,1460,1100,1530,10,10,formas2);
            canvas2.drawText("Autorizo a Novitierra a confirmar los datos declarados en el presente formulario y recabar antecedentes",100,1550,titlePaint);
            canvas2.drawText("personales y crediticios tales como el informe de la central de riesgos de la autoridad de supervision del",100,1580,titlePaint);
            canvas2.drawText("sistema financiero ASFI y otros que estimara necesarios por si misma y/o terceras personas.",100,1610,titlePaint);

            canvas2.drawText("Firma cliente.................................................................",100,1670,myPaint);
            canvas2.drawText("Firma.............................................................................",620,1670,myPaint);
            canvas2.drawText("Nombre y Apellidos:",100,1710,myPaint);
            canvas2.drawText(nombre_cliente.getText().toString()+" "+apellidoPaterno.getText().toString()+" "+apellidoMaterno.getText().toString(),300,1710,titlePaint);
            canvas2.drawText("Asesor de Inversion:",100,1750,myPaint);
            canvas2.drawText(asesor.getText().toString(),300,1750,titlePaint);

            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.form1_parte_inferior);
            scaledbmp = Bitmap.createScaledBitmap(bmp,pageWidth,50,false);
            canvas2.drawBitmap(scaledbmp,0,1780,myPaint);

            myPDF.finishPage(myPage2);
//            //////FIN PAGINA 2 /////


        ////// INICIA PAGINA 3 ///////
        PdfDocument.PageInfo myPageInfo3 = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
        PdfDocument.Page myPage3 = myPDF.startPage(myPageInfo3);
        Canvas canvas3 = myPage3.getCanvas();

        titulos.setTextAlign(Paint.Align.CENTER);
        titulos.setTextSize(45f);
        titulos.setColor(Color.BLACK);
        titulos.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));


        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        scaledbmp = Bitmap.createScaledBitmap(bmp,400,200,false);
        canvas3.drawBitmap(scaledbmp,20,20,myPaint);

        canvas3.drawText("ACTA DE ENTREGA DE LOTE",600,200,titulos);
        titulos.setTextAlign(Paint.Align.LEFT);
        titulos.setTextSize(30f);
        titulos.setColor(Color.BLACK);
        titulos.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));


        canvas3.drawRoundRect(100,250,400,400,10,10,formas);
        canvas3.drawLine(100,300,400,300,formas2);
        canvas3.drawLine(200,250,200,400,formas);
        canvas3.drawLine(300,250,300,400,formas2);
        canvas3.drawText("DIA",130,290,titulos);
        canvas3.drawText("MES",230,290,titulos);
        canvas3.drawText("AÑO",330,290,titulos);

        titulos.setTextSize(35f);
        canvas3.drawRoundRect(100,450,550,600,10,10,formas2);
        canvas3.drawLine(100,500,550,500,formas2);
        canvas3.drawText("URBANIZACION",200,495,titulos);
        canvas3.drawRoundRect(650,450,1100,600,10,10,formas2);
        canvas3.drawLine(650,500,1100,500,formas2);
        canvas3.drawText("N° DE CONTRATO",750,495,titulos);
        titulos.setTextSize(30f);
        canvas3.drawText(spinner_urbanizacion.getSelectedItem().toString(),190,550,titulos);
        titulos.setTextSize(35f);


        canvas3.drawText("N° DE PROYECTO",100,660,myPaint);
        canvas3.drawText(codigo_proyecto.getText().toString(),160,710,myPaint);
        canvas3.drawRoundRect(100,670,280,740,10,10,formas);
        canvas3.drawText("U.V.",350,660,myPaint);
        canvas3.drawText(uv.getText().toString(),360,710,myPaint);
        canvas3.drawRoundRect(290,670,470,740,10,10,formas2);
        canvas3.drawText("MANZANO",530,660,myPaint);
        canvas3.drawText(mz.getText().toString(),550,710,myPaint);
        canvas3.drawRoundRect(480,670,660,740,10,10,formas);
        canvas3.drawText("LOTE",720,660,myPaint);
        canvas3.drawText(lt.getText().toString(),750,710,myPaint);
        canvas3.drawRoundRect(670,670,850,740,10,10,formas2);
        canvas3.drawText("CATEGORIA",900,660,myPaint);
        canvas3.drawText(cat.getText().toString(),940,710,myPaint);
        canvas3.drawRoundRect(860,670,1040,740,10,10,formas);

        canvas3.drawText("TIPO DE VENTA",100,800,myPaint);
        canvas3.drawText(rbSelected.getText().toString(),160,850,myPaint);
        canvas3.drawRoundRect(100,810,280,880,10,10,formas);
        canvas3.drawText("SUPERFICIE M2",310,800,myPaint);
        canvas3.drawText(mts2.getText().toString(),350,850,myPaint);
        canvas3.drawRoundRect(290,810,470,880,10,10,formas2);
        canvas3.drawText("VALOR CUOTA",510,800,myPaint);
        canvas3.drawRoundRect(480,810,660,880,10,10,formas);
        canvas3.drawText("PLAZO",720,800,myPaint);
        canvas3.drawRoundRect(670,810,850,880,10,10,formas2);
        canvas3.drawText("FECHA PRIMERA CUOTA",860,800,myPaint);
        canvas3.drawRoundRect(860,810,1040,880,10,10,formas);

        canvas3.drawText("Yo ..................................................................................................................................................................",100,920,titlePaint);
        canvas3.drawText(nombre_cliente.getText().toString()+" "+apellidoPaterno.getText().toString()+" "+apellidoMaterno.getText().toString(),230,915,myPaint);
        canvas3.drawText("con documento de identidad N° .......................... expedido en ................................ al firmar el presente documento",100,940,titlePaint);
        canvas3.drawText(ci_cliente.getText().toString(),390,939,myPaint);
        canvas3.drawText(expedido.getText().toString(),640,939,myPaint);
        canvas3.drawText("certifico estoy conforme con el lote de terreno que estoy adquiriendo, y que me encuentro completamente de",100,960,titlePaint);
        canvas3.drawText("acuerdo con los datos detallados previamente",100,980,titlePaint);

        canvas3.drawText("Por tanto, en caso de presentar algun reclamo en el futuro por los datos aqui manifestados me ajustare a las ",100,1020,titlePaint);
        canvas3.drawText("politicas de la empresa asumiendo los costos que deriven de mi decision de compra.",100,1040,titlePaint);

        canvas3.drawText("Aviso importante:",100,1080,myPaint);
        canvas3.drawText("Estimado Cliente, antes de iniciar cualquier mejora en su lote de terreno debe comunicarse con el Area de",100,1100,titlePaint);
        canvas3.drawText("Atencion al Cliente de la Empresa para proceder a la verificacion de medidas y colindancias, y posterior",100,1120,titlePaint);
        canvas3.drawText("autorizacion en la Alcaldia correspondiente. Caso contrario, de ocurrir algun problema posterior (construccion,",100,1140,titlePaint);
        canvas3.drawText("alambrado, embardado o cualquier tipo de mejora fuera de los limites del terreno)",100,1160,titlePaint);
        canvas3.drawText("SERA DE SU ENTERA RESPONSABILIDAD",100,1180,myPaint);
        canvas3.drawRoundRect(95,1060,1100,1190,10,10,formas);

        canvas3.drawText("Firma del Cliente",150,1330,myPaint);
        canvas3.drawLine(100,1300,400,1300,formas);
        canvas3.drawText("Asistente Cartera",800,1330,myPaint);
        canvas3.drawLine(700,1300,1000,1300,formas);

        canvas3.drawText("Aclaracion de firma.....................................",100,1400,myPaint);
        canvas3.drawText("Aclaracion de firma.....................................",700,1400,myPaint);

        canvas3.drawRect(100,1500,500,1710,formas2);
        canvas3.drawText("Huella Digital",230,1760,myPaint);

        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.form1_parte_inferior);
        scaledbmp = Bitmap.createScaledBitmap(bmp,pageWidth,50,false);
        canvas3.drawBitmap(scaledbmp,0,1850,myPaint);

        myPDF.finishPage(myPage3);
        /////FIN PAGINA 3/////////

        /////INICIO DE PAGINA 4 ////
//        PdfDocument.PageInfo myPageInfo4 = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
//        PdfDocument.Page myPage4 = myPDF.startPage(myPageInfo4);
//        Canvas canvas4 = myPage4.getCanvas();
//
//        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
//        scaledbmp = Bitmap.createScaledBitmap(bmp,400,200,false);
//        canvas4.drawBitmap(scaledbmp,20,20,myPaint);
//
//        titulos.setTextAlign(Paint.Align.CENTER);
//        titulos.setTextSize(45f);
//        titulos.setColor(Color.BLACK);
//        titulos.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
//
//        canvas4.drawText("RESERVA PARA COMPRA",600,200,titulos);
//        canvas4.drawText("DE LOTE DE TERRENO",600,240,titulos);
//
//        canvas4.drawText("Por el presente documento el/Señor(a) .................................................................................................",100,300,titlePaint);
//        canvas4.drawText(nombre_cliente.getText().toString()+" "+apellidoPaterno.getText().toString()+" "+apellidoMaterno.getText().toString(),500,295,myPaint);
//        canvas4.drawText("con documento de identidad N° ...................... expedido en ............................... certifica un deposito",100,330,titlePaint);
//        canvas4.drawText(ci_cliente.getText().toString(),390,325,myPaint);
//        canvas4.drawText(expedido.getText().toString(),630,325,myPaint);
//        canvas4.drawText("en dinero por concepto de RESERVA de un lote de terreno ubicado en la urbanizacion ",100,360,titlePaint);
//        canvas4.drawText(spinner_urbanizacion.getSelectedItem().toString(),860,360,myPaint);
//        canvas4.drawText("Proyecto: "+codigo_proyecto.getText().toString()+"  UV: "+uv.getText().toString()+"  Mz: "+mz.getText().toString()+"  Lote: "+lt.getText().toString()+"  Categoria: "+cat.getText().toString()+"  Mts2: "+mts2.getText().toString(),100,390,titlePaint);
//        canvas4.drawText("a la empresa Novitierra, de acuerdo y conforme con las condiciones siguientes: ",100,420,titlePaint);
//        canvas4.drawText("1. Ha efectuado el deposito de ............ DOLARES DE LOS ESTADOS UNIDOS DE AMERICA/BOLIVIANOS ",100,450,titlePaint);
//        canvas4.drawText("segun comprobante del Banco ",100,470,titlePaint);
//
//
//        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.form1_parte_inferior);
//        scaledbmp = Bitmap.createScaledBitmap(bmp,pageWidth,50,false);
//        canvas4.drawBitmap(scaledbmp,0,1780,myPaint);
//
//        myPDF.finishPage(myPage4);
        ///FIN DE PAGINA 4/////

            File file = new File(Environment.getExternalStorageDirectory(),"/Formularios Solicitante.pdf");
            try {
                myPDF.writeTo(new FileOutputStream(file));

            } catch (IOException e) {
                e.printStackTrace();
            }
            myPDF.close();

        String path = Environment.getExternalStorageDirectory()+"/Formularios Solicitante.pdf";
        File pdf = new File(path);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdf));
        share.setType("application/pdf");
        startActivity(share);

    }

}
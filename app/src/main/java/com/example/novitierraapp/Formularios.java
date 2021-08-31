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

import com.example.novitierraapp.entidades.Global;
import com.example.novitierraapp.entidades.Proyectos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;

public class Formularios extends Fragment {

    private FormulariosViewModel mViewModel;
    EditText nombre_cliente, apellidoPaterno, apellidoMaterno, ci_cliente, extension_cliente,uv,mz,lt,cat,asesor,codigo_asesor,fechaNac, apellidoCasada,nacionalidad,profesion,costoAprox;
    EditText propietarioVivienta,telefonoPropietario,pais,ciudad,barrio,avenida,calle,numero,telFijo,telMovil,telFijoOfc,telMovOfc,correoPersonal,expedido,mts2;
    EditText nombreEmpresa, rubroEmpresa, direccionEmpresa, ingresosEmpresa,primerReferencia,segundaReferencia,telfReferencia1,telfReferencia2,parentesco,relacion;
    RadioGroup radioGroup, radioGroupGenero, radioGroupVivienda, radioGroupIngresos;
    RadioButton rb_plazo, rb_contado, rbSelected, rbMasculino, rbFemenino, rbSelectedGenero,rbSelectedMonedaVivienda,rbViviendaBs,rbViviendaDolar, rbIngresosBs, rbIngresosDolar,rbSelectedIngresos;
    Spinner spinner_urbanizacion, spinnerIdentificacion,spinnerEstadoCivil,spinnerNivelEstudio, spinnerTipoVivienda, spinnerDpto, spinnerTenencia, spinnerPrefijo;
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
    ArrayList<String> listaPrefijo = new ArrayList<>();

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
        spinnerPrefijo=view.findViewById(R.id.tipoPrefijo);

        codigo_proyecto = view.findViewById(R.id.idProyecto);
        guardar = view.findViewById(R.id.btguardar);
        btFechaNac = view.findViewById(R.id.btDatePickerFechaNac);
        fechaNac.setText(fechaHoy());

        codigo_asesor.setText(Global.codigo.toString());
        asesor.setText(Global.nombreSesion+" "+Global.apellidoSesion);
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
        cargarListaPrefijo(view);
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
    public void cargarListaPrefijo(View view) {
        listaPrefijo.add("Ninguno");
        listaPrefijo.add("Vda");
        listaPrefijo.add("de");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaPrefijo);
        spinnerPrefijo.setAdapter(adapter);
    }


/////boton generador de pdf/////
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void generarPDF (View v){

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

            ////definimos pagina 1
            PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(2550,4200,1).create();
            PdfDocument.Page myPage1 = myPDF.startPage(myPageInfo1);
            Canvas canvas = myPage1.getCanvas();
            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.form1);
            scaledbmp = Bitmap.createScaledBitmap(bmp,2550,4200,false);
            canvas.drawBitmap(scaledbmp,0,0,myPaint);

            Bitmap check,scaled2;
            check = BitmapFactory.decodeResource(getResources(),R.drawable.check1);
            scaled2 = Bitmap.createScaledBitmap(check,100,100,false);

            canvas.drawText(spinner_urbanizacion.getSelectedItem().toString(),920,330,titlePaint);
            canvas.drawText(nombre_cliente.getText().toString()+" "+apellidoPaterno.getText().toString()+" "+apellidoMaterno.getText().toString(),600,490,titlePaint);
            canvas.drawText(ci_cliente.getText().toString(),680,630,titlePaint);
            canvas.drawText(extension_cliente.getText().toString(),1470,630,titlePaint);
            String plazoContado = rbSelected.getText().toString();
            if(plazoContado.contains("Plazo")){
                canvas.drawBitmap(scaled2,980,700,myPaint);
            }else {
                canvas.drawBitmap(scaled2,1430,700,myPaint);
            }
            canvas.drawText(codigo_proyecto.getText().toString(),400,910,titlePaint);
            canvas.drawText(uv.getText().toString(),880,910,titlePaint);
            canvas.drawText(mz.getText().toString(),1310,910,titlePaint);
            canvas.drawText(lt.getText().toString(),1730,910,titlePaint);
            canvas.drawText(cat.getText().toString(),2230,910,titlePaint);
            canvas.drawText(asesor.getText().toString(),250,1080,titlePaint);
            canvas.drawText(codigo_asesor.getText().toString(),1730,1080,titlePaint);

            myPDF.finishPage(myPage1);
            //// FIN PAGINA 1/////

            //////PAGINA 2
            PdfDocument.PageInfo myPageInfo2 = new PdfDocument.PageInfo.Builder(2550,4200,1).create();
            PdfDocument.Page myPage2 = myPDF.startPage(myPageInfo2);
            Canvas canvas2 = myPage2.getCanvas();

            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.form4);
            scaledbmp = Bitmap.createScaledBitmap(bmp,2550,4200,false);
            canvas2.drawBitmap(scaledbmp,0,0,myPaint);

            canvas2.drawText(apellidoPaterno.getText().toString(),450,580,titlePaint);
            canvas2.drawText(apellidoMaterno.getText().toString(),1550,580,titlePaint);
            canvas2.drawText(nombre_cliente.getText().toString(),450,750,titlePaint);
            canvas2.drawText(apellidoCasada.getText().toString(),1450,750,titlePaint);
            canvas2.drawText(spinnerPrefijo.getSelectedItem().toString(),2060,750,titlePaint);
            canvas2.drawText(ci_cliente.getText().toString(),1510,920,titlePaint);
            canvas2.drawText(extension_cliente.getText().toString(),2060,920,titlePaint);

            canvas2.drawText(spinnerIdentificacion.getSelectedItem().toString(),650,835,titlePaint);
            canvas2.drawText(nacionalidad.getText().toString(),650,935,titlePaint);
            canvas2.drawText(fechaNac.getText().toString(),650,990,titlePaint);
            canvas2.drawText(spinnerEstadoCivil.getSelectedItem().toString(),650,1040,titlePaint);
            canvas2.drawText(rbSelectedGenero.getText().toString(),650,1090,titlePaint);
            canvas2.drawText(spinnerNivelEstudio.getSelectedItem().toString(),650,1140,titlePaint);
            canvas2.drawText(profesion.getText().toString(),650,1195,titlePaint);
            canvas2.drawText(spinnerTipoVivienda.getSelectedItem().toString(),1120,1255,titlePaint);
            canvas2.drawText(spinnerTenencia.getSelectedItem().toString(),1740,1255,titlePaint);

            canvas2.drawText(costoAprox.getText().toString()+rbSelectedMonedaVivienda.getText().toString(),250,1410,titlePaint);
            canvas2.drawText(propietarioVivienta.getText().toString(),760,1410,titlePaint);
            canvas2.drawText(telefonoPropietario.getText().toString(),1950,1410,titlePaint);

            canvas2.drawText(pais.getText().toString(),270,1540,titlePaint);
            canvas2.drawText(spinnerDpto.getSelectedItem().toString(),820,1540,titlePaint);
            canvas2.drawText(ciudad.getText().toString(),300,1660,titlePaint);
            canvas2.drawText(barrio.getText().toString(),1300,1660,titlePaint);
            canvas2.drawText(avenida.getText().toString(),300,1810,titlePaint);
            canvas2.drawText(calle.getText().toString(),1300,1810,titlePaint);
            canvas2.drawText(numero.getText().toString(),1940,1810,titlePaint);

            canvas2.drawText(telFijo.getText().toString(),250,2040,titlePaint);
            canvas2.drawText(telFijoOfc.getText().toString(),250,2210,titlePaint);
            canvas2.drawText(telMovil.getText().toString(),700,2040,titlePaint);
            canvas2.drawText(telMovOfc.getText().toString(),700,2210,titlePaint);
            canvas2.drawText(correoPersonal.getText().toString(),250,2370,titlePaint);

            canvas2.drawText(nombreEmpresa.getText().toString(),1300,2040,titlePaint);
            canvas2.drawText(direccionEmpresa.getText().toString(),1300,2210,titlePaint);
            canvas2.drawText(rubroEmpresa.getText().toString(),1300,2370,titlePaint);
            canvas2.drawText(ingresosEmpresa.getText().toString()+rbSelectedIngresos.getText().toString(),2060,2370,titlePaint);

            canvas2.drawText(primerReferencia.getText().toString(),250,2590,titlePaint);
            canvas2.drawText(segundaReferencia.getText().toString(),250,2740,titlePaint);
            canvas2.drawText(parentesco.getText().toString(),1510,2590,titlePaint);
            canvas2.drawText(relacion.getText().toString(),1510,2740,titlePaint);
            canvas2.drawText(telfReferencia1.getText().toString(),2060,2590,titlePaint);
            canvas2.drawText(telfReferencia2.getText().toString(),2060,2740,titlePaint);
            titlePaint.setTextSize(40f);
            canvas2.drawText(nombre_cliente.getText().toString()
                    +apellidoPaterno.getText().toString()
                    +apellidoMaterno.getText().toString()
                    ,530,3950,titlePaint);
            canvas2.drawText(asesor.getText().toString(),1870,3950,titlePaint);
            titlePaint.setTextSize(50f);


            myPDF.finishPage(myPage2);
              //////FIN PAGINA 2 /////
//
//
//        ////// INICIA PAGINA 3 ///////
            PdfDocument.PageInfo myPageInfo3 = new PdfDocument.PageInfo.Builder(2550,4200,1).create();
            PdfDocument.Page myPage3 = myPDF.startPage(myPageInfo3);
            Canvas canvas3 = myPage3.getCanvas();

            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.formentregalote);
            scaledbmp = Bitmap.createScaledBitmap(bmp,2550,4200,false);
            canvas3.drawBitmap(scaledbmp,0,0,myPaint);

            myPaint.setTextAlign(Paint.Align.CENTER);
            myPaint.setTextSize(70f);
            myPaint.setColor(Color.BLACK);
            myPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
            canvas3.drawText(spinner_urbanizacion.getSelectedItem().toString(),700,990,myPaint);
            titlePaint.setTextSize(60f);
            canvas3.drawText(codigo_proyecto.getText().toString(),270,1280,titlePaint);
            canvas3.drawText(uv.getText().toString(),740,1280,titlePaint);
            canvas3.drawText(mz.getText().toString(),1200,1280,titlePaint);
            canvas3.drawText(lt.getText().toString(),1680,1280,titlePaint);
            canvas3.drawText(cat.getText().toString(),2180,1280,titlePaint);

            canvas3.drawText(rbSelected.getText().toString(),270,1680,titlePaint);
            canvas3.drawText(mts2.getText().toString(),740,1680,titlePaint);
            titlePaint.setTextSize(45f);
            canvas3.drawText(nombre_cliente.getText().toString()+apellidoPaterno.getText().toString()+
                    apellidoMaterno.getText().toString(),270,1895,titlePaint);
            canvas3.drawText(ci_cliente.getText().toString(),840,1952,titlePaint);
            canvas3.drawText(expedido.getText().toString(),1380,1952,titlePaint);

            myPDF.finishPage(myPage3);
        /////FIN PAGINA 3/////////

        /////INICIO DE PAGINA 4 ////

//
//        myPDF.finishPage(myPage4);
        ///FIN DE PAGINA 4/////

            File file = new File(Environment.getExternalStorageDirectory(),"/FormularioSolicitante.pdf");
            try {
                myPDF.writeTo(new FileOutputStream(file));

            } catch (IOException e) {
                e.printStackTrace();
            }
            myPDF.close();

        String path = Environment.getExternalStorageDirectory()+"/FormularioSolicitante.pdf";
        File pdf = new File(path);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdf));
        share.setType("application/pdf");
        startActivity(share);

    }

}
package com.example.novitierraapp;

import static java.security.AccessController.getContext;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.Calendar;

public class formulario1y3 extends Fragment {

    EditText nombre,apellidop,apellidom,apellidoc,documento,uv,mz,lt,cat,mt2,asesor,codigoasesor;
    TextView codigo_proyecto,tvPlazo;
    Spinner spinnerIdentificacion, spinnerPrefijo,spinnerExtension,spinnerPlazo,spinner_urbanizacion;
    RadioGroup radioGroup;
    RadioButton rb_plazo, rb_contado, rbSeleccionPlazoContado;
    Button btform13;
    ArrayList<String>listaIdentificacion = new ArrayList<>();
    ArrayList<String> listaExtension = new ArrayList<>();
    ArrayList<String> listaPrefijo = new ArrayList<>();
    ArrayList<String> listaPlazo = new ArrayList<>();
    ArrayList<Proyectos> listaProyectos = new ArrayList<>();
    Bitmap imagen,scaled;
    private String path = Environment.getExternalStorageDirectory().getPath() + "/Download/Formularios1y3.pdf";
    private File file = new File(path);


    private Formulario1y3ViewModel mViewModel;

    public static formulario1y3 newInstance() {
        return new formulario1y3();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.formulario1y3_fragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        tvPlazo = view.findViewById(R.id.tvplazo13);
        codigo_proyecto=view.findViewById(R.id.idProyectof13);
        nombre = view.findViewById(R.id.nombreClientef13);
        apellidop = view.findViewById(R.id.apellidoPClientef13);
        apellidom =view.findViewById(R.id.apellidoMClientef13);
        apellidoc = view.findViewById(R.id.apellidoCasadaf13);
        documento=view.findViewById(R.id.ciClientef13);
        uv=view.findViewById(R.id.uvf13);
        mz=view.findViewById(R.id.mzf13);
        lt=view.findViewById(R.id.ltf13);
        cat=view.findViewById(R.id.catf13);
        mt2=view.findViewById(R.id.mts2f13);
        asesor=view.findViewById(R.id.fullnameAsesorf13);
        codigoasesor=view.findViewById(R.id.codigoAsesorf13);

        spinnerIdentificacion=view.findViewById(R.id.tipoIdentificacionf13);
        spinnerPrefijo=view.findViewById(R.id.tipoPrefijof13);
        spinnerExtension=view.findViewById(R.id.spinnerExtensionf13);
        spinnerPlazo=view.findViewById(R.id.cuotasplazof13);
        spinner_urbanizacion=view.findViewById(R.id.urbanizacionf13);

        radioGroup=view.findViewById(R.id.radiogroupf13);
        rb_plazo=view.findViewById(R.id.aPlazof13);
        rb_contado=view.findViewById(R.id.aContadof13);

        btform13 = view.findViewById(R.id.btguardarf13);

        cargarListaTipoIdentificacion(spinnerIdentificacion);
        cargarListaPrefijo(spinnerPrefijo);
        cargarListaExtensiones(spinnerExtension);
        cargarListaPlazo(spinnerPlazo);
        cargarListaUrbanizacion(spinner_urbanizacion);

        codigoasesor.setText(Global.codigo.toString());
        asesor.setText(Global.nombreSesion+" "+Global.apellidoSesion);

        spinner_urbanizacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codigo_proyecto.setText(listaProyectos.get(position).getCodigo().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rb_plazo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPlazo.setVisibility(View.VISIBLE);
                spinnerPlazo.setVisibility(View.VISIBLE);
                SeleccionPlazoContado(v);
            }
        });
        rb_contado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPlazo.setSelection(0);
                tvPlazo.setVisibility(View.GONE);
                spinnerPlazo.setVisibility(View.GONE);
                SeleccionPlazoContado(v);
            }
        });
        btform13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(condiciones()){
                    btform13.setEnabled(false);
                    new Handler().postDelayed(new Runnable(){
                        public void run(){
                            generarFormularios13();
                        }
                    }, 1000);

                }

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(Formulario1y3ViewModel.class);
        // TODO: Use the ViewModel
    }
    public void cargarListaPrefijo(Spinner spinner) {
        listaPrefijo.add("Ninguno");
        listaPrefijo.add("Vda de");
        listaPrefijo.add("de");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaPrefijo);
        spinner.setAdapter(adapter);
    }
    public void cargarListaTipoIdentificacion(Spinner spinner){
        listaIdentificacion.add("Carnet de Identidad");
        listaIdentificacion.add("Pasaporte");
        listaIdentificacion.add("Carnet Extranjero");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaIdentificacion);
        spinner.setAdapter(adapter);
    }
    public void cargarListaPlazo(Spinner spinner){
        listaPlazo.add("--");
        listaPlazo.add("12");
        listaPlazo.add("24");
        listaPlazo.add("36");
        listaPlazo.add("48");
        listaPlazo.add("60");
        listaPlazo.add("72");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaPlazo);
        spinner.setAdapter(adapter);
    }
    public void cargarListaExtensiones(Spinner spinner){
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
        spinner.setAdapter(adapter);
    }
    public void cargarListaUrbanizacion(Spinner spinner){
        listaProyectos.add(new Proyectos(100,"LA ENCONADA II"));
        listaProyectos.add(new Proyectos(101,"LA PASCANA DE COTOCA"));
        listaProyectos.add(new Proyectos(200,"LA PASCANA DE COTOCA II"));
        listaProyectos.add(new Proyectos(201,"LA TIERRA PROMETIDA"));
        listaProyectos.add(new Proyectos(204,"AME TAUNA"));
        listaProyectos.add(new Proyectos(205,"AME TAUNA I"));
        listaProyectos.add(new Proyectos(206,"MACORORO I"));
        listaProyectos.add(new Proyectos(207,"MACORORO II"));
        listaProyectos.add(new Proyectos(208,"MACORORO III"));
        ArrayAdapter<Proyectos> adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaProyectos);
        spinner_urbanizacion.setAdapter(adapter);
    }

    public void SeleccionPlazoContado(View v){
        int radiobtid = radioGroup.getCheckedRadioButtonId();
        rbSeleccionPlazoContado = v.findViewById(radiobtid);
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

    public Boolean condiciones(){
        if(nombre.getText().toString().length()==0){
            mensaje("Ingrese un nombre de cliente");
            return false;
        }else{
            if(documento.getText().toString().length()==0){
                mensaje("Ingrese un numero de carnet o numero de documento");
                return false;
            }else{
                    if(uv.getText().toString().length()==0 || mz.getText().toString().length()==0|| lt.getText().toString().length()==0 || cat.getText().toString().length()==0){
                              mensaje("Colocar todo los datos UV MZ LT CAT");
                              return false;
                            }else{
                                if(mt2.getText().toString().length()==0){
                                    mensaje("Colocar metros2");
                                    return false;
                                }else{
                                    if(asesor.getText().toString().length()==0 || codigoasesor.getText().toString().length()==0){
                                        mensaje("Colocar datos del asesor y su codigo");
                                    }else{
                                        if(rb_contado.isChecked() || rb_plazo.isChecked()){
                                            ///camino de plazo
                                            if(rb_plazo.isChecked()){
                                                if(spinnerPlazo.getSelectedItem().toString().contains("--")){
                                                    mensaje("Seleccionar cuotas a plazo");
                                                    return false;
                                                }else {
                                                    return true;
                                                }
                                            }else{
                                                ///camino del contado
                                                return true;
                                            }
                                        }else{
                                            mensaje("Seleccionar Plazo o Contado");
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
        return false;
    }
    public void mensaje(String string){
        Toast.makeText(getContext(),string,Toast.LENGTH_LONG).show();
    }

    public void generarFormularios13(){
        String prefijoObtenido;
        String plazoContado = rbSeleccionPlazoContado.getText().toString();
        Calendar cal = Calendar.getInstance();
        Integer year = cal.get(Calendar.YEAR);
        Integer month = cal.get(Calendar.MONTH);
        month=month+1;
        Integer day= cal.get(Calendar.DAY_OF_MONTH);

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
        imagen = BitmapFactory.decodeResource(getResources(),R.drawable.f1png);
        scaled = Bitmap.createScaledBitmap(imagen,2539,3874,false);
//            bmp = BitmapFactory.decodeResource(getResources(),R.drawable.form1);
//            scaledbmp = Bitmap.createScaledBitmap(bmp,2539,3874,false);
        canvas.drawBitmap(scaled,0,0,myPaint);

        Bitmap check,scaledcheck;
        check = BitmapFactory.decodeResource(getResources(),R.drawable.check1);
        scaledcheck = Bitmap.createScaledBitmap(check,100,100,false);

        canvas.drawText(spinner_urbanizacion.getSelectedItem().toString(),920,305,titlePaint);
        canvas.drawText(nombre.getText().toString().toUpperCase()+" "+apellidop.getText().toString().toUpperCase()+" "+apellidom.getText().toString().toUpperCase()+" "+prefijoObtenido.toUpperCase()+" "+apellidoc.getText().toString().toUpperCase(),600,460,titlePaint);
        canvas.drawText(documento.getText().toString(),680,585,titlePaint);
        canvas.drawText(spinnerExtension.getSelectedItem().toString(),1480,585,titlePaint);

        if(plazoContado.contains("A plazo")){
            canvas.drawBitmap(scaledcheck,980,640,myPaint);
        }else {
            canvas.drawBitmap(scaledcheck,1430,640,myPaint);
        }
        canvas.drawText(codigo_proyecto.getText().toString(),400,835,titlePaint);
        canvas.drawText(tresDigitos(uv.getText().toString()),880,835,titlePaint);
        canvas.drawText(tresDigitos(mz.getText().toString()),1310,835,titlePaint);
        canvas.drawText(tresDigitos(lt.getText().toString()),1730,835,titlePaint);
        canvas.drawText(cat.getText().toString(),2230,835,titlePaint);
        canvas.drawText(asesor.getText().toString().toUpperCase(),250,995,titlePaint);
        canvas.drawText(codigoasesor.getText().toString(),1730,995,titlePaint);

        myPDF.finishPage(myPage1);
        //// FIN PAGINA 1/////

        ////// INICIA PAGINA 3 ///////
        PdfDocument.PageInfo myPageInfo3 = new PdfDocument.PageInfo.Builder(2539,3874,1).create();
        PdfDocument.Page myPage3 = myPDF.startPage(myPageInfo3);
        Canvas canvas3 = myPage3.getCanvas();
        imagen = BitmapFactory.decodeResource(getResources(),R.drawable.f3png);
        scaled = Bitmap.createScaledBitmap(imagen,2539,3874,false);
//        Bitmap imagen3,scaled3 ;
//        imagen3 = BitmapFactory.decodeResource(getResources(),R.drawable.form3legal02);
//        scaled3 = Bitmap.createScaledBitmap(imagen3,2539,3874,false);

//          bmp = BitmapFactory.decodeResource(getResources(),R.drawable.newentrega);

        canvas3.drawBitmap(scaled,0,0,myPaint);
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
        canvas3.drawText(tresDigitos(uv.getText().toString().toUpperCase()),740,1200,titlePaint);
        canvas3.drawText(tresDigitos(mz.getText().toString().toUpperCase()),1200,1200,titlePaint);
        canvas3.drawText(tresDigitos(lt.getText().toString()),1680,1200,titlePaint);
        canvas3.drawText(cat.getText().toString().toUpperCase(),2180,1200,titlePaint);
        canvas3.drawText(rbSeleccionPlazoContado.getText().toString(),270,1555,titlePaint);
        canvas3.drawText(mt2.getText().toString(),740,1555,titlePaint);
        if(plazoContado.contains("Contado")){
            canvas3.drawText("",1680,1555,titlePaint);
        }else {
            canvas3.drawText(spinnerPlazo.getSelectedItem().toString(),1680,1555,titlePaint);
        }
        titlePaint.setTextSize(45f);
        canvas3.drawText(nombre.getText().toString().toUpperCase()+" "+apellidop.getText().toString().toUpperCase()+" "+
                apellidom.getText().toString().toUpperCase()+" "+prefijoObtenido.toUpperCase()+" "+apellidoc.getText().toString().toUpperCase(),242,1750,titlePaint);
        canvas3.drawText(documento.getText().toString(),1980,1750,titlePaint);
        canvas3.drawText(spinnerExtension.getSelectedItem().toString(),270,1808,titlePaint);

        canvas3.drawText(nombre.getText().toString().toUpperCase()+" "
                +apellidop.getText().toString().toUpperCase()+" "
                +apellidom.getText().toString().toUpperCase()+" "+prefijoObtenido.toUpperCase()+" "+apellidoc.getText().toString().toUpperCase(),425,2960,titlePaint);
//            canvas3.drawText(asesor.getText().toString(),1910,2960,titlePaint);

        myPDF.finishPage(myPage3);
        /////FIN PAGINA 3/////////
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
        btform13.setEnabled(true);
    }
}
package com.example.novitierraapp;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Formularios extends Fragment {

    private FormulariosViewModel mViewModel;
    EditText nombre_cliente, apellido_cliente, ci_cliente, extension_cliente,uv,mz,lt,cat,asesor,codigo_asesor;
    RadioGroup radioGroup;
    RadioButton rb_plazo, rb_contado, rbSelected;
    Spinner spinner_urbanizacion;
    TextView codigo_proyecto;
    Button guardar;
    Proyectos proyectos;
    ArrayList<Proyectos> listaProyectos = new ArrayList<>();
    Bitmap bmp, scaledbmp;

    int pageWidth=1200;

    public static Formularios newInstance() {
        return new Formularios();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nombre_cliente = view.findViewById(R.id.nombreCliente);
        apellido_cliente= view.findViewById(R.id.apellidoCliente);
        ci_cliente = view.findViewById(R.id.ciCliente);
        extension_cliente = view.findViewById(R.id.extensionCliente);
        uv= view.findViewById(R.id.uv);
        mz=view.findViewById(R.id.mz);
        lt= view.findViewById(R.id.lt);
        cat=view.findViewById(R.id.lt);
        asesor=view.findViewById(R.id.fullnameAsesor);
        codigo_asesor= view.findViewById(R.id.codigoAsesor);
        radioGroup = view.findViewById(R.id.radiogroup);
        rb_plazo = view.findViewById(R.id.aPlazo);
        rb_contado= view.findViewById(R.id.aContado);
        spinner_urbanizacion= view.findViewById(R.id.urbanizacion);
        codigo_proyecto = view.findViewById(R.id.idProyecto);
        guardar = view.findViewById(R.id.btguardar);
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        scaledbmp = Bitmap.createScaledBitmap(bmp,400,200,false);

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
        cargarListaUrbanizacion(view);
        spinner_urbanizacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codigo_proyecto.setText(listaProyectos.get(position).getCodigo().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ActivityCompat.requestPermissions(getActivity(),new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPDF(v);
                Toast.makeText(getContext(),"PDF Generado",Toast.LENGTH_SHORT).show();
            }
        });

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
        Toast.makeText(getContext(),"seleccionaste: "+ rbSelected.getText(),Toast.LENGTH_SHORT).show();
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

    public void generarPDF (View v){
        PdfDocument myPDF = new PdfDocument();
        Paint myPaint = new Paint();
        Paint titlePaint = new Paint();
        PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
        PdfDocument.Page myPage1 = myPDF.startPage(myPageInfo1);
        Canvas canvas = myPage1.getCanvas();

        //logo e imagen final
        canvas.drawBitmap(scaledbmp,20,20,myPaint);
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.form1_parte_inferior);
        scaledbmp = Bitmap.createScaledBitmap(bmp,pageWidth,47,false);
        canvas.drawBitmap(scaledbmp,0,490,myPaint);

        ////PAGINA 1 //////
        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(30f);
        myPaint.setColor(Color.BLACK);
        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));

        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTextSize(30f);
        titlePaint.setColor(Color.BLACK);

        canvas.drawText("Urbanizacion: ",350,190,myPaint);
        canvas.drawText(spinner_urbanizacion.getSelectedItem().toString(),570,190,titlePaint);
        canvas.drawText("Nombre del Cliente: ",30,250,myPaint);
        canvas.drawText(nombre_cliente.getText().toString()+" "+apellido_cliente.getText().toString(),340,250,titlePaint);
        canvas.drawText("Documento de Identidad: ",30,290,myPaint);
        canvas.drawText(ci_cliente.getText().toString(),380,290,titlePaint);
        canvas.drawText("Extension: ",550,290,myPaint);
        canvas.drawText(extension_cliente.getText().toString(),710,290,titlePaint);
        canvas.drawText("N° de Reserva: ",780,290,myPaint);
        canvas.drawText("Codigo del Cliente: ",30,330,myPaint);
        canvas.drawText("Pago a: ",410,330,myPaint);
        canvas.drawText(rbSelected.getText().toString(),520,330,titlePaint);
        canvas.drawText("N° de Contrato: ",780,330,myPaint);
        canvas.drawText("Proyecto: ",30,370,myPaint);
        canvas.drawText(codigo_proyecto.getText().toString(),170,370,titlePaint);
        canvas.drawText("UV: ",240,370,myPaint);
        canvas.drawText(uv.getText().toString(),310,370,titlePaint);
        canvas.drawText("Mz: ",360,370,myPaint);
        canvas.drawText(mz.getText().toString(),420,370,titlePaint);
        canvas.drawText("Lote: ",480,370,myPaint);
        canvas.drawText(lt.getText().toString(),560,370,titlePaint);
        canvas.drawText("Categoria: ",620,370,myPaint);
        canvas.drawText(cat.getText().toString(),780,370,titlePaint);
        canvas.drawText("Nombre y Apellido del Asesor de Inversion: ",30,410,myPaint);
        canvas.drawText(asesor.getText().toString(),620,410,titlePaint);
        canvas.drawText("Codigo Asesor: ",30,450,myPaint);
        canvas.drawText(codigo_asesor.getText().toString(),250,450,titlePaint);
        //// FIN PAGINA 1/////





        myPDF.finishPage(myPage1);
        File file = new File(Environment.getExternalStorageDirectory(),"/Formulario1.pdf");
        try {
            myPDF.writeTo(new FileOutputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
        }
        myPDF.close();
    }

}
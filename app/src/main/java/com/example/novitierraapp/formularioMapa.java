package com.example.novitierraapp;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
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

public class formularioMapa extends Fragment {

    private FormularioMapaViewModel mViewModel;

    EditText nombre_cliente, apellidoPaterno, apellidoMaterno,apellidoCasada, ci_cliente,uv,mz,lt,cat;
    EditText ciudad,barrio,avenida,calle,numero,telMovil,primerReferencia,segundaReferencia,telfReferencia1,telfReferencia2,parentesco,relacion,zona;
    EditText observacion1, observacion2;
    Spinner spinnerPrefijo, spinnerExtension;
    Button guardar;

    ArrayList<String> listaExtension = new ArrayList<>();
    ArrayList<String> listaPrefijo = new ArrayList<>();

    Bitmap imagen,scaled;
    //***PARA PDF****
    private String path = Environment.getExternalStorageDirectory().getPath() + "/Download/FormularioMapa.pdf";
    private File file = new File(path);



    public static formularioMapa newInstance() {
        return new formularioMapa();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.formulario_mapa_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FormularioMapaViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        nombre_cliente = view.findViewById(R.id.nombreCliente);
        //nombre_cliente.setRawInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        apellidoPaterno= view.findViewById(R.id.apellidoPCliente);
        apellidoMaterno= view.findViewById(R.id.apellidoMCliente);
        apellidoCasada = view.findViewById(R.id.apellidoCasada);
        ci_cliente = view.findViewById(R.id.ciCliente);

        ciudad=view.findViewById(R.id.ciudadVivienda);
        zona=view.findViewById(R.id.zonaVivienda);
        barrio=view.findViewById(R.id.barrioVivienda);
        avenida=view.findViewById(R.id.avenidadVivienda);
        calle=view.findViewById(R.id.calleVivienda);
        numero=view.findViewById(R.id.numeroVivienda);
        telMovil=view.findViewById(R.id.telfMovilCliente);
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
        observacion1 = view.findViewById(R.id.observacion1);
        observacion2 = view.findViewById(R.id.observacion2);

        spinnerPrefijo=view.findViewById(R.id.tipoPrefijo);
        spinnerExtension=view.findViewById(R.id.spinnerExtension);

        guardar = view.findViewById(R.id.btguardar);

        /////cargamos los spinners

        cargarListaPrefijo();
        cargarListaExtensiones();

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeshabilitarBoton();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        generarPDF();
                    }
                }, 1000); //1000 millisegundos = 1 segundo.
            }
        });
    }

    public void mensaje(String mensaje){
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_LONG).show();
    }

//    public Boolean requisitos(){
//        if(nombre_cliente.getText().toString().length()==0){
//            mensaje("Colocar nombre");
//        }else {
//            if(ci_cliente.getText().toString().length()==0){
//                mensaje("Colocar numero documento de identidad");
//            }else {
//                if(barrio.getText().toString().length()==0){
//                    mensaje("Colocar Barrio");
//                }
//            }
//        }
//
//        return false;
//    }

    public void DeshabilitarBoton(){
        guardar.setEnabled(false);
    }
    public void habilitarBoton(){
        guardar.setEnabled(true);
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

        ///INICIO DE PAGINA 4 ////
        PdfDocument.PageInfo myPageInfo4 = new PdfDocument.PageInfo.Builder(2539,3874,1).create();
        PdfDocument.Page myPage4 = myPDF.startPage(myPageInfo4);
        Canvas canvas4 = myPage4.getCanvas();
        imagen = BitmapFactory.decodeResource(getResources(),R.drawable.fm);
//        imagen = BitmapFactory.decodeResource(getResources(),R.drawable.nuevoformmapa);
        scaled = Bitmap.createScaledBitmap(imagen,2539,3874,false);
        if(Global.ubicacion==null){
            Global.ubicacion=BitmapFactory.decodeResource(getResources(),R.drawable.nomap);
        }
//            Bitmap imagen4,scaled4;
//            imagen4 = BitmapFactory.decodeResource(getResources(),R.drawable.nuevoformmapa);
//            scaled4 = Bitmap.createScaledBitmap(imagen4,2539,3874,false);
        canvas4.drawBitmap(scaled,0,0,myPaint);
        canvas4.drawText(tresDigitos(uv.getText().toString()),600,460,titlePaint);
        canvas4.drawText(tresDigitos(mz.getText().toString()),1190,460,titlePaint);
        canvas4.drawText(tresDigitos(lt.getText().toString()),1780,460,titlePaint);
        canvas4.drawText(cat.getText().toString(),2250,460,titlePaint);
        canvas4.drawText(nombre_cliente.getText().toString().toUpperCase()+" "+apellidoPaterno.getText().toString().toUpperCase()+" "+apellidoMaterno.getText().toString().toUpperCase()+" "+prefijoObtenido.toUpperCase()+" "+apellidoCasada.getText().toString().toUpperCase(),800,575,titlePaint);
        canvas4.drawText(ci_cliente.getText().toString(),800,637,titlePaint);
        canvas4.drawText(telMovil.getText().toString(),1700,637,titlePaint);
        canvas4.drawText("Barrio:"+" "+barrio.getText().toString()+" Avenida: "+avenida.getText().toString()+" Calle: "+calle.getText().toString()+" Numero: "+numero.getText().toString(),800,705,titlePaint);
        canvas4.drawText(primerReferencia.getText().toString()+" "+telfReferencia1.getText().toString()+" - "+segundaReferencia.getText().toString()+" "+telfReferencia2.getText().toString(),800,775,titlePaint);
        canvas4.drawText(zona.getText().toString(),800,850,titlePaint);
        canvas4.drawText(observacion1.getText().toString(),350,3475,titlePaint);
        canvas4.drawText(observacion2.getText().toString(),350,3525,titlePaint);

        scaled = Bitmap.createScaledBitmap(Global.ubicacion,2280,2500,false);
        canvas4.drawBitmap(scaled,150,900,myPaint);
        myPDF.finishPage(myPage4);
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
        habilitarBoton();
    }


}
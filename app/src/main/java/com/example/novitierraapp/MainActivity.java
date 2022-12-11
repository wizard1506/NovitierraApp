package com.example.novitierraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.novitierraapp.entidades.usuarios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView tvRegistro;
    EditText userLogin,passwordLogin;
    Button login;
    ImageButton btnreferidor;
    List<usuarios> userList;
    RequestQueue requestQueue;
    String usu,usu2;
    String pass,pass2;
    private static final String URL = "http://wizardapps.xyz/novitierra/validar_usuario.php" ;
    private static final String URL2 = "http://wizardapps.xyz/novitierra/userLogged.php" ;
//    private static final String URL = "https://novitierra.000webhostapp.com/validar_usuario.php" ;
//    private static final String URL2 = "https://novitierra.000webhostapp.com/userLogged.php" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userList = new ArrayList<>();
        setContentView(R.layout.activity_main);
        userLogin=findViewById(R.id.userLogin);
        passwordLogin=findViewById(R.id.passwordLogin);
        login = findViewById(R.id.btlogin);
        btnreferidor = findViewById(R.id.btnSoyReferidor);
        tvRegistro=findViewById(R.id.tvRegistrarme);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (campoVacio()!=true){
                    traerUsuario();
                }else {
                    Toast.makeText(MainActivity.this, "Ingrese sus datos correctamente.", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegistrarseLogin.class);
                startActivity(intent);

            }
        });

        btnreferidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),loginReferidor.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Utils.isPermissionGranted(this)){
            new AlertDialog.Builder(this).setTitle("Permiso de aplicacion").setMessage("Debido a la version de android es necesario otorgar permisos").setPositiveButton("Permitir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    otorgarPermisos();
                }
            }).setNegativeButton("Denegar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setIcon(R.drawable.casita_sola).show();
        }
        else{
            mensaje("Permisos de aplicacion ya otorgados");
        }
    }

    public void mensaje(String mensaje){
        Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show();
    }

    private void otorgarPermisos(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                Uri uri = Uri.fromParts("package",getPackageName(),null);
                intent.setData(uri);
                startActivityForResult(intent,101);

            }catch (Exception e){
                e.printStackTrace();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent,101);

            }
        }else  {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
            },101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0){

            if(requestCode==101){

                boolean readExt  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(!readExt){
                    otorgarPermisos();
                }
            }



        }
    }

    public boolean campoVacio(){
        if (userLogin.getText().toString().isEmpty()|| passwordLogin.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }

    private void validarUsuario(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(),navMenu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Bienvenido(a)"+" "+Global.nombreSesion.toString(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Usuario o Contraseña incorrecta", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"No se pudo conectar al servidor", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("usuario",userLogin.getText().toString());
                parametros.put("password",passwordLogin.getText().toString());

                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void traerUsuario(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject respuesta = array.getJSONObject(i);
//                            Global.id_user=respuesta.getInt("id_usuario");
                            Global.useridSesion=respuesta.getString("id_usuario");
                            Global.nombreSesion=respuesta.getString("nombres");
                            Global.apellidoSesion=respuesta.getString("apellidos");
                            Global.ciSesion = respuesta.getString("ci");
                            Global.telefonoSesion = respuesta.getString("telefono");
                            Global.codigo=respuesta.getInt("codigo");
                            Global.grupo = respuesta.getString("grupo");
                            Global.userSesion=respuesta.getString("usuario");
                            Global.upass=respuesta.getString("upassword");
                            usu=respuesta.getString("usuario");
                            pass=respuesta.getString("upassword");
                            usu2=userLogin.getText().toString();
                            pass2=passwordLogin.getText().toString();
                            if(usu2.equals(usu)&&pass2.equals(pass)){
                                validarUsuario(URL);
                            }else {
                                Toast.makeText(MainActivity.this, "Usuario o Contraseña incorrecta", Toast.LENGTH_LONG).show();
                            }
                        }
                    }catch(JSONException e) {
                        Toast.makeText(MainActivity.this, "Usuario no registrado", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Ocurrio algun error", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("usuario",userLogin.getText().toString());
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    }


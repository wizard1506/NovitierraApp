package com.example.novitierraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView tvRegistro;
    EditText passwordLogin;
    ImageButton login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        passwordLogin=findViewById(R.id.passwordLogin);
        login = findViewById(R.id.btlogin);
        tvRegistro=findViewById(R.id.tvRegistrarme);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = passwordLogin.getText().toString();
                if(codigo.contains("novitierra")){
                    Intent intent = new Intent(getApplicationContext(),navMenu.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"Ingrese su contraseña.",Toast.LENGTH_LONG).show();
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
    }
}
package com.example.josemi.aplicacingsaudios;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class EstadoActivity extends AppCompatActivity {

    private String paciente;
    private TextView texto;
    private CheckBox es1,es2,es3,es4,es5,si,no;
    private Button selec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //Lock de la pantalla en vertical

        paciente = getIntent().getExtras().getString("paciente");

        texto = findViewById(R.id.texto);
        texto.setText("Seleccione el/los estados para el paciente: " + paciente);

        es1 = findViewById(R.id.es1);
        es2 = findViewById(R.id.es2);
        es3 = findViewById(R.id.es3);
        si = findViewById(R.id.si);
        no = findViewById(R.id.no);

        selec = findViewById(R.id.seleccionar);

        selec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pulsado el Botón de Seleccionar",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}

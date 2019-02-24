package com.example.josemi.aplicacingsaudios;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OpcionesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String paciente;
    private TextView texto;
    private Button selec;
    private CheckBox opc1,opc2,opc3;
    private Spinner opc4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //Lock de la pantalla en vertical

        texto = findViewById(R.id.texto);
        texto.setText("Seleccione los apartados y opciones para el paciente: " + paciente);

        selec = findViewById(R.id.seleccionar);
        opc1 = findViewById(R.id.opc1);
        opc2 = findViewById(R.id.opc2);
        opc3 = findViewById(R.id.opc3);
        opc4 = findViewById(R.id.opc4);

        opc4.setSelection(-1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.o4,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opc4.setAdapter(adapter);
        opc4.setOnItemSelectedListener(this);

        selec.setEnabled(false);

        selec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pulsado el Botón de Seleccionar",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String p = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),"La opción seleccionada es: " + p,Toast.LENGTH_LONG).show();
        selec.setEnabled(true);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

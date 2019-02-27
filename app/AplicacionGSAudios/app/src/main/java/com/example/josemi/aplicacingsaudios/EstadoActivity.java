package com.example.josemi.aplicacingsaudios;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class EstadoActivity extends AppCompatActivity {

    private String paciente,ruta,nf;
    private TextView texto;
    private CheckBox es1,es2,es3,es4,es5,si,no;
    private Button selec;
    private List<CheckBox> checksEs,checksSN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //Lock de la pantalla en vertical

        paciente = getIntent().getExtras().getString("paciente");
        ruta = getIntent().getExtras().getString("ruta");
        nf = getIntent().getExtras().getString("nombre");

        texto = findViewById(R.id.texto);
        texto.setText("Seleccione el/los estados para el paciente: " + paciente);

        checksEs = new LinkedList();
        checksSN = new LinkedList();

        es1 = findViewById(R.id.es1);
        checksEs.add(es1);
        es2 = findViewById(R.id.es2);
        checksEs.add(es2);
        es3 = findViewById(R.id.es3);
        checksEs.add(es3);
        si = findViewById(R.id.si);
        checksSN.add(si);
        no = findViewById(R.id.no);
        checksSN.add(no);

        selec = findViewById(R.id.seleccionar);

        selec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pulsado el Botón de Seleccionar",Toast.LENGTH_LONG).show();
                if(validar()) {
                    String csv = ruta + "/" + nf + "_Estado" + ".csv";
                    List<String> salida = comprobar();
                    try {
                        CSVWriter csvw = new CSVWriter(new FileWriter(csv));
                        csvw.writeNext(salida.toArray(new String[salida.size()]));
                        csvw.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Mal",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private List<String> comprobar(){
        List<String> salida = new LinkedList();
        for(CheckBox c:checksEs){
            salida.add(Boolean.toString(c.isChecked()));
        }
        for(CheckBox c:checksSN){
            salida.add(Boolean.toString(c.isChecked()));
        }
        return salida;
    }

    private boolean validar(){
        boolean es=false,sn=false;
        for (CheckBox c: checksEs){
            if(c.isChecked()){
                es=true;
            }
        }
        for (CheckBox c: checksSN){
            if(c.isChecked()){
                sn=true;
            }
        }
        if(es==sn){
            return false;
        }
        return true;
    }
}

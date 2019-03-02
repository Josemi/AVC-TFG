package com.example.josemi.aplicacingsaudios;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class EstadoActivity extends AppCompatActivity {

    private String paciente,ruta,nf,csv;
    private TextView texto;
    private CheckBox es1,es2,es3,es4,es5,si,no;
    private Button selec;
    private List<CheckBox> checksEs,checksSN;
    private File acsv;


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

        csv = ruta + "/" + nf + "_Estado" + ".csv";
        acsv = new File(csv);
        if(acsv.exists()) {
            cargarCsv();
            acsv.delete();
        }
        selec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pulsado el Botón de Seleccionar",Toast.LENGTH_LONG).show();
                if(validar()) {
                    List<String> salida = comprobar();
                    try {
                        CSVWriter csvw = new CSVWriter(new FileWriter(csv));
                        csvw.writeNext(salida.toArray(new String[salida.size()]));
                        csvw.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"No se puede dejar vacio, ni rellenar valores de ambas columnas, ni marcar \"si\" y \"no\" a la vez",Toast.LENGTH_LONG).show();
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
        boolean es=false,s=false,n=false,sn=false;
        for (CheckBox c: checksEs){
            if(c.isChecked()){
                es=true;
            }
        }
        s=si.isChecked();
        n=no.isChecked();

        if(s==true || n==true){
            sn=true;
        }
        if(es==sn){
            return false;
        }else if(s==true && n==true){
            return false;
        }
        return true;
    }

    private void cargarCsv(){
        String [] valores = null;
        try{
            Scanner scanner = new Scanner(acsv);
            String linea = scanner.nextLine();
            valores = linea.split(",");
            scanner.close();
        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        }

        int i = 0;
        for (CheckBox c: checksEs){
            c.setChecked(Boolean.parseBoolean(valores[i].replace("\"","")));
            i++;
        }

        for (CheckBox c: checksSN){
            c.setChecked(Boolean.parseBoolean(valores[i].replace("\"","")));
            i++;
        }
    }
}

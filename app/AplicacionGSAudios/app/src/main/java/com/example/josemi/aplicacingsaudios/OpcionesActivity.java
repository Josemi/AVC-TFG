package com.example.josemi.aplicacingsaudios;

import android.app.Activity;
import android.content.Intent;
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

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class OpcionesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String paciente,ruta,nf,csv;
    private TextView texto;
    private Button selec;
    private CheckBox opc1,opc2,opc3;
    private Spinner opc4;
    private List<CheckBox> checks;
    private List<Spinner> spinners;
    private File acsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //Lock de la pantalla en vertical

        checks = new LinkedList();
        spinners = new LinkedList();

        paciente = getIntent().getExtras().getString("paciente");
        ruta = getIntent().getExtras().getString("ruta");
        nf = getIntent().getExtras().getString("nombre");

        texto = findViewById(R.id.texto);
        texto.setText("Seleccione los apartados y opciones para el paciente: " + paciente);

        selec = findViewById(R.id.seleccionar);
        opc1 = findViewById(R.id.opc1);
        checks.add(opc1);
        opc2 = findViewById(R.id.opc2);
        checks.add(opc2);
        opc3 = findViewById(R.id.opc3);
        checks.add(opc3);
        opc4 = findViewById(R.id.opc4);
        spinners.add(opc4);

        opc4.setSelection(-1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.o4,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opc4.setAdapter(adapter);
        opc4.setOnItemSelectedListener(this);


        csv = ruta + "/" + nf + "_Opciones" + ".csv";
        acsv = new File(csv);
        if(acsv.exists()){
            cargarCsv();
            selec.setEnabled(true);
            acsv.delete();
        }else{
            selec.setEnabled(false);
        }

        selec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pulsado el Botón de Seleccionar",Toast.LENGTH_LONG).show();
                String csv = ruta + "/" + nf + "_Opciones" + ".csv";
                List<String> salida = comprobar();
                try {
                    CSVWriter csvw = new CSVWriter(new FileWriter(csv));
                    csvw.writeNext(salida.toArray(new String[salida.size()]));
                    csvw.close();
                }
                catch (IOException ex){
                    ex.printStackTrace();
                }
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

    }

    private List<String> comprobar(){
        List<String> salida = new LinkedList();
        for(CheckBox c:checks){
            salida.add(Boolean.toString(c.isChecked()));
        }
        for(Spinner s : spinners){
            salida.add(s.getSelectedItem().toString());
        }
        return salida;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String p = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(),"La opción seleccionada es: " + p,Toast.LENGTH_LONG).show();
        selec.setEnabled(true);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        for (CheckBox c: checks){
            c.setChecked(Boolean.parseBoolean(valores[i].replace("\"","")));
            i++;
        }
        for(Spinner s: spinners){
            ArrayAdapter<CharSequence> ad = (ArrayAdapter<CharSequence>) s.getAdapter();
            s.setSelection(ad.getPosition(valores[i].replace("\"","")));
            i++;
        }
    }
}

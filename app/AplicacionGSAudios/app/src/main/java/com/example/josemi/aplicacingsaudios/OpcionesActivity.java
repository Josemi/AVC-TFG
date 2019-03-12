/**
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */


//Package
package com.example.josemi.aplicacingsaudios;

//Imports
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

/**
 * Clase para la actividad de la pantalla de selección de opciones. Implementa AdapterView
 */
public class OpcionesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //Variables
    private String paciente,ruta,nf,csv; //Strings con el nombre del paciente, la ruta, el nombre del fichero y el nombre del csv
    private TextView texto; //TextView que se muestra en la parte superior de la pantalla
    private Button selec; //Botón selec
    private CheckBox opc1,opc2,opc3,opc5,opc6,opc7,opc8; //CheckBox con las distintas opciones
    private Spinner opc4,opc9; //Spinner con una de las opciones
    private List<CheckBox> checks; //Lista de todos los CheckBox
    private List<Spinner> spinners; //Lista de todos los Spinners
    private File acsv; //Archivo del csv

    /**
     * Método que se ejecuta al crear el activity.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //Lock de la pantalla en vertical

        //Inicializamos las dos listas
        checks = new LinkedList();
        spinners = new LinkedList();

        //Recogemos los parámetros del intent
        paciente = getIntent().getExtras().getString("paciente");
        ruta = getIntent().getExtras().getString("ruta");
        nf = getIntent().getExtras().getString("nombre");

        //Inicializamos el TextView
        texto = findViewById(R.id.texto);
        texto.setText("Seleccione los apartados y opciones para el paciente: " + paciente);

        //Inicializamos el botón selec, los checkbox y los spinners y los metemos en sus listas
        selec = findViewById(R.id.seleccionar);
        opc1 = findViewById(R.id.opc1);
        checks.add(opc1);
        opc2 = findViewById(R.id.opc2);
        checks.add(opc2);
        opc3 = findViewById(R.id.opc3);
        checks.add(opc3);
        opc4 = findViewById(R.id.opc4);
        spinners.add(opc4);
        opc5 = findViewById(R.id.opc5);
        checks.add(opc5);
        opc6 = findViewById(R.id.opc6);
        checks.add(opc6);
        opc7 = findViewById(R.id.opc7);
        checks.add(opc7);
        opc8 = findViewById(R.id.opc8);
        checks.add(opc8);
        opc9 = findViewById(R.id.opc9);
        spinners.add(opc9);

        //Inicialización de los spinners
        opc4.setSelection(-1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.o4,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opc4.setAdapter(adapter);
        opc4.setOnItemSelectedListener(this);

        opc9.setSelection(-1);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.o9,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opc9.setAdapter(adapter2);
        opc9.setOnItemSelectedListener(this);

        //Ruta del fichero
        csv = ruta + "/" + nf + "_Opciones" + ".csv";
        acsv = new File(csv);

        //Si existe cargamos su contenido, ponemos el botón selec a disponible y eliminamos el fichero
        if(acsv.exists()){
            cargarCsv();
            selec.setEnabled(true);
            acsv.delete();
        }else{ //Sino existe ponemos el botón selec a no disponible
            selec.setEnabled(false);
        }

        //Listener del botón selec
        selec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mensaje de que hemos pulsado el botón
                Toast.makeText(getApplicationContext(),"Pulsado el Botón de Seleccionar",Toast.LENGTH_LONG).show();
                String csv = ruta + "/" + nf + "_Opciones" + ".csv";

                //Recogemos los valores de los checkbox y spinners
                List<String> salida = comprobar();
                try {
                    //Lo escribimos en el fichero
                    CSVWriter csvw = new CSVWriter(new FileWriter(csv));
                    csvw.writeNext(salida.toArray(new String[salida.size()]));
                    csvw.close();
                }
                catch (IOException ex){
                    ex.printStackTrace();
                }

                //Devolvemos el resultado de la actividad
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);

                //Finalizamos la actividad
                finish();
            }
        });

    }

    /**
     * Método que nos devuelve una lista con los resultados de los checkbox y spinners
     * @return Lista resultado
     */
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

    /**
     * Método que se ejecuta al seleccionar un item de un spinner
     * @param parent Adaptador
     * @param view Vista
     * @param position Posición seleccionada
     * @param id Id del spinner
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String p = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(),"La opción seleccionada es: " + p,Toast.LENGTH_LONG).show();
        selec.setEnabled(true);
    }

    /**
     * Método que se ejecuta cuando no se ha seleccionado un iten en un spinner
     * @param parent Adaptador
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Método que nos permite cargar un fichero csv en nuestra pantalla
     */
    private void cargarCsv(){
        String [] valores = null;
        try{
            //Recogemos los valores del fichero csv
            Scanner scanner = new Scanner(acsv);
            String linea = scanner.nextLine();
            valores = linea.split(",");
            scanner.close();
        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        }

        int i = 0;
        //Recorremos los checkbox y los ponemos como venia en el fichero
        for (CheckBox c: checks){
            c.setChecked(Boolean.parseBoolean(valores[i].replace("\"","")));
            i++;
        }
        //Recorremos los spinners y ponemos la opción puesta en el fichero
        for(Spinner s: spinners){
            ArrayAdapter<CharSequence> ad = (ArrayAdapter<CharSequence>) s.getAdapter();
            s.setSelection(ad.getPosition(valores[i].replace("\"","")));
            i++;
        }
    }
}

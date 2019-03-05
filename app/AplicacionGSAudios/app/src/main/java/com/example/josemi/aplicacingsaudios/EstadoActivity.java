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

/**
 * Clase para la actividad de la pantalla de selección de estados.
 */
public class EstadoActivity extends AppCompatActivity {
    //Variables
    private String paciente,ruta,nf,csv; //Strings con el nombre del paciente, la ruta, el nombre del fichero y el nombre del csv
    private TextView texto; //TextView que se muestra en la parte superior de la pantalla
    private CheckBox es1,es2,es3,si,no; //Checkbox con las opciones de los estamos
    private Button selec; //Botón selec
    private List<CheckBox> checksEs,checksSN; //Lista de los distintos checkbox
    private File acsv; //Archivo

    /**
     * Método que se ejecuta al crear el activity.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //Lock de la pantalla en vertical

        //Recogemos los parámetros
        paciente = getIntent().getExtras().getString("paciente");
        ruta = getIntent().getExtras().getString("ruta");
        nf = getIntent().getExtras().getString("nombre");

        //Inicializamos el TextView
        texto = findViewById(R.id.texto);
        texto.setText("Seleccione el/los estados para el paciente: " + paciente);

        //Inicializamos las listas
        checksEs = new LinkedList();
        checksSN = new LinkedList();

        //Inicializamos los checkbox y los metemos en su lista
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

        //Inicializamos el botón selec
        selec = findViewById(R.id.seleccionar);

        //Ruta del fichero
        csv = ruta + "/" + nf + "_Estado" + ".csv";
        acsv = new File(csv);
        //Si existe lo cargamos y lo eliminamos
        if(acsv.exists()) {
            cargarCsv();
            acsv.delete();
        }

        //Listener del botón selec
        selec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mensaje
                Toast.makeText(getApplicationContext(),"Pulsado el Botón de Seleccionar",Toast.LENGTH_LONG).show();

                //Si es valido las opciones puestas
                if(validar()) {
                    //Recogemos los valores
                    List<String> salida = comprobar();
                    try {
                        //Los escribimos en el fichero
                        CSVWriter csvw = new CSVWriter(new FileWriter(csv));
                        csvw.writeNext(salida.toArray(new String[salida.size()]));
                        csvw.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    //Enviamos el resultado
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);

                    //Finalizamos la actividad
                    finish();
                }else{//Si no es válido
                    //Mostramosn un mensaje
                    Toast.makeText(getApplicationContext(),"No se puede dejar vacio, ni rellenar valores de ambas columnas, ni marcar \"si\" y \"no\" a la vez",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Método que nos devuelve la lista con las opciones puestas.
     * @return Lista resultado
     */
    private List<String> comprobar(){
        List<String> salida = new LinkedList();
        //Recorremos los checkbox de los estados normales
        for(CheckBox c:checksEs){
            salida.add(Boolean.toString(c.isChecked()));
        }
        //Recorremos los checkbox de si y no
        for(CheckBox c:checksSN){
            salida.add(Boolean.toString(c.isChecked()));
        }
        return salida;
    }

    /**
     * Método que nos devuelve la validez de las opciones puestas. Es no valido cuando las dos columnas son false, las dos son true o cuando si y no están checkados a la vez.
     * @return true si las opciones son válidas
     */
    private boolean validar(){
        boolean es=false,s=false,n=false,sn=false;
        //Recorremos los checkbox de los estados normales
        for (CheckBox c: checksEs){
            if(c.isChecked()){
                es=true;
            }
        }
        //Ponemos los valores de si y no
        s=si.isChecked();
        n=no.isChecked();

        //Comprobamos que si y no no sean ambos true
        if(s==true || n==true){
            sn=true;
        }

        //Comprobamos si las dos columnas son iguales
        if(es==sn){
            return false;
        }else if(s==true && n==true){
            return false;
        }
        return true;
    }

    /**
     * Método que nos permite cargar un csv ya existente.
     */
    private void cargarCsv(){
        String [] valores = null;
        try{
            //Recogemos los valores del fichero
            Scanner scanner = new Scanner(acsv);
            String linea = scanner.nextLine();
            valores = linea.split(",");
            scanner.close();
        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        }

        //Los ponemos en nuestros checkbox
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

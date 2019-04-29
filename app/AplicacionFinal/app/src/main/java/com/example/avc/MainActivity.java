/**
 * AVC ~ Asistente Virtual para la Comunicación
 *
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */

//Package.
package com.example.avc;

//Imports.
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


/**
 * Clase de la pantalla principal de la aplicación.
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //ImageButtons para entender, opciones, información del ImageButton entender, del ImageButton opciones y de la pantalla información.
    private ImageButton ent,opc,infent,infopc, info;

    //Spinner para la selección del paciente.
    private Spinner spac;

    //String del paciente, de la ruta hasta la carpeta principal, ruta de la carpeta de configuración y del fichero de selección de paciente.
    private String paciente,ruta,config,cpac;

    //File para la carpeta Apace principal, para la carpeta de configuración y para el fichero de selección del paciente.
    private File dir,dconfig,dcpac;

    //MediaPlayer que reproduce los ImageButtons de información.
    private MediaPlayer rpr;

    //Activity para el uso en los diálogos.
    private Activity yo;

    //Bandera para saber cuando es la primera vez que entramos en la selección del spinner.
    private boolean flag;

    //AudioManager para poder controlar cuando no hay volumen.
    private AudioManager auman;

    /**
     * Método que se ejecutará al crearse el Activity.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Llamada al constructor.
        super.onCreate(savedInstanceState);

        //Seleción del layout con el que se relaciona el activity.
        setContentView(R.layout.activity_main);

        //Inicializamos la variable Activity yo con nuestro MainActivity.
        yo = this;

        //Inicializamos el spinner.
        spac = findViewById(R.id.spaciente);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.pac,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spac.setAdapter(adapter);
        spac.setOnItemSelectedListener(this);

        //Inicializamos la variable del paciente con lo seleccionado en el spinner.
        paciente = spac.getSelectedItem().toString();

        //Inicializamos los ImageButtons
        ent = findViewById(R.id.bEntender);
        opc = findViewById(R.id.bOpc);
        infent = findViewById(R.id.bInfEnt);
        infopc = findViewById(R.id.bInfOpc);
        info = findViewById(R.id.bInfAVC);

        //Pedimos los permisos.
        askForPermissions();

        //Inicializamos el AudioManager.
        auman = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //Comprobación y/o creación de la estructura de carpeta y selección en el spinner del paciente almacenado.
        inicio();

        //Creamos la animación para los ImageButtons.
        final Animation animScale = AnimationUtils.loadAnimation(this,R.anim.anim_scale);

        //Listener del ImageButton información
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el nuevo Intent.
                Intent miIntent = new Intent(yo,InfoActivity.class);

                //Empezamos el nuevo intent.
                startActivity(miIntent);
            }
        });

        //Listener del ImageButton entender
        ent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el nuevo Intent.
                Intent miIntent = new Intent(yo,InterpretarActivity.class);

                //Parámetros del intent.
                miIntent.putExtra("paciente",paciente); //Paciente
                miIntent.putExtra("ruta",ruta); //Ruta

                //Empezamos el nuevo intent.
                startActivity(miIntent);
            }
        });

        //Listener del ImageButton opciones.
        opc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el nuevo Intent.
                Intent miIntent = new Intent(yo,OpcionesActivity.class);

                //Parámetros del intent.
                miIntent.putExtra("paciente",paciente);

                //Comenzamos el nuevo intent.
                startActivity(miIntent);
            }
        });

        //Listener del ImageButton información sobre entender.
        infent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el diálogo.
                AlertDialog.Builder infoBuilder = new AlertDialog.Builder(yo);
                final AlertDialog info = infoBuilder.create();
                infoBuilder.setTitle("Información botón Qué quiero decir");
                infoBuilder.setMessage("Haz clic en  “qué quiero decir” y podrás saber qué es lo que estoy intentando decirte con los sonidos que hago.");

                //Reproducimos el audio correspondiente.
                rpr = MediaPlayer.create(yo,R.raw.quequierodecir);
                rpr.start();

                //Botón para cerrar el diálogo.
                infoBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Si estamos reproduciendo el audio lo paramos
                        if(rpr!=null) {
                            if (rpr.isPlaying()) {
                                rpr.stop();
                            }
                        }
                        //Cerramos el diálogo.
                        info.cancel();
                    }
                });
                //Mostramos el diálogo.
                infoBuilder.show();

                //Comprobamos el volumen multimedia del dispositivo.
                if(auman.getStreamVolume(AudioManager.STREAM_MUSIC)==0){
                    Toast.makeText(getApplicationContext(),"El volumen multimedia está muteado, si quiere escuchar la explicación suba el volumen.",Toast.LENGTH_LONG).show();
                }
            }
        });

        //Listener del ImageButton de información de opciones.
        infopc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el diálogo.
                AlertDialog.Builder infoBuilder = new AlertDialog.Builder(yo);
                final AlertDialog info = infoBuilder.create();
                infoBuilder.setTitle("Información botón Registro de Información");
                infoBuilder.setMessage("Registro de Información es sólo para el/la cuidador/a, padre o madre de la persona.");

                //Reproducimos el audio correspondiente.
                rpr = MediaPlayer.create(yo,R.raw.registroinfo);
                rpr.start();

                //listener del botón para cerrar el diálogo.
                infoBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Si estamos reproduciendo el audio lo paramos.
                        if(rpr!=null) {
                            if (rpr.isPlaying()) {
                                rpr.stop();
                            }
                        }

                        //Cerramos el diálogo.
                        info.cancel();
                    }
                });

                //Mostramos el diálogo.
                infoBuilder.show();

                //Comprobamos el volumen multimedia del dispositivo.
                if(auman.getStreamVolume(AudioManager.STREAM_MUSIC)==0){
                    Toast.makeText(getApplicationContext(),"El volumen multimedia está muteado, si quiere escuchar la explicación suba el volumen.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Método que se ejecuta cuando se selecciona un elemento en el Spinner.
     * @param parent Adaptador del spinner
     * @param view Vista
     * @param position Posición del spinner
     * @param id Id del spinner
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Ponemos el string del paciente con el nuevo valor
        paciente = parent.getItemAtPosition(position).toString();

        if(flag) {
            //Llamamos al método que guarda la selección en el fichero para tener persistencia.
            guardarPaciente();
            //Mensaje con el paciente seleccionado.
            Toast.makeText(getApplicationContext(),"El paciente seleccionado es " + paciente,Toast.LENGTH_LONG).show();
        }

        flag = true;
    }

    /**
     * Método que se ejecuta cuando no se selecciona un elemento en el Spinner. No lo uso pero he de tenerlo al implementar la interfaz del adaptador
     * @param parent Adaptador
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Método para pedir los permisos.
     */
    private void askForPermissions() {
        //Permisos que queremos pedir
        String[] perm = {Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Si alguno no está dado los pedimos
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),perm[0]) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this.getApplicationContext(),perm[1]) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(perm,1234);
        }
    }

    /**
     * Método que nos permite comprobar y/o generar la estructura de carpetas y cargar el paciente persistente.
     */
    public void inicio(){
        //Inicializamos el string y el file de la ruta.
        ruta = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Apace";
        dir = new File(ruta);

        //Si la carpeta de la ruta no existe la creamos
        if(!dir.exists()){
            dir.mkdir();
        }

        //Inicializamos el string y el file de la ruta de la carpeta de configuración.
        config = ruta + "/config";
        dconfig = new File(config);

        //Si la carpeta no existe la creamos.
        if(!dconfig.exists()){
            dconfig.mkdir();
        }

        //Inicializamos el string y el file de la ruta del fichero de la persistencia del paciente.
        cpac = config + "/paciente.csv";
        dcpac =  new File(config,"paciente.csv");
        String[] valor=new String[1];
        if(dcpac.exists()) {
            try {
                //Recogemos los valores del fichero csv
                Scanner scanner = new Scanner(dcpac);
                String linea = scanner.nextLine();
                valor = linea.split(",");
                scanner.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            ArrayAdapter<CharSequence> ad = (ArrayAdapter<CharSequence>) spac.getAdapter();
            try { //Si al leer el valor 0 no es nada es que no existe el fichero (si hacía un dcpac.exists() del csv siempre existía aunque no existiese en la primera ejecución).
                spac.setSelection(ad.getPosition(valor[0].replace("\"", ""))); //Debería solo haber un elemento en el csv que es el paciente.
            }catch(RuntimeException ex){
                spac.setSelection(ad.getPosition("JMiguel")); //Debería solo haber un elemento en el csv que es el paciente.
            }
            paciente = spac.getSelectedItem().toString();
        }else{
            guardarPaciente();
        }
    }

    /**
     * Método quee nos permite dar persistencia a la selección del paciente.
     */
    private void guardarPaciente(){
        //Si el fichero de la persistencia del paciente existe lo eliminamos
        if(dcpac.exists()){
            dcpac.delete();
        }

        String[] valor = new String[1];
        valor[0] = spac.getSelectedItem().toString();

        //Escribimos en el fichero el valor actual del spinner del paciente.
        try {
            //Lo escribimos en el fichero
            CSVWriter csvw = new CSVWriter(new FileWriter(cpac));
            csvw.writeNext(valor);
            csvw.close();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
}

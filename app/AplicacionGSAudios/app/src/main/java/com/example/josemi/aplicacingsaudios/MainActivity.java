/**
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */

//Package
package com.example.josemi.aplicacingsaudios;

//Imports
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Clase MainActivity con la actividad de la pantalla principal.
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Botones para movernos a las pantallas y enviar el comprimido
    private Button grabar,opciones,estado,enviar,sel,cancelar;

    //Spinner para seleccionar el paciente
    private Spinner sp;

    //Strings para el paciente, las rutas, los nombres de los ficheros y el formato del audio
    private String paciente,ruta,rutac,nf,formato,azip;

    //Archivos de la carpeta principal, la creada, el archivo del audio y los dos csv
    private File dir,dirc,audio,opc,est,afile;

    /**
     * Método que se ejecutará al crearse el Activity.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Llamada al construvtor
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hacemos que no se pueda girar la pantalla
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //Lock de la pantalla en vertical

        //Llamada al método que pide los permisos de almacenamiento y de acceso al microfono
        askForPermissions();

        //Inicialización del spinner del paciente
        sp = findViewById(R.id.paciente);
        sp.setSelection(-1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.pac,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);
        //Guardamos el valor en el String paciente
        paciente = sp.getSelectedItem().toString();

        //Ruta a la carpeta Apace desde donde trabajamos
        ruta = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Apace";
        dir = new File(ruta);
        //Si la carpeta de la ruta no existe la creamos
        if(!dir.exists()){
            dir.mkdir();
        }

        //Inicializamos los botones
        grabar = findViewById(R.id.grabar);
        grabar.setEnabled(false);
        opciones = findViewById(R.id.opciones);
        opciones.setEnabled(false);
        estado = findViewById(R.id.estados);
        estado.setEnabled(false);
        enviar = findViewById(R.id.enviar);
        enviar.setEnabled(false);
        sel = findViewById(R.id.selec);
        sel.setEnabled(true);
        cancelar = findViewById(R.id.cancelar);
        cancelar.setEnabled(false);

        //Formato del audio, se hace en esta pantalla para poder confirmar la existencia del fichero
        formato = ".mp4";

        //Listener del botón grabar
        grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos un intent para movernos de pantalla
                Intent intent = new Intent(MainActivity.this,GrabarActivity.class);

                //Parámetros que pasamos
                intent.putExtra("ruta",rutac); //Ruta de la carpeta
                intent.putExtra("nombre",nf); //Nombre del fichero
                intent.putExtra("formato",formato); //Formato del fichero

                //Inicializamos el File del audio
                audio = new File(rutac+"/"+nf+formato);

                //Comenzamos la nueva actividad con un code de 1, para poder saber si ha finalizado correctamente
                startActivityForResult(intent,1);
            }
        });

        //Listener del botón opciones
        opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creamos el intent
                Intent intent = new Intent(MainActivity.this,OpcionesActivity.class);

                //Parámetros que pasamos
                intent.putExtra("paciente", paciente); //Nombre del paciente
                intent.putExtra("ruta",rutac); //Ruta de la carpeta
                intent.putExtra("nombre",nf); //Nombre del fichero

                //Inicializamos el File con el csv de las opciones
                opc=new File(rutac + "/" + nf + "_Opciones" + ".csv");

                //Comenzamos la nueva actividad con un code de 2
                startActivityForResult(intent,2);
            }
        });

        //Listener del botón estado
        estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos el intent
                Intent intent = new Intent(MainActivity.this,EstadoActivity.class);

                //Parámetros que pasamos a la nueva actividad
                intent.putExtra("paciente", paciente); //Nombre del paciente
                intent.putExtra("ruta",rutac); //Ruta de la carpeta
                intent.putExtra("nombre",nf); //Nombre del fichero

                //Inicializamos el File del estado
                est=new File(rutac + "/" + nf + "_Estado" + ".csv");

                //Comenzamos la nueva actividad con code de 3
                startActivityForResult(intent,3);
            }
        });


        //Listener del botón enviar
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mensaje
                Toast.makeText(getApplicationContext(),"Pulsado Botón de Enviar",Toast.LENGTH_LONG).show();

                //Llamada al método para comprimir
                comprimir();

                //Actualizamos la visibilidad de los botones
                sp.setEnabled(true);
                sel.setEnabled(true);
                grabar.setEnabled(false);
                opciones.setEnabled(false);
                estado.setEnabled(false);
                enviar.setEnabled(false);
                cancelar.setEnabled(false);

            }
        });

        //Listener del botón seleccionar
        sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hacemos que no se pueda cambiar de paciente
                sp.setEnabled(false);
                sel.setEnabled(false);

                //Podemos grabar y cancelar
                grabar.setEnabled(true);
                cancelar.setEnabled(true);

                //Creamos la carpeta
                crearCarpeta();

                //Imprimimos el paciente
                Toast.makeText(getApplicationContext(),"El cliente con el que se va a grabar es: " + paciente + ", si desea cambiarlo pulse el botón Cancelar",Toast.LENGTH_LONG).show();
            }
        });

        //Listener del botón cancelar
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hacemos que se pueda cambiar de paciente
                sp.setEnabled(true);
                sel.setEnabled(true);

                //Ponemos a inhabilitado el resto
                grabar.setEnabled(false);
                opciones.setEnabled(false);
                estado.setEnabled(false);
                enviar.setEnabled(false);
                cancelar.setEnabled(false);

                //Imprimimos el paciente
                Toast.makeText(getApplicationContext(),"Cancelada la grabación",Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Método para pedir los permisos.
     */
    private void askForPermissions() {
        //Permisos que queremos pedir
        String[] perm = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Si alguno no está dado los pedimos
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),perm[0]) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this.getApplicationContext(),perm[1]) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(perm,1234);
        }
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
        //Ponemos en el String paciente la opción seleccionada
        paciente = parent.getItemAtPosition(position).toString();
    }

    /**
     * Método que se ejecuta cuando no se selecciona un elemento en el Spinner. No lo uso pero he de tenerlo al implementar la interfaz del adaptador
     * @param parent Adaptador
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Método que nos permite crear una nueva carpeta y dar nombre a los ficheros
     */
    private void crearCarpeta(){

        //Obtenemos la fecha actual
        Date ahora = new Date();

        //Creamos el formato
        SimpleDateFormat ff = new SimpleDateFormat("hh-mm-ss_dd-MM-yyyy");

        //Ponemos el nombre de los archivos
        nf = paciente + "_" + ff.format(ahora);

        //Ruta de la nueva carpeta
        rutac = ruta + "/" + nf;

        //Creamos el File
        dirc = new File(rutac);

        //Si la carpeta de la ruta no existe la creamos, no debería existir, pero por si acaso mejor ponerlo.
        if(!dirc.exists()){
            dirc.mkdir();
        }
    }

    /**
     * Método para comprobar si la finalización de los activities ha sido correcta.
     *
     * @param requestCode Código del intent
     * @param resultCode Resultado de la actividad
     * @param data Intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Switch de la procedencia
        switch(requestCode) {
            case (1) : {
                //Si el resultado ha sido OK
                if (resultCode == Activity.RESULT_OK) {
                    //Opciones pasa a estar disponible
                    opciones.setEnabled(true);
                }
                break;
            }
            case (2): {
                if (resultCode == Activity.RESULT_OK) {
                    //Estado pasa a estar disponible
                    estado.setEnabled(true);
                }
                break;
            }
            case (3):{
                if (resultCode == Activity.RESULT_OK) {
                    //Enviar pasa a estar disponible
                    enviar.setEnabled(true);
                }
                break;
            }
        }
    }

    /**
     * Método para comprimir los archivos creados.
     */
    private void comprimir(){
        //Archivos y carpetas
        azip = rutac + ".zip";
        afile = new File(azip);
        File [] archivos = {audio,opc,est};
        try {
            //Streams de los datos
            FileOutputStream fos = new FileOutputStream(afile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            //Por cada uno de los archivos
            for(int i=0; i < archivos.length;i++){
                //Buffer
                byte [] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(archivos[i]);
                zos.putNextEntry(new ZipEntry(archivos[i].getName()));
                int l;
                while ((l=fis.read(buffer)) > 0){
                    zos.write(buffer,0,l);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        }   catch (FileNotFoundException ex){
            ex.printStackTrace();
        }   catch (IOException ex){
            ex.printStackTrace();
        }
    }
}

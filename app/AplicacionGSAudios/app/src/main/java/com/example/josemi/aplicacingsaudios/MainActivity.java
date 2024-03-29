/**
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */

//Package
package com.example.josemi.aplicacingsaudios;

//Imports
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
    private ImageButton grabar,opciones,estado,enviar,sel,cancelar, infgrabar, infopciones, infestado;

    //Spinner para seleccionar el paciente
    private Spinner sp;

    //Strings para el paciente, las rutas, los nombres de los ficheros y el formato del audio
    private String paciente,ruta,rutac,nf,formato,azip;

    //Archivos de la carpeta principal, la creada, el archivo del audio y los dos csv
    private File dir,dirc,audio,opc,est,afile;

    //Conexión
    private ConnectivityManager conexion;

    //Activity para pasarle a los diálogos.
    private Activity yo;

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

        yo = this;

        //Conexión
        conexion = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //Inicialización del spinner del paciente
        sp = findViewById(R.id.paciente);
        sp.setSelection(-1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.pac, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);
        //Guardamos el valor en el String paciente
        paciente = sp.getSelectedItem().toString();

        //Creamos la animación para los ImageButtons.
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);

        //Ruta a la carpeta Apace desde donde trabajamos
        ruta = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Apace";
        dir = new File(ruta);
        //Si la carpeta de la ruta no existe la creamos
        if (!dir.exists()) {
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
        infgrabar = findViewById(R.id.infgrabar);
        infopciones = findViewById(R.id.infopciones);
        infestado = findViewById(R.id.infestados);

        //Formato del audio, se hace en esta pantalla para poder confirmar la existencia del fichero
        formato = ".mp4";

        //Listener del botón grabar
        grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos un intent para movernos de pantalla
                Intent intent = new Intent(MainActivity.this, GrabarActivity.class);

                //Parámetros que pasamos
                intent.putExtra("ruta", rutac); //Ruta de la carpeta
                intent.putExtra("nombre", nf); //Nombre del fichero
                intent.putExtra("formato", formato); //Formato del fichero

                //Inicializamos el File del audio
                audio = new File(rutac + "/" + nf + formato);

                //Comenzamos la nueva actividad con un code de 1, para poder saber si ha finalizado correctamente
                startActivityForResult(intent, 1);
            }
        });

        //Listener del botón opciones
        opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el intent
                Intent intent = new Intent(MainActivity.this, OpcionesActivity.class);

                //Parámetros que pasamos
                intent.putExtra("paciente", paciente); //Nombre del paciente
                intent.putExtra("ruta", rutac); //Ruta de la carpeta
                intent.putExtra("nombre", nf); //Nombre del fichero

                //Inicializamos el File con el csv de las opciones
                opc = new File(rutac + "/" + nf + "_Opciones" + ".csv");

                //Comenzamos la nueva actividad con un code de 2
                startActivityForResult(intent, 2);
            }
        });

        //Listener del botón estado
        estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el intent
                Intent intent = new Intent(MainActivity.this, EstadoActivity.class);

                //Parámetros que pasamos a la nueva actividad
                intent.putExtra("paciente", paciente); //Nombre del paciente
                intent.putExtra("ruta", rutac); //Ruta de la carpeta
                intent.putExtra("nombre", nf); //Nombre del fichero

                //Inicializamos el File del estado
                est = new File(rutac + "/" + nf + "_Estado" + ".csv");

                //Comenzamos la nueva actividad con code de 3
                startActivityForResult(intent, 3);
            }
        });


        //Listener del botón enviar
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Realizamos la animación.
                v.startAnimation(animScale);

                //Si tenemos conexion comprimimos y enviamos
                if (conexion.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || conexion.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
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

                    //Enviamos por correo el comprimido
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        @SuppressWarnings("WrongThread")
                        public Void doInBackground(Void... arg) {
                            enviarComprimido();
                            return null;
                        }
                    }.execute();

                    //Mensaje
                    Toast.makeText(getApplicationContext(), "Se ha enviado correctamente", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "No hay conexión a Internet", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Listener del botón seleccionar
        sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Realizamos la animación.
                v.startAnimation(animScale);

                //Hacemos que no se pueda cambiar de paciente
                sp.setEnabled(false);
                sel.setEnabled(false);
                sel.setBackgroundResource(R.drawable.boton2);

                //Podemos grabar y cancelar
                grabar.setEnabled(true);
                grabar.setBackgroundResource(R.drawable.boton);
                cancelar.setEnabled(true);
                cancelar.setBackgroundResource(R.drawable.boton);

                //Creamos la carpeta
                crearCarpeta();

                //Imprimimos el paciente
                Toast.makeText(getApplicationContext(), "El cliente con el que se va a grabar es: " + paciente + ", si desea cambiarlo pulse el botón Cancelar", Toast.LENGTH_LONG).show();
            }
        });

        //Listener del botón cancelar
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Realizamos la animación.
                v.startAnimation(animScale);

                //Hacemos que se pueda cambiar de paciente
                sp.setEnabled(true);
                sel.setEnabled(true);
                sel.setBackgroundResource(R.drawable.boton);

                //Ponemos a inhabilitado el resto
                grabar.setEnabled(false);
                grabar.setBackgroundResource(R.drawable.boton2);
                opciones.setEnabled(false);
                opciones.setBackgroundResource(R.drawable.boton2);
                estado.setEnabled(false);
                estado.setBackgroundResource(R.drawable.boton2);
                enviar.setEnabled(false);
                enviar.setBackgroundResource(R.drawable.boton2);
                cancelar.setEnabled(false);
                cancelar.setBackgroundResource(R.drawable.boton2);

                //Imprimimos el paciente
                Toast.makeText(getApplicationContext(), "Cancelada la grabación", Toast.LENGTH_LONG).show();
            }
        });

        //Listener de información de grabar
        infgrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el diálogo.
                AlertDialog.Builder infoBuilder = new AlertDialog.Builder(yo);
                final AlertDialog info = infoBuilder.create();
                infoBuilder.setTitle("Información botón Grabar");
                infoBuilder.setMessage("Haz clic en Grabar, para  ir a la pantalla donde podrá grabar al paciente seleccionado.");

                //Listener del botón aceptar del diálogo.
                infoBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        info.cancel();
                    }
                });
                //Mostramos el diálogo.
                infoBuilder.show();
            }
        });

        //Listener de información de grabar
        infopciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el diálogo.
                AlertDialog.Builder infoBuilder = new AlertDialog.Builder(yo);
                final AlertDialog info = infoBuilder.create();
                infoBuilder.setTitle("Información botón Registro Información");
                infoBuilder.setMessage("Haz clic en Registro Información, para  ir a la pantalla donde podrá rellenar la información adicional relacionada" +
                        "con el paciente seleccionado.");


                //Listener del botón aceptar del diálogo.
                infoBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        info.cancel();
                    }
                });
                //Mostramos el diálogo.
                infoBuilder.show();
            }
        });

        //Listener de información de grabar
        infestado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el diálogo.
                AlertDialog.Builder infoBuilder = new AlertDialog.Builder(yo);
                final AlertDialog info = infoBuilder.create();
                infoBuilder.setTitle("Información botón Qué Me Pasa");
                infoBuilder.setMessage("Haz clic en Qué Me Pasa, para  ir a la pantalla donde podrá seleccionar la emoción o respuesta que ha grabado del paciente.");

                //Listener del botón aceptar del diálogo.
                infoBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        info.cancel();
                    }
                });
                //Mostramos el diálogo.
                infoBuilder.show();
            }
        });
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
                    opciones.setBackgroundResource(R.drawable.boton);
                }
                break;
            }
            case (2): {
                if (resultCode == Activity.RESULT_OK) {
                    //Estado pasa a estar disponible
                    estado.setEnabled(true);
                    estado.setBackgroundResource(R.drawable.boton);
                }
                break;
            }
            case (3):{
                if (resultCode == Activity.RESULT_OK) {
                    //Enviar pasa a estar disponible
                    enviar.setEnabled(true);
                    enviar.setBackgroundResource(R.drawable.boton);
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

    /**
     * Método que nos permite enviar el comprimido por correo
     */
    private void enviarComprimido(){

        try{
            GMailSender sender = new GMailSender("comprimidosApace@gmail.com", "apaceubu");
            sender.sendMail(nf,
                    "",
                    "comprimidosApace@gmail.com",
                    "comprimidosApace@gmail.com",
                   afile);
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
    }
}

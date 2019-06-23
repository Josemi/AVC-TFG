package com.example.josemi.apace;


import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.io.File;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase con la lógica de la aplicación prototipo.
 */
public class MainActivity extends AppCompatActivity {

        //Variables
        private Button play,stop,record,generador; //Botonoes para reproducir, parar, grabar y generar un nombre
        private MediaRecorder audioRec; //MediaRecorder que nos permite grabar
        private EditText outputFile; //EditText donde se escribe el nombre del fichero
        private String ruta,formato; //String de la ruta a la carpeta donde se almacenarán los audios
        private Boolean permisos; //Boolean para setear las opciones del MediaRecorder


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            permisos = false; //Permisos a falso
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //Lock de la pantalla en vertical

            //Inicializamos los botonoes y el EditText
            play = findViewById(R.id.play);
            stop = findViewById(R.id.stop);
            record = findViewById(R.id.record);
            generador = findViewById(R.id.generador);
            outputFile = findViewById(R.id.ruta);

            //Inicializamos la ruta a la carpeta donde se encuentran los audios
            ruta = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Apace";
            File dir = new File(ruta);

            //Formato
            formato = ".mp4";

            //Ponemos que no se puedan clickear los botones de stop y de play
            stop.setEnabled(false);
            play.setEnabled(false);

            //Inicializamos el MediaRecorder
            audioRec = new MediaRecorder();

            //Pedimos los permisos
            askForPermissions();

            //Si la carpeta de la ruta no existe la creamos
            if(!dir.exists()){
                    dir.mkdir();
            }

            //Listener del botón Record
            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Si es la primera vez que entramos ponemos los set de MediaRecorder
                    if (!permisos){
                        setConfig(audioRec);
                        permisos = true;
                    }

                    //Ruta del nuevo audio
                    String audio = ruta + "/" +outputFile.getText().toString() + formato;
                    File aufile = new File(audio);

                    //Si ya existe un fichero con ese nombre no se comienza la grabación y se muestra un mensaje
                    if(aufile.exists()) {
                            Toast.makeText(getApplicationContext(), "El nombre del fichero ya está en uso", Toast.LENGTH_LONG).show();

                    //Si la ruta está vacia se informa de que no se puede grabar un audio sin nombre y no se graba
                    }else if(outputFile.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "Debe poner dirección de la ruta", Toast.LENGTH_LONG).show();

                    //Si no ocurren cualquiera de estas cosas se pasa a grabar
                    }else {
                            try {
                                    audioRec.setOutputFile(audio);
                                    audioRec.prepare();
                                    audioRec.start();
                            } catch (IOException e) {
                                    e.printStackTrace();
                            }

                            //Habilitamos el botón de play y de stop, e inhabilitamos el botón de grabar y el EditText del nombre
                            record.setEnabled(false);
                            stop.setEnabled(true);
                            outputFile.setEnabled(false);

                            //Mensaje de que se está grabando
                            Toast.makeText(getApplicationContext(), "Grabación en curso", Toast.LENGTH_LONG).show();
                    }
                }
            });


            //Listener del botón Stop
            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    //Paramos la grabación
                    audioRec.stop();
                    audioRec.release();

                    //Reinicializamos el MediaRecorder ya que no se puede reutilizar
                    audioRec = new MediaRecorder();
                    setConfig(audioRec);

                    //Habilitamos el botón record y play y el EditText del nombre,inhabilitamos el botón stop
                    record.setEnabled(true);
                    play.setEnabled(true);
                    stop.setEnabled(false);
                    outputFile.setEnabled(true);

                    //Mensaje de que el audio se ha grabado correctamente
                    Toast.makeText(getApplicationContext(), "Audio grabado correctamente" , Toast.LENGTH_LONG).show();
                }
            });

            //Listener del botón play
            play.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //Creamos el MediaPlayer para reproducir el audio
                    MediaPlayer repAudio = new MediaPlayer();
                    try {
                        //Reproducimos el audio de la ruta
                        repAudio.setDataSource(ruta + "/" +outputFile.getText().toString() + formato);
                        repAudio.prepare();
                        repAudio.start();

                        //Mensaje de que se está reproduciendo el audio
                        Toast.makeText(getApplicationContext(),"Reproduciendo el último audio grabado", Toast.LENGTH_LONG).show();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });

            //Listener del botón para generar nombres
            generador.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //Obtenemos la fecha actual
                    Calendar hoy = Calendar.getInstance();

                    //Pasamos el Calendar a Date para poder darle formato
                    Date dhoy = hoy.getTime();

                    //Creamos el formato que queremos, en nuestro caso con horas minutos segundos
                    SimpleDateFormat ff = new SimpleDateFormat("hh-mm-ss_dd-MM-yyyy");

                    //Creamos el string que será el nombre generado
                    String aux = ff.format(dhoy);

                    //Ponemos el EditText con este String
                    outputFile.setText(aux);
                }
            });
    }

    /**
    * Método para pedir permisos para grabar audios y para escribir en el almacenamiento.
    *
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
     * Métood que permite configurar el MediaRecorder.
     * @param audioRec MediaRecorder a configurar.
     */
    private void setConfig(MediaRecorder audioRec){
        audioRec.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        audioRec.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        audioRec.setAudioEncodingBitRate(128000);
        audioRec.setAudioSamplingRate(48000);
    }
}

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
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Clase para la actividad de la pantalla de grabar audios.
 */
public class GrabarActivity extends AppCompatActivity {

    //Variables
    private Button play,stop,record, selec; //Botonoes para reproducir, parar, grabar y generar un nombre
    private MediaRecorder audioRec; //MediaRecorder que nos permite grabar
    private String ruta,formato,nf,audio; //String de la ruta a la carpeta donde se almacenarán los audios
    private MediaPlayer repAudio; //Reproductor de los audios
    private File aufile; //Archivo de salida

    /**
     * Método que se ejecuta al crear el activity
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabar);

        //Hacemos que la pantalla no se pueda girar
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //Lock de la pantalla en vertical

        //Recogemos los parámetros que llegan con el intent
        ruta = getIntent().getExtras().getString("ruta");
        nf = getIntent().getExtras().getString("nombre");
        formato = getIntent().getExtras().getString("formato");

        //Inicializamos los botonoes y el EditText
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        record = findViewById(R.id.record);
        selec = findViewById(R.id.seleccionar);

        //Inicializamos el MediaRecorder
        audioRec = new MediaRecorder();

        //Lamada al método que configura el Media Recorder
        setConfig(audioRec);

        //Inicializamos el String del fichero
        audio = ruta + "/" + nf + formato;
        //Inicializamos el File del fichero
        aufile = new File(audio);

        //Si el fichero existe ponemos visible el botón de grabar, de reproducir y de seleccionar
        if(aufile.exists()){
            setVisibilidad(true,false,true,true);
        }else{
            //Si no existe, es la primera vez que entramos, ponemos a visible solo el botón de grabar
            setVisibilidad(true,false,false,false);
        }

        //Listener del botón record
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Ponemos a visible solo el botón de stop
                setVisibilidad(false,true,false,false);

                //Si el reproductor no es nulo y está reproduciendo lo paremos
                if(repAudio!=null) {
                    if (repAudio.isPlaying()) {
                        repAudio.stop();
                    }
                }

                //Si ya existe, es decir, es la segunda vez que grabamos borramos la grabación anterior
                if(aufile.exists()) {
                    aufile.delete();
                }

                //Comenzamos a grabar
                try {
                    audioRec.setOutputFile(audio);
                    audioRec.prepare();
                    audioRec.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Mensaje de que se está grabando
                Toast.makeText(getApplicationContext(), "Grabación en curso", Toast.LENGTH_LONG).show();

            }
        });

        //Listener del botón stop
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Ponemos a visibles los botones de grabar reproducir y seleccionar
                setVisibilidad(true,false,true,true);

                //Paramos la grabación
                audioRec.stop();
                audioRec.release();

                //Reinicializamos el MediaRecorder ya que no se puede reutilizar
                audioRec = new MediaRecorder();
                setConfig(audioRec);

                //Mensaje de que el audio se ha grabado correctamente
                Toast.makeText(getApplicationContext(), "Audio grabado correctamente" , Toast.LENGTH_LONG).show();
            }
        });

        //Listener del botón play
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Reproducimos el audio de la ruta
                    repAudio = new MediaPlayer();
                    repAudio.setDataSource(ruta + "/" + nf + formato);
                    repAudio.prepare();
                    repAudio.start();

                    //Mensaje de que se está reproduciendo el audio
                    Toast.makeText(getApplicationContext(),"Reproduciendo el último audio grabado", Toast.LENGTH_LONG).show();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        //Listener del botón selec
        selec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si el reproducrto no es nulo y está reproduciendo lo paramos
                if(repAudio!=null) {
                    if (repAudio.isPlaying()) {
                        repAudio.stop();
                    }
                }

                //Menmsaje de que se ha pulsado el botón selec
                Toast.makeText(getApplicationContext(),"Audio guardado",Toast.LENGTH_LONG).show();

                //Creamos un nuevo intent para poder enviar el resultado de la pantalla
                Intent resultIntent = new Intent();
                //Ponemos como resultado OK
                setResult(Activity.RESULT_OK, resultIntent);

                //Finalizamos la actividad
                finish();
            }
        });
    }

    /**
     * Método para settear la configuración del Media Recorder
     * @param audioRec Media Recorder
     */
    private void setConfig(MediaRecorder audioRec){
        audioRec.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        audioRec.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        audioRec.setAudioEncodingBitRate(128000);
        audioRec.setAudioSamplingRate(48000);
    }

    /**
     * Método que nos permite cambiar la visibilidad de todos los botones de la actividad.
     * @param rec Boolean con la visibilidad del botón record
     * @param st Boolean con la visibilidad del botón stop
     * @param pl Boolean con la visibilidad del botón play
     * @param sel Boolean con la visibilidad del botón selec
     */
    private void setVisibilidad(boolean rec,boolean st, boolean pl, boolean sel){
        record.setEnabled(rec);
        stop.setEnabled(st);
        play.setEnabled(pl);
        selec.setEnabled(sel);
    }

}

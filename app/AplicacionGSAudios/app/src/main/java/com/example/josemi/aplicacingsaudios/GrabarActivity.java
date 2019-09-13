/**
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */


//Package
package com.example.josemi.aplicacingsaudios;

//Imports
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Clase para la actividad de la pantalla de grabar audios.
 */
public class GrabarActivity extends AppCompatActivity {

    //Variables
    private ImageButton play,stop,record, selec, canc, infplay, infstop, infrecord; //Botonoes para reproducir, parar, grabar y generar un nombre
    private MediaRecorder audioRec; //MediaRecorder que nos permite grabar
    private String ruta,formato,nf,audio; //String de la ruta a la carpeta donde se almacenarán los audios
    private MediaPlayer repAudio; //Reproductor de los audios
    private File aufile; //Archivo de salida
    private boolean grab;
    //Activity para pasarle a los diálogos.
    private Activity yo;
    //Chronometer para el tiempo grabado.
    private Chronometer crono;

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

        grab = true;

        //Recogemos los parámetros que llegan con el intent
        ruta = getIntent().getExtras().getString("ruta");
        nf = getIntent().getExtras().getString("nombre");
        formato = getIntent().getExtras().getString("formato");

        yo = this;

        //Inicializamos los botonoes y el EditText
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        record = findViewById(R.id.record);
        selec = findViewById(R.id.seleccionar);
        canc = findViewById(R.id.canc);
        infplay = findViewById(R.id.infplay);
        infstop = findViewById(R.id.infstop);
        infrecord = findViewById(R.id.infrecord);
        crono = findViewById(R.id.crono);

        //Creamos la animación para los ImageButtons.
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);

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

                //Realizamos la animación.
                v.startAnimation(animScale);

                //Ponemos a visible solo el botón de stop
                setVisibilidad(false,true,false,false);

                grab = false;

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

                //Empezamos a contar con el cronómetro.
                crono.setBase(SystemClock.elapsedRealtime());
                crono.start();

                //Mensaje de que se está grabando
                Toast.makeText(getApplicationContext(), "Grabación en curso", Toast.LENGTH_LONG).show();

            }
        });

        //Listener del botón stop
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Realizamos la animación.
                v.startAnimation(animScale);

                //Ponemos a visibles los botones de grabar reproducir y seleccionar
                setVisibilidad(true,false,true,true);

                grab = false;

                //Paramos el crono.
                crono.stop();


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

                //Realizamos la animación.
                v.startAnimation(animScale);

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

                //Realizamos la animación.
                v.startAnimation(animScale);

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

        //Listener del botón cancelar
        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Realizamos la animación.
                v.startAnimation(animScale);

                //Si estamos grabamos paramos.
                if(audioRec!= null && grab==false) {
                    try {
                        audioRec.stop();
                        audioRec.release();
                    }catch(IllegalStateException ex){
                        ex.printStackTrace();
                    }catch(RuntimeException ex){
                        ex.printStackTrace();
                    }
                }

                //Si estamos reproduciendo un audio paramos.
                if(repAudio!=null) {
                    if (repAudio.isPlaying()) {
                        repAudio.stop();
                    }
                }

                //Result intent con resultado cancelado.
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        });

        //Listener de información de grabar
        infrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el diálogo.
                AlertDialog.Builder infoBuilder = new AlertDialog.Builder(yo);
                final AlertDialog info = infoBuilder.create();
                infoBuilder.setTitle("Información botón Grabar");
                infoBuilder.setMessage("Haz clic en Grabar, para comenzar a grabar al paciente, si ya hay un audio grabado se sobreescribirá.");

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
        infstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el diálogo.
                AlertDialog.Builder infoBuilder = new AlertDialog.Builder(yo);
                final AlertDialog info = infoBuilder.create();
                infoBuilder.setTitle("Información botón Parar");
                infoBuilder.setMessage("Haz clic en Parar, para parar la grabación en curso y guardarla.");

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
        infplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el diálogo.
                AlertDialog.Builder infoBuilder = new AlertDialog.Builder(yo);
                final AlertDialog info = infoBuilder.create();
                infoBuilder.setTitle("Información botón Escuchar");
                infoBuilder.setMessage("Haz clic en Escuchar, para  escuchar el último audio grabado.");

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
        if(rec){
            record.setBackgroundResource(R.drawable.boton);
        }else{
            record.setBackgroundResource(R.drawable.boton2);
        }
        stop.setEnabled(st);
        if(st){
            stop.setBackgroundResource(R.drawable.boton);
        }else{
            stop.setBackgroundResource(R.drawable.boton2);
        }
        play.setEnabled(pl);
        if(pl){
            play.setBackgroundResource(R.drawable.boton);
        }else{
            play.setBackgroundResource(R.drawable.boton2);
        }
        selec.setEnabled(sel);
        if(sel){
            selec.setBackgroundResource(R.drawable.boton);
        }else{
            selec.setBackgroundResource(R.drawable.boton2);
        }
    }

    /**
     * Método que se ejecuta cuando se pulsa el botón "atras" del dispositivo.
     */
    @Override
    public void onBackPressed() {
        //Si estamos grabamos paramos.
        if(audioRec!= null && grab==false) {
            try {
                audioRec.stop();
                audioRec.release();
            }catch(IllegalStateException ex){
                ex.printStackTrace();
            }catch(RuntimeException ex){
                ex.printStackTrace();
            }
        }

        //Si estamos reproduciendo un audio paramos.
        if(repAudio!=null) {
            if (repAudio.isPlaying()) {
                repAudio.stop();
            }
        }

        //Result intent con resultado cancelado.
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }

}

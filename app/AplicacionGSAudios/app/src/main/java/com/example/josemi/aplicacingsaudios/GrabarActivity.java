package com.example.josemi.aplicacingsaudios;

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

public class GrabarActivity extends AppCompatActivity {

    //Variables
    private Button play,stop,record, selec; //Botonoes para reproducir, parar, grabar y generar un nombre
    private MediaRecorder audioRec; //MediaRecorder que nos permite grabar
    private String ruta,formato,nf,audio; //String de la ruta a la carpeta donde se almacenarán los audios
    private MediaPlayer repAudio;
    private File aufile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //Lock de la pantalla en vertical

        ruta = getIntent().getExtras().getString("ruta");
        nf = getIntent().getExtras().getString("nombre");
        formato = getIntent().getExtras().getString("formato");

        //Inicializamos los botonoes y el EditText
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        record = findViewById(R.id.record);
        selec = findViewById(R.id.seleccionar);



        //Ponemos que no se puedan clickear los botones de stop y de play


        //Inicializamos el MediaRecorder
        audioRec = new MediaRecorder();
        setConfig(audioRec);

        audio = ruta + "/" + nf + formato;
        aufile = new File(audio);

        if(aufile.exists()){
            setVisibilidad(true,false,true,true);
        }else{
            setVisibilidad(true,false,false,false);
        }


        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibilidad(false,true,false,false);

                if(repAudio!=null) {
                    if (repAudio.isPlaying()) {
                        repAudio.stop();
                    }
                }

                //Si ya existe, es decir, es la segunda vez que grabamos borramos la grabación anterior
                if(aufile.exists()) {
                    aufile.delete();
                }

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

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos el MediaPlayer para reproducir el audio
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

        selec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repAudio!=null) {
                    if (repAudio.isPlaying()) {
                        repAudio.stop();
                    }
                }
                Toast.makeText(getApplicationContext(),"Pulsado el Botón de Seleccionar",Toast.LENGTH_LONG).show();

                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void setConfig(MediaRecorder audioRec){
        audioRec.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        audioRec.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        audioRec.setAudioEncodingBitRate(128000);
        audioRec.setAudioSamplingRate(48000);
    }

    private void setVisibilidad(boolean rec,boolean st, boolean pl, boolean sel){
        record.setEnabled(rec);
        stop.setEnabled(st);
        play.setEnabled(pl);
        selec.setEnabled(sel);
    }

}

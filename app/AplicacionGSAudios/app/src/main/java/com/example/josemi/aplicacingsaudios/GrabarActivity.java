package com.example.josemi.aplicacingsaudios;

import android.content.pm.ActivityInfo;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class GrabarActivity extends AppCompatActivity {

    //Variables
    private Button play,stop,record, selec; //Botonoes para reproducir, parar, grabar y generar un nombre
    private MediaRecorder audioRec; //MediaRecorder que nos permite grabar
    private String ruta,formato,paciente,outputFile; //String de la ruta a la carpeta donde se almacenarán los audios

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //Lock de la pantalla en vertical

        //Inicializamos los botonoes y el EditText
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        record = findViewById(R.id.record);
        selec = findViewById(R.id.seleccionar);
        paciente = "Paco"; //En el enlace de ventanas hay que pasarlo como argumento

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

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pulsado el Botón de Grabar",Toast.LENGTH_LONG).show();
                stop.setEnabled(true);
                record.setEnabled(false);
                play.setEnabled(false);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pulsado el Botón de Parar",Toast.LENGTH_LONG).show();
                record.setEnabled(true);
                play.setEnabled(true);
                stop.setEnabled(false);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pulsado el Botón de Reproducir",Toast.LENGTH_LONG).show();
            }
        });

        selec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pulsado el Botón de Seleccionar",Toast.LENGTH_LONG).show();
            }
        });
    }

}

package com.example.josemi.apace;


import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SearchEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.io.File;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

        private Button play,stop,record,generador;
        private MediaRecorder audioRec;
        private EditText outputFile;
        private String ruta;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

            play = findViewById(R.id.play);
            stop = findViewById(R.id.stop);
            record = findViewById(R.id.record);
            generador = findViewById(R.id.generador);
            outputFile = findViewById(R.id.ruta);
            ruta = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Apace";
            File dir = new File(ruta);

            stop.setEnabled(false);
            play.setEnabled(false);

            audioRec = new MediaRecorder();

            /*
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != -PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO},10);
            }
            while(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != -PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }*/

            if (askForPermissions()) {
                audioRec.setAudioSource(MediaRecorder.AudioSource.MIC);
                audioRec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                audioRec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            }

            if(!dir.exists()){
                    dir.mkdir();
            }
            record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            String audio = ruta + "/" +outputFile.getText().toString() +".3gp";
                            File aufile = new File(audio);
                            if(aufile.exists()) {
                                    Toast.makeText(getApplicationContext(), "El nombre del fichero ya está en uso", Toast.LENGTH_LONG).show();
                            }else if(outputFile.getText().toString().equals("")){
                                    Toast.makeText(getApplicationContext(), "Debe poner dirección de la ruta", Toast.LENGTH_LONG).show();
                            }else {
                                    try {
                                            audioRec.setOutputFile(audio);
                                            audioRec.prepare();
                                            audioRec.start();
                                    } catch (IOException e) {
                                            e.printStackTrace();
                                    }
                                    record.setEnabled(false);
                                    stop.setEnabled(true);
                                    outputFile.setEnabled(false);
                                    Toast.makeText(getApplicationContext(), "Grabación en curso", Toast.LENGTH_LONG).show();
                            }
                    }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                            audioRec.stop();
                            audioRec.release();
                            audioRec = new MediaRecorder();
                            audioRec.setAudioSource(MediaRecorder.AudioSource.MIC);
                            audioRec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            audioRec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            record.setEnabled(true);
                            play.setEnabled(true);
                            stop.setEnabled(false);
                            outputFile.setEnabled(true);
                            Toast.makeText(getApplicationContext(), "Audio grabado correctamente" , Toast.LENGTH_LONG).show();
                    }
            });

            play.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                            MediaPlayer repAudio = new MediaPlayer();
                            try {
                                    repAudio.setDataSource(ruta + "/" +outputFile.getText().toString() +".3gp");
                                    repAudio.prepare();
                                    repAudio.start();
                                    Toast.makeText(getApplicationContext(),"Reproduciendo el último audio grabado", Toast.LENGTH_LONG).show();
                            }catch(Exception e){
                                    e.printStackTrace();
                            }
                    }
            });

            generador.setOnClickListener((new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                            Calendar hoy = Calendar.getInstance();
                            Date dhoy = hoy.getTime();
                            SimpleDateFormat ff = new SimpleDateFormat("hh-mm-ss_dd-MM-yyyy");
                            String aux = ff.format(dhoy);
                            outputFile.setText(aux);
                    }
            }));
    }

    private Boolean askForPermissions(){
            String[] perm = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),perm[0]) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this.getApplicationContext(),perm[1]) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, perm,1);
                return true;
            }
            return true;
    }
}

/**
 * AVC ~ Asistente Virtual para la Comunicación
 *
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */

//Package.
package com.example.avc;

//Imports.
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Clase de la pantalla donde grabamos el audio de la interpretación.
 */
public class ResultadoActivity extends AppCompatActivity {

    //List con los ImageButton para poder recorrelo para cambiar la visibilidad.
    private List<ImageButton> lb;

    //ImageButton de grabar, parar, reproducir, información sobre grabar/parar/reproducir, interpretar y cancelar.
    private ImageButton grb,stp,play,infgrb,infstp,infplay,inter,canc;

    //String del paciente, la ruta, el formato del audio, ruta del audio y fecha actual.
    private String paciente,ruta,formato,aust,fecha;

    //TextView con el texto informativo de la pantalla.
    private TextView texto;

    //Tipo de interpretación true = estado y false = pregunta  y flag de la grabación.
    private boolean tipo, grab;

    //Chronometer para el tiempo grabado.
    private Chronometer crono;

    //MediaRecorder para grabar el audio.
    private MediaRecorder audio;

    //MediaPlayer para reproducir el audio.
    private MediaPlayer rpr;

    //Date con la fecha actual.
    private Date fechaD;

    //Activity para pasarle a los diálogos.
    private Activity yo;

    //File del audio.
    private File aufile;

    //AudioManager para comprobar el volumen multimedia.
    private AudioManager auman;

    //Conexión
    private ConnectivityManager conexion;

    /**
     * Método que se ejecuta al iniciar la Activity.
     * @param savedInstanceState Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Llamada al onCreate del padre.
        super.onCreate(savedInstanceState);

        //Set del layout.
        setContentView(R.layout.activity_resultado);

        //Obtenemos la conexión del dispositivo Android para poder comprobar sus estado.
        conexion = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        //Creamos la lista de ImageButton.
        lb = new ArrayList<>(4);

        //Inicializamos los ImageButtons y los metemos en la lista.
        grb = findViewById(R.id.bGrb);
        lb.add(grb);
        stp = findViewById(R.id.bParar);
        lb.add(stp);
        play = findViewById(R.id.bRpr);
        lb.add(play);
        infgrb = findViewById(R.id.bInfGrb);
        infstp = findViewById(R.id.bInfPr);
        infplay = findViewById(R.id.bInfRpr);
        inter = findViewById(R.id.bEnte);
        lb.add(inter);
        canc = findViewById(R.id.bCance);
        crono = findViewById(R.id.crono);

        //Inicializamos el primer estado de visibilidad.
        boolean[] a = {true,false,false,false};
        cambiarVisibilidad(a);

        //Inicializamos la variable con nuestra activity para los diálogos.
        yo = this;

        //Inicializamos la animación.
        final Animation animScale = AnimationUtils.loadAnimation(this,R.anim.anim_scale);

        //Recogemos el intent y sus parámetros.
        Intent miIntent = getIntent();
        //Paciente.
        paciente = miIntent.getStringExtra("paciente");
        //Ruta.
        ruta = miIntent.getStringExtra("ruta");
        //Tipo, true = estado y false = pregunta.
        tipo = miIntent.getBooleanExtra("tipo",true);

        //Formato.
        formato = ".mp4";

        //Inicializamos el AudioManager.
        auman = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //Inicializamos el audio.
        audio = new MediaRecorder();
        //Llamada al método para configurar el MediaRecorder.
        setConfig(audio);

        //Recogemos la fecha actual.
        Calendar hoy = Calendar.getInstance();

        //Pasamos el Calendar a Date para poder darle formato
        fechaD = hoy.getTime();

        //Creamos el formato que queremos, en nuestro caso con horas minutos segundos
        SimpleDateFormat ff = new SimpleDateFormat("hh-mm-ss_dd-MM-yyyy");

        //Creamos el string que será el nombre generado
        fecha= ff.format(fechaD);

        //Inicializamos el string con la ruta del audio y su file.
        aust = ruta + "/" + paciente + "_" + fecha + formato;
        aufile = new File(aust);

        //String auxiliar para poder crear el texto.
        String aux = "Grabe a " + paciente + " para poder interpretar su ";

        if(tipo){
            aux+="estado.";
        }else{
            aux+="respuesta.";
        }
        texto = findViewById(R.id.text);
        texto.setText(aux);

        grab = true;

        //Listener del ImageButton de grabar.
        grb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                grab = false;

                //Si el reproductor no es nulo y está reproduciendo lo paremos
                if(rpr!=null) {
                    if (rpr.isPlaying()) {
                        rpr.stop();
                    }
                }

                //Si el audio existe lo eliminamos.
                if(aufile.exists()) {
                    aufile.delete();
                }

                //Comenzamos a grabar
                try {
                    audio.setOutputFile(aust);
                    audio.prepare();
                    audio.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch(IllegalStateException ex){
                    ex.printStackTrace();
                }

                //Mensaje de que se está grabando
                Toast.makeText(getApplicationContext(), "Grabación en curso", Toast.LENGTH_LONG).show();

                //Empezamos a contar con el cronómetro.
                crono.setBase(SystemClock.elapsedRealtime());
                crono.start();

                //Cambiamos la visibilidad de los ImageButton.
                boolean[] a={false,true,false,false};
                cambiarVisibilidad(a);
            }
        });

        //Listener del ImageButton parar.
        stp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalStateException{

                //Si el crono marca más de un segundo.
                if((int) (SystemClock.elapsedRealtime() - crono.getBase()) > 1500) {

                    //Realizamos la animación.
                    v.startAnimation(animScale);

                    grab = true;

                    //Si el reproductor no es nulo y está reproduciendo lo paremos

                    if(rpr!=null) {
                        if (rpr.isPlaying()) {
                            rpr.stop();
                        }
                    }

                    //Paramos el crono.
                    crono.stop();

                    //Paramos la grabación
                    try {
                        audio.stop();
                        audio.release();
                        //La excepción salta cuando se llama al listener y se intenta parar desde espresso en el testing de la app.
                    }catch(IllegalStateException ex){
                        ex.printStackTrace();
                    }catch(RuntimeException ex){
                        ex.printStackTrace();
                    }
                    //Reinicializamos el MediaRecorder ya que no se puede reutilizar
                    audio = new MediaRecorder();

                    setConfig(audio);
                    //Mensaje de que el audio se ha grabado correctamente
                    Toast.makeText(getApplicationContext(), "Audio grabado correctamente", Toast.LENGTH_LONG).show();

                    //Cambiamos la visibilidad.
                    boolean[] a = {true, false, true, true};
                    cambiarVisibilidad(a);
                }else{
                    //Si paramos antes de 1 segundo en el crono no paramos y mostramos el siguiente mensaje.
                    Toast.makeText(getApplicationContext(), "El audio no puede ser tan corto.", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Listener del ImageButton de reproducir.
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);
                try {
                    //Reproducimos el audio de la ruta
                    rpr = new MediaPlayer();
                    rpr.setDataSource(ruta + "/" + paciente + "_" + fecha + formato);
                    rpr.prepare();
                    rpr.start();

                    //Comprobamos el volumen multimedia del dispositivo.
                    if(auman.getStreamVolume(AudioManager.STREAM_MUSIC)==0){
                        Toast.makeText(getApplicationContext(),"El volumen multimedia está muteado, si quiere escuchar la grabación suba el volumen.",Toast.LENGTH_LONG).show();
                    }

                    //Mensaje de que se está reproduciendo el audio
                    Toast.makeText(getApplicationContext(),"Reproduciendo el último audio grabado", Toast.LENGTH_LONG).show();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        //Listener del ImageButton cancelar.
        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Si no hemos parado de grabar paramos.
                if(audio!= null && grab==false) {
                    try {
                        audio.stop();
                        audio.release();
                    }catch(IllegalStateException ex){
                        ex.printStackTrace();
                    }catch(RuntimeException ex){
                        ex.printStackTrace();
                    }
                }

                //Result intent con resultado ok.
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        //Listener del ImageButton de información grabar.
        infgrb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el diálogo.
                AlertDialog.Builder infoBuilder = new AlertDialog.Builder(yo);
                final AlertDialog info = infoBuilder.create();
                infoBuilder.setTitle("Información botón Grabar");
                infoBuilder.setMessage("Primero tienes que grabar para poder entender lo que quiero decir. \n" +
                        "Haz clic en  “Grabar” para grabar el sonido que estoy haciendo. \n" +
                        "No tiene que haber ruidos a mi alrededor.\n" +
                        "Solo tienes que grabar el sonido que hago.\n" +
                        "Cuando termines de grabar pulsa la casilla parar.\n" +
                        "Si crees que se ha grabado mal, pulsa parar.");

                //Reproducimos el audio indicado.
                rpr = MediaPlayer.create(yo,R.raw.grabar);
                rpr.start();

                //Listener del botón aceptar del diálogo.
                infoBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Si estamos reproduciendo algo lo paramos.
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

        //Listener del ImageButton de información sobre parar.
        infstp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el diálogo.
                AlertDialog.Builder infoBuilder = new AlertDialog.Builder(yo);
                final AlertDialog info = infoBuilder.create();
                infoBuilder.setTitle("Información botón Parar");
                infoBuilder.setMessage("Haz clic en “Parar”cuando termines de grabar.\n" +
                        "Pulsa la casilla “Parar” cuando creas que hay un error en lo que estás grabado.\n" +
                        "Después pulsa la casilla “Escuchar”, para asegurarte que está bien grabado.");

                //Reproducimos el audio indicado.
                rpr = MediaPlayer.create(yo,R.raw.parar);
                rpr.start();

                //Listener del botón acpetar del diálogo.
                infoBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Si estamos reproduciendo paramos.
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

        //Listener del ImageButton de información de reproducir.
        infplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el diálogo.
                AlertDialog.Builder infoBuilder = new AlertDialog.Builder(yo);
                final AlertDialog info = infoBuilder.create();
                infoBuilder.setTitle("Información botón Escuchar");
                infoBuilder.setMessage("Haz clic en “Escuchar”, para  oír lo que has grabado. \n" +
                        "Si se ha grabado bien, haz clic en  “Entender” para saber lo que quiero decir. \n" +
                        "Si no se ha grabado bien, haz clic en “Grabar” para grabarme de nuevo.");

                //Reproducimos el audio.
                rpr = MediaPlayer.create(yo,R.raw.escuchar);
                rpr.start();

                //Listener del botón aceptar.
                infoBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Si estamos reproduciendo un audio lo paramos.
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

        //Listener del ImageButton de interpretar.
        inter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Comprobamos la conexión.
                if(conexion.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED || conexion.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //Creamos el nuevo intent y sus parámetros.
                    Intent miIntent = new Intent(yo, RFinalActivity.class);

                    //Paciente.
                    miIntent.putExtra("paciente", paciente);
                    //Tipo de interpretación.
                    miIntent.putExtra("tipo", tipo);
                    //String de la ruta al audio.
                    miIntent.putExtra("audio", aust);
                    //Iniciamos el activity.
                    startActivityForResult(miIntent,1);
                }else{
                    Toast.makeText(getApplicationContext(),"Tiene que estar conectado a Internet.",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * Método para cambiat la visibilidad de los botones.
     * @param bol Array booleano con la visibilidad de los botones.
     */
    private void cambiarVisibilidad(boolean[] bol){
        int j = 0;
        //Recorremos los ImageButtons.
        for(ImageButton i : lb){
            //Habilitamos según el booleano relacionado.
            i.setEnabled(bol[j]);
            //Cambiamos el background según el booleano relacionado.
            if(bol[j]){
                i.setBackgroundResource(R.drawable.boton);
            }else{
                i.setBackgroundResource(R.drawable.boton2);
            }
            j++;
        }
    }

    /**
     * Método para configurar el MediaRecorder.
     * @param audioRec MediaRecorder a configurar.
     */
    private void setConfig(MediaRecorder audioRec){
        audioRec.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        audioRec.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        audioRec.setAudioEncodingBitRate(128000);
        audioRec.setAudioSamplingRate(48000);
    }

    /**
     * Método que se ejecuta cuando se pulsa el botón "atras" del dispositivo.
     */
    @Override
    public void onBackPressed(){
        //Si estamos grabamos paramos.
        if(audio!= null && grab==false) {
            try {
                audio.stop();
                audio.release();
            }catch(IllegalStateException ex){
                ex.printStackTrace();
            }catch(RuntimeException ex){
                ex.printStackTrace();
            }
        }

        //Si estamos reproduciendo un audio paramos.
        if(rpr!=null) {
            if (rpr.isPlaying()) {
                rpr.stop();
            }
        }

        //Result intent con resultado ok.
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
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
        switch (requestCode) {
            case (1): {
                //Si el resultado ha sido OK
                if (resultCode == Activity.RESULT_OK) {
                    //Finalizamos la activity si el resultado ha sido correcto.
                    finish();
                }
                break;
            }
        }
    }
}

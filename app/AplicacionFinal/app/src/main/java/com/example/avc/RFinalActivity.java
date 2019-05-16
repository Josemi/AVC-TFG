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
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Clase con la pantalla donde se envía y recibe el audio y su resultado y se muestra este.
 */
public class RFinalActivity extends AppCompatActivity {

    //ImageButton de acpetar y reproducir.
    private ImageButton acp,rp;

    //ImageView del resultado (Máximo de 5 ).
    private ImageView im1,im2,im3,im4,im5;

    //TextView de los resultados.
    private TextView texto,r1,r2,r3,r4,r5;

    //Lista de ImageView.
    private List<ImageView> lim;

    //Lista de TextView.
    private List<TextView> lres;

    //Strinf del paciente y la ruta del audio.
    private String paciente,audio;

    //Boolean con el tipo de interpretación true = estado, false = pregunta.
    private boolean tipo;

    //MediaPlayer para reproducir los audios.
    private MediaPlayer rpr;

    //File con el audio.
    private File aufile;

    //AudioManager para comprobar el volumen multimedia.
    private AudioManager auman;

    /**
     * Método que se ejecuta al crear el activity.
     * @param savedInstanceState Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Llamada al onCreate del padre.
        super.onCreate(savedInstanceState);

        //Set del layout.
        setContentView(R.layout.activity_rfinal);

        //Recogemos el intent y sus parámetros.
        Intent miIntent = getIntent();
        //Paciente.
        paciente = miIntent.getStringExtra("paciente");
        //Tipo.
        tipo = miIntent.getBooleanExtra("ruta",true);
        //Ruta del audio.
        audio = miIntent.getStringExtra("audio");

        //Inicializamos el file del audio.
        aufile = new File(audio);

        //Inicializamos el AudioManager.
        auman = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //Inicializamos la animación.
        final Animation animScale = AnimationUtils.loadAnimation(this,R.anim.anim_scale);

        //Inicializamos los ImageButtons.
        acp = findViewById(R.id.bAcept);
        rp = findViewById(R.id.brepro);

        //Creamos las listas de ImageView y de TextView.
        lim = new ArrayList<>(5);
        lres = new ArrayList<>(5);

        //Inicializamos los ImageView y los metemos en la lista.
        im1 = findViewById(R.id.im1);
        lim.add(im1);
        im2 = findViewById(R.id.im2);
        lim.add(im2);
        im3 = findViewById(R.id.im3);
        lim.add(im3);
        im4 = findViewById(R.id.im4);
        lim.add(im4);
        im5 = findViewById(R.id.im5);
        lim.add(im5);

        //Inicializamos los textos y los metemos en la lista.
        texto = findViewById(R.id.ttexto);
        r1 = findViewById(R.id.r1);
        lres.add(r1);
        r2 = findViewById(R.id.r2);
        lres.add(r2);
        r3 = findViewById(R.id.r3);
        lres.add(r3);
        r4 = findViewById(R.id.r4);
        lres.add(r4);
        r5 = findViewById(R.id.r5);
        lres.add(r5);

        //Texto.
        String aux = "Resultado de la grabación de " + paciente + " sobre su ";

        if(tipo){
            aux+="estado.";
        }else{
            aux+="respuesta.";
        }
        texto.setText(aux);

        //Método que envía, recoge y muestra el resultado.
        resolver();

        //Listener del ImageButton de aceptar.
        acp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Si estamos reproduciendo paramos.
                if(rpr!=null) {
                    if (rpr.isPlaying()) {
                        rpr.stop();
                    }
                }

                //Eliminamos el audio.
                aufile.delete();
                finish();
            }
        });

        //Listener del ImageButton de reproducir.
        rp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Si estamos reproduciendo paramos.
                if(rpr!=null) {
                    if (rpr.isPlaying()) {
                        rpr.stop();
                    }
                }
                try {
                    //Reproducimos el audio de la ruta
                    rpr = new MediaPlayer();
                    rpr.setDataSource(audio);
                    rpr.prepare();
                    rpr.start();

                    //Comprobamos el volumen multimedia del dispositivo.
                    if(auman.getStreamVolume(AudioManager.STREAM_MUSIC)==0){
                        Toast.makeText(getApplicationContext(),"El volumen multimedia está muteado, si quiere escuchar la grabación suba el volumen.",Toast.LENGTH_LONG).show();
                    }

                    //Mensaje de que se está reproduciendo el audio
                    Toast.makeText(getApplicationContext(),"Reproduciendo el audio relacionado con el resultado.", Toast.LENGTH_LONG).show();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Método que envía el audio, recibe y muestra el resultado.
     */
    private void resolver() {
        //Hacemos el post.
        PostClasifica p = new PostClasifica(getApplicationContext(),"http://192.168.137.1:5000/Clasifica",paciente,tipo,"1");
        p.execute();
        List<String> res = null;
        try {
            //Obtenemos el resultado.
            res = p.get();
        }catch(InterruptedException ex){
            ex.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Si está vacío, error del server se finaliza el Activity y se vuelve a ResultadoActivity con el audio ya cargado.
        if(res.isEmpty()){
            Toast.makeText(getApplicationContext(),"No se pudo conectar con el servidor",Toast.LENGTH_LONG).show();
            finish();
        }else {
            //Creamos un nuevo intent para poder enviar el resultado de la pantalla
            Intent resultIntent = new Intent();
            //Ponemos como resultado OK
            setResult(Activity.RESULT_OK, resultIntent);
            res.remove(0);
            res.remove(res.size() - 1);

            //Tratamos la salida.
            for (int i = 0; i < res.size(); i++) {
                res.set(i, res.get(i).replaceAll("\"", ""));
                res.set(i, res.get(i).replaceAll(",", ""));
                res.set(i, res.get(i).replaceAll(" ",""));
            }
        }

        //Lo transformamos en un HashMap.
        HashMap<String,String> resmap = listAMap(res);

        //Ponemos las imagenes y los textos correspondientes al resultado.
        int j =0;
        for(String i: resmap.keySet()){
            try {
                Field idField = R.drawable.class.getDeclaredField(i);
                lim.get(j).setImageResource(idField.getInt(idField));
                lres.get(j).setText(resmap.get(i) + "%");
            }catch (Exception e) {
                e.printStackTrace();
            }
            j++;
        }

        //El resto ponemos invisibles.
        for(;j<lim.size();j++){
            lim.get(j).setVisibility(View.INVISIBLE);
            lres.get(j).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Método para transformar la lista en HashMap.
     * @param res Lista resultado.
     * @return HashMap con el resultado.
     */
    private HashMap<String,String> listAMap(List<String> res){
        HashMap<String,String> mapa = new HashMap<>();
        for(String i: res){
            String[] val = i.split(":");
            mapa.put(val[0],val[1]);
        }
        return mapa;
    }
}

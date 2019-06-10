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
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;

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

    //Strinf del paciente y la ruta del audio, link al servidor,token de seguridad.
    private String paciente,audio,link,token;

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
        tipo = miIntent.getExtras().getBoolean("tipo");
        //Ruta del audio.
        audio = miIntent.getStringExtra("audio");
        //Link al servidor
        link = miIntent.getStringExtra("link");
        //Token de seguridad.
        token = miIntent.getStringExtra("token");

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
        String baudio=null;

        try {
            byte[] b = new byte[(int) aufile.length()];
            FileInputStream fis = new FileInputStream(aufile);
            fis.read(b);
            baudio = Base64.encodeToString(b, Base64.NO_WRAP);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Hacemos el post.
        PostClasifica p = new PostClasifica(link+"/Clasifica",paciente,tipo,baudio,this,token);
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
        if(res!=null){
            //Si está vacío, error del server se finaliza el Activity y se vuelve a ResultadoActivity con el audio ya cargado.
            if(res.isEmpty() || res.size()<=1){
                Toast.makeText(getApplicationContext(),"ERROR Er9:\nResultado devuelto por el servidor vacío.",Toast.LENGTH_LONG).show();
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
                    res.set(i, res.get(i).replaceAll(" ", ""));
                }

                //Recogemos el valor con más porcentaje, que es el primero en la lista.
                String may = res.get(0).split(":")[0];

                //Si estamos reproduciendo paramos.
                if(rpr!=null) {
                    if (rpr.isPlaying()) {
                        rpr.stop();
                    }
                }

                //Reproducimos el audio del estado-respuesta con mayor porcentaje.
                try {
                    Field idField = R.raw.class.getDeclaredField(may);
                    //Reproducimos el audio indicado.
                    rpr = MediaPlayer.create(this,idField.getInt(idField));
                    rpr.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Lo transformamos en un dos listas.
                List<List<String>>  lista = listALists(res);

                //Ponemos las imagenes y los textos correspondientes al resultado.
                int j = 0;
                for (String i : lista.get(0)) {
                    try {
                        Field idField = R.drawable.class.getDeclaredField(i);
                        lim.get(j).setImageResource(idField.getInt(idField));
                        lres.get(j).setText(lista.get(1).get(j) + "%");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    j++;
                }

                //El resto ponemos invisibles.
                for (; j < lim.size(); j++) {
                    lim.get(j).setVisibility(View.INVISIBLE);
                    lres.get(j).setVisibility(View.INVISIBLE);
                }
            }
        }else{
            finish();
        }
    }

    /**
     * Método para transformar la lista en dos listas, una con los estados o respuestas y otra con los porcentajes.
     * @param res Lista resultado.
     * @return HashMap con el resultado.
     */
    private List<List<String>> listALists(List<String> res){
        List<List<String>> lista = new ArrayList<>(2);
        lista.add(new LinkedList<String>());
        lista.add(new LinkedList<String>());
        for(String i: res){
            String[] val = i.split(":");
            lista.get(0).add(val[0]);
            lista.get(1).add(val[1]);
        }
        return lista;
    }
}

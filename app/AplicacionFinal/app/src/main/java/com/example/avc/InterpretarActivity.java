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
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Clase de la pantalla de selección de la interpretación que se quiere hacer.
 */
public class InterpretarActivity extends AppCompatActivity {

    //ImageButton de estado, pregunta, información del estado, información de pregunta y cancelar.
    private ImageButton est,pre,infest,infpre,canc;

    //String con el identificador del paciente y de la ruta absoluta a la carpeta de trabajo.
    private String paciente,ruta;

    //Texto de explicación.
    private TextView texto;

    //Activity para usarlo en los diálogos.
    private Activity yo;

    //MediaPlayer que nos permite reproducir los diálogos.
    private MediaPlayer rpr;

    /**
     * Método que se ejecuta en la creación del Activity.
     * @param savedInstanceState Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Llamada al creador del padre.
        super.onCreate(savedInstanceState);

        //Seleción del layout con el que se relaciona el activity.
        setContentView(R.layout.activity_interpretar);

        //Inicializamos los ImageButtons.
        est = findViewById(R.id.bEstado);
        pre = findViewById(R.id.bPregunta);
        infest = findViewById(R.id.bInfoEstado);
        infpre = findViewById(R.id.bInfoPregunta);
        canc = findViewById(R.id.bCancelar);

        //Inicializamos el TextView,
        texto = findViewById(R.id.texto);

        //Ponemos el activity como nuestro activity.
        yo = this;

        //Recogemos el intent y sus parámetros.
        Intent miIntent = getIntent();
        paciente = miIntent.getStringExtra("paciente");
        ruta = miIntent.getStringExtra("ruta");

        //Ponemos el texto.
        texto.setText("Tipo de interpretación para " + paciente);

        //Inicializamos la animación.
        final Animation animScale = AnimationUtils.loadAnimation(this,R.anim.anim_scale);

        //Listener del ImageButton estado.
        est.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el nuevo intent.
                Intent miIntent = new Intent(yo,ResultadoActivity.class);
                //Pasamos los parámetros identificador del paciente, ruta y el tipo de interpretación (true = estado; false = pregunta).
                miIntent.putExtra("paciente",paciente);
                miIntent.putExtra("ruta",ruta);
                miIntent.putExtra("tipo",true);

                //Comenzamos el activity con Result para saber como acaba la activity.
                startActivityForResult(miIntent,1);
            }
        });

        //Listener del ImageButton Pregunta.
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el nuevo intent.
                Intent miIntent = new Intent(yo,ResultadoActivity.class);
                //Pasamos los parámetros identificador del paciente, ruta y el tipo de interpretación (true = estado; false = pregunta).
                miIntent.putExtra("paciente",paciente);
                miIntent.putExtra("ruta",ruta);
                miIntent.putExtra("tipo",false);

                //Comenzamos el activity con Result para saber como acaba la activity.
                startActivityForResult(miIntent,1);
            }
        });

        //Listener del ImageButton información de estado.
        infest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el diálogo.
                AlertDialog.Builder infoBuilder = new AlertDialog.Builder(yo);
                final AlertDialog info = infoBuilder.create();
                infoBuilder.setTitle("Información botón Estado");
                infoBuilder.setMessage("Botón que nos redirige a la pantalla para interpretar y entender un estado emocional.");

                //Reproducimos el audio indicado.
                rpr = MediaPlayer.create(yo,R.raw.informacion);
                rpr.start();

                //Listener del botón de aceptar del diálogo.
                infoBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Si estamos reproduciendo algo lo parammos.
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
            }
        });

        //Listener del ImageButton de información de pregunta.
        infpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Creamos el diálogo.
                AlertDialog.Builder infoBuilder = new AlertDialog.Builder(yo);
                final AlertDialog info = infoBuilder.create();
                infoBuilder.setTitle("Información botón Respuesta");
                infoBuilder.setMessage("Botón que nos redirige a la pantalla para interpretar y enteder una respuesta (SI/NO).");

                //Reproducimos el audio indicado.
                rpr = MediaPlayer.create(yo,R.raw.informacion);
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
            }
        });

        //Listener del ImageButton cancelar.
        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Terminamos el activity.
                finish();
            }
        });
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
                    //Nos quedamos en la pantalla de selección de interpretación.
                }else{
                    finish();
                }
                break;
            }
        }
    }
}

/**
 * AVC ~ Asistente Virtual para la Comunicación
 *
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */

//Package
package com.example.avc;

//Imports
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

/**
 * Clase de la pantalla de información sobre el proyecto.
 */
public class InfoActivity extends AppCompatActivity {

    //ImageButton del botón aceptar.
    private ImageButton acep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Llamada al constructor.
        super.onCreate(savedInstanceState);

        //Seleción del layout con el que se relaciona el activity.
        setContentView(R.layout.activity_info);

        //Inicializamos el ImageButton
        acep = findViewById(R.id.binfoacep);

        //Creamos la animación para los ImageButtons.
        final Animation animScale = AnimationUtils.loadAnimation(this,R.anim.anim_scale);

        //Listener del ImageButton entender
        acep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Finalizamos la activity.
                finish();
            }
        });
    }
}

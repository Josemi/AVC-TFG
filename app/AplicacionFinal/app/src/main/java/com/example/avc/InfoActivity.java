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
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Clase de la pantalla de información sobre el proyecto.
 */
public class InfoActivity extends AppCompatActivity {

    //ImageButton del botón aceptar.
    private ImageButton acep;

    //TextView con el texto.
    private TextView t;

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

        //Ponemos el texto
        t = findViewById(R.id.tprv);

        t.setText(Html.fromHtml("<b>Universidad de Burgos:</b><br>- Dr. César Represa Pérez.<br>- Dr. José Francisco Díez Pastor.<br>- Sergio Chico Carrancio.<br>- José Miguel Ramírez Sanz.<br><b>APACE Burgos:</b>" +
                "<br>- Rut Prieto Bayón.<br>- Rocío Cerrejón Martín.<br>- Victoria Gancedo Almansa.<br>- Roberto Pérez Porras.<br><b>Universidad de Salamanca:</b><br>- Pilar Porras Navalon.<br>- Nati Rodríguez Marcos." +
                "<br>- David Fernández Rodríguez."));

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

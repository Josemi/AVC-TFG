/**
 * AVC ~ Asistente Virtual para la Comunicación
 *
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */

//Package.
package com.example.avc;

//Imports.
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Clase de la pantalla que nos permite ver y modificar las opciones adicionales relacionadas con un paciente.
 */
public class OpcionesActivity extends AppCompatActivity {

    //ImageButton para guardar y cancelar.
    private ImageButton gua, canc;

    //Lista con los CheckBoxes de las opciones.
    private List<CheckBox> lch;

    //Lsita con los Spinners de las opciones.
    private List<Spinner> lsp;

    //String del paciente.
    private String paciente;

    //TextView con el texto de explicación de la pantalla.
    private TextView texto;

    //boolean para saber si se ha modificado algún valor.
    private boolean cambio=false;

    //Conexión
    private ConnectivityManager conexion;

    /**
     *  Método que se ejecuta en la creación del Activity
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Llamada al constructor.
        super.onCreate(savedInstanceState);

        //Seleción del layout con el que se relaciona el activity.
        setContentView(R.layout.activity_opciones);

        //Inicializamos el ImageButton y lo ponemos a deshabilitado.
        gua = findViewById(R.id.bGuardar);
        gua.setEnabled(false);

        //Inicializamos el ImageButton de cancelar.
        canc = findViewById(R.id.bC);

        //Recogemos el Intent y sus parámetros.
        Intent miIntent = getIntent();
        paciente = miIntent.getStringExtra("paciente");

        //Obtenemos la conexión del dispositivo Android para poder comprobar sus estado.
        conexion = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        //Inicializamos y ponemos el texto
        texto = findViewById(R.id.tttexto);
        texto.setText("Opciones almacenadas para " + paciente);

        //Creamos la animación de los ImageButton.
        final Animation animScale = AnimationUtils.loadAnimation(this,R.anim.anim_scale);

        //Cargamos el csv desde el servidor y miramos si ha salido bien.
        if(cargarCSV()){

        }else{
            Toast.makeText(getApplicationContext(), "Error al cargar los valores guardados.", Toast.LENGTH_LONG).show();
            finish();
        }

        //Listener del ImageButton guardar
        gua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación
                v.startAnimation(animScale);

                //Si el flag con el cambio de alguna de las opciones es true.
                if(cambio){
                    //Guardamos el csv en el servidor si sale bien acabamos el activity sino mensaje de error.
                    if(guardarCSV()){
                        Toast.makeText(getApplicationContext(), "Se han guardado correctamente los valores.", Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Error al guardar los valores en el servidor.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //Listener del ImageButton cancelar
        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación.
                v.startAnimation(animScale);

                //Acabamos la activity.
                finish();
            }
        });

    }

    /**
     * Carga el csv con las opciones guardadas para el paciente seleccionado en MainAcitivty.
     * @return true si se ha podido cargar.
     */
    private boolean cargarCSV(){
        //Comprobamos la conexión.
        if(conexion.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED || conexion.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }else{
            return false;
        }
    }

    /**
     * Guarda en el servidor el csv con las opciones.
     * @return
     */
    private boolean guardarCSV(){
        //Comprobamos la conexión.
        if(conexion.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED || conexion.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }else{
            return false;
        }
    }
}

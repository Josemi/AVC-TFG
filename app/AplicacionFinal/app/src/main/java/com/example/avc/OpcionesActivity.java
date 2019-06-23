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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.post.PostGuaOpciones;
import com.example.post.PostObtOpciones;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Clase de la pantalla que nos permite ver y modificar las opciones adicionales relacionadas con un paciente.
 */
public class OpcionesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //ImageButton para guardar y cancelar.
    private ImageButton gua, canc;

    //Lista con los CheckBoxes de las opciones.
    private List<CheckBox> lch;

    //Checkbox con las opciones.
    private CheckBox c1,c2,c3,c4,c5,c6,c7;

    //Lsita con los Spinners de las opciones.
    private List<Spinner> lsp;

    //SPinners con el resto de opciones.
    private Spinner s1,s2;

    //String del paciente, link del servidor,token de seguridad.
    private String paciente,link,token;

    //TextView con el texto de explicación de la pantalla.
    private TextView texto;

    //int para saber si se ha modificado algún valor.
    private int cambio;

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

        //Inicializamos las dos listas.
        lch = new ArrayList<>(7);
        lsp= new ArrayList<>(2);

        //Inicializamos el ImageButton de cancelar.
        canc = findViewById(R.id.bC);

        //Recogemos el Intent y sus parámetros.
        Intent miIntent = getIntent();
        paciente = miIntent.getStringExtra("paciente");
        link = miIntent.getStringExtra("link");
        token = miIntent.getStringExtra("token");

        //Obtenemos la conexión del dispositivo Android para poder comprobar sus estado.
        conexion = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        //Inicializamos y ponemos el texto
        texto = findViewById(R.id.tttexto);
        texto.setText("Opciones almacenadas para " + paciente);

        //Inicializamos y metemos en la lista a los checkbox y spinners
        c1 = findViewById(R.id.c1);
        lch.add(c1);
        c2 = findViewById(R.id.c2);
        lch.add(c2);
        c3 = findViewById(R.id.c3);
        lch.add(c3);
        c4 = findViewById(R.id.c4);
        lch.add(c4);
        c5 = findViewById(R.id.c5);
        lch.add(c5);
        c6 = findViewById(R.id.c6);
        lch.add(c6);
        c7 = findViewById(R.id.c7);
        lch.add(c7);

        s1 = findViewById(R.id.s1);
        lsp.add(s1);
        s2 = findViewById(R.id.s2);
        lsp.add(s2);

        //Inicialización de los spinners
        s1.setSelection(-1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.s1,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(this);

        s2.setSelection(-1);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.s2,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(adapter2);
        s2.setOnItemSelectedListener(this);

        //Creamos la animación de los ImageButton.
        final Animation animScale = AnimationUtils.loadAnimation(this,R.anim.anim_scale);

        //Cargamos el csv desde el servidor y miramos si ha salido bien.
        if(!cargarServer()){
            finish();
        }

        //Listener del ImageButton guardar
        gua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realizamos la animación
                v.startAnimation(animScale);

                //Si el flag con el cambio de alguna de las opciones es true.
                if(cambio>1){
                    //Guardamos el csv en el servidor si sale bien acabamos el activity sino mensaje de error.
                    if(guardarServer()){
                        Toast.makeText(getApplicationContext(), "Se han guardado correctamente los valores.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });

        //Listeners de los checkbox
        for(int i = 0; i < lch.size(); i++) {
            lch.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gua.setEnabled(true);
                    gua.setBackgroundResource(R.drawable.boton);
                    cambio++;
                }
            });
        }

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
     * Método que se ejecuta al seleccionar un item de un spinner
     * @param parent Adaptador
     * @param view Vista
     * @param position Posición seleccionada
     * @param id Id del spinner
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(cambio>1) {
            gua.setEnabled(true);
            gua.setBackgroundResource(R.drawable.boton);
        }else {
            cambio++;
        }
    }

    /**
     * Método que se ejecuta cuando no se ha seleccionado un iten en un spinner
     * @param parent Adaptador
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Carga el csv con las opciones guardadas para el paciente seleccionado en MainAcitivty.
     * @return true si se ha podido cargar.
     */
    private boolean cargarServer(){
        //Comprobamos la conexión.
        if(conexion.getActiveNetworkInfo()==null) {
            Toast.makeText(getApplicationContext(), "ERROR Er6:\nNecesita conexión a Internet para usar la aplicación.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(conexion.getActiveNetworkInfo().isConnected()){
            //Hacemos el post
            PostObtOpciones p = new PostObtOpciones(link + "/ObtOpciones",paciente,this,token);
            p.execute();
            List<String> res = null;
            try {
                //Obtenemos el resultado
                res = p.get();
            }catch(InterruptedException ex){
                ex.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if(res!=null){
                //Si está vacío es error del servidor, se finaliza el Activity y se vuelve al menú principal, se devuelve false.
                if(res.isEmpty()){
                    Toast.makeText(getApplicationContext(),"ERROR Er7:\nFichero con las opciones del paciente " + paciente + "vacío, contacte con el Administrador.",Toast.LENGTH_LONG).show();
                    finish();
                    return false;
                //Sino se trata el resultado y se cargan las opciones y se devuelve true.
                }else {
                    res.remove(0);
                    res.remove(res.size() - 1);
                    for (int i = 0; i < res.size(); i++) {
                        res.set(i, res.get(i).replaceAll("\"", ""));
                        res.set(i, res.get(i).replaceAll(",", ""));
                        res.set(i, res.get(i).replaceAll(" ", ""));
                    }
                }

                //Cambiamos las opciones con los valores de la lista.
                cambiarOpciones(res);
                Toast.makeText(getApplicationContext(),"Valores de " + paciente + " cargados con éxito.",Toast.LENGTH_LONG).show();
                return true;
            }else{
                return false;
            }
        }else{
            //Si no hay conexión devolvemos false.
            Toast.makeText(getApplicationContext(), "ERROR Er6:\nNecesita conexión a Internet para usar la aplicación.", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * Guarda en el servidor el csv con las opciones.
     * @return
     */
    private boolean guardarServer(){
        if(conexion.getActiveNetworkInfo()==null) {
            Toast.makeText(getApplicationContext(), "ERROR Er6:\nNecesita conexión a Internet para usar la aplicación.", Toast.LENGTH_LONG).show();
            return false;
        }
        //Comprobamos la conexión.
        if(conexion.getActiveNetworkInfo().isConnected()){
            //Obtenemos las opciones a guardar.
            List<String> valores = obtenerOpciones();

            //Hacemos el post
            PostGuaOpciones p = new PostGuaOpciones(link + "/GuaOpciones",paciente,valores,this,token);
            p.execute();
            Boolean res = null;
            try {
                //Obtenemos el resultado.
                res = p.get();
            }catch(InterruptedException ex){
                ex.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            //Retornamos el resultado, true si se ha guardado false si no se ha guardado.
            return res;
        }else{
            //Si no hay conexión devolvemos false.
            Toast.makeText(getApplicationContext(), "ERROR Er6:\nNecesita conexión a Internet para usar la aplicación.", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * Método que nos permite cambiar las opciones de los checkboxs y los spinners.
     * @param opciones lista con las opciones a cargar.
     */
    private void cambiarOpciones(List<String> opciones){
        int i = 0;
        //Cargamos los checkboxs.
        for(;i<lch.size();i++){
            if(opciones.get(i).equals("true")){
                lch.get(i).setChecked(true);
            }else{
                lch.get(i).setChecked(false);
            }
        }
        //Cargamos los spinners.
        for(int j=0;j<lsp.size();j++){
            ArrayAdapter<CharSequence> ad = (ArrayAdapter<CharSequence>) lsp.get(j).getAdapter();
            lsp.get(j).setSelection(ad.getPosition(opciones.get(i)));
            i++;
        }
    }

    /**
     * Método que obtiene las opciones de los checkboxs y los spinners.
     * @return lista con los valoresd de las opciones.
     */
    private List<String> obtenerOpciones(){
        //Lsita de las opciones.
        List<String> valores = new ArrayList<>(lsp.size()+lch.size());
        //Guardamos los valores de los checkboxs.
        for(int i = 0;i<lch.size();i++){
            valores.add(Boolean.toString(lch.get(i).isChecked()));
        }
        //Guardamos los valores de los spinners.
        for(int j=0;j<lsp.size();j++){
            valores.add(lsp.get(j).getSelectedItem().toString());
        }
        return valores;
    }
}

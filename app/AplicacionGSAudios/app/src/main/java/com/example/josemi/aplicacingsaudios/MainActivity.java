package com.example.josemi.aplicacingsaudios;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button grabar,opciones,estado,enviar;
    private Spinner sp;
    private String paciente,ruta,rutac,nf,formato;
    private File dir,dirc,audio,opc,est;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //Lock de la pantalla en vertical

        askForPermissions();

        sp = findViewById(R.id.paciente);
        sp.setSelection(-1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.pac,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);
        paciente = sp.getSelectedItem().toString();

        ruta = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Apace";
        dir = new File(ruta);
        //Si la carpeta de la ruta no existe la creamos
        if(!dir.exists()){
            dir.mkdir();
        }

        grabar = findViewById(R.id.grabar);
        grabar.setEnabled(false);
        opciones = findViewById(R.id.opciones);
        opciones.setEnabled(false);
        estado = findViewById(R.id.estados);
        estado.setEnabled(false);
        enviar = findViewById(R.id.enviar);
        enviar.setEnabled(false);

        crearCarpeta();

        formato = ".mp4";


        grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GrabarActivity.class);
                intent.putExtra("ruta",rutac);
                intent.putExtra("nombre",nf);
                intent.putExtra("formato",formato);
                audio = new File(rutac+"/"+nf+formato);
                startActivityForResult(intent,1);
            }
        });

        opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,OpcionesActivity.class);
                intent.putExtra("paciente", paciente);
                intent.putExtra("ruta",rutac);
                intent.putExtra("nombre",nf);
                opc=new File(rutac + "/" + nf + "_Opciones" + ".csv");
                startActivityForResult(intent,2);
            }
        });

        estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EstadoActivity.class);
                intent.putExtra("paciente", paciente);
                intent.putExtra("ruta",rutac);
                intent.putExtra("nombre",nf);
                est=new File(rutac + "/" + nf + "_Estado" + ".csv");
                startActivityForResult(intent,3);
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pulsado Botón de Enviar",Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Método para pedir los permisos.
     */
    private void askForPermissions() {
        //Permisos que queremos pedir
        String[] perm = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Si alguno no está dado los pedimos
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),perm[0]) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this.getApplicationContext(),perm[1]) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(perm,1234);
        }
    }

    /**
     * Método que se ejecuta cuando se selecciona un elemento en el Spinner.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        paciente = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),"El paciente seleccionado es: " + paciente,Toast.LENGTH_LONG).show();
        grabar.setEnabled(true);
    }

    /**
     * Método que se ejecuta cuando no se selecciona un elemento en el Spinner.
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void crearCarpeta(){
        Date ahora = new Date();
        SimpleDateFormat ff = new SimpleDateFormat("hh-mm-ss_dd-MM-yyyy");
        nf = paciente + "_" + ff.format(ahora);
        rutac = ruta + "/" + nf;
        dirc = new File(rutac);
        //Si la carpeta de la ruta no existe la creamos, no debería existir, pero por si acaso mejor ponerlo.
        if(!dirc.exists()){
            dirc.mkdir();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    opciones.setEnabled(true);
                }
                break;
            }
            case (2): {
                if (resultCode == Activity.RESULT_OK) {
                    estado.setEnabled(true);
                }
                break;
            }
            case (3):{
                if (resultCode == Activity.RESULT_OK) {
                    enviar.setEnabled(true);
                }
                break;
            }
        }
    }


}

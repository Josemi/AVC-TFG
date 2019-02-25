package com.example.josemi.aplicacingsaudios;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button grabar,opciones,estado,enviar;
    private Spinner sp;
    private String paciente;

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

        grabar = findViewById(R.id.grabar);
        grabar.setEnabled(false);
        opciones = findViewById(R.id.opciones);
        opciones.setEnabled(false);
        estado = findViewById(R.id.estados);
        estado.setEnabled(false);
        enviar = findViewById(R.id.enviar);
        enviar.setEnabled(false);

        grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pulsado Botón de Grabar",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this,GrabarActivity.class);
                intent.putExtra("paciente", paciente);
                startActivity(intent);
                opciones.setEnabled(true);
            }
        });

        opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pulsado Botón de Opciones",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this,OpcionesActivity.class);
                intent.putExtra("paciente", paciente);
                startActivity(intent);
                estado.setEnabled(true);
            }
        });

        estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pulsado Botón de Estado/s",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this,EstadoActivity.class);
                intent.putExtra("paciente", paciente);
                startActivity(intent);
                enviar.setEnabled(true);
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


}

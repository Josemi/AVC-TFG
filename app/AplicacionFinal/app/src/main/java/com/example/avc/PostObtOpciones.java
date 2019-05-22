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
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase para el post que obtiene las opciones de un paciente.
 */
public class PostObtOpciones extends AsyncTask<Void,Void, List<String>> {
    //Lista resultado.
    public List<String> resultadoapi;

    //Link del servidor.
    public String linkrequestAPI="";

    //Paciente seleccionado.
    private String paciente;

    //Contexto para hacer los toast.
    private Context con;

    //Código resultado.
    private int responsecode;

    //Token de seguridad.
    private String token;

    /**
     * Constructor de la clase del post.
     * @param link link del servidor.
     * @param paciente paciente seleccionado.
     * @param con context para los toast
     * @param token token de seguridad.
     */
    public PostObtOpciones(String link,String paciente,Context con,String token){
        resultadoapi = new LinkedList<>();
        this.linkrequestAPI=link;
        this.paciente=paciente;
        this.token = token;
        this.con = con;
    }

    /**
     * Método que se ejecuta antes del execute.
     */
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    /**
     * Método que se ejecuta después del execute que devuelve el resultado.
     * @param s lista con el resultado.
     */
    @Override
    protected void onPostExecute(List<String> s){
        super.onPostExecute(s);
        resultadoapi=s;
        if(responsecode==HttpURLConnection.HTTP_FORBIDDEN) {
            Toast.makeText(con, "ERROR Er2:\nToken de seguridad incorrecto, contacte con el Administrador.", Toast.LENGTH_LONG).show();
        }else if(responsecode==HttpURLConnection.HTTP_INTERNAL_ERROR){
            Toast.makeText(con, "ERROR Er5:\nEl fichero de opciones de " + paciente + " no está, avise al Administrador.", Toast.LENGTH_LONG).show();
        }else if(responsecode!=HttpURLConnection.HTTP_OK){
            Toast.makeText(con, "ERROR Er1:\nNo se pudo conectar con el servidor.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Método que se ejcuta en segundo plano y obtiene el resultado.
     * @param voids
     * @return lista con las opciones.
     */
    @Override
    protected List<String> doInBackground(Void... voids) {
        List<String> result = new LinkedList<>();
        String wsURL=linkrequestAPI;
        try {
            //Conexión.
            URL url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //Settings de la conexión.
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(5000);

            //Parámetros.
            OutputStream dos = urlConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(dos,"UTF-8"));
            bw.write("paciente="+paciente+"&token="+token);
            bw.flush();
            bw.close();
            dos.close();

            //Respuesta.
            responsecode= urlConnection.getResponseCode();
            if(responsecode==HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String linea;
                while((linea=in.readLine())!=null){
                    result.add(linea);
                }
                in.close();
            }else{
                result = null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

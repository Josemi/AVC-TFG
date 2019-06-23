/**
 * AVC ~ Asistente Virtual para la Comunicación
 *
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */

//Package
package com.example.post;

//Imports
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
 * Clase que realiza la comunicación post con el servidor para obtener el resultado.
 */
public class PostClasifica extends AsyncTask<Void,Void, List<String>> {
    //Resultado.
    public List<String> resultadoapi;

    //Link del servidor.
    public String linkrequestAPI="";

    //Paciente.
    private String paciente;

    //Tipo, estado o pregunta.
    private boolean tipo;

    //Audio en base64.
    private String audio;

    //Context para los toast.
    private Context con;

    //Número de respuesta.
    private int responsecode;

    //Token de seguridad.
    private String token;

    /**
     * Constructor de la clase.
     * @param link link del servidor.
     * @param paciente paciente seleccionado.
     * @param tipo tipo de interpretación.
     * @param audio audio en base64.
     * @param con context para los toast
     * @param token token de seguridad.
     */
    public PostClasifica(String link,String paciente,boolean tipo, String audio,Context con,String token){
        resultadoapi = new LinkedList<>();
        this.linkrequestAPI=link;
        this.paciente=paciente;
        this.tipo = tipo;
        this.audio=audio;
        this.con=con;
        this.token=token;
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
     * @param s Lista resultado.
     */
    @Override
    protected void onPostExecute(List<String> s){
        super.onPostExecute(s);
        resultadoapi=s;
        if(responsecode==HttpURLConnection.HTTP_FORBIDDEN) {
            Toast.makeText(con, "ERROR Er2:\nToken de seguridad incorrecto, contacte con el Administrador.", Toast.LENGTH_LONG).show();
        }else if(responsecode==HttpURLConnection.HTTP_INTERNAL_ERROR){
            Toast.makeText(con, "ERROR Er8:\nEl fichero de clasificación de " + paciente + " no está, avise al Administrador.", Toast.LENGTH_LONG).show();
        }else if(responsecode!=HttpURLConnection.HTTP_OK){
            Toast.makeText(con, "ERROR Er1:\nNo se pudo conectar con el servidor.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Obtiene el resultado de la aplicación en segundo plano.
     * @param voids
     * @return resultado.
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

            //Parámetros
            OutputStream dos = urlConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(dos,"UTF-8"));
            bw.write("paciente="+paciente+"&tipo="+Boolean.toString(tipo)+"&audio="+audio+"&token="+token);
            bw.flush();
            bw.close();
            dos.close();

            //Respuesta
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

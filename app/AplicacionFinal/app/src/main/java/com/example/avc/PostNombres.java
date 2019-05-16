/**
 * AVC ~ Asistente Virtual para la Comunicación
 *
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */

//Package
package com.example.avc;

//Imports
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase para hacer el post para obtener los pacientes.
 */
public class PostNombres extends AsyncTask<Void,Void, List<String>> {
    //Lista de pacientes.
    public List<String> resultadoapi;

    //Link del servidor.
    public String linkrequestAPI="";

    /**
     * Constructor de la clase.
     * @param link link del servidor.
     */
    public PostNombres(String link){
        resultadoapi = new LinkedList<>();
        this.linkrequestAPI=link;
    }

    /**
     * Método que se ejecuta antes del execute.
     */
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    /**
     * Método que se ejecuta después del execute y que devuelve el resultado.
     * @param s lista de los pacientes.
     */
    @Override
    protected void onPostExecute(List<String> s){
        super.onPostExecute(s);
        resultadoapi=s;
    }

    /**
     * Método que obtiene el resultado en segundo plano.
     * @param voids
     * @return
     */
    @Override
    protected List<String> doInBackground(Void... voids) {
        List<String> result = new LinkedList<>();
        String wsURL=linkrequestAPI;
        try {
            //Conexión
            URL url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //Settings de la conexión
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(5000);

            //Respuesta
            int responsecode= urlConnection.getResponseCode();
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

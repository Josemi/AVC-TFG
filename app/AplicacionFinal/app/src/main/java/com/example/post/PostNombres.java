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
 * Clase para hacer el post para obtener los pacientes.
 */
public class PostNombres extends AsyncTask<Void,Void, List<String>> {
    //Lista de pacientes.
    private List<String> resultadoapi;

    //Link del servidor.
    private String linkrequestAPI="";

    //Clave de seguridad.
    private String token;

    //Contexto para los toast.
    private Context con;

    //Código de respuesta.
    private int responsecode;

    /**
     * Constructor de la clase.
     * @param link link del servidor.
     * @param token token de seguridad.
     * @param con context para los toast
     */
    public PostNombres(String link,String token,Context con){
        resultadoapi = new LinkedList<>();
        this.linkrequestAPI=link;
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
     * Método que se ejecuta después del execute y que devuelve el resultado.
     * @param s lista de los pacientes.
     */
    @Override
    protected void onPostExecute(List<String> s){
        super.onPostExecute(s);
        resultadoapi=s;
        if(responsecode==HttpURLConnection.HTTP_FORBIDDEN) {
            Toast.makeText(con, "ERROR Er2:\nToken de seguridad incorrecto, contacte con el Administrador.", Toast.LENGTH_LONG).show();
        }else if(responsecode==HttpURLConnection.HTTP_INTERNAL_ERROR){
            Toast.makeText(con, "ERROR Er4:\nEl fichero con la lista de nombres no está en el servidor, contacte con el Administrador.", Toast.LENGTH_LONG).show();
        }else if(responsecode!=HttpURLConnection.HTTP_OK){
            Toast.makeText(con, "ERROR Er1:\nNo se pudo conectar con el servidor.", Toast.LENGTH_LONG).show();
        }
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
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(5000);

            //Parámetros.
            OutputStream dos = urlConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(dos,"UTF-8"));
            bw.write("token="+token);
            bw.flush();
            bw.close();
            dos.close();

            //Respuesta
            responsecode= urlConnection.getResponseCode();
            if(responsecode==HttpURLConnection.HTTP_OK){
               BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
               String linea;
               while((linea=in.readLine())!=null){
                   if(linea=="None"){
                       return null;
                   }
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

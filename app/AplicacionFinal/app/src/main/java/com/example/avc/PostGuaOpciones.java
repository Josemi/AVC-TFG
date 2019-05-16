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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Clase que hace el post para guardar las opciones de un paciente.
 */
public class PostGuaOpciones extends AsyncTask<Void,Void, Boolean>{
    //Resultado del post.
    public Boolean resultadoapi;

    //Link del servidor.
    public String linkrequestAPI="";

    //Paciente seleccionado.
    private String paciente;

    //Valores a guardar.
    private List<String> valores;

    /**
     * Constructor de la clase.
     * @param link link del servidor.
     * @param paciente paciente seleccionado.
     * @param valores valores a guardar.
     */
    public PostGuaOpciones(String link,String paciente,List<String> valores){
        resultadoapi = new Boolean(false);
        this.linkrequestAPI=link;
        this.paciente=paciente;
        this.valores = valores;
    }

    /**
     * Método que se ejecuta antes de execute.
     */
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    /**
     * Métood que se ejecuta después del execute y obtiene el resultado.
     * @param s resultado, true si se ha realizado con éxito.
     */
    @Override
    protected void onPostExecute(Boolean s){
        super.onPostExecute(s);
        resultadoapi=s;
    }

    /**
     * Método que realiza el post en segundo plano.
     * @param voids
     * @return true si se ha realizado con éxito.
     */
    @Override
    protected Boolean doInBackground(Void... voids) {
        String wsURL=linkrequestAPI;
        try {
            //Conexión.
            URL url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //Settigns de la conexión.
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(5000);

            //Parámetros.
            OutputStream dos = urlConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(dos,"UTF-8"));
            String parametros = "paciente="+paciente;
            for(int i=1; i<=valores.size();i++){
                parametros+="&v"+i+"="+valores.get(i-1);
            }
            bw.write(parametros);
            bw.flush();
            bw.close();
            dos.close();

            //Resultado.
            int responsecode= urlConnection.getResponseCode();
            if(responsecode==HttpURLConnection.HTTP_OK){
               return true;
            }else{
                return false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

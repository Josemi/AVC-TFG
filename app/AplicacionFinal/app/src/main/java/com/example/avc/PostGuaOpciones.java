package com.example.avc;

import android.content.Context;
import android.os.AsyncTask;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PostGuaOpciones extends AsyncTask<Void,Void, Boolean>{
    private Context httpContext;
    public Boolean resultadoapi;
    public String linkrequestAPI="";
    private String paciente;
    private List<String> valores;

    public PostGuaOpciones(Context c,String link,String paciente,List<String> valores){
        resultadoapi = new Boolean(false);
        this.httpContext = c;
        this.linkrequestAPI=link;
        this.paciente=paciente;
        this.valores = valores;
    }


    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean s){
        super.onPostExecute(s);
        resultadoapi=s;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String wsURL=linkrequestAPI;
        try {
            URL url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

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

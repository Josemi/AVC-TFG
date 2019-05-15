package com.example.avc;

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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PostClasifica extends AsyncTask<Void,Void, List<String>> {

    private Context httpContext;
    public List<String> resultadoapi;
    public String linkrequestAPI="";
    private String paciente;
    private boolean tipo;
    private String audio;

    public PostClasifica(Context c,String link,String paciente,boolean tipo, String audio){
        resultadoapi = new LinkedList<>();
        this.httpContext = c;
        this.linkrequestAPI=link;
        this.paciente=paciente;
        this.tipo = tipo;
        this.audio=audio;
    }


    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<String> s){
        super.onPostExecute(s);
        resultadoapi=s;
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        List<String> result = new LinkedList<>();
        String wsURL=linkrequestAPI;
        try {
            URL url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream dos = urlConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(dos,"UTF-8"));
            bw.write("paciente="+paciente+"&tipo="+tipo+"&audio="+audio);
            bw.flush();
            bw.close();
            dos.close();

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

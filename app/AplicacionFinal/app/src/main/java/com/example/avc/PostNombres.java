package com.example.avc;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class PostNombres extends AsyncTask<Void,Void, List<String>> {
    private Context httpContext;
    public List<String> resultadoapi;
    public String linkrequestAPI="";

    public PostNombres(Context c,String link){
        resultadoapi = new LinkedList<>();
        this.httpContext = c;
        this.linkrequestAPI=link;
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

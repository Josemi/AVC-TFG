package com.example.avc;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PosTask extends AsyncTask<Void,Void, List<String>> {
    private Context httpContext;
    ProgressDialog progressDialog;
    public List<String> resultadoapi;
    public String linkrequestAPI="";

    public PosTask(Context c,String link){
        resultadoapi = new LinkedList<>();
        this.httpContext = c;
        this.linkrequestAPI=link;
    }


    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        //progressDialog = ProgressDialog.show(httpContext,"Procesando","espere");
    }

    @Override
    protected void onPostExecute(List<String> s){
        super.onPostExecute(s);
        //progressDialog.dismiss();
        resultadoapi=s;
        Toast.makeText(httpContext,"Conexión realizada",Toast.LENGTH_LONG).show();
    }

    public String getStringFromJSON(JSONObject params) throws Exception{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr= params.keys();
        while(itr.hasNext()){
            String key = itr.next();
            Object val = params.get(key);

            if(first){
                first=false;
            }else{
                result.append("&");
            }
            result.append(URLEncoder.encode(key,"UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(val.toString(),"UTF-8"));
        }
        return result.toString();
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

            //OutputStream os = urlConnection.getOutputStream();
            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            //writer.flush();
            //writer.close();
            //os.close();

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

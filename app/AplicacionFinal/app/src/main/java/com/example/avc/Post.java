package com.example.avc;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Post extends AsyncTask<Void, Void, String>
{
    @Override
    protected String doInBackground(Void... params) {
        try {
            URL url = new URL("http://192.168.1.59:5000/Nombres");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            connection.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            out.writeBytes("");
            out.flush();

            out.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder resout = new StringBuilder();

            while((line= br.readLine()) != null){
                resout.append(line);
                Log.d("linea",line);
            }
            br.close();
            return resout.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

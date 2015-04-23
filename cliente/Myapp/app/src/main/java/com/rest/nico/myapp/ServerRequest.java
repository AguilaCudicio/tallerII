package com.rest.nico.myapp;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerRequest {

    public String getUsersOnline (String token) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        //todo construir la URL con el usuario y token en vez de hardcodear
        HttpGet httpGet = new HttpGet("http://www.cheesejedi.com/rest_services/get_big_cheese?level=1");
        String text;
        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            text = inputStreamToString(entity.getContent());
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }

        return text;
    }

    public String getMessages (String token, String secondUser) {
        //todo completar
        return null;
    }

    //todo completar el resto de los metodos



    private  String inputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}

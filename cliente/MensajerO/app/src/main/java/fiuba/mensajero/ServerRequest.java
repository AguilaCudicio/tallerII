package fiuba.mensajero;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServerRequest {

    private String url;     //url base

    public ServerRequest() {
        url= "http://190.173.13.167:5000";
    }

    //devuelve un array con todos los usuarios
    public ArrayList<String> getUsersOnline (String user, String token) {
        StringBuilder stringBuilder = new StringBuilder(url);
        stringBuilder.append("/usuarios");
        String finalURL = stringBuilder.toString();

        RestMethod rest = new RestMethod();
        String resp = rest.GET(finalURL);
        int respCode = rest.getStatusCode();
        ArrayList<String> list;

        if (respCode == 200 && resp != null) {
            JSONParser jp = new JSONParser();
            list = jp.parseUsersOnline(resp);
        }
        else {
            list = null;
        }

       return list;
    }

    //devuelve un string "ok" o el motivo de error si fallo
    public String register(String user, String password, String name) {
        StringBuilder stringBuilder = new StringBuilder(url);
        stringBuilder.append("/usuario/");
        stringBuilder.append(user);
        String finalURL = stringBuilder.toString();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("password", password);
            jsonObject.accumulate("nombre", name);
            jsonObject.accumulate("foto", "vacio");
            jsonObject.accumulate("ubicacion", "vacio");
        }
        catch (JSONException e) {
            Log.e("register", "json fallo crear register body");
        }
        RestMethod rest = new RestMethod();
        String resp = rest.POST(finalURL, jsonObject);
        int respCode = rest.getStatusCode();

        String ret;
        if (respCode == 201 && resp != null) {
            ret = "ok";
        }
        else {
            JSONParser jp = new JSONParser();
            ret = jp.getError(resp);
        }

        return ret;
    }

    public String logIn(String user, String password) {
        StringBuilder stringBuilder = new StringBuilder(url);
        stringBuilder.append("/login");
        String finalURL = stringBuilder.toString();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("password", password);
            jsonObject.accumulate("id", user);
        }
        catch (JSONException e) {
            Log.e("register", "json fallo crear login body");
        }
        RestMethod rest = new RestMethod();
        String resp = rest.POST(finalURL, jsonObject);
        int respCode = rest.getStatusCode();

        String ret, token;
        JSONParser jp = new JSONParser();
        if (respCode == 201 && resp != null) {
            ret = "ok";
            token = jp.getToken(resp);
            //todo almacenar el token en un archivo ?
        }
        else {
            ret = "usuario o clave invalida";
        }

        return ret;
    }


    public String getMessages (String token, String secondUser) {
        return null;
    }



}

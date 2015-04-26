package com.rest.nico.myapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JSONParser {

    public ArrayList<String> parseUsersOnline(String jsonstr) {

        ArrayList<String> list = new ArrayList<>();
        String nombre, estado;

        try {
            JSONArray users = new JSONArray(jsonstr);

            Log.d("cantidad elementos", Integer.toString(users.length()));
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                nombre = user.getString("nombre");
                estado = user.getString("estado");
                list.add(nombre);
                list.add(estado);
            }
        }
        catch (JSONException e) {
            return null;
        }

        return list;

    }
}

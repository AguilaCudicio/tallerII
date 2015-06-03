package fiuba.mensajero;

import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class JSONParser {

    public ArrayList<UserData> parseUsersOnline(String jsonstr) {
        ArrayList<UserData> list = new ArrayList<>();
        String nombre, estado, id;
        boolean nuevomsg;
        try {
            JSONArray users = new JSONArray(jsonstr);
            for (int i = 0; i < users.length(); i++) {
                boolean conectado = false;
                JSONObject user = users.getJSONObject(i);
                nombre = user.getString("nombre");
                estado = user.getString("estado");
                id = user.getString("id");
                nuevomsg = user.getBoolean("nuevomsg");
                if (estado.equals("conectado")) {
                    conectado = true;
                }
                UserData userData = new UserData(id, nombre, conectado, nuevomsg);
                list.add(userData);
            }
        }
        catch (JSONException e) {
            Log.e("JSON PARSER", "error al parsear lista de usaurios", e);
        }

        return list;
    }

    public ArrayList<MessageData> parseMessages(String jsonstr) {
        ArrayList<MessageData> list = new ArrayList<>();
        String id, time, message;
        int t;
        try {
            JSONArray msgs = new JSONArray(jsonstr);
            for (int i = 0; i < msgs.length(); i++) {
                JSONObject us = msgs.getJSONObject(i);
                id = us.getString("id");
                t = us.getInt("time");
                long dv = (long)t* 1000L;
                Date date = new Date(dv);
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
                df.setTimeZone(TimeZone.getTimeZone("GMT-3"));
                time = df.format(date);
                message = us.getString("msg");
                MessageData msgdata = new MessageData(id, time, message);
                list.add(msgdata);
            }
        }
        catch (JSONException e) {
            Log.e("JSON PARSER", "error al parsear mensajes", e);
        }

        return list;
    }

    public String parseError(String jsonstr) {
        String error = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            error = jsonObject.getString("error");
        }
        catch (JSONException e) {
            Log.e("JSON PARSER", "error al obtener mensaje de error");
        }

        return error;
    }

    public String parseToken(String jsonstr) {
        String token = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            token = jsonObject.getString("token");
        }
        catch (JSONException e) {
            Log.e("JSON PARSER", "error al obtener el token");
        }

        return token;
    }

    boolean isEmpty(String s) {
        return  s.trim().length() == 0;
    }

    public ProfileData parseProfile(String jsonstr) {
        ProfileData profile = null;
        String nombre, foto, ultimoacceso, telefono, email, ubicacion;
        int t;
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            nombre = jsonObject.getString("nombre");
            if (isEmpty(nombre)) nombre = null;
            foto = jsonObject.getString("foto");
            if (isEmpty(foto)) foto = null;
            ubicacion = jsonObject.getString("ubicacion");
            if (isEmpty(ubicacion)) ubicacion = null;
            telefono = jsonObject.getString("telefono");
            if (isEmpty(telefono)) telefono = null;
            email = jsonObject.getString("email");
            if (isEmpty(email)) email = null;
            t = jsonObject.getInt("ultimoacceso");
            long dv = (long)t* 1000L;
            Date date = new Date(dv);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.US);
            df.setTimeZone(TimeZone.getTimeZone("GMT-3"));
            ultimoacceso = df.format(date);
            profile = new ProfileData(nombre, foto, ultimoacceso, telefono, email, ubicacion);

        }
        catch (JSONException e) {
            Log.e("JSON PARSER", "error al parsear un profile");
        }
        return profile;
    }
}

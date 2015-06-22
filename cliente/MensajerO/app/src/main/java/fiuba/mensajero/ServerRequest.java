package fiuba.mensajero;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Clase que realiza operaciones concretas de la aplicacion que involucran al servidor
 */
public class ServerRequest {

    private String url;     //url base
    private String user;
    private String token;
    private String errormsg;
    private String pass;

    /**
     * Constructor. Construye URL del servidor y recupera datos guardados localmente.
     * @param c Contexto actual
     * @throws ServerAddressNotFoundException cuando la ip del servidor no fue especificada
     */
    public ServerRequest(Context c) throws ServerAddressNotFoundException {
        Context context;
        errormsg = null;
        SharedPreferences sharedPref= c.getSharedPreferences("appdata", 0);
        String ip = sharedPref.getString("ip", null);
        token = sharedPref.getString("token", null);
        user = sharedPref.getString("user", null);
        pass = sharedPref.getString("password", null);

        if (ip == null) {
            throw new ServerAddressNotFoundException("La IP del servidor no fue especificada");
        }
        url="http://" + ip + ":5000";
        Log.i("URL DEL SERVER ES", url);
    }

    /**
     * Mensaje de error de acuerdo a un codigo de error http
     * @return String del error
     */
    public String getErrormsg() {
        return errormsg;
    }

    /**
     * Solicita al servidor los usuarios online.
     * @return ArrayList de UserData con los usuarios registrados o null si fallo el http GET
     */
    public ArrayList<UserData> getUsersOnline () {
        String finalURL = url + "/usuarios" + "?r_user=" + this.user + "&token=" + this.token;
        RestMethod rest = new RestMethod();
        String resp = rest.GET(finalURL);
        int respCode = rest.getStatusCode();
        ArrayList<UserData> list = null ;
        JSONParser jp = new JSONParser();
        switch (respCode) {
            case 200:   list = jp.parseUsersOnline(resp);
                        break;
            case 401:   errormsg = jp.parseError(resp);
                        break;
            case -1:    errormsg = "No se pudo conectar con el servidor";
                        break;
            default:    errormsg = "Http protocol error: " + String.valueOf(respCode);
                        break;
        }

       return list;
    }

    /**
     * Solicit al servidor el registro de un nuevo usuario.
     * @param user Id del usuario nuevo a registrar
     * @param password Password del usuario a registrar
     * @param name Nombre del usuario a registrar
     * @return String con la leyenda "ok" o null si fallo el http POST
     */
    public String register(String user, String password, String name) {
        String finalURL = url + "/usuario/" + user;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("password", password);
            jsonObject.accumulate("nombre", name);
        }
        catch (JSONException e) {
            Log.e("register", "json fallo crear register body");
        }
        RestMethod rest = new RestMethod();
        String resp = rest.POST(finalURL, jsonObject);
        int respCode = rest.getStatusCode();

        String ret = null;
        switch (respCode) {
            case 201:   ret = "ok";
                        break;
            case 401:   JSONParser jp = new JSONParser();
                        errormsg = jp.parseError(resp);
                        break;
            case -1:    errormsg = "No se pudo conectar con el servidor";
                        break;
            default:    errormsg = "Http protocol error: " + String.valueOf(respCode);
                        break;
        }

        return ret;
    }

    /**
     * Solicita al servidor el logueo del usuario.
     * @param user id del usuario a loguear
     * @param password password del usuario a loguear
     * @return String con un token valido o null si fallo el http POST
     */
    public String logIn(String user, String password) {
        String finalURL = url + "/login";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("password", password);
            jsonObject.accumulate("id", user);
        }
        catch (JSONException e) {
            Log.e("login", "json fallo crear login body");
        }
        RestMethod rest = new RestMethod();
        String resp = rest.POST(finalURL, jsonObject);
        int respCode = rest.getStatusCode();
        String token = null;
        JSONParser jp = new JSONParser();
        switch (respCode) {
            case 201:   token = jp.parseToken(resp);
                        break;
            case 401:   errormsg = jp.parseError(resp);
                        break;
            case -1:    errormsg = "No se pudo conectar con el servidor";
                        break;
            default:    errormsg = "Http protocol error: " + String.valueOf(respCode);
                        break;
        }
        return token;
    }


    /**
     * Solicita al servidor los mensajes provenientes de un usuario o del broadcast.
     * @param user2 Usuario emisor de los mensajes o null para el broadcast
     * @return ArrayList de tipo MessageData con la informacion de los mensajes enviados o null si fallo el http GET
     */
    public ArrayList<MessageData> getMessages(String user2) {
        String finalURL;
        if (user2.equals("broadcast"))
            finalURL = url + "/broadcast?r_user=" + user + "&token=" + token;
        else
            finalURL = url + "/conversacion/" + user2 + "?r_user=" + user + "&token=" + token;
        RestMethod rest = new RestMethod();
        String resp = rest.GET(finalURL);
        int respCode = rest.getStatusCode();
        ArrayList<MessageData> list = null;
        JSONParser jp = new JSONParser();
        switch (respCode) {
            case 200:   list = jp.parseMessages(resp);
                        break;
            case 401:   errormsg = jp.parseError(resp);
                        break;
            case -1:    errormsg = "No se pudo conectar con el servidor";
                        break;
            default:    errormsg = "Http protocol error: " + String.valueOf(respCode);
                        break;
        }
        return list;
    }

    /**
     * Solicita al servidor el envio de un nuevo mensaje.
     * @param user2 Usuario receptor del mensaje o null si se quiere enviar a todos (broadcast)
     * @param message Mensaje a enviar
     * @return String con la leyenda "ok" si fue exitoso o null si fallo el http POST
     */
    public String sendMessage(String user2, String message) {
        String finalURL;
        if (user2.equals("broadcast"))
            finalURL = url + "/broadcast?r_user=" + user + "&token=" + token;
        else
            finalURL = url + "/conversacion/" + user2 + "?r_user=" + user + "&token=" + token;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("mensaje", message);
        }
        catch (JSONException e) {
            Log.e("sendmessage", "json fallo crear register body");
        }
        RestMethod rest = new RestMethod();
        String resp = rest.POST(finalURL, jsonObject);
        int respCode = rest.getStatusCode();

        String ret = null;
        switch (respCode) {
            case 201:   ret = "ok";
                        break;
            case 401:   JSONParser jp = new JSONParser();
                        errormsg = jp.parseError(resp);
                        break;
            case -1:    errormsg = "No se pudo conectar con el servidor";
                        break;
            default:    errormsg = "Http protocol error: " + String.valueOf(respCode);
                        break;
        }
        return ret;
    }

    /**
     * Solicitar al servidor el perfil de un usuario
     * @param user2 Id del usuario solicitado
     * @return objeto ProfileData con la informacion del perfil del usuario o null si fallo el http GET
     */
    public ProfileData getProfile(String user2) {
        if (user2 == null)
            user2 = user;
        String finalURL = url + "/usuario/" + user2 + "?r_user=" + user + "&token=" + token;
        RestMethod rest = new RestMethod();
        String resp = rest.GET(finalURL);
        int respCode = rest.getStatusCode();
        ProfileData ret = null;
        JSONParser jp = new JSONParser();
        switch (respCode) {
            case 200:   ret = jp.parseProfile(resp);
                break;
            case 401:   errormsg = jp.parseError(resp);
                break;
            case -1:    errormsg = "No se pudo conectar con el servidor";
                break;
            default:    errormsg = "Http protocol error: " + String.valueOf(respCode);
                break;
        }
        return ret;
    }

    boolean isEmpty(String s) {
        return  s.trim().length() == 0;
    }


    /**
     * Solicitar al servidor el cambio en los datos de un usuario. Dejar en null o vacio los campos que no se desean modificar
     * @param nombre Nuevo nombre del usuario
     * @param password Nueva password del usuario
     * @param foto Nueva foto del usuario
     * @param fotochica Forma reducida de la nueva foto
     * @param telefono Nuevo telefono del usuario
     * @param email Nuevo email del usuario
     * @param ubicacion Nueva ubicacion del usuario
     * @param showOffline True si se desea aparecer desconectado
     * @return String con la leyenda "ok" o null si hubo error en el http PUT
     */
    public String editProfile(String nombre, String password, String foto, String fotochica, String telefono, String email, String ubicacion, String showOffline) {
        String finalURL = url + "/usuario/" + user + "?r_user=" + user + "&token=" + token + "&password=" + pass;
        Log.d("DAME LA URL", finalURL);
        JSONObject jsonObject = new JSONObject();
        try {
            if (password != null && !isEmpty(password) )
                jsonObject.accumulate("password", password);
            if (nombre != null && !isEmpty(nombre) )
                jsonObject.accumulate("nombre", nombre);
            if (foto != null && !isEmpty(foto) )
                jsonObject.accumulate("foto", foto);
            if (fotochica != null && !isEmpty(fotochica) )
                jsonObject.accumulate("fotochica", fotochica);
            if (telefono != null && !isEmpty(telefono) )
                jsonObject.accumulate("telefono", telefono);
            if (email != null && !isEmpty(email) )
                jsonObject.accumulate("email", email);
            if (ubicacion != null && !isEmpty(ubicacion) )
                jsonObject.accumulate("ubicacion", ubicacion);
            if (showOffline != null && !isEmpty(showOffline)) {
                if (showOffline.equals("true"))
                    jsonObject.accumulate("appear_offline", true);
                if (showOffline.equals("false"))
                    jsonObject.accumulate("appear_offline", false);
            }
            Log.d("JSONENVIADO", jsonObject.toString());
        }
        catch (JSONException e) {
            Log.e("editprofile", "json fallo crear register body");
        }
        RestMethod rest = new RestMethod();
        String resp = rest.PUT(finalURL, jsonObject);
        int respCode = rest.getStatusCode();

        String ret = null;
        switch (respCode) {
            case 201:   ret = "ok";
                        break;
            case 401:   JSONParser jp = new JSONParser();
                        errormsg = jp.parseError(resp);
                        break;
            case -1:    errormsg = "No se pudo conectar con el servidor";
                        break;
            default:    errormsg = "Http protocol error: " + String.valueOf(respCode);
                        break;
        }

        return ret;
    }
}

package fiuba.mensajero;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JSONParser {

    public ArrayList<UserData> parseUsersOnline(String jsonstr) {
        ArrayList<UserData> list = new ArrayList<>();
        String nombre, estado, id;
        try {
            JSONArray users = new JSONArray(jsonstr);
            for (int i = 0; i < users.length(); i++) {
                boolean conectado = false;
                JSONObject user = users.getJSONObject(i);
                nombre = user.getString("nombre");
                estado = user.getString("estado");
                id = user.getString("id");
                if (estado.equals("conectado")) {
                    conectado = true;
                }
                UserData userData = new UserData(id, nombre, conectado);
                list.add(userData);
            }
        }
        catch (JSONException e) {
            Log.e("JSON PARSER", "I got an error", e);
        }

        return list;
    }

    public String getError(String jsonstr) {
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

    public String getToken(String jsonstr) {
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
}

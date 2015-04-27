package fiuba.mensajero;

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

            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                nombre = user.getString("nombre");
                estado = user.getString("estado");
                if (estado.equals("conectado")) {
                    estado = estado.concat(nombre);
                    list.add(estado);
                }
                else
                    list.add(nombre);
            }
        }
        catch (JSONException e) {
            Log.e("JSONPARSER", "I got an error", e);
        }

        return list;

    }
}

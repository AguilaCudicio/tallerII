package fiuba.mensajero;


import java.util.ArrayList;

public class ServerRequest {

    private String url;

    public ServerRequest() {
        //url = "http://echo.jsontest.com";
        //url = "http://200.127.232.72:5000";
        url= "http://200.127.232.72:5000/Usuario";
    }


    public ArrayList<String> getUsersOnline (String user, String token) {
        //todo construir la url como corresponde con el token
        StringBuilder stringBuilder = new StringBuilder(url);
        //stringBuilder.append("/nombre/pedro/estado/conectado");
        //stringBuilder.append("/Usuario");

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


    public String getMessages (String token, String secondUser) {
        //todo completar
        return null;
    }

    //todo completar el resto de los metodos


}

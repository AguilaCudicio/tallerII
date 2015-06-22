package fiuba.mensajero;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.util.ArrayList;

public class NetworkService extends IntentService {

    public NetworkService() {
        super("NetworkService");
    }

    public static final int RUNNING = 0;
    public static final int OK = 1;
    public static final int ERROR = 2;
    public static final int OK_MSG = 3;


    protected void onHandleIntent(Intent intent) {
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String command = intent .getStringExtra("command");
        Bundle b = new Bundle();
        Context context= getApplicationContext();
        ServerRequest serverRequest;
        try {
            serverRequest = new ServerRequest(context);
        }
        catch (ServerAddressNotFoundException e) {
            b.putString("error", e.getMessage());
            receiver.send(ERROR, b);
            return;
        }

        if(command.equals("getListaConectados")) {
            receiver.send(RUNNING, Bundle.EMPTY);
            try {
                ArrayList<UserData> res = serverRequest.getUsersOnline();
                if (res != null) {
                    b.putParcelableArrayList("result", res);
                    receiver.send(OK, b);
                }
                else {
                    b.putString("error", serverRequest.getErrormsg());
                    receiver.send(ERROR, b);
                }
            } catch(Exception e) {
                b.putString("error", e.toString());
                receiver.send(ERROR, b);
            }
        }

        if (command.equals("register")) {
            receiver.send(RUNNING, Bundle.EMPTY);
            try {
                String user = intent.getStringExtra("user");
                String password = intent.getStringExtra("password");
                String name = intent.getStringExtra("name");
                String res = serverRequest.register(user, password, name);
                if (res != null) {
                    b.putString("result", res);
                    receiver.send(OK, b);
                }
                else {
                    b.putString("error", serverRequest.getErrormsg());
                    receiver.send(ERROR, b);
                }
            } catch(Exception e) {
                b.putString("error", e.toString());
                receiver.send(ERROR, b);
            }
        }

        if (command.equals("logIn")) {
            receiver.send(RUNNING, Bundle.EMPTY);
            try {
                String user = intent.getStringExtra("user");
                String password = intent.getStringExtra("password");
                String res = serverRequest.logIn(user, password);
                if (res != null) {
                    b.putString("result", res);
                    receiver.send(OK, b);
                }
                else {
                    b.putString("error", serverRequest.getErrormsg());
                    receiver.send(ERROR, b);
                }
            } catch(Exception e) {
                b.putString("error", e.toString());
                receiver.send(ERROR, b);
            }
        }

        if(command.equals("getMessages")) {
            receiver.send(RUNNING, Bundle.EMPTY);
            try {
                String user2 = intent.getStringExtra("user2");
                ArrayList<MessageData> res = serverRequest.getMessages(user2);
                if (res != null) {
                    b.putParcelableArrayList("result", res);
                    receiver.send(OK, b);
                } else {
                    b.putString("error", serverRequest.getErrormsg());
                    receiver.send(ERROR, b);
                }
            } catch (Exception e) {
                b.putString("error", e.toString());
                receiver.send(ERROR, b);
            }
        }

          if (command.equals("sendMessage")) {
            receiver.send(RUNNING, Bundle.EMPTY);
            try {
                String user2 = intent.getStringExtra("user2");
                String message = intent.getStringExtra("message");
                String res = serverRequest.sendMessage(user2, message);
                if (res != null) {
                    b.putString("result", res);
                    receiver.send(OK_MSG, b);
                }
                else {
                    b.putString("error", serverRequest.getErrormsg());
                    receiver.send(ERROR, b);
                }
            } catch(Exception e) {
                b.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(ERROR, b);
            }
        }

        if(command.equals("getProfile")) {
            receiver.send(RUNNING, Bundle.EMPTY);
            try {
                String user2 = intent.getStringExtra("user2");
                ProfileData res = serverRequest.getProfile(user2);
                b.putParcelable("result", res);
                receiver.send(OK, b);
            } catch(Exception e) {
                b.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(ERROR, b);
            }
        }

        if(command.equals("editProfile")) {
            receiver.send(RUNNING, Bundle.EMPTY);
            String nombre = intent.getStringExtra("nombre");
            String foto = intent.getStringExtra("foto");
            String fotochica = intent.getStringExtra("fotochica");
            String password = intent.getStringExtra("password");
            String telefono = intent.getStringExtra("telefono");
            String email = intent.getStringExtra("email");
            String ubicacion = intent.getStringExtra("ubicacion");
            String editando = intent.getStringExtra("editando");
            String showOffline = intent.getStringExtra("showOffline");
            String res = serverRequest.editProfile(nombre, password, foto, fotochica, telefono, email, ubicacion, showOffline);
            if (res != null) {
                b.putString("result", res);
                receiver.send(OK_MSG, b);
            }
            else {
                b.putString("error", serverRequest.getErrormsg());
                receiver.send(ERROR, b);
            }
        }
    }


}

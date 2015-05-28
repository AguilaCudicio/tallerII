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
                String token = intent.getStringExtra("token");
                String user = intent.getStringExtra("user");
                ArrayList<UserData> res = serverRequest.getUsersOnline(user, token);
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
                String token = intent.getStringExtra("token");
                String user = intent.getStringExtra("user");
                String user2 = intent.getStringExtra("user2");
                ArrayList<String> res = serverRequest.getMessages(user, user2, token);
                if (res != null) {
                    b.putStringArrayList("result", res);
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

        /*  if (command.equals("sendMessage")) {
            receiver.send(RUNNING, Bundle.EMPTY);
            try {
                String user = intent.getStringExtra("user");
                String user2 = intent.getStringExtra("user2");
                String token = intent.getStringExtra("token");
                String message = intent.getStringExtra("message");
                String res = serverRequest.sendMessage(user, user2, token, message);
                b.putString("result", res);
                receiver.send(OK, b);
            } catch(Exception e) {
                b.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(ERROR, b);
            }
        }

        if (command.equals("sendBroadcast")) {
            receiver.send(RUNNING, Bundle.EMPTY);
            try {
                String user = intent.getStringExtra("user");
                String token = intent.getStringExtra("token");
                String message = intent.getStringExtra("message");
                String res = serverRequest.sendBroadcast(user, token, message);
                b.putString("result", res);
                receiver.send(OK, b);
            } catch(Exception e) {
                b.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(ERROR, b);
            }
        }

        if(command.equals("getProfile")) {
            receiver.send(RUNNING, Bundle.EMPTY);
            try {
                String token = intent.getStringExtra("token");
                String user = intent.getStringExtra("user");
                ArrayList<String> res = serverRequest.getProfile(user, token);
                b.putStringArrayList("result", res);
                receiver.send(OK, b);
            } catch(Exception e) {
                b.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(ERROR, b);
            }
        }

        if(command.equals("editProfile")) {
            receiver.send(RUNNING, Bundle.EMPTY);
            try {
                String token = intent.getStringExtra("token");
                String user = intent.getStringExtra("user");
                String nombre = intent.getStringExtra("nombre");
                String foto = intent.getStringExtra("foto");
                String res = serverRequest.editProfile(user, token);
                b.putString("result", res);
                receiver.send(OK, b);
            } catch(Exception e) {
                b.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(ERROR, b);
            }
        }
        */
    }


}

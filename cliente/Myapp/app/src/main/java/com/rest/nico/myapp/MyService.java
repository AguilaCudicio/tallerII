package com.rest.nico.myapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.util.ArrayList;



public class MyService extends IntentService {

    public MyService() {
        super("MyService");
    }

    public static final int RUNNING = 0;
    public static final int OK = 1;
    public static final int ERROR = 2;


    protected void onHandleIntent(Intent intent) {


        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String command = intent .getStringExtra("command");
        Bundle b = new Bundle();
        ServerRequest serverRequest = new ServerRequest();

        if(command.equals("getListaConectados")) {
            receiver.send(RUNNING, Bundle.EMPTY);
            try {
                String token = intent.getStringExtra("token");
                String user = intent.getStringExtra("user");
                ArrayList<String> res = serverRequest.getUsersOnline(user, token);
                b.putStringArrayList("result", res);
                receiver.send(OK, b);
            } catch(Exception e) {
                b.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(ERROR, b);
            }
        }

     /*   if (command.equals("getMensajes")) {
            receiver.send(RUNNING, Bundle.EMPTY);
            try {
                //llamar al metodo que hace get
                String res = getMensajes();
                b.putString("result", res);
                receiver.send(OK, b);
            } catch(Exception e) {
                b.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(ERROR, b);
            }
        } */

    }


}
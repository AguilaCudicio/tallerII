package com.rest.nico.myapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class MainActivity extends Activity implements OnClickListener, MyResultReceiver.Receiver {

    public MyResultReceiver mReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.my_button).setOnClickListener(this);

        String str = "Aca va a aparecer el texto requerido";
        EditText et = (EditText)findViewById(R.id.my_edit);
        et.setText(str);

        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);

    }

    @Override
    public void onClick(View arg0) {
        Button b = (Button)findViewById(R.id.my_button);
        b.setClickable(false);
        getUsersOnline(null, null);

    }

    public void getUsersOnline(String user, String token) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("command", "getListaConectados");
        intent.putExtra("token", token);
        intent.putExtra("user", user);
        startService(intent);
    }

    public void logIn(String user, String password) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("command", "logIn");
        intent.putExtra("user", user);
        intent.putExtra("password", password);
        startService(intent);
    }

    // user = usuario del profile
    public void getProfile(String user, String token) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("command", "getProfile");
        intent.putExtra("token", token);
        intent.putExtra("user", user);
        startService(intent);
    }

    // crear o modificar perfil
    public void modifyProfile(String user, String token, String nombre, String foto, String ubicacion, Boolean conectado ) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("command", "modifyProfile");
        intent.putExtra("token", token);
        intent.putExtra("user", user);
        intent.putExtra("conectado", conectado);
        intent.putExtra("nombre", nombre);
        intent.putExtra("foto", foto);
        intent.putExtra("ubicacion", ubicacion);
        startService(intent);
    }

    public void sendMessage(String token, String user, String userDestiny, String message) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("command", "sendMessage");
        intent.putExtra("token", token);
        intent.putExtra("user", user);
        intent.putExtra("userDestiny", userDestiny);
        intent.putExtra("message", message);
        startService(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        EditText et;
        switch (resultCode) {
            case MyService.RUNNING:
                et = (EditText)findViewById(R.id.my_edit);
                et.setText("Solicitando info al servidor");
                break;
            case MyService.OK:
                ArrayList<String> list = resultData.getStringArrayList("result");
                et = (EditText)findViewById(R.id.my_edit);
                et.setText("");
                if (list != null)
                    for (String s : list) {
                        et.append(s);
                        et.append(" ");
                        if (s.equals("conectado") || s.equals("desconectado")) {
                            et.append("/n");
                        }
                    }
                else
                    et.append("fallo algo");

                Button b = (Button)findViewById(R.id.my_button);

                b.setClickable(true);
                // hide progress
                break;
            case MyService.ERROR:
                // handle the error;
                break;
        }
    }

}
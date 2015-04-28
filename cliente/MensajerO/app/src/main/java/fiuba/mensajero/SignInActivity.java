package fiuba.mensajero;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;


public class SignInActivity extends ActionBarActivity implements MyResultReceiver.Receiver {

    public MyResultReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    //* handler para el boton de Terminar
    public void handTerminar(View view) {

        final EditText usr = (EditText) findViewById(R.id.editTextNombreUsuario);
        String usuario = usr.getText().toString();

        final EditText nom = (EditText) findViewById(R.id.editTextNombreMostrar);
        String nombre = nom.getText().toString();

        final EditText pass = (EditText) findViewById(R.id.editTextPassw);
        String password = pass.getText().toString();

        /* TODO: Deberia enviar password, usuario y nombre al servidor */
        register(usuario, password, nombre);

      /*  AlertDialog alerta = new AlertDialog.Builder(this).create();
        alerta.setTitle("Registro completo");
        alerta.setMessage("Volviendo a la pantalla principal, oprima log in para ingresar");
        alerta.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alerta.setIcon(R.drawable.okk);
        alerta.show();  */

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void register(String user, String password, String name) {
        Intent intent = new Intent(this, NetworkService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("command", "register");
        intent.putExtra("user", user);
        intent.putExtra("password", password);
        intent.putExtra("name", name);
        startService(intent);
    }

    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case NetworkService.RUNNING:
                Log.i("onreceiveresult", "esta corriendo el servicio de registro");
                //aca se podria mostrar algo mientras el servicio esta corriendo
                break;
            case NetworkService.OK:
                Log.d("onreceiveresult", "se completo la operacion de registro ");
                String mensaje = resultData.getString("result");
                if (mensaje == null)
                     Log.e("RESULTADO DE REGISTRO", "parece que no hay internet");
                else
                    //nota si el mensaje es "ok" entonces tuvo exito, si no el mensaje es el problema
                    Log.i("RESULTADO DE REGISTRO", mensaje);

                break;
            case NetworkService.ERROR:
                // handle the error;
                Log.e("onreceiveresult", "salto un error en registro");
                break;
        }
    }
}

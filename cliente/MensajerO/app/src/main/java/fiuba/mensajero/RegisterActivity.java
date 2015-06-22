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

/**
 * Activity para el registro de usuarios nuevos.
 */
public class RegisterActivity extends ActionBarActivity implements MyResultReceiver.Receiver {

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


    public void handTerminar(View view) {

        final EditText usr = (EditText) findViewById(R.id.editTextNombreUsuario);
        String usuario = usr.getText().toString();

        final EditText nom = (EditText) findViewById(R.id.editTextNombreMostrar);
        String nombre = nom.getText().toString();

        final EditText pass = (EditText) findViewById(R.id.editTextPassw);
        String password = pass.getText().toString();

        register(usuario, password, nombre);


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

    /**
     * Invocar al servicio para obtener el perfil del servidor
     * @param user Id del usuario nuevo
     * @param password password del usuario nuevo
     * @param name nombre del usuario nuevo
     */
    public void register(String user, String password, String name) {
        Intent intent = new Intent(this, NetworkService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("command", "register");
        intent.putExtra("user", user);
        intent.putExtra("password", password);
        intent.putExtra("name", name);
        startService(intent);
    }

    /**
     * Procesa la respuesta del servicio de red. Notifica el resultado con dialogos.
     * @param resultCode codigo de error del resultado
     * @param resultData datos de la respuesta
     */
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
                     Log.e("RESULTADO DE REGISTRO", "error inesperado");
                else if (mensaje.equals("ok")) {
                    //nota si el mensaje es "ok" entonces tuvo exito, si no el mensaje es el problema
                    Log.i("RESULTADO DE REGISTRO", mensaje);
                    AlertDialog alerta = new AlertDialog.Builder(this).create();
                    alerta.setTitle("Registro completo");
                    alerta.setMessage("Volviendo a la pantalla principal, oprima login para ingresar");
                    alerta.setButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alerta.show();
                }

                break;
            case NetworkService.ERROR:
                // handle the error;
                Log.e("onreceiveresult", "salto un error en registro");
                AlertDialog alerta = new AlertDialog.Builder(this).create();
                alerta.setTitle("Error");
                String err = resultData.getString("error");
                alerta.setMessage(err);
                alerta.setButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alerta.show();
                break;
        }
    }
}

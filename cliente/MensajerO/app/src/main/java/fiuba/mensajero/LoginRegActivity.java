package fiuba.mensajero;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

public class LoginRegActivity extends ActionBarActivity implements MyResultReceiver.Receiver {

    public MyResultReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg);
        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);

    }

    //* handler para el boton de Terminar
    public void handTerminar(View view) {

        final EditText nom = (EditText) findViewById(R.id.editTextNombre);
        String user = nom.getText().toString();

        final EditText pass = (EditText) findViewById(R.id.editTextPass);
        String password = pass.getText().toString();

        logIn(user, password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_reg, menu);
        return true;
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

    public void logIn(String user, String password) {
        Intent intent = new Intent(this, NetworkService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("command", "logIn");
        intent.putExtra("user", user);
        intent.putExtra("password", password);
        startService(intent);
    }

    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case NetworkService.RUNNING:
                Log.i("onreceiveresult", "esta corriendo el servicio de login");
                //aca se podria mostrar algo mientras el servicio esta corriendo
                break;
            case NetworkService.OK:
                Log.d("onreceiveresult", "se completo la operacion de login ");
                String mensaje = resultData.getString("result");
                if (mensaje == null)
                    Log.e("RESULTADO DE REGISTRO", "parece que no anda internet");
                else
                    //nota si el mensaje es "ok" entonces tuvo exito, si no el mensaje es el problema
                    Log.i("RESULTADO DE REGISTRO", mensaje);
                    Intent flist = new Intent(this, ListViewFriendsActivity.class);
                    // TODO: Aca deberia recibir el nombre de usuario.
                    flist.putExtra("nombre", "nombre-cualquiera");
                    startActivity(flist);
                break;
            case NetworkService.ERROR:
                // handle the error;
                AlertDialog alerta = new AlertDialog.Builder(this).create();
                alerta.setTitle("Datos incorrectos");
                alerta.setMessage("El password o nombre ingresados son incorrrectos");
                alerta.setButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Hacer algo aca?
                    }
                });
                alerta.setIcon(R.drawable.noo);
                alerta.show();
                Log.e("onreceiveresult", "salto un error en login");
                break;
        }
    }

}

package fiuba.mensajero;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class SignInActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
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

        AlertDialog alerta = new AlertDialog.Builder(this).create();
        alerta.setTitle("Registro completo");
        alerta.setMessage("Volviendo a la pantalla principal, oprima log in para ingresar");
        alerta.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alerta.setIcon(R.drawable.okk);
        alerta.show();

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
}

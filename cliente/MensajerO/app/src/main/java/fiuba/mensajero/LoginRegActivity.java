package fiuba.mensajero;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.view.View;

public class LoginRegActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg);

    }

    //* handler para el boton de Terminar
    public void handTerminar(View view) {
        boolean passCorrecto= false;

        if(passCorrecto) {
            //Entrar, mostrar lista de amigos..
        }
        else {
            AlertDialog alerta = new AlertDialog.Builder(this).create();
            alerta.setTitle("Datos incorrectos");
            alerta.setMessage("El password o nombre ingresados son incorrrectos");
            alerta.setButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // aquí puedes añadir funciones
                }
            });
            alerta.setIcon(R.drawable.noo);
            alerta.show();
        }
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
}

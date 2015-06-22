package fiuba.mensajero;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Activity que se muestra en pantalla al inciar la app. Otorga la opcion de loguearse, registrarse o configuracion
 */
public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        if (intent.hasExtra("user")) {
            Intent intent2 = new Intent(this, LoginRegActivity.class);
            intent2.putExtra("user", intent.getStringExtra("user"));
            intent2.putExtra("password", intent.getStringExtra("password"));
            startActivity(intent2);
            finish();
        }
    }

    /**
     * Invoca a la activity para loguearse
     * @param view boton que invoca el metodo
     */
    public void changeActivityLogin(View view) {
        startActivity(new Intent(this, LoginRegActivity.class));
    }

    /**
     * Invoca a la activity para registrarse
     * @param view boton que invoca el metodo
     */    public void changeActivitySign(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    /**
     * Invoca a la activity para configuracion
     * @param view boton que invoca el metodo
     */    public void changeActivityConfig(View view) {
        startActivity(new Intent(this, ConfigActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    /**
     * Borra todos los datos guardados localmennte e inicia LoginActivity (usado para desconectarse)
     * @param context Contexto actual
     */
    public static void logout(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        SharedPreferences sharedPref = context.getSharedPreferences("appdata", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("user");
        editor.remove("token");
        editor.remove("password");
        editor.remove("foto");
        editor.remove("nombre");
        editor.remove("telefono");
        editor.remove("email");
        editor.commit();
        context.startActivity(intent);
    }
}

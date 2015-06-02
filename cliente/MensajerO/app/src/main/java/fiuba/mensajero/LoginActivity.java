package fiuba.mensajero;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    //* handler para el boton de Log In
    public void changeActivityLogin(View view) {
        startActivity(new Intent(this, LoginRegActivity.class));
    }

    //* handler para el boton de Sign In
    public void changeActivitySign(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    //* handler para el boton de Config
    public void changeActivityConfig(View view) {
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

    //hacer logout
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

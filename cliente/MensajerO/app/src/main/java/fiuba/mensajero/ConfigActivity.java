package fiuba.mensajero;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Activity de configuracion. Permite cambiar IP del servidor.
 */
public class ConfigActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        SharedPreferences sharedPref= getSharedPreferences("appdata", 0);
        String ip = sharedPref.getString("ip", null);
        if (ip != null) {
            EditText et = (EditText) findViewById(R.id.editTextconfig);
            et.setText(ip);
        }
    }

    /**
     * Guarda la ip del servidor especificada en el cuadro de texto
     * @param view vista que llama al metodo
     */
    public void handGuardar(View view) {

        final EditText ip = (EditText) findViewById(R.id.editTextconfig);
        String ipserver = ip.getText().toString();

        try
        {
            SharedPreferences sharedPref= getSharedPreferences("appdata", 0);
            SharedPreferences.Editor editor= sharedPref.edit();
            editor.putString("ip", ipserver);
            editor.commit();

            AlertDialog alerta = new AlertDialog.Builder(this).create();
            alerta.setTitle("Cambios realizados");
            alerta.setMessage("Configuracion guardada exitosamente");
            alerta.setButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alerta.show();
        }
        catch (Exception ex)
        {
            Log.e("Archivo", "Error al escribir archivo de ip de servidor");
            AlertDialog alerta = new AlertDialog.Builder(this).create();
            alerta.setTitle("Error al guardar la configuracion");
            alerta.setMessage("Cambios no guardados");
            alerta.setButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alerta.show();
        }

    }
}

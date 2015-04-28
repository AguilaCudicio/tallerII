package fiuba.mensajero;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.OutputStreamWriter;

public class ConfigActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
    }

    //* handler para el boton de Guardar
    public void handGuardar(View view) {

        final EditText ip = (EditText) findViewById(R.id.editTextconfig);
        String ipserver = ip.getText().toString();

        try
        {
            OutputStreamWriter fout= new OutputStreamWriter(openFileOutput("ipserver.txt", Context.MODE_PRIVATE));
            fout.write(ipserver);
            fout.close();
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

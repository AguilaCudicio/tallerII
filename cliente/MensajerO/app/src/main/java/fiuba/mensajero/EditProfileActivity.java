package fiuba.mensajero;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;


public class EditProfileActivity extends ActionBarActivity implements MyResultReceiver.Receiver {
    private static int RESULT_LOAD_IMG = 1;
    String imgString, nombre, password, email, telefono;
    public MyResultReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        imgString = null;
        //mostrar en los edittext la info actual (excepto password)
       /* SharedPreferences sharedPref= getSharedPreferences("appdata", 0);
        String nombre = sharedPref.getString("nombre", null);
        if (nombre != null) {
            EditText nom = (EditText) findViewById(R.id.editText);
            nom.setText(nombre);
        }*/
    }


    //* handler para el boton de cargar imagen.
    // Crea un intent de galería
    public void uploadImage(View view) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imgDecodableString;
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.uploadimageview);

                //Cambiar el segundo argumento para redimensionar a un tamaño mas grande o mas chico
                Bitmap bm = BitmapUtilities.getResizedBitmap(imgDecodableString,1000);
                imgView.setImageBitmap(bm);
                imgString = BitmapUtilities.bitmapToString(bm);

            } else {
                Toast.makeText(this, "Tiene que seleccionar una imagen",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Se ha producido un error!", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isEmpty(String s) {
        return s.trim().length() == 0;
    }

    public void saveChanges(View view) {
        //obtengo los valores a guardar
        EditText et = (EditText) findViewById(R.id.editTextPassword);
        password = et.getText().toString();
        et = (EditText) findViewById(R.id.editText);
        nombre = et.getText().toString();
        et = (EditText) findViewById(R.id.editTextTelPE);
        telefono = et.getText().toString();
        et = (EditText) findViewById(R.id.editTextMailPEd);
        email = et.getText().toString();

        //debo mandar esta info al servidor antes de guardarla localmente
        Intent intent = new Intent(this, NetworkService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("command", "editProfile");
        intent.putExtra("password", password);
        intent.putExtra("nombre", nombre);
        intent.putExtra("telefono", telefono);
        intent.putExtra("email", email);
        intent.putExtra("foto", imgString);
        startService(intent);
    }

        public void saveLocalChanges() {
        //guardar la info necesaria localmente
        SharedPreferences sharedPref= getSharedPreferences("appdata", 0);
        SharedPreferences.Editor editor= sharedPref.edit();
        if (!isEmpty(nombre))
            editor.putString("nombre", nombre);
        if (!isEmpty(password))
            editor.putString("password", password);     //deberia hacer algun chequeo de validez
        if (!isEmpty(email))
            editor.putString("email", email);
        if (!isEmpty(telefono))
            editor.putString("telefono", telefono);
        if (imgString != null)
            editor.putString("foto", imgString);
        editor.commit();

        finish();
    }


    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case NetworkService.RUNNING:
                Log.i("EDITPROFILE", "esta corriendo el servicio de editprofile");
                break;
            case NetworkService.OK:
                Log.d("EDITPROFILE", "se completo la operacion de editprofile ");
                String res = resultData.getString("result");
                if (res == null)
                    Log.e("EDITPROFILE", "error inesperado");
                else if (res.equals("ok")) {
                    //nota si el mensaje es "ok" entonces tuvo exito, si no el mensaje es el problema
                    Log.i("EDITPROFILE", res);
                    AlertDialog alerta = new AlertDialog.Builder(this).create();
                    alerta.setTitle("Perfil modificado");
                    alerta.setMessage("Los cambios en el perfil fueron aceptados");
                    alerta.setButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            saveLocalChanges();
                        }
                    });
                    alerta.show();
                }

                break;
            case NetworkService.ERROR:
                // handle the error;
                Log.e("EDITPROFILE", "salto un error en editprofile");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
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

package fiuba.mensajero;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class EditProfileActivity extends ActionBarActivity {
    private static int RESULT_LOAD_IMG = 1;
    String imgString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        imgString = null;
        //mostrar en los edittext la info actual (excepto password)
        SharedPreferences sharedPref= getSharedPreferences("appdata", 0);
        String nombre = sharedPref.getString("nombre", null);
        if (nombre != null) {
            EditText nom = (EditText) findViewById(R.id.editText);
            nom.setText(nombre);
        }
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
        String imgDecodableString = null;
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
                // Poner en ImageView
                Bitmap bm = BitmapFactory.decodeFile(imgDecodableString);
                imgView.setImageBitmap(bm);
                imgString = ProfileActivity.bitmapToString(bm);

            } else {
                Toast.makeText(this, "Tiene que seleccionar una imagen",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Se ha producido un error!", Toast.LENGTH_LONG).show();
        }

    }

    public void saveChanges(View view) {
        //obtengo los valores a guardar
        EditText pass = (EditText) findViewById(R.id.editTextPassword);
        String password = pass.getText().toString();
        EditText nom = (EditText) findViewById(R.id.editText);
        String nombre = nom.getText().toString();

        //debo mandar esta info al servidor antes de guardarla localmente, pero para probar ahora guardo directamente


        //guardar la info necesaria localmente
        SharedPreferences sharedPref= getSharedPreferences("appdata", 0);
        SharedPreferences.Editor editor= sharedPref.edit();
        editor.putString("nombre", nombre);
        editor.putString("password", password);     //deberia hacer algun chequeo de validez
        if (imgString != null)
            editor.putString("foto", imgString);
        editor.commit();

        //para recuperar despues la info seria asi por ej
        //        SharedPreferences sharedPref= getSharedPreferences("appdata", 0);
        //        String nombre = sharedPref.getString("nombre", null);

        finish();
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
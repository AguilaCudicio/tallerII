package fiuba.mensajero;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;


public class ProfileActivity extends ActionBarActivity {

    ImageButton avatar;
    ProfileData profile;
    boolean owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        addListenerProfile();
        Intent intent = getIntent();
        owner = !intent.hasExtra("nombre");
  /*     if(intent.hasExtra("nombre")) {
            //si tiene info adicional el intent se trata del perfil de otra persona
            owner = false
        }
        else {
            //si no tiene info se trata del perfil del usuario logueado
            SharedPreferences sharedPref= getSharedPreferences("appdata", 0);
            String nombre = sharedPref.getString("nombre", null);
            String foto = sharedPref.getString("foto", null);
            profile = new ProfileData(nombre, foto);
            showProfile();
        }
*/
    }

    public static String bitmapToString(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap stringToBitmap(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void getProfile() {
        if(!owner) {
            //si es el perfil de otra persona hago request al server
        }
        else {
            //si no se trata del perfil del dueño de la app
            SharedPreferences sharedPref= getSharedPreferences("appdata", 0);
            String nombre = sharedPref.getString("nombre", null);
            String foto = sharedPref.getString("foto", null);
            profile = new ProfileData(nombre, foto);
            showProfile();
        }


    }
    public void showProfile() {
        //mostrar el profile
        String foto = profile.getFoto();
        String nombre = profile.getNombre();
        avatar = (ImageButton) findViewById(R.id.imageButtonProfile);
        if (foto != null) {
            Bitmap bm = stringToBitmap(foto);
            avatar.setImageBitmap(bm);
        }
        else {
            Resources resources = this.getResources();
            avatar.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.noimage));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getProfile();
        Log.i("RESUMO ACTIVITY", "tuc");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    public void addListenerProfile() {

        avatar = (ImageButton) findViewById(R.id.imageButtonProfile);
        avatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                final Dialog profileDialog = new Dialog(ProfileActivity.this);
                profileDialog.setTitle("Foto de perfil");

                profileDialog.setContentView(getLayoutInflater().inflate(R.layout.profilepicture_layout, null));

                Button button = (Button) profileDialog.findViewById(R.id.ButtonProfile);
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profileDialog.cancel();
                    }
                });
                profileDialog.show();
            }
        });

    }

   public void changeActivityEditProfile(View view) {
       Intent flist = new Intent(this, EditProfileActivity.class);
       startActivity(flist);
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

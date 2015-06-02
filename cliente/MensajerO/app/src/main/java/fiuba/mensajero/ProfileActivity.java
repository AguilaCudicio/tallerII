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
import android.widget.ImageView;
import android.widget.TextView;

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
    }

    public void getProfile() {
        if(!owner) {
            //si es el perfil de otra persona hago request al server
        }
        else {
            //si no se trata del perfil del dueño de la app, lo obtengo localmente
            SharedPreferences sharedPref= getSharedPreferences("appdata", 0);
            String nombre = sharedPref.getString("nombre", null);
            String foto = sharedPref.getString("foto", null);
            String ultimoacceso = sharedPref.getString("ultimoacceso", null);
            String telefono = sharedPref.getString("telefono", null);
            String email = sharedPref.getString("email", null);
            profile = new ProfileData(nombre, foto, ultimoacceso, telefono, email);
            showProfile();
        }


    }
    public void showProfile() {
        TextView tv = (TextView) findViewById(R.id.textViewNameProfile);
        String text;
        if (profile.getNombre() != null) {
            text = "Nombre: " + profile.getNombre();
            tv.setText(text);
        }
        if (profile.getUltimoacceso() != null) {
            tv = (TextView) findViewById(R.id.textViewDate);
            text = "Ultimo acceso: " + profile.getUltimoacceso();
            tv.setText(text);
        }
        if (profile.getEmail() != null) {
            tv = (TextView) findViewById(R.id.textViewMailProfile);
            text = "Email: " + profile.getEmail();
            tv.setText(text);
        }
        if (profile.getTelefono() != null) {
            tv = (TextView) findViewById(R.id.textViewTelProfile);
            text = "Telefono: " + profile.getTelefono();
            tv.setText(text);
        }
        String foto = profile.getFoto();
        avatar = (ImageButton) findViewById(R.id.imageButtonProfile);
        if (foto != null) {
            Bitmap bm = BitmapUtilities.stringToBitmap(foto);
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
                String foto = profile.getFoto();
                profileDialog.setContentView(getLayoutInflater().inflate(R.layout.profilepicture_layout, null));

                ImageView image = (ImageView) profileDialog.findViewById(R.id.ImageLargeView);
                if (foto != null && image !=null) {
                    Bitmap bm = BitmapUtilities.stringToBitmap(foto);
                    image.setImageBitmap(bm);
                }

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

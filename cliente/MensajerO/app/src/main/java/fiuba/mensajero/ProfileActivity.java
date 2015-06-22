package fiuba.mensajero;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
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
import java.util.ArrayList;


public class ProfileActivity extends ActionBarActivity implements MyResultReceiver.Receiver {

    ImageButton avatar;
    ProfileData profile;
    public MyResultReceiver mReceiver;
    String user2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        addListenerProfile();
        Intent intent = getIntent();
        if (intent.hasExtra("user2")) {
            user2 = intent.getStringExtra("user2");
            View b = findViewById(R.id.buttonChangeProfile);
            b.setVisibility(View.GONE);
        } else {
            user2 = null;
        }
        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    public void onResume() {
        super.onResume();
        getProfile();
    }

    public void getProfile() {
        Intent intent = new Intent(this, NetworkService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("command", "getProfile");
        intent.putExtra("user2", user2);
        startService(intent);

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
        if (profile.getUbicacion() != null) {
            tv = (TextView) findViewById(R.id.textViewLocationProfile);
            text = "Ubicacion: " + profile.getUbicacion();
            tv.setText(text);
        }
        String foto = profile.getFoto();
        avatar = (ImageButton) findViewById(R.id.imageButtonProfile);
        if (foto != null) {
            try {
                Bitmap bm = BitmapUtilities.stringToBitmap(foto);
                avatar.setImageBitmap(bm);
            }
            catch (Exception e) {
                Resources resources = this.getResources();
                avatar.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.noimage));
            }
        }
        else {
            avatar.setImageResource(R.drawable.noimage);

        }
    }

    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case NetworkService.RUNNING:
                Log.i("PROFILEACT", "esta corriendo el servicio de getprofile");
                //aca se podria mostrar algo mientras el servicio esta corriendo
                break;
            case NetworkService.OK:
                Log.d("PROFILEACT", "se completo la operacion ");
                profile = resultData.getParcelable("result");
                if (profile == null) {
                    Log.e("PROFILEACT", "error inesperado");
                }
                else {
                    showProfile();
                }
                break;
            case NetworkService.ERROR:
                AlertDialog alerta = new AlertDialog.Builder(this).create();
                alerta.setTitle("Error");
                String err = resultData.getString("error");
                alerta.setMessage(err);
                alerta.setButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                if (!isFinishing())
                     alerta.show();
                Log.e("PROFILEACT", err);
                break;
        }
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

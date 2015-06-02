package fiuba.mensajero;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;



public class FriendProfileActivity extends ActionBarActivity {

    ImageButton avatar;
    //TODO: rellenar profile con info del contacto
    ProfileData profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
    }

    public void addListenerProfile() {

        avatar = (ImageButton) findViewById(R.id.imageButtonProfile);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                final Dialog profileDialog = new Dialog(FriendProfileActivity.this);
                profileDialog.setTitle("Foto de perfil");
                String foto = profile.getFoto();
                profileDialog.setContentView(getLayoutInflater().inflate(R.layout.profilepicture_layout, null));

                ImageView image = (ImageView) profileDialog.findViewById(R.id.ImageLargeView);
                if (foto != null && image != null) {
                    Bitmap bm = BitmapUtilities.stringToBitmap(foto);
                    image.setImageBitmap(bm);
                }

                Button button = (Button) profileDialog.findViewById(R.id.ButtonProfile);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profileDialog.cancel();
                    }
                });
                profileDialog.show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_profile, menu);
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

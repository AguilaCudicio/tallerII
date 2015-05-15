package fiuba.mensajero;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageButton;


public class ProfileActivity extends ActionBarActivity {

    ImageButton avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        addListenerProfile();
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

   public void changeActivityEditProfile() {
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

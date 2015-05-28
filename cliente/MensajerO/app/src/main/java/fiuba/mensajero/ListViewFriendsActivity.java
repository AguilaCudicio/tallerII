package fiuba.mensajero;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import static android.app.AlertDialog.*;

public class ListViewFriendsActivity extends ListActivity implements MyResultReceiver.Receiver {
    public MyResultReceiver mReceiver;
    private ArrayList<UserData> contactos;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_listviewfriendsactivity);
        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        getUsersOnline(null, null);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        AlertDialog alertDialog = new Builder(this).create();

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("id", contactos.get(position).getId());
        startActivity(intent);

        super.onListItemClick(l, v, position, id);
    }

    public void getUsersOnline(String user, String token) {
        Intent intent = new Intent(this, NetworkService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("command", "getListaConectados");
        intent.putExtra("token", token);
        intent.putExtra("user", user);
        startService(intent);
    }

    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case NetworkService.RUNNING:
                Log.i("onreceiveresult", "esta corriendo el servicio");
                //aca se podria mostrar algo mientras el servicio esta corriendo
                break;
            case NetworkService.OK:
                Log.d("onreceiveresult", "se completo la operacion ");

                ArrayList<UserData> list = resultData.getParcelableArrayList("result");

                if (list == null)
                  Log.e("onreceiveresult lista", "error inesperado");
                else {
                    contactos = list;
                    AdaptFriends adapt = new AdaptFriends(this, list);
                    setListAdapter(adapt);
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
                alerta.show();
                Log.e("onreceiveresult", err);
                break;
        }
    }


    //* handler para el boton de Perfil
    public void changeActivityProfile(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

}

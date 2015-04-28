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
    static String EXTRA_MESSAGE ="";
    private String[] contactos;
    public MyResultReceiver mReceiver;

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
        intent.putExtra(EXTRA_MESSAGE, contactos[position]);
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

              /*  contactos = new String[] { "conectadoJuan", "conectadoFernando", "conectadoMaria", "conectadoRoberto",
                        "conectadoAna", "Irene", "Matilda", "Alma",
                        "Francisco", "Adrian", "Elena", "Jesica", "Roberto",
                        "Matias", "Soledad", "Victoria", "Nadia", "Elsa" };
                AdaptFriends adapt = new AdaptFriends (this, contactos);
                setListAdapter(adapt);*/


                ArrayList<String> list = resultData.getStringArrayList("result");
                if (list == null)
                  contactos = new String[] { "conectadoValores", "conectadoDefault", "para testeo" };
                else {
                    contactos = new String[list.size()];
                    contactos = list.toArray(contactos);
                }
                AdaptFriends adapt = new AdaptFriends (this, contactos);
                setListAdapter(adapt);
                break;
            case NetworkService.ERROR:
                // handle the error;
                Log.e("onreceiveresult", "salto un error");

                break;
        }
    }

}
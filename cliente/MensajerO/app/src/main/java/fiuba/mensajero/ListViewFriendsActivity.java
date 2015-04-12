package fiuba.mensajero;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import static android.app.AlertDialog.*;

public class ListViewFriendsActivity extends ListActivity {
    static String EXTRA_MESSAGE ="";
    String[] contactos;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        /* TODO: Aca deberian ir las personas conectadas*/
        contactos = new String[] { "conectadoJuan", "conectadoFernando", "conectadoMaria", "conectadoRoberto",
                "conectadoAna", "Irene", "Matilda", "Alma",
                "Francisco", "Adrian", "Elena", "Jesica", "Roberto",
                "Matias", "Soledad", "Victoria", "Nadia", "Elsa" };
        AdaptFriends adapt = new AdaptFriends (this, contactos);
        setListAdapter(adapt);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        AlertDialog alertDialog = new Builder(this).create();

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "Conversacion con "+contactos[position]);
        startActivity(intent);

        super.onListItemClick(l, v, position, id);
    }

}
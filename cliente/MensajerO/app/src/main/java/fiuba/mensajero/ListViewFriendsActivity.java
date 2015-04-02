package fiuba.mensajero;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;

public class ListViewFriendsActivity extends ListActivity {
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        /* TODO: Aca deberian ir las personas conectadas*/
        String[] contactos = new String[] { "conectadoJuan", "conectadoFernando", "conectadoMaria", "conectadoRoberto",
                "conectadoAna", "Irene", "Matilda", "Alma",
                "Francisco", "Adrian", "Elena", "Jesica", "Roberto",
                "Matias", "Soledad", "Victoria", "Nadia", "Elsa" };
        AdaptFriends adapt = new AdaptFriends (this, contactos);
        setListAdapter(adapt);
    }

}
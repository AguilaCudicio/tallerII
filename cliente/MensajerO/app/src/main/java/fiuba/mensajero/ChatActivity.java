package fiuba.mensajero;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity para visualizar y realizar conversaciones entre usuarios.
 */
public class ChatActivity extends ActionBarActivity implements MyResultReceiver.Receiver {

    MyFragment fragment;
    public MyResultReceiver mReceiver;
    private UserData contacto;
    private String nombre;
    private Handler handler;
    private int interval;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        fragment = new MyFragment();
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        handler = new Handler();
        interval = 10000; //10 seg
        Intent intent = getIntent();
        contacto = intent.getParcelableExtra("contacto");
        SharedPreferences sharedPref= getSharedPreferences("appdata", 0);
        nombre = sharedPref.getString("nombre", "Yo");
    }

    Runnable msgUpdater = new Runnable() {
        @Override
        public void run() {
            getMessages();
            handler.postDelayed(msgUpdater, interval);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        msgUpdater.run();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(msgUpdater);
    }

    public void onStop() {
        super.onStop();
        handler.removeCallbacks(msgUpdater);
    }

    /**
     * Iniciar el servicio para obtencion de los mensajes de la conversacion
     */
    public void getMessages() {
        Intent intent = new Intent(this, NetworkService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("command", "getMessages");
        intent.putExtra("user2", contacto.getId());

        TextView tv = (TextView) findViewById(R.id.textView13);
        tv.setText(contacto.getNombre());

        ImageView iv = (ImageView) findViewById(R.id.imageButtonProfileFriend);
        String foto = contacto.getFoto();
        if (foto != null && !isEmpty(foto) ) {
            Bitmap bm = BitmapUtilities.stringToBitmap(foto);
            iv.setImageBitmap(bm);
        } else {
            iv.setImageResource(R.drawable.noimage);
        }
        startService(intent);
    }

    /**
     * Procesa la respuesta del servicio de red. Notifica el resultado con dialogos.
     * @param resultCode codigo de error del resultado
     * @param resultData datos de la respuesta
     */
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case NetworkService.RUNNING:
                Log.i("ChatActivity", "esta corriendo el servicio chat msg");
                //aca se podria mostrar algo mientras el servicio esta corriendo
                break;
            case NetworkService.OK_MSG:
                String res = resultData.getString("result");
                if (res.equals("ok")) {
                    Log.i("ChatActivity", "mensaje enviado");
                }
                break;
            case NetworkService.OK:
                Log.d("ChatActivity", "se completo la operacion de chat msg ");
                ArrayList<MessageData> list = resultData.getParcelableArrayList("result");
                if (list == null)
                    Log.e("ChatActivity ", "error inesperado");
                else {
                    fragment.clearMessages();
                    for (int i = list.size()-1; i >= 0; i--) {
                        String id = list.get(i).getId();
                        if (id.equals(contacto.getId()))
                            fragment.addMessage(contacto.getNombre(), list.get(i).getMessage());
                        else
                            fragment.addMessage(nombre, list.get(i).getMessage());
                        TextView tv = (TextView) findViewById(R.id.textViessw13);
                        tv.setText(list.get(i).getTime());
                    }
                    //Actualizar la vista del Fragment para que se vean los nuevos mensajes.
                    fragment.getView().requestLayout();
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

    /**
     * Iniciar el servicio para enviar el mensaje del cuadro de texto al apretar el boton de enviar
     * @param view boton que invoca el metodo
     */
    public void handEnviar(View view) {
        final EditText msg = (EditText) findViewById(R.id.edit_message);
        String message = msg.getText().toString();
        if (!message.isEmpty()) {
            Intent intent = new Intent(this, NetworkService.class);
            intent.putExtra("receiver", mReceiver);
            intent.putExtra("command", "sendMessage");
            intent.putExtra("user2", contacto.getId());
            intent.putExtra("message", message);
            msg.setText("");
            startService(intent);
            getMessages();
        }
        else {
            Log.d("ChatActivity", "no se puede enviar un mensaje vacio");
        }
    }

    boolean isEmpty(String s) {
        return  s.trim().length() == 0;
    }

    /**
     * Iniciar la activity de profile para mostrar el perfil del usuario
     * @param view boton que invoca al metodo
     */
    public void showFriendInfo(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("user2", contacto.getId());
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

class MyFragment extends ListFragment {
    private List<ListViewItem> mItems ;

    public MyFragment() {
        mItems= new ArrayList<ListViewItem>();
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources resources = getResources();
        setListAdapter(new ListViewDemoAdapter(getActivity(), mItems));
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.footer_chat, container, false); }

    public void addMessage(String friend,String mess) {
        mItems.add(new ListViewItem(friend,mess));
    }

    public void clearMessages() {
        mItems.clear();
    }

   //public void onListItemClick(ListView listView, View view, int position, long id) {
    //    ...
    //}
}

class ListViewItem {
    public final String title;
    public final String description;
    public ListViewItem(String title, String description) {
        this.title = title;
        this.description = description;
    }
}


class ListViewDemoAdapter extends ArrayAdapter<ListViewItem> {

    public ListViewDemoAdapter(Context context, List<ListViewItem> items) {
        super(context, R.layout.listview_item, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // update the item view
        ListViewItem item = getItem(position);
        viewHolder.tvTitle.setText(item.title);
        viewHolder.tvDescription.setText(item.description);
        return convertView;
    }

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvDescription;
    }

}
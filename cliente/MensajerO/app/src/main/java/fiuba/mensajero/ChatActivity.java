package fiuba.mensajero;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends ActionBarActivity implements MyResultReceiver.Receiver {

    public MyResultReceiver mReceiver;
    private UserData contacto;
    private Handler handler;
    private int interval;
    private boolean pauseUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        handler = new Handler();
        interval = 10000; //10 seg
        Intent intent = getIntent();
        contacto = intent.getParcelableExtra("contacto");

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

    public void getMessages() {
        Intent intent = new Intent(this, NetworkService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("command", "getMessages");
        intent.putExtra("user2", contacto.getId());
        startService(intent);
    }


    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case NetworkService.RUNNING:
                Log.i("onreceiveresult", "esta corriendo el servicio chat msg");
                //aca se podria mostrar algo mientras el servicio esta corriendo
                break;
            case NetworkService.OK:
                Log.d("onreceiveresult", "se completo la operacion de chat msg ");
                if (pauseUpdate) {
                    String res = resultData.getString("result");
                    if (res.equals("ok")) {
                        pauseUpdate = false;
                        msgUpdater.run();
                    }
                }
                else {
                    ArrayList<MessageData> list = resultData.getParcelableArrayList("result");
                    if (list == null)
                        Log.e("onreceiveresult lista", "error inesperado");
                    else {
                        MyFragment fragment = new MyFragment();
                        for (int i = 0; i < list.size(); i++) {
                            //TODO agregar el tiempo list.get(i).getTime()
                            fragment.addMessage(list.get(i).getId(), list.get(i).getMessage());
                            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
                        }
                    }
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


    public void handEnviar(View view) {
        //no hace nada por ahora
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
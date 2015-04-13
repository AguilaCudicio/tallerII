package fiuba.mensajero;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get the message from the intent
        Intent intent = getIntent();
        String nombre = intent.getStringExtra(ListViewFriendsActivity.EXTRA_MESSAGE);

        MyFragment fragment = new MyFragment();
        //TODO: Mensajes de prueba, cambiarlos.
        fragment.addMessage(nombre,"hola");
        fragment.addMessage(nombre,"chau");
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        // initialize the items list
        Resources resources = getResources();
        setListAdapter(new ListViewDemoAdapter(getActivity(), mItems));
    }

    public void addMessage(String friend,String mess) {
        mItems.add(new ListViewItem(friend,mess));
    }

   //public void onListItemClick(ListView listView, View view, int position, long id) {
    //    ...
    //}
}
class ListViewItem {
    public final String title; // the text for the ListView item title
    public final String description; // the text for the ListView item description
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
    /**
     * The view holder design pattern prevents using findViewById()
     * repeatedly in the getView() method of the adapter.
     *
     */
    private static class ViewHolder {
        TextView tvTitle;
        TextView tvDescription;
    }
}
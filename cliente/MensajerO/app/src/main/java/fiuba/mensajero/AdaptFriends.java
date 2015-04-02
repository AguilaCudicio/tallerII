package fiuba.mensajero;



import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AdaptFriends extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public AdaptFriends(Context context, String[] contactos) {
        super(context, R.layout.rowlayout, contactos);
        this.context = context;
        this.values = contactos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values[position]);

        String s = values[position];
        if (s.startsWith("conectado")) {
            imageView.setImageResource(R.drawable.okk);
        } else {
            imageView.setImageResource(R.drawable.noo);
        }

        return rowView;
    }
}
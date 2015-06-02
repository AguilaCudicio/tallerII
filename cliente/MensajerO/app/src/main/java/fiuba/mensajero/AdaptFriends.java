package fiuba.mensajero;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class AdaptFriends extends ArrayAdapter<UserData> {
    private final Context context;

    public AdaptFriends(Context context, ArrayList<UserData> contactos) {
        super(context, R.layout.rowlayout, contactos);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserData contacto = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setText(contacto.getNombre());

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        if (contacto.getId().equals("broadcast")) {
            rowView.setBackgroundColor(Color.argb(228,135,159,255));
        }
        else {
            if (contacto.isConectado()) {
                imageView.setImageResource(R.drawable.okk);
            } else {
                imageView.setImageResource(R.drawable.noo);
            }
        }
        if (contacto.hasNewMessages()) {
           ImageView imageViewAlert = (ImageView) rowView.findViewById(R.id.iconNewMsg);
           imageViewAlert.setImageResource(R.drawable.warning);
           rowView.setBackgroundColor(Color.argb(50,0,0,20));
        }

        return rowView;
    }
}
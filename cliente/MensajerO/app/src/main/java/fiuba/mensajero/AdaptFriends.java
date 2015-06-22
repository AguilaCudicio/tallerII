package fiuba.mensajero;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Extension de ArrayAdapter para manejar el tipo de dato definido UserData
 */
public class AdaptFriends extends ArrayAdapter<UserData> {
    /**
     * Contexto actual
     */
    private final Context context;

    /**
     * Constructor
     * @param context Contexto actual
     * @param contactos Array de tipo UserData que contiene la informacion de contactos
     */
    public AdaptFriends(Context context, ArrayList<UserData> contactos) {
        super(context, R.layout.rowlayout, contactos);
        this.context = context;
    }

    /**
     * Metodo que indica que mostrar en pantalla por cada subitem de la lista
     * @param position Posicion en el array
     * @param convertView View a modificar
     * @param parent View parent a la que pertenece
     * @return Subitem de la lista
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserData contacto = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setText(contacto.getNombre());

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        ImageView fotoview = (ImageView) rowView.findViewById(R.id.iconFriend);

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

        String foto = contacto.getFoto();
        if (foto != null && !isEmpty(foto) ) {
            Bitmap bm = BitmapUtilities.stringToBitmap(foto);
            fotoview.setImageBitmap(bm);
        } else {
            fotoview.setImageResource(R.drawable.noimage);
        }


        return rowView;
    }

    private boolean isEmpty(String s) {
        return  s.trim().length() == 0;
    }

}
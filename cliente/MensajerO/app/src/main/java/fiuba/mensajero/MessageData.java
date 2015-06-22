package fiuba.mensajero;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Clase contenedora de datos de un mensaje de una conversacion. Implementa parcelable para posibilitar la comunicacion del objeto entre activities.
 */
public class MessageData implements Parcelable {

    private  String id;
    private  String time;
    private  String message;

    /**
     * Constructor
     * @param id id del usuario emisor del mensaje
     * @param time tiempo en que se manda el mensaje
     * @param message mensaje enviado
     */
    public MessageData(String id, String time, String message) {
        this.id = id;
        this.time = time;
        this.message = message;
    }

    /**
     * Obtener el id del usuario emisor
     * @return String con el id del emisor
     */
    public String getId() {
        return id;
    }

    /**
     * Obtener el tiempo del mensaje enviado
     * @return String con el tiempo del mensaje
     */
    public String getTime() {
        return time;
    }

    /**
     * Obtener mensaje enviado
     * @return String con el mensaje
     */
    public String getMessage() {
        return message;
    }


    public MessageData(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.time = in.readString();
        this.message = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MessageData> CREATOR = new Parcelable.Creator<MessageData>() {
        public MessageData createFromParcel(Parcel in) {
            return new MessageData(in);
        }

        public MessageData[] newArray(int size) {
            return new MessageData[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(time);
        dest.writeString(message);
    }
}
package fiuba.mensajero;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Clase contenedora de datos de un contacto. Implementa parcelable para posibilitar la comunicacion del objeto entre activities.
 */
public class UserData implements Parcelable {

    private  String id;
    private  String nombre;
    private  boolean conectado;
    private  boolean nuevomsg;
    private  String fotochica;

    /**
     * Constructor
     * @param id id del usuario
     * @param nombre nombre del usuario
     * @param conectado true si este usuario esta conectado
     * @param nuevomsg true si hay nuevos mensajes de este usuario
     * @param fotochica foto baja resolucion del usuario
     */
    public UserData(String id, String nombre, boolean conectado, boolean nuevomsg, String fotochica) {
        this.id = id;
        this.nombre = nombre;
        this.conectado = conectado;
        this.nuevomsg = nuevomsg;
        this.fotochica = fotochica;
    }

    /**
     * Obtiene id del usuario
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene nombre del usuario
     * @return String
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Estado de conexion del usuario
     * @return true si el usuario esta conectado
     */
    public boolean isConectado() {
        return conectado;
    }

    /**
     * Mensjes nuevos del usuario
     * @return true si el usuario ha realizado nuevos mensajes
     */
    public boolean hasNewMessages() { return nuevomsg; }

    /**
     * Obtiene foto del usuario
     * @return String
     */
    public String getFoto() {
        return fotochica;
    }

    public UserData(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.nombre = in.readString();
        this.conectado = (in.readInt() != 0);
        this.nuevomsg = (in.readInt() != 0);
        this.fotochica = in.readString();

    }

    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<UserData> CREATOR = new Parcelable.Creator<UserData>() {
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nombre);
        dest.writeInt( conectado ? 1 : 0 );
        dest.writeInt( nuevomsg ? 1 : 0 );
        dest.writeString(fotochica);
    }
}

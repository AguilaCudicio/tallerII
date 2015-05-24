package fiuba.mensajero;


import android.os.Parcel;
import android.os.Parcelable;

public class UserData implements Parcelable {

    private  String id;
    private  String nombre;
    private  boolean conectado;

    public UserData(String id, String nombre, boolean conectado) {
        this.id = id;
        this.nombre = nombre;
        this.conectado = conectado;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isConectado() {
        return conectado;
    }

    public UserData(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.nombre = in.readString();
        this.conectado = (in.readInt() != 0);
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
    }
}

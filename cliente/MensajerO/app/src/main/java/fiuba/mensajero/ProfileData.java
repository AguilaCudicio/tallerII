package fiuba.mensajero;

import android.os.Parcel;
import android.os.Parcelable;


public class ProfileData implements Parcelable {

    //todo poner los datos de profile que vamos a usar

    private  String nombre;
    private  String foto;

    public ProfileData(String nombre, String foto) {
        this.nombre = nombre;
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFoto() {
        return foto;
    }

    public ProfileData(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.nombre = in.readString();
        this.foto = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ProfileData> CREATOR = new Parcelable.Creator<ProfileData>() {
        public ProfileData createFromParcel(Parcel in) {
            return new ProfileData(in);
        }

        public ProfileData[] newArray(int size) {
            return new ProfileData[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(foto);
    }
}
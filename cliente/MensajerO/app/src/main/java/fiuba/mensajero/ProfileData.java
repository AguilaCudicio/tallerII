package fiuba.mensajero;

import android.os.Parcel;
import android.os.Parcelable;


public class ProfileData implements Parcelable {

    //todo poner los datos de profile que vamos a usar

    private String nombre;
    private String foto;
    private String ultimoacceso;
    private String telefono;
    private String email;

    public ProfileData(String nombre, String foto, String ultimoacceso, String telefono, String email) {
        this.nombre = nombre;
        this.foto = foto;
        this.ultimoacceso = ultimoacceso;
        this.telefono = telefono;
        this.email = email;
    }


    public String getNombre() {
        return nombre;
    }

    public String getFoto() {
        return foto;
    }

    public String getUltimoacceso() { return ultimoacceso; }

    public String getTelefono() { return telefono; }

    public String getEmail() { return email; }

    public ProfileData(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.nombre = in.readString();
        this.foto = in.readString();
        this.ultimoacceso = in.readString();
        this.telefono = in.readString();
        this.email = in.readString();
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
        dest.writeString(ultimoacceso);
        dest.writeString(telefono);
        dest.writeString(email);
    }
}
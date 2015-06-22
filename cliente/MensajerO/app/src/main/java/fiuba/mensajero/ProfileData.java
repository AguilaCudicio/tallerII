package fiuba.mensajero;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Clase contenedora de datos de un perfil de usuario. Implementa parcelable para posibilitar la comunicacion del objeto entre activities.
 */
public class ProfileData implements Parcelable {
    private String nombre;
    private String foto;
    private String ultimoacceso;
    private String telefono;
    private String email;
    private String ubicacion;

    /**
     * Constructor
     * @param nombre nombre del usuario
     * @param foto foto del usuario
     * @param ultimoacceso fecha y hora de ultimo acceso del usuario
     * @param telefono telefono del usuario
     * @param email email del usuario
     * @param ubicacion ubicacion del usuario
     */
    public ProfileData(String nombre, String foto, String ultimoacceso, String telefono, String email, String ubicacion) {
        this.nombre = nombre;
        this.foto = foto;
        this.ultimoacceso = ultimoacceso;
        this.telefono = telefono;
        this.email = email;
        this.ubicacion = ubicacion;
    }

    /**
     * Obtener el nombre del usuario
     * @return String
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtener foto del usuario
     * @return String
     */
    public String getFoto() {
        return foto;
    }

    /**
     * Obtener fecha y hora de ultimo acceso del usuario
     * @return String
     */
    public String getUltimoacceso() { return ultimoacceso; }

    /**
     * Obtener telefono del usuario
     * @return String
     */
    public String getTelefono() { return telefono; }

    /**
     * Obtener email del usuario
     * @return String
     */
    public String getEmail() { return email; }

    /**
     * Obtener ubicacion del usuario
     * @return String
     */
    public String getUbicacion() {  return ubicacion; }

    public ProfileData(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.nombre = in.readString();
        this.foto = in.readString();
        this.ultimoacceso = in.readString();
        this.telefono = in.readString();
        this.email = in.readString();
        this.ubicacion = in.readString();
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
        dest.writeString(ubicacion);
    }
}
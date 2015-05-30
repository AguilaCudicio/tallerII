package fiuba.mensajero;

import android.os.Parcel;
import android.os.Parcelable;


public class MessageData implements Parcelable {

    private  String id;
    private  String time;
    private  String message;

    public MessageData(String id, String time, String message) {
        this.id = id;
        this.time = time;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

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
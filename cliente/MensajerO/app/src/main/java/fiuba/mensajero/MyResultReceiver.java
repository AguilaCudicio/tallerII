package fiuba.mensajero;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Clase utilizada para permitir que las activities reciban respuesta despues de haber llamado a un servicio
 */
public class MyResultReceiver extends ResultReceiver  {

    private Receiver mReceiver;

    public MyResultReceiver(Handler handler) {
        super(handler);
        // TODO Auto-generated constructor stub
    }

    /**
     * Interfaz a implementar por las activities que requieran respuesta de un servicio
     */
    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);

    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }

}
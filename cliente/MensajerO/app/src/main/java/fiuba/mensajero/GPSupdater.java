package fiuba.mensajero;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Servicio para obtener periodicamente la posicion actual del dispositivo y notificarla al servidor
 */
public class GPSupdater extends Service implements LocationListener, MyResultReceiver.Receiver {
    public MyResultReceiver mReceiver;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    Location location;
    double latitude;
    double longitude;
    protected LocationManager locationManager;
    private int distancegps = 10;   //minima distancia entre updates
    private int intervalgps = 60000; //minimo tiempo entre updates
    private int intervalserver = 10000; // intervalo de updates al server
    private Handler handler;
    private String ubicacion;
    static boolean hideLocation;
    private int connectionTry;


    /**
     * Inicializa variables. Empieza a correr periodicamente los metodos de obtencion e informe de ubicacion
     */
    public void onCreate() {
        super.onCreate();
        latitude = 0;
        longitude = 0;
        ubicacion = "desconocida";
        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(GPSupdater.this);
        handler = new Handler();
        getLocationUpdates();
        gpsUpdater.run();
        hideLocation = false;
        connectionTry = 0;
    }

    /**
     * Ocultar la ubicacion actual. Quedara seteada como "desconocida"
     * @param val true si desea ocultar ubicacion
     */
    public static void hideLocation(boolean val) {
        hideLocation = val;
    }

    public void onDestroy() {
        stopGPSupdate();
        handler.removeCallbacks(gpsUpdater);
    }

    Runnable gpsUpdater = new Runnable() {
        @Override
        public void run() {
            updateLocation();
            handler.postDelayed(gpsUpdater, intervalserver);
        }
    };

    /**
     * Obtiene la direccion a partir de la latitud  y longitud y se la envia al servidor. Si no puede obtenerla o se especifico ocultarla, se envia "desconocida"
     */
    public void updateLocation() {
        Log.d("LONGITUD", String.valueOf(longitude));
        Log.d("LATITUD", String.valueOf(latitude));

        if (hideLocation)
            ubicacion = "desconocida";
        else {
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                ubicacion = "desconocida";
            }

            if (addresses != null && !addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();

                ubicacion = address + ", " + city + ", " + state + ", " + country;
            }
        }
        Log.d("UBICACION", ubicacion);

        Intent intent = new Intent(GPSupdater.this, NetworkService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("command", "editProfile");
        intent.putExtra("ubicacion", ubicacion);
        startService(intent);

    }

    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == NetworkService.OK) {
            connectionTry = 0;
        }
        if (resultCode == NetworkService.ERROR) {
            connectionTry++;
            if (connectionTry >= 2) {
                forceLogout();
            }
        }
    }

    public void forceLogout() {
        ChatActivity.forcelogout = true;
        ListViewFriendsActivity.forcelogout = true;
        EditProfileActivity.forcelogout = true;
        ProfileActivity.forcelogout = true;
    }

    /**
     * Detiene la actualizacion de posicion periodica
     */
    public void stopGPSupdate(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSupdater.this);
        }
    }

    /**
     * Obtiene la longitud y latitud actual del dispositivo
     */
    public void getLocationUpdates() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d("GPSupdater", "No hay proveedores para obtener ubicacion");
            } else {
                if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, distancegps, intervalgps, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza cambios cuando se le informa una nueva ubicacion del proveedor
     * @param location ubicacion proveniente del proveedor
     */
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.d("GPSUPDATER", "recibiendo una nueva ubicacion");
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}

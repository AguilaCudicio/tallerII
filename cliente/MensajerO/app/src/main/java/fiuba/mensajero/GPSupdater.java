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
    private int intervalserver = 15000; // intervalo de updates al server
    private Handler handler;
    private String ubicacion;


    public void onCreate() {
        super.onCreate();
        latitude = 0;
        longitude = 0;
        ubicacion = "casa";
        mReceiver = new MyResultReceiver(new Handler());
        mReceiver.setReceiver(GPSupdater.this);
        handler = new Handler();
        getLocationUpdates();
        gpsUpdater.run();
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


    public void updateLocation() {
        Log.d("LONGITUD", String.valueOf(longitude));
        Log.d("LATITUD", String.valueOf(latitude));


        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && !addresses.isEmpty()) {
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            ubicacion = address + ", " + city + ", " + state + ", " + country;
            Log.d("UBICACION", ubicacion);
        }

        Intent intent = new Intent(GPSupdater.this, NetworkService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("command", "editProfile");
        intent.putExtra("ubicacion", ubicacion);
        startService(intent);

    }

    public void onReceiveResult(int resultCode, Bundle resultData) {
        

    }

    public void stopGPSupdate(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSupdater.this);
        }
    }

    public void getLocationUpdates() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no provider is enabled
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

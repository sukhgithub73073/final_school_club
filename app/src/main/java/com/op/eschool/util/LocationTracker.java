package com.op.eschool.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.gson.Gson;
import com.op.eschool.models.LocationDetailModel;

public class LocationTracker {

    public static String TAG = LocationTracker.class.getName();

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location = null;
    Location netLocation = null;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 1000;
    protected LocationManager locationManager;
    protected LocationManager netLocationManager;
    static Context mcontext;
    private static LocationTracker instance;


    public static synchronized LocationTracker getInstance(Context ctx) {
        mcontext = ctx;
        if (instance == null) {
            instance = new LocationTracker();
        }
        return instance;
    }

    myListener listener;
    public void connectToLocation(myListener listener) {
        this.listener=listener;
        stopLocationUpdates();
        displayLocation();
    }

    private void displayLocation() {
        try {
            FLog.i(TAG,"displayLocation");
            Location location = getLocation("GPS");
            Location netLocation = getLocation("NETWORK");

            boolean fromMock = location.isFromMockProvider() ;
            boolean netFromMock = netLocation.isFromMockProvider() ;

            LocationDetailModel locationDetailModel = new LocationDetailModel(fromMock , location.getAccuracy() , location.getLatitude() , location.getLongitude() ,
                    netFromMock, netLocation.getAccuracy() , netLocation.getLatitude() , netLocation.getLongitude()) ;
            if (location != null && netLocation != null ) {
                updateLattitudeLongitude(locationDetailModel);
            }
        } catch (SecurityException e) {
            e.printStackTrace();        } catch (Exception e) {
            e.printStackTrace();        }
    }

    public Location getLocation(String type) {
        try {
            locationManager = (LocationManager) mcontext
                    .getSystemService(Context.LOCATION_SERVICE);
            netLocationManager = (LocationManager) mcontext
                    .getSystemService(Context.LOCATION_SERVICE);

            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                this.canGetLocation = false;

            } else {

                this.canGetLocation = true;

                if (isNetworkEnabled && type.equalsIgnoreCase("NETWORK")) {

                    ///FLog.w(TAG + "-->Network", "Network Enabled");
                    if (netLocationManager != null) {
                        netLocation = netLocationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        netLocationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationProviderListener);
                        return netLocation;
                    }

                } else if (isGPSEnabled && type.equalsIgnoreCase("GPS")) {
                    //FLog.w(TAG + "-->GPS", "GPS Enabled");
                    if (locationManager != null) {

                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationProviderListener);

                        return location;
                    }
                }
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();        }
        return location;
    }

    public void updateLattitudeLongitude(LocationDetailModel locationDetailModel) {

        listener.onUpdate(locationDetailModel);
    }

    public void stopLocationUpdates(){
        try {
            if (locationManager != null) {
                FLog.i(TAG,"stopLocationUpdates");
                locationManager.removeUpdates(locationProviderListener);
                locationManager = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public LocationListener locationProviderListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location d) {
            try {
                location = getLocation("GPS");
                netLocation = getLocation("NETWORK");

                boolean fromMock = location.isFromMockProvider() ;
                boolean netFromMock = netLocation.isFromMockProvider() ;
                LocationDetailModel locationDetailModel = new LocationDetailModel(fromMock , location.getAccuracy() , location.getLatitude() , location.getLongitude() ,
                        netFromMock, netLocation.getAccuracy() , netLocation.getLatitude() , netLocation.getLongitude()) ;
                if (location != null && netLocation != null ) {
                   // FLog.w("LocationListener" , "onLocationChanged" + new Gson().toJson(locationDetailModel)) ;

                    updateLattitudeLongitude(locationDetailModel);
                }
            } catch (Exception e) {

                e.printStackTrace();

            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {


        }

        @Override
        public void onProviderEnabled(String s) {


        }

        @Override
        public void onProviderDisabled(String s) {


        }
    };

    public interface myListener{
        void onUpdate(LocationDetailModel locationDetailModel) ;
    }

}

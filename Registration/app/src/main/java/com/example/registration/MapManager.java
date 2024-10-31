package com.example.registration;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.List;

public class MapManager {
    private GoogleMap mMap;
    private Context context;
    private Geocoder geocoder;
    Marker marker;
    public static LatLng searchedLoc;
    public static LatLng currentLoc;
    Double distance;

    public MapManager(GoogleMap googleMap, Context context){
        this.mMap = googleMap;
        this.context = context;
        this.geocoder = new Geocoder(context);
    }

    //utilizing geocoder to search using long and lat
    public void geocoderLocationFetch(Location location){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Log.d("location", "latitude" + latitude + "longitude" + longitude);

        //geocoder will derive the location name, the country, and city of the user as a list
        try{
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String addres = addresses.get(0).getCountryName();
            //combining long and lat to place the marker on google map
            LatLng latLng = new LatLng(latitude, longitude);

            if(marker != null){
                marker.remove();
            }
            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(addres));
            mMap.setMaxZoomPreference(15);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    //using geocoder to search using query and calculating the distance
    public double searchLocationDistanceAndMark(Location currentLocation, String location){
        List <Address> addressList = null;
        if(location != null || location.equals("")){
            Geocoder geocoder = new Geocoder(context);
            try{
                addressList = geocoder.getFromLocationName(location, 1);
            } catch(IOException e){
                e.printStackTrace();
            }
            //retrieving first location
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            searchedLoc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            //adding market to the current location gotten above
            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
            mMap.setMaxZoomPreference(10);

            //move camera to that position
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            distance = SphericalUtil.computeDistanceBetween(currentLoc, searchedLoc);
            return distance;
        } else{
            distance = (double)0;
            return distance;
        }
    }
}


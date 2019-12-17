package android.beginnerranch.bariatriapp.maps;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import android.beginnerranch.bariatriapp.R;
import android.os.StrictMode;
import android.util.Log;

import java.util.List;

public class Restaurants extends Fragment implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Double latitude = 34d;
    protected Double longtitude = 141d;
    protected PlacesClient googlePlaces = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setContentView(R.layout.activity_restaurants);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, (LocationListener)this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), "AIzaSyAItzSVMgoNQrl1_QYH2QPAzGCi2e8Fw7U");
        }
        googlePlaces = Places.createClient(getContext());
        mMap = googleMap;
        updateLocation();

    }

    private void updateLocation() {
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longtitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Your location"));
        mMap.setMaxZoomPreference(15.0f);
        mMap.setMinZoomPreference(5.0f);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f) );
        searchRestaurants();
    }

    protected Boolean searchRestaurants() {
        GooglePlaces googlePlaces = new GooglePlaces();

        try {
            String types = "cafe|restaurant";

            double radius = 30000; // 1 mile (in meters)

            List<Place> listPlace = googlePlaces.search(latitude, longtitude, radius, types);
            for( Place place: listPlace) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(place.getLatitude(), place.getLongitude())).title(place.getName()));

            }
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(latitude != location.getLatitude() && longtitude != location.getLongitude()) {
            latitude = location.getLatitude();
            longtitude = location.getLongitude();
            updateLocation();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("Latitude", "status");
    }
}

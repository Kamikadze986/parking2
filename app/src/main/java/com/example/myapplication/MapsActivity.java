package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.retrofit.RouteApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private int p;
    private double x=0,y=0,Ax,Ay,min=99999, lon, lat;
    private double [][] parking = new double[5][4];
    private List<LatLng> places = new ArrayList<>();
    private UiSettings mUiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        parking();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

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
    public void parking(){
        x=56.141479;
        y=47.212692;
        // парковка 1
        parking[0][0]=56.149107;//широта
        parking[0][1]=47.196090;//долгота
        parking[0][2]=20;//кол-во всего мест
        parking[0][3]=10;//кол-во занятых мест
        // парковка 2
        parking[1][0]=56.144885;//широта
        parking[1][1]=47.223308;//долгота
        parking[1][2]=20;//кол-во всего мест
        parking[1][3]=20;//кол-во занятых мест
        // парковка 3
        parking[2][0]=56.111975;//широта
        parking[2][1]=47.262420;//долгота
        parking[2][2]=30;//кол-во всего мест
        parking[2][3]=30;//кол-во занятых мест
        // парковка 4
        parking[3][0]=56.135726;//широта
        parking[3][1]=47.241413;//долгота
        parking[3][2]=20;//кол-во всего мест
        parking[3][3]=20;//кол-во занятых мест
        // парковка 5
        parking[4][0]=56.141482;//широта
        parking[4][1]=47.212966;//долгота
        parking[4][2]=25;//кол-во всего мест
        parking[4][3]=25;//кол-во занятых мест
        for (int i=0;i<=4;i++){  //находим ближайшую
            if (parking[i][3]>=parking[i][2]) continue;
            Ax=Math.abs(parking[i][0]-x);
            Ay=Math.abs(parking[i][1]-y);
            if(Ax+Ay<min) {
                min=Ax+Ay;
                p=i;
            }
        }
        lon =parking[p][0];
        lat =parking[p][1];
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        places.add(new LatLng(x, y));
        places.add(new LatLng(lon, lat));
        mUiSettings = googleMap.getUiSettings();

        // Keep the UI Settings state in sync with the checkboxes.
        mUiSettings.setZoomControlsEnabled(true);
        String mapsApiKey= "AIzaSyCO_UV7Q43xZ0YEIg2pn3D1j4Nq0tBe2C8";
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(places.get(1), 13));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/directions/json")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RouteApi routeApi = retrofit.create(RouteApi.class);
        routeApi.getRoute(x+","+y, lon+","+lat, mapsApiKey);


        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(new LatLng(56.141479, 47.212692),
                        new LatLng(lon, lat)));
        polyline1.setTag("A");

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lon, lat))
                .title("Marker")
                .draggable(false)
        );
    }
}
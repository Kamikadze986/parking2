package com.example.myapplication;

import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.retrofit.RouteApi;
import com.example.myapplication.retrofit.data.EndLocation;
import com.example.myapplication.retrofit.data.Main;
import com.example.myapplication.retrofit.data.Step;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private int p;
    private double x = 0, y = 0, Ax, Ay, min = 99999, lon, lat;
    private double[][] parking = new double[5][4];
    private List<LatLng> places = new ArrayList<>();
    private UiSettings mUiSettings;
    private Retrofit retrofit;
    private List<EndLocation> endLocations;
    private SupportMapFragment mapFragment;
    private OnMapReadyCallback onMapReadyCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        parking();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        onMapReadyCallback = this;

        String startLocation = String.format(Locale.ENGLISH, "%f,%f", x, y);
        String endLocation = String.format(Locale.ENGLISH, "%f,%f", lon, lat);
        endLocations = new ArrayList<>();

//        endLocations.add(new EndLocation(y, x));
//        endLocations.add(new EndLocation(47.21230370000001, 56.1418072));
//        endLocations.add(new EndLocation(47.2084118, 56.1408132));
//        endLocations.add(new EndLocation(47.2012857, 56.1481901));
//        endLocations.add(new EndLocation(47.1960482, 56.1487798));
//        endLocations.add(new EndLocation(47.1959895, 56.1490644));
//        endLocations.add(new EndLocation(47.196091, 56.1490652));
//        endLocations.add(new EndLocation(lat, lon));
//        mapFragment.getMapAsync(onMapReadyCallback);

        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/directions/json?origin="
                        + startLocation + "&destination=" + endLocation + "AIzaSyDgKD5TgE5xUpCFokNl1smtpZ8CamDSsq0")
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONObject main = new JSONObject(response.body().string());
                    JSONArray routes = main.getJSONArray("routes");
                    JSONObject route = routes.getJSONObject(0);
                    JSONArray legs = route.getJSONArray("legs");
                    JSONObject leg = legs.getJSONObject(0);
                    JSONArray steps = leg.getJSONArray("steps");
                    for (int i = 0; i < steps.length(); i++) {
                        JSONObject stepJson = steps.getJSONObject(i);
                        JSONObject endLocationJson = stepJson.getJSONObject("end_location");
                        EndLocation endLocation = new EndLocation();
                        endLocation.setLat(endLocationJson.getDouble("lat"));
                        endLocation.setLng(endLocationJson.getDouble("lng"));
                        endLocations.add(endLocation);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mapFragment.getMapAsync(onMapReadyCallback);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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
    public void parking() {
        x = 56.141479;
        y = 47.212692;
        // парковка 1
        parking[0][0] = 56.149107;//широта
        parking[0][1] = 47.196090;//долгота
        parking[0][2] = 20;//кол-во всего мест
        parking[0][3] = 10;//кол-во занятых мест
        // парковка 2
        parking[1][0] = 56.144885;//широта
        parking[1][1] = 47.223308;//долгота
        parking[1][2] = 20;//кол-во всего мест
        parking[1][3] = 20;//кол-во занятых мест
        // парковка 3
        parking[2][0] = 56.111975;//широта
        parking[2][1] = 47.262420;//долгота
        parking[2][2] = 30;//кол-во всего мест
        parking[2][3] = 30;//кол-во занятых мест
        // парковка 4
        parking[3][0] = 56.135726;//широта
        parking[3][1] = 47.241413;//долгота
        parking[3][2] = 20;//кол-во всего мест
        parking[3][3] = 20;//кол-во занятых мест
        // парковка 5
        parking[4][0] = 56.141482;//широта
        parking[4][1] = 47.212966;//долгота
        parking[4][2] = 25;//кол-во всего мест
        parking[4][3] = 25;//кол-во занятых мест
        for (int i = 0; i <= 4; i++) {  //находим ближайшую
            if (parking[i][3] >= parking[i][2]) continue;
            Ax = Math.abs(parking[i][0] - x);
            Ay = Math.abs(parking[i][1] - y);
            if (Ax + Ay < min) {
                min = Ax + Ay;
                p = i;
            }
        }
        lon = parking[p][0];
        lat = parking[p][1];
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        places.add(new LatLng(x, y));
        places.add(new LatLng(lon, lat));
        mUiSettings = googleMap.getUiSettings();

        // Keep the UI Settings state in sync with the checkboxes.
        mUiSettings.setZoomControlsEnabled(true);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(places.get(1), 13));
        String mapsApiKey = "AIzaSyDgKD5TgE5xUpCFokNl1smtpZ8CamDSsq0";


        PolylineOptions line = new PolylineOptions();
        line.width(5);
        line.color(Color.parseColor("#CC0000FF"));
        line.visible(true);
        for (int i = 0; i < endLocations.size(); i++) {
            line.add(new LatLng(endLocations.get(i).getLng(),
                    endLocations.get(i).getLat()));

        }
        googleMap.addPolyline(line);


        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lon, lat))
                .title("Marker")
                .draggable(false)
        );
    }
}
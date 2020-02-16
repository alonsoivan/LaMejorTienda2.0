package com.ivn.lamejortienda.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ivn.lamejortienda.R;
import com.ivn.lamejortienda.clases.DirectionsJSONParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private boolean modo = false;  // Para ver en que modo esta: claro-oscuro
    private MapView mapView;
    private GoogleMap googleMap;
    private LatLng tienda = new LatLng(40.394257, -3.745465);
    private String mode;      // Incida el modo de ruta: andando, en coche..

    public static final String MAPVIEW_BUNDLE_KEY = "AIzaSyCjhFV0xn40V0ojoNE2Qc1cfEzjRHcCJdc";

    FloatingActionButton btModo;
    FloatingActionButton btAndar;
    FloatingActionButton btCoche;
    FloatingActionButton btIr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.mapa);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);

        btIr = findViewById(R.id.btIr);
        btCoche = findViewById(R.id.btCoche);
        btAndar = findViewById(R.id.btAndar);
        btModo = findViewById(R.id.btModo);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btModo:
                if(!modo) {
                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.modo_noche));
                    modo = !modo;
                }else{
                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.modo_normal));
                    modo = !modo;
                }
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(tienda).title("LMT2.0").snippet("Abierto L-V"));
                new MapaActivity.DownloadTask().execute(getDirectionsUrl());
                break;
            case R.id.btIr:
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(tienda).title("LMT2.0").snippet("Abierto L-V"));
                new MapaActivity.DownloadTask().execute(getDirectionsUrl());

                // Mover cam a ubicación actual
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                LatLng lt = new LatLng(location.getLatitude(),location.getLongitude());

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lt, 16));

                break;
            case R.id.btCoche:
                Toast.makeText(this,"Modo = Conduciendo",Toast.LENGTH_SHORT).show();
                mode = "mode=driving";
                break;
            case R.id.btAndar:
                Toast.makeText(this,"Modo = Andando",Toast.LENGTH_SHORT).show();
                mode = "mode=walking";
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        map.addMarker(new MarkerOptions().position(tienda).title("LMT2.0").snippet("Abierto L-V"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(tienda, 16));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        //Disable Map Toolbar and compass and locationButton:
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);

        btModo.setOnClickListener(this);
        btIr.setOnClickListener(this);
        btCoche.setOnClickListener(this);
        btAndar.setOnClickListener(this);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new MapaActivity.ParserTask().execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            //MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(15);
                if (modo)
                    lineOptions.color(Color.rgb(255, 211, 105));
                else
                    lineOptions.color(Color.rgb(1, 86, 104));

                lineOptions.geodesic(true);

            }
            // Drawing polyline in the Google Map for the i-th route
            googleMap.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = null;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED  && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);

            String provider = preferencias.getString("opcion_ubicacion","GPS");
            System.out.println(provider);

            if(provider.equals("GPS")) {
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location == null)
                    location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }else
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }



        // Origin of route
        String str_origin = "origin=" + location.getLatitude() + "," + location.getLongitude();

        // Destination of route
        String str_dest = "destination=" + tienda.latitude + "," + tienda.longitude;

        // Sensor enabled
        String sensor = "sensor=true";

        //String mode = "mode=walking";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + "transit_mode=subway";

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+"&key=" +"AIzaSyCjhFV0xn40V0ojoNE2Qc1cfEzjRHcCJdc";

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}

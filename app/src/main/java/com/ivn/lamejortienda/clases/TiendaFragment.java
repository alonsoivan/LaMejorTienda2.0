package com.ivn.lamejortienda.clases;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ivn.lamejortienda.R;
import com.ivn.lamejortienda.activities.MapaActivity;

import static com.ivn.lamejortienda.activities.MapaActivity.MAPVIEW_BUNDLE_KEY;

public class TiendaFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private LatLng tienda = new LatLng(40.394257, -3.745465);
    private GoogleMap googleMap;
    private MapView mapView;
    private Button btIrMapa;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_tienda, container, false);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        btIrMapa = root.findViewById(R.id.btIrMapa);
        btIrMapa.setOnClickListener(this);
        mapView = root.findViewById(R.id.mapView);



        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        return root;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        map.addMarker(new MarkerOptions().position(tienda).title("LMT2.0").snippet("Abierto L-V"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(tienda, 16));

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        //Disable Map Toolbar and compass and locationButton:
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onClick(View v) {
        Intent mapa = new Intent(getContext(), MapaActivity.class);
        startActivity(mapa);
    }
}
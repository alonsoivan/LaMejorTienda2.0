package com.ivn.lamejortienda.activities;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ivn.lamejortienda.R;

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        LatLng casa = new LatLng(40.3497332,-3.6941772);
        mMap.addMarker(new MarkerOptions().position(casa).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(41.3497332,-3.6941772)).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(casa));
        //mMap.setMinZoomPreference(14.0f);


    }
}


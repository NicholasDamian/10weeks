package com.example.nicholashall.myapplication.Views;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.nicholashall.myapplication.MainActivity;
import com.example.nicholashall.myapplication.Models.User;
import com.example.nicholashall.myapplication.Network.RestClient;
import com.example.nicholashall.myapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by nicholashall on 11/7/16.
 */

public class MapPageView extends RelativeLayout implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private Context context;
    private ArrayList<User> people;


    @Bind(R.id.map)
    public MapView mapView;

    public MapPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        ((MainActivity)context).showMenuItem(true);

        mapView.onCreate(((MainActivity) getContext()).savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap) {
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        googleMap.moveCamera( newLatLngZoom(sydney,15));
//        googleMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pokemon))
//                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
//                .position(sydney));

        mMap = googleMap;
        mMap.getUiSettings().isMyLocationButtonEnabled();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e){

        }
        mMap.clear();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(loc));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 13.0f));

            User user = new User(location.getLongitude(),location.getLatitude());
            final RestClient restClient = new RestClient();
            restClient.getApiService().CheckIn(user).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "you checked in", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, R.string.registration_failed, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context,"Its really broken", Toast.LENGTH_SHORT).show();
                }
            });

            restClient.getApiService().getUsers(40000).enqueue(new Callback<User[]>() {
            @Override
            public void onResponse(Call<User[]> call, Response<User[]> response) {
                Toast.makeText(context, "well here is everyone", Toast.LENGTH_SHORT).show();
                people = new ArrayList<>(Arrays.asList(response.body()));


//                Inside the for loop i need to find the path to full name of the object
                for (User user : people) {
                        LatLng loc = new LatLng(user.getLatitude(), user.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(loc).snippet(user.getFullName()));
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            Toast.makeText(context, "You caught " + marker.getSnippet(), Toast.LENGTH_SHORT).show();
                            marker.remove();
                            return false;
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<User[]> call, Throwable t) {
                Log.d(TAG, "GetPosts Failed!");

            }
            });



            mMap.clear();
        }
    };



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location services connected.");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


}

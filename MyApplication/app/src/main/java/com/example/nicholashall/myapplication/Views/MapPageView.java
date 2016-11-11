package com.example.nicholashall.myapplication.Views;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.nicholashall.myapplication.MainActivity;
import com.example.nicholashall.myapplication.Models.Account;
import com.example.nicholashall.myapplication.Models.User;
import com.example.nicholashall.myapplication.Network.RestClient;
import com.example.nicholashall.myapplication.PeopleMon;
import com.example.nicholashall.myapplication.R;
import com.example.nicholashall.myapplication.Stages.CaughtPeopleListStage;
import com.example.nicholashall.myapplication.Stages.NearbyPeopleStage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
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
    public Double longitude = -82.809195;
    public Double latitude = 37.816380;
    public static Location mLocation;
    LatLng Home = new LatLng(latitude,longitude);
    public Bitmap myMarker;

    @Bind(R.id.map)
    public MapView mapView;

    public MapPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @OnClick(R.id.caught_peoplemon_view_button)
    public void showAddCategoryView(){
        Flow flow = PeopleMon.getMainFlow();
        History newHistory = flow.getHistory().buildUpon()
                .push(new CaughtPeopleListStage())
                .build();
        flow.setHistory(newHistory, Flow.Direction.FORWARD);
    }

    @OnClick(R.id.nearby_peoplemon_view_button)
    public void showListCategoryView(){
        Flow flow = PeopleMon.getMainFlow();
        History newHistory = flow.getHistory().buildUpon()
                .push(new NearbyPeopleStage())
                .build();
        flow.setHistory(newHistory, Flow.Direction.FORWARD);
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

        mLocation = new Location("");
        mLocation.setLatitude(latitude);
        mLocation.setLongitude(longitude);
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

            RestClient restClient = new RestClient();
            restClient.getApiService().getUserInfo().enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if(response.isSuccessful()){
                        Account authUser = response.body();
                    }


                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {

                }
            });


            mMap.addMarker(new MarkerOptions().position(loc));


            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 17.0f));
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            mLocation = location;
            checkIn();
            checkNearby();
            mMap.clear();


            GroundOverlayOptions radar = new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromResource(R.mipmap.radar))
                    .position(loc, 200f, 200f);
            GroundOverlay imageOverlay = mMap.addGroundOverlay(radar);


            final Circle circle = mMap.addCircle(new CircleOptions().center(loc)
                    .strokeColor(Color.BLUE).radius(80));
            ValueAnimator vAnimator = new ValueAnimator();
            vAnimator.setRepeatCount(ValueAnimator.INFINITE);
            vAnimator.setRepeatMode(ValueAnimator.RESTART);  /* PULSE */
            vAnimator.setIntValues(80, 0);
            vAnimator.setDuration(2500);
            vAnimator.setEvaluator(new IntEvaluator());
            vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    // Log.e("", "" + animatedFraction);
                    circle.setRadius(animatedFraction * 80);
                }
            });
            vAnimator.start();


        }
    };


    @Override
    public void onConnected(@Nullable Bundle bundle) {
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

    public void checkNearby(){
        RestClient restClient = new RestClient();
        restClient.getApiService().getUsers(100).enqueue(new Callback<User[]>() {
            @Override
            public void onResponse(Call<User[]> call, Response<User[]> response) {
                Toast.makeText(context, "well here is everyone", Toast.LENGTH_SHORT).show();
                people = new ArrayList<>(Arrays.asList(response.body()));

                for (User user : response.body()) {
                    latitude = user.getLatitude();
                    longitude = user.getLongitude();
                    String userId = user.getUserId();

                    if (user.getAvatarBase64() == null || user.getAvatarBase64().length() <= 100) {


                        final LatLng userpos = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().title(user.getUserName())
                                //           .icon(BitmapDescriptorFactory.fromBitmap(decodedByte))
                                .snippet(user.getUserId())
                                .position(userpos));
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                Location userLoc = new Location("");
                                userLoc.setLatitude(marker.getPosition().latitude);
                                userLoc.setLongitude(marker.getPosition().longitude);
                                final String CaughtUserId = marker.getSnippet();
                                final User user = new User(CaughtUserId, mLocation.distanceTo(userLoc));


                                Double latC = marker.getPosition().latitude;
                                Double lngC = marker.getPosition().longitude;
                                LatLng markCircle = new LatLng(latC, lngC);
                                final Circle circle = mMap.addCircle(new CircleOptions().center(markCircle)
                                        .strokeColor(Color.RED).radius(10));
                                ValueAnimator vAnimator = new ValueAnimator();
                                vAnimator.setRepeatCount(1);
                                vAnimator.setRepeatMode(ValueAnimator.REVERSE);
                                vAnimator.setIntValues(10, 0);
                                vAnimator.setDuration(500);
                                vAnimator.setEvaluator(new IntEvaluator());
                                vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                                vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        float animatedFraction = valueAnimator.getAnimatedFraction();
                                        circle.setRadius(animatedFraction * 10);
                                    }
                                });
                                vAnimator.start();


                                RestClient restClient = new RestClient();
                                restClient.getApiService().catchUser(user).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(context, "Person Caught!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "That is a Pokemon you can't catch those ", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                                marker.remove();
                                return false;
                            }
                        });
                    }else {

                        try {
                            String encodedImage = user.getAvatarBase64();
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            decodedByte = Bitmap.createScaledBitmap(decodedByte, 120, 120, false);

                            final LatLng userpos = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().title(user.getUserName())
                                    .icon(BitmapDescriptorFactory.fromBitmap(decodedByte))
                                    .snippet(user.getUserId())
                                    .position(userpos));


                        }catch (Exception e){

                        }
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                Location userLoc = new Location("");
                                userLoc.setLatitude(marker.getPosition().latitude);
                                userLoc.setLongitude(marker.getPosition().longitude);
                                final String CaughtUserId = marker.getSnippet();
                                final User user = new User(CaughtUserId, mLocation.distanceTo(userLoc));


                                Double latC = marker.getPosition().latitude;
                                Double lngC = marker.getPosition().longitude;
                                LatLng markCircle = new LatLng(latC, lngC);
                                final Circle circle = mMap.addCircle(new CircleOptions().center(markCircle)
                                        .strokeColor(Color.RED).radius(10));
                                ValueAnimator vAnimator = new ValueAnimator();
                                vAnimator.setRepeatCount(1);
                                vAnimator.setRepeatMode(ValueAnimator.REVERSE);
                                vAnimator.setIntValues(10, 0);
                                vAnimator.setDuration(500);
                                vAnimator.setEvaluator(new IntEvaluator());
                                vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                                vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        float animatedFraction = valueAnimator.getAnimatedFraction();
                                        circle.setRadius(animatedFraction * 10);
                                    }
                                });
                                vAnimator.start();


                                RestClient restClient = new RestClient();
                                restClient.getApiService().catchUser(user).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(context, "Person Caught!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "That is a Pokemon you can't catch those ", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                                marker.remove();
                                return false;
                            }
                        });


                    }
                }
            }
            @Override
            public void onFailure(Call<User[]> call, Throwable t) {
                Log.d(TAG, "GetPosts Failed!");
            }
        });
    }

    public void checkIn(){
        User user = new User(longitude,latitude);
        final LatLng Home = new LatLng(longitude, latitude);
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
    }
}





package com.suelee.maps;


import static com.suelee.maps.PermissionHelper.REQUEST_CODE_PERMISSION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.suelee.maps.data.DataHelper;
import com.suelee.maps.data.FlowerShop;
import com.suelee.maps.data.LocationMark;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient fusedLocationProviderClient;

    private GoogleMap map;

    private Marker currentLocationMarker;

   private boolean isFromSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        if (getIntent() != null){
           isFromSearch = getIntent().getBooleanExtra("isFromSearch",false);
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        checkLocationPermission();
        map = googleMap;
        googleMap.setMapType(GoogleMapHelper.MapType);
        googleMap.setIndoorEnabled(GoogleMapHelper.indoorEnabled);
        googleMap.setOnMapClickListener(this::showPopWindow);
    }

    private PopupWindow popupWindow;
    private View contentView;
    @SuppressLint("InflateParams")
    private void showPopWindow(LatLng latLng) {
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        if (popupWindow == null){
            contentView = LayoutInflater.from(MapActivity.this).inflate(R.layout.activity_popwindow, null);
            popupWindow = new PopupWindow(contentView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
        ImageView imageView = contentView.findViewById(R.id.iv_close);
        imageView.setOnClickListener(view -> {
            dismissPopWindow();
        });
        Button btAdd = contentView.findViewById(R.id.bt_add);
        EditText etName = contentView.findViewById(R.id.et_name);
        EditText etType = contentView.findViewById(R.id.et_type);
        EditText etService = contentView.findViewById(R.id.et_service);
        EditText etReviews = contentView.findViewById(R.id.et_reviews);
        btAdd.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String type = etType.getText().toString();
            String service = etService.getText().toString();
            String reviews = etReviews.getText().toString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(type) || TextUtils.isEmpty(service) || TextUtils.isEmpty(reviews)){
                Toast.makeText(MapActivity.this,"Information loss",Toast.LENGTH_LONG).show();
                return;
            }
            drawLocationMarker(latLng,name);
            LocationMark locationMark = new LocationMark(name,latitude,longitude);
            FlowerShop flowerShop = new FlowerShop();
            flowerShop.setMark(locationMark);
            flowerShop.setType(type);
            flowerShop.setServices(service);
            flowerShop.setReviews(reviews);
            DataHelper.getFlowerShopList().add(flowerShop);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("mapMarkData");
            Gson gson = new Gson();
            myRef.setValue(gson.toJson(DataHelper.getFlowerShopList()));
            dismissPopWindow();
        });

    }

    private void dismissPopWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
    private void checkLocationPermission() {
        String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        PermissionHelper.checkPermission(this,
                permission,
                PermissionHelper.REQUEST_PHOTO_CODE, new PermissionHelper.RequestPermissionCallBack() {
                    @Override
                    public void onPermission() {
                        startLocationUpdates();
                    }

                    @Override
                    public void onDenied() {
                        PermissionHelper.requestPermission(MapActivity.this, permission);
                    }
                });
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000);
        fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (PermissionHelper.verifyPermissions(grantResults)) {
                startLocationUpdates();
            } else {
                Toast.makeText(MapActivity.this,
                        "If you do not obtain the location permission, google map may be unavailable. Grant the location permission",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if (currentLocationMarker == null ){
                List<FlowerShop> data = isFromSearch ? DataHelper.getSearchFlowerShopList()
                        : DataHelper.getFlowerShopList();
                for (FlowerShop shop : data) {
                    LocationMark mark = shop.getMark();
                    if (mark != null) {
                        drawLocationMarker(
                                new LatLng(mark.getLatitude(), mark.getLongitude()),
                                mark.getName());
                    }
                }
                for (Location location : locationResult.getLocations()) {
                    currentLocationMarker = drawLocationMarker(
                            new LatLng(location.getLatitude(), location.getLongitude()),
                            "My Location");
                }
            }
        }
    };

    @SuppressLint("NewApi")
    private Marker drawLocationMarker(LatLng latLng,String markerName) {
        Marker marker = map.addMarker(
                new MarkerOptions().position(latLng).title(markerName));
        map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(latLng, 14f));
        return marker;
    }
}
package com.plantalot.fragments;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.plantalot.R;

import java.util.concurrent.Executor;


public class NewGardenFragment extends Fragment implements OnMapReadyCallback {

	private final static String TAG = "New garden fragment";

	private MapView mMapView;
	private GoogleMap map;
	private CameraPosition cameraPosition;

 	// Default location & zoom when location permission is not grandet
	private final LatLng defaultLocation = new LatLng(41.9028, 12.2964);
	private static final int DEFAULT_ZOOM = 15;
	private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

	private boolean locationPermissionGranted;

	// The entry point to the Fused Location Provider.
	private FusedLocationProviderClient fusedLocationProviderClient;

	// The geographical location where the device is currently located. That is, the last-known
	// location retrieved by the Fused Location Provider.
	private Location lastKnownLocation;

	// Keys for storing activity state.
	// [START maps_current_place_state_keys]
	private static final String KEY_CAMERA_POSITION = "camera_position";
	private static final String KEY_LOCATION = "location";
	// [END maps_current_place_state_keys]


	private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(savedInstanceState != null){
			lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
			cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
		}
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.nuovo_giardino_fragment, container, false);

		// *** IMPORTANT ***
		// MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
		// objects or sub-Bundles.
		Bundle mapViewBundle = null;
		if (savedInstanceState != null) {
			mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
		}
		mMapView = (MapView) view.findViewById(R.id.map);
		mMapView.onCreate(mapViewBundle);
		mMapView.getMapAsync(this);
		return view;
	}



	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (map != null) {
			outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
			outState.putParcelable(KEY_LOCATION, lastKnownLocation);
		}
		super.onSaveInstanceState(outState);

		Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
		if (mapViewBundle == null) {
			mapViewBundle = new Bundle();
			outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
		}

		mMapView.onSaveInstanceState(mapViewBundle);
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
		mMapView.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
		mMapView.onStop();
	}

	@Override
	public void onMapReady(GoogleMap map) {
		// Prompt the user for permission
		getLocationPermission();
		// Turn on the My Location layer and the related control on the map
		updateLocationUI();
		// Get the current location of the device and set the position on the map
		getDeviceLocation();
	}

	private void getLocationPermission() {
		if (ContextCompat.checkSelfPermission(getContext(),
				android.Manifest.permission.ACCESS_FINE_LOCATION)
				== PackageManager.PERMISSION_GRANTED) {
			locationPermissionGranted = true;
		} else {
			ActivityCompat.requestPermissions(getActivity(),
					new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
					PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
		}
	}

	@SuppressLint("MissingPermission")
	private void updateLocationUI() {
		if (map == null) {
			return;
		}
		try {
			if (locationPermissionGranted) {
				map.setMyLocationEnabled(true);
				map.getUiSettings().setMyLocationButtonEnabled(true);
			} else {
				map.setMyLocationEnabled(false);
				map.getUiSettings().setMyLocationButtonEnabled(false);
				lastKnownLocation = null;
				getLocationPermission();
			}
		} catch (SecurityException e)  {
			Log.e("Exception: %s", e.getMessage());
		}
	}

	private void getDeviceLocation(){
		try {
			if (locationPermissionGranted) {
				@SuppressLint("MissingPermission") Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
				locationResult.addOnCompleteListener((Executor) this, new OnCompleteListener<Location>() {
					@Override
					public void onComplete(@NonNull Task<Location> task) {
						if (task.isSuccessful()) {
							// Set the map's camera position to the current location of the device.
							lastKnownLocation = task.getResult();
							if (lastKnownLocation != null) {
								map.moveCamera(CameraUpdateFactory.newLatLngZoom(
										new LatLng(lastKnownLocation.getLatitude(),
												lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
							}
						} else {
							Log.d(TAG, "Current location is null. Using defaults.");
							Log.e(TAG, "Exception: %s", task.getException());
							map.moveCamera(CameraUpdateFactory
									.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
							map.getUiSettings().setMyLocationButtonEnabled(false);
						}
					}
				});
			}
		} catch (SecurityException e)  {
			Log.e("Exception: %s", e.getMessage(), e);
		}
	}

	@Override
	public void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mMapView.onLowMemory();
	}
}

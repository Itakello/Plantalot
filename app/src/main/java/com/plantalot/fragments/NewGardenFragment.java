package com.plantalot.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.plantalot.R;
import com.plantalot.classes.Giardino;
import com.plantalot.database.DbUsers;
import com.plantalot.utils.Consts;

import java.sql.SQLOutput;
import java.util.concurrent.Executor;


public class NewGardenFragment extends Fragment implements OnMapReadyCallback {

	private final static String TAG = "MAPPA";
	private GoogleMap map;
	private CameraPosition cameraPosition;
	private Marker currMarker;

	// The entry point to the Fused Location Provider.
	private FusedLocationProviderClient fusedLocationProviderClient;

	// A default location (Sydney, Australia) and default zoom to use when location permission is
	// not granted.
	private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
	private static final int DEFAULT_ZOOM = 15;
	private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
	private boolean locationPermissionGranted;

	// The geographical location where the device is currently located. That is, the last-known
	// location retrieved by the Fused Location Provider.
	private Location lastKnownLocation;

	private ActivityResultLauncher<String[]> locationPermissionRequest =
			registerForActivityResult(new ActivityResultContracts
							.RequestMultiplePermissions(), result -> {
						Boolean fineLocationGranted = result.get(
								Manifest.permission.ACCESS_FINE_LOCATION);
						Boolean coarseLocationGranted = result.get(
								Manifest.permission.ACCESS_COARSE_LOCATION);
						if ((fineLocationGranted != null && fineLocationGranted)
							|| (coarseLocationGranted != null && coarseLocationGranted)) {
							locationPermissionGranted = true;
							Log.d(TAG, "Location permission granted");
						} else {
							locationPermissionGranted = false;
							Log.e(TAG, "Location permission NOT granted");
						}
						Log.d(TAG, "New update after location permission question");
						updateLocationUI();
						getDeviceLocation();
					}
			);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.nuovo_giardino_fragment, container, false);

		SupportMapFragment mapFragment = new SupportMapFragment();
		getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_map, mapFragment).commit();

		if (mapFragment != null)
			mapFragment.getMapAsync(this);

		Button save_btn = (Button) view.findViewById(R.id.save_giardino);
		save_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO check giardino esistente + lunghezza nome giardino (lunghezza < 15)
				String giardino_name = String.valueOf(((TextInputEditText)view.findViewById(R.id.nome_giardino)).getText());
				Log.d(TAG, "Adding :" + giardino_name);
				LatLng markerLoc = currMarker.getPosition();
				DbUsers.writeNewGiardino(giardino_name, markerLoc);
				// Select current garden in DB
//				Bundle b = new Bundle();
//				b.putString(Consts.KEY_GIARDINO, giardino_name);
				Navigation.findNavController(v).navigate(R.id.action_goto_home);//, b);
			}
		});

		return view;
	}

	@Override
	public void onMapReady(@NonNull GoogleMap googleMap) {
		this.map = googleMap;
		Log.d(TAG, "Map ready");

		map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng point) {
				map.clear();
				currMarker = map.addMarker(new MarkerOptions().position(point));
			}
		});

		getLocationPermission();
		// Turn on the My Location layer and the related control on the map.
		updateLocationUI();
		// Get the current location of the device and set the position of the map.
		getDeviceLocation();
	}

	private void getLocationPermission() {
		if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
				== PackageManager.PERMISSION_GRANTED) {
			locationPermissionGranted = true;
			Log.d(TAG, "Permission already granted");
		} else {
			// TODO manage "Don't ask again" option (after rejecting 2 getlocationPermissions)
			Log.d(TAG, "Asking location permission");
			locationPermissionRequest.launch(new String[] {
					Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.ACCESS_COARSE_LOCATION
			});
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
				map.getUiSettings().setMyLocationButtonEnabled(false);
				lastKnownLocation = null;
			}
		} catch (SecurityException e)  {
			Log.e("Exception: ", e.getMessage());
		}
	}

	private void getDeviceLocation() {
		/*
		 * Get the best and most recent location of the device, which may be null in rare
		 * cases when a location is not available.
		 */
		Log.d(TAG, "Setting current position on the map");
		try {
			if (locationPermissionGranted) {
				@SuppressLint("MissingPermission") Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
				locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
					@Override
					public void onComplete(@NonNull Task<Location> task) {
						if (task.isSuccessful()) {
							// Set the map's camera position to the current location of the device.
							lastKnownLocation = task.getResult();
							if (lastKnownLocation != null) {
								LatLng currLoc = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
								map.moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc, DEFAULT_ZOOM));
								currMarker = map.addMarker(new MarkerOptions().position(currLoc));
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

}

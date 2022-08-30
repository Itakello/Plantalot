package com.plantalot.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.plantalot.MyApplication;
import com.plantalot.R;
import com.plantalot.classes.Giardino;


public class NuovoGiardinoFragment extends Fragment implements OnMapReadyCallback {
	
	private final static String TAG = "NuovoGiardinoFragment";
	private GoogleMap map;
	private Marker currMarker;
	private MyApplication app;
	
	// The entry point to the Fused Location Provider.
	private FusedLocationProviderClient fusedLocationProviderClient;
	
	// A default location (Sydney, Australia) and default zoom to use when location permission is
	// not granted.
	private final LatLng defaultLocation = new LatLng(46.0657555, 11.1483961);
	private static final int DEFAULT_ZOOM = 15;
	private boolean locationPermissionGranted;
	
	// The geographical location where the device is currently located. That is, the last-known
	// location retrieved by the Fused Location Provider.
	private Location lastKnownLocation;
	
	// Location permission request
	private final ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(
			new ActivityResultContracts.RequestMultiplePermissions(), result -> {
				Boolean fineLocationGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
				Boolean coarseLocationGranted = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);
				if ((fineLocationGranted != null && fineLocationGranted) || (coarseLocationGranted != null && coarseLocationGranted)) {
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
		app = (MyApplication) this.getActivity().getApplication();
		fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
		Log.d(TAG, "" + getParentFragmentManager().getBackStackEntryCount());
	}
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		String oldName = getArguments().getString("nomeGiardino");
		Log.wtf("oldName", oldName);
		
		View view = inflater.inflate(R.layout.nuovo_giardino_fragment, container, false);
		
		new Handler().post(() -> {
			SupportMapFragment mapFragment = new SupportMapFragment();
			getChildFragmentManager().beginTransaction().replace(R.id.frame_layout_map, mapFragment).commit();
			if (mapFragment != null) mapFragment.getMapAsync(this);
		});
		
		TextView newOrEdit = view.findViewById(R.id.nuovo_giardino_new_or_edit_text);
		newOrEdit.setText(oldName == null ? R.string.nuovo_giardino : R.string.modifica_giardino);
		
		Button backBtn = view.findViewById(R.id.nuovo_giardino_back_btn);
		MaterialButton saveDeleteBtn = view.findViewById(R.id.nuovo_giardino_save_delete_btn);
		TextInputEditText inputNome = view.findViewById(R.id.nuovo_giardino_input_nome);
		
		if (oldName == null) {  // save
			
			saveDeleteBtn.setOnClickListener(v -> {
				String nomeGiardino = String.valueOf(inputNome.getText());
				LatLng markerLoc = currMarker.getPosition();
				if (!nomeGiardino.isEmpty()) {
					Giardino giardino = new Giardino(nomeGiardino, markerLoc);
					if (app.user.addGiardino(giardino)) {
						Navigation.findNavController(view).popBackStack();
					} else {
						inputNome.setError(getString(R.string.errore_nome_esistente));
					}
				} else {
					inputNome.setError(getString(R.string.errore_campo_vuoto));
				}
			});
			backBtn.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());
			
		} else {  // delete
			
			// TODO change color
			inputNome.setText(oldName);
			saveDeleteBtn.setText(R.string.elimina);
			saveDeleteBtn.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_round_delete_24));
			
			saveDeleteBtn.setOnClickListener(v -> {
				MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
				builder.setTitle("Eliminare " + oldName + "?");
				builder.setNegativeButton(R.string.annulla, (dialog, j) -> dialog.cancel());
				builder.setPositiveButton(R.string.conferma, (dialog, j) -> {
					dialog.cancel();
					app.user.removeGiardino(oldName);
					Navigation.findNavController(view).popBackStack();
				});
				builder.show();
			});
			
			backBtn.setOnClickListener(v -> {  // FIXME osBack, TODO lat/lon
				String newName = String.valueOf(inputNome.getText());
				if (!newName.isEmpty()) {
					if (app.user.editNomeGiardino(oldName, newName)) {
						Navigation.findNavController(view).popBackStack();
					} else {
						inputNome.setError(getString(R.string.errore_nome_esistente));
					}
				} else {
					inputNome.setError(getString(R.string.errore_campo_vuoto));
				}
			});
		}
		
		return view;
	}
	
	@Override
	public void onMapReady(@NonNull GoogleMap googleMap) {
		this.map = googleMap;
		Log.d(TAG, "Map ready");
		
		map.setOnMapClickListener(point -> {
			map.clear();
			currMarker = map.addMarker(new MarkerOptions().position(point));
		});
		
		new Handler().post(() -> {
			getLocationPermission();
			updateLocationUI();  // Turn on the My Location layer and the related control on the map.
			getDeviceLocation();  // Get the current location of the device and set the position of the map.
		});
	}
	
	private void getLocationPermission() {
		if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
				== PackageManager.PERMISSION_GRANTED) {
			locationPermissionGranted = true;
			Log.d(TAG, "Permission already granted");
		} else {
			// TODO manage "Don't ask again" option (after rejecting 2 getlocationPermissions)
			Log.d(TAG, "Asking location permission");
			locationPermissionRequest.launch(new String[]{
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
		} catch (SecurityException e) {
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
				locationResult.addOnCompleteListener(getActivity(), task -> {
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
				});
			}
		} catch (SecurityException e) {
			Log.e("Exception: %s", e.getMessage(), e);
		}
	}
	
}

package com.plantalot.classes;

import com.google.android.gms.maps.model.LatLng;

public class LatLngGiardino {

    private double latitude;
    private double longitude;

    public LatLngGiardino(){}

    public LatLngGiardino(LatLng location) {
        this.latitude = location.latitude;
        this.longitude = location.longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

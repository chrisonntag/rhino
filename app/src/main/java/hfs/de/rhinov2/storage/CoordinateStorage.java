package hfs.de.rhinov2.storage;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by anselm on 16.09.17.
 */

public final class CoordinateStorage {
    public static LatLng coordinates;

    public static void setCoordinates(double lat, double lng) {
        coordinates = new LatLng(lat, lng);
    }
}

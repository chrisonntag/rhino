package hfs.de.rhinov2.storage;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by anselm on 16.09.17.
 */

public class CoordinateStorage {
    private static LatLng coordinates;
    private static final CoordinateStorage holder = new CoordinateStorage();
    public CoordinateStorage getInstrance(){
        return holder;
    }
    public LatLng getCoordinates(){
        return coordinates;
    }
    public void setCoordinates(LatLng )
}

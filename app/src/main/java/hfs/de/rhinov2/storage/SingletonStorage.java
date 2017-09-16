package hfs.de.rhinov2.storage;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by anselm on 16.09.17.
 */
@Getter
@Setter
public class SingletonStorage {
    // coords
    private static LatLng coordinates;

    // threat data
    private static String threatArea;
    private static List<String> threatCategories;
    private static String threatDesc;
    private static String threatHeadline;
    private static List<String> threatInstructions;
    private static String threatType;
    private static String threatSeverity;
    private static String threatSource;

    private static final SingletonStorage storageInstance = new SingletonStorage();

    public static SingletonStorage getInstance(){
        return storageInstance;
    }
}

package hfs.de.rhinov2.storage;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by anselm on 16.09.17.
 */

@Setter
@Getter
public class SingletonStorage {
    // coords
    private Double lat;
    private Double lng;
    private String city;

    // threat data
    private String threatArea;
    private List<String> threatCategories;
    private String threatHeadline;
    private List<String> threatInstructions;
    private String threatType;
    private String threatSeverity;
    private String threatSource;


    private static final SingletonStorage storageInstance = new SingletonStorage();

    public static SingletonStorage getInstance() {
        return storageInstance;
    }

    public void clearLocationData(){
        lat=null;
        lng=null;
        city=null;
    }
}

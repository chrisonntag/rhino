package hfs.de.rhinov2.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import hfs.de.rhinov2.R;
import hfs.de.rhinov2.storage.SingletonStorage;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";
    private PlaceAutocompleteFragment autocompleteFragment;
    //private Button locate;
    public static final String prefpath = "myPrefernces";
    static SharedPreferences preferences;
    private Button set;
    private SingletonStorage store = SingletonStorage.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        preferences = getSharedPreferences(prefpath, Context.MODE_PRIVATE);

        if (preferences.contains("City") && preferences.contains("Latitude") &&preferences.contains("Longitude")) {
            store.setCity(preferences.getString("City", null));
            store.setLng((double) preferences.getFloat("Longitude", 0.0f));
            store.setLat((double) preferences.getFloat("Latitude", 0.0f));
            changeActivity();
        } else {

            deletePreferences();
            autocompleteFragment = (PlaceAutocompleteFragment)
                    getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place.
                    autocompleteFragment.setText(place.getName());
                    store.setCity(place.getName().toString());
                    store.setLat(place.getLatLng().latitude);
                    store.setLng(place.getLatLng().longitude);
                    Log.i(TAG, "Place: " + place.getName());
                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }


            });

            //locate = (Button) findViewById(R.id.button4);
        /*locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getLocation();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/

            set = (Button) findViewById(R.id.set);
            set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (store.getCity() != null) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("City", store.getCity());
                        editor.putFloat("Latitude", (float) store.getLat());
                        editor.putFloat("Longitude", (float) store.getLng());
                        editor.commit();
                        changeActivity();
                    }
                }
            });
        }
    }

    private void changeActivity() {
        Intent mainActivity = new Intent(StartActivity.this, MainActivity.class);
        startActivity(mainActivity);
    }

    public static void deletePreferences() {
        //SharedPreferences toDelete = preferences.getSharedPreferences(prefpath, Context.MODE_PRIVATE);
        //toDelete.edit().clear().commit();
        preferences.edit().clear().commit();
    }

    /*public void getLocation() throws IOException {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String bestProvider = mLocationManager.getBestProvider(criteria, true);
        if (bestProvider == null) {
            autocompleteFragment.setText("no location found");
        }

        Location location = mLocationManager.getLastKnownLocation(bestProvider);
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        set.setText(gc.isPresent()+"");
        List<Address> currentPlace = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        set.setText(currentPlace.size());
        CoordinateStorage.setCoordinates(location.getLatitude(), location.getLongitude());
        autocompleteFragment.setText(currentPlace.get(0).getLocale().toString());
    }*/
}

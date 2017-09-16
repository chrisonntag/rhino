package hfs.de.rhinov2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import hfs.de.rhinov2.R;
import hfs.de.rhinov2.storage.SingletonStorage;
import hfs.de.rhinov2.update.Update;
import hfs.de.rhinov2.update.UpdateListAdapter;
import hfs.de.rhinov2.update.UpdateRESTService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements UpdateListAdapter.ItemClickListener {

    private static final String BASE_URL = "http://pi@172.31.1.15:5000/de.hfs.rhino/";
    // List adapter
    private UpdateListAdapter mAdapter;

    // Location locationUpdateButton
    private EditText cityLabel;
    private SingletonStorage store = SingletonStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityLabel = (EditText) findViewById(R.id.cityLabel);
        cityLabel.setText(store.getCity());

        // Create recycler view
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.mainRView);

        // Configure
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        // Add sample data
        List<Update> data = new ArrayList<>();
        data.add(new Update("No danger yet", "Set your location and press update"));
        mAdapter = new UpdateListAdapter(this, data);

        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        // Add dividers
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // Add main locationUpdateButton button
        final Button listUpdateButton = (Button) findViewById(R.id.listUpdateButton);
        listUpdateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listUpdateButtonClicked();
            }
        });

        Button switchCity = (Button) findViewById(R.id.switchCity);
        switchCity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                StartActivity.deletePreferences();
                Intent startActivity = new Intent(MainActivity.this, StartActivity.class);
                startActivity(startActivity);
            }
        });

    }

    private void listUpdateButtonClicked() {
        mAdapter.removeLast();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UpdateRESTService service = retrofit.create(UpdateRESTService.class);

        Call<JsonObject> updates = service.getUpdates(store.getLng(), store.getLat());
        updates.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                boolean endangered = response.body().get("endangered").getAsBoolean();
                if (endangered) {
                    mAdapter.clear();
                    JsonArray alerts = response.body().getAsJsonArray("alerts");
                    Iterator<JsonElement> iterator = alerts.iterator();
                    while (iterator.hasNext()) {
                        JsonElement element = iterator.next();
                        System.out.println(element.toString());
                        String title = element.getAsJsonObject().get("event_desc").getAsString();
                        String instructions = element.getAsJsonObject().get("instructions").getAsJsonArray().get(0).getAsString();
                        mAdapter.add(new Update(title, instructions));
                    }
                } else {
                    mAdapter.setEmpty();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, String.format("Update %s", "FAIL"), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) throws IOException {
        Intent detailActivity = new Intent(MainActivity.this, DetailActivity.class);

        startActivity(detailActivity);
    }
}

package hfs.de.rhinov2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    private static final String BASE_URL = "http://pi@172.31.1.15/rhino/";
    // List adapter
    private UpdateListAdapter mAdapter;

    // Location locationUpdateButton
    private TextView cityLabel;

    private static final SingletonStorage STORAGE = SingletonStorage.getInstance();

    // rest calls
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    UpdateRESTService service = retrofit.create(UpdateRESTService.class);

    JsonArray alerts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityLabel = (TextView) findViewById(R.id.cityLabel);
        cityLabel.setText(STORAGE.getCity());

        // Create recycler view
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.mainRView);

        // Configure
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        // Add sample data
        List<Update> data = new ArrayList<>();
        data.add(new Update("No danger yet", "Set your location and press update", R.color.colorSeverityUnknown));
        mAdapter = new UpdateListAdapter(this, data);

        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        // Add dividers
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // Add main locationUpdateButton button
        final FloatingActionButton listUpdateButton = (FloatingActionButton) findViewById(R.id.refresh);
        listUpdateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listUpdateButtonClicked();
            }
        });

        FloatingActionButton switchCity = (FloatingActionButton) findViewById(R.id.swichCity);
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

        Call<JsonObject> updates = service.getUpdates(STORAGE.getLat(), STORAGE.getLng());
        updates.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                boolean endangered = response.body().get("endangered").getAsBoolean();
                if (endangered) {
                    mAdapter.clear();
                    alerts = response.body().getAsJsonArray("alerts");
                    Iterator<JsonElement> iterator = alerts.iterator();
                    while (iterator.hasNext()) {
                        JsonObject element = iterator.next().getAsJsonObject();
                        System.out.println(element.toString());
                        String title = element.get("event_desc").getAsString();
                        String instructions = element.getAsJsonObject().get("instructions").getAsJsonArray().get(0).getAsString();
                        Integer severity = element.get("severity_level").getAsInt();
                        assert (severity > 0 && severity < 6);
                        int color = R.color.colorSeverityUnknown;
                        switch (severity) {
                            case 1:
                                color = R.color.colorSeverity1;
                                break;
                            case 2:
                                color = R.color.colorSeverity2;
                                break;
                            case 3:
                                color = R.color.colorSeverity3;
                                break;
                            case 4:
                                color = R.color.colorSeverity4;
                                break;
                            case 5:
                            default:
                                System.out.println("unknown severity!");

                        }
                        mAdapter.add(new Update(title, instructions, color));
                    }
                } else {
                    mAdapter.setEmpty();
                    alerts = null;
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
        if (alerts != null) {
            Intent detailActivity = new Intent(MainActivity.this, DetailActivity.class);
            JsonObject element = alerts.get(position).getAsJsonObject();

            Iterator<JsonElement> categoriesIterator = element.get("categories").getAsJsonArray().iterator();
            Iterator<JsonElement> instructionsIterator = element.get("instructions").getAsJsonArray().iterator();

            STORAGE.setThreatArea(element.get("area_name").getAsString());
            STORAGE.setThreatCategories(iteratorToList(categoriesIterator));
            STORAGE.setThreatHeadline(element.get("headline").getAsString());
            STORAGE.setThreatInstructions(iteratorToList(instructionsIterator));
            STORAGE.setThreatType(element.get("msg_type").getAsString());
            STORAGE.setThreatSeverity(element.get("severity").getAsString());
            STORAGE.setThreatSource(element.get("source").getAsString());

            startActivity(detailActivity);
        }
    }

    private static List<String> iteratorToList(Iterator<JsonElement> categoriesIterator) {
        List<String> result = new ArrayList<>();
        while(categoriesIterator.hasNext()){
            result.add(categoriesIterator.next().getAsString());
        }
        return result;
    }
}

package hfs.de.rhinov2.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import hfs.de.rhinov2.R;
import hfs.de.rhinov2.storage.SingletonStorage;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        SingletonStorage.getInstance();
    }
}

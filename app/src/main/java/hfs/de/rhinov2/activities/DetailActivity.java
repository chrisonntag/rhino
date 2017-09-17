package hfs.de.rhinov2.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import hfs.de.rhinov2.R;
import hfs.de.rhinov2.storage.SingletonStorage;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        SingletonStorage storage = SingletonStorage.getInstance();

        setLabelText(R.id.textViewArea, storage.getThreatArea());
        //setLabelText(R.id.textViewCategory, storage.getThreatCategories());
        setLabelText(R.id.textViewHeadline, storage.getThreatHeadline());
        //setLabelText(R.id.textViewInstr, storage.getThreatInstructions());
        setLabelText(R.id.textViewType, storage.getThreatType());
        setLabelText(R.id.textViewSeverity, storage.getThreatSeverity());
        setLabelText(R.id.textViewSource, storage.getThreatSource());
    }
    private void setLabelText(int id, String text){
        ((TextView) findViewById(id)).setText(text);
    }
    private void setLabelText(int id, List<String> textList){
        StringBuilder sb = new StringBuilder();
        for(String text : textList){
            sb.append(text);
        }
        setLabelText(id, sb.toString());
    }

}

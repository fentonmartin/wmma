package at.foobartech.wheremymoneyat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.collect.Lists;
import com.orm.SugarContext;

import java.util.ArrayList;

import at.foobartech.wheremymoneyat.model.Record;
import at.foobartech.wheremymoneyat.view.activity.AddRecordActivity;
import at.foobartech.wheremymoneyat.view.activity.CategoryActivity;
import at.foobartech.wheremymoneyat.view.adapter.RecordAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.add)
    FloatingActionButton add;

    @BindView(R.id.listView)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SugarContext.init(this);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populate();
    }

    private void populate() {
        final ArrayList<Record> records = Lists.newArrayList(Record.findAll(Record.class));
        final ArrayAdapter adapter = new RecordAdapter(this, records);
        listView.setAdapter(adapter);
    }

    @OnClick(R.id.add)
    void handleAdd(View view) {
        final Intent intent = new Intent(this, AddRecordActivity.class);
        this.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_categories) {
            final Intent intent = new Intent(this, CategoryActivity.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

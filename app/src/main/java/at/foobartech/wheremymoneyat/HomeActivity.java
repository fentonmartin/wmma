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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import at.foobartech.wheremymoneyat.model.Category;
import at.foobartech.wheremymoneyat.model.Record;
import at.foobartech.wheremymoneyat.view.activity.AddRecordActivity;
import at.foobartech.wheremymoneyat.view.activity.CategoryActivity;
import at.foobartech.wheremymoneyat.view.adapter.OverviewAdapter;
import at.foobartech.wheremymoneyat.view.viewmodel.OverviewItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

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
        refreshListView();
    }

    private void refreshListView() {
        final ArrayList<Category> allCategories = Lists.newArrayList(Category.findAll(Category.class));

        final ArrayList<OverviewItem> items = new ArrayList<>();
        int totalAmount = 0;
        for (final Category category : allCategories) {
            final String[] args = {category.getId().toString(), getMonth() + ""};
            final List<Record> records = Record.find(Record.class, "category = ? AND month = ?", args);

            if (!records.isEmpty()) {
                int totalAmountCategory = records.stream().map(Record::getAmount).reduce(0, (a, b) -> a + b);
                totalAmount += totalAmountCategory;
                items.add(new OverviewItem(category.getName(), totalAmountCategory));
            }
        }

        Collections.sort(items, (o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName()));
        items.add(0, new OverviewItem("Total", totalAmount));

        final ArrayAdapter adapter = new OverviewAdapter(this, items);

//        final ArrayList<Record> records = Lists.newArrayList(Record.findAll(Record.class));
//        final ArrayAdapter adapter = new RecordAdapter(this, records);
        listView.setAdapter(adapter);
    }

    private int getMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH);
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

package at.foobartech.wheremymoneyat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.collect.Lists;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import at.foobartech.wheremymoneyat.model.Category;
import at.foobartech.wheremymoneyat.model.Record;
import at.foobartech.wheremymoneyat.view.activity.TransactionActivity;
import at.foobartech.wheremymoneyat.view.activity.CategoryActivity;
import at.foobartech.wheremymoneyat.view.activity.DetailActivity;
import at.foobartech.wheremymoneyat.view.adapter.OverviewAdapter;
import at.foobartech.wheremymoneyat.view.viewmodel.OverviewItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class HomeActivity extends AppCompatActivity {


    @BindView(R.id.add)
    FloatingActionButton add;

    @BindView(R.id.listView)
    ListView listView;

    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SugarContext.init(this);
        ButterKnife.bind(this);

        selectedDate = Calendar.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        setTitle(title());

        final ArrayList<Category> allCategories = Lists.newArrayList(Category.findAll(Category.class));

        final ArrayList<OverviewItem> items = new ArrayList<>();
        int totalAmount = 0;
        for (final Category category : allCategories) {
            final String[] args = {category.getId().toString(), Integer.toString(selectedDate.get(Calendar.MONTH))};
            final List<Record> records = Record.find(Record.class, "category = ? AND month = ?", args);

            if (!records.isEmpty()) {
                int totalAmountCategory = records.stream().map(Record::getAmount).reduce(0, (a, b) -> a + b);
                totalAmount += totalAmountCategory;
                items.add(new OverviewItem(category.getName(), totalAmountCategory, category.getId()));
            }
        }

        Collections.sort(items, (o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName()));
        items.add(0, new OverviewItem("Total", totalAmount, -1));

        final ArrayAdapter adapter = new OverviewAdapter(this, items);
        listView.setAdapter(adapter);
    }

    private String title() {
        return selectedDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }

    @OnClick(R.id.add)
    void handleAdd(View view) {
        final Intent intent = new Intent(this, TransactionActivity.class);
        this.startActivity(intent);
    }

    @OnItemClick(R.id.listView)
    void handleItemClick(AdapterView<?> parent, View view, int position, long id) {
        OverviewItem item = (OverviewItem) listView.getItemAtPosition(position);
        final Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("month", selectedDate.get(Calendar.MONTH));
        intent.putExtra("categoryId", item.getCategoryId());
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
        int id = item.getItemId();

        switch (id) {
            case R.id.action_categories:
                final Intent intent = new Intent(this, CategoryActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.action_next_month:
                if (!isCurrentMonth()) {
                    selectedDate.add(Calendar.MONTH, 1);
                    refreshView();
                }
                return true;

            case R.id.action_prev_month:
                selectedDate.add(Calendar.MONTH, -1);
                refreshView();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isCurrentMonth() {
        final Calendar thisMonth = Calendar.getInstance();
        return selectedDate.get(Calendar.MONTH) == thisMonth.get(Calendar.MONTH);
    }
}

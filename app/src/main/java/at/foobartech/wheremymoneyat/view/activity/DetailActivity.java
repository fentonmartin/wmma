package at.foobartech.wheremymoneyat.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import at.foobartech.wheremymoneyat.R;
import at.foobartech.wheremymoneyat.model.Category;
import at.foobartech.wheremymoneyat.model.Record;
import at.foobartech.wheremymoneyat.view.adapter.DetailAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());


    private Category category;
    private int month;

    @BindView(R.id.listView)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        parseIntent();
        refreshView();
    }

    private void refreshView() {
        setTitle(title());

        final List<Record> records;
        if (category != null) {
            final String[] args = {category.getId().toString(), Integer.toString(month)};
            records = Record.find(Record.class, "category = ? AND month = ?", args);
        } else {
            records = Record.find(Record.class, "month = ?", Integer.toString(month));
        }

        final DetailAdapter detailAdapter = DetailAdapter.create(this, records);
        listView.setAdapter(detailAdapter);
    }

    private String title() {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        return DATE_FORMAT.format(c.getTime());
    }

    private void parseIntent() {
        long categoryId = getIntent().getLongExtra("categoryId", -1);
        if (categoryId != -1) {
            this.category = Category.findById(Category.class, categoryId);
        } else {
            category = null;
        }

        int month = getIntent().getIntExtra("month", -1);
        if (month != -1) {
            this.month = month;
        } else {
            final Calendar c = Calendar.getInstance();
            this.month = c.get(Calendar.MONTH);
        }
    }
}
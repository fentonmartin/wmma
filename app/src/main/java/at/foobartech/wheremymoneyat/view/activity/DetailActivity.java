package at.foobartech.wheremymoneyat.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.Calendar;
import java.util.List;

import at.foobartech.wheremymoneyat.R;
import at.foobartech.wheremymoneyat.WMMAUtils;
import at.foobartech.wheremymoneyat.model.Category;
import at.foobartech.wheremymoneyat.model.Record;
import at.foobartech.wheremymoneyat.view.adapter.DetailAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private Category category;
    private int month;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        initToolbar();
        initRecyclerView();
        parseIntent();
        refreshView();
    }

    private void initRecyclerView() {
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

        final DetailAdapter detailAdapter = new DetailAdapter(records);
        recyclerView.setAdapter(detailAdapter);
    }

    private String title() {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        return WMMAUtils.formatDatePretty(c.getTime());
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

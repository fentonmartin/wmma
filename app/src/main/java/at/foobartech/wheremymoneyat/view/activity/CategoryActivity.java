package at.foobartech.wheremymoneyat.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.collect.Lists;

import java.util.ArrayList;

import at.foobartech.wheremymoneyat.R;
import at.foobartech.wheremymoneyat.model.Category;
import at.foobartech.wheremymoneyat.view.adapter.CategoryAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity {

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.add)
    FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populate();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return true;
    }

    private void populate() {
        final ArrayList<Category> categories = Lists.newArrayList(Category.findAll(Category.class));
        final ArrayAdapter adapter = new CategoryAdapter(this, categories);
        listView.setAdapter(adapter);
    }
}

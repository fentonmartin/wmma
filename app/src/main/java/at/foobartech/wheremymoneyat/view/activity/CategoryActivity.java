package at.foobartech.wheremymoneyat.view.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.common.collect.Lists;

import java.util.ArrayList;

import at.foobartech.wheremymoneyat.R;
import at.foobartech.wheremymoneyat.model.Category;
import at.foobartech.wheremymoneyat.view.adapter.CategoryAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @OnClick(R.id.add)
    void handleAdd(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.createCategoryTitle);
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(R.string.buttonOK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                new Category(name).save();
            }
        });
        builder.setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}

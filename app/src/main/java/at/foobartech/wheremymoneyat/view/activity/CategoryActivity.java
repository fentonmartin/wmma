package at.foobartech.wheremymoneyat.view.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import at.foobartech.wheremymoneyat.R;
import at.foobartech.wheremymoneyat.model.Category;
import at.foobartech.wheremymoneyat.model.Record;
import at.foobartech.wheremymoneyat.view.adapter.CategoryAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryActivity extends AppCompatActivity {

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.add)
    FloatingActionButton add;

    private ArrayAdapter listViewAdapter;
    private ActionMode mActionMode;

    private final AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (mActionMode != null) {
                return false;
            }

            mActionMode = startSupportActionMode(mActionModeCallback);
            listView.setItemChecked(position, true);
            return true;
        }
    };

    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            mode.getMenuInflater().inflate(R.menu.context_category, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    deleteSelectedCategory();
                    mActionMode.finish();
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            listView.clearChoices();
            listView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
            refreshListView();
            mActionMode.finish();
            mActionMode = null;
        }
    };


    private void deleteSelectedCategory() {
        if (listView.getCheckedItemCount() <= 0) {
            return;
        }

        final Category category = (Category) listView.getItemAtPosition(listView.getCheckedItemPosition());
        if (category != null) {
            final List<Record> records = Record.find(Record.class, "category = ?", category.getId().toString());

            if (records == null || records.isEmpty()) {
                category.delete();
                refreshListView();
            } else {
                Toast.makeText(this, String.format(this.getString(R.string.errorCategoryDelete), category.getName()), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createCategory(String name) {
        new Category(name).save();
        refreshListView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        listView.setOnItemLongClickListener(longClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListView();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }

    private void refreshListView() {
        final ArrayList<Category> categories = Lists.newArrayList(Category.findAll(Category.class));


        listViewAdapter = new CategoryAdapter(this, categories);
        listView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.add)
    void handleAdd(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.createCategoryTitle);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.buttonOK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = input.getText().toString();
                createCategory(name);
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

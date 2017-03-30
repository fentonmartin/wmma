package at.foobartech.wheremymoneyat.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import at.foobartech.wheremymoneyat.R;
import at.foobartech.wheremymoneyat.model.Category;
import at.foobartech.wheremymoneyat.model.Record;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @BindView(R.id.tv_amount)
    TextView amountTextView;

    @BindView(R.id.et_date)
    MaterialEditText etDate;

    @BindView(R.id.spinner_category)
    Spinner spinnerCategory;

    @BindView(R.id.et_note)
    MaterialEditText etNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        ButterKnife.bind(this);

        etDate.setText(DATE_FORMAT.format(new Date()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        populate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                createNewRecord();
                break;
        }
        return true;
    }

    private void createNewRecord() {
        try {
            final int amount = getAmount();
            final String note = getNote();
            final Date date = getDate();
            final Category category = getCategory();

            final Record newRecord = new Record(amount, date, category);
            newRecord.setNote(note);
            newRecord.save();
            Toast.makeText(this, "Record saved!", Toast.LENGTH_SHORT).show();

            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    private Category getCategory() {
        final Category selectedItem = (Category) spinnerCategory.getSelectedItem();
        return selectedItem;
    }

    private Date getDate() {
        try {
            return DATE_FORMAT.parse(etDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getNote() {
        return etNote.getText().toString();
    }

    private int getAmount() {
        return (int) (Double.parseDouble(amountTextView.getText().toString()) * 100);
    }

    private void populate() {
        final Iterator<Category> allCategories = Category.findAll(Category.class);
        final ArrayList<Category> categories = Lists.newArrayList(allCategories);
        final ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_record, menu);
        return true;
    }

    @OnClick(R.id.et_date)
    void onDateClick(View view) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        etDate.setText(DATE_FORMAT.format(c.getTime()));
    }

}

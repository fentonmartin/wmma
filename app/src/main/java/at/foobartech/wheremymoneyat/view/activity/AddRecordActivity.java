package at.foobartech.wheremymoneyat.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import at.foobartech.wheremymoneyat.R;
import at.foobartech.wheremymoneyat.model.Category;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class AddRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @BindView(R.id.tv_amount)
    TextView amountTextView;

    @BindView(R.id.rg_categories)
    RadioGroup rgCategories;

    @BindView(R.id.et_date)
    EditText etDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        ButterKnife.bind(this);

        etDate.setText(DATE_FORMAT.format(new Date()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        populate();
    }

    private void populate() {
        final Iterator<Category> allCategories = Category.findAll(Category.class);

        while (allCategories.hasNext()) {
            final Category category = allCategories.next();

            final RadioButton radioButton = new RadioButton(this);
            radioButton.setText(category.getName());
            radioButton.setId(category.getId().intValue()); //there will *never* be more thant 2^31 categories
            rgCategories.addView(radioButton);
        }
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

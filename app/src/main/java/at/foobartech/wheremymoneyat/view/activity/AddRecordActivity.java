package at.foobartech.wheremymoneyat.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.common.collect.Lists;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import at.foobartech.wheremymoneyat.R;
import at.foobartech.wheremymoneyat.model.Category;
import at.foobartech.wheremymoneyat.model.Record;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, Validator.ValidationListener {

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @NotEmpty
    @BindView(R.id.tv_amount)
    TextView etAmount;

    @NotEmpty
    @BindView(R.id.et_date)
    MaterialEditText etDate;

    @NotEmpty
    @BindView(R.id.et_category)
    MaterialEditText etCategory;

    @BindView(R.id.et_note)
    MaterialEditText etNote;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        ButterKnife.bind(this);

        validator = new Validator(this);
        validator.setValidationListener(this);

        etDate.setText(DATE_FORMAT.format(new Date()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_done:
                createNewTransaction();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_record, menu);
        return true;
    }

    @OnClick(R.id.et_date)
    void onDateClicked(View view) {
        final Calendar now = Calendar.getInstance();
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
        final Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        etDate.setText(DATE_FORMAT.format(c.getTime()));
    }

    @OnClick(R.id.et_category)
    void onCategoryClicked() {
        final Iterator<Category> allCategories = Category.findAll(Category.class);
        final ArrayList<Category> categories = Lists.newArrayList(allCategories);
        new MaterialDialog.Builder(this)
                .title(R.string.category)
                .items(categories)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        etCategory.setText(categories.get(which).getName());
                    }
                })
                .show();
    }

    @Override
    public void onValidationSucceeded() {
        try {
            final int amount = getAmount();
            final Date date = parseDate();
            final Category category = parseCategory();
            final String note = parseNote();

            final Record newRecord = new Record(amount, date, category);
            newRecord.setNote(note);
            newRecord.save();
            Toast.makeText(this, R.string.transactionCreated, Toast.LENGTH_SHORT).show();

            finish();
        } catch (Exception e) {
            Toast.makeText(this, R.string.errorGeneric, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (final ValidationError error : errors) {
            final View view = error.getView();
            final String message = error.getCollatedErrorMessage(this);

            if (view instanceof MaterialEditText) {
                ((MaterialEditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void createNewTransaction() {
        validator.validate();
    }

    private Category parseCategory() {
        final List<Category> categories = Category.find(Category.class, "name = ?", etCategory.getText().toString());
        if (categories.isEmpty()) {
            throw new IllegalStateException("Category must not be null!");
        }
        return categories.get(0);
    }

    private Date parseDate() throws ParseException {
        return DATE_FORMAT.parse(etDate.getText().toString());
    }

    private String parseNote() {
        return etNote.getText().toString();
    }

    private int getAmount() {
        return (int) (Double.parseDouble(etAmount.getText().toString()) * 100);
    }
}

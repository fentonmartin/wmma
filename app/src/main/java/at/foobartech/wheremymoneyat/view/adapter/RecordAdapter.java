package at.foobartech.wheremymoneyat.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import at.foobartech.wheremymoneyat.model.Record;
import at.foobartech.wheremymoneyat.model.Tag;

/**
 * @author Thomas Feichtinger
 */
public class RecordAdapter extends ArrayAdapter<Record> {

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("E dd-MM-yy", Locale.getDefault());

    public RecordAdapter(Context context, ArrayList<Record> records) {
        super(context, 0, records);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        final Record r = getItem(position);
        if (r == null) {
            return convertView;
        }

        final TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
        final TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);

        text1.setText(String.format(Locale.getDefault(), "%.2f", r.getAmount() / 100d));

        final StringBuilder builder = new StringBuilder();

        builder.append(DATE_FORMAT.format(r.getDate()));
        builder.append(" | ");
        builder.append(r.getCategory().getName());
        builder.append(" | ");
        if (r.getTags() != null) {
            for (Tag tag : r.getTags()) {
                builder.append(tag.getName());
                builder.append(" ");
            }
        }

        text2.setText(builder.toString());

        return convertView;
    }
}

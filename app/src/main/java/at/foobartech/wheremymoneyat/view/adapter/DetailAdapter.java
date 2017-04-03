package at.foobartech.wheremymoneyat.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import at.foobartech.wheremymoneyat.R;
import at.foobartech.wheremymoneyat.model.Record;
import at.foobartech.wheremymoneyat.view.viewmodel.DetailItem;

/**
 * @author Thomas Feichtinger
 */

public class DetailAdapter extends ArrayAdapter<DetailItem> {

    public DetailAdapter(Context context, ArrayList<DetailItem> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final DetailItem item = getItem(position);
        if (item != null) {

            switch (item.getType()) {
                case DIVIDER:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_detail_divider, parent, false);
                    break;
                case ITEM:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_detail_item, parent, false);
            }

            final TextView title = (TextView) convertView.findViewById(R.id.tv_title);
            final TextView amount = (TextView) convertView.findViewById(R.id.tv_amount);

            amount.setText(String.format(Locale.getDefault(), "%.2f", item.getAmount() / 100d));
            title.setText(item.getTitle());
        }
        return convertView;
    }

    public static DetailAdapter create(Context context, final List<Record> allRecords) {
        final ImmutableListMultimap<Date, Record> groupedRecords = Multimaps.index(allRecords, Record::getDateWithoutTime);

        final ArrayList<DetailItem> result = new ArrayList<>();

        final ArrayList<Date> dates = Lists.newArrayList(groupedRecords.keySet());
        Collections.sort(dates, (o1, o2) -> o2.compareTo(o1));
        for (final Date date : dates) {
            final ImmutableList<Record> records = groupedRecords.get(date);
            int totalAmount = records.stream().map(Record::getAmount).reduce(0, (a, b) -> a + b);
            result.add(DetailItem.createDivider(date, totalAmount));

            for (Record r : records) {
                result.add(DetailItem.createItem(r.getCategory().getName(), r.getAmount(), r.getNote()));
            }
        }
        return new DetailAdapter(context, result);
    }
}

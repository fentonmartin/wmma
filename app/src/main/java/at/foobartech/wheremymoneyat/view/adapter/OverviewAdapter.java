package at.foobartech.wheremymoneyat.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import at.foobartech.wheremymoneyat.R;
import at.foobartech.wheremymoneyat.view.viewmodel.OverviewItem;

/**
 * @author Thomas Feichtinger
 */
public class OverviewAdapter extends ArrayAdapter<OverviewItem> {

    public OverviewAdapter(Context context, ArrayList<OverviewItem> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_overview, parent, false);
        }

        final OverviewItem item = getItem(position);
        if (item != null) {
            final TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            final TextView tvAmount = (TextView) convertView.findViewById(R.id.tv_amount);

            tvTitle.setText(item.getName());
            tvAmount.setText(String.format(Locale.getDefault(), "%.2f", item.getTotalAmount() / 100d));

            if (position == 0) {
                tvTitle.setTypeface(null, Typeface.BOLD);
                tvAmount.setTypeface(null, Typeface.BOLD);
                tvAmount.setTextColor(convertView.getResources().getColor(R.color.colorAccent));
            }
        }
        return convertView;

    }
}

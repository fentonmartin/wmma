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

import at.foobartech.wheremymoneyat.model.Category;

/**
 * @author Thomas Feichtinger
 */
public class CategoryAdapter extends ArrayAdapter<Category> {

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("E dd-MM-yy", Locale.getDefault());

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        super(context, 0, categories);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_activated_1, parent, false);
        }

        final Category category = getItem(position);
        if (category != null) {
            final TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
            text1.setText(String.format("%s", category.getName()));
        }
        return convertView;
    }
}

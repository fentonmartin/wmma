package at.foobartech.wheremymoneyat.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import at.foobartech.wheremymoneyat.R;
import at.foobartech.wheremymoneyat.WMMAUtils;
import at.foobartech.wheremymoneyat.model.Record;

/**
 * @author Thomas Feichtinger
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {

    private final List<Date> titles;
    private final List<Integer> amounts;
    private final List<ImmutableList<Record>> records;
    private final boolean[] expandedState;

    public DetailAdapter(final List<Record> records) {
        final ImmutableListMultimap<Date, Record> groupedRecords = Multimaps.index(records, Record::getDateWithoutTime);
        final List<Date> dates = Lists.newArrayList(groupedRecords.keySet());
        Collections.sort(dates, (d1, d2) -> d2.compareTo(d1));

        this.titles = new ArrayList<>();
        this.records = new ArrayList<>();
        this.amounts = new ArrayList<>();
        for (final Date d : dates) {
            this.titles.add(d);
            ImmutableList<Record> r = groupedRecords.get(d);
            this.amounts.add(r.stream().map(Record::getAmount).reduce(0, (a, b) -> a + b));
            this.records.add(r);
        }
        this.expandedState = new boolean[titles.size()];
        expandedState[0] = true;
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_detail, parent, false);
        return new DetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {
        holder.tvTitle.setText(WMMAUtils.formatDatePretty(titles.get(position)));
        holder.tvAmount.setText(WMMAUtils.formatAmount(amounts.get(position)));

        holder.llContent.setVisibility(expandedState[holder.getAdapterPosition()] ? View.VISIBLE : View.GONE);

        holder.tvTitle.setOnClickListener(v -> {
            int i = holder.getAdapterPosition();
            expandedState[i] = !expandedState[i];
            notifyDataSetChanged();
        });

        holder.llContent.removeAllViews();
        for (final Record r : records.get(position)) {
            final View view = LayoutInflater.from(holder.context).inflate(R.layout.item_detail_item, null);
            final TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            final TextView tvAmount = (TextView) view.findViewById(R.id.tv_amount);
            tvTitle.setText(r.getCategory().getName());
            tvAmount.setText(WMMAUtils.formatAmount(r.getAmount()));
            holder.llContent.addView(view);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    static class DetailViewHolder extends RecyclerView.ViewHolder {

        final TextView tvTitle;
        final TextView tvAmount;
        final LinearLayout llContent;
        private final Context context;

        DetailViewHolder(final View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            this.tvAmount = (TextView) itemView.findViewById(R.id.tv_amount);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            this.llContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
        }
    }

}

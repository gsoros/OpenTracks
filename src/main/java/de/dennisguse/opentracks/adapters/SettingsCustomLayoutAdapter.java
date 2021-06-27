package de.dennisguse.opentracks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.util.StatsUtils;

public class SettingsCustomLayoutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LinkedHashMap<String, Boolean> prefStatsItems;
    private final Context context;
    private final SettingsCustomLayoutItemClickListener itemClickListener;

    public SettingsCustomLayoutAdapter(Context context, SettingsCustomLayoutItemClickListener itemClickListener, LinkedHashMap<String, Boolean> prefStatsItems) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.prefStatsItems = prefStatsItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_stats_item, parent, false);
        return new SettingsCustomLayoutAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SettingsCustomLayoutAdapter.ViewHolder viewHolder = (SettingsCustomLayoutAdapter.ViewHolder) holder;
        String statTitle = (String) prefStatsItems.keySet().toArray()[position];
        boolean isVisible = prefStatsItems.get(statTitle);
        viewHolder.itemView.setTag(statTitle);
        viewHolder.title.setText(statTitle);
        viewHolder.value.setText(StatsUtils.emptyValue(context, statTitle));

        viewHolder.title.setTextAppearance(context, isVisible ? R.style.StatsLabel : R.style.StatsLabelNotVisible);
        viewHolder.value.setTextAppearance(context, isVisible ? R.style.StatsValue : R.style.StatsValueNotVisible);
        viewHolder.statusIcon.setVisibility(isVisible ? View.GONE : View.VISIBLE);
        viewHolder.statusIcon.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_visibility_off_24));
        viewHolder.moveIcon.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        if (prefStatsItems == null) {
            return 0;
        } else {
            return prefStatsItems.size();
        }
    }

    public void swapValues(LinkedHashMap<String, Boolean> data) {
        this.prefStatsItems = data;
        if (this.prefStatsItems != null) {
            this.notifyDataSetChanged();
        }
    }

    public LinkedHashMap<String, Boolean> move(int fromPosition, int toPosition) {
        List<String> keys = new ArrayList<>(prefStatsItems.keySet());
        List<Boolean> values = new ArrayList<>(prefStatsItems.values());

        String keyToMove = keys.remove(fromPosition);
        keys.add(toPosition, keyToMove);
        Boolean valueToMove = values.remove(fromPosition);
        values.add(toPosition, valueToMove);

        prefStatsItems = new LinkedHashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            prefStatsItems.put(keys.get(i), values.get(i));
        }

        notifyItemMoved(fromPosition, toPosition);

        return prefStatsItems;
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView title;
        final TextView value;
        final TextView unit;
        final ImageView statusIcon;
        final ImageView moveIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.stats_desc_main);
            value = itemView.findViewById(R.id.stats_value);
            unit = itemView.findViewById(R.id.stats_unit);
            statusIcon = itemView.findViewById(R.id.stats_icon_show_status);
            moveIcon = itemView.findViewById(R.id.stats_iv_drag_indicator);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String statTitle = (String) view.getTag();
            itemClickListener.onSettingsCustomLayoutItemClicked(statTitle);
        }
    }

    public interface SettingsCustomLayoutItemClickListener {
        void onSettingsCustomLayoutItemClicked(@NonNull String title);
    }
}

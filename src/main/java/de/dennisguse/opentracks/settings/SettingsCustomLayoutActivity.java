package de.dennisguse.opentracks.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;

import de.dennisguse.opentracks.AbstractActivity;
import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.adapters.SettingsCustomLayoutAdapter;
import de.dennisguse.opentracks.databinding.ActivitySettingsCustomLayoutBinding;
import de.dennisguse.opentracks.util.PreferencesUtils;

public class SettingsCustomLayoutActivity extends AbstractActivity implements SettingsCustomLayoutAdapter.SettingsCustomLayoutItemClickListener {

    private ActivitySettingsCustomLayoutBinding viewBinding;
    private SettingsCustomLayoutAdapter settingsCustomLayoutAdapter;
    private SharedPreferences sharedPreferences;
    private LinkedHashMap<String, Boolean> prefStatsItems = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferencesUtils.getSharedPreferences(this);
        prefStatsItems = PreferencesUtils.getCustomLayout(sharedPreferences, this);
        settingsCustomLayoutAdapter = new SettingsCustomLayoutAdapter(this, this, prefStatsItems);

        RecyclerView recyclerView = viewBinding.recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.stats_grid_columns)));
        recyclerView.setAdapter(settingsCustomLayoutAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                prefStatsItems = settingsCustomLayoutAdapter.move(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!prefStatsItems.isEmpty()) {
            PreferencesUtils.setCustomLayout(sharedPreferences, this, prefStatsItems);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences = null;
        prefStatsItems = null;
    }

    @Override
    protected void setupActionBarBack(Toolbar toolbar) {
        super.setupActionBarBack(toolbar);
        toolbar.setTitle(R.string.menu_settings);
    }

    @Override
    protected View getRootView() {
        viewBinding = ActivitySettingsCustomLayoutBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }

    @Override
    public void onSettingsCustomLayoutItemClicked(@NonNull String title) {
        prefStatsItems.put(title, !prefStatsItems.get(title));
        settingsCustomLayoutAdapter.swapValues(prefStatsItems);
    }
}

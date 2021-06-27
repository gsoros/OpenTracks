package de.dennisguse.opentracks.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;

import de.dennisguse.opentracks.AbstractActivity;
import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.adapters.SettingsCustomLayoutAdapter;
import de.dennisguse.opentracks.databinding.ActivitySettingsCustomLayoutBinding;
import de.dennisguse.opentracks.util.PreferencesUtils;
import de.dennisguse.opentracks.util.StatsUtils;

public class SettingsCustomLayoutActivity extends AbstractActivity implements SettingsCustomLayoutAdapter.SettingsCustomLayoutItemClickListener {

    private ActivitySettingsCustomLayoutBinding viewBinding;
    private SettingsCustomLayoutAdapter adapterVisible;
    private SettingsCustomLayoutAdapter adapterNotVisible;
    private LinkedHashMap<String, Boolean> prefStatsVisible = new LinkedHashMap<>();
    private LinkedHashMap<String, Boolean> prefStatsNotVisible = new LinkedHashMap<>();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferencesUtils.getSharedPreferences(this);

        // Recycler view with visible stats.
        prefStatsVisible = StatsUtils.filterVisible(PreferencesUtils.getCustomLayout(sharedPreferences, this), true);
        adapterVisible = new SettingsCustomLayoutAdapter(this, this, prefStatsVisible);

        RecyclerView recyclerViewVisible = viewBinding.recyclerViewVisible;
        recyclerViewVisible.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.stats_grid_columns)));
        recyclerViewVisible.setAdapter(adapterVisible);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                prefStatsVisible = adapterVisible.move(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewVisible);

        // Spinner with items per row.
        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new Integer[]{1, 2, 3});
        viewBinding.spinnerOptions.setAdapter(spinnerAdapter);
        viewBinding.spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recyclerViewVisible.setLayoutManager(new GridLayoutManager(SettingsCustomLayoutActivity.this, position + 1));
                PreferencesUtils.setLayoutColumns(sharedPreferences, SettingsCustomLayoutActivity.this, position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewBinding.spinnerOptions.setSelection(PreferencesUtils.getLayoutColumns(sharedPreferences, SettingsCustomLayoutActivity.this) - 1);

        // Recycler view with not visible stats.
        prefStatsNotVisible = StatsUtils.filterVisible(PreferencesUtils.getCustomLayout(sharedPreferences, this), false);
        adapterNotVisible = new SettingsCustomLayoutAdapter(this, this, prefStatsNotVisible);
        RecyclerView recyclerViewNotVisible = viewBinding.recyclerViewNotVisible;
        recyclerViewNotVisible.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotVisible.setAdapter(adapterNotVisible);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!prefStatsVisible.isEmpty()) {
            LinkedHashMap<String, Boolean> maps = new LinkedHashMap<>();
            maps.putAll(prefStatsVisible);
            maps.putAll(prefStatsNotVisible);
            PreferencesUtils.setCustomLayout(sharedPreferences, this, maps);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences = null;
        prefStatsVisible = null;
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
        if (prefStatsVisible.containsKey(title)) {
            prefStatsVisible.remove(title);
            prefStatsNotVisible.put(title, false);
        } else if (prefStatsNotVisible.containsKey(title)) {
            prefStatsNotVisible.remove(title);
            prefStatsVisible.put(title, true);
            viewBinding.scrollView.fullScroll(ScrollView.FOCUS_UP);
        } else {
            return;
        }

        adapterVisible.swapValues(prefStatsVisible);
        adapterNotVisible.swapValues(prefStatsNotVisible);
    }
}

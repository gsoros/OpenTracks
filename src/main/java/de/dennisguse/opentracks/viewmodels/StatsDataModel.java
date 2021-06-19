package de.dennisguse.opentracks.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import de.dennisguse.opentracks.services.TrackRecordingService;

public class StatsDataModel extends AndroidViewModel {

    private MutableLiveData<List<StatsData>> statsData;

    public StatsDataModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<StatsData>> getStatsData() {
        if (statsData == null) {
            statsData = new MutableLiveData<>();
        }
        return statsData;
    }

    public void update(TrackRecordingService.RecordingData recordingData, LinkedHashMap<String, Boolean> prefStatsItems, boolean metricUnit) {
        new Thread(() -> {
            List<String> statsOrderList = new ArrayList<>(prefStatsItems.keySet());
            List<StatsData> statsDataList = StatsBuilder.fromRecordingData(getApplication(), recordingData, statsOrderList, metricUnit).stream().filter(i -> prefStatsItems.get(i.getDescMain())).collect(Collectors.toList());
            statsData.postValue(statsDataList);
        }).start();
    }
}

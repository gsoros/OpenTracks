package de.dennisguse.opentracks.viewmodels;

import android.util.Pair;

public class StatsData {
    private final String value;
    private String unit;
    private final String descMain;
    private String descSecondary;

    public StatsData(String value, String descMain) {
        this.value = value;
        this.descMain = descMain;
    }

    public StatsData(Pair<String, String> valueAndUnit, String descMain) {
        this.value = valueAndUnit.first;
        this.unit = valueAndUnit.second;
        this.descMain = descMain;
    }

    public StatsData(String value, String unit, String descMain, String descSecondary) {
        this.value = value;
        this.unit = unit;
        this.descMain = descMain;
        this.descSecondary = descSecondary;
    }

    public String getDescMain() {
        return descMain;
    }

    public String getUnit() {
        return unit;
    }

    public String getDescSecondary() {
        return descSecondary;
    }

    public boolean hasDescSecondary() {
        return descSecondary != null;
    }

    public String getValue() {
        return value;
    }

    public boolean hasValue() {
        return value != null;
    }
}

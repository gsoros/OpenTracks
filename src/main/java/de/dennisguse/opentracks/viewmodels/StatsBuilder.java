package de.dennisguse.opentracks.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.content.data.Speed;
import de.dennisguse.opentracks.content.data.TrackPoint;
import de.dennisguse.opentracks.content.sensor.SensorDataSet;
import de.dennisguse.opentracks.services.TrackRecordingService;
import de.dennisguse.opentracks.util.StringUtils;

class StatsBuilder {

    public static List<StatsData> fromRecordingData(@NonNull Context context, @NonNull TrackRecordingService.RecordingData recordingData, @NonNull List<String> statsOrderItems, boolean metricUnits) {
        return statsOrderItems.stream().map(i -> StatsBuilder.build(context, recordingData, i, metricUnits)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static StatsData build(@NonNull Context context, @NonNull TrackRecordingService.RecordingData recordingData, @NonNull String statItemTitle, boolean metricUnits) {
        StatsData data = null;

        if (statItemTitle.equals(context.getString(R.string.stats_total_time))) {
            data = new StatsData(StringUtils.formatElapsedTime(recordingData.getTrackStatistics().getTotalTime()), context.getString(R.string.stats_total_time));
        } else if (statItemTitle.equals(context.getString(R.string.stats_moving_time))) {
            data = new StatsData(StringUtils.formatElapsedTime(recordingData.getTrackStatistics().getMovingTime()), context.getString(R.string.stats_moving_time));
        } else if (statItemTitle.equals(context.getString(R.string.stats_distance))) {
            data = new StatsData(StringUtils.getDistanceParts(context, recordingData.getTrackStatistics().getTotalDistance(), metricUnits), context.getString(R.string.stats_distance));
        } else if (statItemTitle.equals(context.getString(R.string.stats_speed))) {
            TrackPoint latestTrackPoint = recordingData.getLatestTrackPoint();
            Speed speed = latestTrackPoint != null && latestTrackPoint.hasSpeed() ? latestTrackPoint.getSpeed() : null;

            SensorDataSet sensorDataSet = recordingData.getSensorDataSet();
            if (sensorDataSet != null && sensorDataSet.getCyclingDistanceSpeed() != null && sensorDataSet.getCyclingDistanceSpeed().hasValue() && sensorDataSet.getCyclingDistanceSpeed().isRecent()) {
                speed = sensorDataSet.getCyclingDistanceSpeed().getValue().speed;
            }

            data = new StatsData(StringUtils.getSpeedParts(context, speed, metricUnits, true), context.getString(R.string.stats_speed));
        } else if (statItemTitle.equals(context.getString(R.string.stats_average_moving_speed))) {
            data = new StatsData(StringUtils.getSpeedParts(context, recordingData.getTrackStatistics().getAverageMovingSpeed(), metricUnits, true), context.getString(R.string.stats_average_moving_speed));
        } else if (statItemTitle.equals(context.getString(R.string.stats_average_speed))) {
            data = new StatsData(StringUtils.getSpeedParts(context, recordingData.getTrackStatistics().getAverageSpeed(), metricUnits, true), context.getString(R.string.stats_average_speed));
        } else if (statItemTitle.equals(context.getString(R.string.stats_max_speed))) {
            data = new StatsData(StringUtils.getSpeedParts(context, recordingData.getTrackStatistics().getMaxSpeed(), metricUnits, true), context.getString(R.string.stats_max_speed));
        } else if (statItemTitle.equals(context.getString(R.string.stats_pace))) {
            TrackPoint latestTrackPoint = recordingData.getLatestTrackPoint();
            Speed speed = latestTrackPoint != null && latestTrackPoint.hasSpeed() ? latestTrackPoint.getSpeed() : null;

            SensorDataSet sensorDataSet = recordingData.getSensorDataSet();
            if (sensorDataSet != null && sensorDataSet.getCyclingDistanceSpeed() != null && sensorDataSet.getCyclingDistanceSpeed().hasValue() && sensorDataSet.getCyclingDistanceSpeed().isRecent()) {
                speed = sensorDataSet.getCyclingDistanceSpeed().getValue().speed;
            }

            data = new StatsData(StringUtils.getSpeedParts(context, speed, metricUnits, false), context.getString(R.string.stats_pace));
        } else if (statItemTitle.equals(context.getString(R.string.stats_average_moving_pace))) {
            data = new StatsData(StringUtils.getSpeedParts(context, recordingData.getTrackStatistics().getAverageMovingSpeed(), metricUnits, false), context.getString(R.string.stats_average_moving_pace));
        } else if (statItemTitle.equals(context.getString(R.string.stats_average_pace))) {
            data = new StatsData(StringUtils.getSpeedParts(context, recordingData.getTrackStatistics().getAverageSpeed(), metricUnits, false), context.getString(R.string.stats_average_pace));
        } else if (statItemTitle.equals(context.getString(R.string.stats_fastest_pace))) {
            data = new StatsData(StringUtils.getSpeedParts(context, recordingData.getTrackStatistics().getMaxSpeed(), metricUnits, true), context.getString(R.string.stats_fastest_pace));
        } else if (statItemTitle.equals(context.getString(R.string.stats_altitude))) {
            TrackPoint latestTrackPoint = recordingData.getLatestTrackPoint();
            Float altitude = latestTrackPoint != null && latestTrackPoint.hasAltitude() ? (float) latestTrackPoint.getAltitude().toM() : null;
            data = new StatsData(StringUtils.formatAltitude(context, altitude, metricUnits), context.getString(R.string.stats_altitude));
        } else if (statItemTitle.equals(context.getString(R.string.stats_gain))) {
            data = new StatsData(StringUtils.formatAltitude(context, recordingData.getTrackStatistics().getTotalAltitudeGain(), metricUnits), context.getString(R.string.stats_gain));
        } else if (statItemTitle.equals(context.getString(R.string.stats_loss))) {
            data = new StatsData(StringUtils.formatAltitude(context, recordingData.getTrackStatistics().getTotalAltitudeLoss(), metricUnits), context.getString(R.string.stats_loss));
        } else if (statItemTitle.equals(context.getString(R.string.stats_latitude))) {
            TrackPoint latestTrackPoint = recordingData.getLatestTrackPoint();
            if (latestTrackPoint != null && latestTrackPoint.hasLocation()) {
                data = new StatsData(StringUtils.formatCoordinate(latestTrackPoint.getLatitude()), context.getString(R.string.stats_latitude));
            } else {
                data = new StatsData("-", context.getString(R.string.stats_latitude));
            }
        } else if (statItemTitle.equals(context.getString(R.string.stats_longitude))) {
            TrackPoint latestTrackPoint = recordingData.getLatestTrackPoint();
            if (latestTrackPoint != null && latestTrackPoint.hasLocation()) {
                data = new StatsData(StringUtils.formatCoordinate(latestTrackPoint.getLongitude()), context.getString(R.string.stats_longitude));
            } else {
                data = new StatsData("-", context.getString(R.string.stats_longitude));
            }
        } else if (statItemTitle.equals(context.getString(R.string.stats_sensors_heart_rate))) {
            SensorDataSet sensorDataSet = recordingData.getSensorDataSet();
            if (sensorDataSet != null && sensorDataSet.getHeartRate() != null && sensorDataSet.getHeartRate().hasValue() && sensorDataSet.getHeartRate().isRecent()) {
                data = new StatsData(
                        StringUtils.formatDecimal(sensorDataSet.getHeartRate().getValue(), 0),
                        context.getString(R.string.sensor_unit_beats_per_minute),
                        context.getString(R.string.stats_sensors_heart_rate),
                        sensorDataSet.getHeartRate().getSensorNameOrAddress());
            } else {
                data = new StatsData("-", context.getString(R.string.sensor_unit_beats_per_minute), context.getString(R.string.stats_sensors_heart_rate), "-");
            }
        } else if (statItemTitle.equals(context.getString(R.string.stats_sensors_cadence))) {
            SensorDataSet sensorDataSet = recordingData.getSensorDataSet();
            if (sensorDataSet != null && sensorDataSet.getCyclingCadence() != null && sensorDataSet.getCyclingCadence().hasValue() && sensorDataSet.getCyclingCadence().isRecent()) {
                data = new StatsData(
                        StringUtils.formatDecimal(sensorDataSet.getCyclingCadence().getValue(), 0),
                        context.getString(R.string.sensor_unit_rounds_per_minute),
                        context.getString(R.string.stats_sensors_cadence),
                        sensorDataSet.getCyclingCadence().getSensorNameOrAddress());
            } else {
                data = new StatsData("-", context.getString(R.string.sensor_unit_rounds_per_minute), context.getString(R.string.stats_sensors_cadence), "-");
            }
        } else if (statItemTitle.equals(context.getString(R.string.stats_sensors_power))) {
            SensorDataSet sensorDataSet = recordingData.getSensorDataSet();
            if (sensorDataSet != null && sensorDataSet.getCyclingPower() != null && sensorDataSet.getCyclingPower().hasValue() && sensorDataSet.getCyclingPower().isRecent()) {
                data = new StatsData(
                        StringUtils.formatDecimal(sensorDataSet.getCyclingPower().getValue(), 0),
                        context.getString(R.string.sensor_unit_power),
                        context.getString(R.string.stats_sensors_power),
                        sensorDataSet.getCyclingPower().getSensorNameOrAddress());
            } else {
                data = new StatsData("-", context.getString(R.string.sensor_unit_power), context.getString(R.string.stats_sensors_power), "-");
            }
        }

        return data;
    }
}

package org.uusoftware.kit;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SecondFragment extends Fragment implements SensorEventListener {

	private SensorManager mSensorManager = null;
	private boolean hasSensor;
	private XYPlot aprHistoryPlot = null;
	private SimpleXYSeries azimuthHistorySeries = null;
	private static final int HISTORY_SIZE = 250;
	TextView Magnetometer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		hasSensor = getActivity().getApplicationContext().getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);

		return;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_second, container, false);

		// AdMob
		AdView adView = (AdView) v.findViewById(R.id.adMob);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);

		Magnetometer = (TextView) v.findViewById(R.id.Magnetometer);

		if (!hasSensor) {
			Magnetometer.setText(getString(R.string.stringerror));
		}

		// Grafik
		aprHistoryPlot = (XYPlot) v.findViewById(R.id.magnetometerplot);
		azimuthHistorySeries = new SimpleXYSeries(
				getString(R.string.stringmagnetometer));
		azimuthHistorySeries.useImplicitXVals();
		aprHistoryPlot.addSeries(azimuthHistorySeries,
				new LineAndPointFormatter());
		aprHistoryPlot.setDomainStepValue(5);
		aprHistoryPlot.setTicksPerRangeLabel(3);
		aprHistoryPlot.setDomainLabel(getString(R.string.measurecount));
		aprHistoryPlot.getDomainLabelWidget().pack();
		aprHistoryPlot.setRangeLabel("µT");
		aprHistoryPlot.getRangeLabelWidget().pack();

		return v;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		mSensorManager = (SensorManager) getActivity().getSystemService(
				Activity.SENSOR_SERVICE);
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float X = event.values[0];
		float Y = event.values[1];
		float Z = event.values[2];

		double XYZ = Math.sqrt((X * X) + (Y * Y) + (Z * Z));
		DecimalFormat f = new DecimalFormat("##.000");

		Magnetometer.setText(f.format(XYZ) + " µT");

		// get rid the oldest sample in history:
		if (azimuthHistorySeries.size() > HISTORY_SIZE) {
			azimuthHistorySeries.removeFirst();
		}

		// add the latest history sample:
		azimuthHistorySeries.addLast(null, XYZ);
		aprHistoryPlot.redraw();

	}
}

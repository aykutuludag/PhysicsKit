package org.uusoftware.kit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class SplashScreen extends Activity {

	private InterstitialAd interstitial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		// AdMob
		AdRequest adRequest = new AdRequest.Builder().build();
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId("ca-app-pub-1576175228836763/5809854536");
		interstitial.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				displayInterstitial();
			}

			public void displayInterstitial() {
				if (interstitial.isLoaded()) {
					interstitial.show();
				}
			}

			@Override
			public void onAdClosed() {
				super.onAdClosed();
				Intent i = new Intent(SplashScreen.this, MainActivity.class);
				startActivity(i);
				finish();
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				super.onAdFailedToLoad(errorCode);
				Intent i = new Intent(SplashScreen.this, MainActivity.class);
				startActivity(i);
				finish();
			}

		});
		interstitial.loadAd(adRequest);

	}
}
package org.uusoftware.kit;

import java.util.ArrayList;

import org.uusoftware.kit.adapter.Child;
import org.uusoftware.kit.adapter.ExpandListAdapter;
import org.uusoftware.kit.adapter.Group;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AboutUsFragment extends Fragment {

	String str = "https://www.facebook.com/uusoftware";
	String str2 = "https://twitter.com/uusoftware1";

	private ExpandListAdapter ExpAdapter;
	private ArrayList<Group> ExpListItems;
	private ExpandableListView ExpandList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_aboutus, container, false);

		// AdMob
		AdView adView = (AdView) v.findViewById(R.id.adMob);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);

		ExpandList = (ExpandableListView) v.findViewById(R.id.exp_list);
		ExpListItems = SetStandardGroups();
		ExpAdapter = new ExpandListAdapter(getActivity(), ExpListItems);
		ExpandList.setAdapter(ExpAdapter);
		ExpandList.expandGroup(0);
		ExpandList.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if (groupPosition == 0) {
					// Do nothing
				}
				if (groupPosition == 1) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(str));
					startActivity(intent);
				}
				if (groupPosition == 2) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(str2));
					startActivity(intent);
				}
				if (groupPosition == 3) {
					Intent i = new Intent(Intent.ACTION_SEND);
					i.setType("plain/text");
					i.putExtra(Intent.EXTRA_EMAIL,
							new String[] { "uusoftware@outlook.com" });
					i.putExtra(Intent.EXTRA_SUBJECT, "Physics Kit");
					try {
						startActivity(Intent.createChooser(i,
								"Send an e-mail..."));
					} catch (android.content.ActivityNotFoundException ex) {
						Toast.makeText(
								getActivity(),
								"There is no e-mail application on your device",
								Toast.LENGTH_SHORT).show();
					}
				}

				return true;
			}
		});

		return v;

	}

	public ArrayList<Group> SetStandardGroups() {

		String group_names[] = getResources().getStringArray(R.array.array1);
		String country_names[] = getResources().getStringArray(R.array.array2);

		int Images[] = { R.drawable.ic_uulogo, R.drawable.ic_facebook,
				R.drawable.ic_twitter, R.drawable.ic_mail };

		ArrayList<Group> list = new ArrayList<Group>();

		ArrayList<Child> ch_list;

		int size = 1;
		int j = 0;

		for (String group_name : group_names) {
			Group gru = new Group();
			gru.setName(group_name);

			ch_list = new ArrayList<Child>();
			for (; j < size; j++) {
				Child ch = new Child();
				ch.setName(country_names[j]);
				ch.setImage(Images[j]);
				ch_list.add(ch);
			}
			gru.setItems(ch_list);
			list.add(gru);

			size = size + 1;
		}

		return list;
	}

};
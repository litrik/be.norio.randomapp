/**
 *	Copyright 2013 Norio bvba
 *
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package be.norio.randomapp.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import be.norio.randomapp.R;
import be.norio.randomapp.util.PrefsUtils;

public class MainActivity extends Activity implements OnClickListener {

	private PackageManager mPackageManager;
	private String mPackageName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPackageManager = getPackageManager();
		mPackageName = PrefsUtils.getPreviousPackageName();

		findViewById(R.id.main_new).setOnClickListener(this);
		if (TextUtils.isEmpty(mPackageName)) {
			noPrevApp();
			return;
		}

		ApplicationInfo info;
		try {
			info = mPackageManager.getApplicationInfo(mPackageName, PackageManager.GET_META_DATA);
			mPackageManager.getApplicationLabel(info);
			((TextView) findViewById(R.id.main_name)).setText(mPackageManager.getApplicationLabel(info));
			((TextView) findViewById(R.id.main_date)).setText(android.text.format.DateUtils.getRelativeTimeSpanString(PrefsUtils
					.getPreviousDate()));
			((ImageView) findViewById(R.id.main_icon)).setImageDrawable(mPackageManager.getApplicationIcon(info));
			findViewById(R.id.main_app_details).setOnClickListener(this);
			findViewById(R.id.main_info).setOnClickListener(this);
			findViewById(R.id.main_uninstall).setOnClickListener(this);
			findViewById(R.id.main_playstore).setOnClickListener(this);
		} catch (NameNotFoundException e) {
			noPrevApp();
		}

	}

	private void noPrevApp() {
		findViewById(R.id.main_previous_app).setVisibility(View.GONE);
		findViewById(R.id.main_first_run).setVisibility(View.VISIBLE);
	}

	protected void launchRandomApp() {
		final String app = getRandomApp();
		PrefsUtils.setPreviousDate(System.currentTimeMillis());
		PrefsUtils.setPreviousPackageName(app);
		launchApp(app);
	}

	private void launchApp(String packageName) {
		try {
			final Intent intent = mPackageManager.getLaunchIntentForPackage(packageName);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getRandomApp() {
		List<ApplicationInfo> installedApps = mPackageManager.getInstalledApplications(PackageManager.GET_META_DATA);
		List<ApplicationInfo> launchableApps = new ArrayList<ApplicationInfo>();
		for (ApplicationInfo applicationInfo : installedApps) {
			final Intent intent = mPackageManager.getLaunchIntentForPackage(applicationInfo.packageName);
			if (intent != null) {
				launchableApps.add(applicationInfo);
			}
		}
		Random r = new Random();
		ApplicationInfo applicationInfo = launchableApps.get(r.nextInt(launchableApps.size()));
		return applicationInfo.packageName;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_app_details:
			launchApp(mPackageName);
			break;
		case R.id.main_new:
			launchRandomApp();
			break;
		case R.id.main_uninstall:
			uninstallApp(mPackageName);
			break;
		case R.id.main_playstore:
			openPlayStore(mPackageName);
			break;
		case R.id.main_info:
			openAppDetails(mPackageName);
			break;
		default:
			break;
		}
	}

	protected void openPlayStore(String packageName) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
		finish();
	}

	private void uninstallApp(String packageName) {
		try {
			Uri packageUri = Uri.parse("package:" + packageName);
			startActivity(new Intent(Intent.ACTION_DELETE, packageUri));
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void openAppDetails(String packageName) {
		try {
			Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			i.addCategory(Intent.CATEGORY_DEFAULT);
			i.setData(Uri.parse("package:" + packageName));
			startActivity(i);
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

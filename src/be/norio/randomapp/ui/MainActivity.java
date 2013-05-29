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
import android.os.Bundle;
import android.text.TextUtils;
import be.norio.randomapp.R;
import be.norio.randomapp.util.PrefsUtils;

public class MainActivity extends Activity {

	private PackageManager mPackageManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPackageManager = getPackageManager();

		String prevApp = PrefsUtils.getPreviousApp();
		if (TextUtils.isEmpty(prevApp)) {
			String app = getRandomApp();
			launchApp(app);
			finish();
		}

	}

	private void launchApp(String packageName) {
		try {
			startActivity(mPackageManager.getLaunchIntentForPackage(packageName));
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
}

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

package be.norio.randomapp.util;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefsUtils {

	private static final String TAG = PrefsUtils.class.getSimpleName();

	private static final int VER_NONE = 0;
	private static final int VER_LAUNCH = 1;

	private static final int PREFS_VERSION = VER_LAUNCH;

	private static final String KEY_PREFS_VERSION = "prefs_version";

	private static boolean APPLY_AVAILABLE = false;

	// Keys
	public static final String KEY_PREVIOUS_APP = "language";
	// Default values

	private static Context CONTEXT;

	static {
		try {
			SharedPreferences.Editor.class.getMethod("apply", new Class[0]);
			APPLY_AVAILABLE = true;
		} catch (NoSuchMethodException e) {
			APPLY_AVAILABLE = false;
		}
	}

	private PrefsUtils() {
	}

	@TargetApi(9)
	public static void apply(SharedPreferences.Editor editor) {
		if (APPLY_AVAILABLE) {
			editor.apply();
		} else {
			editor.commit();
		}
	}

	private static SharedPreferences getPrefs() {
		return PreferenceManager.getDefaultSharedPreferences(CONTEXT);
	}

	public static void initialize(Application application) {
		CONTEXT = application;

		int version = getPrefs().getInt(KEY_PREFS_VERSION, VER_NONE);
		switch (version) {
		case VER_NONE:
			version = VER_LAUNCH;
		}

		PrefsUtils.apply(getPrefs().edit().putInt(KEY_PREFS_VERSION, version));
	}

	public static Context getPrefsContext() {
		return CONTEXT;
	}

	public static String getPreviousApp() {
		return getPrefs().getString(KEY_PREVIOUS_APP, null);
	}

	public static void setPreviousApp(String packageName) {
		PrefsUtils.apply(getPrefs().edit().putString(KEY_PREVIOUS_APP, packageName));
	}

}

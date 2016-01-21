package com.example.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import java.io.File;
import java.util.List;

public class AppUtils {

	/**
	 * @return
	 * @see {@linkplain #getMyCacheDir(String)}
	 */
	public static String getMyCacheDir() {
		return getMyCacheDir(null);
	}

	/**
	 * 获取或创建Cache目录
	 *
	 * @param bucket
	 *            临时文件目录，bucket = "/cache/" ，则放在"sdcard/linked-joyrun/cache"; 如果bucket=""或null,则放在"sdcard/linked-joyrun/"
	 */
	public static String getMyCacheDir(String bucket) {
		String dir;

		// 保证目录名称正确
		if (bucket != null) {
			if (!bucket.equals("")) {
				if (!bucket.endsWith("/")) {
					bucket = bucket + "/";
				}
			}
		} else
			bucket = "";

		String joyrun_default = "/html/";

		if (FileUtils.isSDCardExist()) {
			dir = Environment.getExternalStorageDirectory().toString() + joyrun_default + bucket;
		} else {
			dir = Environment.getDownloadCacheDirectory().toString() + joyrun_default + bucket;
		}

		File f = new File(dir);
		if (!f.exists()) {
			f.mkdirs();
		}
		return dir;
	}

	/**
	 * dp转换为px
	 */
	public static int dpToPx(Context context, float adius) {
		float density = context.getResources().getDisplayMetrics().density;
		return (int) (adius * density);
	}

	/**
	 * 判断Activity是否存在
	 *
	 * @param context
	 * @param activityClass
	 * @return
	 */
	public static boolean isActivityExist(Context context, Class<? extends Activity> activityClass) {
		try {
			context = context.getApplicationContext();
			Intent intent = new Intent(context, activityClass);
			ComponentName cmpName = intent.resolveActivity(context.getPackageManager());

			if (cmpName != null) { // 说明系统中存在这个activity
				ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
				List<RunningTaskInfo> taskInfoList = am.getRunningTasks(70);

				for (RunningTaskInfo taskInfo : taskInfoList) {
					if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
						return true;
					}
				}
			}
		} catch (Exception e) {}

		return false;
	}

	/**
	 * 判断Service是否running
	 *
	 * @param context
	 * @param serviceClass
	 * @return
	 */
	public static boolean isServiceRunning(Context context, Class<? extends Service> serviceClass) {
		try {
			context = context.getApplicationContext();

			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(2000);

			for (ActivityManager.RunningServiceInfo info : serviceList) {
				String name = info.service.getClassName();

				if (name != null && name.contains(serviceClass.getName())) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}

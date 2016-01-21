package com.example.html;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.utils.AppUtils;
import com.example.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WebViewActivity extends Activity implements OnClickListener {

	private WebView			webView;
	private static String	folder	= AppUtils.getMyCacheDir();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);

		findViewById(R.id.btn_assets).setOnClickListener(this);
		findViewById(R.id.btn_storage).setOnClickListener(this);

		initWebView();

		copyAssetsFiles();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_assets:

				loadHtmlFromAssets();

				break;
			case R.id.btn_storage:

				loadHtmlFromStorage();

				break;

			default:
				break;
		}

	}

	private void initWebView() {
		// 初始化水印布局
		webView = (WebView) findViewById(R.id.webView);

		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setSupportZoom(false);// 允许放大缩小
		settings.setBuiltInZoomControls(false);// 显示放大缩小按钮
		settings.setDisplayZoomControls(false);// api 11以上
		settings.setSupportMultipleWindows(true);
		settings.setGeolocationEnabled(true);// 启用地理定位

		webView.setHorizontalScrollBarEnabled(false);

		webView.setBackgroundColor(Color.TRANSPARENT); // 设置背景色
	}

	private void loadHtmlFromAssets() {

		String html = FileUtils.readAssest(this, "html/content.html");

		html = html.replace("@fontName0", "Impact");
		html = html.replace("@fontName1", "STANK");
		html = html.replace("@fontPath0", "../font/Impact.ttf");// assets相对路径
		html = html.replace("@fontPath1", "file://" + folder + "font/STANK.ttf");// sd卡里的ttf文件绝对路径

		html = html.replace("@image_assets", "image0.png");
		html = html.replace("@image_res", "file:///android_res/drawable/image0.png");
		html = html.replace("@image_file", "file://" + folder + "image0.png");

		String baseurl = "file:///android_asset/html/";

		webView.loadDataWithBaseURL(baseurl, html, "text/html", "UTF-8", null);
	}

	private void loadHtmlFromStorage() {
		String path = folder + "content.html";
		if (new File(path).exists()) {

			String html = FileUtils.readString(path);

			html = html.replace("@fontName0", "Impact");
			html = html.replace("@fontName1", "STANK");
			html = html.replace("@fontPath0", "file:///android_asset/font/Impact.ttf");// assets绝对路径
			html = html.replace("@fontPath1", "font/STANK.ttf");// sd卡相对路径 ("font/STANK.ttf"也可以)

			html = html.replace("@image_assets", "file:///android_asset/html/image0.png");
			html = html.replace("@image_res", "file:///android_res/drawable/image0.png");
			html = html.replace("@image_file", "image0.png");

			String baseurl = "file://" + path;

			webView.loadDataWithBaseURL(baseurl, html, "text/html", "UTF-8", null);
		}
	}

	/***
	 * 复制assets文件到sd卡
	 */
	private void copyAssetsFiles() {
		// 删除原文件，再创建文件夹
		FileUtils.delAllFile(folder);
		AppUtils.getMyCacheDir("font");

		try {
			String[] fileNames = getAssets().list("font");

			for (String fileName : fileNames) {
				copyFile("font/" + fileName, folder + "font/" + fileName);
			}

			fileNames = getAssets().list("html");
			for (String fileName : fileNames) {
				copyFile("html/" + fileName, folder + fileName);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void copyFile(String filename, String targetPath) {
		try {
			InputStream input = getAssets().open(filename);
			BufferedInputStream bin = new BufferedInputStream(input);

			FileOutputStream fout = new FileOutputStream(new File(targetPath));

			int hasRead;
			byte[] buffer = new byte[1024];

			while ((hasRead = bin.read(buffer)) > -1) {
				fout.write(buffer, 0, hasRead);
			}

			fout.flush();
			fout.close();

			input.close();
			bin.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

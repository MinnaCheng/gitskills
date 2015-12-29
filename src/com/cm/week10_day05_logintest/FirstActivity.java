/**APP首页
 * 新起一个线程，实现如下后台处理：
a、使用HttpURLConnection链接Url：http://223.4.20.248:8088/ForAjax/servlet/Hello
b、发送POST类型请求,请求数据：
请求：Json数据格式打包：
例如：
{
"name":"admin",
"password":"123456"
}
c、接收服务器返回数据：
服务器返回格式：JSON数据格式。
例如：
{
"map":[{"username":"admin","loginStatus":"success","age":"21","password":"33333"}]
}
d、将接收到的JSON数据解析、并保存到SharePreference中。

 * */
package com.cm.week10_day05_logintest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;

import org.apache.http.HttpConnection;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cm.week10_day05_logintest.util.Constants;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.LoginFilter.UsernameFilterGeneric;
import android.util.Log;
import android.widget.TextView;

public class FirstActivity extends Activity implements Runnable {
	public static final int MESSAGE_INT = 1;
	public static final String TAG = "FirstActivity";
	//服务器传回来的数据格式
/*	private String mResult = "{"
			+ "\"map\":[{\"username\":\"admin\",\"loginStatus\":\"success\",\"age\":\"21\",\"password\":\"33333\"}]"
			+ "}";*/
	private String mResult = new String();
	public SharedPreferences mPreferences;
	private TextView mTextViewUserInfo;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_INT:
				ArrayList<String> userInfoList = (ArrayList<String>) msg.obj;
				mTextViewUserInfo.setText(userInfoList.toString());
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_first);
		super.onCreate(savedInstanceState);
		mTextViewUserInfo = (TextView) findViewById(R.id.txVLoginMessage);
		Thread thread = new Thread(this);
		thread.start();

	}
	//打包
	private void  packageJson() throws JSONException {
		String userName = new String();
		String passWord = new String();
		Intent intent = getIntent();
		if (null != intent) {
			userName = intent.getStringExtra("userName");
			passWord = intent.getStringExtra("passWord");
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userName", userName);
		jsonObject.put("passWord", passWord);
		mResult = jsonObject.toString();
		
	}
//网址不可用，暂时不用这个方法获取
	private void connectedToInternet() throws MalformedURLException {
		Log.d(TAG, "connectedToInternet() ");
		URL url = new URL("http://223.4.20.248:8088/ForAjax/servlet/Hello");
		String userName = new String();
		String passWord = new String();
		try {
			// 发送POST类型数据请求，用Json数据格式打包
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(3000);
			connection.setDoInput(true);
			connection.setDoOutput(false);
			connection.setUseCaches(false);
			Intent intent = getIntent();
			if (null != intent) {
				userName = intent.getStringExtra("userName");
				passWord = intent.getStringExtra("passWord");
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userName", userName);
			jsonObject.put("passWord", passWord);

			DataOutputStream outputStream = new DataOutputStream(
					connection.getOutputStream());
			outputStream.write(jsonObject.toString().getBytes());
			outputStream.flush();
			outputStream.close();

			// 接收服务器返回数据：服务器返回格式：JSON数据格式。
			connection.setRequestProperty("Charset", "UTF-8");
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream is = connection.getInputStream();
				if (is != null) {
					InputStreamReader isr = new InputStreamReader(is, "utf-8");
					BufferedReader br = new BufferedReader(isr);
					StringBuffer buffer = new StringBuffer();
					String line = br.readLine();
					while (line != null) {
						buffer.append(line);
					}
					is.close();
					mResult = buffer.toString();

				}
			} else {
				Log.e(TAG, "连接失败");
			}
			connection.disconnect();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 解析JSON数据，添加至一个ArrayList数组,并返回。
	private ArrayList<String> praseJson() throws JSONException {
		Log.d(TAG, " praseJson()");
		JSONObject jsonObjectprase = new JSONObject(mResult);
		JSONArray mapList = jsonObjectprase.getJSONArray("map");
		ArrayList<String> map = new ArrayList<String>();
		for (int i = 0; i < mapList.length(); i++) {
			String name = mapList.getJSONObject(i).getString("username");
			map.add(0, name);
			String loginStatus = mapList.getJSONObject(i).getString(
					"loginStatus");
			map.add(1, loginStatus);
			String age = mapList.getJSONObject(i).getString("age");
			map.add(2, age);
			String password = mapList.getJSONObject(i).getString("password");
			map.add(3, password);
		}
		return map;
	}

	// 将解析出来的map用arraylist的get(int index)方法取出map中的对应值，保存到sharedPreference中
	private void savePreference() throws JSONException {
		Log.d(TAG, "savePreference()");
		mPreferences = getSharedPreferences("login_user", Context.MODE_PRIVATE);
		Editor editor = mPreferences.edit();
		ArrayList<String> arrayListUserInfo = new ArrayList<String>();
		arrayListUserInfo = praseJson();
		editor.putString(Constants.KEY_USER_NAME, arrayListUserInfo.get(0)
				.toString());
		editor.putString(Constants.KEY_LOGIN_STATUES, arrayListUserInfo.get(1)
				.toString());
		editor.putString(Constants.KEY_AGE, arrayListUserInfo.get(2).toString());
		editor.putString(Constants.KEY_PASSWORD, arrayListUserInfo.get(3)
				.toString());
		editor.commit();
	}

	@Override
	public void run() {
		try {
			// connectedToInternet();
			packageJson();
			ArrayList<String> userInfo = praseJson();
			Message message = Message.obtain();
			message.what = MESSAGE_INT;
			message.obj = userInfo;
			mHandler.handleMessage(message);
			try {
				savePreference();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}

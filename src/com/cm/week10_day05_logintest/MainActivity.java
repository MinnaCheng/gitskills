/*要求：
1,密码输入框（要求最低6位数字和字符的组合）,否则Toast 弹出提示
2,登录按钮 如果用户名输入、密码输入有一个输入内容为空时，登录按钮不可点。
3,点击登录按钮后，跳转到APP首页
4，当用户再次登录时从sharedPreference中取出用户名和密码*/

package com.cm.week10_day05_logintest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cm.week10_day05_logintest.util.Constants;

public class MainActivity extends Activity {
	private EditText mEdTUserName;
	private EditText mEdTPsW;
	private Button mButtonLogin;
	private TextView mTxVProblem;
	private TextView mTxVNewUser;
	private boolean mNameSign = false;
	private boolean mPsWSign = false;
	private static final int PASSWORD_MIN_LENGTH = 6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mEdTUserName = (EditText) findViewById(R.id.textViewName);
		mEdTPsW = (EditText) findViewById(R.id.textViewPsW);
		mButtonLogin = (Button) findViewById(R.id.buttonLogin);
		mButtonLogin.setClickable(false);

		mEdTUserName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (null != mEdTUserName.getText()) {
					mNameSign = true;
				}
				mButtonLogin.setClickable(mPsWSign && mNameSign);
			}
		});

		mEdTPsW.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}

			@Override
			public void afterTextChanged(Editable edt) {
				String password = edt.toString();
				if (null != mEdTPsW.getText()) {
					mPsWSign = true;
					if (password.length() < PASSWORD_MIN_LENGTH) {
						Toast toast = Toast.makeText(MainActivity.this,
								"密码不能少于6位！", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
				mButtonLogin.setClickable(mPsWSign && mNameSign);
			}
		});
		// 如果用户名输入、密码输入内容都不为空时，登录按钮可点。
		/*while (mPsWSign && mNameSign) {
			mButtonLogin.setClickable(true);
		} */ 
			
	

		mButtonLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,
						FirstActivity.class);
				intent.putExtra("userName", mEdTUserName.getText().toString());
				intent.putExtra("passWord", mEdTPsW.getText().toString());
				startActivity(intent);
			}
		});
	}

	// 取出sharedPreference中的值
	private void getUserInfo() {
		Log.d("mainActivty", "getUserInfo()");
		FirstActivity firstActivity = new FirstActivity();
		firstActivity.mPreferences = getSharedPreferences("login_user",
				Context.MODE_PRIVATE);
		String username = firstActivity.mPreferences.getString(
				Constants.KEY_USER_NAME, "");
		String password = firstActivity.mPreferences.getString(
				Constants.KEY_PASSWORD, "");
		if (!username.isEmpty()&&username!=null) {
			mEdTUserName.setText(username);
			mEdTPsW.setText(password);
		}
		
	}

	@Override
	protected void onStart() {
		// 当用户再次登录时从sharedPreference中取出用户名和密码。
		getUserInfo();
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

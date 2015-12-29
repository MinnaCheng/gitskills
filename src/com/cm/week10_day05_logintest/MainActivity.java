/*Ҫ��
1,���������Ҫ�����6λ���ֺ��ַ�����ϣ�,����Toast ������ʾ
2,��¼��ť ����û������롢����������һ����������Ϊ��ʱ����¼��ť���ɵ㡣
3,�����¼��ť����ת��APP��ҳ
4�����û��ٴε�¼ʱ��sharedPreference��ȡ���û���������*/

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
								"���벻������6λ��", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
				mButtonLogin.setClickable(mPsWSign && mNameSign);
			}
		});
		// ����û������롢�����������ݶ���Ϊ��ʱ����¼��ť�ɵ㡣
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

	// ȡ��sharedPreference�е�ֵ
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
		// ���û��ٴε�¼ʱ��sharedPreference��ȡ���û��������롣
		getUserInfo();
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

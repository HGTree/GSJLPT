package com.example.gsjl.activity;

import java.util.List;

import com.example.gsjl.model.User;
import com.example.gsjl.myview.EmailAutoCompleteTextView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends Activity {

	private Intent intent;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView quit = (TextView) findViewById(R.id.login_quit);
		quit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});

		TextView forget = (TextView) findViewById(R.id.login_forget);
		forget.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, CheckPhoneActivity.class);
				startActivity(intent);

			}
		});

		TextView register = (TextView) findViewById(R.id.login_register);
		register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();

				alert.setTitle("温馨提示：");
				alert.setMessage("请与部门信息管理员联系！");
				alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						// Toast.makeText(MainActivity.this, "q",
						// Toast.LENGTH_SHORT).show();
					}
				});
				alert.show();
			}
		});

		Button button_login = (Button) findViewById(R.id.login_login);
		button_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MainActivity.this, HomeActivity.class);
				EmailAutoCompleteTextView username = (EmailAutoCompleteTextView) findViewById(R.id.login_username);
				EditText password = (EditText) findViewById(R.id.login_password);
				String user = username.getText().toString();
				String pass = password.getText().toString();
				if ("".equals(user) || "".equals(pass)) {
					Toast.makeText(MainActivity.this, "邮箱或密码为空", Toast.LENGTH_SHORT).show();
				} else {
					// Bmob查询
					BmobQuery<User> bmobQuery = new BmobQuery<User>();
					bmobQuery.addWhereEqualTo("Uemail", user);
					bmobQuery.addWhereEqualTo("Upassword", pass);
					bmobQuery.findObjects(new FindListener<User>() {

						@Override
						public void done(List<User> arg0, BmobException arg1) {
							// TODO Auto-generated method stub
							if (arg1 == null) {
								if (arg0.size() == 1) {
									String userID = arg0.get(0).getObjectId();
									Bundle bundle = new Bundle();
									bundle.putCharSequence("userID", userID);
									intent.putExtras(bundle);
									Toast.makeText(MainActivity.this, "成功登陆", Toast.LENGTH_SHORT).show();
									Message message = handler.obtainMessage();
									message.what = 0;
									message.obj = userID;
									// 向handler发送消息
									handler.sendMessage(message);

								} else {
									Toast.makeText(MainActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
								}
							}
						}
					});
					handler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							if (msg.obj != null) {
								BmobIM.connect((String) msg.obj, new ConnectListener() {

									@Override
									public void done(String arg0, BmobException arg1) {
										if (arg1 == null) {
											Log.i("连接成功", arg0);
											startActivity(intent);
											finish();
										} else {
											Log.i("连接失败", arg1.getMessage() + arg1.getErrorCode());
										}

									}
								});
							}
						}
					};

				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

package com.example.gsjl.activity;

import java.util.List;

import com.example.gsjl.model.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年5月1日下午2:59:49
 *
 */
public class CheckPhoneActivity extends Activity {

	private Intent intent;
	private String userphone;
	private String phonenum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkphone);

		Button checkback = (Button) findViewById(R.id.checkphone_back);
		checkback.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});

		Button send = (Button) findViewById(R.id.sendcheckcode);
		send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 发送验证码
				EditText editText = (EditText) findViewById(R.id.phonecheck);
				phonenum = editText.getText().toString();
//				if ((phonenum.equals(HomeActivity.user.getUphone())&&HomeActivity.user.getUphone()!=null)||HomeActivity.user.getUphone()==null) {
					if (phonenum == null || phonenum.length() != 11) {
						Toast.makeText(CheckPhoneActivity.this, "手机号格式错误！", Toast.LENGTH_SHORT).show();
					} else {
						BmobQuery<User> bmobQuery = new BmobQuery<User>();
						bmobQuery.addWhereEqualTo("Uphone", phonenum);
						bmobQuery.findObjects(new FindListener<User>() {

							@Override
							public void done(List<User> arg0, BmobException arg1) {
								// 查询手机号是否存在
								if (arg1 == null) {
									if (arg0.size() != 0) {
										Log.i("手机号存在", "发送验证码");
										BmobSMS.requestSMSCode(phonenum, "GSJLPT", new QueryListener<Integer>() {
											@Override
											public void done(Integer arg0, BmobException arg1) {
												// TODO Auto-generated method
												// stub
												if (arg1 == null) {
													Toast.makeText(CheckPhoneActivity.this, "验证码已发送",
															Toast.LENGTH_SHORT).show();
													Log.i("发送短信", "发送成功");
												} else {
													Log.i("发送短信", "发送失败：");
												}

											}
										});
									} else {
										Log.i("手机号不存在", "请注册账号");
										Toast.makeText(CheckPhoneActivity.this, "手机号不存在，请先注册账号", Toast.LENGTH_SHORT)
												.show();
									}
								} else {
									Log.i("查询手机", "查询失败：" + arg1.getMessage());
								}

							}
						});

					}
//				} else {
//					Toast.makeText(CheckPhoneActivity.this, "这不是您的手机号码", Toast.LENGTH_SHORT).show();
//				}
			
			}
		});

		Button checkphone = (Button) findViewById(R.id.checkphone_state);
		checkphone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(CheckPhoneActivity.this, UpdatePasswordActivity.class);
				EditText phone = (EditText) findViewById(R.id.phonecheck);
				EditText code = (EditText) findViewById(R.id.checkcode);
				userphone = phone.getText().toString();
				String usercode = code.getText().toString();
				if ("".equals(userphone) || "".equals(usercode)) {
					Toast.makeText(CheckPhoneActivity.this, "手机号或验证码为空", Toast.LENGTH_SHORT).show();
				} else {
					BmobSMS.verifySmsCode(userphone, usercode, new UpdateListener() {

						@Override
						public void done(BmobException arg0) {
							// TODO Auto-generated method stub
							if (arg0 == null) {// 短信验证码已验证成功
								Log.i("验证短信", "验证通过");
								Bundle bundle = new Bundle();
								bundle.putCharSequence("userphone", userphone);
								Log.i("验证的手机号", userphone);
								intent.putExtras(bundle);
								startActivity(intent);
								finish();
							} else {
								Log.i("验证短信",
										"验证失败：code =" + arg0.getErrorCode() + ",msg = " + arg0.getLocalizedMessage());
								Toast.makeText(CheckPhoneActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
							}
						}
					});
				}

			}
		});

	}

}

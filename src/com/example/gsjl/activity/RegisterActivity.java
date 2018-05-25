package com.example.gsjl.activity;

import java.util.List;

import com.example.gsjl.model.Department;
import com.example.gsjl.model.User;
import com.example.gsjl.myview.EmailAutoCompleteTextView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年4月30日下午2:49:10
 *
 */
public class RegisterActivity extends Activity {
	private Handler handler;
	private String usersex;
	private String usernames;
	private String emails;
	private String passwords;
	private String passwordres;
	private String phones;
	private String roles;
	User user = new User();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		Button back = (Button) findViewById(R.id.register_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		Button register_yes = (Button) findViewById(R.id.register_yes);
		Button register = (Button) findViewById(R.id.register);
		register_yes.setOnClickListener(tab);
		register.setOnClickListener(tab);

	}

	View.OnClickListener tab = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// 将部门信息存入数据库
			EditText username = (EditText) findViewById(R.id.username);
			EmailAutoCompleteTextView email = (EmailAutoCompleteTextView) findViewById(R.id.email);
			EditText password = (EditText) findViewById(R.id.password);
			EditText passwordre = (EditText) findViewById(R.id.passwordre);
			EditText role = (EditText) findViewById(R.id.role);
			EditText phone = (EditText) findViewById(R.id.phone);
			RadioGroup radio_sex = (RadioGroup) findViewById(R.id.radio_sex);
			RadioButton r;
			for (int i = 0; i < radio_sex.getChildCount(); i++) {
				r = (RadioButton) radio_sex.getChildAt(i);
				if (r.isChecked()) {
					usersex = r.getText().toString();
					break;
				}
			}
			Spinner spinner = (Spinner) findViewById(R.id.department);
			String department = spinner.getSelectedItem().toString();
			usernames = username.getText().toString();
			emails = email.getText().toString();
			passwords = password.getText().toString();
			String passwordres = passwordre.getText().toString();
			phones = phone.getText().toString();
			roles = role.getText().toString();
			if ("".equals(department) || "".equals(usernames) || "".equals(emails) || "".equals(passwords)
					|| "".equals(passwordres) || "".equals(phones) || "".equals(roles)) {
				Toast.makeText(RegisterActivity.this, "部分信息未填！", Toast.LENGTH_SHORT).show();
			} else if (passwords.equals(passwordres)) {
				if (phones.length() == 11) {
					// 查询部门id
					BmobQuery<Department> deBmobQuery = new BmobQuery<Department>();
					
					deBmobQuery.addWhereEqualTo("Dname", department);
					deBmobQuery.findObjects(new FindListener<Department>() {

						@Override
						public void done(List<Department> arg0, BmobException arg1) {
							if (arg1 == null) {
								if (arg0 != null) {
									Log.i("查询部门成功", arg0.get(0).getDname());
									Message message = handler.obtainMessage();
									message.what = 0;
									message.obj = arg0.get(0);
									// 向handler发送消息
									handler.sendMessage(message);
								}
							} else {
								Log.i("查询部门信息", "查询失败" + arg1.getMessage() + "" + arg1.getErrorCode());
							}

						}
					});
					// 保存用户信息
					// 接受handler消息

					handler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							//发送邮件
							BmobUser.requestEmailVerify(emails, new UpdateListener() {
								@Override
								public void done(BmobException e) {
									if (e == null) {
										Log.i("请求验证邮件成功", "请到" + emails + "邮箱中进行激活。");
									} else {
										Log.i("验证邮件失败:", "" + e.getMessage());
									}
								}
							});
							user.setDid((Department) msg.obj);
							user.setUname(usernames);
							user.setUpassword(passwords);
							user.setUphone(phones);
							user.setUemail(emails);
							user.setUrole(roles);
							user.setUsex(usersex);
							user.setUimage(0x7f020088);
							user.save(new SaveListener<String>() {

								@Override
								public void done(String arg0, BmobException arg1) {
									// 存储用户信息
									if (arg1 == null) {
										Log.i("注册成功", "注册人：" + HomeActivity.user.getObjectId());
										Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
										finish();
									} else {
										Log.i("用户注册", "注册失败：" + arg1.getMessage() + arg1.getErrorCode());
										Toast.makeText(RegisterActivity.this, "邮箱或手机号已存在！", Toast.LENGTH_SHORT).show();
									}
								}
							});
						}
					};

				} else {
					Toast.makeText(RegisterActivity.this, "手机号格式错误！", Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(RegisterActivity.this, "密码设置不一致！", Toast.LENGTH_SHORT).show();
			}
		}
	};

}

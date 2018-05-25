package com.example.gsjl.activity;

import java.util.List;

import com.example.gsjl.model.Department;
import com.example.gsjl.model.MessageFroum;
import com.example.gsjl.model.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年4月30日下午11:57:50
 *
 */
public class UpdateUserinfoActivity extends Activity {
	private String usersex;
	private EditText upusername;
	private EditText upemail;
	private EditText uprole;
	private EditText upphone;
	private RadioGroup sex;
	private Handler handler;
	private User user = new User();
	private String department;
	private String username;
	private String email;
	private String role;
	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updateuserinfo);
		upusername = (EditText) findViewById(R.id.upusername);
		upemail = (EditText) findViewById(R.id.upemail);
		uprole = (EditText) findViewById(R.id.uprole);
		upphone = (EditText) findViewById(R.id.upphone);

		BmobQuery<User> user = new BmobQuery<User>();
		user.getObject(HomeActivity.user.getObjectId(), new QueryListener<User>() {

			@Override
			public void done(User arg0, BmobException arg1) {
				if (arg1 == null) {
					if (arg0 != null) {
						Log.i("查询成功", arg0.getUname());
						upusername.setText(arg0.getUname());
						upemail.setText(arg0.getUemail());
						uprole.setText(arg0.getUrole());
						upphone.setText(arg0.getUphone());
					}
				} else {
					Log.i("查询失败", arg1.getMessage() + arg1.getErrorCode());
				}

			}
		});
		Button updateback = (Button) findViewById(R.id.updateinfo_back);
		updateback.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog alert = new AlertDialog.Builder(UpdateUserinfoActivity.this).create();
				alert.setTitle("温馨提示");
				alert.setMessage("返回后你编辑的内容将会丢失！");
				alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(UpdateUserinfoActivity.this, "取消返回", Toast.LENGTH_SHORT).show();

					}
				});
				alert.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();

					}
				});
				alert.show();

			}
		});

		Button update = (Button) findViewById(R.id.updateinfo_yes);
		Button updatere = (Button) findViewById(R.id.updateuserinfo);
		update.setOnClickListener(tab);
		updatere.setOnClickListener(tab);
	}

	View.OnClickListener tab = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// 将部门信息存入数据库
			sex = (RadioGroup) findViewById(R.id.radio_upsex);
			RadioButton r;
			for (int i = 0; i < sex.getChildCount(); i++) {
				r = (RadioButton) sex.getChildAt(i);
				if (r.isChecked()) {
					usersex = r.getText().toString();
					break;
				}
			}
			Spinner spinner = (Spinner) findViewById(R.id.updepartment);
			department = spinner.getSelectedItem().toString();
			username = upusername.getText().toString();
			email = upemail.getText().toString();
			role = uprole.getText().toString();
			phone = upphone.getText().toString();
			// 查询部门信息
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
			if ("".equals(username) || "".equals(email) || "".equals(role) || "".equals(phone)
					|| "".equals(department)) {
				Toast.makeText(UpdateUserinfoActivity.this, "部分信息未填！", Toast.LENGTH_SHORT).show();
			} else {
				handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case 0:
							user.setDid((Department) msg.obj);
							user.setUname(username);
							user.setUemail(email);
							user.setUrole(role);
							user.setUphone(phone);
							user.setUsex(usersex);
							user.update(HomeActivity.user.getObjectId(), new UpdateListener() {

								@Override
								public void done(BmobException arg0) {
									// 更新数据
									if (arg0 == null) {
										Log.i("更新用户表成功", "");
										Toast.makeText(UpdateUserinfoActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
										finish();
									} else {
										Log.i("更新失败", arg0.getMessage() + arg0.getErrorCode());
									}

								}
							});
							break;
						}

					}
				};
			}
		}
	};
}

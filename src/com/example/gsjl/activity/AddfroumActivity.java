package com.example.gsjl.activity;

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
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年5月2日下午1:56:41
 *
 */
public class AddfroumActivity extends Activity {
	private Handler handler;
	private String froumdetail;
	private MessageFroum mf = new MessageFroum();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addfroum);

		Button addback = (Button) findViewById(R.id.adddfroum_back);
		addback.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog alert = new AlertDialog.Builder(AddfroumActivity.this).create();
				alert.setMessage("确定放弃发帖吗");
				alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

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

		Button addfroum = (Button) findViewById(R.id.addfroumyes);
		Button froum_add = (Button) findViewById(R.id.froum_add);
		addfroum.setOnClickListener(tab);
		froum_add.setOnClickListener(tab);
	}

	View.OnClickListener tab = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			EditText detail = (EditText) findViewById(R.id.froum_detail);
			froumdetail = detail.getText().toString();
			if ("".equals(froumdetail)) {
				Toast.makeText(AddfroumActivity.this, "话题内容不能为空", Toast.LENGTH_SHORT).show();
			} else {
				// 获取用户信息
				BmobQuery<User> user = new BmobQuery<User>();
				user.getObject(HomeActivity.user.getObjectId(), new QueryListener<User>() {

					@Override
					public void done(User arg0, BmobException arg1) {
						if (arg1 == null) {
							if (arg0 != null) {
								Log.i("查询用户信息成功", arg0.getUname());
								Message message = handler.obtainMessage();
								message.what = 0;
								message.obj = arg0;
								// 向handler发送消息
								handler.sendMessage(message);
							}
						} else {
							Log.i("查询用户信息失败", arg1.getMessage() + "," + arg1.getErrorCode());
						}

					}
				});
				// 存储话题信息
				handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case 0:
							mf.setIsHeading("是");
							mf.setMFcontent(froumdetail);
							mf.setUid((User) msg.obj);
							mf.save(new SaveListener<String>() {

								@Override
								public void done(String arg0, BmobException arg1) {
									// 存储用户信息
									if (arg1 == null) {
										Log.i("保存话题成功", "发布人：" + HomeActivity.user.getObjectId());
										Toast.makeText(AddfroumActivity.this, "发布成功！", Toast.LENGTH_SHORT).show();
										finish();
									} else {
										Log.i("发布话题失败", arg1.getMessage() + arg1.getErrorCode());
										Toast.makeText(AddfroumActivity.this, "发布失败！", Toast.LENGTH_SHORT).show();
									}
								}
							});
							setResult(0x411);
							finish();
							break;
						}

					}
				};

			}
		}
	};

}

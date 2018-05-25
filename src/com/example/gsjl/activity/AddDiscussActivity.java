package com.example.gsjl.activity;

import com.example.gsjl.model.Crowd;
import com.example.gsjl.model.MessageFroum;
import com.example.gsjl.model.User_Crowd;
import com.example.gsjl.util.EventImageUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.bmob.newim.listener.QueryListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.greenrobot.event.EventBus;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年5月2日下午6:59:21
 *
 */
public class AddDiscussActivity extends Activity {
	private ImageView imageView;
	private int image = 0;
	private Handler handlerid;
	private Handler HandlerCU;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discussadd);
		EventBus.getDefault().register(this);

		imageView = (ImageView) findViewById(R.id.image_discuss);
		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddDiscussActivity.this, ChooseImageActivity.class);
				startActivity(intent);

			}
		});

		Button adddiscuss_back = (Button) findViewById(R.id.adddiscuss_back);
		adddiscuss_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog alert = new AlertDialog.Builder(AddDiscussActivity.this).create();
				alert.setMessage("放弃创建群聊吗？");
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

		Button department_add = (Button) findViewById(R.id.discuss_add);
		department_add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText name = (EditText) findViewById(R.id.discuss_name);
				EditText detail = (EditText) findViewById(R.id.discuss_detail);
				String discuss_name = name.getText().toString();
				String discuss_detail = detail.getText().toString();
				if ("".equals(discuss_name) || "".equals(discuss_detail)) {
					Toast.makeText(AddDiscussActivity.this, "群名称和描述不能为空", Toast.LENGTH_SHORT).show();
				} else {

					Crowd crowd = new Crowd();
					if (image == 0) {
						crowd.setCimage(0x7f020088);
					} else {
						crowd.setCimage(ChooseImageActivity.imageid[image]);
					}

					crowd.setCname(discuss_name);
					crowd.setCexplain(discuss_detail);
					crowd.setUid(HomeActivity.user);
					crowd.save(new SaveListener<String>() {

						@Override
						public void done(String arg0, BmobException arg1) {
							if (arg1 == null) {
								Log.i("创建群聊成功", "群聊id：" + arg0);
//								Toast.makeText(AddDiscussActivity.this, "创建群聊成功", Toast.LENGTH_SHORT).show();
								setResult(0x311);
								finish();
								Message message = handlerid.obtainMessage();
								message.what = 0;
								message.obj = arg0;
								handlerid.sendMessage(message);
							} else {
								Log.i("创建群聊失败", "-------" + arg1.getMessage() + arg1.getErrorCode());
							}

						}
					});
				}
			}
		});
		handlerid = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				BmobQuery<Crowd> crowd = new BmobQuery<Crowd>();
				crowd.getObject((String) msg.obj, new cn.bmob.v3.listener.QueryListener<Crowd>() {

					@Override
					public void done(Crowd arg0, BmobException arg1) {
						if (arg1 == null) {
							Message message = handlerid.obtainMessage();
							message.what = 0;
							message.obj = arg0;
							HandlerCU.sendMessage(message);
						} else {
							Log.i("查询群聊失败", "" + arg1.getMessage() + arg1.getErrorCode());
						}

					}
				});
			}

		};
		HandlerCU = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				User_Crowd uc = new User_Crowd();
				uc.setCid((Crowd) msg.obj);
				uc.setUid(HomeActivity.user);
				uc.save(new SaveListener<String>() {

					@Override
					public void done(String arg0, BmobException arg1) {
						if (arg1 == null) {
							Log.i("增加本人", "增加成功");
						} else {
							Log.i("增加本人", "添加创建人进群失败" + arg1.getMessage() + arg1.getErrorCode());
						}

					}
				});
			}

		};
	}

	public void onEvent(EventImageUtil event) {
		imageView = (ImageView) findViewById(R.id.image_discuss);
		imageView.setBackgroundResource(ChooseImageActivity.imageid[event.getMsg()]);
		image = event.getMsg();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}

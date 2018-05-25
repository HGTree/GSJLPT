package com.example.gsjl.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.gsjl.model.Crowd;
import com.example.gsjl.model.User_Crowd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年5月2日下午9:13:15
 *
 */
public class DissucssDetailActivity extends Activity {
	public static Crowd crowd;
	private int position;
	private Handler handlerquit;
	private Handler handlercancel;
	private Handler handlercanceland;
	private Handler handlercancelandagain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.disscussdetail);

		Bundle bundle = getIntent().getExtras();
		position = bundle.getInt("position");

		Button discussdetail_back = (Button) findViewById(R.id.discussdetail_back);
		discussdetail_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		Button discussdetail_adduser = (Button) findViewById(R.id.discussdetail_adduser);
		discussdetail_adduser.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(DissucssDetailActivity.this,AddUserActivity.class);
				startActivity(intent);

			}
		});
		Button discussdetail_quit = (Button) findViewById(R.id.discussdetail_quit);
		discussdetail_quit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog alert = new AlertDialog.Builder(DissucssDetailActivity.this).create();
				alert.setMessage("确定退出群聊吗");
				alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				alert.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						BmobQuery<User_Crowd> uc = new BmobQuery<User_Crowd>();
						uc.addWhereEqualTo("Uid", HomeActivity.user);
						uc.include("Cid,Uid");
						uc.order("-createdAt");
						uc.findObjects(new FindListener<User_Crowd>() {

							@Override
							public void done(List<User_Crowd> arg0, BmobException arg1) {
								if (arg1 == null) {
									if (arg0 != null) {
										Log.i("查询群信息", "查询成功！共有：" + arg0.size() + "群。");
										if((arg0.get(position).getCid().getUid().getObjectId()).equals(HomeActivity.user.getObjectId())){
											Toast.makeText(DissucssDetailActivity.this, "你是群主，请解散该群", Toast.LENGTH_SHORT).show();
										}else{
											
											Message message = handlerquit.obtainMessage();
											message.what = 0;
											message.obj = arg0.get(position).getObjectId();
											handlerquit.sendMessage(message);
										}
										
									} else {
										Log.i("查询群信息", "群为空");
									}
								} else {
									Log.i("查询群信息", "查询失败！" + arg1.getMessage() + arg1.getErrorCode());
								}

							}
						});

					}
				});
				alert.show();
			}
		});
		Button discussdetail_cancel = (Button) findViewById(R.id.discussdetail_cancel);
		discussdetail_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog alert = new AlertDialog.Builder(DissucssDetailActivity.this).create();
				alert.setMessage("确定解散群聊吗");
				alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				alert.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						BmobQuery<User_Crowd> uc = new BmobQuery<User_Crowd>();
						uc.addWhereEqualTo("Uid", HomeActivity.user);
						uc.include("Cid,Uid");
						uc.order("-createdAt");
						uc.findObjects(new FindListener<User_Crowd>() {

							@Override
							public void done(List<User_Crowd> arg0, BmobException arg1) {
								if (arg1 == null) {
									if (arg0 != null) {
										Log.i("查询群信息", "查询成功！共有：" + arg0.size() + "群。");
										if((arg0.get(position).getCid().getUid().getObjectId()).equals(HomeActivity.user.getObjectId())){
											
											Message message = handlercancel.obtainMessage();
											message.what = 0;
											message.obj = arg0.get(position).getCid();
											handlercancel.sendMessage(message);
											Message messages = handlercanceland.obtainMessage();
											messages.what = 1;
											messages.obj = arg0.get(position).getCid();
											handlercanceland.sendMessage(messages);
											
										}else{
											Toast.makeText(DissucssDetailActivity.this, "你不是群主!", Toast.LENGTH_SHORT).show();
										}
										
									} else {
										Log.i("查询群信息", "群为空");
									}
								} else {
									Log.i("查询群信息", "查询失败！" + arg1.getMessage() + arg1.getErrorCode());
								}

							}
						});

					}
				});
				alert.show();

			}
		});
		// 退出群聊。删除该用户记录
		handlerquit = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				User_Crowd uc = new User_Crowd();
				uc.setObjectId(((String) msg.obj));
				uc.delete(new UpdateListener() {

					@Override
					public void done(BmobException arg0) {
						if (arg0 == null) {
							Log.i("删除群成功", "删除用户群聊表中用户成功。");
							setResult(0x3311);
							finish();
						} else {
							Toast.makeText(DissucssDetailActivity.this, "你已退出，请切换界面刷新！", Toast.LENGTH_SHORT).show();
							Log.i("删除群失败", "" + arg0.getErrorCode() + arg0.getErrorCode());
						}

					}
				});
			}

		};

		// 删除群聊
		handlercancel = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				Crowd crowd = new Crowd();
				crowd.setObjectId(((Crowd) msg.obj).getObjectId());
				crowd.delete(new UpdateListener() {

					@Override
					public void done(BmobException arg0) {
						if (arg0 == null) {
							Log.i("删除成功", "删除群聊成功。");
						} else {
							Log.i("删除失败", "删除群聊失败");
							Toast.makeText(DissucssDetailActivity.this, "你已解散群，请切换界面刷新！", Toast.LENGTH_SHORT).show();
						}

					}
				});
			}

		};

		// 查询该群聊的用户信息
		handlercanceland = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				BmobQuery<User_Crowd> bmobQuery = new BmobQuery<User_Crowd>();
				bmobQuery.addWhereEqualTo("Cid", (Crowd) msg.obj);
				bmobQuery.findObjects(new FindListener<User_Crowd>() {

					@Override
					public void done(List<User_Crowd> arg0, BmobException arg1) {
						if (arg1 == null) {
							Log.i("查询群对应的所有用户", "群聊里面有" + arg0.size() + "个用户");
							Message message = handlercancelandagain.obtainMessage();
							message.what = 0;
							message.obj = arg0;
							handlercancelandagain.sendMessage(message);

						} else {
							Log.i("查询群对应的所有用户失败", "" + arg1.getMessage() + arg1.getErrorCode());
						}

					}
				});
			}

		};
		// 删除群聊中的用户信息
		handlercancelandagain = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				new BmobBatch().deleteBatch((List<BmobObject>) msg.obj).doBatch(new QueryListListener<BatchResult>() {

					@Override
					public void done(List<BatchResult> o, BmobException e) {
						if (e == null) {
							for (int i = 0; i < o.size(); i++) {
								BatchResult result = o.get(i);
								BmobException ex = result.getError();
								if (ex == null) {
									Log.i("删除群用户", "第" + i + "个数据批量删除成功");
									setResult(0x3312);
									finish();
								} else {
									Log.i("删除群用户失败", "第" + i + "个数据批量删除失败");
								}
							}
						} else {
							Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
						}
					}
				});
			}

		};

		// 加载群聊信息
		BmobQuery<User_Crowd> uc = new BmobQuery<User_Crowd>();
		uc.addWhereEqualTo("Uid", HomeActivity.user);
		uc.include("Cid");
		uc.order("-createdAt");
		uc.findObjects(new FindListener<User_Crowd>() {

			@Override
			public void done(List<User_Crowd> arg0, BmobException arg1) {
				if (arg1 == null) {
					if (arg0 != null) {
						Log.i("查询群信息", "查询成功！共有：" + arg0.size() + "群。");
						crowd=arg0.get(position).getCid();
						Intent intent = getIntent();
						Bundle bundle = intent.getExtras();
						ImageView imageView = (ImageView) findViewById(R.id.image_discussdetail);
						TextView textView = (TextView) findViewById(R.id.discussdetail_name);
						TextView textView2 = (TextView) findViewById(R.id.discussdetail_detail);
						imageView.setBackgroundResource(
								(arg0.get(bundle.getInt("position")).getCid().getCimage()).intValue());
						textView.setText(arg0.get(bundle.getInt("position")).getCid().getCname());
						textView2.setText(arg0.get(bundle.getInt("position")).getCid().getCexplain());
					} else {
						Log.i("查询群信息", "群为空");
					}
				} else {
					Log.i("查询群信息", "查询失败！" + arg1.getMessage() + arg1.getErrorCode());
				}

			}
		});
	}
}

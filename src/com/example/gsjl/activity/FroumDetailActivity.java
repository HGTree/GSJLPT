package com.example.gsjl.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.gsjl.model.MessageFroum;
import com.example.gsjl.model.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年5月2日下午2:21:48
 *
 */
public class FroumDetailActivity extends Activity {
	private List<Map<String, Object>> list;
	private Handler handler;
	private Handler handlertime;
	private EditText editText;
	private Handler handlermsg;
	private Message messagemsg;

	// 刷新
	private void refresh() {
		onCreate(null);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.froumdetail);
		load();
		// 返回话题界面
		Button froumdetail_back = (Button) findViewById(R.id.froumdetail_back);
		froumdetail_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回上一层界面
				setResult(0x412);
				finish();

			}
		});
		//长按删除事件
		ListView listView=(ListView) findViewById(R.id.froumdetail);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				Bundle bundle = getIntent().getExtras();
				BmobQuery<MessageFroum> messageFroum = new BmobQuery<MessageFroum>();
				messageFroum.addWhereEqualTo("MFid", bundle.getString("MFid"));
				messageFroum.order("-updatedAt");
				messageFroum.findObjects(new FindListener<MessageFroum>() {

					@Override
					public void done(List<MessageFroum> arg0, BmobException arg1) {
						if (arg1 == null) {
							if (arg0 != null) {
								Log.i("查询回复成功", "共" + arg0.size() + "条回复");
								if(HomeActivity.user.getUrole().equals("管理员")||arg0.get(position).getUid().getObjectId().equals(HomeActivity.user.getObjectId())){
									messagemsg = handlermsg.obtainMessage();
									messagemsg.what = 0;
									messagemsg.obj = arg0.get(position).getObjectId();
									Log.i("-----------------", ""+arg0.get(position).getObjectId());
									AlertDialog alert = new AlertDialog.Builder(FroumDetailActivity.this).create();
									alert.setMessage("确定删除回复吗");
									alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {

										}
									});
									alert.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											
											// 向handler发送消息
											handlermsg.sendMessage(messagemsg);
										}
									});
									alert.show();
								}
								

							} else {
								Log.i("查询回复成功", "无回复");
							}
						} else {
							Log.i("查询消息失败", "原因" + arg1.getErrorCode() + "," + arg1.getMessage());
						}

					}
				});
				
				return true;
			}
		});
		
		//删除回复
		handlermsg = new Handler() {
			@Override
			public void handleMessage(Message msg) {
					MessageFroum message=new MessageFroum();
					message.setObjectId((String) msg.obj);
					message.delete(new UpdateListener() {
						
						@Override
						public void done(BmobException arg0) {
							if(arg0==null){
								Log.i("删除回复", "删除成功");
							}else{
								Log.i("删除回复", "删除失败"+arg0.getMessage()+arg0.getErrorCode());
							}
							
						}
					});
					load();
				}
			};
		TextView textView = (TextView) findViewById(R.id.froum_message_input);
		textView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取用户信息
				BmobQuery<User> user = new BmobQuery<User>();
				user.getObject(HomeActivity.user.getObjectId(), new QueryListener<User>() {

					@Override
					public void done(User arg0, BmobException arg1) {
						if (arg1 == null) {
							if (arg0 != null) {
								Log.i("查询用户信息", "成功" + arg0.getUname());
								Message message = handler.obtainMessage();
								message.what = 0;
								message.obj = arg0;
								// 向handler发送消息
								handler.sendMessage(message);

							} else {
								Log.i("查询用户信息", "为空");
							}
						} else {
							Log.i("查询用户信息", "查询失败" + arg1.getMessage() + "," + arg1.getErrorCode());
						}

					}
				});
				// 发送回复
				handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						editText = (EditText) findViewById(R.id.froum_message);
						String messagedetail = editText.getText().toString();
						Intent intent = getIntent();
						Bundle bundle = intent.getExtras();
						MessageFroum messageF = new MessageFroum();
						messageF.setUid((User) msg.obj);
						messageF.setIsHeading("否");
						messageF.setMFcontent(messagedetail);
						messageF.setMFid(bundle.getString("MFid"));
						Log.i("存储的信息为", msg.obj + "" + messagedetail + bundle.getString("MFid"));
						messageF.save(new SaveListener<String>() {

							@Override
							public void done(String arg0, BmobException arg1) {
								if (arg1 == null) {
									Log.i("存储成功", arg0);
									editText.setText("");
									Toast.makeText(FroumDetailActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
									refresh();
									BmobQuery<MessageFroum> message = new BmobQuery<MessageFroum>();
									message.getObject(arg0, new QueryListener<MessageFroum>() {

										@Override
										public void done(MessageFroum arg0, BmobException arg1) {
											if (arg1 == null) {
												if (arg0 != null) {
													Log.i("查询保存对象", "查询成功");
													Message message = handlertime.obtainMessage();
													message.what = 1;
													message.obj = arg0.getUpdatedAt();
													// 向handler发送消息
													handlertime.sendMessage(message);
												} else {
													Log.i("查询保存对象", "查询失败,为空！");
												}
											} else {
												Log.i("查询保存对象", "查询失败" + arg1.getMessage() + arg1.getErrorCode());
											}

										}
									});
								} else {
									Log.i("存储回复失败", "" + arg1.getErrorCode() + "," + arg1.getMessage());
								}

							}
						});
					}
				};
				handlertime = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						Intent intent = getIntent();
						Bundle bundle = intent.getExtras();
						MessageFroum time = new MessageFroum();
						time.setIsHeading("是");
						time.update(bundle.getString("MFid"), new UpdateListener() {

							@Override
							public void done(BmobException arg0) {
								if (arg0 == null) {
									Log.i("更新标题时间", "更新成功");
								} else {
									Log.i("查询标题失败", "------" + arg0.getMessage() + arg0.getErrorCode());
								}

							}
						});

					}

				};

			}
		});
	}

	public void load() {
		// 更新话题详情，获取回复
		list = new ArrayList<Map<String, Object>>();
		Bundle bundle = getIntent().getExtras();
		// 通过传递过来的数据查询数据库
		TextView froumtitle = (TextView) findViewById(R.id.froum_title);
		froumtitle.setText(bundle.getString("detail"));
		BmobQuery<MessageFroum> messageFroum = new BmobQuery<MessageFroum>();
		messageFroum.addWhereEqualTo("MFid", bundle.getString("MFid"));
		messageFroum.include("Uid");
		messageFroum.order("-updatedAt");
		messageFroum.findObjects(new FindListener<MessageFroum>() {

			@Override
			public void done(List<MessageFroum> arg0, BmobException arg1) {
				if (arg1 == null) {
					if (arg0 != null) {
						Log.i("查询回复成功", "共" + arg0.size() + "条回复");
						for (MessageFroum message : arg0) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("userimages", message.getUid().getUimage());
							map.put("name", message.getUid().getUname());
							map.put("detail", message.getMFcontent());
							map.put("time", message.getCreatedAt());
							list.add(map);
						}
						ListView froumdetail = (ListView) findViewById(R.id.froumdetail);
						SimpleAdapter froumadapter = new SimpleAdapter(FroumDetailActivity.this, list,
								R.layout.froumdetail_item, new String[] { "userimages", "name", "detail", "time" },
								new int[] { R.id.userimage_froumdetail, R.id.username_froumdetail,
										R.id.froumdetail_detail, R.id.time_froumdetail });
						froumadapter.setViewBinder(new ViewBinder() {
							public boolean setViewValue(View view, Object data, String textRepresentation) {
								if (view instanceof ImageView || data instanceof Drawable) {
									ImageView iv = (ImageView) view;
									iv.setBackgroundResource(((Number) data).intValue());
									return true;
								} else {
									return false;
								}
							}
						});
						froumdetail.setAdapter(froumadapter);

					} else {
						Log.i("查询回复成功", "无回复");
					}
				} else {
					Log.i("查询消息失败", "原因" + arg1.getErrorCode() + "," + arg1.getMessage());
				}

			}
		});
	}
}

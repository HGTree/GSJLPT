package com.example.gsjl.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.gsjl.model.Crowd;
import com.example.gsjl.model.User;
import com.example.gsjl.model.User_Crowd;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
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

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年5月14日下午8:56:25
 *
 */
public class SerachActivity extends Activity {
	private Handler handler;
	private EditText editText;
	private List<Map<String, Object>> list;
	private int TAG;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serach);
		ImageView imageView = (ImageView) findViewById(R.id.serach_back);
		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		TextView textView = (TextView) findViewById(R.id.search_user);
		textView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				editText = (EditText) findViewById(R.id.serachdetail);
				if ((editText.getText()).equals("")) {
					Toast.makeText(SerachActivity.this, "请输入查询姓名", Toast.LENGTH_SHORT).show();
				} else {
					BmobQuery<User> bmobQuery = new BmobQuery<User>();
					bmobQuery.addWhereEqualTo("Uname", editText.getText());
					bmobQuery.findObjects(new FindListener<User>() {

						@Override
						public void done(List<User> arg0, BmobException arg1) {
							if (arg1 == null) {
								if (arg0.size() != 0) {
									Log.i("查找用户成功", "------有" + arg0.size() + "个结果");
									TAG=1;
									Message message = handler.obtainMessage();
									message.what = 0;
									message.obj = arg0;
									handler.sendMessage(message);
								} else {
									Log.i("查找用户成功", "未找到查询用户");
									TAG=0;
									Message message = handler.obtainMessage();
									message.what = 0;
									message.obj = arg0;
									handler.sendMessage(message);
								}

							} else {
								Log.i("查找用户失败", "" + arg1.getMessage() + arg1.getErrorCode());
								Toast.makeText(SerachActivity.this, "服务器故障！", Toast.LENGTH_SHORT).show();
							}

						}
					});
					handler = new Handler() {

						
						@Override
						public void handleMessage(final Message msg) {
							final List<User> userserach=(List<User>) msg.obj;
							list = new ArrayList<Map<String, Object>>();
							BmobQuery<Crowd> bmobQuery = new BmobQuery<Crowd>();
							bmobQuery.addWhereEqualTo("Cname", editText.getText());
							bmobQuery.findObjects(new FindListener<Crowd>() {

								@Override
								public void done(List<Crowd> arg0, BmobException arg1) {
									if (arg1 == null) {
										if (arg0.size() != 0) {
											Log.i("查找群聊成功", "------有" + arg0.size() + "个结果");
											for (Crowd crowd : arg0) {
												Map<String, Object> map = new HashMap<String, Object>();
												map.put("Cimages", crowd.getCimage());
												map.put("Cname", crowd.getCname());
												map.put("Cexplain", crowd.getCexplain());
												map.put("userorcrowd", "群聊");
												list.add(map);
											}
											if(TAG==1){
												for (User user : userserach) {
													Map<String, Object> map = new HashMap<String, Object>();
													map.put("Cimages", user.getUimage());
													map.put("Cname", user.getUname());
													map.put("Cexplain", user.getUemail());
													map.put("userorcrowd", "好友");
													list.add(map);
												}
											}				
											ListView listView = (ListView) findViewById(R.id.serachlistview);
											SimpleAdapter deadapter = new SimpleAdapter(SerachActivity.this, list,
													R.layout.serach_itemlist,
													new String[] { "Cimages", "Cname", "Cexplain", "userorcrowd"},
													new int[] { R.id.serach_discuss_image, R.id.serach_discuss_name,
															R.id.serach_discuss_detail,R.id.userorcrowd });
											deadapter.setViewBinder(new ViewBinder() {
												public boolean setViewValue(View view, Object data,
														String textRepresentation) {
													if (view instanceof ImageView || data instanceof Drawable) {
														ImageView iv = (ImageView) view;
														iv.setBackgroundResource(((Number) data).intValue());
														return true;
													} else {
														return false;
													}
												}
											});
											listView.setAdapter(deadapter);

										} else {
											Log.i("查找群聊成功", "未找到查询的群聊");
											if(TAG==0){
												Toast.makeText(SerachActivity.this, "未查找到群和好友！", Toast.LENGTH_SHORT).show();
											}else{
												for (User user : userserach) {
													Map<String, Object> map = new HashMap<String, Object>();
													map.put("Cimages", user.getUimage());
													map.put("Cname", user.getUname());
													map.put("Cexplain", user.getUemail());
													map.put("userorcrowd", "好友");
													list.add(map);
												}
												ListView listView = (ListView) findViewById(R.id.serachlistview);
												SimpleAdapter deadapter = new SimpleAdapter(SerachActivity.this, list,
														R.layout.serach_itemlist,
														new String[] { "Cimages", "Cname", "Cexplain", "userorcrowd"},
														new int[] { R.id.serach_discuss_image, R.id.serach_discuss_name,
																R.id.serach_discuss_detail,R.id.userorcrowd });
												deadapter.setViewBinder(new ViewBinder() {
													public boolean setViewValue(View view, Object data,
															String textRepresentation) {
														if (view instanceof ImageView || data instanceof Drawable) {
															ImageView iv = (ImageView) view;
															iv.setBackgroundResource(((Number) data).intValue());
															return true;
														} else {
															return false;
														}
													}
												});
												listView.setAdapter(deadapter);
												Log.i("------------", ""+list.size());
											}
											
										}

									} else {
										Log.i("查找群聊失败", "" + arg1.getMessage() + arg1.getErrorCode());
									}

								}
							});
						}

					};
				}

			}
		});
	}

}

package com.example.gsjl.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.gsjl.model.User;
import com.example.gsjl.model.User_Crowd;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
*@author hugeng E-mail:958996499@qq.com
*@version:2018年5月14日下午4:51:45
*
*/
public class AddUserActivity extends Activity{
	private List<Map<String, Object>> list;
	private ListView listView;
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userlist);
		
		Button button=(Button) findViewById(R.id.userinfo_back);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		list = new ArrayList<Map<String, Object>>();
		listView=(android.widget.ListView) findViewById(R.id.userlistview);
		BmobQuery<User> user = new BmobQuery<User>();
		user.addWhereNotEqualTo("objectId", HomeActivity.user.getObjectId());
		user.order("Uemail");
		user.findObjects(new FindListener<User>() {

			@Override
			public void done(List<User> arg0, BmobException arg1) {
				if (arg1 == null) {
					if (arg0 != null) {
						Log.i("查询用户信息", "查询成功！共有：" + arg0.size() + "条用户信息。");
						
						// 查询好友信息
						for (User us : arg0) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("userimages", us.getUimage());
							map.put("name", us.getUname());
							map.put("email", us.getUemail());
							list.add(map);
						}
						SimpleAdapter deadapter = new SimpleAdapter(AddUserActivity.this, list, R.layout.talk_itemlist,
								new String[] { "userimages", "name", "email" },
								new int[] { R.id.talk_userimage, R.id.talk_username, R.id.email_talk });
						deadapter.setViewBinder(new ViewBinder() {
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
						listView.setAdapter(deadapter);
					} else {
						Log.i("查询用户信息", "用户信息为空");
					}
				} else {
					Log.i("查询用户信息", "查询失败！" + arg1.getMessage() + arg1.getErrorCode());
				}

			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				BmobQuery<User> user = new BmobQuery<User>();
				user.addWhereNotEqualTo("objectId", HomeActivity.user.getObjectId());
				user.order("Uemail");
				user.findObjects(new FindListener<User>() {

					@Override
					public void done(List<User> arg0, BmobException arg1) {
						if (arg1 == null) {
							if (arg0 != null) {
								Log.i("查询用户信息", "查询成功！共有：" + arg0.size() + "条用户信息。");
								Message message = handler.obtainMessage();
								message.what = 0;
								message.obj = arg0.get(position);
								handler.sendMessage(message);
								
							} else {
								Log.i("查询用户信息", "用户信息为空");
							}
						} else {
							Log.i("查询用户信息", "查询失败！" + arg1.getMessage() + arg1.getErrorCode());
						}

					}
				});
				
			}
		});
		handler =new Handler(){

			@Override
			public void handleMessage(Message msg) {
				User_Crowd user_Crowd=new User_Crowd();
				user_Crowd.setUid((User)msg.obj);
				user_Crowd.setCid(DissucssDetailActivity.crowd);
				user_Crowd.save(new SaveListener<String>() {
					
					@Override
					public void done(String arg0, BmobException arg1) {
						if(arg1==null){
							Log.i("邀请加入群聊成功", ""+arg0);
							Toast.makeText(AddUserActivity.this, "邀请成功！", Toast.LENGTH_SHORT).show();
							finish();
						}else{
							Log.i("邀请加入群聊失败",""+arg1.getMessage()+arg1.getErrorCode());
						}
						
					}
				});
			}
			
		};
		
	}
	
}

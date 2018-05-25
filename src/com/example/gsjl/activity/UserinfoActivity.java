package com.example.gsjl.activity;

import com.example.gsjl.model.Department;
import com.example.gsjl.model.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
*@author hugeng E-mail:958996499@qq.com
*@version:2018年4月30日下午11:31:10
*
*/
public class UserinfoActivity extends Activity{
	private TextView username;
	private TextView email;
	private TextView sex;
	private TextView department;
	private TextView role;
	private TextView phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_layout);
		BmobQuery<User> bmobQuery = new BmobQuery<User>();
		bmobQuery.include("Did");
		bmobQuery.getObject(HomeActivity.user.getObjectId(), new QueryListener<User>() {
			
			@Override
			public void done(User arg0, BmobException arg1) {
				// 查询用户信息
				if(arg1==null){
					if(arg0!=null){
						Log.i("查询成功", arg0.getUname());
						
						username=(TextView) findViewById(R.id.usernamein);
						email=(TextView) findViewById(R.id.emailin);
						sex=(TextView) findViewById(R.id.sexin);
						role=(TextView) findViewById(R.id.rolein);
						phone=(TextView) findViewById(R.id.phonein);
						department=(TextView) findViewById(R.id.departmentin);
						department.setText(arg0.getDid().getDname());
						username.setText(arg0.getUname());
						email.setText(arg0.getUemail());
						sex.setText(arg0.getUsex());
						role.setText(arg0.getUrole());
						phone.setText(arg0.getUphone());
					}
				}else{
					Log.i("查询失败", arg1.getMessage()+arg1.getErrorCode());
				}
				
			}
		});
		Button back=(Button) findViewById(R.id.userinfo_back);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		Button update=(Button) findViewById(R.id.update_userinfo);
		update.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(UserinfoActivity.this,UpdateUserinfoActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
}

package com.example.gsjl.activity;

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
*@version:2018年5月7日下午2:34:06
*
*/
public class TalkDetailActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.talkdetail);
		
		Button talkinfo_back =(Button) findViewById(R.id.talkinfo_back);
		Button talk_userinfo =(Button) findViewById(R.id.talk_userinfo);
		talkinfo_back.setOnClickListener(tab);
		talk_userinfo.setOnClickListener(tab);
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		TextView name =(TextView) findViewById(R.id.talkusernamein);
		name.setText(bundle.getString("talk_name"));
		BmobQuery<User> bmobQuery=new BmobQuery<User>();
		bmobQuery.include("Did");
		bmobQuery.getObject(bundle.getString("friendId"), new QueryListener<User>() {
			
			@Override
			public void done(User arg0, BmobException arg1) {
				if(arg1==null){
					Log.i("查询好友信息","查询成功："+arg0.getUname());
					TextView textView1=(TextView) findViewById(R.id.talkemailin);
					TextView textView2=(TextView) findViewById(R.id.talksexin);
					TextView textView3=(TextView) findViewById(R.id.talkdepartmentin);
					TextView textView4=(TextView) findViewById(R.id.talkrolein);
					TextView textView5=(TextView) findViewById(R.id.talkphonein);
					textView1.setText(arg0.getUemail());
					textView2.setText(arg0.getUsex());
					textView3.setText(arg0.getDid().getDname());
					textView4.setText(arg0.getUrole());
					textView5.setText(arg0.getUphone());
					
				}else{
					Log.i("查询好友信息","查询失败:"+arg1.getMessage()+arg1.getErrorCode());
				}
			}
		});
		
		
	}
	View.OnClickListener tab =new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
			
		}
	};
}

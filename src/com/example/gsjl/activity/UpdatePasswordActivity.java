package com.example.gsjl.activity;

import java.util.List;

import com.example.gsjl.model.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
*@author hugeng E-mail:958996499@qq.com
*@version:2018年5月1日下午3:14:42
*
*/
public class UpdatePasswordActivity extends Activity{
	private Bundle bundle;
	private String userID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatepassword);
		
		Intent intent=getIntent();
		bundle=intent.getExtras();
		BmobQuery<User> bmobQuery=new BmobQuery<User>();
		bmobQuery.addWhereEqualTo("Uphone", bundle.getString("userphone"));
		Log.i("手机号", bundle.getString("userphone"));
		bmobQuery.findObjects(new FindListener<User>() {
			
			@Override
			public void done(List<User> arg0, BmobException arg1) {
				// TODO Auto-generated method stub
				if(arg1==null){
					Log.i("查询成功", String.valueOf(arg0.size()));
					if(arg0.size()==1){
						for(User user:arg0){
							userID=user.getObjectId();
						}
					}
				}
				
			}
		});
		
		Button uppassback =(Button) findViewById(R.id.updatepassword_back);
		uppassback.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog alert = new AlertDialog.Builder(UpdatePasswordActivity.this).create();	
				alert.setMessage("确定放弃修改吗");	
				alert.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						

					}
				});
				alert.setButton(DialogInterface.BUTTON_POSITIVE,"确认", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();

					}
				});
				alert.show(); 
			}
		});
		Button uppassok1 =(Button) findViewById(R.id.updatepassword_yes);
		Button uppassok2 =(Button) findViewById(R.id.updatepassword_input);
		uppassok1.setOnClickListener(update);
		uppassok2.setOnClickListener(update);
	}
	
	View.OnClickListener update =new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			EditText newpassword =(EditText) findViewById(R.id.newpassword);
			EditText newpasswordre =(EditText) findViewById(R.id.newpasswordre);
			String pass=newpassword.getText().toString();
			String passre=newpasswordre.getText().toString();
			if("".equals(passre)||"".equals(pass)||!pass.equals(passre)){
				Toast.makeText(UpdatePasswordActivity.this, "密码为空或不一致", Toast.LENGTH_SHORT).show();
			}else{
				User user = new User();
				user.setUpassword(pass);
				user.update(userID, new UpdateListener() {
					
					@Override
					public void done(BmobException arg0) {
						// TODO Auto-generated method stub
						if(arg0==null){
				            Log.i("bmob","更新成功");
				            Toast.makeText(UpdatePasswordActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
				            finish();
				        }else{
				            Log.i("bmob","更新失败："+arg0.getMessage()+","+arg0.getErrorCode());
				        }
						
					}
				});		
			}		
		}
	};
}

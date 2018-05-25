package com.example.gsjl.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.gsjl.fragment.Discuss_fragment;
import com.example.gsjl.fragment.Forum_fragment;
import com.example.gsjl.fragment.Me_fragment;
import com.example.gsjl.fragment.Talk_fragment;
import com.example.gsjl.model.User;
import com.example.gsjl.util.EventUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import de.greenrobot.event.EventBus;

/**
*@author hugeng E-mail:958996499@qq.com
*@version:2018年4月28日下午4:20:08
*
*/
public class HomeActivity extends Activity {
	private ImageView image_me;
	private ImageView image_talk;
	private ImageView image_discuss;
	private ImageView image_forum;
	public static User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);
		Intent intent=getIntent();
		Bundle bundle =intent.getExtras();
		//更新本地用户信息
		
		BmobQuery<User> updateuser =new BmobQuery<User>();
		updateuser.findObjects(new FindListener<User>() {
			
			@Override
			public void done(List<User> arg0, BmobException arg1) {
				if(arg1==null){
					List<BmobIMUserInfo> list=new ArrayList<BmobIMUserInfo>();
					for(User user:arg0){
						BmobIMUserInfo userin=new BmobIMUserInfo();
						userin.setAvatar((user.getUimage()).toString());
						userin.setName(user.getUname());
						userin.setUserId(user.getObjectId());
						list.add(userin);
					}
					BmobIM.getInstance().updateBatchUserInfo(list);
					Log.i("更新本地用户信息成功", "--------------------------");
				}else{
					Log.i("更新本地用户信息失败", arg1.getMessage()+arg1.getErrorCode());
				}
				
			}
		});
		
		
		
		//获取当前用户信息
		BmobQuery<User> bmobQuery = new BmobQuery<User>();
		bmobQuery.getObject(bundle.getString("userID"), new QueryListener<User>() {
			
			@Override
			public void done(User arg0, BmobException arg1) {
				// TODO Auto-generated method stub
				if(arg1==null){
					Log.i("获取登陆用户信息", arg0.getUname()+arg0.getObjectId());
					user=arg0;
					EventBus.getDefault().post(new EventUtil(arg0));//将用户信息传递给fragment
		            
		        }else{
		        	Log.i("获取登陆用户信息", "查询失败：" + arg1.getMessage());
		        }
			}
		});
		
		image_me =(ImageView)findViewById(R.id.me_image);
		image_talk =(ImageView)findViewById(R.id.talk_image);
		image_discuss =(ImageView)findViewById(R.id.discuss_image);
		image_forum =(ImageView)findViewById(R.id.forum_image);
		image_me.setOnClickListener(tab);
		image_talk.setOnClickListener(tab);
		image_discuss.setOnClickListener(tab);
		image_forum.setOnClickListener(tab);
	}
	
	View.OnClickListener tab =new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			FragmentManager fm=getFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			Fragment f=null;
			switch (v.getId()) {
			case R.id.me_image:
				f=new Me_fragment();
				image_me.setEnabled(false);
				image_talk.setEnabled(true);
				image_discuss.setEnabled(true);
				image_forum.setEnabled(true);
				break;
			case R.id.talk_image:
				f=new Talk_fragment();
				image_me.setEnabled(true);
				image_talk.setEnabled(false);
				image_discuss.setEnabled(true);
				image_forum.setEnabled(true);
				break;
			case R.id.discuss_image:
				f=new Discuss_fragment();
				image_me.setEnabled(true);
				image_talk.setEnabled(true);
				image_discuss.setEnabled(false);
				image_forum.setEnabled(true);
				break;
			case R.id.forum_image:
				f=new Forum_fragment();
				image_me.setEnabled(true);
				image_talk.setEnabled(true);
				image_discuss.setEnabled(true);
				image_forum.setEnabled(false);
				break;
			default:
				break;
			}
			ft.replace(R.id.fragment, f);
			ft.commit();
			Intent intent=getIntent();
			Bundle bundle =intent.getExtras();
			//获取当前用户信息
			BmobQuery<User> bmobQuery = new BmobQuery<User>();
			bmobQuery.getObject(bundle.getString("userID"), new QueryListener<User>() {
				
				@Override
				public void done(User arg0, BmobException arg1) {
					// TODO Auto-generated method stub
					if(arg1==null){
						Log.i("获取登陆用户信息", arg0.getUname()+arg0.getObjectId());
						user=arg0;
						EventBus.getDefault().post(new EventUtil(arg0));//将用户信息传递给fragment
			            
			        }else{
			        	Log.i("获取登陆用户信息", "查询失败：" + arg1.getMessage());
			        }
				}
			});
		}
	};
}

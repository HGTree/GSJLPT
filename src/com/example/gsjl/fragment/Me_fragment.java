package com.example.gsjl.fragment;

import com.example.gsjl.activity.CheckPhoneActivity;
import com.example.gsjl.activity.ChooseImageActivity;
import com.example.gsjl.activity.DepartmentinfoActivity;
import com.example.gsjl.activity.MainActivity;
import com.example.gsjl.activity.R;
import com.example.gsjl.activity.RegisterActivity;
import com.example.gsjl.activity.UserinfoActivity;
import com.example.gsjl.util.EventImageUtil;
import com.example.gsjl.util.EventUtil;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.newim.BmobIM;
import de.greenrobot.event.EventBus;

/**
*@author hugeng E-mail:958996499@qq.com
*@version:2018年4月28日下午4:36:51
*
*/
public class Me_fragment extends Fragment{
	private ImageView imageView;
	private TextView textView;
	private View view;
	private String role;
	private String userid;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}
	
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);//
	}

	//用Eventbus传递的消息展示
	public void onEvent(EventUtil event){
		textView=(android.widget.TextView) view.findViewById(R.id.username);
		textView.setText(event.getUser().getUname());
		imageView=(android.widget.ImageView) view.findViewById(R.id.user_image);
		imageView.setBackgroundResource((event.getUser().getUimage()).intValue());
		role=event.getUser().getUrole();
		userid=event.getUser().getObjectId();
	}
	public void onEvent(EventImageUtil event){
		imageView=(android.widget.ImageView) view.findViewById(R.id.user_image);
		imageView.setBackgroundResource(ChooseImageActivity.imageid[event.getMsg()]);
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.me_fragment, null);
		
		ImageView image=(ImageView) view.findViewById(R.id.user_image);
//		String image2= String.valueOf(R.drawable.head1);
		image.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent =new Intent(getActivity(),ChooseImageActivity.class);
				
				startActivity(intent);	
			}
		});
		
		Button adduser =(Button) view.findViewById(R.id.me_add);
		adduser.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if("员工".equals(role)){
					Log.i("增加员工失败", "操作人："+userid+"职位："+role);
					Toast.makeText(getActivity(), "你没有权限增加员工", Toast.LENGTH_SHORT).show();
				}
				else{
					Log.i("增加员工", "操作人："+userid+"职位："+role);
					Intent intent = new Intent(getActivity(),RegisterActivity.class);
					startActivity(intent);
					
				}		
			}
		});
		
		TextView userinfo=(TextView) view.findViewById(R.id.userinfo);
		userinfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),UserinfoActivity.class);
				startActivity(intent);
			}
		});
		
		TextView updatepass=(TextView) view.findViewById(R.id.updatepass);
		updatepass.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),CheckPhoneActivity.class);
				startActivity(intent);
			}
		});
		
		TextView department=(TextView) view.findViewById(R.id.departmentinfo);
		department.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),DepartmentinfoActivity.class);
				startActivity(intent);
			}
		});
		
		TextView quit=(TextView) view.findViewById(R.id.quit);
		quit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				BmobIM.getInstance().clearAllConversation();
				
				AlertDialog alert = new AlertDialog.Builder(getActivity()).create();	
				alert.setMessage("你真的要退出吗？");	
				alert.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						

					}
				});
				alert.setButton(DialogInterface.BUTTON_POSITIVE,"确认", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(getActivity(),MainActivity.class);
						BmobIM.getInstance().disConnect();
						Log.i("断开服务器连接", "用户："+userid);
						startActivity(intent);
						getActivity().finish();

					}
				});
				alert.show(); 
			}
		});
		
		return view;
	}
}

package com.example.gsjl.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
*@author hugeng E-mail:958996499@qq.com
*@version:2018年5月2日下午7:32:48
*
*/
public class DiscussMessageActivity extends Activity{

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==0x3311||resultCode==0x3312){
			setResult(0x311);
			DiscussMessageActivity.this.finish();
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discussmessage);
		
		Button discussmessage_back =(Button) findViewById(R.id.discussmessage_back);
		discussmessage_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		
		Button discussdetail =(Button) findViewById(R.id.discussdetail);
		discussdetail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DiscussMessageActivity.this,DissucssDetailActivity.class);
				Bundle bundle=getIntent().getExtras();
				intent.putExtras(bundle);
				startActivityForResult(intent, 0x311);
//				startActivity(intent);
			}
		});
		//发送消息
		TextView discuss_message_input =(TextView) findViewById(R.id.discuss_message_input);
		discuss_message_input.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 存入数据库
				EditText message =(EditText) findViewById(R.id.discuss_message);
				String discuss_message=message.getText().toString();
				if("".equals(discuss_message)){
					Toast.makeText(DiscussMessageActivity.this, "消息不能为空", Toast.LENGTH_SHORT).show();
				}else{
					message.setText("");
					Toast.makeText(DiscussMessageActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
					
				}
				
				
			}
		});
		
		
		
		Bundle bundle=getIntent().getExtras();
		//通过传递过来的数据查询数据库
		TextView froumtitle =(TextView) findViewById(R.id.discussmessage_title);
		froumtitle.setText(bundle.getString("discuss_name"));
		List<Map<String,Object>> forumitems=getDate();
		ListView froumdetail=(ListView) findViewById(R.id.discussmessage_detail);
		SimpleAdapter froumadapter = new SimpleAdapter(this, forumitems,R.layout.discussmessage_item,
				new String[] { "time", "userimages" ,"message"}, 
				new int[] {R.id.discussmessage_time, R.id.image_discuss_user ,R.id.discussmessage_user}); 
		froumdetail.setAdapter(froumadapter);
		
	}
	
	public List<Map<String,Object>> getDate(){
		
		//从数据库取出论坛消息数据
				String[] time=new String[]{"20:07","20:08","20:09","20:10","20:11","20:12","20:13","20:14","20:15","20:16"};
				int[] userimages=new int[]{R.drawable.head1,R.drawable.head2,R.drawable.head3,R.drawable.head4,R.drawable.head5,
						R.drawable.head6,R.drawable.head7,R.drawable.head8,R.drawable.head9};
				String[] message=new String[]{"大家今天工作进度怎么样","顺利完成","我还差一点，半小时内搞定","我今天外出，还未完成",
						"我完成了","我也是","看来大家总体完成情况还不错，发个红包鼓励一下大家","谢谢老板","红包"};
				
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		for(int i=0;i<userimages.length;i++){
			Map<String, Object> map = new HashMap<String, Object>(); 
			map.put("time", time[i]);
			map.put("userimages", userimages[i]);
			map.put("message", message[i]);
			list.add(map);
		}
		return list;
	}

}

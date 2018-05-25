package com.example.gsjl.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.gsjl.adapter.MessageAdatper;
import com.example.gsjl.fragment.Discuss_friend_fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.exception.BmobException;
import de.greenrobot.event.EventBus;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年5月7日下午2:21:33
 *
 */
public class TalkMessageActivity extends Activity {
	private String talk_messages;
	private BmobIMConversation messageManager;
	private EditText message;
	private List<Map<String, Object>> list;
	
	public void onEventMainThread(MessageEvent event){
		
		if((event.getFromUserInfo().getUserId()).equals(getIntent().getExtras().get("friendId"))){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userimages", string2int(event.getFromUserInfo().getAvatar()));
			map.put("name",event.getFromUserInfo().getName());
			map.put("tag", event.getFromUserInfo().getUserId());
			map.put("detail",event.getMessage().getContent());
			map.put("time", Long2String(event.getMessage().getUpdateTime()));
			list.add(map);
			updatelistdeta();
		}
		Log.i("", "event.getFromUserInfo().getName()");
		
	}
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		setContentView(R.layout.talkmessage);

		list = new ArrayList<Map<String, Object>>();
		Bundle bundle = getIntent().getExtras();
		// 通过传递过来的数据查询数据库
		TextView talktitle = (TextView) findViewById(R.id.talkmessage_title);
		talktitle.setText(bundle.getString("talk_name"));
		BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(
				BmobIM.getInstance().getUserInfo(bundle.getString("friendId")), new ConversationListener() {

					@Override
					public void done(BmobIMConversation arg0, BmobException arg1) {
						if (arg1 == null) {
							Log.i("创建会话成功", "--------------");
						} else {
							Log.i("创建会话失败", arg1.getMessage() + arg1.getErrorCode());
						}

					}
				});
		conversationEntrance.queryMessages(null, 20, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> lists, BmobException arg1) {
                if (arg1 == null) {
                    if (null != lists && lists.size() > 0) {
                    	Log.i("查询会话消息成功", "");
                    	for(BmobIMMessage mess:lists){
                    		Map<String, Object> map = new HashMap<String, Object>();
                    		if((mess.getFromId()).equals(HomeActivity.user.getObjectId())){
                    			map.put("userimages", (HomeActivity.user.getUimage()).intValue());
                    			map.put("name", HomeActivity.user.getUname());
                    			map.put("tag", HomeActivity.user.getObjectId());
                    		}else{
                    			map.put("userimages", string2int((BmobIM.getInstance().getUserInfo(mess.getFromId())).getAvatar()));
                    			map.put("name",(BmobIM.getInstance().getUserInfo(mess.getFromId())).getName());
                    			map.put("tag", mess.getFromId());
                    		}
                    		
        					map.put("detail", mess.getContent());
        					map.put("time", Long2String(mess.getCreateTime()));
        					list.add(map);
        					updatelistdeta();
                    	}
                    	
                    }
                } else {
                	Log.i("查询会话消息失败", arg1.getMessage() + arg1.getErrorCode());
                }
            }
        });
		messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
		messageManager.updateLocalCache();

		Button talkmessage_back = (Button) findViewById(R.id.talkmessage_back);
		talkmessage_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(0x211);
				finish();

			}
		});

		Button talk_detail = (Button) findViewById(R.id.talk_detail);
		talk_detail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TalkMessageActivity.this, TalkDetailActivity.class);
				Bundle bundle = getIntent().getExtras();
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		// 发送消息
		TextView talk_message_input = (TextView) findViewById(R.id.talk_message_input);
		talk_message_input.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 存入数据库
				message = (EditText) findViewById(R.id.talk_messages);
				talk_messages = message.getText().toString();
				if ("".equals(talk_messages)) {
					Toast.makeText(TalkMessageActivity.this, "消息不能为空", Toast.LENGTH_SHORT).show();
				} else {
					sendMessage();

				}

			}
		});

		// List<Map<String,Object>> forumitems=getDate();
		// ListView froumdetail=(ListView)
		// findViewById(R.id.talkmessage_detail);
		// SimpleAdapter froumadapter = new SimpleAdapter(this,
		// forumitems,R.layout.talkmessage_item,
		// new String[] { "time", "userimages" ,"message"},
		// new int[] {R.id.talkmessage_time, R.id.image_talk_user
		// ,R.id.talkmessage_user});
		// froumdetail.setAdapter(froumadapter);

	}

	/**
	 * 发送文本消息
	 */
	private void sendMessage() {
		// TODO 发送消息：6.1、发送文本消息
		BmobIMTextMessage msg = new BmobIMTextMessage();
		msg.setContent(talk_messages);
		// 可随意设置额外信息
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("level", "1");
		msg.setExtraMap(map);
		messageManager.sendMessage(msg, new MessageSendListener() {
			
			@Override
			public void done(BmobIMMessage arg0, BmobException arg1) {
				if(arg1==null){
					Log.i("发送消息成功", ""+arg0);
					message.setText("");
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userimages", (HomeActivity.user.getUimage()).intValue());
					map.put("name", HomeActivity.user.getUname());
					map.put("tag", HomeActivity.user.getObjectId());
					map.put("detail",talk_messages);
					map.put("time", Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+":"+Calendar.getInstance().get(Calendar.MINUTE)
							+":"+Calendar.getInstance().get(Calendar.SECOND));
					list.add(map);
					updatelistdeta();
				}else{
					Log.i("发送消息失败", arg1.getMessage()+arg1.getErrorCode());
					Toast.makeText(TalkMessageActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}
	private void updatelistdeta(){
		ListView listView =(ListView) findViewById(R.id.talkmessage_detail);
		if(list.size()>6){
			listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			listView.setStackFromBottom(true);
		}
		MessageAdatper mAdapter=new MessageAdatper(list, TalkMessageActivity.this);
		listView.setAdapter(mAdapter);
	}
	public String Long2String(Long time){
		SimpleDateFormat sdf= new SimpleDateFormat("HH:mm:ss");
		java.util.Date dt = new Date(time);
		String Stime = sdf.format(dt);
		return Stime;
	}
	public int string2int(String image){
		String head=image.substring(0, 1);
		String tail=image.substring(2, 11);
		String im=head+tail;
		
		return Integer.parseInt(im);
	}
}

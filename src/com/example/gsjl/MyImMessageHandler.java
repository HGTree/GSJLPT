package com.example.gsjl;

import java.util.List;
import java.util.Map;

import com.example.gsjl.activity.MainActivity;

import android.content.Context;
import android.util.Log;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.v3.exception.BmobException;
import de.greenrobot.event.EventBus;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年5月11日下午2:02:34
 *
 */
public class MyImMessageHandler extends BmobIMMessageHandler {
//	private Context context;
//
//	public MyImMessageHandler(Context context) {
//		this.context = context;
//	}

	@Override
	public void onMessageReceive(MessageEvent arg0) {
		// TODO Auto-generated method stub
		super.onMessageReceive(arg0);
		
		Log.i("------------------", arg0.getMessage().getContent());

		executeMessage(arg0);
		// MainActivity.tv_message.append("接收到："+messageEvent.getMessage().getContent()+"\n");
	}

	@Override
	public void onOfflineReceive(OfflineMessageEvent arg0) {
		// TODO Auto-generated method stub
		super.onOfflineReceive(arg0);
		Log.i("------------------", "");
		
		
		// 每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
		Map<String, List<MessageEvent>> map = arg0.getEventMap();
		Log.i("", "有" + map.size() + "个用户发来离线消息");
		// 挨个检测下离线消息所属的用户的信息是否需要更新
		for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
			List<MessageEvent> list = entry.getValue();
			int size = list.size();
			Log.i("", "用户" + entry.getKey() + "发来" + size + "条消息");
			for (int i = 0; i < size; i++) {
				// 处理每条消息
				executeMessage(list.get(i));
			}
		}
	}

	/**
	 * 处理消息
	 *
	 * @param event
	 */
	private void executeMessage(final MessageEvent event) {
		// 检测用户信息是否需要更新

		BmobIMMessage msg = event.getMessage();

		// SDK内部内部支持的消息类型
		processSDKMessage(msg, event);

	}

	/**
	 * 处理SDK支持的消息
	 *
	 * @param msg
	 * @param event
	 */
	private void processSDKMessage(BmobIMMessage msg, MessageEvent event) {

		// 直接发送消息事件
		EventBus.getDefault().post(event);

	}

}

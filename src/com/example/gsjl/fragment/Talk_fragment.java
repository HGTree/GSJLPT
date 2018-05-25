package com.example.gsjl.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.gsjl.activity.R;
import com.example.gsjl.activity.TalkMessageActivity;
import com.example.gsjl.adapter.MessageAdatper;
import com.example.gsjl.adapter.UnreadAdapter;

import android.app.ListFragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.SimpleAdapter.ViewBinder;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.v3.b.name;
import de.greenrobot.event.EventBus;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年4月28日下午4:37:17
 *
 */
public class Talk_fragment extends ListFragment {
	private Handler handler;
	private List<Map<String, Object>> list;
	private View view;

	public void onEventMainThread(OfflineMessageEvent event) {
		updatedata();

	}

	public void onEventMainThread(MessageEvent event) {
		updatedata();

	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		updatedata();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.talk_fragment, null);

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		updatedata();

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub

		super.onListItemClick(l, v, position, id);
		List<BmobIMConversation> imlist = BmobIM.getInstance().loadAllConversation();
		Intent intent = new Intent(getActivity(), TalkMessageActivity.class);
		intent.putExtra("talk_name", BmobIM.getInstance().getUserInfo(imlist.get(position).getConversationId()).getName());
		intent.putExtra("friendId", imlist.get(position).getConversationId());
		startActivityForResult(intent, 0x211);
		Log.i("", "" + imlist.get(position).getConversationId() + imlist.get(position).getConversationTitle());
	}

	public void updatedata() {

		list = new ArrayList<Map<String, Object>>();
		List<BmobIMConversation> imlist = BmobIM.getInstance().loadAllConversation();
		Log.i("0000000000000000", "" + imlist.size());
		if (imlist != null && imlist.size() > 0) {
			for (BmobIMConversation item : imlist) {
				Map<String, Object> map = new HashMap<String, Object>();

				if (item.getMessages().isEmpty()) {
					map.put("detail", "");
				} else {
					map.put("detail", item.getMessages().get(0).getContent());
				}
				map.put("uimage", string2int(BmobIM.getInstance().getUserInfo(item.getConversationId()).getAvatar()));
				// Log.i("-------------", ""+item.getConversationIcon());
				// map.put("uimage",2130837607);
				map.put("name", BmobIM.getInstance().getUserInfo(item.getConversationId()).getName());
				map.put("time", Long2String(item.getUpdateTime()));
				map.put("num", BmobIM.getInstance().getUnReadCount(item.getConversationId()));
				list.add(map);

			}
		}
		UnreadAdapter mAdapter=new UnreadAdapter(list, getActivity());
		setListAdapter(mAdapter);
	}

	public int string2int(String image) {
		String head = image.substring(0, 1);
		String tail = image.substring(2, 11);
		String im = head + tail;

		return Integer.parseInt(im);
	}

	public String Long2String(Long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		java.util.Date dt = new Date(time);
		String Stime = sdf.format(dt);
		return Stime;
	}

}

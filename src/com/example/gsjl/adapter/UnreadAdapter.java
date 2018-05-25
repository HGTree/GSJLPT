package com.example.gsjl.adapter;

import java.util.List;
import java.util.Map;

import com.example.gsjl.activity.HomeActivity;
import com.example.gsjl.activity.R;
import com.example.gsjl.adapter.MessageAdatper.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
*@author hugeng E-mail:958996499@qq.com
*@version:2018年5月17日下午8:30:50
*
*/
public class UnreadAdapter extends BaseAdapter{
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;

	public UnreadAdapter(List<Map<String, Object>> list, Context context) {
		super();
		this.list = list;
		mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
				view =mInflater.inflate(R.layout.talk_message_item, null);
				viewHolder=new ViewHolder();
				viewHolder.icon=(ImageView)view.findViewById(R.id.message_image);
				viewHolder.name=(TextView)view.findViewById(R.id.message_name);
				viewHolder.detail=(TextView)view.findViewById(R.id.message_newmessage);
				viewHolder.time=(TextView)view.findViewById(R.id.message_time);
				viewHolder.unread=(TextView)view.findViewById(R.id.message_unread);
				view.setTag(viewHolder);
		}else{
			view=convertView;
			viewHolder=(ViewHolder) view.getTag();
		}
		viewHolder.time.setText((CharSequence) list.get(position).get("time"));
		viewHolder.icon.setBackgroundResource((Integer) list.get(position).get("uimage"));
		viewHolder.name.setText((CharSequence) list.get(position).get("name"));
		viewHolder.detail.setText((CharSequence) list.get(position).get("detail"));
		if(((Long)list.get(position).get("num"))>0){
			viewHolder.unread.setText((list.get(position).get("num")).toString());
			viewHolder.unread.setVisibility(View.VISIBLE);
		}else{
			viewHolder.unread.setVisibility(View.GONE);
		}
		
		return view;
	}

	static class ViewHolder {
		ImageView icon;
		TextView name;
		TextView detail;
		TextView time;
		TextView unread;
	}

}

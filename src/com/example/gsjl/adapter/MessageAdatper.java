package com.example.gsjl.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.gsjl.activity.HomeActivity;
import com.example.gsjl.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年5月17日下午7:22:52
 *
 */
public class MessageAdatper extends BaseAdapter {
	private List<Map<String, Object>> list;
	private LayoutInflater mInflater;

	public MessageAdatper(List<Map<String, Object>> list, Context context) {
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
			if((list.get(position).get("tag")).equals(HomeActivity.user.getObjectId())){
				view =mInflater.inflate(R.layout.talkmessage_item_backup, null);
				viewHolder=new ViewHolder();
				viewHolder.time=(TextView)view.findViewById(R.id.talkmessage_time_backup);
				viewHolder.icon=(ImageView)view.findViewById(R.id.image_talk_user_backup);
				viewHolder.name=(TextView)view.findViewById(R.id.talkmessage_username_backup);
				viewHolder.detail=(TextView)view.findViewById(R.id.talkmessage_user_backup);
			}else{
				view =mInflater.inflate(R.layout.talkmessage_item, null);
				viewHolder=new ViewHolder();
				viewHolder.time=(TextView)view.findViewById(R.id.talkmessage_time);
				viewHolder.icon=(ImageView)view.findViewById(R.id.image_talk_user);
				viewHolder.name=(TextView)view.findViewById(R.id.talkmessage_username);
				viewHolder.detail=(TextView)view.findViewById(R.id.talkmessage_user);	
			}
			view.setTag(viewHolder);
		}else{
			view=convertView;
			viewHolder=(ViewHolder) view.getTag();
		}
		viewHolder.time.setText((CharSequence) list.get(position).get("time"));
		viewHolder.icon.setBackgroundResource((Integer) list.get(position).get("userimages"));
		viewHolder.name.setText((CharSequence) list.get(position).get("name"));
		viewHolder.detail.setText((CharSequence) list.get(position).get("detail"));
		return view;
	}

	static class ViewHolder {
		TextView time;
		ImageView icon;
		TextView name;
		TextView detail;
	}

}

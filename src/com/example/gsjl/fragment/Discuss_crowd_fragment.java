package com.example.gsjl.fragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.gsjl.activity.DiscussMessageActivity;
import com.example.gsjl.activity.HomeActivity;
import com.example.gsjl.activity.R;
import com.example.gsjl.model.Crowd;
import com.example.gsjl.model.User;
import com.example.gsjl.model.User_Crowd;

import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
*@author hugeng E-mail:958996499@qq.com
*@version:2018年5月12日下午5:33:17
*
*/
public class Discuss_crowd_fragment extends ListFragment{
	private List<Map<String, Object>> list;
	private List<Map<String, Object>> listrefresh;
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		listrefresh = new ArrayList<Map<String, Object>>();
		BmobQuery<User_Crowd> uc=new BmobQuery<User_Crowd>();
		uc.addWhereEqualTo("Uid", HomeActivity.user);
		uc.include("Cid");
		uc.order("-createdAt");
		uc.findObjects(new FindListener<User_Crowd>() {
			
			@Override
			public void done(List<User_Crowd> arg0, BmobException arg1) {
				if(arg1==null){
					if(arg0!=null){
						Log.i("查询群信息", "查询成功！共有："+arg0.size()+"群。");
						for (User_Crowd userc : arg0) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("Cimages", userc.getCid().getCimage());
							map.put("Cname", userc.getCid().getCname());
							map.put("Cexplain", userc.getCid().getCexplain());
							listrefresh.add(map);
						}
						SimpleAdapter deadapter = new SimpleAdapter(getActivity(), listrefresh, R.layout.disscuss_itemlist,
								new String[] { "Cimages", "Cname", "Cexplain" },
								new int[] { R.id.discuss_image, R.id.discuss_name, R.id.discuss_detail});
						deadapter.setViewBinder(new ViewBinder() {
							public boolean setViewValue(View view, Object data, String textRepresentation) {
								if (view instanceof ImageView || data instanceof Drawable) {
									ImageView iv = (ImageView) view;
									iv.setBackgroundResource(((Number) data).intValue());
									return true;
								} else {
									return false;
								}
							}
						});
						setListAdapter(deadapter);
					}else{
						Log.i("查询群信息", "群为空");
					}
				}else{
					Log.i("查询群信息","查询失败！"+arg1.getMessage()+arg1.getErrorCode());
				}
				
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.discuss_crowd, null);
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		list = new ArrayList<Map<String, Object>>();
		BmobQuery<User_Crowd> uc=new BmobQuery<User_Crowd>();
		uc.addWhereEqualTo("Uid", HomeActivity.user);
		uc.include("Cid");
		uc.order("-createdAt");
		uc.findObjects(new FindListener<User_Crowd>() {
			
			@Override
			public void done(List<User_Crowd> arg0, BmobException arg1) {
				if(arg1==null){
					if(arg0!=null){
						Log.i("查询群信息", "查询成功！共有："+arg0.size()+"群。");
						for (User_Crowd userc : arg0) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("Cimages", userc.getCid().getCimage());
							map.put("Cname", userc.getCid().getCname());
							map.put("Cexplain", userc.getCid().getCexplain());
							list.add(map);
						}
						SimpleAdapter deadapter = new SimpleAdapter(getActivity(), list, R.layout.disscuss_itemlist,
								new String[] { "Cimages", "Cname", "Cexplain" },
								new int[] { R.id.discuss_image, R.id.discuss_name, R.id.discuss_detail});
						deadapter.setViewBinder(new ViewBinder() {
							public boolean setViewValue(View view, Object data, String textRepresentation) {
								if (view instanceof ImageView || data instanceof Drawable) {
									ImageView iv = (ImageView) view;
									iv.setBackgroundResource(((Number) data).intValue());
									return true;
								} else {
									return false;
								}
							}
						});
						setListAdapter(deadapter);
					}else{
						Log.i("查询群信息", "群为空");
					}
				}else{
					Log.i("查询群信息","查询失败！"+arg1.getMessage()+arg1.getErrorCode());
				}
				
			}
		});

		
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String discuss_name = ((TextView) v.findViewById(R.id.discuss_name)).getText().toString();
		Intent intent = new Intent(getActivity(), DiscussMessageActivity.class);
		intent.putExtra("discuss_name", discuss_name);
		intent.putExtra("position", position);
		startActivityForResult(intent, 0x311);
	}

}

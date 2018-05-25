package com.example.gsjl.fragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.gsjl.activity.HomeActivity;
import com.example.gsjl.activity.R;
import com.example.gsjl.activity.TalkMessageActivity;
import com.example.gsjl.model.User;

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
import android.widget.SimpleAdapter.ViewBinder;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年5月12日下午5:32:44
 *
 */
public class Discuss_friend_fragment extends ListFragment {
	private List<Map<String, Object>> list;
	private String talk_name ;
	public static User userinfo;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// View view =inflater.inflate(R.layout.discuss_user, container, true);
		View view = inflater.inflate(R.layout.discuss_user, null);
		return view;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		list = new ArrayList<Map<String, Object>>();
		BmobQuery<User> user = new BmobQuery<User>();
		user.addWhereNotEqualTo("objectId", HomeActivity.user.getObjectId());
		user.order("Uemail");
		user.findObjects(new FindListener<User>() {

			@Override
			public void done(List<User> arg0, BmobException arg1) {
				if (arg1 == null) {
					if (arg0 != null) {
						Log.i("查询用户信息", "查询成功！共有：" + arg0.size() + "条用户信息。");
						// 查询好友信息
						for (User us : arg0) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("userimages", us.getUimage());
							map.put("name", us.getUname());
							map.put("email", us.getUemail());
							list.add(map);
						}
						SimpleAdapter deadapter = new SimpleAdapter(getActivity(), list, R.layout.talk_itemlist,
								new String[] { "userimages", "name", "email" },
								new int[] { R.id.talk_userimage, R.id.talk_username, R.id.email_talk });
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
					} else {
						Log.i("查询用户信息", "用户信息为空");
					}
				} else {
					Log.i("查询用户信息", "查询失败！" + arg1.getMessage() + arg1.getErrorCode());
				}

			}
		});

	}

	@Override
	public void onListItemClick(ListView l, View v, final int position, long id) {

		super.onListItemClick(l, v, position, id);
		talk_name = ((TextView) v.findViewById(R.id.talk_username)).getText().toString();
		BmobQuery<User> user = new BmobQuery<User>();
		user.addWhereNotEqualTo("objectId", HomeActivity.user.getObjectId());
		user.order("Uemail");
		user.findObjects(new FindListener<User>() {

			@Override
			public void done(List<User> arg0, BmobException arg1) {
				if (arg1 == null) {
					if (arg0 != null) {
						Log.i("查询用户信息", "查询成功！共有：" + arg0.size() + "条用户信息。");		
						userinfo=arg0.get(position);
						Intent intent = new Intent(getActivity(), TalkMessageActivity.class);
						intent.putExtra("talk_name", talk_name);
						intent.putExtra("friendId", arg0.get(position).getObjectId());
						startActivity(intent);
					} else {
						Log.i("查询用户信息", "用户信息为空");
					}
				} else {
					Log.i("查询用户信息", "查询失败！" + arg1.getMessage() + arg1.getErrorCode());
				}

			}
		});
		
//		intent.putExtra("imagec", bytes);
//		ImageView im = (ImageView) v.findViewById(R.id.talk_userimage);
//		Drawable imagec = im.getDrawable();
//		drawableToBitamp(imagec);
//		byte[] bytes = bitmap2Bytes(bitmap);
	}

	// drawable转化成bitmap的方法
	// 传输图片数据,转换格式
//	private Bitmap bitmap;
//	private void drawableToBitamp(Drawable drawable) {
//		int w = drawable.getIntrinsicWidth();
//		int h = drawable.getIntrinsicHeight();
//		System.out.println("Drawable转Bitmap");
//		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//				: Bitmap.Config.RGB_565;
//		bitmap = Bitmap.createBitmap(w, h, config);
//		// 注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
//		Canvas canvas = new Canvas(bitmap);
//		drawable.setBounds(0, 0, w, h);
//		drawable.draw(canvas);
//	}
//
//	private byte[] bitmap2Bytes(Bitmap bm) {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
//		return baos.toByteArray();
//	}

}

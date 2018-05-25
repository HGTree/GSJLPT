package com.example.gsjl.activity;

import com.example.gsjl.model.User;
import com.example.gsjl.util.EventImageUtil;
import com.example.gsjl.util.EventUtil;
import com.example.gsjl.activity.HomeActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import de.greenrobot.event.EventBus;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
*@author hugeng E-mail:958996499@qq.com
*@version:2018年5月7日下午9:53:49
*
*/
public class ChooseImageActivity extends Activity{
	public  static int[] imageid=new int[]{R.drawable.head1,R.drawable.head2,R.drawable.head3,R.drawable.head4,R.drawable.head47,
			R.drawable.head5,R.drawable.head6,R.drawable.head7,R.drawable.head8,R.drawable.head9,R.drawable.head10,
			R.drawable.head11,R.drawable.head12,R.drawable.head13,R.drawable.head14,R.drawable.head15,R.drawable.head16,
			R.drawable.head17,R.drawable.head18,R.drawable.head19,R.drawable.head20,R.drawable.head21,R.drawable.head22,
			R.drawable.head23,R.drawable.head24,R.drawable.head25,R.drawable.head26,R.drawable.head27,R.drawable.head28,
			R.drawable.head29,R.drawable.head30,R.drawable.head31,R.drawable.head32,R.drawable.head33,R.drawable.head34,
			R.drawable.head35,R.drawable.head36,R.drawable.head37,R.drawable.head38,R.drawable.head39,R.drawable.head40,
			R.drawable.head41,R.drawable.head42,R.drawable.head43,R.drawable.head44,R.drawable.head45,R.drawable.head46};
	private int image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.chooseimage);
		GridView gridview = (GridView) findViewById(R.id.gridView1); 			
		BaseAdapter adapter=new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ImageView imageview;							
				if(convertView==null){
					imageview=new ImageView(ChooseImageActivity.this);		
					imageview.setAdjustViewBounds(true);
					imageview.setMaxWidth(158);
					imageview.setMaxHeight(150);
					imageview.setPadding(5, 5, 5, 5);				
				}else{
					imageview=(ImageView)convertView;
				}
				imageview.setImageResource(imageid[position]);		
				return imageview;	//����ImageView
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return position;
			}

			@Override
			public int getCount() {
				return imageid.length;
			}
		};
		
		gridview.setAdapter(adapter); 									
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {		
				EventBus.getDefault().post(new EventImageUtil(position));
				image=position;
				User user=new User();
				user.setUimage(imageid[image]);
				user.update(HomeActivity.user.getObjectId(), new UpdateListener() {
					
					@Override
					public void done(BmobException arg0) {
						// TODO Auto-generated method stub
						if(arg0==null){
				            Log.i("bmob","更新成功");
				            
				        }else{
				            Log.i("bmob","更新失败："+arg0.getMessage()+","+arg0.getErrorCode());
				        }
						
					}
				});
				finish();
			}
		});
	}

}

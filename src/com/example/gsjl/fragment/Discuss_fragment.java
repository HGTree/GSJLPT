package com.example.gsjl.fragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.gsjl.activity.AddDiscussActivity;
import com.example.gsjl.activity.AddfroumActivity;
import com.example.gsjl.activity.DiscussMessageActivity;
import com.example.gsjl.activity.FroumDetailActivity;
import com.example.gsjl.activity.R;
import com.example.gsjl.activity.SerachActivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年4月28日下午4:38:09
 *
 */
public class Discuss_fragment extends Fragment {
	private Discuss_crowd_fragment crowd;
	private Discuss_friend_fragment friend;
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==0x311){
			onCreate(null);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.discuss_fragment, null);
		crowd=new Discuss_crowd_fragment();
		friend=new Discuss_friend_fragment();
		FragmentManager fmuser=getChildFragmentManager();
		FragmentTransaction ftuser=fmuser.beginTransaction();
		ftuser.add(R.id.which_fragment, crowd);
		ftuser.add(R.id.which_fragment, friend);
		ftuser.hide(crowd);
		ftuser.show(friend);
		ftuser.commit();
		
		TextView search=(TextView) view.findViewById(R.id.discuss_name);
		search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),SerachActivity.class);
				startActivity(intent);
				
			}
		});
		
		Button adddisscuss = (Button) view.findViewById(R.id.discuss_add);
		adddisscuss.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), AddDiscussActivity.class);
				startActivityForResult(intent, 0x311);
			}
		});
		
		TextView friends=(TextView) view.findViewById(R.id.discuss_user);
		TextView crowds=(TextView) view.findViewById(R.id.discuss_crowd);
		friends.setOnClickListener(tab);
		crowds.setOnClickListener(tab);
		return view;
	}

	View.OnClickListener tab=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			FragmentManager fmuser=getChildFragmentManager();
			FragmentTransaction ftuser=fmuser.beginTransaction();
			Fragment f=null;
			switch (v.getId()) {
			case R.id.discuss_user:
				f=new Discuss_friend_fragment();
				ftuser.hide(crowd);
				ftuser.show(friend);
				ftuser.commit();
				break;
			case R.id.discuss_crowd:
				f=new Discuss_crowd_fragment();
				ftuser.hide(friend);
				ftuser.show(crowd);
				ftuser.commit();
				break;
			default:
				break;
			}
			
		}
	};
	

}

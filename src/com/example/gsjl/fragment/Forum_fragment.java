package com.example.gsjl.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.gsjl.activity.AddfroumActivity;
import com.example.gsjl.activity.FroumDetailActivity;
import com.example.gsjl.activity.HomeActivity;
import com.example.gsjl.activity.R;
import com.example.gsjl.model.MessageFroum;
import com.example.gsjl.model.User;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.GetChars;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年4月28日下午4:38:39
 *
 */
public class Forum_fragment extends ListFragment {
	private List<Map<String,Object>> list;
	private String detail;
	private String deleteid;
	private Handler handler;
	private Handler handlerdetail;
	private Handler handlerdelete;
	private Message message;
	private Message messagedetail;
	private Message messagedelete;
	// 刷新
	private void refresh() {
		onCreate(null);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.forum_fragment, null);
		
		ListView listView=(ListView) view.findViewById(android.R.id.list);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//				Toast.makeText(getActivity(), "你点击了"+position, Toast.LENGTH_SHORT).show();
				BmobQuery<MessageFroum> me = new BmobQuery<MessageFroum>();
				me.addWhereEqualTo("isHeading", "是");
				me.order("-updatedAt");
				me.include("Uid");
				me.findObjects(new FindListener<MessageFroum>() {
					
					@Override
					public void done(List<MessageFroum> arg0, BmobException arg1) {
						// TODO Auto-generated method stub
						if(arg1==null){
							if(arg0!=null){
								Log.i("查询话题成功","共"+arg0.size()+"条记录,点击了第"+position+"条，"+arg0.get(position).getMFcontent()+","+HomeActivity.user.getUrole());
								if(HomeActivity.user.getUrole().equals("管理员")||arg0.get(position).getUid().getObjectId().equals(HomeActivity.user.getObjectId())){
									Log.i("", "--------------");
									
									message = handler.obtainMessage();
									message.what = 0;
									message.obj = arg0.get(position).getObjectId();
									messagedetail = handlerdetail.obtainMessage();
									messagedetail.what = 1;
									messagedetail.obj = arg0.get(position).getObjectId();
									AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
									alert.setMessage("确定放弃删除话题吗");
									alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {

										}
									});
									alert.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog, int which) {
											
											// 向handler发送消息
											handler.sendMessage(message);
											handlerdetail.sendMessage(messagedetail);
										}
									});
									alert.show();
								}
							}
						}else{
							Log.i("查询话题失败",arg1.getMessage()+","+arg1.getErrorCode());
						}
					}
				});

				return true;
			}
		});
		//删除话题
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
					MessageFroum message=new MessageFroum();
					message.setObjectId((String) msg.obj);
					message.delete(new UpdateListener() {
						
						@Override
						public void done(BmobException arg0) {
							if(arg0==null){
								Log.i("删除话题", "删除成功");
							}else{
								Log.i("删除话题", "删除失败"+arg0.getMessage()+arg0.getErrorCode());
							}
							
						}
					});
					refresh();
				}
			};
			//查询话题下的回复
			handlerdetail = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					BmobQuery<MessageFroum> message=new BmobQuery<MessageFroum>();
					message.addWhereEqualTo("MFid", (String)msg.obj);
					message.findObjects(new FindListener<MessageFroum>() {
						@Override
						public void done(List<MessageFroum> arg0, BmobException arg1) {
							if(arg1==null){
								if(arg0!=null){
									Log.i("查询话题回复", "查询成功");
									messagedelete = handlerdelete.obtainMessage();
									messagedelete.what = 0;
									messagedelete.obj = arg0;
									handlerdelete.sendMessage(messagedelete);
								}
								else{
									Log.i("查询话题回复", "为空");
								}
							}else{
								Log.i("查询话题回复", "查询失败"+arg1.getMessage()+arg1.getErrorCode());
							}
							
						}
					});
					}
				};
				//删除话题下的回复
				handlerdelete = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						new BmobBatch().deleteBatch((List<BmobObject>) msg.obj).doBatch(new QueryListListener<BatchResult>() {

				            @Override
				            public void done(List<BatchResult> o, BmobException e) {
				                if(e==null){
				                	if(o!=null){
				                		for(int i=0;i<o.size();i++){
					                        BatchResult result = o.get(i);
					                        BmobException ex =result.getError();
					                        if(ex==null){
					                            Log.i("删除回复", "第"+i+"个数据批量删除成功");
					                        }else{
					                            Log.i("删除回复", "第"+i+"个数据批量删除失败："+ex.getMessage()+","+ex.getErrorCode());
					                        }
					                    }
				                	}else{
				                		Log.i("删除回复", "回复为空");
				                	}
				                    
				                }else{
				                    Log.i("删除回复","失败："+e.getMessage()+","+e.getErrorCode());
				                }
				            }
				        });
						
						}
					};
		
		Button addfroum = (Button) view.findViewById(R.id.froum_add);
		addfroum.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), AddfroumActivity.class);
				startActivityForResult(intent, 0x411);

			}
		});

		// ListView listView =(ListView) view.findViewById(android.R.id.list);
		// listView.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		load();
	}
	
	
	
	@Override
	public void onListItemClick(ListView l, View v, final int position, long id) {
		// TODO Auto-generated method stub

		super.onListItemClick(l, v, position, id);
		TextView textView = (TextView) v.findViewById(R.id.froum_detail);
		detail = textView.getText().toString();
		// Toast.makeText(getActivity(), detail, Toast.LENGTH_SHORT).show();
		BmobQuery<MessageFroum> mess = new BmobQuery<MessageFroum>();
		mess.addWhereEqualTo("isHeading", "是");
		mess.order("-updatedAt");
		mess.findObjects(new FindListener<MessageFroum>() {
			
			@Override
			public void done(List<MessageFroum> arg0, BmobException arg1) {
				// TODO Auto-generated method stub
				if(arg1==null){
					if(arg0!=null){
						Log.i("查询话题成功","共"+arg0.size()+"条记录,点击了第"+position+"条，"+arg0.get(position).getMFcontent()+","+arg0.get(position).getObjectId());
						Intent intent = new Intent(getActivity(), FroumDetailActivity.class);
						intent.putExtra("detail", detail);
						intent.putExtra("MFid", arg0.get(position).getObjectId());
						startActivityForResult(intent, 0x412);
					}
				}else{
					Log.i("查询话题失败",arg1.getMessage()+","+arg1.getErrorCode());
				}
			}
		});
		
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		load();
	}
	
	public void load(){
		// 从数据库取出论坛数据
				list = new ArrayList<Map<String, Object>>();
				BmobQuery<MessageFroum> mf = new BmobQuery<MessageFroum>();
				mf.addWhereEqualTo("isHeading", "是");
				mf.include("Uid");
				mf.order("-updatedAt");
				mf.findObjects(new FindListener<MessageFroum>() {
					
					@Override
					public void done(List<MessageFroum> arg0, BmobException arg1) {
						if (arg1 == null) {
							if (arg0 != null) {
								Log.i("查询消息成功", "共有" + arg0.size()+"条消息"+HomeActivity.user.getUname());
								
								for (MessageFroum MF : arg0) {
									// 查询论坛信息
									Map<String, Object> map = new HashMap<String, Object>(); 
									map.put("userimages", MF.getUid().getUimage());
									map.put("name", MF.getUid().getUname());
									map.put("detail", MF.getMFcontent());
									map.put("time", MF.getCreatedAt());
									list.add(map);
								}	
								SimpleAdapter froumadapter = new SimpleAdapter(getActivity(), list, R.layout.forum_itemlist,
										new String[] { "userimages", "name", "detail", "time" },
										new int[] { R.id.userimage_froum, R.id.username_froum, R.id.froum_detail,
												R.id.time_froum });
								froumadapter.setViewBinder(new ViewBinder(){
									public boolean setViewValue(View view,Object data,
											String textRepresentation){
										if (view instanceof ImageView||data instanceof Drawable){
											ImageView iv=(ImageView)view;
											iv.setBackgroundResource(  ((Number)data).intValue());
											return true;
										} else{
											return false;
										}
									}
								});
								setListAdapter(froumadapter);
								
							}
						} else {
							Log.i("查询内容失败", arg1.getMessage() + "," + arg1.getErrorCode());
						}
						
					}
				});
	}
	
}

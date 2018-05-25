package com.example.gsjl.myview;


import com.example.gsjl.activity.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年4月28日下午1:22:37
 *
 */
public class EmailAutoCompleteTextView extends AutoCompleteTextView {
	private static final String TAG ="EmailAutoCompleteTextView";
	private String[] emailfixs=new String[]{"@qq.com","@163.com","@sina.com","@yahoo.cn","@foxmail.com","@139.com"};
	
	public  EmailAutoCompleteTextView(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		init(context);
	}
	
	public  EmailAutoCompleteTextView(Context context,AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context,attrs);
		init(context);
	}
	
	public  EmailAutoCompleteTextView(Context context,AttributeSet attrs,int defStyle) {
		// TODO Auto-generated constructor stub
		super(context,attrs,defStyle);
		init(context);
	}
	
	private void setAdapterString(String[] es) {
		// TODO Auto-generated method stub
		if(es!=null&&es.length>0)
			this.emailfixs=es;
	}
	
	private void init(final Context context) {
		// TODO Auto-generated method stub
		this.setAdapter(new EmailAutoCompleteAdapter(context,R.layout.auto_complete ,emailfixs));
		this.setThreshold(4);
		this.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					String text=EmailAutoCompleteTextView.this.getText().toString();
					if(!"".equals(text))
						performFiltering(text, 0);
				}else{
					EmailAutoCompleteTextView ev=(EmailAutoCompleteTextView) v;
					String text =ev.getText().toString();
					if(text!=null&&!text.matches("^[a-zA-Z0-9_]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$")){
						Toast toast=Toast.makeText(context, "邮箱地址格式不正确", Toast.LENGTH_SHORT);
						toast.show();
					}
					}
				}
		});
	}
	
	@Override
	protected void replaceText(CharSequence text) {
		// TODO Auto-generated method stub
		String t=this.getText().toString();
		int index=t.indexOf("@");
		if(index!=-1){
			t=t.substring(0,index);
		}
		super.replaceText(t+""+text);
	}

	@Override
	protected void performFiltering(CharSequence text, int keyCode) {
		// TODO Auto-generated method stub
		String t=text.toString();
		int index=t.indexOf("@");
		if(index==-1){
			if(t.matches("^[a-zA-Z0-9_]+$")){
				super.performFiltering("@", keyCode);
			}else{
				this.dismissDropDown();
			}
		}else{
			super.performFiltering(t.substring(index), keyCode);
		}
	}
	
	public class EmailAutoCompleteAdapter extends ArrayAdapter<String>{
		public EmailAutoCompleteAdapter(Context context,int textViewResourceID,String[] emString){
			super(context,textViewResourceID,emString);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v=convertView;
			if(v==null){
				v=LayoutInflater.from(getContext()).inflate(R.layout.auto_complete ,null);
			}
			TextView tv=(TextView)v.findViewById(R.id.tv);
			String t=EmailAutoCompleteTextView.this.getText().toString();
			int index =t.indexOf("@");
			if(index!=-1){
				t=t.substring(0,index);
				}
			tv.setText(t+""+getItem(position));
			return v;
				
			}
	}
}

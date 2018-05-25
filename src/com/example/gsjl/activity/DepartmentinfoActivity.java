package com.example.gsjl.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.gsjl.model.Department;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author hugeng E-mail:958996499@qq.com
 * @version:2018年5月1日下午8:07:32
 *
 */
public class DepartmentinfoActivity extends Activity {
	private List<Map<String, Object>> item;
	private ListView department;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.departmentinfo);

		Button deback = (Button) findViewById(R.id.department_back);
		deback.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});

		Button adddepartment = (Button) findViewById(R.id.addpartment);
		adddepartment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DepartmentinfoActivity.this, DepartmentAddActivity.class);
				startActivity(intent);
				finish();

			}
		});

		department = (ListView) findViewById(R.id.list_department);
		item = new ArrayList<Map<String, Object>>();
		// 从数据库取出部门信息数据
		BmobQuery<Department> depart = new BmobQuery<Department>();
		depart.addQueryKeys("Dname,Ddetail");
		depart.findObjects(new FindListener<Department>() {
			@Override
			public void done(List<Department> arg0, BmobException arg1) {
				if (arg1 == null) {
					if (arg0 != null) {
						Log.i("查询成功", "共查询到" + arg0.size() + "条信息");
						for (Department de : arg0) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("title", de.getDname());
							map.put("detail", de.getDdetail());
							Log.i("增加信息", de.getDname() + "..." + de.getDdetail());
							item.add(map);
							Log.i("", "" + item.size());
						}
						SimpleAdapter adapter=new SimpleAdapter(DepartmentinfoActivity.this, item, R.layout.department_itemlist, 
								new String[] { "title", "detail" }, new int[] { R.id.department_name, R.id.department_detail });
						department.setAdapter(adapter);

					}
				} else {
					Log.i("查询失败", "查询部门信息失败" + arg1.getErrorCode() + arg1.getMessage());
				}
			}
		});
		
	}
}

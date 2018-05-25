package com.example.gsjl.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
*@author hugeng E-mail:958996499@qq.com
*@version:2018年5月1日下午8:31:08
*
*/
public class DepartmentAddActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.departmentadd);
		
		Button adddepartment_back =(Button) findViewById(R.id.adddepartment_back);
		adddepartment_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				AlertDialog alert = new AlertDialog.Builder(DepartmentAddActivity.this).create();	
				alert.setMessage("放弃增加部门吗？");	
				alert.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						

					}
				});
				alert.setButton(DialogInterface.BUTTON_POSITIVE,"确认", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();

					}
				});
				alert.show(); 
				
			}
		});
		
		Button addpartmentyes =(Button) findViewById(R.id.addpartmentyes);
		Button department_add =(Button) findViewById(R.id.department_add);
		addpartmentyes.setOnClickListener(tab);
		department_add.setOnClickListener(tab);
	}
	
	View.OnClickListener tab =new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// 将部门信息存入数据库
			EditText title =(EditText) findViewById(R.id.department_name);
			EditText detail =(EditText) findViewById(R.id.department_detail);
			String titles=title.getText().toString();
			String details=detail.getText().toString();
			if("".equals(titles)||"".equals(details)){
				Toast.makeText(DepartmentAddActivity.this, "部门名称和简介不能为空", Toast.LENGTH_SHORT).show();
			}else{
				title.setText("");
				detail.setText("");
				Toast.makeText(DepartmentAddActivity.this, "增加成功", Toast.LENGTH_SHORT).show();
				
				finish();
			}
		}
	};
	
}

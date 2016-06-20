package com.hawk.ui.activity.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hawk.ui.activity.MainActivity;
import com.hawk.ui.activity.R;
import com.hawk.ui.activity.basic.SplashActivity;
import com.hawk.life.ui.widget.SudokuView;
import com.hawk.util.PreferenceUtil;
import com.hawk.util.UIHelper;

//验证收拾密码的界面
public class SudokuActivity extends Activity {
	private SudokuView sudoku = null;    //手势密码的视图
	private TextView hintView = null;     //输入提示
	private TextView forgetView = null;      //忘记密码链接

	private String input = "";              //输入的字符串
	private int time = 5 ;                 //默认可以输入密码的次数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sudoku);
		
		sudoku = (SudokuView)this.findViewById(R.id.sudoku);
		forgetView = (TextView)this.findViewById(R.id.forget_password);

		//为手势视图添加触摸监听事件
		sudoku.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				boolean res = v.onTouchEvent(event);
				switch(event.getAction()){
				case MotionEvent.ACTION_UP:            //手指离开界面事件
					input = sudoku.getText();
					String password = PreferenceUtil.get_sudoku_passwd(getApplicationContext());
					
					if( input.equals(password) ){           //如果相同
						UIHelper.showToast(SudokuActivity.this, "输入成功");
						
		         	    Intent intent =new Intent();
		                intent.setClass(getApplicationContext(), MainActivity.class);
		                startActivity(intent);
						SudokuActivity.this.finish();
					}
					else{             //如果不同
						time--;
						if(time == 0){
							Dialog dialog = new AlertDialog.Builder(SudokuActivity.this)
							.setCancelable(false)
							.setTitle("您已经解锁登录失败5次，请重新登录！")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									startActivity(new Intent(SudokuActivity.this,SplashActivity.class));
									SudokuActivity.this.finish();
									dialog.dismiss();
								}
							}).create();
							dialog.show();
							break;
						}
						hintView.setText("密码错误，还可以输入"+time+"次");
						Toast.makeText(SudokuActivity.this, "输入错误,请重输！", Toast.LENGTH_SHORT).show();
						sudoku.finishDraw();
					}
					break;
				}
				
				return res;
			}
		});
		forgetView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showConfirmDialog();
			}
		});

	}
	//忘记手势密码时触发的事件
	private void showConfirmDialog(){

		Dialog dialog =new AlertDialog.Builder(this)
		.setCancelable(false)
		.setTitle("忘记手势密码，需重新登录")
		.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
			 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), SplashActivity.class));
				SudokuActivity.this.finish();
			}
		})
		.setNegativeButton("取消", null).create();
		dialog.show();
	}
}

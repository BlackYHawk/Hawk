package com.hawk.ui.activity.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hawk.ui.activity.R;
import com.hawk.ui.widget.SudokuView;
import com.hawk.util.PreferenceUtil;

//手势密码修改界面
public class SudokuSettingActivity extends Activity {

	private SudokuView sudoku = null;                          //手势视图
	private TextView prompt = null;                            //提示
	private int log = 0;        //密码标志，0输入第一次，1输入第二次
	private String password = "";
	private String password_again = "";
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_sudoku_setting);

		sudoku = (SudokuView)this.findViewById(R.id.setting_sudoku);
		prompt = (TextView)this.findViewById(R.id.title_textView);
		
		//为手势视图添加触摸事件监听
		sudoku.setOnTouchListener(new OnTouchListener(){
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				boolean res = v.onTouchEvent(event);
				switch(event.getAction()){
				case MotionEvent.ACTION_UP:             //当手指离开界面时
					String input = sudoku.getText();
					if(input == ""){
						Toast.makeText(SudokuSettingActivity.this, "输入为空，请重输", Toast.LENGTH_SHORT).show();
						break;
					}
					if(log == 0){
						log++;
						password = input;
						prompt.setText("请再次输入");
						Toast.makeText(SudokuSettingActivity.this, "请再次输入", Toast.LENGTH_SHORT).show();
						sudoku.finishDraw();
					}
					else if(log == 1){
						password_again = input;
						if(password.equalsIgnoreCase(password_again)){
							PreferenceUtil.set_sudoku_passwd(getApplicationContext(), password);
							Toast.makeText(SudokuSettingActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
							SudokuSettingActivity.this.finish();
						}
						else{
							log = 0;
							prompt.setText("请输入密码");
							sudoku.finishDraw();
							Toast.makeText(SudokuSettingActivity.this, "两次输入不相同，请重新输入", Toast.LENGTH_SHORT).show();
						}
					}
					break;
				}
				
				return res;
			}
		});
	}

	//监听返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_BACK){          //若没输入新密码就按返回键时，设置手势密码未启动
			boolean sudoku = PreferenceUtil.get_sudoku_on(getApplicationContext());
			String password = PreferenceUtil.get_sudoku_passwd(getApplicationContext());
			
			if(sudoku && password == null ){
				PreferenceUtil.set_sudoku_on(getApplicationContext(), false);
			}
		}
		
		return super.onKeyDown(keyCode, event);
	}

}

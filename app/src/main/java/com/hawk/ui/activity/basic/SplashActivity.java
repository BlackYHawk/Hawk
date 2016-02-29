package com.hawk.ui.activity.basic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hawk.base.GlobalContext;
import com.hawk.ui.activity.MainActivity;
import com.hawk.ui.activity.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 欢迎页
 */
public class SplashActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ui_splash);

        if (true) {
            delayToMain();
        }
        else {
            toAccount();
        }

	}

    private void check() {

    }

    private void delayToMain() {
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                toMain();
            }

        }, 500);
    }

    private void toMain() {
        Intent intent = new Intent(GlobalContext.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        GlobalContext.getInstance().startActivity(intent);
        finish();
    }

    private void toAccount() {

    }

}

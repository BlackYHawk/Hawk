package com.hawk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.hawk.activity.R;
import com.hawk.application.AppContext;

public class DrawerMenu extends Fragment {

    private Context context;
	private View share;
	private View chat;
	private View photo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

        context = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_drawer_menu, container, false);
		share = view.findViewById(R.id.func_share);
		share.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				func_share();
			}
			
		});
		
		chat = view.findViewById(R.id.func_chat);
		chat.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				func_chat();
			}
			
		});
		
		photo = view.findViewById(R.id.func_photo);
		photo.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				func_photo();
			}
			
		});

		return view;
	}
	
	private void func_share()
	{

	}
	
	private void func_chat()
	{

	}
	
	private void func_photo()
	{

	}

    @Override
    public void onDestroy() {
        super.onDestroy();

        AppContext.getRefWatcher(context).watch(this);
    }
}

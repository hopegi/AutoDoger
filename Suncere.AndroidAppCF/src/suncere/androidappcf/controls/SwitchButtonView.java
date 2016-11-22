package suncere.androidappcf.controls;

import suncere.androidappcf.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SwitchButtonView extends LinearLayout{

	Button btnOpen,btnClose;
	boolean switchState;
	
	
	public SwitchButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LinearLayout.inflate(context, R.layout.switch_button_view, this);
		InitialView();
	}

	public SwitchButtonView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LinearLayout.inflate(context, R.layout.switch_button_view, this);
		InitialView();
	}

	private void InitialView()
	{
		switchState=false;
		btnOpen=(Button)findViewById(R.id.sbvBtnOpen);
		btnOpen.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View sender) {
				// TODO Auto-generated method stub
				On_sbvBtnOpen_Click( sender);
			}});
		btnClose=(Button)findViewById(R.id.sbvBtnClose);
		btnClose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View sender) {
				// TODO Auto-generated method stub
				On_sbvBtnClose_Click( sender);
			}});
		
		this.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SetSwitchState(!GetSwitchState());
			}});
	}
	
	public void On_sbvBtnClose_Click(View sender)
	{
		btnOpen.setVisibility(View.INVISIBLE);
		btnClose.setVisibility(View.VISIBLE);
		switchState=false;
	}
	
	public void On_sbvBtnOpen_Click(View sender)
	{
		btnOpen.setVisibility(View.VISIBLE);
		btnClose.setVisibility(View.INVISIBLE);
		switchState=true;	
	}
	
	public boolean GetSwitchState()
	{
		return switchState;
	}
	
	public void SetSwitchState(boolean value)
	{
		//Log.d("TAG",	 "TAG " +value);
		if(value)
			this.On_sbvBtnOpen_Click(btnOpen);
		else
			this.On_sbvBtnClose_Click(btnClose);
		
	}
}

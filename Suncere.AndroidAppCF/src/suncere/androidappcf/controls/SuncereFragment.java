package suncere.androidappcf.controls;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SuncereFragment extends Fragment{

	protected View  contentView;

	int layout;
	
	protected void setContentView(int id)
	{
		this.layout=id;
	}
	
	protected int getContentView()
	{
		return layout;
	}
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
	{
		contentView=inflater.inflate(layout, container,false);
		return contentView;
	}
	
	protected View findViewById(int id)
	{
		return contentView.findViewById(id);
	}
	
	public void overridePendingTransition(int enterAnim,int exitAnim)
	{
		this.getActivity().overridePendingTransition(enterAnim, exitAnim);
	}
	
}

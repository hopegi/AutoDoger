package suncere.androidappcf.controls;

import java.util.ArrayList;
import java.util.List;

import suncere.androidappcf.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class LanuchView extends RelativeLayout
{
	ViewPager vp;
	MyUIPagerControlView pager;
//	View finishView;
	Class<?> desActivity;

	public LanuchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		IniViews();
	}
	
	public LanuchView(Context context) {
		super(context);
		 IniViews();
	}
	
	private void IniViews()
	{
		this.inflate(this.getContext(), R.layout.lanuch_view, null);
		vp=(ViewPager) this.findViewById(R.id.lanuchVp);
		pager=(MyUIPagerControlView) this.findViewById(R.id.lanuchPager);
	}
	
	public void BindPageContent(int... pageContents)
	{
		this.pager.setCount(pageContents.length);
		this.pager.setSelectedIndex(0);
		
		List<View> viewLst=new ArrayList<View>();
		
		View vpItem=null;
		for(int pageContent : pageContents)
		{
			vpItem=this.inflate(getContext(), pageContent, null);
			viewLst.add(vpItem);
		}
		
		PagerAdapter adapter=new MyViewPagerAdapter(viewLst);
		try
		{
			vp.setAdapter(adapter);
		}
		catch(Exception ex)
		{
			
		}
		vp.setOnPageChangeListener(On_vp_Change);
	}
	
	OnPageChangeListener On_vp_Change=new OnPageChangeListener()
	{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int arg0) {
			pager.setSelectedIndex(arg0);
			
		}
		
	};

	private class MyViewPagerAdapter extends PagerAdapter {

		List<View> viewLst;
		public MyViewPagerAdapter(List<View> viewLst)
		{
			this.viewLst=viewLst;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.viewLst.size();
		}

		@Override
		public boolean isViewFromObject(View view1, Object view2) {
			// TODO Auto-generated method stub
			return view1==view2;
			
		}
		
		public Object instantiateItem(ViewGroup container,int position)
		{
			container.addView(viewLst.get(position), 0);
			return viewLst.get(position);
		}

		public void destoryItem(ViewGroup container,int postion,Object object)
		{
			container.removeView(viewLst.get(postion));
		}
		
		public void destroyItem(View container,int position,Object object)
		{
			((ViewGroup)container).removeView(viewLst.get(position));
		}

	}
	
	public void setFinishView(View finishView) {
		finishView.setClickable(true);
		finishView.setOnClickListener(On_finish_Click);
	}

	public void setDesActivity(Class<?> desActivity) {
		this.desActivity = desActivity;
	}

	OnClickListener On_finish_Click=new OnClickListener(){

		@Override
		public void onClick(View sender) {
			Context context=LanuchView.this.getContext();
			if(desActivity!=null)
			{
				Intent intent=new Intent(context,desActivity);
				context.startActivity(intent);
			}
			if(context instanceof Activity)
				((Activity)context).finish();
		}};
	
	//ÍË³ö°´Å¥
	//Ìø×ªActivity
}

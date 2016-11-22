package suncere.androidappcf.controls;

import suncere.androidappcf.R;
import suncere.androidappcf.tools.ScreenSuitableTool;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;

public abstract class SuncereAppDesActivity extends SuncereFragmentActivity {

	protected Button startButton;
	protected ViewPager viewPager;
	protected MyUIPagerControlView pagerControlView;
	boolean isFirstTime;
	Class<?> mainActivityClass;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle extras=this.getIntent().getExtras();
		if(extras!=null&&extras.containsKey("IsFirstTime"))
		{
			isFirstTime=extras.getBoolean("IsFirstTime");
			mainActivityClass=(Class<?>) extras.getSerializable("MainActivity");
		}
		else isFirstTime=false;
		if(this.GetLayoutId()!=-1)
		{
			this.setContentView(this.GetLayoutId());
		}
		else
		{
			this.setContentView(this.CreateView());
		}
		InitView();
	}
	
 	protected boolean DisableBackButton()
	{
		return mainActivityClass!=null;
	}
	
	protected void GetMajorView()
	{
		View rootView= ((ViewGroup)this.findViewById(android.R.id.content)).getChildAt(0);
		startButton=(Button) GetViewByType(rootView,Button.class);
		viewPager=(ViewPager) GetViewByType(rootView,ViewPager.class);
		pagerControlView=(MyUIPagerControlView) GetViewByType(rootView,MyUIPagerControlView.class);
	}
	
	private void InitView()
	{
		GetMajorView();
		this.startButton.setVisibility(View.GONE);
		
		this.startButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(isFirstTime)
				{
					Intent intent=new Intent( );
//					intent.setClass(SuncereApplication.CurrentApplication().CurrentActivity(),mainActivityClass);
					intent.setClass(SuncereAppDesActivity.this, mainActivityClass);
					startActivity(intent);
				}
				finish();
			}
		});
		
		this.viewPager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int arg0) {
				pagerControlView.setSelectedIndex(arg0);
				if(arg0==ImageResIdArrary().length-1)
					startButton.setVisibility(View.VISIBLE);
				else
					startButton.setVisibility(View.GONE);
			}
		});
	}
	
	private View CreateView()
	{
		RelativeLayout rootView=new RelativeLayout(this);
		this.setContentView(rootView);
		rootView.getLayoutParams().height=ViewGroup.LayoutParams.MATCH_PARENT;
		rootView.getLayoutParams().width=ViewGroup.LayoutParams.MATCH_PARENT;
		
		viewPager=new ViewPager(this);
		int[] imgResArry=this.ImageResIdArrary();
		View[] viewList=new View[imgResArry.length];
		for(int i=0;i<viewList.length;i++)
			viewList[i]=CreatePagerViewItem(imgResArry[i]);
		PagerAdapter adapter=new MyViewPageAdapter(viewList);
		viewPager.setAdapter(adapter);
		rootView.addView(viewPager);
		viewPager.getLayoutParams().height=ViewGroup.LayoutParams.MATCH_PARENT;
		viewPager.getLayoutParams().width=ViewGroup.LayoutParams.MATCH_PARENT;
		
		MyUIPagerControlView pagerControl=new MyUIPagerControlView(this);
		pagerControl.setCount(viewList.length);
		rootView.addView(pagerControl);
		RelativeLayout.LayoutParams pcLayoutParam =(LayoutParams) pagerControl.getLayoutParams();
		pcLayoutParam.height=RelativeLayout.LayoutParams.WRAP_CONTENT;
		pcLayoutParam.width=RelativeLayout.LayoutParams.WRAP_CONTENT;
		pcLayoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
		pcLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		pcLayoutParam.bottomMargin= (int) ScreenSuitableTool.ConvertSuitableDpi(20) ;
		
		Button btn=new Button(this);
		btn.setText("开始体验 >");
		btn.setTextColor(Color.WHITE);
		btn.setBackgroundColor( this.getResources().getColor( R.color.aqi_1g ) );
//		btn.setTextSize(ScreenSuitableTool.ConvertSuitableDpi(18));
		btn.setTextSize(18);
		rootView.addView(btn);
		RelativeLayout.LayoutParams btnLayoutParam=(LayoutParams) btn.getLayoutParams();
		btnLayoutParam.height=RelativeLayout.LayoutParams.WRAP_CONTENT;
		btnLayoutParam.width=RelativeLayout.LayoutParams.WRAP_CONTENT;
		btnLayoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
		btnLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		btnLayoutParam.bottomMargin= (int) ScreenSuitableTool.ConvertSuitableDpi(50) ;
		
		return rootView;
	}
	
	private View CreatePagerViewItem(int imgResId)
	{
		ImageView pviv=new ImageView(this);
		pviv.setImageResource(imgResId);
		pviv.setScaleType(ScaleType.FIT_XY);
		return pviv;
	}
	
	protected View GetViewByType(View parent,Class<?> viewType)
	{
		View result=null;
		if(parent==null)return null;
		if(parent.getClass().equals( viewType))return parent;
		if(parent instanceof ViewGroup)
		{
			ViewGroup vg=(ViewGroup)parent;
			
			for(int i=0;i<vg.getChildCount();i++)
			{
				result=GetViewByType(vg.getChildAt(i),viewType);
				if(result!=null)
					break;
			}
		}
		return result;
	}
	
	@Override
	public void RefreshViewData() {
		
	}

	public boolean IsFullScreen()
	{
		return true;
	}

	/*
	 * 新的布局文件
	 */
	public int GetLayoutId()
	{
		return -1;
	}
	
	protected abstract int[] ImageResIdArrary();
	
	
	
	public class MyViewPageAdapter extends PagerAdapter {

		View[] viewLst;
		public MyViewPageAdapter(View[] viewLst)
		{
			this.viewLst=viewLst;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.viewLst.length;
		}

		@Override
		public boolean isViewFromObject(View view1, Object view2) {
			// TODO Auto-generated method stub
			return view1==view2;
			
		}
		
		public Object instantiateItem(ViewGroup container,int position)
		{
			container.addView(viewLst[position], 0);
			return viewLst[position];
		}

		public void destoryItem(ViewGroup container,int postion,Object object)
		{
			container.removeView(viewLst[postion]);
		}
		
		public void destroyItem(View container,int position,Object object)
		{
			((ViewGroup)container).removeView(viewLst[position]);
		}

	}
}

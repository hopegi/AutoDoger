package suncere.androidappcf.controls;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import suncere.androidappcf.R;
import suncere.androidappcf.tools.ScreenSuitableTool;
import suncere.androidappcf.tools.ViewIdGenerator;
import android.R.bool;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TabView extends LinearLayout {

	private int mTabBarColor;
	private int mSelectColor;
	private int mUnselectColor;
	private String mSelectIcons;
	private String mUnselectIcons;
	private String mTabFragments;
	private int mDefaultSelectIndex;
	private String mTabTitles;
	
	private int[] mSelectIconArray;
	private int[] mUnSelectIconArray;
	private Class<?> []mTabFragmentClassArray;
	private String[] mTabTitleArray;
	private List<SuncereFragment> fragLst;

	private boolean isInit;
	FrameLayout tabPageContainer;
	LinearLayout tabContainer;
	
	List<View> tabPageLst;
	List<View> tabLst;
	int preSelectIndex=-1;
	private OnTabSelectedChangingListener onTabSelectedChangingListener;

/*
    <suncere.androidappcf.controls.TabView 
	    android:layout_width="fill_parent"
	    android:layout_height="fill_parent"
	    tab:selectColor="#fff"
	    tab:selectIcons="forecast_w,live_w,sort_w,setting_w"
	    tab:unselectIcons="forecast,live,sort,setting"
	    tab:tabFragments=".ForecastFragment,.LiveFragment,.SortFragment,.SettingFragment"
	    tab:tabTitles="预报,实况,排名,设置"/>	
 */
	
	public TabView(Context context) {
		this(context,null);
		
	}
	
	public TabView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public TabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		fragLst=new ArrayList<SuncereFragment>();
		tabPageLst=new ArrayList<View>();
		tabLst=new ArrayList<View>();
		isInit=false;
		
		TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.tabview);
		
		mDefaultSelectIndex=ta.getInt(R.styleable.tabview_defaultSelectIndex,0);
		mSelectColor=ta.getColor(R.styleable.tabview_selectColor, Color.BLUE);
		mSelectIcons=ta.getString(R.styleable.tabview_selectIcons);
		mTabFragments=ta.getString(R.styleable.tabview_tabFragments);
		mTabTitles=ta.getString(R.styleable.tabview_tabTitles);
		mUnselectColor=ta.getColor(R.styleable.tabview_unselectColor, Color.GRAY);
		mUnselectIcons=ta.getString(R.styleable.tabview_unselectIcons);
		mTabBarColor=ta.getColor(R.styleable.tabview_tabBarColor, Color.BLACK);
		
		ta.recycle();
		
		mTabTitleArray=mTabTitles.split(",",-1);
		mUnSelectIconArray=this.GetDrawablesIdByResName(mUnselectIcons.split(","));
		mSelectIconArray=this.GetDrawablesIdByResName(mSelectIcons.split(","));
		String[] fragName=mTabFragments.split(",");
		mTabFragmentClassArray =new Class<?> [fragName.length ];
		for(int i=0;i<fragName.length;i++)
		{
			try {
				if(fragName[i].startsWith("."))
						mTabFragmentClassArray[i]=Class.forName( SuncereApplication.CurrentApplication().getPackageName()+ fragName[i]);
				else
					mTabFragmentClassArray[i]=Class.forName(fragName[i]);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		tabPageContainer=new FrameLayout(getContext());
		this.addView(tabPageContainer);
		tabContainer=new LinearLayout(getContext());
		this.addView(tabContainer);
		
	}

	public int[] getmSelectIconArray() {
		return mSelectIconArray;
	}

	public void setmSelectIconArray(int[] mSelectIconArray) {
		this.mSelectIconArray = mSelectIconArray;
	}

	public int[] getmUnSelectIconArray() {
		return mUnSelectIconArray;
	}

	public void setmUnSelectIconArray(int[] mUnSelectIconArray) {
		this.mUnSelectIconArray = mUnSelectIconArray;
	}

	public Class<?>[] getmTabFragmentClassArray() {
		return mTabFragmentClassArray;
	}

	public void setmTabFragmentClassArray(Class<?>[] mTabFragmentClassArray) {
		this.mTabFragmentClassArray = mTabFragmentClassArray;
	}

	public String[] getmTabTitleArray() {
		return mTabTitleArray;
	}

	public void setmTabTitleArray(String[] mTabTitleArray) {
		this.mTabTitleArray = mTabTitleArray;
	}

	private int[] GetDrawablesIdByResName(String[] nameArr)
	{
		int[] result=new int[nameArr.length];
		Class<?> RType=null;
		try {
			RType = Class.forName(getContext().getPackageName()+".R");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Class<?>[] innerClassArr=RType.getDeclaredClasses();
		Class<?>drawableClass=null;
		for(int i=0;i<innerClassArr.length;i++)
		{
			if(innerClassArr[i].getSimpleName().equals("drawable"))
			{
				drawableClass=innerClassArr[i];
				break;
			}
		}
		
		Field resField;
		
		for(int i=0;i<nameArr.length;i++)
		{
			try {
				resField=drawableClass.getDeclaredField(nameArr[i]);
				result[i]=resField.getInt(null);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public void setOnTabSelectedChangingListener(
			OnTabSelectedChangingListener onTabSelectedChangingListener) {
		this.onTabSelectedChangingListener = onTabSelectedChangingListener;
	}
	
	public List<SuncereFragment> GetFragmentList()
	{
		List<SuncereFragment> result=new ArrayList<SuncereFragment>();
		
		result.addAll(fragLst);
		
		return result;
	}
	
	public int GetSelectedTabIndex()
	{
		return this.preSelectIndex;
	}
	
	public void ChangeTab(int index)
	{
		if(index<0||index>tabLst.size())
			return;
		View sender=tabLst.get(index);
		this.On_TablClick_Listner.onClick(sender);
	}
	
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		this.setOrientation(LinearLayout.VERTICAL);
		if(changed&&!isInit)
		{
			LinearLayout.LayoutParams tabPageContLayoutParams=(LayoutParams) tabPageContainer.getLayoutParams();
			tabPageContLayoutParams.height=LinearLayout.LayoutParams.MATCH_PARENT;
			tabPageContLayoutParams.width=LinearLayout.LayoutParams.MATCH_PARENT;
			tabPageContLayoutParams.weight=1;
			
			for (Class<?> fragClass : mTabFragmentClassArray) {
				this.AddTabFrag(fragClass);
			}
			
			LinearLayout.LayoutParams tabContLayoutParams=(LayoutParams) tabContainer.getLayoutParams();
			tabContLayoutParams.height=LinearLayout.LayoutParams.WRAP_CONTENT;
			tabContLayoutParams.width=LinearLayout.LayoutParams.MATCH_PARENT;
			tabContainer.setBackgroundColor(mTabBarColor);
			
			for(int i=0;i<mSelectIconArray.length;i++)
				AddTab(i);
			
			this.setPadding(0, 0, 0, 0);
			if(mDefaultSelectIndex!=-1)
				On_TablClick_Listner.onClick(	 tabLst.get(mDefaultSelectIndex));
		}
		isInit=true;
	}
	
	private void AddTabFrag( Class<?> fragClass)
	{
		FrameLayout layout=new FrameLayout(this.getContext());
		layout.setId(ViewIdGenerator.generateViewId());
//		layout.setId(View.generateViewId());

		tabPageContainer.addView(layout);
		layout.getLayoutParams().height=ViewGroup.LayoutParams.MATCH_PARENT;
		layout.getLayoutParams().width=ViewGroup.LayoutParams.MATCH_PARENT;
		FragmentManager fm=((SuncereFragmentActivity) this.getContext()).getFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();
		try {
			SuncereFragment frag=(SuncereFragment)fragClass.newInstance() ;
			tran.add( layout.getId() ,  frag);
			fragLst.add(frag);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		tran.addToBackStack(null);
		tran.commit();

		layout.setVisibility(View.GONE);
		tabPageLst.add(layout);
		
	}
	
	private void AddTab(int index)
	{
		LinearLayout tab=new LinearLayout(this.getContext());
		tab.setOrientation(VERTICAL);
		tabContainer.addView(tab);
		LinearLayout.LayoutParams params=(LayoutParams) tab.getLayoutParams();
		params.weight=1;
		params.height=ViewGroup.LayoutParams.WRAP_CONTENT;
		params.width=ViewGroup.LayoutParams.MATCH_PARENT;
		int paddingBotton=this.getPaddingBottom()==-1?  (int) ScreenSuitableTool.ConvertSuitableDpi(5):this.getPaddingBottom();
		int paddingTop=this.getPaddingTop()==-1? (int) ScreenSuitableTool.ConvertSuitableDpi(5):this.getPaddingTop();
		tab.setPadding(0, paddingTop, 0, paddingBotton);
		tab.setClickable(true);
		
		ImageView img=new ImageView(getContext());
		tab.addView(img);
		params=(LayoutParams) img.getLayoutParams();
		params.width=ViewGroup.LayoutParams.WRAP_CONTENT;
		params.height=ViewGroup.LayoutParams.WRAP_CONTENT;
		params.gravity=Gravity.CENTER;
		params.topMargin=paddingTop;
		img.setImageResource( mUnSelectIconArray[index] );
		
		TextView tv=new TextView(getContext());
		tab.addView(tv);
		params=(LayoutParams) tv.getLayoutParams();
		params.width=ViewGroup.LayoutParams.WRAP_CONTENT;
		params.height=ViewGroup.LayoutParams.WRAP_CONTENT;
		params.gravity=Gravity.CENTER;
		params.bottomMargin=paddingBotton;
		tv.setTextColor( mUnselectColor );
		tv.setText(  mTabTitleArray[index]  );
		
		tab.setOnClickListener(On_TablClick_Listner);
		
		tabLst.add(tab);
	}
	
	
	OnClickListener On_TablClick_Listner =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int index=tabLst.indexOf(v);
			if(preSelectIndex==index)return;
			if(onTabSelectedChangingListener!=null && !onTabSelectedChangingListener.OnTabSelectedChanging(v, index))return;
			
			if(preSelectIndex!=-1)
			{
				tabPageLst.get(preSelectIndex).setVisibility(View.GONE);
				ViewGroup tab= (ViewGroup) tabLst.get(preSelectIndex);
				((ImageView)tab.getChildAt(0)).setImageResource(mUnSelectIconArray[preSelectIndex]);
				((TextView)tab.getChildAt(1) ).setTextColor(mUnselectColor);
			}
			
			preSelectIndex=index;
			tabPageLst.get(index).setVisibility(View.VISIBLE);
			ViewGroup currTab=(ViewGroup) v;
			((ImageView)currTab.getChildAt(0)).setImageResource(mSelectIconArray[preSelectIndex]);
			((TextView)currTab.getChildAt(1) ).setTextColor(mSelectColor);
			
		}
	};

	public interface OnTabSelectedChangingListener
	{
		boolean OnTabSelectedChanging(View sender, int index);
	}
}

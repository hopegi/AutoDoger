package suncere.androidappcf.controls;

import java.util.ArrayList;
import java.util.List;

import suncere.androidappcf.R;
import suncere.androidappcf.tools.ScreenSuitableTool;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class MyUIPagerControlView extends LinearLayout {

	private int count;//总数

	private int selectedIndex;//当前选中索引
	
	private List<View> markLst;
	
	public MyUIPagerControlView(Context context) 
	{
		super(context);
		markLst=new ArrayList<View>();
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.selectedIndex=0;
	}

	public MyUIPagerControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		markLst=new ArrayList<View>();
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.selectedIndex=0;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
		this.ManageSubView(count);
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.ChangeSelectSubView(this.selectedIndex, selectedIndex);
		this.selectedIndex = selectedIndex;
	}

	
	private void ManageSubView(int value)
	{
		if(markLst.size()==value)return;
		if(markLst.size()>value)
		{
			while(markLst.size()!=value)
			{
				this.removeViewAt(markLst.size()-1);
				markLst.remove(markLst.size()-1);
			}
		}
		else if(markLst.size()<value)
		{
			while(markLst.size()!=value)
			{
				View view=new View(this.getContext());
				view.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.uicontrol_unselect_circle));
				this.addView(view);
				LayoutParams para=(LayoutParams) view.getLayoutParams();
				para.height=(int) ScreenSuitableTool.ConvertSuitableDpi( 20);
				para.width=(int) ScreenSuitableTool.ConvertSuitableDpi(20);
				para.leftMargin=(int) ScreenSuitableTool.ConvertSuitableDpi(10);
				para.rightMargin=(int) ScreenSuitableTool.ConvertSuitableDpi(10);
				markLst.add(view);
			}
		}
		this.setSelectedIndex(0);
	}

	private void ChangeSelectSubView(int preValue, int currValue)
	{
		if(preValue>-1&&preValue<this.markLst.size())
		{
			this.markLst.get(preValue).setBackgroundDrawable(this.getResources().getDrawable(R.drawable.uicontrol_unselect_circle));
		}
		if(currValue>-1&&currValue<this.markLst.size())
		{
			this.markLst.get(currValue).setBackgroundDrawable(this.getResources().getDrawable(R.drawable.uicontrol_select_circle));
		}
	}
}

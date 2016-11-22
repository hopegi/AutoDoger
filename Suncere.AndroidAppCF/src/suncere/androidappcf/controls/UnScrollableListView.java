package suncere.androidappcf.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ListView;


public class UnScrollableListView extends ListView {

	public UnScrollableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public UnScrollableListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void onMeasure(int widthMeasureSpec,int  heightMeasureSpec)
	{
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
		                MeasureSpec.AT_MOST);  
		        super.onMeasure(widthMeasureSpec, expandSpec);  
	}
}

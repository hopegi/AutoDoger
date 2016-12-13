package suncere.androidappcf.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;



public class UnScrollableGridView extends GridView {

	public UnScrollableGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public UnScrollableGridView(Context context) {
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

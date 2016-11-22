package suncere.androidappcf.tools;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SegmentLabelTool
{
	private List<TextView> viewList;
	private int normalTextColor;
	private int normalBackground;
	private int selectedTextColor;
	private int selectedBackground;
	private Context context;
	private View contentView;
	private SegmentClick SegmentClickListener;
	
	public SegmentLabelTool(Context context)
	{
		this.context=context;
		viewList=new ArrayList<TextView>();
	}
	
	public SegmentLabelTool(View view)
	{
		this.contentView=view;
		viewList=new ArrayList<TextView>();
	}
	
	public SegmentLabelTool setNormalTextColor(int normalTextColor) {
		this.normalTextColor = normalTextColor;
		return this;
	}

	public SegmentLabelTool setNormalBackground(int normalBackground) {
		this.normalBackground = normalBackground;
		return this;
	}

	public SegmentLabelTool setSelectedTextColor(int selectedTextColor) {
		this.selectedTextColor = selectedTextColor;
		return this;
	}

	public SegmentLabelTool setSelectedBackground(int selectedBackground) {
		this.selectedBackground = selectedBackground;
		return this;
	}

	public SegmentLabelTool setViewList(TextView... textView )
	{
		for(int i=0;i<textView.length;i++)
		{
			viewList.add(textView[i]);
			textView[i].setClickable(true);
			textView[i].setOnClickListener(On_ListItem_Click);
		}
		return this;
	}
	
	public SegmentLabelTool setViewList(int... textView)
	{
		TextView view;
		for(int i=0;i<textView.length;i++)
		{
			if(this.contentView!=null)
				view=(TextView) contentView.findViewById(  textView[i]);
			else
				view= (TextView) ((Activity) context).findViewById(textView[i]);
			viewList.add(view);
			view.setClickable(true);
			view.setOnClickListener(On_ListItem_Click);
		}
		return this;
	}
	
	public SegmentLabelTool SetSegmentClick(SegmentClick listener)
	{
		this.SegmentClickListener=listener;
		return this;
	}
	
	private  OnClickListener On_ListItem_Click=new OnClickListener(){

		@Override
		public void onClick(View sender) {
			for(int i=0;i<viewList.size();i++)
			{
				if(viewList.get(i)==sender)
				{
					viewList.get(i).setTextColor(selectedTextColor);
					viewList.get(i).setBackgroundResource(selectedBackground);
				}
				else
				{

					viewList.get(i).setTextColor(normalTextColor);
					viewList.get(i).setBackgroundResource(normalBackground);
				}
			}
			SegmentClickListener.OnClick(sender,  viewList.indexOf(sender)   );
		}
	};
	

	public interface SegmentClick
	{
		void OnClick(View sender,int index);
	}
}


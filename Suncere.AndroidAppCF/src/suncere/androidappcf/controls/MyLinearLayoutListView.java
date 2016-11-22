package suncere.androidappcf.controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyLinearLayoutListView extends LinearLayout {

	List<View> items;
	IMyLinearLayoutListItem itemValueType;
	String[] DataKeys;
	int [] DataViewIds;
	int itemLayout;
	View emptyView;
	int parentHeight=LinearLayout.LayoutParams.FILL_PARENT;
	View measureView;
	int splitHeight=0;

	public MyLinearLayoutListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setOrientation(LinearLayout.VERTICAL);
	}

	public MyLinearLayoutListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setOrientation(LinearLayout.VERTICAL);
	}
	
	public int getSplitHeight() {
		return splitHeight;
	}

	public void setSplitHeight(int splitHeight) {
		this.splitHeight = splitHeight;
	}
	
	public void removeView(View view)
	{
		super.removeView(view);
		items.remove(view);
	}
	
	public void onMeasure(int widthMeasureSpec,int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		try
		{
		if(emptyView!=null)
		{
			this.parentHeight=((View)this.getParent()).getMeasuredHeight();
			LayoutParams layout=(LayoutParams) emptyView.getLayoutParams();
			layout.height=this.measureView==null?layout.FILL_PARENT:this.measureView.getMeasuredHeight();//((View)this.getParent().getParent().getParent()).getMeasuredHeight();
			emptyView.setLayoutParams(layout);
		}
		}catch(Exception ex)
		{}
	}
	
	public void SetItemViewType(IMyLinearLayoutListItem value)
	{
		this.itemValueType=value;
	}
	
	public void EmptyViewId(int layoutId,View measureView)
	{
		this.emptyView=View.inflate(getContext(), layoutId, null);
		this.measureView=measureView;
	}
	
	public void ConfigDataBind(int layoutId,String[] dataKeys,int[] dataViewIds)
	{
		this.itemLayout=layoutId;
		this.DataKeys=dataKeys;
		this.DataViewIds=dataViewIds;
	}
	
	public  void SetDataSource(List<HashMap<String,Object>> datas)
	{
		if(items==null) items=new ArrayList<View>();

		int itemsSize=items.size();
		int dataSize=datas.size();
		if(itemsSize<dataSize)
			for(int i=itemsSize;i<dataSize;i++){
				View view= View.inflate(getContext(), itemLayout, null);
				items.add(view);
				this.addView(view);
				if(this.splitHeight>0)
				{
					LayoutParams llp=(LayoutParams) view.getLayoutParams();
					llp.topMargin=this.splitHeight;
				}
			}
		else if(itemsSize>dataSize)
		{
			while(dataSize<items.size())
			{
				this.removeViewAt(0);
				items.remove(0);
				Log.d("", " MyLinearListView Count "+this.getChildCount()+"  this.item.count "+this.items.size());
			}
			
		}
		itemsSize=items.size();
		
		
		HashMap<String,Object> dataItem;
		for(int viewIndex=0;viewIndex<itemsSize;viewIndex++)
		{
			View itemView=items.get(viewIndex);
			View view;
			dataItem=datas.get(viewIndex);
			for(int i=0;i<this.DataKeys.length;i++)
			{
				view=itemView.findViewById(this.DataViewIds[i]);
				try{
					if(view.getClass()==TextView.class)
						((TextView)view).setText(dataItem.get(this.DataKeys[i])==null?"":dataItem.get(this.DataKeys[i]).toString());
				}catch(Exception ex)
				{
					Log.d("", " MyListView Bind "+ex.getMessage());
				}
			}
			this.itemValueType.AfterBind(dataItem, itemView);
		}
		
		if(datas.size()==0)
		{
			try{
				this.addView(emptyView);
				LayoutParams layout=(LayoutParams) emptyView.getLayoutParams();
				layout.height=((View)this.getParent()).getMeasuredHeight();
				layout.weight=layout.FILL_PARENT;
				emptyView.setLayoutParams(layout);
			}catch(Exception ex)
			{}
		}
		else
		{
			this.removeView(emptyView);
		}
		
		this.refreshDrawableState();
	}
	
	
	
	public interface IMyLinearLayoutListItem   {

		public IMyLinearLayoutListItem CreateInstance(Context context);
		
		public void AfterBind(HashMap<String,Object> data,View itemView);

	}
	
}

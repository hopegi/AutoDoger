package suncere.androidapp.viewautobinder;

import android.support.v4.view.PagerAdapter;
import android.view.View;

public class AutoBaseViewPagerAdapter extends PagerAdapter  {

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	///防止notifyDataSetChanged无反应
	@Override    
	public int getItemPosition(Object object) {    
	    return POSITION_NONE;    
	}  
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	
//	List<View> viewLst;
//	public MyViewPagerAdapter(List<View> viewLst)
//	{
//		this.viewLst=viewLst;
//	}
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return this.viewLst.size();
//	}
//
//	@Override
//	public boolean isViewFromObject(View view1, Object view2) {
//		// TODO Auto-generated method stub
//		return view1==view2;
//		
//	}
//	
//	public Object instantiateItem(ViewGroup container,int position)
//	{
//		container.addView(viewLst.get(position), 0);
//		return viewLst.get(position);
//	}
//
//	public void destoryItem(ViewGroup container,int postion,Object object)
//	{
//		container.removeView(viewLst.get(postion));
//	}
//	
//	public void destroyItem(View container,int position,Object object)
//	{
//		((ViewGroup)container).removeView(viewLst.get(position));
//	}
}

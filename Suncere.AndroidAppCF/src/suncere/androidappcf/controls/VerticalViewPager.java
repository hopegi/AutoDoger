package suncere.androidappcf.controls;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;
import android.widget.Scroller;

public class VerticalViewPager  extends  ScrollView {

	/**
	 * 内容总高度
	 */
	private int mContentTotalHeight=0;
	/**
	 * 控件实际高度
	 */
	private int mViewHeight=-1;
    /** 
     * 屏幕的高度 
     */  
    private int mScreenHeight;  
    /** 
     * 手指按下时的getScrollY 
     */  
    private int mScrollStart;  
    /** 
     * 手指抬起时的getScrollY 
     */  
    private int mScrollEnd;  
    /** 
     * 记录移动时的Y 
     */  
    private int mLastY;  
    /** 
     * 滚动的辅助类 
     */  
    private OverScroller _mscroller;  
    /** 
     * 是否正在滚动 
     */  
    private boolean isScrolling;  
    /** 
     * 加速度检测 
     */  
    private VelocityTracker mVelocityTracker;  
    /** 
     * 记录当前页 
     */  
    private int currentPage = 0;  
    /**
     * 上次onMeasure的时候的MeasureHeight
     */
	int preMeasureHeight=9999;
    
//    private boolean needResetPageHeight=true;
    
	private OnPageChangeListener mOnPageChangeListener;  
	

    public void setmOnPageChangeListener(OnPageChangeListener mOnPageChangeListener) {
		this.mOnPageChangeListener = mOnPageChangeListener;
	}

//	public void setNeedResetPageHeight(boolean needResetPageHeight) {
//		this.needResetPageHeight = needResetPageHeight;
//	}
	
    private OverScroller getmSroller()
    {
    	if(_mscroller!=null)return _mscroller;
    	
//    	try {
//    		ScrollView superInstance=this;
////    		ScrollView.class.getDeclaredField("mEdgeGlowTop").get(superInstance);
//    		_mscroller=new OverScroller(this.getContext());
//    		Field mScrollerField=ScrollView.class.getDeclaredField("mScroller");
////			_mscroller = (OverScroller) mScrollerField.get(superInstance);
//    		mScrollerField.set(superInstance, _mscroller);
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//			
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//			
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//			
//		}
    	if(_mscroller==null)
    		_mscroller=new OverScroller(this.getContext());
    	return _mscroller;
    }
    
    
	public VerticalViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
//		super.setOnTouchListener(on_scrollView_Touch);
		InitView();
	}

	public VerticalViewPager(Context context) {
		super(context);
//		super.setOnTouchListener(on_scrollView_Touch);
		InitView();
	}
	
	private void InitView()
	{
//		this.setOrientation(LinearLayout.VERTICAL);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);  
        DisplayMetrics outMetrics = new DisplayMetrics();  
        wm.getDefaultDisplay().getMetrics(outMetrics);  
        mScreenHeight = outMetrics.heightPixels;
        super.onAttachedToWindow();
        super.onFinishInflate();
	}
	
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  

        //V1
//        int measureHeight=this.getHeight();
        
        int measureHeight=this.getMeasuredHeight();

        int count =((ViewGroup) getChildAt(0)). getChildCount();  
//        Log.d("", "onMeasure " +this.getMeasuredHeight() );
        

        
        //V1
//        if(mViewHeight!=-1||measureHeight<=0)return;
    	
    	if(this.getMeasuredHeight()>preMeasureHeight)return;
    	preMeasureHeight=this.getMeasuredHeight();
    	mViewHeight=-1;
        
        for (int i = 0; i < count; ++i)  
        {  
            View childView =((ViewGroup) getChildAt(0)). getChildAt(i);  
            if(this.mViewHeight==-1)
            	childView.getLayoutParams().height=measureHeight;
            	mContentTotalHeight+=measureHeight;
        }  
        
        
        if(this.mViewHeight==-1){
        	this.mViewHeight=measureHeight;
        	this.mScreenHeight=this.mViewHeight;
        }
    }  
     

    
	protected void onScrollChanged(int l,int t,int oldl,int oldt)
	{
		super.onScrollChanged(l, t, oldl, oldt);
//		super.computeScroll();
	}
	
	protected boolean innernalOnTouchEvent(MotionEvent event)
	{
		boolean result=super.onTouchEvent(event);

//	String actionType="";
//	switch (event.getAction()) {
//	case MotionEvent.ACTION_DOWN:
//		actionType="ACTION_DOWN";
//		break;
//	case MotionEvent.ACTION_MOVE:
//		actionType="ACTION_MOVE";
//		break;
//	case MotionEvent.ACTION_UP:
//		actionType="ACTION_UP";
//		break;
//	default:
//		break;
//	}
	
    // 如果当前正在滚动，调用父类的onTouchEvent  
    if (isScrolling)  
        return super.onTouchEvent(event);  

    int action = event.getAction();  
    int y = (int) event.getY();  

    obtainVelocity(event);  
    switch (action)  
    {  
    case MotionEvent.ACTION_DOWN:  

        mScrollStart = getScrollY();  
        mLastY = y;  
        break;  
    case MotionEvent.ACTION_MOVE:  

        if (! getmSroller() .isFinished())
        {  
        	getmSroller().abortAnimation();  
        }  

        int dy = mLastY - y;  
        // 边界值检查  
        int scrollY = getScrollY();  
        // 已经到达顶端，下拉多少，就往上滚动多少  
        if (dy < 0 && scrollY + dy < 0)  
        {  
            dy = -scrollY;  
            getParent().requestDisallowInterceptTouchEvent(false);
        }  else {
            getParent().requestDisallowInterceptTouchEvent(true);
		}
        // 已经到达底部，上拉多少，就往下滚动多少  
//        if (dy > 0 && scrollY + dy > this.getChildAt(0). getHeight() - mScreenHeight)
        if(dy>0&&scrollY+dy>mContentTotalHeight-mScreenHeight)
        {  
//            dy =this.getChildAt(0). getHeight() - mScreenHeight - scrollY;
        	dy=mContentTotalHeight-mScreenHeight-scrollY;
        }  

        scrollBy(0, dy);  
        mLastY = y;  
        break;  
    case MotionEvent.ACTION_UP:  

        mScrollEnd = getScrollY();  

        int dScrollY = mScrollEnd - mScrollStart;  

        if (wantScrollToNext())// 往上滑动  
        {  
            if (shouldScrollToNext())  
            {  
            	getmSroller().startScroll(0, getScrollY(), 0, mScreenHeight - dScrollY);
            	
//            	this.scrollTo(0, mScreenHeight - dScrollY);

            } else  
            {  
            	getmSroller().startScroll(0, getScrollY(), 0, -dScrollY);
            	
//            	this.scrollTo(0, -dScrollY);
            }  

        }  

        if (wantScrollToPre())// 往下滑动  
        {  
            if (shouldScrollToPre())  
            {  
            	getmSroller().startScroll(0, getScrollY(), 0, -mScreenHeight - dScrollY);
            	
//            	this.scrollTo(0, -mScreenHeight - dScrollY);

            } else  
            {  
            	getmSroller().startScroll(0, getScrollY(), 0, -dScrollY);
            	
//            	this.scrollTo(0, -dScrollY);
            }  
        }  
        isScrolling = true;  
        postInvalidate();  
        recycleVelocity();  
        break;  
    }  

    return result;  
	}
	
    @Override  
    public boolean onTouchEvent(MotionEvent event)  
    {  
//    	return super.onTouchEvent(event);
    	
    	return this.innernalOnTouchEvent(event);
    }  
  
    /** 
     * 根据滚动距离判断是否能够滚动到下一页 
     *  
     * @return 
     */  
    private boolean shouldScrollToNext()  
    {  
        return mScrollEnd - mScrollStart > mScreenHeight / 2 || Math.abs(getVelocity()) > 600;  
    }  
  
    /** 
     * 根据用户滑动，判断用户的意图是否是滚动到下一页 
     *  
     * @return 
     */  
    private boolean wantScrollToNext()  
    {  
        return mScrollEnd > mScrollStart;  
    }  
  
    /** 
     * 根据滚动距离判断是否能够滚动到上一页 
     *  
     * @return 
     */  
    private boolean shouldScrollToPre()  
    {  
        return -mScrollEnd + mScrollStart > mScreenHeight / 2 || Math.abs(getVelocity()) > 600;  
    }  
  
    /** 
     * 根据用户滑动，判断用户的意图是否是滚动到上一页 
     *  
     * @return 
     */  
    private boolean wantScrollToPre()  
    {  
        return mScrollEnd < mScrollStart;  
    }  
  
    @Override  
    public void computeScroll()  
    {  
//        super.computeScroll();  
        if (getmSroller().computeScrollOffset())  
//        if(super.i)
        {  
        	scrollTo(0, getmSroller().getCurrY());  
        	
//        	this.scrollTo(0, this.getScrollY());
            postInvalidate();  
        } else  
        {  
  
            int position = getScrollY() / mScreenHeight;  
  
            Log.e("xxx", position + "," + currentPage);  
            if (position != currentPage)  
            {  
                if (mOnPageChangeListener != null)  
                {  
                    currentPage = position;  
                    mOnPageChangeListener.onPageChange(currentPage);  
                }  
            }  
  
            isScrolling = false;  
        }  
  
    }  
  
    /** 
     * 获取y方向的加速度 
     *  
     * @return 
     */  
    private int getVelocity()  
    {  
        mVelocityTracker.computeCurrentVelocity(1000);  
        return (int) mVelocityTracker.getYVelocity();  
    }  
  
    /** 
     * 释放资源 
     */  
    private void recycleVelocity()  
    {  
        if (mVelocityTracker != null)  
        {  
            mVelocityTracker.recycle();  
            mVelocityTracker = null;  
        }  
    }  
  
    /** 
     * 初始化加速度检测器 
     *  
     * @param event 
     */  
    private void obtainVelocity(MotionEvent event)  
    {  
        if (mVelocityTracker == null)  
        {  
            mVelocityTracker = VelocityTracker.obtain();  
        }  
        mVelocityTracker.addMovement(event);  
    }
		
		public interface OnPageChangeListener
		{
			void onPageChange(int pageIndex);
		}
		
//		IOnSkipFinish skipFinishEvent;
//
//		public void setSkipFinishEvent(IOnSkipFinish skipFinishEvent) {
//			this.skipFinishEvent = skipFinishEvent;
//		}
}

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
	 * �����ܸ߶�
	 */
	private int mContentTotalHeight=0;
	/**
	 * �ؼ�ʵ�ʸ߶�
	 */
	private int mViewHeight=-1;
    /** 
     * ��Ļ�ĸ߶� 
     */  
    private int mScreenHeight;  
    /** 
     * ��ָ����ʱ��getScrollY 
     */  
    private int mScrollStart;  
    /** 
     * ��ָ̧��ʱ��getScrollY 
     */  
    private int mScrollEnd;  
    /** 
     * ��¼�ƶ�ʱ��Y 
     */  
    private int mLastY;  
    /** 
     * �����ĸ����� 
     */  
    private OverScroller _mscroller;  
    /** 
     * �Ƿ����ڹ��� 
     */  
    private boolean isScrolling;  
    /** 
     * ���ٶȼ�� 
     */  
    private VelocityTracker mVelocityTracker;  
    /** 
     * ��¼��ǰҳ 
     */  
    private int currentPage = 0;  
    /**
     * �ϴ�onMeasure��ʱ���MeasureHeight
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
	
    // �����ǰ���ڹ��������ø����onTouchEvent  
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
        // �߽�ֵ���  
        int scrollY = getScrollY();  
        // �Ѿ����ﶥ�ˣ��������٣������Ϲ�������  
        if (dy < 0 && scrollY + dy < 0)  
        {  
            dy = -scrollY;  
            getParent().requestDisallowInterceptTouchEvent(false);
        }  else {
            getParent().requestDisallowInterceptTouchEvent(true);
		}
        // �Ѿ�����ײ����������٣������¹�������  
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

        if (wantScrollToNext())// ���ϻ���  
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

        if (wantScrollToPre())// ���»���  
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
     * ���ݹ��������ж��Ƿ��ܹ���������һҳ 
     *  
     * @return 
     */  
    private boolean shouldScrollToNext()  
    {  
        return mScrollEnd - mScrollStart > mScreenHeight / 2 || Math.abs(getVelocity()) > 600;  
    }  
  
    /** 
     * �����û��������ж��û�����ͼ�Ƿ��ǹ�������һҳ 
     *  
     * @return 
     */  
    private boolean wantScrollToNext()  
    {  
        return mScrollEnd > mScrollStart;  
    }  
  
    /** 
     * ���ݹ��������ж��Ƿ��ܹ���������һҳ 
     *  
     * @return 
     */  
    private boolean shouldScrollToPre()  
    {  
        return -mScrollEnd + mScrollStart > mScreenHeight / 2 || Math.abs(getVelocity()) > 600;  
    }  
  
    /** 
     * �����û��������ж��û�����ͼ�Ƿ��ǹ�������һҳ 
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
     * ��ȡy����ļ��ٶ� 
     *  
     * @return 
     */  
    private int getVelocity()  
    {  
        mVelocityTracker.computeCurrentVelocity(1000);  
        return (int) mVelocityTracker.getYVelocity();  
    }  
  
    /** 
     * �ͷ���Դ 
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
     * ��ʼ�����ٶȼ���� 
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

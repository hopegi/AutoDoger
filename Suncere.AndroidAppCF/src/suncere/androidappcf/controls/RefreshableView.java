package suncere.androidappcf.controls;

import suncere.androidappcf.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 可进行下拉刷新的自定义控件。
 * 
 * @author guolin
 * 
 */
public class RefreshableView extends LinearLayout implements OnTouchListener {

	/**
	 * 下拉状态
	 */
	public static final int STATUS_PULL_TO_REFRESH = 0;

	/**
	 * 释放立即刷新状态
	 */
	public static final int STATUS_RELEASE_TO_REFRESH = 1;

	/**
	 * 正在刷新状态
	 */
	public static final int STATUS_REFRESHING = 2;

	/**
	 * 刷新完成或未刷新状态
	 */
	public static final int STATUS_REFRESH_FINISHED = 3;

	/**
	 * 下拉头部回滚的速度
	 */
	public static final int SCROLL_SPEED = -20;

	/**
	 * 一分钟的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_MINUTE = 60 * 1000;

	/**
	 * 一小时的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_HOUR = 60 * ONE_MINUTE;

	/**
	 * 一天的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_DAY = 24 * ONE_HOUR;

	/**
	 * 一月的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_MONTH = 30 * ONE_DAY;

	/**
	 * 一年的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_YEAR = 12 * ONE_MONTH;

	/**
	 * 上次更新时间的字符串常量，用于作为SharedPreferences的键值
	 */
	private static final String UPDATED_AT = "updated_at";

	/**
	 * 下拉刷新的回调接口
	 */
	private PullToRefreshListener mListener;

	/**
	 * 用于存储上次更新时间
	 */
	private SharedPreferences preferences;

	/**
	 * 下拉头的View
	 */
	private View header;

	/**
	 * 需要去下拉刷新的ListView
	 */
	private ScrollView scrollView;
	private ListView listView;
	private LinearLayout linearLayout;
	private RelativeLayout relativeLayout;

	/**
	 * 刷新时显示的进度条
	 */
	private ProgressBar progressBar;

	/**
	 * 指示下拉和释放的箭头
	 */
	private ImageView arrow;

	/**
	 * 指示下拉和释放的文字描述
	 */
	private TextView description;

	/**
	 * 上次更新时间的文字描述
	 */
	private TextView updateAt;

	/**
	 * 下拉头的布局参数
	 */
	private MarginLayoutParams headerLayoutParams;

	/**
	 * 上次更新时间的毫秒值
	 */
	private long lastUpdateTime;

	/**
	 * 为了防止不同界面的下拉刷新在上次更新时间上互相有冲突，使用id来做区分
	 */
	private int mId = -1;

	/**
	 * 下拉头的高度
	 */
	private int hideHeaderHeight;

	/**
	 * 当前处理什么状态，可选值有STATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
	 * STATUS_REFRESHING 和 STATUS_REFRESH_FINISHED
	 */
	private int currentStatus = STATUS_REFRESH_FINISHED;;

	/**
	 * 记录上一次的状态是什么，避免进行重复操作
	 */
	private int lastStatus = currentStatus;

	/**
	 * 手指按下时的屏幕纵坐标
	 */
	private float yDown;
	
//	/**
//	 * 手指按下时的屏幕横坐标
//	 */
//	private float xDown;

	/**
	 * 在被判定为滚动之前用户手指可以移动的最大值。
	 */
	private int touchSlop;

	/**
	 * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
	 */
	private boolean loadOnce;

	/**
	 * 当前是否可以下拉，只有ListView滚动到头的时候才允许下拉
	 */
	private boolean ableToPull;
	
	/**
	 * 是否第一次隐藏刷新头，防止卡住
	 */
	private boolean isFirstHide;
	
	/**
	 * 刷新的异步任务
	 */
	private RefreshingTask refreshingTask;

	
	///在InterceptCallTouch方法中是否调用OnTouch方法  已更改
	///从外部操作是否拦截触控到子视图中
	private boolean isInterceptCallTouch;
	
	public boolean isInterceptCallTouch() {
		return isInterceptCallTouch;
	}

	public void setInterceptCallTouch(boolean isInterceptCallTouch) {
		this.isInterceptCallTouch = isInterceptCallTouch;
	}
	
	private InterceptTouchHandler intercepTouchHandler;

	public void setIntercepTouchHandler(InterceptTouchHandler intercepTouchHandler) {
		this.intercepTouchHandler = intercepTouchHandler;
	}

	/**
	 * 下拉刷新控件的构造函数，会在运行时动态添加一个下拉头的布局。
	 * 
	 * @param context
	 * @param attrs
	 */
	public RefreshableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		header = LayoutInflater.from(context).inflate(R.layout.pull_refresh_view, null, true);
		progressBar = (ProgressBar) header.findViewById(R.id.progress_bar);
		arrow = (ImageView) header.findViewById(R.id.arrow);
		description = (TextView) header.findViewById(R.id.description);
		updateAt = (TextView) header.findViewById(R.id.updated_at);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		refreshUpdatedAtValue();
		setOrientation(VERTICAL);
		addView(header, 0);
		this.isInterceptCallTouch=true;
	}

	/**
	 * 进行一些关键性的初始化操作，比如：将下拉头向上偏移进行隐藏，给ListView注册touch事件。
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed && !loadOnce) {
			this.isFirstHide=true;
			hideHeaderHeight = -header.getHeight();
			headerLayoutParams = (MarginLayoutParams) header.getLayoutParams();
			headerLayoutParams.topMargin =hideHeaderHeight;
			
			header.layout(0, hideHeaderHeight,header.getWidth() , -hideHeaderHeight);
			
			View tempView=getChildAt(1);
			int viewMarginTop=( (MarginLayoutParams) tempView.getLayoutParams()).topMargin;

			Log.d("", "onLayout(boolean changed, int l, int t, int r, int b)" +viewMarginTop);
			tempView.layout(0, viewMarginTop, tempView.getWidth(),this.getMeasuredHeight());
			
			
			if(tempView instanceof ScrollView)
			{
				scrollView = (ScrollView) getChildAt(1);
				scrollView.setOnTouchListener(this);
			}
			else if(tempView instanceof ListView)
			{
				listView=(ListView)getChildAt(1);
				listView.setOnTouchListener(this);
				if(listView.getEmptyView()!=null)
				{
					listView.getEmptyView().setClickable(true);
					listView.getEmptyView().setOnTouchListener(this);
				}
			}
			else if(tempView instanceof LinearLayout)
			{
				linearLayout=(LinearLayout)getChildAt(1);
				linearLayout.setClickable(true);
				linearLayout.setOnTouchListener(this);
			}
			else if(tempView instanceof RelativeLayout)
			{
				relativeLayout=(RelativeLayout) getChildAt(1);
				relativeLayout.setClickable(true);
				relativeLayout.setOnTouchListener(this);
			}
			loadOnce = true;
		}
	}
	
	/**
	 * 当ListView被触摸时调用，其中处理了各种下拉刷新的具体逻辑。
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
//		String actionType="";
//    	switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			actionType="ACTION_DOWN";
//			break;
//		case MotionEvent.ACTION_MOVE:
//			actionType="ACTION_MOVE";
//			break;
//		case MotionEvent.ACTION_UP:
//			actionType="ACTION_UP";
//			break;
//		default:
//			break;
//		}
//		
//		Log.d("", "  Vertical1111111111111111   onTouch " +"   "+actionType);
		
		setIsAbleToPull(event);
		if (ableToPull) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				yDown = event.getRawY();
//				xDown=event.getRawX();
				break;
			case MotionEvent.ACTION_MOVE:{
				float yMove = event.getRawY();
//				float xMove=event.getRawX();
				int distance = (int) (yMove - yDown);
//				int distanceX=(int)(xMove-xDown);
				// 如果手指是下滑状态，并且下拉头是完全隐藏的，就屏蔽下拉事件
				//Log.d("Tag", "Touch distance  "+yMove +"  "+ yDown);
				if (distance <= 0 && headerLayoutParams.topMargin <= hideHeaderHeight) {
					return false;
				}
//				if(Math.abs(distanceX)>Math.abs(distance))
//				{
//					return false;
//				}
				if (distance < touchSlop) {
					return false;
				}
				if (currentStatus != STATUS_REFRESHING) {
										
					if (headerLayoutParams.topMargin > 0) {
						currentStatus = STATUS_RELEASE_TO_REFRESH;
					} else {
						currentStatus = STATUS_PULL_TO_REFRESH;
					}
					// 通过偏移下拉头的topMargin值，来实现下拉效果
					if( (distance / 2) + hideHeaderHeight<=1){
						headerLayoutParams.topMargin = (distance / 2) + hideHeaderHeight;
						header.setLayoutParams(headerLayoutParams);
					}
					else
					{
						headerLayoutParams.topMargin = 1;
						header.setLayoutParams(headerLayoutParams);
					}
				}
				break;
			}
			case MotionEvent.ACTION_UP:
			default:{
					
					if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
						// 松手时如果是释放立即刷新状态，就去调用正在刷新的任务
						currentStatus = STATUS_REFRESHING;
	//					updateHeaderView();
						
						description.setText(getResources().getString(R.string.refreshing));
						progressBar.setVisibility(View.VISIBLE);
						arrow.clearAnimation();
						arrow.setVisibility(View.GONE);
						
						refreshingTask= new RefreshingTask();
						refreshingTask.execute();
						
					} else if (currentStatus == STATUS_PULL_TO_REFRESH) {
						
						// 松手时如果是下拉状态，就去调用隐藏下拉头的任务
						HideHeaderTask task= new HideHeaderTask();
						task.execute();
						if(this.isFirstHide)
						{
	//						Log.d("", "HideHeaderTask "+task.getStatus());
	//						if(task.getStatus()==AsyncTask.Status.PENDING){
							headerLayoutParams.topMargin = hideHeaderHeight;
							header.setLayoutParams(headerLayoutParams);
							currentStatus = STATUS_REFRESH_FINISHED;
							task.cancel(true);
						}
					}
					yDown=9999;
					break;
				}
			}
			// 时刻记得更新下拉头中的信息
			if (currentStatus == STATUS_PULL_TO_REFRESH
					|| currentStatus == STATUS_RELEASE_TO_REFRESH) {
				updateHeaderView();
				// 当前正处于下拉或释放状态，要让ListView失去焦点，否则被点击的那一项会一直处于选中状态
//				Log.d("", "STATUS_PULL_TO_REFRESH STATUS_RELEASE_TO_REFRESH");
				if(scrollView!=null)
				{
					scrollView.setPressed(false);
					scrollView.setFocusable(false);
					scrollView.setFocusableInTouchMode(false);
				}
				else if(listView!=null)
				{
					listView.setPressed(false);
					listView.setFocusable(false);
					listView.setFocusableInTouchMode(false);
				}
				lastStatus = currentStatus;
				// 当前正处于下拉或释放状态，通过返回true屏蔽掉ListView的滚动事件
				return true;
			}
		}
//		return false;
		return super.onTouchEvent(event);
	}

	public boolean onInterceptTouchEvent(MotionEvent event)
	{
		if(this.isInterceptCallTouch) this.onTouch(null,event);
		boolean result= super.onInterceptTouchEvent(event);
		
//    	String actionType="";
//    	switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			actionType="ACTION_DOWN";
//			break;
//		case MotionEvent.ACTION_MOVE:
//			actionType="ACTION_MOVE";
//			break;
//		case MotionEvent.ACTION_UP:
//			actionType="ACTION_UP";
//			break;
//		default:
//			break;
//		}
//		
//		Log.d("", "  Vertical1111111111111111   onInterceptTouchEvent "+result +"   "+actionType);
////		return result;
		
		if(this.intercepTouchHandler!=null)
			return this.intercepTouchHandler.HandleInterceptTouch(this, event);
		return result;
//		if(this.isInterceptCallTouch)
			
//		return this.isInterceptCallTouch;
	}
	
	public boolean dispatchTouchEvent(MotionEvent event)
	{
//		if(event.getAction()==MotionEvent.ACTION_UP&&currentStatus == STATUS_PULL_TO_REFRESH&&!ableToPull) {
//			
//			Log.d("", "dispatchTouchEvent 123455");
//			// 松手时如果是下拉状态，就去调用隐藏下拉头的任务
//			headerLayoutParams.topMargin = -hideHeaderHeight;
//			header.setLayoutParams(headerLayoutParams);
//			currentStatus = STATUS_REFRESH_FINISHED;
//			yDown=9999;
//		}
		return super.dispatchTouchEvent(event);
	}
	
	/**
	 * 给下拉刷新控件注册一个监听器。
	 * 
	 * @param listener
	 *            监听器的实现。
	 * @param id
	 *            为了防止不同界面的下拉刷新在上次更新时间上互相有冲突， 请不同界面在注册下拉刷新监听器时一定要传入不同的id。
	 */
	public void setOnRefreshListener(PullToRefreshListener listener, int id) {
		mListener = listener;
		mId = id;
	}

	/**
	 * 当所有的刷新逻辑完成后，记录调用一下，否则你的ListView将一直处于正在刷新状态。
	 */
	public void finishRefreshing() {
		currentStatus = STATUS_REFRESH_FINISHED;
		preferences.edit().putLong(UPDATED_AT + mId, System.currentTimeMillis()).commit();
		new HideHeaderTask().execute();
	}
	
	/**
	 * 根据当前ListView的滚动状态来设定 {@link #ableToPull}
	 * 的值，每次都需要在onTouch中第一个执行，这样可以判断出当前应该是滚动ListView，还是应该进行下拉。
	 * 
	 * @param event
	 */
	private void setIsAbleToPull(MotionEvent event) {
		boolean flag=false;
		View firstChild =null;
		if(scrollView!=null)
		{
			firstChild= ((ViewGroup)scrollView.getChildAt(0)).getChildAt(0);
			flag=scrollView.getScrollY()==0;
		}
		else if(listView!=null)
		{
			firstChild= listView.getChildAt(0);
			int firstVisiblePos= listView.getFirstVisiblePosition();
			if(firstChild!=null)
				flag=firstChild.getTop()==0&&firstVisiblePos==0;
			if(listView.getVisibility()==View.GONE &&  listView.getEmptyView()!=null)
				flag=true;
		}
		else if(linearLayout!=null)
		{
			firstChild=linearLayout.getChildAt(0);
			flag=true;
		}
		else if(relativeLayout!=null)
		{
			firstChild=relativeLayout.getChildAt(0);
			flag=true;
		}
		if(firstChild!=null){
			if(flag)
			{
				if (!ableToPull) {
					yDown = event.getRawY();
				}
				// 如果首个元素的上边缘，距离父布局值为0，就说明ListView滚动到了最顶部，此时应该允许下拉刷新
				ableToPull = true;
			}
			else
			{
				Log.d("", " currentStatus "+this.currentStatus);
				if (headerLayoutParams.topMargin != hideHeaderHeight&& this.currentStatus!=STATUS_REFRESHING  ) {
					headerLayoutParams.topMargin = hideHeaderHeight;
					header.setLayoutParams(headerLayoutParams);
				}
				ableToPull = false;
			}
		} 
		else
		{
			// 如果ListView中没有元素，也应该允许下拉刷新
			ableToPull = true;
		}
	}

	/**
	 * 更新下拉头中的信息。
	 */
	private void updateHeaderView() {
		if (lastStatus != currentStatus) {
			if (currentStatus == STATUS_PULL_TO_REFRESH) {
				description.setText(getResources().getString(R.string.pull_to_refresh));
				arrow.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				rotateArrow();
			} else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
				description.setText(getResources().getString(R.string.release_to_refresh));
				arrow.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				rotateArrow();
			} else if (currentStatus == STATUS_REFRESHING) {
				description.setText(getResources().getString(R.string.refreshing));
				progressBar.setVisibility(View.VISIBLE);
				arrow.clearAnimation();
				arrow.setVisibility(View.GONE);
			}
			refreshUpdatedAtValue();
		}
	}

	/**
	 * 根据当前的状态来旋转箭头。
	 */
	private void rotateArrow() {
		float pivotX = arrow.getWidth() / 2f;
		float pivotY = arrow.getHeight() / 2f;
		float fromDegrees = 0f;
		float toDegrees = 0f;
		if (currentStatus == STATUS_PULL_TO_REFRESH) {
			fromDegrees = 180f;
			toDegrees = 360f;
		} else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
			fromDegrees = 0f;
			toDegrees = 180f;
		}
		RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
		animation.setDuration(100);
		animation.setFillAfter(true);
		arrow.startAnimation(animation);
	}

	/**
	 * 刷新下拉头中上次更新时间的文字描述。
	 */
	private void refreshUpdatedAtValue() {
		lastUpdateTime = preferences.getLong(UPDATED_AT + mId, -1);
		long currentTime = System.currentTimeMillis();
		long timePassed = currentTime - lastUpdateTime;
		long timeIntoFormat;
		String updateAtValue;
		if (lastUpdateTime == -1) {
			updateAtValue = getResources().getString(R.string.not_updated_yet);
		} else if (timePassed < 0) {
			updateAtValue = getResources().getString(R.string.time_error);
		} else if (timePassed < ONE_MINUTE) {
			updateAtValue = getResources().getString(R.string.updated_just_now);
		} else if (timePassed < ONE_HOUR) {
			timeIntoFormat = timePassed / ONE_MINUTE;
			String value = timeIntoFormat + "分钟";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_DAY) {
			timeIntoFormat = timePassed / ONE_HOUR;
			String value = timeIntoFormat + "小时";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_MONTH) {
			timeIntoFormat = timePassed / ONE_DAY;
			String value = timeIntoFormat + "天";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_YEAR) {
			timeIntoFormat = timePassed / ONE_MONTH;
			String value = timeIntoFormat + "个月";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		} else {
			timeIntoFormat = timePassed / ONE_YEAR;
			String value = timeIntoFormat + "年";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		}
		updateAt.setText(updateAtValue);
	}

	/**
	 * 正在刷新的任务，在此任务中会去回调注册进来的下拉刷新监听器。
	 * 
	 * @author guolin
	 */
	class RefreshingTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			int topMargin = headerLayoutParams.topMargin;
			while (true) {
				topMargin = topMargin + SCROLL_SPEED;
				if (topMargin <= 0) {
					topMargin = 0;
					break;
				}
				publishProgress(topMargin);
				sleep(10);
			}
			
			currentStatus = STATUS_REFRESHING;
			publishProgress(0);
			

			if (mListener != null) {
					mListener.onRefresh();
			}
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... topMargin) {
			updateHeaderView();
			headerLayoutParams.topMargin = topMargin[0];
			header.setLayoutParams(headerLayoutParams);
		}

	}

	/**
	 * 正在刷新的任务
	 * 
	 * @author hopegi
	 */
	class SimpleRefreshingTask extends RefreshingTask {

		@Override
		protected Void doInBackground(Void... params) {
			if(headerLayoutParams!=null){
				int topMargin = headerLayoutParams.topMargin;
				while (true) {
					topMargin = topMargin + SCROLL_SPEED;
					if (topMargin <= 0) {
						topMargin = 0;
						break;
					}
					publishProgress(topMargin);
					sleep(10);
				}
			}
			currentStatus = STATUS_REFRESHING;
			publishProgress(0);
			
			
			return null;
		}

//		@Override
//		protected void onProgressUpdate(Integer... topMargin) {
//			updateHeaderView();
//			headerLayoutParams.topMargin = topMargin[0];
//			header.setLayoutParams(headerLayoutParams);
//		}

	}
	
	public void CancelRefresh()
	{
		if(this.currentStatus==STATUS_REFRESHING&&this.refreshingTask!=null&&this.refreshingTask.getStatus()==AsyncTask.Status.RUNNING)
		{
			headerLayoutParams.topMargin = hideHeaderHeight;
			header.setLayoutParams(headerLayoutParams);
			currentStatus = STATUS_REFRESH_FINISHED;
			this.refreshingTask.cancel(true);
		}
	}
	
	public void SimpleCancleRefresh()
	{
		if(this.currentStatus==STATUS_REFRESHING&&this.refreshingTask!=null)
		{
			headerLayoutParams.topMargin = hideHeaderHeight;
			header.setLayoutParams(headerLayoutParams);
			currentStatus = STATUS_REFRESH_FINISHED;
		}
	}
	
	/**
	 * 隐藏下拉头的任务，当未进行下拉刷新或下拉刷新完成后，此任务将会使下拉头重新隐藏。
	 * 
	 * @author guolin
	 */
	class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			int topMargin = headerLayoutParams.topMargin;
			while (true) {
				topMargin = topMargin + SCROLL_SPEED;
				if (topMargin <= hideHeaderHeight) {
					topMargin = hideHeaderHeight;
					break;
				}
				publishProgress(topMargin);
				sleep(10);
			}
			return topMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... topMargin) {
			headerLayoutParams.topMargin = topMargin[0];
			header.setLayoutParams(headerLayoutParams);
		}

		@Override
		protected void onPostExecute(Integer topMargin) {
			headerLayoutParams.topMargin = topMargin;
			header.setLayoutParams(headerLayoutParams);
			currentStatus = STATUS_REFRESH_FINISHED;
			isFirstHide=false;
		}
	}

	/**
	 * 使当前线程睡眠指定的毫秒数。
	 * 
	 * @param time
	 *            指定当前线程睡眠多久，以毫秒为单位
	 */
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下拉刷新的监听器，使用下拉刷新的地方应该注册此监听器来获取刷新回调。
	 * 
	 * @author guolin
	 */
	public interface PullToRefreshListener {

		/**
		 * 刷新时会去回调此方法，在方法内编写具体的刷新逻辑。注意此方法是在子线程中调用的， 你可以不必另开线程来进行耗时操作。
		 */
		void onRefresh();

	}
	
	/**
	 * onTerceptTouch时调用此接口决定是否拦截触控事件，优先级比 isInterceptCallTouch 要高，推荐使用此接口
	 * @author 居士
	 *
	 */
	public interface InterceptTouchHandler
	{
		/*
		 * 决定是否拦截触控事件，优先级比 isInterceptCallTouch，true拦截，false 不拦截
		 */
		boolean HandleInterceptTouch(RefreshableView sender,MotionEvent event);
	}
	
	public void ShowRefreshingState()
	{
		if(this.currentStatus==this.STATUS_REFRESHING)return;
		
		description.setText(getResources().getString(R.string.refreshing));
		progressBar.setVisibility(View.VISIBLE);
		arrow.clearAnimation();
		arrow.setVisibility(View.GONE);
		
		new RefreshingTask().execute();
	}
	
	public void SimpleShowRefreshingState()
	{
		if(this.currentStatus==this.STATUS_REFRESHING)return;
		
		description.setText(getResources().getString(R.string.refreshing));
		progressBar.setVisibility(View.VISIBLE);
		arrow.clearAnimation();
		arrow.setVisibility(View.GONE);
		
		 this.refreshingTask= new SimpleRefreshingTask();
		 this.refreshingTask.execute();
	}
}

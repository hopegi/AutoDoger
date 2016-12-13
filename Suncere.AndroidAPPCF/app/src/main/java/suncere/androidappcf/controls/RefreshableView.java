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
 * �ɽ�������ˢ�µ��Զ���ؼ���
 * 
 * @author guolin
 * 
 */
public class RefreshableView extends LinearLayout implements OnTouchListener {

	/**
	 * ����״̬
	 */
	public static final int STATUS_PULL_TO_REFRESH = 0;

	/**
	 * �ͷ�����ˢ��״̬
	 */
	public static final int STATUS_RELEASE_TO_REFRESH = 1;

	/**
	 * ����ˢ��״̬
	 */
	public static final int STATUS_REFRESHING = 2;

	/**
	 * ˢ����ɻ�δˢ��״̬
	 */
	public static final int STATUS_REFRESH_FINISHED = 3;

	/**
	 * ����ͷ���ع����ٶ�
	 */
	public static final int SCROLL_SPEED = -20;

	/**
	 * һ���ӵĺ���ֵ�������ж��ϴεĸ���ʱ��
	 */
	public static final long ONE_MINUTE = 60 * 1000;

	/**
	 * һСʱ�ĺ���ֵ�������ж��ϴεĸ���ʱ��
	 */
	public static final long ONE_HOUR = 60 * ONE_MINUTE;

	/**
	 * һ��ĺ���ֵ�������ж��ϴεĸ���ʱ��
	 */
	public static final long ONE_DAY = 24 * ONE_HOUR;

	/**
	 * һ�µĺ���ֵ�������ж��ϴεĸ���ʱ��
	 */
	public static final long ONE_MONTH = 30 * ONE_DAY;

	/**
	 * һ��ĺ���ֵ�������ж��ϴεĸ���ʱ��
	 */
	public static final long ONE_YEAR = 12 * ONE_MONTH;

	/**
	 * �ϴθ���ʱ����ַ���������������ΪSharedPreferences�ļ�ֵ
	 */
	private static final String UPDATED_AT = "updated_at";

	/**
	 * ����ˢ�µĻص��ӿ�
	 */
	private PullToRefreshListener mListener;

	/**
	 * ���ڴ洢�ϴθ���ʱ��
	 */
	private SharedPreferences preferences;

	/**
	 * ����ͷ��View
	 */
	private View header;

	/**
	 * ��Ҫȥ����ˢ�µ�ListView
	 */
	private ScrollView scrollView;
	private ListView listView;
	private LinearLayout linearLayout;
	private RelativeLayout relativeLayout;

	/**
	 * ˢ��ʱ��ʾ�Ľ�����
	 */
	private ProgressBar progressBar;

	/**
	 * ָʾ�������ͷŵļ�ͷ
	 */
	private ImageView arrow;

	/**
	 * ָʾ�������ͷŵ���������
	 */
	private TextView description;

	/**
	 * �ϴθ���ʱ�����������
	 */
	private TextView updateAt;

	/**
	 * ����ͷ�Ĳ��ֲ���
	 */
	private MarginLayoutParams headerLayoutParams;

	/**
	 * �ϴθ���ʱ��ĺ���ֵ
	 */
	private long lastUpdateTime;

	/**
	 * Ϊ�˷�ֹ��ͬ���������ˢ�����ϴθ���ʱ���ϻ����г�ͻ��ʹ��id��������
	 */
	private int mId = -1;

	/**
	 * ����ͷ�ĸ߶�
	 */
	private int hideHeaderHeight;

	/**
	 * ��ǰ����ʲô״̬����ѡֵ��STATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
	 * STATUS_REFRESHING �� STATUS_REFRESH_FINISHED
	 */
	private int currentStatus = STATUS_REFRESH_FINISHED;;

	/**
	 * ��¼��һ�ε�״̬��ʲô����������ظ�����
	 */
	private int lastStatus = currentStatus;

	/**
	 * ��ָ����ʱ����Ļ������
	 */
	private float yDown;
	
//	/**
//	 * ��ָ����ʱ����Ļ������
//	 */
//	private float xDown;

	/**
	 * �ڱ��ж�Ϊ����֮ǰ�û���ָ�����ƶ������ֵ��
	 */
	private int touchSlop;

	/**
	 * �Ƿ��Ѽ��ع�һ��layout������onLayout�еĳ�ʼ��ֻ�����һ��
	 */
	private boolean loadOnce;

	/**
	 * ��ǰ�Ƿ����������ֻ��ListView������ͷ��ʱ�����������
	 */
	private boolean ableToPull;
	
	/**
	 * �Ƿ��һ������ˢ��ͷ����ֹ��ס
	 */
	private boolean isFirstHide;
	
	/**
	 * ˢ�µ��첽����
	 */
	private RefreshingTask refreshingTask;

	
	///��InterceptCallTouch�������Ƿ����OnTouch����  �Ѹ���
	///���ⲿ�����Ƿ����ش��ص�����ͼ��
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
	 * ����ˢ�¿ؼ��Ĺ��캯������������ʱ��̬���һ������ͷ�Ĳ��֡�
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
	 * ����һЩ�ؼ��Եĳ�ʼ�����������磺������ͷ����ƫ�ƽ������أ���ListViewע��touch�¼���
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
	 * ��ListView������ʱ���ã����д����˸�������ˢ�µľ����߼���
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
				// �����ָ���»�״̬����������ͷ����ȫ���صģ������������¼�
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
					// ͨ��ƫ������ͷ��topMarginֵ����ʵ������Ч��
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
						// ����ʱ������ͷ�����ˢ��״̬����ȥ��������ˢ�µ�����
						currentStatus = STATUS_REFRESHING;
	//					updateHeaderView();
						
						description.setText(getResources().getString(R.string.refreshing));
						progressBar.setVisibility(View.VISIBLE);
						arrow.clearAnimation();
						arrow.setVisibility(View.GONE);
						
						refreshingTask= new RefreshingTask();
						refreshingTask.execute();
						
					} else if (currentStatus == STATUS_PULL_TO_REFRESH) {
						
						// ����ʱ���������״̬����ȥ������������ͷ������
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
			// ʱ�̼ǵø�������ͷ�е���Ϣ
			if (currentStatus == STATUS_PULL_TO_REFRESH
					|| currentStatus == STATUS_RELEASE_TO_REFRESH) {
				updateHeaderView();
				// ��ǰ�������������ͷ�״̬��Ҫ��ListViewʧȥ���㣬���򱻵������һ���һֱ����ѡ��״̬
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
				// ��ǰ�������������ͷ�״̬��ͨ������true���ε�ListView�Ĺ����¼�
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
//			// ����ʱ���������״̬����ȥ������������ͷ������
//			headerLayoutParams.topMargin = -hideHeaderHeight;
//			header.setLayoutParams(headerLayoutParams);
//			currentStatus = STATUS_REFRESH_FINISHED;
//			yDown=9999;
//		}
		return super.dispatchTouchEvent(event);
	}
	
	/**
	 * ������ˢ�¿ؼ�ע��һ����������
	 * 
	 * @param listener
	 *            ��������ʵ�֡�
	 * @param id
	 *            Ϊ�˷�ֹ��ͬ���������ˢ�����ϴθ���ʱ���ϻ����г�ͻ�� �벻ͬ������ע������ˢ�¼�����ʱһ��Ҫ���벻ͬ��id��
	 */
	public void setOnRefreshListener(PullToRefreshListener listener, int id) {
		mListener = listener;
		mId = id;
	}

	/**
	 * �����е�ˢ���߼���ɺ󣬼�¼����һ�£��������ListView��һֱ��������ˢ��״̬��
	 */
	public void finishRefreshing() {
		currentStatus = STATUS_REFRESH_FINISHED;
		preferences.edit().putLong(UPDATED_AT + mId, System.currentTimeMillis()).commit();
		new HideHeaderTask().execute();
	}
	
	/**
	 * ���ݵ�ǰListView�Ĺ���״̬���趨 {@link #ableToPull}
	 * ��ֵ��ÿ�ζ���Ҫ��onTouch�е�һ��ִ�У����������жϳ���ǰӦ���ǹ���ListView������Ӧ�ý���������
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
				// ����׸�Ԫ�ص��ϱ�Ե�����븸����ֵΪ0����˵��ListView���������������ʱӦ����������ˢ��
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
			// ���ListView��û��Ԫ�أ�ҲӦ����������ˢ��
			ableToPull = true;
		}
	}

	/**
	 * ��������ͷ�е���Ϣ��
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
	 * ���ݵ�ǰ��״̬����ת��ͷ��
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
	 * ˢ������ͷ���ϴθ���ʱ�������������
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
			String value = timeIntoFormat + "����";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_DAY) {
			timeIntoFormat = timePassed / ONE_HOUR;
			String value = timeIntoFormat + "Сʱ";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_MONTH) {
			timeIntoFormat = timePassed / ONE_DAY;
			String value = timeIntoFormat + "��";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_YEAR) {
			timeIntoFormat = timePassed / ONE_MONTH;
			String value = timeIntoFormat + "����";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		} else {
			timeIntoFormat = timePassed / ONE_YEAR;
			String value = timeIntoFormat + "��";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		}
		updateAt.setText(updateAtValue);
	}

	/**
	 * ����ˢ�µ������ڴ������л�ȥ�ص�ע�����������ˢ�¼�������
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
	 * ����ˢ�µ�����
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
	 * ��������ͷ�����񣬵�δ��������ˢ�»�����ˢ����ɺ󣬴����񽫻�ʹ����ͷ�������ء�
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
	 * ʹ��ǰ�߳�˯��ָ���ĺ�������
	 * 
	 * @param time
	 *            ָ����ǰ�߳�˯�߶�ã��Ժ���Ϊ��λ
	 */
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����ˢ�µļ�������ʹ������ˢ�µĵط�Ӧ��ע��˼���������ȡˢ�»ص���
	 * 
	 * @author guolin
	 */
	public interface PullToRefreshListener {

		/**
		 * ˢ��ʱ��ȥ�ص��˷������ڷ����ڱ�д�����ˢ���߼���ע��˷����������߳��е��õģ� ����Բ������߳������к�ʱ������
		 */
		void onRefresh();

	}
	
	/**
	 * onTerceptTouchʱ���ô˽ӿھ����Ƿ����ش����¼������ȼ��� isInterceptCallTouch Ҫ�ߣ��Ƽ�ʹ�ô˽ӿ�
	 * @author ��ʿ
	 *
	 */
	public interface InterceptTouchHandler
	{
		/*
		 * �����Ƿ����ش����¼������ȼ��� isInterceptCallTouch��true���أ�false ������
		 */
		boolean HandleInterceptTouch(RefreshableView sender, MotionEvent event);
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

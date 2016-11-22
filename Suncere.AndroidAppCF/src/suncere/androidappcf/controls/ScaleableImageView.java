package suncere.androidappcf.controls;

import suncere.androidappcf.controls.ScaleArea.FlingListenner;
import suncere.androidappcf.controls.ScaleArea.ZoomListenner;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class ScaleableImageView extends LinearLayout {

	ScaleArea scaleArea;
	ZoomListenner _zListenner;
	FlingListenner _fListenner;
	
	public ScaleableImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		InitView();
	}

	public ScaleableImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		InitView();
	}
	

	public void set_zListenner(ZoomListenner _zListenner) {
		this._zListenner = _zListenner;
	}

	public void set_fListenner(FlingListenner _fListenner) {
		this._fListenner = _fListenner;
	}
	
	protected void InitView()
	{
		scaleArea=new ScaleArea(this.getContext(),zListenner,fListenner);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT); 
		this.addView(scaleArea, params);
	}
	
	public void setImage(Bitmap bitmap)
	{
		scaleArea.initScaleView(bitmap);
	}
	
	public void setImage(String imgPath)
	{
		Bitmap bitmap=BitmapFactory.decodeFile(imgPath);
		this.setImage(bitmap);
	}

	ZoomListenner zListenner=new ZoomListenner() {
        @Override
        public void doZoom(int w,int h) {
                //处理图片处于缩放状态时的任务      
        		if(_zListenner!=null)
        			_zListenner.doZoom(w, h);
            }
        };

      FlingListenner fListenner=new FlingListenner() {
        @Override
        public void doFling(int direction) {
                //处理轻扫图片时的任务（一般用来切换图片）      
        			if(_fListenner!=null)
        				_fListenner.doFling(direction);
                }
        };
}

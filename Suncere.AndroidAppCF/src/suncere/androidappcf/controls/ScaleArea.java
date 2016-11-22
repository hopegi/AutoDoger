package suncere.androidappcf.controls;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.content.Context;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class ScaleArea extends FrameLayout
{ 

    private int imgDisplayW;
    private int imgDisplayH;
    private int imgW;
    private int imgH;
    private ScaleView touchView;
    private Context context;
    private Bitmap img;
    ZoomListenner zlistenner;
    FlingListenner flistenner;


  public ScaleArea(Context context,ZoomListenner zlistenner,FlingListenner flistenner)
    { 
        super(context);
        this.context = context;
        this.zlistenner=zlistenner;
        this.flistenner=flistenner;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
        int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
    }


@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        imgDisplayW = w;
        imgDisplayH = h;


    	touchView = new ScaleView(context, imgDisplayW, imgDisplayH);// 这句就是我们的自定义ImageView
        touchView.setZlistenner(zlistenner);
        touchView.setFlingListenner(flistenner);
        touchView.setImageBitmap(img);
        this.addView(touchView);
        Log.d("", "onSizeChanged ScaleArea");
    }


    public ScaleView getView(){
            return touchView;
    }
    public void show(){
            touchView.setVisibility(View.VISIBLE);
    }
    public void hide(){
            touchView.setVisibility(View.GONE);
    }


public void initScaleView(Bitmap bimap)
    {
            if(bimap==null){
                    return;
            }
        if (null == touchView){
            touchView = new ScaleView(context, imgDisplayW, imgDisplayH);// 这句就是我们的自定义ImageView
            this.addView(touchView);
        }else{
            touchView.clearAnimation();
            touchView.setImageBitmap(null);
        }

       recycleImg();
        img =bimap;

        imgW = img.getWidth();
        imgH = img.getHeight();
       touchView.setImageBitmap(bimap);

   //对于较小的图片（即：宽与高都小于屏幕时，我们直接取屏幕的宽与高，）
        int layout_w = imgDisplayW ;
        int layout_h =  imgDisplayH ;


      if (imgW >= imgH){
            if (layout_w == imgDisplayW){
                layout_h = (int) (imgH * ((float) imgDisplayW / imgW));
            }
        }else{
            if (layout_h == imgDisplayH){
                    layout_w=(int)(imgW*((float)(imgDisplayH-460)/imgH));
            }
        }


  FrameLayout.LayoutParams flLayoutParams = new LayoutParams(layout_w, layout_h);     
        flLayoutParams.gravity=Gravity.CENTER;
        touchView.setLayoutParams(flLayoutParams);// 这是自定义imageView的大小，也就是触摸范围
    }


   /**
     * 回收图片
     */
    public void recycleImg()
    {
        if(null != img)
        {
            img.recycle();
            img = null;
        }
    }
    
    private class ScaleView extends ImageView
    {
        static final int TRANS_DURATION = 400;
        static final int NONE = 0;// 没有状态
        static final int DRAG = 1; // 移动状态
        static final int ZOOM = 2; // 缩放状态
        static final int BIGGER = 3; // 放大图片
        static final int SMALLER = 4; // 缩小图片
        private int mode = NONE; // mode用于标示当前处于什么状态

         private float beforeLenght; // 第一次触摸两点的距离    
         private float afterLenght; // 移动后两点的距离
        private float scale = 0.04f; // 缩放因子

        private int screenW;
        private int screenH;

        private int start_x;// 开始触摸点
        private int start_y;
        private int stop_x;// 结束触摸点
        private int stop_y;
        private TranslateAnimation trans; // 回弹动画

      public ScaleView(Context context, int w, int h)// w，h就是图片的移动范围
        {
            super(context);
            this.setPadding(0, 0, 0, 0);
            screenW = w;
            screenH = h;

            this.setLongClickable(true); 

        }


    // 用来计算2个触摸点的距离
        private float spacing(MotionEvent event)
        {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return FloatMath.sqrt(x * x + y * y);
        }

        float moveX,moveY,toX,toY;//单点按下时的坐标与起来时的坐标
        boolean isZoom=false;//是否处于缩放（指放大）
        boolean isSingle=true;//是否向左拨动

    @Override
        public boolean onTouchEvent(MotionEvent event)
        {    

            switch (event.getAction() & MotionEvent.ACTION_MASK) {//MotionEvent.ACTION_MASK表示多点触控事件
            case MotionEvent.ACTION_DOWN:
                     if(this.getWidth()>screenW||this.getHeight()>screenH){
                    mode = DRAG;
                     }


                    stop_x = (int) event.getRawX();//表示相对于屏幕左上角为原点的坐标
                    stop_y = (int) event.getRawY();
                    start_x = stop_x - this.getLeft();


                    start_y = stop_y - this.getTop();

                    if(event.getPointerCount()==2){
                        beforeLenght = spacing(event);
                    }
                    //获取此时坐标
                    moveX=event.getX();
                    moveY=event.getY();
                    break;


             case MotionEvent.ACTION_POINTER_DOWN:
                    if (spacing(event) > 10f) {
                            mode = ZOOM;
                            beforeLenght = spacing(event);
                    }
                    isSingle=false;
                    break;
            case MotionEvent.ACTION_UP:
                    if(this.getWidth()<screenW){
                    this.setPosition((screenW-this.getWidth())/2, (screenH-this.getHeight())/2, (screenW-this.getWidth())/2+this.getWidth(), (screenH- 
                    		this.getHeight())/2+this.getHeight());

                    }
                     //获取此时坐标
                    toX=event.getX();
                    toY=event.getY();

                    //是否拨动
                    if(event.getPointerCount()==1&&isSingle==true){
                            if(isZoom==false&&Math.abs(moveX-toX)>60){
                                    if(moveX-toX>0){
                                            direction=LEFT;
                                            flistenner.doFling(direction);
                                    }else{
                                            direction=RIGHT;
                                            flistenner.doFling(direction);
                                    }
                            }
                }
                    isSingle=true;
                    direction=NO;

                     int disX = 0;
                     int disY = 0;
                if(getHeight()<=screenH )
                    {
                        if(this.getTop()<0 )
                        {
                            disY = getTop();
                            //layout(left , top, right,bottom)函数表示设置view的位置。
                            this.layout(this.getLeft(), 0, this.getRight(), 0 + this.getHeight());
                        }
                        else if(this.getBottom()>=screenH)
                        {
                            disY = getHeight()- screenH+getTop();
                            this.layout(this.getLeft(), screenH-getHeight(), this.getRight(), screenH);
                        }

                   }else{
                        int Y1 = getTop();
                        int Y2 = getHeight()- screenH+getTop();
                            if(Y1>0)
                            {
                                disY= Y1;
                                this.layout(this.getLeft(), 0, this.getRight(), 0 + this.getHeight());
                            }else if(Y2<0){
                                disY = Y2;
                                this.layout(this.getLeft(), screenH-getHeight(), this.getRight(), screenH);
                            }
                    }

               if(getWidth()<=screenW)
                    {
                        if(this.getLeft()<0)
                        {
                            disX = getLeft();
                            this.layout(0, this.getTop(), 0+getWidth(), this.getBottom());
                        }
                        else if(this.getRight()>screenW)
                        {
                            disX = getWidth()-screenW+getLeft();
                            this.layout(screenW-getWidth(), this.getTop(), screenW, this.getBottom());
                        }

                }else {
                        int X1 = getLeft();
                        int X2 = getWidth()-screenW+getLeft();
                        if(X1>0) {
                            disX = X1;
                            this.layout(0, this.getTop(), 0+getWidth(), this.getBottom());
                        }else if(X2<0) {
                            disX = X2;
                            this.layout(screenW-getWidth(), this.getTop(), screenW, this.getBottom());
                        }

                    }

                   //如果图片缩放到宽高任意一个小于200，那么自动放大，直到大于200.
                    while(getWidth()<screenW-2*(screenW*0.02)) {
                        setScale(scale,BIGGER,0);
                    }
                    //根据disX和disY的偏移量采用移动动画回弹归位，动画时间为500毫秒。
                    if(disX!=0 || disY!=0)
                    {
                        trans = new TranslateAnimation(disX, 0, disY, 0);
                        trans.setDuration(TRANS_DURATION);
                        this.startAnimation(trans);
                    }
                    mode = NONE;
                    break;

            case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
            case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                            //执行拖动事件的时，不断变换自定义imageView的位置从而达到拖动效果
                            if(this.getTop()<0||this.getBottom()>screenH){
                                this.setPosition(stop_x - start_x, stop_y - start_y, stop_x + this.getWidth() - start_x, stop_y - start_y + this.getHeight());               
                        }else{
                                   this.setPosition(stop_x - start_x, this.getTop(), stop_x + this.getWidth() - start_x, this.getBottom());               
                        }
                       stop_x = (int) event.getRawX();
                       stop_y = (int) event.getRawY(); 

                    } else if (mode == ZOOM) {
                            
                        if(spacing(event)>10f)
                        {

                        	afterLenght = spacing(event);
                            float gapLenght = afterLenght - beforeLenght;                     
                            if(gapLenght == 0) {  
                               break;
                            }
                          //图片宽度（也就是自定义imageView）必须大于70才可以缩放
                            else if(Math.abs(gapLenght)>5f&&getWidth()>70)
                            {
                                if(gapLenght>0) { 
                                    this.setScale(scale,BIGGER,1);   
                                }else {  
                                    this.setScale(scale,SMALLER,1);   
                                }                             
                                beforeLenght = afterLenght; 
                            }
                        }
                    }
                    break;
             }
            return true;
        }


      //----------------------------------------------------zoom监听器
      ZoomListenner zlistenner;
        public ZoomListenner getZlistenner() {
                    return zlistenner;
            }

            public void setZlistenner(ZoomListenner zlistenner) {
                    this.zlistenner = zlistenner;
            }




    //----------------------------------------------------fling监听器

        public static final int LEFT=20;
        public static final int RIGHT=30;
        public static final int NO=40;
        int direction=NO;
        FlingListenner flistenner;
        public FlingListenner getFlistenner() {
                    return flistenner;
            }
        public void setFlingListenner(FlingListenner flistenner) {
                    this.flistenner = flistenner;
            }




        /**
         * 放大的最大范围
         */
        float MaxWigth=5400;
        float MaxHeight=7800;

    private void setScale(float temp, int flag,int zoom)
        {

            if (flag == BIGGER)
            {
                    if(this.getWidth()>MaxWigth||this.getHeight()>MaxHeight){
                            
                    }else{

                   // setFrame(left , top, right,bottom)函数表示改变当前view的框架，也就是大小。
                    this.setFrame(this.getLeft() - (int) (temp * this.getWidth()),
                                            this.getTop() - (int) (temp * this.getHeight()),
                                            this.getRight() + (int) (temp * this.getWidth()),
                                            this.getBottom() + (int) (temp * this.getHeight()));

                   this.setPosition((screenW-this.getWidth())/2, (screenH-this.getHeight())/2, (screenW-this.getWidth())/2+this.getWidth(), (screenH-           this.getHeight())/2+this.getHeight());

                    }

            }


           else if (flag == SMALLER)
            {
                this.setFrame(this.getLeft() + (int) (temp * this.getWidth()),
                    this.getTop() + (int) (temp * this.getHeight()),
                    this.getRight() - (int) (temp * this.getWidth()),
                    this.getBottom() - (int) (temp * this.getHeight()));
            }
           if(mode==ZOOM){
                        zlistenner.doZoom(this.getWidth(),this.getHeight());
           }

          if(zoom==0){
                   
           }else{
                   if(this.getWidth()>screenW){
                       isZoom=true;
               }else{
                       isZoom=false;
               }
           }


        }
      private void setPosition(int left, int top, int right, int bottom)
        {
            this.layout(left, top, right, bottom);
        }



    }

	    /**
	     * 拨动事件
	     * @param l ImageView的getLeft
	     * @param r ImageView的getRight
	     */
	public interface FlingListenner{
	        public void doFling(int direction);
	};
    
    /**
     * 缩放事件
     * @param w ImageView的宽
     * @param h  ImageView的高
     */
    public interface ZoomListenner{
        public void doZoom(int w,int h);
};
}
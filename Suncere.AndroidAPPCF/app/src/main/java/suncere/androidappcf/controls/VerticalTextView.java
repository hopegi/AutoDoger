package suncere.androidappcf.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

public class VerticalTextView extends TextView {
	
	final boolean topDown;

	   public VerticalTextView(Context context, AttributeSet attrs){
	      super(context, attrs);
	      final int gravity = getGravity();
	      if(Gravity.isVertical(gravity) && (gravity&Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
	         setGravity((gravity&Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.TOP);
	         topDown = false;
	      }else
	         topDown = true;
	   }

	   @Override
	   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
	      super.onMeasure(heightMeasureSpec, widthMeasureSpec);
	      setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
	   }

	   @Override
	   protected void onDraw(Canvas canvas){
	      TextPaint textPaint = getPaint(); 
	      textPaint.setColor(getCurrentTextColor());
	      textPaint.drawableState = getDrawableState();

	      canvas.save();

	      if(topDown){
	         canvas.translate(getWidth(), 0);
	         canvas.rotate(90);
	      }else {
	         canvas.translate(0, getHeight());
	         canvas.rotate(-90);
	      }


	      canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());

	      getLayout().draw(canvas);
	      canvas.restore();
	  }
	
//	final boolean topDown;
//
//
//	   public VerticalTextView(Context context, AttributeSet attrs){
//	      super(context, attrs);
//	      final int gravity = getGravity();
//	      if(Gravity.isVertical(gravity) && (gravity&Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
//	         setGravity((gravity&Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.TOP);
//	         topDown = false;
//	      }else
//	         topDown = true;
//	   }
//
//
//	   @Override
//	   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
//	      super.onMeasure(heightMeasureSpec, widthMeasureSpec);
//	      setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
//	   }
//
//
//	   @Override
//	   protected boolean setFrame(int l, int t, int r, int b){
//	      return super.setFrame(l, t, l+(b-t), t+(r-l));
//	   }
//	   
//	   int width=-1;
//	   int height=-1;
//	   
//	   protected void onScrollChanged(int horiz, int vert, int oldHoriz, int oldVert)
//	   {
//		   super.onScrollChanged(horiz, vert, oldHoriz, oldVert);
//		   Log.d("", "Event onScrollChanged");
//	   }
//
//	   protected void drawableStateChanged()
//	   {
//		   super.drawableStateChanged();
//		   Log.d("", "Event drawableStateChanged");
//	   }
//	   
//	   protected void dispatchDraw(Canvas canvas)
//	   {
//		   super.dispatchDraw(canvas);
//		   Log.d("", "Event dispatchDraw");
//	   }
//	   
//	   @Override
//	   public void draw(Canvas canvas){
//		   
//		   Log.d("", "draw "+this.getId());
//		   TextPaint textPaint = getPaint(); 
//		      textPaint.setColor(getCurrentTextColor());
//		      textPaint.drawableState = getDrawableState();
//		   canvas.save();
//		   if(width==-1)
//			   width=this.getWidth();
//		   if(height==-1)
//			   height=this.getHeight();
//	      if(topDown){
//	         canvas.translate(getHeight(), 0);
//	         canvas.rotate(90);
//	      }else {
//	         canvas.translate(0, getWidth());
//	         canvas.rotate(-90);
//	      }
//	      canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
//	      getLayout().draw(canvas);
//	      canvas.restore();
////	      canvas.clipRect(0, 0, width, height, android.graphics.Region.Op.REPLACE);
////	      super.draw(canvas);
//		   
//	   }
	   
}

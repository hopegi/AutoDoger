package suncere.androidappcf.controls;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.achartengine.chart.TimeChart;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.Point;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import suncere.androidappcf.R;
import suncere.androidappcf.aqi.AQITool;
import suncere.androidappcf.aqi.AQITool.AQIPresentEnum;
import suncere.androidappcf.tools.Convert;
import suncere.androidappcf.tools.ScreenSuitableTool;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.Log;
import android.view.View;

class MyTimeChart extends TimeChart {

	float labelTextSize;
	XYMultipleSeriesDataset dataset;
	Resources resources;
	MyCubeChartView view;
	private boolean needZebraText;
	private boolean needHideZero;
	
	float oldX = 0;
	float oldY = 0;
	
	
	private int mstart=0;
	private List<Float>mpoints;
	 XYSeries mseries;
//	 XYMultipleSeriesRenderer mrenderer;
	
	 private float firstMultiplier;
	  private float secondMultiplier;
	  private Point p1 = new Point();
	  private Point p2 = new Point();
	  private Point p3 = new Point();
	
	public void setNeedZebraText(boolean needZebraText) {
		this.needZebraText = needZebraText;
		
	}
	
	public void setNeedHideZero(boolean needHideZero)
	{
		this.needHideZero=needHideZero;
	}
	
	private static final long serialVersionUID = 1L;
	
	public MyTimeChart(XYMultipleSeriesDataset dataset,
			XYMultipleSeriesRenderer renderer,Resources res,MyCubeChartView view) {
		super(dataset, renderer);
		labelTextSize=renderer.getLabelsTextSize();
		this.dataset=dataset;
		this.resources=res;
		this.view=view;
		this.needZebraText=false;
		mpoints=new ArrayList<Float>();
//		Log.e("hjo", "MyTimeChart MyTimeChart��");
		
	}

	@Override
	protected void drawPath(Canvas arg0, List<Float> arg1, Paint arg2,
			boolean arg3) {
		//������д�����������ȥ��0��
//		Log.e("hjo", "��drawPath��"); 
		
		/***************************************************************************
	
		����˳��  drawPath-->drawChartValuesText-->drawString-->drawXLabels-->drawYLabels
		***************************************************************************/
		
		
		if (view.isHideY()) {
			this.mRenderer.setMargins(new int[] { (int) (ScreenSuitableTool.ConvertSuitableDpi( -30)),  (int) (ScreenSuitableTool.ConvertSuitableDpi( -20)), 15, 20 });
		}
		
		this.mRenderer.setShowGrid(view.isShowGrid());
		this.mRenderer.setAxesColor(view.getXColor());
		
		if (view.getXLabelsColor()!=Color.WHITE) {
			this.mRenderer.setXLabelsColor(view.getXLabelsColor());
		}
		if (view.getYLabelsColor()!=Color.WHITE) {
			this.mRenderer.setYLabelsColor(0,view.getYLabelsColor());
		}
		
	}
	    
	@Override
	protected void drawString(Canvas arg0, String arg1, float arg2, float arg3,
			Paint arg4) {
		
		///80��Y���Xֵ ���ֵ������Text��С�ͻ���Margin�б��ʱ������
//		Log.e("hjo", "����drawString");
		if( this.view.isShowYStaff()&&arg2!=80&&  arg1.length()!=0)
		{
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.WHITE);
			paint.setStrokeWidth(1);
			Path path = new Path();
			path.moveTo(arg2, 860);
			path.lineTo(arg2, arg3+ 25);
			PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 },1);
			paint.setPathEffect(effects);
			arg0.drawPath(path, paint);
		}
		super.drawString(arg0, arg1, arg2, arg3, arg4);
	}
	
	@Override
	protected void drawYLabels(Map<Integer,List<Double>> allYLabels,
            Canvas canvas,
            Paint paint,
            int maxScaleNumber,
            int left,
            int right,
            int bottom,
            double[] yPixelsPerUnit,
            double[] minY)
	{

		

		
//		Log.e("hjo", "��Y�᣺"+bottom); 
		paint.setTextSize( ScreenSuitableTool.ConvertSuitableDpi(  40));
		super.drawYLabels(allYLabels, canvas, paint, maxScaleNumber, 
		left, right, bottom, yPixelsPerUnit, minY);
		
		//��Y���AQI��ɫ��
		if(this.view.isShowAQIColorY())
		{
	
			float strokeWidth=paint.getStrokeWidth();
			int color= paint.getColor();
			
			paint.setColor(Color.GREEN);
			paint.setStrokeWidth(13);
	
			float delta=(bottom- maxScaleNumber-labelTextSize)/10;
			float start=1+labelTextSize;
			float end=start+4*delta;
			
			
			paint.setColor(resources.getColor(R.color.aqi_6g));
			canvas.drawLine(left+6.5f, start, left+6.5f, end, paint);
			
			start=end;
			end+=2*delta;
			paint.setColor(resources.getColor(R.color.aqi_5g));
			canvas.drawLine(left+6.5f, start, left+6.5f, end, paint);
	
			start=end;
			end+=delta;
			paint.setColor(resources.getColor(R.color.aqi_4g));
			canvas.drawLine(left+6.5f, start, left+6.5f, end, paint);
	
			start=end;
			end+=delta;
			paint.setColor(resources.getColor(R.color.aqi_3g));
			canvas.drawLine(left+6.5f, start, left+6.5f, end, paint);
	
			start=end;
			end+=delta;
			paint.setColor(resources.getColor(R.color.aqi_2g));
			canvas.drawLine(left+6.5f, start, left+6.5f, end, paint);
	
			start=end;
			end+=delta;
			paint.setColor(resources.getColor(R.color.aqi_1g));
			canvas.drawLine(left+6.5f, start, left+6.5f, end, paint);
			
			paint.setStrokeWidth(strokeWidth);
			paint.setColor(color);
		}
	}

	
	
	protected void drawXLabels(List<Double> xLabels, Double[] xTextLabelLocations,
			Canvas canvas, Paint paint, int left, int top, int bottom, 
			double xPixelsPerUnit, double minX, double maxX)
	{
//		Log.e("hjo", "����drawXLabels   X�����꣺"+bottom);
//		Log.e("hjo", "��X�᣺"+bottom); 
		
		
//		
		
		
		float textSize=paint.getTextSize();
		int textColor=paint.getColor();
		paint.setColor(Color.argb(255,0,161,248));
		paint.setTextSize(   ScreenSuitableTool.ConvertSuitableDpi( 40));
		super.drawXTextLabels(xTextLabelLocations, canvas, paint, true, left, top, bottom, xPixelsPerUnit, minX, maxX);		
		//��ԭ����
		paint.setTextSize(textSize);
		paint.setColor(textColor);
		
		Paint paint4=new Paint();
		paint4.setColor(Color.argb(150, 255, 255, 255));
//		paint4.setTextSize(   ScreenSuitableTool.ConvertSuitableDpi(60));
		paint4.setStrokeWidth(ScreenSuitableTool.ConvertSuitableDpi(4));
		
		if (mseries!=null ) {
			
    	if ( view.isShowLinePoint() || view.isShowArea()){
		  for (int i = 0; i+mstart<mseries.getItemCount()&&i*2+1<mpoints.size(); i++) {
			
	        if ( view.isShowLinePoint()) {//������
				int muber=(int) (bottom-mpoints.get(i*2+1))/20;//ÿ�����ߵ� ScreenSuitableTool.ConvertSuitableDpi(bottom)
				for (int j = 0; j < muber; j++) {
					float start=mpoints.get(i*2+1)+ScreenSuitableTool.ConvertSuitableDpi(30)*j+15;
					float end=mpoints.get(i*2+1)+ScreenSuitableTool.ConvertSuitableDpi(30)*(j+1);
					if (start>=bottom) {//ScreenSuitableTool.ConvertSuitableDpi(829)
						canvas.drawLine(mpoints.get(i*2),  start-5, mpoints.get(i*2), start, paint4);
						break;
					}else if (start<ScreenSuitableTool.ConvertSuitableDpi(bottom) &&
							end>=bottom) {
						canvas.drawLine(mpoints.get(i*2),  start, mpoints.get(i*2), bottom, paint4);
						break;
					}else {
						canvas.drawLine(mpoints.get(i*2),  start, mpoints.get(i*2), end, paint4);
		 			}
		    	}
			}
			
			if (view.isShowArea()) {
				Paint paint3 = new Paint();
				paint3.setColor(Color.argb(50, 255, 255, 255));
				if (i!=0 ) {// && i!=series.getItemCount()
					if (mseries.getY(i + mstart)==0  ) {//���ֵΪ0 ����
						continue ;
					}else if (i>1 && mseries.getY(i + mstart-1)==0) {
						continue ;
					}else if (mseries.getY(i + mstart)>mseries.getY(i + mstart-1)) {//���ߴ���ǰ��
						canvas.drawRect(mpoints.get((i -1)* 2), mpoints.get((i-1) * 2+1), mpoints.get(i * 2), bottom, paint3);//�з�Ϊ���κ�������
						//���������
						   Path path3 = new Path();  
						    path3.moveTo(mpoints.get((i -1)* 2), mpoints.get((i-1) * 2+1));// �˵�Ϊ����ε����  
					        path3.lineTo(mpoints.get(i * 2), mpoints.get(i * 2+1));  
					        path3.lineTo(mpoints.get(i * 2), mpoints.get((i-1) * 2+1));  
					        path3.close(); // ʹ��Щ�㹹�ɷ�յĶ����  
					        canvas.drawPath(path3, paint3);  
					}else if (mseries.getY(i + mstart)==mseries.getY(i + mstart-1)) {
						canvas.drawRect(mpoints.get((i -1)* 2), mpoints.get((i-1) * 2+1), mpoints.get(i * 2), bottom, paint3);//�з�Ϊ���κ�������
					}else if (mseries.getY(i + mstart)<mseries.getY(i + mstart-1)) {//ǰ�ߴ��ں���
						canvas.drawRect(mpoints.get((i-1) * 2), mpoints.get(i * 2+1), mpoints.get(i * 2), bottom, paint3);//�з�Ϊ���κ�������
						//���������
						    Path path3 = new Path();  
						    path3.moveTo(mpoints.get((i -1)* 2), mpoints.get((i-1) * 2+1));// �˵�Ϊ����ε����  
						    path3.lineTo(mpoints.get(i * 2), mpoints.get(i * 2+1));  
						    path3.lineTo(mpoints.get((i-1) * 2), mpoints.get(i * 2+1));  
						    path3.close(); // ʹ��Щ�㹹�ɷ�յĶ����  
					        canvas.drawPath(path3, paint3);  
					}
				}
			 }
		  }
		 }
		}
	}
	

	
	
	protected void drawChartValuesText(Canvas canvas,
            XYSeries series,
            SimpleSeriesRenderer renderer,
            Paint paint,
            List<Float> points,
            int seriesIndex,
            int startIndex)
	{
		
//		Log.e("hjo", "��drawChartValuesText��"); 
			
		
		//��ͼ������   
		mstart=startIndex;
		mpoints.clear();
		mpoints.addAll(points);
		mseries=new XYSeries(series.getTitle());
		mseries=series;
		
		float textSize=paint.getTextSize();
		paint.setTextSize(   ScreenSuitableTool.ConvertSuitableDpi(  40));
		renderer.setDisplayChartValuesDistance(1);
		
		boolean zebra,zero;
		int offset=series.getItemCount()%2;
		List<Float> nPoints=new ArrayList<Float>(points.size());
		
		for(int i=0;i+startIndex<series.getItemCount()&&i*2+1<points.size();i++){
			//�����ߴ���
			Paint paint2 = new Paint();
			paint2.setAntiAlias(true);
			paint2.setStyle(Paint.Style.STROKE);
			paint2.setColor(Color.WHITE);
			paint2.setStrokeWidth(2f);
			if (view.getLineWidth()!=0) {
				paint2.setStrokeWidth(view.getLineWidth());
			}
			Path path = new Path();
			path.moveTo(mpoints.get(i * 2), mpoints.get(i * 2 + 1));
			path.lineTo(oldX, oldY);
			oldX = mpoints.get(i * 2);
			oldY = mpoints.get(i * 2 + 1);
			int index = i + mstart - 1;
			if (index >= 0 && i != 0) {
				if (mseries.getY(i + mstart) != 0
						&& mseries.getY(index) != 0) {
					canvas.drawPath(path, paint2);
				}
			}
		}
		
		
		for(int i=0;i+startIndex<series.getItemCount()&&i*2+1<points.size();i++)
		{
			zebra=false;
			zero=false;
			//ȥ��ֵΪ0��ValesText
			if(this.needHideZero&& series.getY(i+startIndex)==0)//this.view.minY
			{
				//��������Ӷ�ʹ��ֵ�����ֽ���
				zero=true;
			}
			if(this.needZebraText&&i%2==offset)
			{
				zebra=true;
			}
			
			if(zero)
			{
				points.set(i*2+1, points.get(i*2+1)*100);
			}
			else
			{
				int preColor=paint.getColor();
				if(view.isAQIColorPoint())	
				{
					paint.setColor( Convert.toInt( AQITool.GetAQIResourceByAQI((int) series.getY(i+startIndex), AQIPresentEnum.Color)));
				}
				canvas.drawCircle(points.get(i*2), points.get(i*2+1), ScreenSuitableTool.ConvertSuitableDpi( 20), paint);
				paint.setColor(preColor);
			}
			//����ſ�ʱ  �ɸ�����ʾ����С�������
//			if(zebra)
//			{
//				points.set(i*2+1, points.get(i*2+1)*100);
//			}
//			else
//			{
//				nPoints.add(points.get(i*2));
//				nPoints.add(points.get(i*2+1));
//			}
			
			
		}
//		if (view.getXYTextColor()>0 ) {
//			paint.setColor(view.getXYTextColor());
//		}
		int textColor=paint.getColor();
		canvas.translate(0,  ScreenSuitableTool.ConvertSuitableDpi(-30));//�ѵ�ǰ������ԭ���Ƶ�(0,ScreenSuitableTool.ConvertSuitableDpi(-30))
		if (view.getChartValueTextColor()!=0) {
			paint.setColor(view.getChartValueTextColor());
		}
		super.drawChartValuesText(canvas, series, renderer, paint, points, seriesIndex, startIndex);
		paint.setColor(textColor);
		canvas.translate(0, ScreenSuitableTool.ConvertSuitableDpi( 30));
		paint.setTextSize(textSize);
		
		
		
		/**************************���������Ϊ������ʱ��ʾ��ȫ���   ����������*****************************************/
//		Paint paint4=new Paint();
//		paint4.setColor(Color.WHITE);
//		paint4.setTextSize(ScreenSuitableTool.ConvertSuitableDpi( 30f));
//
//		
//		 int seriesNr = this.mDataset.getSeriesCount();
//		    int length = points.size();
//		    float halfDiffX = getHalfDiffX(points, length, seriesNr);
//		    for (int i = 0; i < length; i += 2) {
//		      int index = startIndex + i / 2;
//		      double value = series.getY(index);
//		      if (!isNullValue(value)) {
//		        float x = ((Float)points.get(i)).floatValue();
////		        if (this.mType == Type.DEFAULT) {
//		          x += seriesIndex * 2 * halfDiffX - (seriesNr - 1.5F) * halfDiffX;
////		        }
//		        if (value >= 0.0D) {
//		          drawText(canvas, getLabel(renderer.getChartValuesFormat(), value), x-ScreenSuitableTool.ConvertSuitableDpi( 15f), ((Float)points.get(i + 1)).floatValue() - renderer.getChartValuesSpacing(), paint4, 0.0F);
//		        }
//		        else
//		          drawText(canvas, getLabel(renderer.getChartValuesFormat(), value), x-ScreenSuitableTool.ConvertSuitableDpi( 15f), ((Float)points.get(i + 1)).floatValue() + renderer.getChartValuesTextSize() + renderer.getChartValuesSpacing() - 3.0F, paint4, 0.0F);
//		      }
//		    }
		
		
	}
	
//	  protected float getHalfDiffX(List<Float> points, int length, int seriesNr)
//	  {
//	    float barWidth = this.mRenderer.getBarWidth();
//	    if (barWidth > 0.0F) {
//	      return barWidth / 2.0F;
//	    }
//	    int div = length;
//	    if (length > 2) {
//	      div = length - 2;
//	    }
//	    float halfDiffX = (((Float)points.get(length - 2)).floatValue() - ((Float)points.get(0)).floatValue()) / div;
//	    if (halfDiffX == 0.0F) {
//	      halfDiffX = 10.0F;
//	    }
//
////	    if (this.mType != Type.STACKED) {
////	      halfDiffX /= seriesNr;
////	    }
//	    return (float)(halfDiffX / (1.0f * (1.0D + this.mRenderer.getBarSpacing())));
//	  }

}

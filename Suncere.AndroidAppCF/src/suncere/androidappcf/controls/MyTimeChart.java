package suncere.androidappcf.controls;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.achartengine.chart.TimeChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

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
	}
	
	protected void drawXLabels(List<Double> xLabels, Double[] xTextLabelLocations, Canvas canvas, Paint paint, int left, int top, int bottom, double xPixelsPerUnit, double minX, double maxX)
	{
		float textSize=paint.getTextSize();
		int textColor=paint.getColor();
		paint.setColor(Color.argb(255,0,161,248));
		paint.setTextSize(   ScreenSuitableTool.ConvertSuitableDpi( 40));
		super.drawXTextLabels(xTextLabelLocations, canvas, paint, true, left, top, bottom, xPixelsPerUnit, minX, maxX);		
		paint.setTextSize(textSize);
		paint.setColor(textColor);
	}
	
	@Override
	protected void drawString(Canvas arg0, String arg1, float arg2, float arg3,
			Paint arg4) {
		
		///80是Y轴的X值 这个值估计在Text大小和或者Margin有变更时出问题
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

		paint.setTextSize( ScreenSuitableTool.ConvertSuitableDpi(  40));
		super.drawYLabels(allYLabels, canvas, paint, maxScaleNumber, left, right, bottom, yPixelsPerUnit, minY);
		
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

	
	
	protected void drawChartValuesText(android.graphics.Canvas canvas,
            XYSeries series,
            SimpleSeriesRenderer renderer,
            android.graphics.Paint paint,
            java.util.List<java.lang.Float> points,
            int seriesIndex,
            int startIndex)
	{
		float textSize=paint.getTextSize();
		paint.setTextSize(   ScreenSuitableTool.ConvertSuitableDpi(  40));
		renderer.setDisplayChartValuesDistance(1);
		
		//List<Integer> removeLst=new ArrayList<Integer>();
		boolean zebra,zero;
		int offset=series.getItemCount()%2;
		List<Float> nPoints=new ArrayList<Float>(points.size());
		for(int i=0;i+startIndex<series.getItemCount()&&i*2+1<points.size();i++)
		{
			zebra=false;
			zero=false;
			//去除值为0的ValesText
			if(this.needHideZero&& series.getY(i+startIndex)==this.view.minY)
			{
//				//更改坐标从而使得值不呈现界面
//				points.set(i*2+1, points.get(i*2+1)*100);
//				removeLst.add(i);
				zero=true;
			}
			if(this.needZebraText&&i%2==offset)
			{
				zebra=true;
//				removeLst.add(i);
			}
			
			
			if(zero)
			{
				points.set(i*2+1, points.get(i*2+1)*100);
//				nPoints.add(Float.MAX_VALUE);
//				nPoints.add(Float.MAX_VALUE);
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
			if(zebra)
			{
//				nPoints.add(Float.MAX_VALUE);
//				nPoints.add(Float.MAX_VALUE);
				points.set(i*2+1, points.get(i*2+1)*100);
			}
			else
			{
				nPoints.add(points.get(i*2));
				nPoints.add(points.get(i*2+1));
				
			}
			
		}
		
		
		canvas.translate(0,  ScreenSuitableTool.ConvertSuitableDpi(  - 30));
		super.drawChartValuesText(canvas, series, renderer, paint, points, seriesIndex, startIndex);
		canvas.translate(0, ScreenSuitableTool.ConvertSuitableDpi( 30));
		paint.setTextSize(textSize);
//		super.drawChartValuesText(canvas, series, renderer, paint, points, seriesIndex, startIndex);
	}

}

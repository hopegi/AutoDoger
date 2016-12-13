package suncere.androidappcf.controls;

import org.achartengine.chart.BarChart;

import java.util.List;
import java.util.Map;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import suncere.androidappcf.R;
import suncere.androidappcf.tools.ScreenSuitableTool;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

class MyBarChart  extends BarChart
{

	float labelTextSize;
	Resources resources;
	XYMultipleSeriesDataset dataset;
	MyBarChartView view;
//	private boolean needHideZero;
//	
//	public void setNeedHideZero(boolean needHideZero)
//	{
//		this.needHideZero=needHideZero;
//	}
	
	public MyBarChart(XYMultipleSeriesDataset dataset,
			XYMultipleSeriesRenderer renderer, Type type,Resources res,MyBarChartView view) {
		super(dataset, renderer, type);
		labelTextSize=renderer.getLabelsTextSize();
		this.resources=res;
		this.dataset=dataset;
		this.view=view;
		
	}

	protected void drawXLabels(List<Double> xLabels, Double[] xTextLabelLocations, Canvas canvas, Paint paint, int left, int top, int bottom, double xPixelsPerUnit, double minX, double maxX)
	{
		float textSize=paint.getTextSize();
		int textColor=paint.getColor();
		paint.setColor(Color.WHITE);
		paint.setTextSize( ScreenSuitableTool.ConvertSuitableDpi( 32) );
		super.drawXTextLabels(xTextLabelLocations, canvas, paint, true, left, top, bottom, xPixelsPerUnit, minX, maxX);		
		paint.setTextSize(textSize);
		paint.setColor(textColor);
	}
	
	protected void drawChartValuesText(Canvas canvas,
            XYSeries series,
            SimpleSeriesRenderer renderer,
            Paint paint,
            List<Float> points,
            int seriesIndex,
            int startIndex)
	{
		float textSize=paint.getTextSize();
		int textColor=paint.getColor();

		if(view.isShowYValue())
		{	
//			paint.setColor(Color.BLACK);
			paint.setColor(this.view.getValueTextColor());
			paint.setTextSize(ScreenSuitableTool.ConvertSuitableDpi(40));
			renderer.setDisplayChartValuesDistance(1);
			for(int i=0;i+startIndex<series.getItemCount()&&i*2+1<points.size();i++)
			{
				if(this.view.getHideZero() && series.getY(i+startIndex)==this.view.minY)
				{
	//				//��������Ӷ�ʹ��ֵ�����ֽ���
	//				points.set(i*2+1, points.get(i*2+1)*100);
	//				removeLst.add(i);
	
					points.set(i*2+1, points.get(i*2+1)*100);
				}
			}
			super.drawChartValuesText(canvas, series, renderer, paint, points, seriesIndex, startIndex);
//			super.drawChartValuesText(canvas, series, renderer, paint, points, seriesIndex, startIndex);
			paint.setTextSize(textSize);
			paint.setColor(textColor);
		}
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
		
		super.drawYLabels(allYLabels, canvas, paint, maxScaleNumber, left, right, bottom, yPixelsPerUnit, minY);
		if(view.isShowAQIColorY)
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
}
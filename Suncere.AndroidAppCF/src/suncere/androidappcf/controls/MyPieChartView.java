package suncere.androidappcf.controls;

import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import suncere.androidappcf.aqi.AQITool;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class MyPieChartView  extends LinearLayout{
	
	private DefaultRenderer renderer;
	private CategorySeries dataset;
	
	///Series的颜色
	private int[] seriesColor;
	///是否显示0值
	private boolean showZero;
	
	public void setShowZero(boolean showZero)
	{
		this.showZero=showZero;
		
	}
	
	public void setSeriesColor(int[] seriesColor)
	{
		this.seriesColor=seriesColor;
		
		this.CreateChart(this.getContext());
	}
	
	public MyPieChartView(Context context)
	{
		this(context, null);
	}

	public MyPieChartView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		CreateChart(context);
	}
	
	private void CreateChart(Context context)
	{
		
	}
	
	protected DefaultRenderer buildCategoryRenderer(double[] values)
	{
		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setScale(0.7f);
		renderer.setShowCustomTextGrid(false);
	    renderer.setShowLegend(false);
		renderer.setShowLabels(true);

		//renderer.setZoomRate(0.1f);
		renderer.setLegendTextSize(0.0f);//设置左下角表注的文字大小
//		renderer.setZoomButtonsVisible(true);
	    renderer.setZoomEnabled(true);
		renderer.setLegendHeight(40);
		//renderer.setChartTitleTextSize(40);//设置图表标题的文字大小
		//			renderer.setChartTitle("统计结果");//设置图表的标题  默认是居中顶部显示
		renderer.setLabelsTextSize(50.0f);//饼图上标记文字的字体大小
		renderer.setLabelsColor(Color.TRANSPARENT);//饼图上标记文字的颜色
		renderer.setPanEnabled(false);//设置是否可以平移
//		renderer.setDisplayValues(false);//是否显示值
		renderer.setClickEnabled(true);//设置是否可以被点击
		//renderer.setMargins(new int[] { 200, 300, 150, 0 });
		//margins - an array containing the margin size values, in this order: top, left, bottom, right
//		for (Integer color : colors)
//		{
//			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
//			r.setColor(color.intValue());
//			renderer.addSeriesRenderer(r);
//		}
		
		if(seriesColor==null||seriesColor.length==0)
		{
			seriesColor=new int[]{Color.argb(255, 0,228,0),Color.argb(255, 255,255,0),Color.argb(255, 255,126,0),
					Color.argb(255, 255,0,0),Color.argb(255, 153,0,76),Color.argb(255,126,0,35),
					Color.argb(255,169,169,169)};
		}
		SimpleSeriesRenderer r ;
		for(int color : seriesColor)
		{
			r=new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}
}

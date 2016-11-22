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
	
	///Series����ɫ
	private int[] seriesColor;
	///�Ƿ���ʾ0ֵ
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
		renderer.setLegendTextSize(0.0f);//�������½Ǳ�ע�����ִ�С
//		renderer.setZoomButtonsVisible(true);
	    renderer.setZoomEnabled(true);
		renderer.setLegendHeight(40);
		//renderer.setChartTitleTextSize(40);//����ͼ���������ִ�С
		//			renderer.setChartTitle("ͳ�ƽ��");//����ͼ��ı���  Ĭ���Ǿ��ж�����ʾ
		renderer.setLabelsTextSize(50.0f);//��ͼ�ϱ�����ֵ������С
		renderer.setLabelsColor(Color.TRANSPARENT);//��ͼ�ϱ�����ֵ���ɫ
		renderer.setPanEnabled(false);//�����Ƿ����ƽ��
//		renderer.setDisplayValues(false);//�Ƿ���ʾֵ
		renderer.setClickEnabled(true);//�����Ƿ���Ա����
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

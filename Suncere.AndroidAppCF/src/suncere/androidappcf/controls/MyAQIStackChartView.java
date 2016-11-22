package suncere.androidappcf.controls;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import suncere.androidappcf.R.color;
import suncere.androidappcf.tools.ScreenSuitableTool;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class MyAQIStackChartView  extends MyBarChartView 
{	 
	

	public MyAQIStackChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
//		CreateChart(context);
	}

	public MyAQIStackChartView(Context context) {
		super(context);
//		CreateChart(context);
	}

	
	protected void CreateChart(Context context)
	{
		//对字段赋初值
		this.maxY=100;
		this.minY=0;
		this.barColor=-1;
		this.isShowAQIColorY=false;
		this.isShowYAutoRange=true;
		this.isStartToRight=false;
		this.showValueCount=7;
		this.isShowYValue=false;
		
		result=null;
		serrisLst=new ArrayList<XYSeries>();
//		String[] titles = new String[] { "AQI_1g","AQI_2g","AQI_3g","AQI_4g","AQI_5g","AQI_6g","Custom"};
		String[] titles = new String[] { "AQI_6g","AQI_5g","AQI_4g","AQI_3g","AQI_2g","AQI_1g","Custom"};
	    List<double[]> x = new ArrayList<double[]>();
	    for (int i = 0; i < titles.length; i++) {
	      x.add(new double[] { -2 });
	    }
		
		
	    List<double[]> values = new ArrayList<double[]>();
	    for (int i = 0; i < titles.length; i++) {
	    	values.add(new double[] { -1 });
//	    	values.add(new double[] { 12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2,
//	    	        13.9 });
	    }
//	    int[] colors = new int[] { 
//	    		this.getResources().getColor( color.aqi_1g),};
	    colors = new int[] { 
	    		this.getResources().getColor( color.aqi_6g),
	    		this.getResources().getColor( color.aqi_5g),
	    		this.getResources().getColor( color.aqi_4g),
	    		this.getResources().getColor( color.aqi_3g),
	    		this.getResources().getColor( color.aqi_2g),
	    		this.getResources().getColor( color.aqi_1g),
	    		Color.BLUE};
//	    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE };
	    styles = new PointStyle[] { PointStyle.CIRCLE,PointStyle.CIRCLE,PointStyle.CIRCLE,PointStyle.CIRCLE,PointStyle.CIRCLE,PointStyle.CIRCLE,PointStyle.CIRCLE };
	    renderer = buildRenderer(colors, styles);
	    int length = renderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
	      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
	    }
	    setChartSettings(renderer, "", "", "", -1, 7, 0, 500,////TODO
	        Color.LTGRAY, Color.GREEN);
	    renderer.setXLabels(12);
	    renderer.setYLabels(10);
	    renderer.setShowGrid(true);
	    renderer.setXLabelsAlign(Align.CENTER);
	    renderer.setYLabelsAlign(Align.RIGHT);
	    renderer.setPanLimits(new double[] { -1, 24, 0, 500 });
	    renderer.setZoomLimits(new double[] { -10, 24, 0, 500 });
	    
	    renderer.setInScroll(true);
	    renderer.setPanEnabled(true, false);
	    renderer.setApplyBackgroundColor(true);
	    renderer.setMarginsColor(Color.argb(0,255, 255, 255));
	    renderer.setZoomEnabled(true);
	    renderer.setExternalZoomEnabled(false);
	    renderer.setZoomButtonsVisible(false);
	    renderer.setShowLegend(false);
	    renderer.setLabelsTextSize(20);
	    renderer.setShowGridY(false);
	    
	    //renderer.setBarSpacing(0.5f);
	    renderer.setBarWidth( ScreenSuitableTool.ConvertSuitableDpi( 40));
	    renderer.setDisplayChartValues(false);

	    int seriesRendererLength = renderer.getSeriesRendererCount();
	    for (int i = 0; i < seriesRendererLength; i++) {
	      SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(i);
	      seriesRenderer.setDisplayChartValues(true);
	    }
	    
	    
	    MyBarChart chart=new MyBarChart(buildDataset(titles, x, values),
	    		renderer,BarChart.Type.STACKED,this.getResources(),this);

	    result= new  GraphicalView(context, chart);
		result.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View view, android.view.MotionEvent event) {
				if(event.getPointerCount()>1)
				return true;
				else return false;
			}
		});
	    
	    this.addView(result);
	}

	//Object[]序列说明 0:x 1:aqi_1g 2:aqi_2g .....
	  public void BindDatas(List<Object[]> datas)
	  {
		  renderer.clearXTextLabels();
		  
//		  series.clear();
		  for(int i=0;i<serrisLst.size();i++)
			this.serrisLst.get(i).clear();
		  
		  double value;
//		  double min=this.maxY;
//		  double max=this.minY;
		 double min=0;
		 double max=100;
		  
		  int xValue=0;
		  for(int i=0;i<datas.size();i++)
		  {
			  //按照起始是否最左端决定x值
			  if(this.isStartToRight)
				  xValue=-datas.size()+i+this.showValueCount;
			  else
				  xValue=i;
			  
			  if(i%2!=0)
				  renderer.addXTextLabel(xValue, datas.get(i)[0].toString());
			  
			  //value= Double.parseDouble(datas.get(i)[1].toString());
			  value=0;
			  
			  Object[] stack=datas.get(i);
			  for(int si=1;si<stack.length;si++)
			  {
				  if(stack[si]==null)continue;
				  value+=Double.parseDouble(stack[si].toString());
//				  if(si<7)
//					  this.serrisLst.get( 6- si  ).add(xValue, value);
			  }
			  double sum=0;
			  //double curVal=0;
			  for(int si=1;si<stack.length;si++)
			  {
				  if(stack[si]==null)continue;
				  //curVal=Double.parseDouble(stack[si].toString())/value*100;
				  sum+=Double.parseDouble(stack[si].toString())/value*100;
				  if(si<7)
					  this.serrisLst.get( 6- si  ).add(xValue, sum);
			  }
			  
//			  ///通过条带状颜色来判别要采取哪种添加示例的方式
//			  if(this.barColor==-1)
//			  {
//				  if(value<=50)
//					  this.serrisLst.get(0).add(xValue, value);
//				  else if(value>50&&value<=100)
//					  this.serrisLst.get(1).add(xValue, value);
//				  else if(value>100&&value<=150)
//					  this.serrisLst.get(2).add(xValue, value);
//				  else if(value>150&&value<=200)
//					  this.serrisLst.get(3).add(xValue, value);
//				  else if(value>200&&value<=300)
//					  this.serrisLst.get(4).add(xValue, value);
//				  else if(value>300&&value<=500)
//					  this.serrisLst.get(5).add(xValue, value);
//			  }
//			  else
//			  {
//				  this.serrisLst.get(6).add(xValue, value);
//			  }
				  
//			  if(min>value)
//				  min=value;
//			  if(max<value)
//				  max=value;
		  }
		  
		  ///按是否自动缩放Y轴显示区间来确定Y轴显示的边缘值
//		  if(this.isShowYAutoRange())
//		  {
//		  
//			  if(max*1.3<this.maxY)
//				  max*=1.3;
//			  else
//				  max=this.maxY;
//			  min*=0.7;
//		  }
//		  else
//		  {
//			  max=this.maxY;
//			  min=this.minY;
//		  }
		  if(!this.isStartToRight){
		    renderer.setPanLimits(new double[] { -1, datas.size(), min, max });
		    renderer.setZoomLimits(new double[] { -10, datas.size(), min, min });
		  }
		  else
		  {
			  renderer.setPanLimits(new double[] { -datas.size()+showValueCount-1, showValueCount, min, max });
			    renderer.setZoomLimits(new double[] { -10, datas.size(), min, min });
		  }
		    setChartSettings(renderer, "", "", "", 1, showValueCount, min, max,
			        Color.BLACK, Color.argb(255,0,161,248));
		  result.invalidate();
		  
	  }
}

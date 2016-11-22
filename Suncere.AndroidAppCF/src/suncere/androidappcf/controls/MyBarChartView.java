package suncere.androidappcf.controls;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class MyBarChartView  extends LinearLayout 
{
	protected XYMultipleSeriesRenderer renderer ;
	protected XYSeries series;
	protected List<XYSeries> serrisLst;
	protected View result;
	protected  XYMultipleSeriesDataset dataset;
	protected int[] colors;//条带颜色数组
	protected PointStyle[] styles;//形状数组，目前是统一一种
	private  MyBarChart chart;
	 
	 ///AQIY轴
	protected boolean isShowAQIColorY;

	///是否动态调整Y轴范围，若自动调整，AQIY轴则不会显示
	protected boolean isShowYAutoRange;
	 
	 ///Y最大值
	protected int maxY;
	 
	 ///Y最小值
	protected int minY;
	 
	 ///条带颜色，若为-1则是条带按AQI颜色显示
	protected int barColor;
	 
	 ///在图表中显示的值的个数
	protected int showValueCount;
	 
	 ///起始在最右边
	protected boolean isStartToRight;
	 
	///是否显示Y值在每个柱状条上
	protected boolean isShowYValue;
	
	///图表值的Color
	protected int valueTextColor;

	///是否显示0值的点
	private boolean isHideZero;
	
	 ///暂未开放条带宽度和间距的属性
	 
	 
	public boolean isShowYValue() {
		return isShowYValue;
	}

	public void setShowYValue(boolean isShowYValue) {
		this.isShowYValue = isShowYValue;
	}

	public int getShowValueCount() {
		return showValueCount;
	}

	public void setShowValueCount(int showValueCount) {
		this.showValueCount = showValueCount;
	}

	public boolean isStartToRight() {
		return isStartToRight;
	}

	public void setStartToRight(boolean isStartToRight) {
		this.isStartToRight = isStartToRight;
	}

	public boolean isHideZero() {
		return isHideZero;
	}

	public void setHideZero(boolean isHideZero) {
		this.isHideZero = isHideZero;
//		if(this.chart!=null)this.chart.setNeedHideZero(this.isHideZero);
	}
	
	public  boolean getHideZero()
	{
		return this.isHideZero;
	}

	public MyBarChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		CreateChart(context);
	}

	 public int getValueTextColor() {
		return valueTextColor;
	}

	public void setValueTextColor(int valueTextColor) {
		this.valueTextColor = valueTextColor;
	}

	public MyBarChartView(Context context) {
		super(context);
		CreateChart(context);
	}


	
	 public boolean isShowAQIColorY() {
		return isShowAQIColorY&&!isShowYAutoRange;
	}

	public void setShowAQIColorY(boolean isShowAQIColorY) {
		this.isShowAQIColorY = isShowAQIColorY;
	}

	public boolean isShowYAutoRange() {
		return isShowYAutoRange;
	}

	public void setShowYAutoRange(boolean isShowYAutoRange) {
		this.isShowYAutoRange = isShowYAutoRange;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public int getBarColor() {
		return barColor;
	}

	public void setBarColor(int barColor) {
		this.barColor = barColor;
		if(barColor!=-1)
		{
			this.CreateChart(this.getContext());
//			colors[6]=this.barColor;
		}
	}
	
	protected void CreateChart(Context context)
	{
		//对字段赋初值
		this.maxY=500;
		this.minY=0;
		
		if(this.barColor==0)
			this.barColor=-1;
		
		this.isShowAQIColorY=false;
		this.isShowYAutoRange=true;
		this.isStartToRight=false;
		this.showValueCount=7;
		this.isShowYValue=true;
		
		result=null;
		serrisLst=new ArrayList<XYSeries>();
		String[] titles = new String[] { "AQI_1g","AQI_2g","AQI_3g","AQI_4g","AQI_5g","AQI_6g","Custom"};
//		String[] titles = new String[] { "AQI_6g","AQI_5g","AQI_4g","AQI_3g","AQI_2g","AQI_1g","Custom"};
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
	    		this.getResources().getColor( color.aqi_1g),
	    		this.getResources().getColor( color.aqi_2g),
	    		this.getResources().getColor( color.aqi_3g),
	    		this.getResources().getColor( color.aqi_4g),
	    		this.getResources().getColor( color.aqi_5g),
	    		this.getResources().getColor( color.aqi_6g),
	    		this.barColor};
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
	    renderer.setDisplayChartValues(true);
	    
	    int seriesRendererLength = renderer.getSeriesRendererCount();
	    for (int i = 0; i < seriesRendererLength; i++) {
	      SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(i);
	      seriesRenderer.setDisplayChartValues(true);
	    }
	    
	    
	   chart=new MyBarChart(buildDataset(titles, x, values),
	    		renderer,BarChart.Type.STACKED,this.getResources(),this);
	    
	    result= new  GraphicalView(context, chart);
		result.setOnTouchListener(new OnTouchListener() {
			
			double preX,preY;
			@Override
			public boolean onTouch(View view, android.view.MotionEvent event) {
				if(event.getPointerCount()>1)
				return true;
			 return false;
			}
		});
	    this.removeAllViews();
	    this.addView(result);
	}
	
	  protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
		    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		    setRenderer(renderer, colors, styles);
		    return renderer;
		  }
	
	  protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
		    renderer.setAxisTitleTextSize(16);
		    renderer.setChartTitleTextSize(20);
		    renderer.setLabelsTextSize(15);
		    renderer.setLegendTextSize(15);
		    renderer.setPointSize(ScreenSuitableTool.ConvertSuitableDpi( 20) );
		    renderer.setMargins(new int[] {0,0, 15, 20 });
//		    renderer.setMargins(new int[] { (int) (30f/480*AppParameters.ScreenDensityDpi),  (int) (80f/480*AppParameters.ScreenDensityDpi), 15, 20 });
		    int length = colors.length;
		    for (int i = 0; i < length; i++) {
		      XYSeriesRenderer r = new XYSeriesRenderer();
		      r.setColor(colors[i]);
		      r.setPointStyle(styles[i]);
		      renderer.addSeriesRenderer(r);
		    }
		  }
	  
	  protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
		      String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
		      int labelsColor) {
		    renderer.setChartTitle(title);
		    renderer.setXTitle(xTitle);
		    renderer.setYTitle(yTitle);
		    renderer.setXAxisMin(xMin);
		    renderer.setXAxisMax(xMax);
		    renderer.setYAxisMin(yMin);
		    renderer.setYAxisMax(yMax);
		    renderer.setAxesColor(axesColor);
		    renderer.setLabelsColor(labelsColor);
		  }

	  protected XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,
		      List<double[]> yValues) {
		    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		    addXYSeries(dataset, titles, xValues, yValues, 0);
		    return dataset;
		  }
		
	  public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,
			      List<double[]> yValues, int scale) {
			    int length = titles.length;
			    
			    for (int i = 0; i < length; i++) {
			      series = new XYSeries(titles[i], scale);
			      this.serrisLst.add(series);
			      double[] xV = xValues.get(i);
			      double[] yV = yValues.get(i);
			      int seriesLength = xV.length;
			      for (int k = 0; k < seriesLength; k++) {
			        series.add(xV[k], yV[k]);
			      }
			      dataset.addSeries(series);
			    }
			  }

	  public void BindData(List<HashMap<String,Object>> datas)
	  {
		  List<Object[]> chartDatas=new ArrayList<Object[]>();
		  
		  for(HashMap<String,Object> item:datas)
		  {
			  ///x,y
			  chartDatas.add(new Object[]{   item.get("LabelXValue"),item.get("YValue")   });
		  }
		  this.BindDatas(chartDatas);
	  }
	  
	  public void BindDatas(List<Object[]> datas)
	  {
		  renderer.clearXTextLabels();
		  for(int j=0;j<4;j++)
		  {
			  for(int i=0;i<10;i++){
			    	renderer.addXTextLabel(i, "");
			    	this.serrisLst.get(j).add( i,500- i*(j+1)*10 );
			  }
		  }
		  
//		  series.clear();
		  if(datas.size()!=0)
		  for(int i=0;i<serrisLst.size();i++)
			this.serrisLst.get(i).clear();  
		  
		  
		  double value;
		  double min=this.maxY;
		  double max=this.minY;
		  if(datas.size()==0)
		  {
			  min=0;
			  max=500;
		  }
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
			  
			  value= Double.parseDouble(datas.get(i)[1].toString());
			  
			  ///通过条带状颜色来判别要采取哪种添加示例的方式
			  if(this.barColor==-1)
			  {
				  if(value==0&&this.isHideZero)
					  this.serrisLst.get(6).add(xValue, 0);
				  else
				  {
					  if( value<=50)
						  this.serrisLst.get(0).add(xValue, value);
					  else if(value>50&&value<=100)
						  this.serrisLst.get(1).add(xValue, value);
					  else if(value>100&&value<=150)
						  this.serrisLst.get(2).add(xValue, value);
					  else if(value>150&&value<=200)
						  this.serrisLst.get(3).add(xValue, value);
					  else if(value>200&&value<=300)
						  this.serrisLst.get(4).add(xValue, value);
					  else if(value>300&&value<=500)
						  this.serrisLst.get(5).add(xValue, value);
				  }
			  }
			  else
			  {
				  this.serrisLst.get(6).add(xValue, value);
			  }
				  
			  if(min>value)
				  min=value;
			  if(max<value)
				  max=value;
		  }
		  
		  ///按是否自动缩放Y轴显示区间来确定Y轴显示的边缘值
		  if(this.isShowYAutoRange())
		  {
		  
			  if(max*1.3<this.maxY)
				  max*=1.3;
			  else
				  max=this.maxY;
			  min*=0.7;
		  }
		  else
		  {
			  max=this.maxY;
			  min=this.minY;
		  }
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
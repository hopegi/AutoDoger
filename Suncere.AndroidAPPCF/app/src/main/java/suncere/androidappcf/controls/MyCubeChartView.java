
package suncere.androidappcf.controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.achartengine.GraphicalView;
import org.achartengine.chart.CubicLineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import suncere.androidappcf.tools.DateTimeTool;
import suncere.androidappcf.tools.ScreenSuitableTool;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyCubeChartView extends LinearLayout {

	XYMultipleSeriesRenderer renderer ;
	XYSeries series;
	View result;
	 XYMultipleSeriesDataset dataset;
	 MyTimeChart chart;
	 int[] colors ;//条带颜色数组
	 PointStyle[] styles;//形状数组，目前是统一一种
	 
	 ///AQIY轴
	 boolean isShowAQIColorY;

	///是否动态调整Y轴范围，若自动调整，AQIY轴则不会显示
	 boolean isShowYAutoRange;
	 
	 ///Y最大值
	 int maxY;
	 
	 ///Y最小值
	 int minY;
	 
	 ///条带颜色，若为-1则是条带按AQI颜色显示
	 int barColor;
	 
	 ///在图表中显示的值的个数
	 int showValueCount;
	 
	 ///起始在最右边
	 boolean isStartToRight;
	 
	 ///是否显示0值的点
	 boolean isHideZero;
	 
	 ///圆点使用AQI颜色
	 boolean AQIColorPoint;
	 
	 ///是否显示垂直标尺
	 boolean showYStaff=false;
	 
	 ///是否显示区域
	 boolean showArea;
	 
	 
	 boolean showLinePoint;
	 
	 int lineWidth;
	 
	 boolean HideY=false;
	 
	 boolean ShowGrid=true;
	
	 
	 int XColor=Color.BLACK;
	 int XLabelsColor=Color.WHITE;
	 int YLabelsColor=Color.WHITE;
	 int ChartValueTextColor;
	 
	 
	 public int getChartValueTextColor() {
		return ChartValueTextColor;
	}

	public void setChartValueTextColor(int chartValueTextColor) {
		ChartValueTextColor = chartValueTextColor;
	}

	public int getXLabelsColor() {
		return XLabelsColor;
	}

	public void setXLabelsColor(int xLabelsColor) {
		XLabelsColor = xLabelsColor;
	}

	public int getYLabelsColor() {
		return YLabelsColor;
	}

	public void setYLabelsColor(int yLabelsColor) {
		YLabelsColor = yLabelsColor;
	}

	public int getXColor() {
		return XColor;
	}

	public void setXColor(int xColor) {
		XColor = xColor;
	}

	public boolean isShowGrid() {
		return ShowGrid;
	}

	public void setShowGrid(boolean showGrid) {
		ShowGrid = showGrid;
	}

	public boolean isHideY() {
		return HideY;
	}

	public void setHideY(boolean hideY) {
		HideY = hideY;
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public boolean isShowLinePoint() {
		return showLinePoint;
	}

	public void setShowLinePoint(boolean showLinePoint) {
		this.showLinePoint = showLinePoint;
	}

	public boolean isShowYStaff() {
		return showYStaff;
	}

	public void setShowYStaff(boolean showYStaff) {
		this.showYStaff = showYStaff;
	}

	public boolean isShowArea() {
		return showArea;
	}

	public void setShowArea(boolean showArea) {
		this.showArea = showArea;
	}

	public boolean isAQIColorPoint() {
		return AQIColorPoint;
	}

	public void setAQIColorPoint(boolean aQIColorPoint) {
		AQIColorPoint = aQIColorPoint;
	}

	public boolean isHideZero() {
		return isHideZero;
	}

	public void setHideZero(boolean isHideZero) {
		this.isHideZero = isHideZero;
		if(this.chart!=null)this.chart.setNeedHideZero(this.isHideZero);
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

	public MyCubeChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		CreateChart(context);
	}

	public MyCubeChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		CreateChart(context);
	}

	public MyCubeChartView(Context context) {
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
		renderer = buildRenderer(colors, styles);
	}
	
	private void CreateChart(Context context)
	{
		result=null;
		//对字段赋初值
		this.maxY=Integer.MAX_VALUE;
		this.minY=0;
		this.barColor=-1;
		this.isShowAQIColorY=true;
		this.isShowYAutoRange=false;
		this.isStartToRight=false;
		this.showValueCount=7;
		this.showArea=false;
		this.showLinePoint=false;
		
		
		String[] titles = new String[] { "Crete" };
	    List<double[]> x = new ArrayList<double[]>();
	    for (int i = 0; i < titles.length; i++) {
	      x.add(new double[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 });
	    }
		
		
	    List<double[]> values = new ArrayList<double[]>();
	    values.add(new double[] { 120.3, 89.5, 167.8, 55.8, 68.4, 61.4, 90.4, 48.1, 87.6, 53.3, 33.2,13.9 });
	    colors = new int[] { Color.WHITE};
	    styles = new PointStyle[] { PointStyle.CIRCLE };
	    renderer = buildRenderer(colors, styles);
	    int length = renderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
	      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
	    }
	    setChartSettings(renderer, "", "", "", -1, 7, 0, 500,////TODO
	        Color.LTGRAY, Color.WHITE);
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
	    renderer.setLabelsTextSize( ScreenSuitableTool.ConvertSuitableDpi( 30) );
	    renderer.setDisplayChartValues(true);
	   
	    
	    //XY轴上的字体颜色
	    renderer.setXLabelsColor(Color.WHITE);
	    renderer.setYLabelsColor(0,Color.WHITE);
	    
//	    renderer.setBarWidth(8f);
//	    renderer.setBarSpacing(20f);
	    
	    chart=new MyTimeChart(buildDataset(titles, x, values),
	    		renderer,this.getResources(),this);
	    chart.setNeedHideZero(this.isHideZero);
	    result= new  GraphicalView(context, chart);
		result.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if(event.getPointerCount()>1)
				return true;
				else return false;
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
		  //PointSize设置为0，让所有点的PointStyle手动描绘，从而使得0不会绘制
		    renderer.setPointSize(0);
		    
		    //隐藏掉Y轴
		    if (isHideY()) {
		    	  
		    	 renderer.setMargins(new int[] { (int) (ScreenSuitableTool.ConvertSuitableDpi( -30)),  (int) (ScreenSuitableTool.ConvertSuitableDpi( -20)), 15, 20 });
			}else {
//				 Log.e("hjo", "不隐藏Y轴！");
				 renderer.setMargins(new int[] { (int) (ScreenSuitableTool.ConvertSuitableDpi( 30)),  (int) (ScreenSuitableTool.ConvertSuitableDpi(  80)), 15, 20 });
			}
		    
//		    renderer.setMargins(new int[] { (int) (ScreenSuitableTool.ConvertSuitableDpi( 30)),  (int) (ScreenSuitableTool.ConvertSuitableDpi(  -20)), 15, 20 });
		    
		    int length = colors.length;
		    for (int i = 0; i < length; i++) {
		      XYSeriesRenderer r = new XYSeriesRenderer();
		      r.setColor(colors[i]);//colors[i]
		      r.setPointStyle(PointStyle.DIAMOND);//styles[i]
		      r.setLineWidth(4f);//画线的大小
//		      if(this.isShowArea())
//		      {
//		    	  r.setFillBelowLine(true); // 确定填充    
//		    	  r.setFillBelowLineColor(Color.argb(50, 255, 255, 255));
//		    	  r.setLineWidth(20f);
//		      }
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

	  
	  protected XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues, List<double[]> yValues) {
		    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		    addXYSeries(dataset, titles, xValues, yValues, 0);
		    return dataset;
		  }
		
	  public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,
			      List<double[]> yValues, int scale) {
			    int length = titles.length;
			    for (int i = 0; i < length; i++) {
			      series = new XYSeries(titles[i], scale);
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
		 
		  renderer.setAxesColor(getXColor());
		  
	
		  renderer.clearXTextLabels();
//		  for(int i=0;i<30;i++)
//		    	renderer.addXTextLabel(i, "");
		  
		  series.clear();
		  
		  double value;
		  double min=this.maxY;
		  double max=this.minY;
		  int xValue=0;
		  int maxLen=0,currLen;
		  double cur;
		  String valStr;
		  
//		  List<Double> aaaaaa=new ArrayList<Double>();
			
		
		  for(int i=0;i<datas.size();i++)
		  {
			//按照起始是否最左端决定x值
			  if(this.isStartToRight)
				  xValue=-datas.size()+i+this.showValueCount;
			  else
				  xValue=i;
			  value= setNAValue(datas.get(i)[1].toString());
			  renderer.addXTextLabel(xValue, datas.get(i)[0].toString());
			  series.add(xValue, setNAValue(datas.get(i)[1].toString()));
			  
			  if(min>value)
				  min=value;
			  if(max<value)
				  max=value;
			  
			  valStr=datas.get(i)[1].toString();
			  cur=setNAValue(valStr);
			//找出长度最大的值设置让value间隔显示
			  if( Math.abs( cur-Math.round(cur))<=0.000000001)
			  {
				  currLen=valStr.indexOf('.')+1;
			  }
			  else
			  {
				  currLen=valStr.length();
			  }
			  if(maxLen<currLen)
				  maxLen=currLen;
		  }
		  //确定是否使用斑马数
		  chart.setNeedZebraText(maxLen>4);
		  
		  ///按是否自动缩放Y轴显示区间来确定Y轴显示的边缘值
		  if(this.isShowYAutoRange())
		  {
		  
//			  Log.e("hjo", "默认的数据是不调用这里的 !");
//			  Toast.makeText(getContext(), "进入到isShowYAutoRange"+this.isShowYAutoRange(), Toast.LENGTH_LONG).show();
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
		  
		  if (max==0) {
			  max=20;
		}
		  
		  ///设置显示区域
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
	  
	  
	  @Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			// TODO Auto-generated method stub
		//滑动处理
			 float mX = 0;
			 float mY = 0;
			 switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mX=ev.getX();
				mY=ev.getY();
				getParent().requestDisallowInterceptTouchEvent(true);//不拦截
				break;
			case MotionEvent.ACTION_MOVE:
				if (Math.abs(ev.getX()-mX)> Math.abs(ev.getY()-mY)) {//如果横向滑动大于纵向滑动
					getParent().requestDisallowInterceptTouchEvent(true);
				}else {
					getParent().requestDisallowInterceptTouchEvent(false);//交给父类处理
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				getParent().requestDisallowInterceptTouchEvent(false);
				break;
			
			default:
				break;
			}
			return super.dispatchTouchEvent(ev);
		}
	  
	  
	  //判断
	  public double setNAValue(Object value){
		  if (value.equals("—")) {
			return 0.0;
		}else if (value.equals("NA")) {
			return 0.0;
		} else {
			return Double.valueOf((String) value);
		}
//		  double   d = 0;
//		  try {
//			d =  Double.valueOf((String) value);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		  return d;
	  }
	  
	  
	  
}
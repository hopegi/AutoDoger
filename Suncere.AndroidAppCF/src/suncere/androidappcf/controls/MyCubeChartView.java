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

public class MyCubeChartView extends LinearLayout {

	XYMultipleSeriesRenderer renderer ;
	XYSeries series;
	View result;
	 XYMultipleSeriesDataset dataset;
	 MyTimeChart chart;
	 int[] colors ;//������ɫ����
	 PointStyle[] styles;//��״���飬Ŀǰ��ͳһһ��
	 
	 ///AQIY��
	 boolean isShowAQIColorY;

	///�Ƿ�̬����Y�᷶Χ�����Զ�������AQIY���򲻻���ʾ
	 boolean isShowYAutoRange;
	 
	 ///Y���ֵ
	 int maxY;
	 
	 ///Y��Сֵ
	 int minY;
	 
	 ///������ɫ����Ϊ-1����������AQI��ɫ��ʾ
	 int barColor;
	 
	 ///��ͼ������ʾ��ֵ�ĸ���
	 int showValueCount;
	 
	 ///��ʼ�����ұ�
	 boolean isStartToRight;
	 
	 ///�Ƿ���ʾ0ֵ�ĵ�
	 boolean isHideZero;
	 
	 ///Բ��ʹ��AQI��ɫ
	 boolean AQIColorPoint;
	 
	 ///�Ƿ���ʾ��ֱ���
	 boolean showYStaff=false;
	 
	 ///�Ƿ���ʾ����
	 boolean showArea=false;
	 
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
		//���ֶθ���ֵ
		this.maxY=500;
		this.minY=0;
		this.barColor=-1;
		this.isShowAQIColorY=true;
		this.isShowYAutoRange=false;
		this.isStartToRight=false;
		this.showValueCount=7;
		
		
		String[] titles = new String[] { "Crete" };
	    List<double[]> x = new ArrayList<double[]>();
	    for (int i = 0; i < titles.length; i++) {
	      x.add(new double[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 });
	    }
		
		
	    List<double[]> values = new ArrayList<double[]>();
	    values.add(new double[] { 120.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2,
	        13.9 });
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
//	    for(int i=0;i<30;i++)
//	    	renderer.addTextLabel(i, "Text"+i);
	    
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
		  //PointSize����Ϊ0�������е��PointStyle�ֶ���棬�Ӷ�ʹ��0�������
		    renderer.setPointSize(0);
		    renderer.setMargins(new int[] { (int) (ScreenSuitableTool.ConvertSuitableDpi( 30)),  (int) (ScreenSuitableTool.ConvertSuitableDpi(  80)), 15, 20 });
		    int length = colors.length;
		    for (int i = 0; i < length; i++) {
		      XYSeriesRenderer r = new XYSeriesRenderer();
		      r.setColor(colors[i]);
		      r.setPointStyle(styles[i]);
		      
		      if(this.isShowArea())
		      {
		    	  r.setFillBelowLine(true); // ȷ�����    
		    	  r.setFillBelowLineColor(Color.argb(50, 255, 255, 255));
		    	  r.setLineWidth(2);
		      }
		      
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
		  for(int i=0;i<datas.size();i++)
		  {
			//������ʼ�Ƿ�����˾���xֵ
			  if(this.isStartToRight)
				  xValue=-datas.size()+i+this.showValueCount;
			  else
				  xValue=i;
			  
			  value= Double.parseDouble(datas.get(i)[1].toString());
			  Log.d("d", "Log Chart Value "+datas.get(i)[1].toString());
			  renderer.addXTextLabel(xValue, datas.get(i)[0].toString());
			  series.add(xValue, Double.parseDouble(datas.get(i)[1].toString()));
			  
			  if(min>value)
				  min=value;
			  if(max<value)
				  max=value;
			  
			  valStr=datas.get(i)[1].toString();
			  cur=Double.parseDouble(valStr);
			//�ҳ���������ֵ������value�����ʾ
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

		  //ȷ���Ƿ�ʹ�ð�����
		  chart.setNeedZebraText(maxLen>4);
		  
		  ///���Ƿ��Զ�����Y����ʾ������ȷ��Y����ʾ�ı�Եֵ
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
		  
		  ///������ʾ����
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
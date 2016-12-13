package suncere.androidappcf.controls;

import org.achartengine.chart.PieChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;


public class MyPieChart extends PieChart {

	CategorySeries dataset;
	DefaultRenderer renderer;
	MyPieChartView view;
	
	public MyPieChart(CategorySeries dataset, DefaultRenderer renderer,MyPieChartView view) {
		super(dataset, renderer);
		this.dataset = dataset;
		this.renderer = renderer;
		this.view = view;
	}

	protected void drawString(Canvas canvas, String arg1, float arg2,
			float arg3, Paint paint)
	{
		// TODO Auto-generated method stub
		super.drawString(canvas, arg1, arg2, arg3, paint);
		float size = paint.getTextSize();
		paint.setTextSize(50.0f);
		int index = 0;
	}
}

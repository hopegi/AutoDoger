package suncere.androidappcf.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

public class BaiduMapExHelper 
{
	 protected MapView mapView;
	 
	 protected BaiduMap mBaiduMap;
	
	 private List<OnMapClickListener> mapClickEventLst;
	 
	 private List<OnMarkerClickListener> markerClickEventLst;
	 
	 private OnBindingMarkValueHandler onBindingMarkValueHandler;

	public BaiduMapExHelper(MapView orgMapView )
	 {
		 mapView=orgMapView;
		 mapClickEventLst=new ArrayList<OnMapClickListener>();
		 markerClickEventLst=new ArrayList<OnMarkerClickListener>();
	 }
	 
	 public void BindMapClickEvent(OnMapClickListener listener)
	 {
		 this.mapClickEventLst.add(listener);
	 }
	 
	 public void BindMarkerClickListener(OnMarkerClickListener listener)
	 {
		 this.markerClickEventLst.add(listener);
	 }
	 
	 public void setOnBindingMarkValueHandler(
				OnBindingMarkValueHandler onBindingMarkValueHandler) {
			this.onBindingMarkValueHandler = onBindingMarkValueHandler;
		}
	 
	 private void InitMapView()
	 {
		 this.mBaiduMap=this.mapView.getMap();
		 this.mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		 this.mBaiduMap.setOnMapClickListener(new OnMapClickListener(){

			@Override
			public void onMapClick(LatLng arg0) {
				mBaiduMap.hideInfoWindow();
				for(OnMapClickListener event :mapClickEventLst)
				{
					event.onMapClick(arg0);
				}
			}

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				boolean eventRes;
				for(OnMapClickListener event :mapClickEventLst)
				{
					eventRes= event.onMapPoiClick(arg0);
					if(!eventRes)return eventRes;
				}
				return true;
			}});
		 
		 this.mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener(){

			@Override
			public boolean onMarkerClick(Marker arg0) {
				boolean eventRes;
				for(OnMarkerClickListener event: markerClickEventLst)
				{
					eventRes=event.onMarkerClick(arg0);
					if(!eventRes)return eventRes;
				}
				
				return true;
			}});
	 }

	 ///设置中心点
	 public void SetCenterPoint(double latitude,double longitude,int zoomSize)
	 {
		 LatLng centerPoint=new LatLng(latitude,longitude);
		 this.SetCenterPoint(centerPoint, zoomSize);
	 }
	 
	 ///设置中心点
	 public void SetCenterPoint(LatLng point,int zoomSize)
	 {
		 MapStatus mMapStatus = new MapStatus.Builder()
	        .target(point)
	        .zoom(zoomSize)
	        .build();

		  MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		  mBaiduMap.setMapStatus(mMapStatusUpdate);
	 }
	 
	//添加地图上的注标
	 public void AddImageMark(View view,HashMap<String,Object> datas, double latitude,double longitude)
	 {
		 BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(view);
		 if(this.onBindingMarkValueHandler!=null)
			 this.onBindingMarkValueHandler.OnBindingMarkValueEvent(view, datas);
		 LatLng point=new LatLng(latitude,longitude);
		 MarkerOptions ImgOption = new MarkerOptions()  
	    .position(point) 
	    .icon(bitmap); 
		Marker marker=(Marker) mBaiduMap.addOverlay(ImgOption);
		 Bundle bundle=new Bundle();
		 bundle.putSerializable("stationData", datas);
		 marker.setExtraInfo(bundle);
	 }
	 
	 //添加地图上的多个注标
	 public void AddImageMarks(View view,List<HashMap<String,Object>>dataSource,double... pointLocations)
	 {
		 
		 for(int i=0;i<dataSource.size();i++)
		 {
			 AddImageMark(view,dataSource.get(i),pointLocations[i],pointLocations[i+1]);
		 }
	 }
	 
	 ///
	 public interface OnBindingMarkValueHandler
	 {
		 void OnBindingMarkValueEvent(View view,HashMap<String,Object> datas);
	 }
}

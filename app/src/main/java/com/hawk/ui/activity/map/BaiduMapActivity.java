package com.hawk.ui.activity.map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.hawk.ui.activity.R;

/**
 * Created by heyong on 16/4/12.
 */
public class BaiduMapActivity extends AppCompatActivity {

    private Toolbar toolbar;
    // 定位相关
    private boolean isFirstLoc = true;// 是否首次定位
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker = null;
    private Marker mMarker;

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private TextView tvAdr;
    private BDLocation bd_location;

    BitmapDescriptor myIcon = BitmapDescriptorFactory
            .fromResource(R.drawable.my_gps);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvAdr = (TextView) findViewById(R.id.tvAdr);
        ImageView btnGoLoc = (ImageView) findViewById(R.id.btnGoLoc);
        btnGoLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bd_location==null)
                    return;
                LatLng ll = new LatLng(bd_location.getLatitude(),
                        bd_location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        });

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bdmapView);
        mBaiduMap = mMapView.getMap();

        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);//设置是否需要地址信息，默认不需要
//		option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(2000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getZIndex() == -1)
                    return true;
                else {

                }
                return true;
            }
        });
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            bd_location = location;

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            OverlayOptions ooA = new MarkerOptions().position(ll).icon(myIcon)
                    .zIndex(-1).draggable(true);
            if(mMarker!=null) {
                mMarker.remove();
            }
            mMarker = (Marker) (mBaiduMap.addOverlay(ooA));

            if (isFirstLoc) {
                isFirstLoc = false;
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, mBaiduMap.getMaxZoomLevel() - 5);
                mBaiduMap.animateMapStatus(u);
                tvAdr.setText(bd_location.getAddrStr());
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        myIcon.recycle();
        super.onDestroy();
    }
}

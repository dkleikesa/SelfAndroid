package com.ljz.qcmian.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.TileProjection;
import com.ljz.qcmian.utils.LogFactory;

public class AMapTileSystemChecker {

    private static final String TAG = "TileSystemChecker";
    private AMapTileSystem mAMapTileSystem = new AMapTileSystem();
    private AMap mAMap;
    private View mMapView;
    private Context mContext;
    private Handler mHandler;

    AMapTileSystemChecker(Context context, AMap map, View v) {
        mAMap = map;
        mContext = context;
        mMapView = v;
        mHandler = new Handler();
    }

    public void run() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
//                mapSizeCheck();
                zoomLevelCheck();
            }
        });
    }

    void mapSizeCheck() {
        for (int i = 3; i <= 20; i++) {
            TileProjection projection = mAMap.getProjection().fromBoundsToTile(new LatLngBounds(new LatLng(-85, -180), new LatLng(85, 179.9999999999d)), i, 256);//此方法有问题
            long x = (projection.maxX - projection.minX + 1) * 256;
            long y = (projection.maxY - projection.minY + 1) * 256;
            long size = mAMapTileSystem.mapSize(i);
            LogFactory.getLLog().d(TAG, "mapSizeCheck:" + " " + i + " amap " + x + " " + y);
            LogFactory.getLLog().d(TAG, "mapSizeCheck:" + " " + i + " self " + size + " " + size);
            //结论：两者地图大小相同 256*2^level
        }
    }

    /**
     * 高德zoom范围[3,19],有室内地图时是20
     * check原理是：同一zoom level下 像素距离对应经纬度差相同
     */
    public void zoomLevelCheck() {

        double wgsLat = 39.90733055555555;
        double wgsLng = 116.39118611111111;
        double centerLat = 39.908692;
        double centerLng = 116.397477;
        double ratioX = 1.1689814814814814;
        double ratioY = 1.1693548387096775;
        addMarker(mAMap, centerLat, centerLng);
        for (int i = 3; i <= 20; i++) {
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(centerLat, centerLng), i));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            double[] dCenter = CoordinateTransformUtil.gcj02towgs84(centerLng, centerLat);
            Point pCenter = mAMapTileSystem.latLongToPixelXY(dCenter[1], dCenter[0], i);
            int showW = mMapView.getWidth();
            int showH = mMapView.getHeight();
            Point leftTopPoint = new Point((int) (pCenter.x - ratioX * px2dip(mContext, showW) / 2), (int) (pCenter.y - ratioY * px2dip(mContext, showH) / 2));
            Point rightBottomPoint = new Point((int) (pCenter.x + ratioX * px2dip(mContext, showW) / 2), (int) (pCenter.y + ratioY * px2dip(mContext, showH) / 2));
            BaseTileSystem.LatLong leftTopwgs =mAMapTileSystem.pixelXYToLatLong(leftTopPoint.x,leftTopPoint.y,i);
            BaseTileSystem.LatLong rightBottomwgs =mAMapTileSystem.pixelXYToLatLong(rightBottomPoint.x,rightBottomPoint.y,i);
            double[] d1=CoordinateTransformUtil.wgs84togcj02(leftTopwgs.longitude,leftTopwgs.latitude);
            double[] d2=CoordinateTransformUtil.wgs84togcj02(rightBottomwgs.longitude,rightBottomwgs.latitude);



            Projection projection = mAMap.getProjection();


            LatLng leftTopLatLng = projection.fromScreenLocation(new Point(0,0));
            LatLng rightBottomLatLng = projection.fromScreenLocation(new Point(showW,showH));

            LogFactory.getLLog().d(TAG, "zoomLevelCheck:" + i + " wgs " + d1[1]+" "+d1[0] + " " + d2[1]+" " +d2[0]);
            LogFactory.getLLog().d(TAG, "zoomLevelCheck:" + i + " gcj " + leftTopLatLng.latitude+" "+leftTopLatLng.longitude + " " + rightBottomLatLng.latitude+" " +rightBottomLatLng.longitude);


//            Point p1 = new Point(10, 10);
//            Point p2 = new Point(110, 110);

//            LatLng latLng1 = projection.fromScreenLocation(p1);
//            LatLng latLng2 = projection.fromScreenLocation(p2);
//            LogFactory.getLLog().d(TAG, "zoomLevelCheck:" + " amap " + p1.x + " " + p1.y + " " + p2.x + " " + p2.y);
//
//            Point p3 = mAMapTileSystem.latLongToPixelXY(latLng1.latitude, latLng1.longitude, i);
//            Point p4 = mAMapTileSystem.latLongToPixelXY(latLng2.latitude, latLng2.longitude, i);
//            Point pCenter = mAMapTileSystem.latLongToPixelXY(centerLat, centerLng, i);
//
//            p3.x = showW / 2 + p3.x - pCenter.x;
//            p3.y = showH / 2 + p3.y - pCenter.y;
//            p4.x = showW / 2 + p4.x - pCenter.x;
//            p4.y = showH / 2 + p4.y - pCenter.y;
//
//            LogFactory.getLLog().d(TAG, "zoomLevelCheck:" + " self " + p3.x + " " + p3.y + " " + p4.x + " " + p4.y);

        }
    }

    public static void addMarker(AMap aMap, double lat, double lng) {
        int circleColor = 0xFFFF0000;
        Paint mFirstCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFirstCirclePaint.setColor(circleColor);
        mFirstCirclePaint.setAlpha(255);
        Bitmap bitmap = Bitmap.createBitmap(40, 40, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(20, 20, 20, mFirstCirclePaint);
        aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap)).position(new LatLng(lat, lng)).anchor(0.5f, 0.5f));
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}

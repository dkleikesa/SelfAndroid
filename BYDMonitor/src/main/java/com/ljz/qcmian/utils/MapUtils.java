//package com.ljz.qcmian.utils;
//
//import android.graphics.Rect;
//
//import com.amap.api.maps.model.LatLng;
//import com.amap.api.maps.model.LatLngBounds;
//import com.amap.api.maps.model.Marker;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//public class MapUtils {
//
//    public static List<LatLng> astLocation2LatlngList(List<AstLocation> dataList) {
//        List<LatLng> list = new ArrayList<>();
//        for (AstLocation location : dataList) {
//            if (location == null || !location.isValid()) {
//                continue;
//            }
//            list.add(new LatLng(location.latitude, location.longitude));
//        }
//        return list;
//    }
//
//    public static Rect getMarkerIconRect(Marker marker, int w, int h) {
//        if (marker == null) {
//            return null;
//        }
//        Rect rect = new Rect();
//        float anchorX = marker.getAnchorU();
//        float anchorY = marker.getAnchorV();
//        rect.top = (int) (h * anchorY);
//        rect.left = -(int) (w * anchorX);
//        rect.right = (int) (w * (1 - anchorX));
//        rect.bottom = -(int) (h * (1 - anchorY));
//        return rect;
//    }
//
//    public static void fitBoundsWithPixRect(LatLngBounds.Builder builder, int visibleWidth, int visibleHeight, Map<LatLng, Rect> map) {
//        if (map == null || map.size() <= 0) {
//            return;
//        }
//        Set<Map.Entry<LatLng, Rect>> entries = map.entrySet();
//        int times = 5;
//        for (int i = 0; i < times; i++) {
//            double[] ratio = latLngPrePixWithLatLngBounds(visibleWidth, visibleHeight, builder.build());
//            for (Map.Entry<LatLng, Rect> entry : entries) {
//                addRectToBounds(builder, entry.getKey(), entry.getValue(), ratio);
//            }
//        }
//    }
//
//    private static void addRectToBounds(LatLngBounds.Builder builder, LatLng center, Rect rect, double[] ratio) {
//        if (center == null) {
//            return;
//        }
//        builder.include(center);
//        LatLng addressDescTopLeft = new LatLng(center.latitude + rect.left * ratio[0],
//                center.longitude + rect.top * ratio[1]);
//        LatLng addressDescBottomRight = new LatLng(center.latitude + rect.right * ratio[0],
//                center.longitude + rect.bottom * ratio[1]);
//        builder.include(addressDescTopLeft);
//        builder.include(addressDescBottomRight);
//    }
//
//    private static double[] latLngPrePixWithLatLngBounds(int visibleWidth, int visibleHeight, LatLngBounds bounds) {
//        double[] latLngRatio = new double[2];
//        latLngRatio[0] = 0;
//        latLngRatio[1] = 0;
//        if ((bounds != null) && (bounds.northeast != null) && (bounds.southwest != null) &&
//                (visibleHeight > 0) && (visibleWidth > 0)) {
//
//            double dLat = Math.abs(bounds.southwest.latitude - bounds.northeast.latitude);
//            double dLng = Math.abs(bounds.southwest.longitude - bounds.northeast.longitude);
//            double vRatio = dLat / ((double) visibleHeight);  //单位像素对应的经纬度
//            double hRatio = dLng / ((double) visibleWidth);   //单位像素对应的经纬度
//
//            //地图上每个像素对应的距离是相同的，但是地图上纬度越高的地方，每一度代表的距离越小，
//            // 因此同一zoom级别下，每个像素对应的dLat会随着纬度而变化，而每个像素对应的dLng不会变。
//            // 其对应关系为：2*PI*R*(dLat/360) = 2*PI*R*(dLng/360)*cos(lat)
//            double h2v = hRatio * Math.cos(Math.toRadians(bounds.southwest.latitude)); //每个下经度和纬度之间有一个比例
//
//            double cos = Math.cos(Math.toRadians(bounds.southwest.latitude));
//            if (cos != 0) {
//                if (vRatio >= h2v) {  //纵向撑满
//                    latLngRatio[0] = vRatio;
//                    latLngRatio[1] = vRatio / cos;
//                } else { //横向撑满
//                    latLngRatio[0] = hRatio * cos;
//                    latLngRatio[1] = hRatio;
//                }
//            }
//        }
//        return latLngRatio;
//    }
//
//}

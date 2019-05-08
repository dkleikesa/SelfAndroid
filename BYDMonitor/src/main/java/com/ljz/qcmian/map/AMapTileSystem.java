package com.ljz.qcmian.map;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.LatLng;


public class AMapTileSystem extends BaseTileSystem {
    public AMapTileSystem() {
        defaultMapSize = 256;
    }

}

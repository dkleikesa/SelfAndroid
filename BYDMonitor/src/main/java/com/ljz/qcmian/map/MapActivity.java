package com.ljz.qcmian.map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Tile;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.TileProjection;
import com.amap.api.maps.model.TileProvider;
import com.ljz.qcmian.R;

import java.io.ByteArrayOutputStream;

public class MapActivity extends Activity {
    AMap aMap;
    MapView mMapView;
    AMapTileSystemChecker checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mMapView = findViewById(R.id.amap);
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        aMap.getUiSettings().setRotateGesturesEnabled(false);
        aMap.getUiSettings().setTiltGesturesEnabled(false);
        aMap.getUiSettings().setScaleControlsEnabled(true);
        checker = new AMapTileSystemChecker(this, aMap, mMapView);
        findViewById(R.id.start_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker.run();
            }
        });
        findViewById(R.id.start_show_tile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (overlay == null) {
                    showTile( 256 * (int)v.getContext().getResources().getDisplayMetrics().density, 256* (int)v.getContext().getResources().getDisplayMetrics().density);
                } else {
                    clearTile();
                }
            }
        });
    }

    TileOverlay overlay;

    void clearTile() {
        overlay.remove();
        overlay = null;
    }

    void showTile(final int width, final int height) {
        TileOverlayOptions options = new TileOverlayOptions().tileProvider(new TileProvider() {
            @Override
            public Tile getTile(int x, int y, int zoom) {

                Paint backGroundPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
                backGroundPainter.setColor(0x2474FF);
                backGroundPainter.setAlpha(100);

                Paint textPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
                textPainter.setColor(0x000000);
                textPainter.setAlpha(255);

                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawRect(new Rect(0, 0, width, height), backGroundPainter);
                float[] array = {0f, 0f, 0, height, 0, height, width, height, width, height, width, 0, width, 0, 0, 0};
                canvas.drawLines(array, textPainter);
                canvas.drawText(x + " " + y + " " + zoom, width / 3, height / 2, textPainter);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                return new Tile(width, height, outputStream.toByteArray());
            }

            @Override
            public int getTileWidth() {
                return width;
            }

            @Override
            public int getTileHeight() {
                return height;
            }
        });
        overlay = aMap.addTileOverlay(options);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }


}

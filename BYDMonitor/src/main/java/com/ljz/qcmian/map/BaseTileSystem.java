package com.ljz.qcmian.map;

import android.graphics.Point;

/**
 * 电子地图中的像素、经纬度、实际距离的计算原理代码
 * 参考:https://msdn.microsoft.com/en-us/library/bb259689.aspx
 */
public class BaseTileSystem {

    private static final double EARTH_RADIUS = 6378137; //地球半径
    private static final double MIN_LATITUDE = -85.05112878; //墨卡托投影的极值
    private static final double MAX_LATITUDE = 85.05112878; //墨卡托投影的极值
    private static final double MIN_LONGITUDE = -180; //墨卡托投影的极值
    private static final double MAX_LONGITUDE = 180;//墨卡托投影的极值

    protected long defaultMapSize = 256;

    /**
     * Clips a number to the specified minimum and maximum values.
     *
     * @param n        the number to clip.
     * @param minValue Minimum allowable value.
     * @param maxValue Maximum allowable value.
     * @return The clipped value.
     */
    private double clip(double n, double minValue, double maxValue) {
        return Math.min(Math.max(n, minValue), maxValue);
    }

    /**
     * Determines the map width and height (in pixels) at a specified level of detail.
     *
     * @param zoomLevel Level of detail, from 1 (lowest detail) to 23 (highest detail).
     * @return The map width and height in pixels.
     */
    public long mapSize(int zoomLevel) {
        return defaultMapSize << zoomLevel;
    }


    /**
     * Determines the ground resolution (in meters per pixel) at a specified latitude and
     * zoom level.
     *
     * @param latitude  Latitude (in degrees) at which to measure theground resolution.
     * @param zoomLevel Level of detail, from 1 (lowest detail) to 23 (highest detail).
     * @return The ground resolution, in meters per pixel.
     */
    public double groundResolution(double latitude, int zoomLevel) {
        latitude = clip(latitude, MIN_LATITUDE, MAX_LATITUDE);
        return Math.cos(latitude * Math.PI / 180) * 2 * Math.PI * EARTH_RADIUS / mapSize(zoomLevel);
    }

    /**
     * Determines the map scale at a specified latitude, zoom level ,and screen resolution.
     *
     * @param latitude  Latitude (in degrees) at which to measure the map scale.
     * @param zoomLevel Level of detail, from 1 (lowest detail) to 23 (highest detail).
     * @param screenDpi Resolution of the screen, in dots per inch.
     * @return The map scale, expressed as the denominator N of the ratio 1 : N.
     */
    public double mapScale(double latitude, int zoomLevel, int screenDpi) {
        return groundResolution(latitude, zoomLevel) * screenDpi / 0.0254;
    }

    /**
     * Converts a point from latitude/longitude WGS-84 coordinates (in degrees) into pixel XY
     * coordinates at a specified level of detail.
     *
     * @param latitude  Latitude of the point, in degrees.
     * @param longitude Longitude of the point, in degrees.
     * @param zoomLevel Zoom level from 1 (lowest detail) to 23 (highest detail).
     * @return Output coordinate in pixels.
     */
    public Point latLongToPixelXY(double latitude, double longitude, int zoomLevel) {
        latitude = clip(latitude, MIN_LATITUDE, MAX_LATITUDE);
        longitude = clip(longitude, MIN_LONGITUDE, MAX_LONGITUDE);

        //经纬度转墨卡托投影地图的位置比例
        double x = (longitude + 180) / 360;
        double sinLatitude = Math.sin(latitude * Math.PI / 180);
        double y = 0.5 - Math.log((1 + sinLatitude) / (1 - sinLatitude)) / (4 * Math.PI);

        //墨卡托投影地图的位置比例转像素
        long mapSize = mapSize(zoomLevel);
        int pixelX = (int) clip(x * mapSize + 0.5, 0, mapSize - 1);
        int pixelY = (int) clip(y * mapSize + 0.5, 0, mapSize - 1);
        return new Point(pixelX, pixelY);
    }

    /**
     * Converts a pixel from pixel XY coordinates at a specified level of detail
     * into latitude/longitude WGS-84 coordinates (in degrees).
     *
     * @param pixelX    X coordinate of the point, in pixels.
     * @param pixelY    Y coordinates of the point, in pixels.
     * @param zoomLevel Zoom level from 1 (lowest detail)to 23 (highest detail).
     * @return Output coordinate in degrees.
     */

    public LatLong pixelXYToLatLong(int pixelX, int pixelY, int zoomLevel) {
        double mapSize = mapSize(zoomLevel);
        double x = (clip(pixelX, 0, mapSize - 1) / mapSize) - 0.5;
        double y = 0.5 - (clip(pixelY, 0, mapSize - 1) / mapSize);

        double latitude = 90 - 360 * Math.atan(Math.exp(-y * 2 * Math.PI)) / Math.PI;
        double longitude = 360 * x;
        return new LatLong(latitude, longitude);
    }


    /**
     * Converts pixel XY coordinates into tile XY coordinates of the tile containing
     * the specified pixel.
     *
     * @param pixelX Pixel X coordinate.
     * @param pixelY Pixel Y coordinate.
     * @return Output the tile coordinate.
     */
    public Point pixelXYToTileXY(int pixelX, int pixelY) {
        int tileX = pixelX / 256;
        int tileY = pixelY / 256;
        return new Point(tileX, tileY);
    }

    /**
     * Converts tile XY coordinates into pixel XY coordinates of the upper-left pixel
     * of the specified tile.
     *
     * @param tileX Tile X coordinate.
     * @param tileY Tile Y coordinate.
     * @return Output the pixel coordinate.
     */
    public Point tileXYToPixelXY(int tileX, int tileY) {
        int pixelX = tileX * 256;
        int pixelY = tileY * 256;
        return new Point(pixelX, pixelY);
    }

    /**
     * Converts tile XY coordinates into a QuadKey at a specified zoomLevel.
     *
     * @param tileX     Tile X coordinate.
     * @param tileY     Tile Y coordinate.
     * @param zoomLevel Zoom level from 1 (lowest detail) to 23 (highest detail).
     * @return A string containing the QuadKey.
     */

    public String TileXYToQuadKey(int tileX, int tileY, int zoomLevel) {
        StringBuilder quadKey = new StringBuilder();
        for (int i = zoomLevel; i > 0; i--) {
            char digit = '0';
            int mask = 1 << (i - 1);
            if ((tileX & mask) != 0) {
                digit++;
            }
            if ((tileY & mask) != 0) {
                digit++;
                digit++;
            }
            quadKey.append(digit);
        }
        return quadKey.toString();
    }

    /**
     * Converts a QuadKey into tile XY coordinates.
     *
     * @param quadKey QuadKey of the tile.
     * @return tile info
     */
    public Tile QuadKeyToTileXY(String quadKey) {
        int tileX = 0;
        int tileY = 0;
        int zoomLevel = quadKey.length();
        for (int i = zoomLevel; i > 0; i--) {
            int mask = 1 << (i - 1);
            switch (quadKey.charAt(zoomLevel - i)) {
                case '0':
                    break;

                case '1':
                    tileX |= mask;
                    break;

                case '2':
                    tileY |= mask;
                    break;

                case '3':
                    tileX |= mask;
                    tileY |= mask;
                    break;

                default:
                    return null;
            }
        }
        return new Tile(tileX, tileY, zoomLevel);
    }

    public static class Tile {
        int tileX;
        int tileY;
        int zoomLevel;

        public Tile(int tileX, int tileY, int zoomLevel) {
            this.tileX = tileX;
            this.tileY = tileY;
            this.zoomLevel = zoomLevel;
        }
    }

    public static class LatLong {
        double latitude;
        double longitude;

        public LatLong(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

}

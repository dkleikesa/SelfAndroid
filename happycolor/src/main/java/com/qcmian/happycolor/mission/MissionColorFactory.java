package com.qcmian.happycolor.mission;

import com.qcmian.happycolor.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MissionColorFactory {
    static final Map<String, Integer> COLORS = new HashMap<>();

    static {
        COLORS.put("黑色", 0xFF000000);
//        COLORS.put("白色", 0xFFFFFFFF);
        COLORS.put("红色", 0xFFFF0000);
        COLORS.put("绿色", 0xFF6B8E23);
        COLORS.put("蓝色", 0xFF0000FF);
        COLORS.put("黄色", 0xFFFFFF00);
        COLORS.put("灰色", 0xFFCCCCCC);
        COLORS.put("紫色", 0xFF800080);
        COLORS.put("橙色", 0xFFFFA500);
    }


    public static MissionColor[] missionColors = new MissionColor[5];
    public static float defaultRadius = 0.1f;

    static {
        MissionColor missionColor = new MissionColor();
        missionColor.backgroundColor = 0xFFEDEDF7;
        missionColor.circles = new Circle[2];
        missionColor.circles[0] = new Circle(0xFFFF0000, 0xFFffffff, defaultRadius, "红色");
        missionColor.circles[1] = new Circle(0xFF00FF00, 0xFFffffff, defaultRadius, "绿色");
        missionColors[0] = missionColor;


        missionColor = new MissionColor();
        missionColor.backgroundColor = 0xFFEDEDF7;
        missionColor.circles = new Circle[4];
        missionColor.circles[0] = new Circle(COLORS.get("黑色"), 0xFFffffff, defaultRadius, "黑色");
        missionColor.circles[1] = new Circle(COLORS.get("红色"), 0xFFffffff, defaultRadius, "红色");
        missionColor.circles[2] = new Circle(COLORS.get("绿色"), 0xFFffffff, defaultRadius, "绿色");
        missionColor.circles[3] = new Circle(COLORS.get("蓝色"), 0xFFffffff, defaultRadius, "蓝色");
        missionColors[1] = missionColor;

        missionColor = new MissionColor();
        missionColor.backgroundColor = 0xFFEDEDF7;
        missionColor.circles = new Circle[8];
        missionColor.circles[0] = new Circle(COLORS.get("黑色"), 0xFFffffff, defaultRadius, "黑色");
        missionColor.circles[1] = new Circle(COLORS.get("红色"), 0xFFffffff, defaultRadius, "红色");
        missionColor.circles[2] = new Circle(COLORS.get("绿色"), 0xFFffffff, defaultRadius, "绿色");
        missionColor.circles[3] = new Circle(COLORS.get("蓝色"), 0xFFffffff, defaultRadius, "蓝色");
        missionColor.circles[4] = new Circle(COLORS.get("黄色"), 0xFFffffff, defaultRadius, "黄色");
        missionColor.circles[5] = new Circle(COLORS.get("紫色"), 0xFFffffff, defaultRadius, "紫色");
        missionColor.circles[6] = new Circle(COLORS.get("灰色"), 0xFFffffff, defaultRadius, "灰色");
        missionColor.circles[7] = new Circle(COLORS.get("橙色"), 0xFFffffff, defaultRadius, "橙色");
        missionColors[2] = missionColor;
    }

    public static MissionColor generate(int num) {
        if (num >= COLORS.size()) {
            num = COLORS.size();
        }
        MissionColor missionColor = new MissionColor();
        missionColor.backgroundColor = 0xFFEDEDF7;
        missionColor.circles = new Circle[num];
        List<Object> keys = new ArrayList(COLORS.keySet());
        for (int i = 0; i < num; i++) {
            int idx = next(keys.size());
            String key = (String) keys.get(idx);
            keys.remove(idx);
            missionColor.circles[i] = new Circle(COLORS.get(key), 0xFFffffff, defaultRadius, key);
        }
        return missionColor;
    }

    private static int next(int max) {
        double seed = Math.random();
        return (int) (seed * max);
    }

    public static class MissionColor {
        public int backgroundColor;
        public Circle[] circles;


    }

    public static class Circle {
        public Circle(int circleColor, int edgeColor, float radius, String name) {
            this.circleColor = circleColor;
            this.edgeColor = edgeColor;
            this.name = name;
            int[] screenSize = Utils.getScreenSize();
            int screenWidth = screenSize[0];
            int screenHeight = screenSize[1];
            int tag = screenWidth >= screenHeight ? screenHeight : screenWidth;
            this.radius = (int) (tag * radius);
        }

        public int radius;
        public int circleColor;
        public int edgeColor;
        public String name;
    }


}

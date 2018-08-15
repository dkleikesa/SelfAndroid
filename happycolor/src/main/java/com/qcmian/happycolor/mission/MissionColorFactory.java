package com.qcmian.happycolor.mission;

public class MissionColorFactory {
    public static MissionColor[] missionColors = new MissionColor[5];
    static {


        MissionColor missionColor = new MissionColor();
        missionColor.backgroundColor = 0xFFEDEDF7;
        missionColor.CircleNum = 4;
        missionColor.circleColors = new CircleColor[missionColor.CircleNum];
        missionColor.circleColors[0] = new CircleColor(0xFF5A3836, 0xFFffffff);
        missionColor.circleColors[1] = new CircleColor(0xFF5A3836, 0xFFffffff);
        missionColor.circleColors[2] = new CircleColor(0xFFC117D8, 0xFFffffff);
        missionColor.circleColors[3] = new CircleColor(0xFFC117D8, 0xFFffffff);
        missionColors[0] = missionColor;
    }

    public static class MissionColor {
        int backgroundColor;
        int CircleNum;
        CircleColor[] circleColors;
    }

    public static class CircleColor {
        public CircleColor(int circle, int edge) {
            circleColor = circle;
            edgeColor = edge;
        }

        int circleColor;
        int edgeColor;
    }


}

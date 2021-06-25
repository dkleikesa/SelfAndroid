package com.qcmian.accessibility;

import android.graphics.PointF;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class PositionModel {
    @SerializedName("list")
    public Map<String ,PointF> map;

}

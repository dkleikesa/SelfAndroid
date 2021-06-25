package com.qcmian.accessibility;

import android.graphics.PointF;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Model {
    @SerializedName("list")
    public List<Item> list;

    @SerializedName("index")
    public List<Integer> index;

    public static class Item {
        @SerializedName("index")
        public int index;
        @SerializedName("name")
        public String name;
        @SerializedName("list")
        public List<PointF> list;
    }
}

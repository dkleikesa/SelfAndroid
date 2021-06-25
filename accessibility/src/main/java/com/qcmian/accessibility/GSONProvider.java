package com.qcmian.accessibility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GSONProvider {
    private static Gson sGSON;

    static {
        sGSON = new GsonBuilder()
                .serializeSpecialFloatingPointValues()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
    }

    private GSONProvider() {
    }

    public static Gson get() {
        return sGSON;
    }
}

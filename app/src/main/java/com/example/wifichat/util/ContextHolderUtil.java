package com.example.wifichat.util;

import android.content.Context;

public class ContextHolderUtil {
    private static Context context;

    public static void init(Context context) {
        ContextHolderUtil.context = context.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}

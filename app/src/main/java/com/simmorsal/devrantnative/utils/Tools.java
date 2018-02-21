package com.simmorsal.devrantnative.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Tools {

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }



    private static int screenWidth = 0, screenHeight = 0;
    public static int getScreenWidth(Context context){
        if (screenWidth != 0)
            return screenWidth;

        calculateScreenSize(context);
        return screenWidth;
    }
    public static int getScreenHeight(Context context) {
        if (screenHeight != 0)
            return screenHeight;

        calculateScreenSize(context);
        return screenHeight;
    }

    private static void calculateScreenSize(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }
}

package com.spx.spotimageview;

import android.app.Application;
import android.graphics.Bitmap;

/**
 * Created by SHAOPENGXIANG on 2017/6/30.
 */

public class TApplication extends Application {

    private Bitmap spotImageBitmap = null;

    public void setSpotImageBitmap(Bitmap spotImageBitmap) {
        this.spotImageBitmap = spotImageBitmap;
    }

    public Bitmap getSpotImageBitmap(){
        Bitmap bitmap = spotImageBitmap;
        spotImageBitmap = null;
        return bitmap;
    }
}

package com.spx.spotimageview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.widget.ImageView;


/**
 * Created by SHAOPENGXIANG on 2017/6/20.
 */

public class SpotImageActivity extends Activity {

    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.spot_image_activity_layout);

        Bitmap bitmap =  ((TApplication)getApplication()).getSpotImageBitmap();

        if (bitmap == null) {
            finish();
            return;
        }

        imageView = (ImageView) findViewById(R.id.image_view);
        imageView.setImageBitmap(bitmap);


        ViewPropertyAnimator alphaAnim = imageView.animate().alpha(1).setDuration(180);
        alphaAnim.start();
    }


}

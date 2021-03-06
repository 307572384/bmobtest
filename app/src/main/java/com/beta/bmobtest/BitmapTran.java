package com.beta.bmobtest;
import com.bumptech.glide.load.resource.bitmap.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.*;

import java.security.MessageDigest;

public abstract class BitmapTran extends BitmapTransformation {
    private static float radius = 0f;

    public BitmapTran(Context context) {
        this(context, 4);
    }

    public BitmapTran(Context context, int dp) {
        super(context);
        this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
                               int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null)
            return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(),
                Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(),
                    Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP,
                BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        return result;
    }
    public String getId() {
        return getClass().getName() + Math.round(radius);
    }

}

package com.zhangshuai.qiniu_demo.glide;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

import static android.R.attr.radius;

public class GlideCircleTransform extends BitmapTransformation {
  private Paint mBorderPaint;
  private float mBorderWidth;
  private int mBorderColor;
  public GlideCircleTransform(Context context) {
    super(context);
  }
  public GlideCircleTransform(Context context, int borderWidth, int borderColor) {
    super(context);
    mBorderWidth = Resources.getSystem().getDisplayMetrics().density * borderWidth;

    mBorderPaint = new Paint();
    mBorderPaint.setDither(true);
    mBorderPaint.setAntiAlias(true);
    mBorderPaint.setColor(borderColor);
    mBorderPaint.setStyle(Paint.Style.STROKE);
    mBorderPaint.setStrokeWidth(mBorderWidth);
    mBorderColor = borderColor;
  }

  @Override
  protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
    return circleCrop(pool, toTransform);
  }  


  private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
    if (source == null) return null;

    int size = (int) (Math.min(source.getWidth(), source.getHeight()) - (mBorderWidth / 2));
    int x = (source.getWidth() - size) / 2;
    int y = (source.getHeight() - size) / 2;
    // TODO this could be acquired from the pool too
    Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
    if (result == null) {
      result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
    }
    Canvas canvas = new Canvas(result);
    Paint paint = new Paint();
    paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
    paint.setAntiAlias(true);
    float r = size / 2f;
    canvas.drawCircle(r, r, r, paint);
    if (mBorderPaint != null) {
      float borderRadius = r - mBorderWidth / 2;
      canvas.drawCircle(r, r, borderRadius, mBorderPaint);
    }
    return result;
  }

  public String getId() {
    return getClass().getName() + Math.round(radius);
  }

  @Override
  public void updateDiskCacheKey(MessageDigest messageDigest) {

  }
}
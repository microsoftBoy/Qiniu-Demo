package com.zhangshuai.qiniu_demo.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.zhangshuai.qiniu_demo.R;

import java.io.File;

/**
 * Created by zhangshuai on 2017/11/7.
 */

public class UserUtils {

    private static GlideCircleTransform mGlideCircleTransform;

    private static GlideRoundTransform mGlideRoundTransform;
    private static File file;
    private static Configuration config;
    private static UploadManager uploadManager;

    /**
     * 显示圆角矩形头像
     *
     * @param context
     * @param url
     * @param view
     */
    public static void showAvatarWithRectangle(Context context, String url, ImageView view) {

        RequestManager with = Glide.with(context);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.transform(getGlideRoundTransformWithArc(context))
                .placeholder(R.mipmap.give_menu);
        with.setDefaultRequestOptions(requestOptions)
                .asBitmap()
                .load(url)
                .into(view);
    }

    /**
     * 显示圆形头像
     *
     * @param context
     * @param url
     * @param view
     */
    public static void showAvatarWithArc(Context context, String url, ImageView view) {
        /*Glide.with(context)
                .load(url)
                .transform(getGlideRoundTransformWithArc(context))
                .placeholder(R.mipmap.give_menu)
                .into(view);*/

        RequestManager with = Glide.with(context);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.transform(getGlideRoundTransformWithArc(context))
                .placeholder(R.mipmap.give_menu);
        with.setDefaultRequestOptions(requestOptions)
                .asBitmap()
                .load(url)
                .into(view);
    }

    public static GlideRoundTransform getGlideRoundTransforms(Context context) {
        if (mGlideRoundTransform == null) {
            return mGlideRoundTransform = new GlideRoundTransform(context, 7);
        }
        return mGlideRoundTransform;
    }

    public static GlideCircleTransform getGlideRoundTransformWithArc(Context context) {
        if (mGlideCircleTransform == null) {
            return mGlideCircleTransform = new GlideCircleTransform(context);
        }
        return mGlideCircleTransform;
    }


    private static void initQiniuSdk() {
        if (config == null) {
            config = new Configuration.Builder()
                    .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                    .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                    .connectTimeout(10)           // 链接超时。默认10秒
                    .useHttps(true)               // 是否使用https上传域名
                    .responseTimeout(60)          // 服务器响应超时。默认60秒
                    .recorder(null)           // recorder分片上传时，已上传片记录器。默认null
                    .recorder(null, null)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                    .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                    .build();
        }
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);

    }

}

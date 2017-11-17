package com.zhangshuai.qiniu_demo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.yayandroid.rotatable.Rotatable;
import com.zhangshuai.qiniu_demo.glide.GlideCircleTransform;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Configuration config;
    private UploadManager uploadManager;
    private EditText editText;
    private EditText editText2;
    private Button button;
    private Button button2;
    private Button button3;
    private Button button4;
    private ProgressBar progressBar;
    private ArrayList<String> photos = new ArrayList<>();
    private ImageView iv;
    private TimePickerView pvCustomTime;
    private TimePickerView pvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initQiniuSdk();
        initView();
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        iv = (ImageView) findViewById(R.id.iv);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
    }

    private void initQiniuSdk() {
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
// 重用uploadManager。一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);

    }

    private File file = null;
    private String path = "";
    private String key = "";
    private String token = "mWzmRbD1k2EDBZnLOypHZ3ayKbwD1jPCQX0PQ_zS:VpmhoB27kLLBOLVfSi5Il288HRU" +
            "=:eyJzY29wZSI6InFpbml1LWRlbW8iLCJkZWFkbGluZSI6MTUxMDkyNzk5Nn0=";

    private void upLoadImg() {
        file = new File(path);
        uploadManager.put(file, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    Log.i("qiniu", "Upload Success");
                    Toast.makeText(MainActivity.this,"Upload Success",Toast.LENGTH_LONG).show();
                } else {
                    Log.i("qiniu", "Upload Fail");
                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                    Toast.makeText(MainActivity.this,"Upload Fail",Toast.LENGTH_LONG).show();
                }
                Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + response);
            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                Log.i("qiniu", key + ": " + percent);
                progressBar.setProgress((int) percent);
            }
        }, null));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button:
                key = editText.getText().toString().trim();
                path = editText2.getText().toString().trim();
                upLoadImg();
                break;
            case R.id.button2:
                selectPhoto();
//                initCustomTimePicker();
                break;
            case R.id.button3:
                selectPhotoWithcamera();
//                preview();
//                initTimePicker();
//                rotation();
                break;
            case R.id.button4:
//                selectPhotoWithcamera();
                takePhoto();
//                showTimePicker();
//                showDialog();
//                showAlertDialog();
//                showLoadingDialog();
                break;
            default:
                break;
        }

    }

    private void rotation() {

        Rotatable build = new Rotatable.Builder(iv)
                .rotationCount(20)
                .direction(Rotatable.ROTATE_Y)
                .build();
        build.rotate(Rotatable.ROTATE_Y, 180, 500);
    }

    private void takePhoto() {
        PhotoPicker.builder()
                .setType(true)
                .setShowCamera(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    private void selectPhoto() {
        PhotoPicker.builder()
                .setPhotoCount(9)
                .setShowCamera(false)
                .setShowGif(true)
                .setPreviewEnabled(true)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    private void selectPhotoWithcamera() {
        PhotoPicker.builder()
                .setPhotoCount(9)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(true)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    private void preview() {
        PhotoPreview.builder()
                .setPhotos(photos)
                .setCurrentItem(0)
                .setShowDeleteButton(true)
                .start(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                Log.i("onActivityResult", "onActivityResult:photos.size()= " + photos.size());
                if (photos.size() > 0) {
                    path = photos.get(0);
                    editText2.setText(path);
                    int i = path.lastIndexOf("/");
                    String fileName = path.substring(i+1, path.length());
                    editText.setText(fileName);
                    Log.i("onActivityResult", "onActivityResult:photos.path= " + path);
                    RequestManager with = Glide.with(this);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.transform(getGlideRoundTransformWithArc(this));
                    with.setDefaultRequestOptions(requestOptions)
                            .load(path)
                            .into(iv);


                }
            }
        }
    }

    private static GlideCircleTransform mGlideCircleTransform;

    public static GlideCircleTransform getGlideRoundTransformWithArc(Context context) {
        if (mGlideCircleTransform == null) {
            return mGlideCircleTransform = new GlideCircleTransform(context);
        }
        return mGlideCircleTransform;
    }

    private void showTimePicker() {

        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView
                .OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                editText2.setText(getTime(date));
            }
        })
                .build();
//        pvTime.setDate(Calendar.getInstance());
// 注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }


    private void initTimePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2013, 0, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019, 11, 28);
        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
//                Button btn = (Button) v;
//                btn.setText(getTime(date));
                editText2.setText(getTime(date));
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("qq", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(R.color.gray)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();

        pvTime.show();
    }

    private void initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                editText2.setText(getTime(date));
            }
        })
                /*.setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
               /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*/
               /*.gravity(Gravity.RIGHT)// default is center*/
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, true, true, true})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();

        pvCustomTime.show();

    }

    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("标题");
        builder.setMessage("内容");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "点击取消", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "点击确定", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("标题");
        alertDialog.setMessage("内容");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface
                .OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "点击确定", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface
                .OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "点击取消", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }

    private void showLoadingDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("提示");
        progressDialog.setMessage("内容");
        progressDialog.setProgress(20);
        progressDialog.show();
    }
}

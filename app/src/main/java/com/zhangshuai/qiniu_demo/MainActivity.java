package com.zhangshuai.qiniu_demo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.yayandroid.rotatable.Rotatable;
import com.zhangshuai.qiniu_demo.Utils.UserUtils;
import com.zhangshuai.qiniu_demo.bean.AnswerBean;
import com.zhangshuai.qiniu_demo.bean.AreasBean;
import com.zhangshuai.qiniu_demo.bean.Bean;
import com.zhangshuai.qiniu_demo.bean.NationBean;
import com.zhangshuai.qiniu_demo.bean.ProvinceBean;
import com.zhangshuai.qiniu_demo.glide.GlideCircleTransform;
import com.zhangshuai.qiniu_demo.receiver.LockScreenReceiver;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;
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
    private String TAG = MainActivity.class.getName();
    private OptionsPickerView pvOptions;

    private ArrayList<AnswerBean> answerBeanArrayList = new ArrayList<>();

    private LockScreenReceiver receiver = new LockScreenReceiver();
    private TextView textview;
    private long startTime;
    private ArrayList<String> urls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initQiniuSdk();
        initView();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(receiver, filter);

        for (int i = 0; i < 5; i++) {
            answerBeanArrayList.add(new AnswerBean(i, "answer " + i, true));
        }

        Gson gson = new Gson();
        String s = gson.toJson(answerBeanArrayList);
        Log.i(TAG, "onCreate: s =" + s);

        String format = String.format("一共有（%s）人", "6");
        Log.i(TAG, "onCreate: format = " + format);

        AnswerBean haha = new AnswerBean(1, "haha", true);
        Bean bean = (Bean) haha;
        Log.i(TAG, "onCreate: bean.type = " + bean.type);
        AnswerBean haha2 = (AnswerBean) bean;
        Log.i(TAG, "onCreate: haha2.answerId = " + haha2.answerId);
        Log.i(TAG, "onCreate: haha2.answer = " + haha2.answer);
        Log.i(TAG, "onCreate: haha2.isAbsCondition = " + haha2.isAbsCondition);
        Log.i(TAG, "onCreate: haha2.type = " + haha2.type);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//            iv.setImageDrawable(packageInfo.applicationInfo.loadIcon(getPackageManager()));
//            iv.setImageResource(packageInfo.applicationInfo.icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        initUrl();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initView() {
        textview = findViewById(R.id.textview);
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(50);
        iv = (ImageView) findViewById(R.id.iv);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button4.setText("响铃");
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = iv.isSelected();
                if (selected) {
                    iv.setSelected(false);
                } else {
                    iv.setSelected(true);
                }
            }
        });
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = textview.isSelected();
                Log.i(TAG, "onClick: isSelected= " + isSelected);
                if (textview.isSelected()) {
                    textview.setSelected(false);
                } else {
                    textview.setSelected(true);
                }

            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "beforeTextChanged: s = " + s);
                Log.i(TAG, "beforeTextChanged: start = " + start);
                Log.i(TAG, "beforeTextChanged: count = " + count);
                Log.i(TAG, "beforeTextChanged: after = " + after);
                Log.i(TAG, "------------------------");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "onTextChanged: s = " + s);
                Log.i(TAG, "onTextChanged: start = " + start);
                Log.i(TAG, "onTextChanged: count = " + count);
                Log.i(TAG, "onTextChanged: before = " + before);
                Log.i(TAG, "------------------------");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "afterTextChanged: s = " + s);
                Log.i(TAG, "===========================");
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i(TAG, "onFocusChange: hasFocus =" + hasFocus);
            }
        });
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
                    Toast.makeText(MainActivity.this, "Upload Success", Toast.LENGTH_LONG).show();
                } else {
                    Log.i("qiniu", "Upload Fail");
                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                    Toast.makeText(MainActivity.this, "Upload Fail", Toast.LENGTH_LONG).show();
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
//                upLoadImg();
                handler.postDelayed(null, 3000);
                break;
            case R.id.button2:
                selectPhoto();
//                initCustomTimePicker();
                break;
            case R.id.button3:
//                selectPhotoWithcamera();
//                preview();
//                initTimePicker();
                rotation();
                break;
            case R.id.button4:
//                button4.setText("初始化json数据");
//                selectPhotoWithcamera();
//                takePhoto();
//                showTimePicker();
//                showDialog();
//                showAlertDialog();
//                showLoadingDialog();
//                Intent intent = new Intent(this, CustomActivity.class);
//                startActivity(intent);
//                initJsonData();
//                startAlarm(this);
//                alphaAnimation();
//                daojishi();
                Intent intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
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
        build.rotate(Rotatable.ROTATE_Y, 180, 5000);
        build.drop();
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
                    String fileName = path.substring(i + 1, path.length());
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
        progressDialog.setTitle("提示");
        progressDialog.setMessage("内容");
        progressDialog.setProgress(20);
        progressDialog.show();
    }

    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

    private void initOptionPicker() {//条件选择器初始化

        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */

        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView
                .OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
//                String tx = options1Items.get(options1).getPickerViewText()
//                        + options2Items.get(options1).get(options2)
//                       /* + options3Items.get(options1).get(options2).get(options3)
// .getPickerViewText()*/;
//                btn_Options.setText(tx);
            }
        })
                .setTitleText("城市选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 1)//默认选中项
                .setBgColor(Color.BLACK)
                .setTitleBgColor(Color.DKGRAY)
                .setTitleColor(Color.LTGRAY)
                .setCancelColor(Color.YELLOW)
                .setSubmitColor(Color.YELLOW)
                .setTextColorCenter(Color.LTGRAY)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("省", "市", "区")
                .setBackgroundId(0x66000000) //设置外部遮罩颜色
                .build();

        //pvOptions.setSelectOptions(1,1);
        /*pvOptions.setPicker(options1Items);//一级选择器*/
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/
    }


    private void initJsonData() {
        /*String json = readFileToJson(this, "nation.json");
        Log.i(TAG, "initJsonData: nation = "+json);
        NationBean nationBean = parseNation(json);
        Log.i(TAG, "initJsonData: nationBean.data.size()"+nationBean.data.size());*/
        String area = readFileToJson(this, "area.json");
        Log.i(TAG, "initJsonData: nation = " + area);
        AreasBean areasBean = parseArea(area);
        Log.i(TAG, "initJsonData: areasBean.nations.size()" + areasBean.nations.size());
        AreasBean.CountryBean countryBean = areasBean.nations.get(0);
        Log.i(TAG, "initJsonData: countryBean.name" + countryBean.name);
        Log.i(TAG, "initJsonData: countryBean.provines.size()" + countryBean.provines.size());
        AreasBean.CountryBean.ProvineBean provineBean = countryBean.provines.get(0);
        Log.i(TAG, "initJsonData: provineBean.name" + provineBean.name);
        Log.i(TAG, "initJsonData: provineBean.cities.size()" + provineBean.cities.size());
        AreasBean.CountryBean.ProvineBean.CityBean cityBean = provineBean.cities.get(0);
        Log.i(TAG, "initJsonData: cityBean.name" + cityBean.name);
    }

    private AreasBean parseArea(String json) {
        Gson gson = new Gson();
        AreasBean areaBean = gson.fromJson(json, AreasBean.class);
        return areaBean;

    }

    private NationBean parseNation(String json) {
        Gson gson = new Gson();
        NationBean nationBean = gson.fromJson(json, NationBean.class);
        return nationBean;

    }

    private String readFileToJson(Context context, String fileName) {
        InputStream open = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();
        AssetManager assets = context.getAssets();
        try {
            open = assets.open(fileName);
            inputStreamReader = new InputStreamReader(open);
            bufferedReader = new BufferedReader(inputStreamReader);
            String readLine;
            while ((readLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(readLine);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                open.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
//            startActivity(new Intent(MainActivity.this,NotificationActivity.class));
            PowerManager pm = (PowerManager) MainActivity.this.getSystemService(Context
                    .POWER_SERVICE);

            if (!pm.isScreenOn()) {
                Log.i(TAG, "handleMessage: isScreenOff ");
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
                wl.acquire();
                wl.release();
            }

            KeyguardManager km = (KeyguardManager) MainActivity.this.getSystemService(Context
                    .KEYGUARD_SERVICE);

            boolean b = km.inKeyguardRestrictedInputMode();

            Log.i(TAG, "handleMessage: interactive = " + b);


            return false;
        }
    });

    @OnClick(R.id.bt_start_activity)
    void startActivity() {
        Intent intent = new Intent(this, Main3Activity.class);
        startActivity(intent);
    }

    /**
     * 触发系统响铃
     *
     * @param context
     */
    public static MediaPlayer startAlarm(Context context) {
        MediaPlayer mMediaPlayer = null;
        try {
            mMediaPlayer = MediaPlayer.create(context, RingtoneManager
                    .getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE));
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
            }
        } catch (Exception e) {

        }

        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(context, R.raw.pstnring);
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
            }
        }

        mMediaPlayer.setLooping(true);

        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();

        return mMediaPlayer;
    }

    private void initUrl() {

        urls = new ArrayList<>();
        urls.add("http://images.yiban001.com/album/164468963455476489f76726864bd40c.jpg");
        urls.add("http://images.yiban001.com/album/7d08d75fedc846a5853a8606aace5822.jpg");
        urls.add("http://images.yiban001.com/album/40fab5f5883249269ef41117452441e7.jpg");
        urls.add("http://images.yiban001.com/album/4cf09e3e2f834f699074e7284a052e2f.jpg");
        urls.add("http://images.yiban001.com/album/56a77ae6573946929606062381329a3f.jpg");
        urls.add("http://images.yiban001.com/album/a611b2b1eacc4f19a87d96fcdd3fed28.jpg");

    }

    private int index;
    private float temp;
    private void alphaAnimation() {
        index = 0;
        if (index == 0){
            UserUtils.showAvatarWithArc(MainActivity.this,urls.get(index),iv);
        }
        final ObjectAnimator alphaOut = ObjectAnimator.ofFloat(iv, "alpha", 1f, 0f);
        alphaOut.setDuration(3000);
        alphaOut.setRepeatCount(ValueAnimator.INFINITE);
        alphaOut.setRepeatMode(ValueAnimator.RESTART);
        alphaOut.setInterpolator(new LinearInterpolator());
        alphaOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                Log.i(TAG, "onAnimationUpdate: animatedValue = " + animatedValue);
                float v = animatedValue - temp;
                if (v > 0){
                    temp = 1.0f;
                    Log.i(TAG, "onAnimationUpdate: animatedValue == 0");
                    index+=1;
                    if (index < urls.size()){
                        UserUtils.showAvatarWithArc(MainActivity.this,urls.get(index),iv);
                        if (index == urls.size()-1){
                            alphaOut.cancel();
                        }
                    }
                    else {
                        UserUtils.showAvatarWithArc(MainActivity.this,urls.get(urls.size()-1),iv);
                        alphaOut.cancel();
                    }
                }
                else {
                    temp = animatedValue;
                }
            }
        });
        alphaOut.start();
    }


    private void daojishi(){
        countDownTimer.start();
    }

    CountDownTimer countDownTimer = new CountDownTimer(3000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            Log.i(TAG, "onTick: millisUntilFinished ="+millisUntilFinished);
        }

        @Override
        public void onFinish() {
            Log.i(TAG, "onFinish: ");
        }
    };

}

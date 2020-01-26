package com.ghostx.qd;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghostx.qd.utils.HttpUtil;
import com.ghostx.qd.utils.ImageUtil;
import com.ghostx.qd.utils.JsonUtil;
import com.ghostx.qd.utils.LogUtil;
import com.ghostx.qd.utils.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class UploadActivity extends AppCompatActivity {

    private Button bv_logout;
    private static final int msgKey1 = 1;
    private TextView tv_time;
    private TextView tv_lpnum;
    private TextView tv_cxname;
    private TextView tv_end_time;
    private TextView tv_progress;
    private ImageView iv_add_1;
    private ImageView iv_add_2;
    private ImageView iv_add_3;
    private TextView tv_add_1;
    private TextView tv_add_2;
    private TextView tv_add_3;
    private Button bv_upload;
    private WebView wv_task;
    private MyWebViewClient myWebViewClient = new MyWebViewClient();
    //    载体
    private ImageView iv_add;
    private TextView tv_add;
    private int sign_add;

    private final int FIRST = 1;
    private final int SECOND = 2;
    private final int THIRD = 3;
    private final int REQUEST_CODE_PICK_IMAGE = 100;

    private Map<String, String> body = new HashMap<String, String>();
    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, String> user_info = new HashMap<String, String>();
    private Map<String, String> car_info = new HashMap<String, String>();

    private ArrayList<String> info_sign_user = new ArrayList<String>() {{
        add("lpnum");
        add("cxname");
        add("id");
    }};
    private ArrayList<String> info_sign_car = new ArrayList<String>() {{
        add("process_id");
        add("step_all");
        add("step_current");
        add("time_end");
    }};

    //    上传一致性参数
    private String gr_user_id = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        setHeaders();
        getTask();
        bindView();
        getCookie();
        showMSG();
    }

    private void showMSG() {
        JSONObject j_obj = HttpUtil.get(HttpUtil.getUrl(HttpUtil.urls.get("msg")));
        try {
            if (j_obj.getInt("type") == 1) {
                LogUtil.showMSG(this, j_obj.getString("msg"));
            } else {
                if (SPUtil.getBoolean(getApplicationContext(), "SHOW_MSG", true)) {
                    LogUtil.showMSG(this, j_obj.getString("msg"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getCookie() {
        String url = "https://huoyun.daojia.com/driver-task/index.html?uid=" + user_info.get("id") + "&from=android#/today";
        wv_task.loadUrl(url);
    }

    private void getTask() {
        body.put("uid", user_info.get("id"));
        JSONObject j_obj = HttpUtil.login(HttpUtil.getUrl(HttpUtil.urls.get("getTaskList"))
                , headers, body);
//        LogUtil.logcat(j_obj);
        try {
            if (j_obj.getString("codeMsg").startsWith("成功")) {
                String data = j_obj.getJSONObject("data")
                        .getJSONObject("taskList").getString("pagedList");
                data = data.trim().replace("\"", "")
                        .replace("[", "")
                        .replace("{", "");
                String[] ss = data.split("\\}");
//                LogUtil.logcat("[json] -> " + ss[0]);
                for (String s : ss[0].split(",")) {
                    if (!s.trim().equals("")) {
                        String[] info = s.split(":");
                        if (info_sign_car.contains(info[0])) {
                            car_info.put(info[0], info[1]);
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void bindView() {
        bv_logout = findViewById(R.id.bv_logout);
        bv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.putBoolean(getApplicationContext(), "LOGIN", false);
                login();
            }
        });
        tv_time = findViewById(R.id.tv_time);
        new TimeThread().start();
        tv_lpnum = findViewById(R.id.tv_lpnum);
        tv_lpnum.setText("车牌号：" + user_info.get("lpnum"));
        tv_cxname = findViewById(R.id.tv_cxname);
        String car_type = String.valueOf(user_info.get("cxname"));

        if (!car_type.trim().equals("null")) {
            car_type = car_type.split(" ")[0];
        } else {
            car_type = getResources().getString(R.string.no_msg);
        }
        tv_cxname.setText("车辆信息：" + car_type);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_end_time.setText("截止时间：" + car_info.get("time_end"));
        tv_progress = findViewById(R.id.tv_progress);
        tv_progress.setText("进度：" + car_info.get("step_current") + " / " + car_info.get("step_all"));
        iv_add_1 = findViewById(R.id.iv_add_1);
        iv_add_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto(FIRST);
            }
        });
        iv_add_2 = findViewById(R.id.iv_add_2);
        iv_add_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto(SECOND);
            }
        });
        iv_add_3 = findViewById(R.id.iv_add_3);
        iv_add_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto(THIRD);
            }
        });
        tv_add_1 = findViewById(R.id.tv_add_1);
        tv_add_2 = findViewById(R.id.tv_add_2);
        tv_add_3 = findViewById(R.id.tv_add_3);
        bv_upload = findViewById(R.id.bv_upload);
        bv_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        wv_task = findViewById(R.id.wv_task);
        WebSettings webSettings = wv_task.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(wv_task, true);
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        wv_task.setWebViewClient(myWebViewClient);
        check_pic();
    }

    private void check_pic() {
        String pic_url_1 = String.valueOf(SPUtil.getString(getApplicationContext()
                , "PIC_URL_1", ""));
        String pic_url_2 = String.valueOf(SPUtil.getString(getApplicationContext()
                , "PIC_URL_2", ""));
        String pic_url_3 = String.valueOf(SPUtil.getString(getApplicationContext()
                , "PIC_URL_3", ""));

        if (pic_url_1.contains("http")) {
            HttpUtil.setBitmap(pic_url_1, iv_add_1);
            tv_add_1.setText(R.string.choose_yes);
            tv_add_1.setTextColor(this.getResources().getColor(R.color.colorGreen));
        }
        if (pic_url_2.contains("http")) {
            HttpUtil.setBitmap(pic_url_2, iv_add_2);
            tv_add_2.setText(R.string.choose_yes);
            tv_add_2.setTextColor(this.getResources().getColor(R.color.colorGreen));
        }
        if (pic_url_3.contains("http")) {
            HttpUtil.setBitmap(pic_url_3, iv_add_3);
            tv_add_3.setText(R.string.choose_yes);
            tv_add_3.setTextColor(this.getResources().getColor(R.color.colorGreen));
        }
    }

    private void upload() {
        String pic_url_1 = String.valueOf(SPUtil.getString(getApplicationContext()
                , "PIC_URL_1", ""));
        String pic_url_2 = String.valueOf(SPUtil.getString(getApplicationContext()
                , "PIC_URL_2", ""));
        String pic_url_3 = String.valueOf(SPUtil.getString(getApplicationContext()
                , "PIC_URL_3", ""));
        if (!pic_url_1.contains("http")
                || !pic_url_2.contains("http")
                || !pic_url_3.contains("http")) {
            LogUtil.showMultiBtnDialog(UploadActivity.this
                    , getResources().getString(R.string.check_pic));
        } else {
            if (gr_user_id == null) {
                gr_user_id = HttpUtil.md5(HttpUtil.stamp());
            }
            body.clear();
            body.put("uid", user_info.get("id"));
            body.put("processId", car_info.get("process_id"));
            String imgUrl = SPUtil.getString(getApplicationContext()
                    , "PIC_URL_1", "")
                    + "," + SPUtil.getString(getApplicationContext()
                    , "PIC_URL_2", "")
                    + "," + SPUtil.getString(getApplicationContext()
                    , "PIC_URL_3", "");
            body.put("imgUrl", imgUrl);
            headers.put("user-agent", "Mozilla/5.0 (Linux; Android 10; Redmi K20 Pro Premium Edition Build/QKQ1.190825.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/77.0.3865.92 Mobile Safari/537.36 58app cdvsupport mobileBoard/Redmi K20 Pro Premium Edition appType/1 channel/59 name/suyunDriver imei/865166028210964 mobileVersion/10 version/6.16.1 manufacturer/Xiaomi");
            String referer = "https://huoyun.daojia.com/driver-task/index.html?uid=" + user_info.get("id") + "&from=android&cityId=37&version=6.16.1";
            headers.put("referer", referer);
            String cookie = headers.get("cookie");
            cookie = cookie + ";" + myWebViewClient.CookieStr;
            headers.put("cookie", cookie);
            JSONObject j_obj = HttpUtil.upload(HttpUtil.getUrl(HttpUtil.urls.get("pic_upload"))
                    , headers, body);
            try {
//                    上传成功
                if (j_obj.getString("code").equals("0")) {
                    bv_upload.setText(R.string.upload_end);
                    bv_upload.setTextColor(this.getResources().getColor(R.color.colorGreen));
                    bv_upload.setClickable(false);
                    LogUtil.showMultiBtnDialog(UploadActivity.this
                            , getResources().getString(R.string.upload_end));
                } else {
                    LogUtil.showMultiBtnDialog(UploadActivity.this
                            , j_obj.getString("codeMsg"));
                    if (j_obj.getString("codeMsg").contains("上传次数超过限制")) {
                        SPUtil.remove(getApplicationContext(), "UUID");
                        HttpUtil.initHeaders("", getApplicationContext());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private void choosePhoto(int num) {
        /**
         * 打开选择图片的界面
         */
        switch (num) {
            case FIRST:
                sign_add = FIRST;
                iv_add = iv_add_1;
                tv_add = tv_add_1;
                break;
            case SECOND:
                sign_add = SECOND;
                iv_add = iv_add_2;
                tv_add = tv_add_2;
                break;
            case THIRD:
                sign_add = THIRD;
                iv_add = iv_add_3;
                tv_add = tv_add_3;
                break;
            default:
//               LogUtil.logcat("error");
        }
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

    }

    public void rechoosePhoto(int num) {
        switch (num) {
            case FIRST:
                tv_add = tv_add_1;
                break;
            case SECOND:
                tv_add = tv_add_2;
                break;
            case THIRD:
                tv_add = tv_add_3;
                break;
            default:
        }
        tv_add.setText(R.string.re_choose);
        tv_add.setTextColor(this.getResources().getColor(R.color.colorAccent));
        LogUtil.showMultiBtnDialog(UploadActivity.this, getResources().getString(R.string.re_choose_dialog));
    }

    @Override
    public void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        switch (req) {
            /**
             * 从相册中选取图片的请求标志
             */
            case REQUEST_CODE_PICK_IMAGE:
                if (res == RESULT_OK) {
                    try {
                        /**
                         * 该uri是上一个Activity返回的
                         */

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        //picturePath就是图片在储存卡所在的位置
                        String picturePath = cursor.getString(columnIndex);
//                        LogUtil.logcat("[picturePath] -> " + picturePath);
                        cursor.close();
                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        opts.inSampleSize = 4;
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage)
                                , null, opts);
                        File pic = new File(picturePath);
                        if (pic.length() >= 2097152) {
                            String[] types = picturePath.trim().split("\\.");
                            String type = types[types.length - 1];
                            HttpUtil.compressQuality(bit, 90, pic, type);
                        } else {
                            if (ImageUtil.copyImage(picturePath)) {
                                String[] paths = picturePath.split("/");
                                String name = paths[paths.length - 1];
                                String path_pre = picturePath.substring(0, picturePath.length()-name.length());
                                picturePath = path_pre + "new_" + name;
                            }
                        }
                        upload_pic(picturePath, sign_add);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//                   LogUtil.logcat("[image] -> error");
                }
                break;
            default:
                break;
        }
    }

    private void upload_pic(String path, int order) {
        File file = new File(path);
        String[] types = path.trim().split("\\.");
        String type = types[types.length - 1];
        JSONObject j_obj = HttpUtil.uploadImage(getApplicationContext()
                , HttpUtil.getUrl(HttpUtil.urls.get("pic"))
                , headers, user_info.get("id"), file, type, order);
        try {
            if (!j_obj.getString("code").equals("0")) {
                rechoosePhoto(order);
            } else {
                HttpUtil.setBitmap(SPUtil.getString(getApplicationContext()
                        , "PIC_URL_" + order, ""), iv_add);
                tv_add.setText(R.string.choose_yes);
                tv_add.setTextColor(this.getResources().getColor(R.color.colorGreen));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void login() {
        SPUtil.remove(getApplicationContext(), "PIC_URL_1");
        SPUtil.remove(getApplicationContext(), "PIC_URL_2");
        SPUtil.remove(getApplicationContext(), "PIC_URL_3");
//        HttpUtil.login(HttpUtil.urls.get("logout"), headers, body);
        Intent intent = new Intent(UploadActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setHeaders() {
        JSONObject j_obj = JsonUtil.text2Json(getApplicationContext());
        try {
            String data = j_obj.getString("data");
            data = data.trim().replace("\"", "")
                    .replace("\\", "")
                    .replace("{", "")
                    .replace("}", "")
                    .replace("null", "");
            String[] info = data.split(",");
            for (String ss : info) {
//                LogUtil.logcat("[info] -> " + ss);
                String[] s = ss.split(":");

                if (info_sign_user.contains(s[0]) && s.length == 2) {
                    user_info.put(s[0], s[1]);
                }
                if (s.length == 2) {
                    try {
//                        LogUtil.logcat(s[0]+" = "+s[1]);
                        HttpUtil.headers.put(s[0], URLEncoder.encode(s[1], "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            data = data.replace(":", "=")
                    .replace(",", ";");
            try {
//                LogUtil.logcat(URLEncoder.encode(data, "UTF-8"));
                headers.put("cookie", URLEncoder.encode(data, "UTF-8")
                        .replace("%3D", "=")
                        .replace("%3B", ";"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        LogUtil.logcat("[json] -> " + j_obj);
    }

    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    tv_time.setText(getTime());
                    break;
                default:
                    break;
            }
        }
    };

    //获得当前年月日时分秒星期
    public String getTime() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));//时
        String mMinute = String.valueOf(c.get(Calendar.MINUTE));//分
        String mSecond = String.valueOf(c.get(Calendar.SECOND));//秒

        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "当前时间：" + mYear + "年" + mMonth + "月" + mDay + "日" + "  "
                + "星期" + mWay + "  " + mHour + ":" + mMinute + ":" + mSecond;
    }


    public class MyWebViewClient extends WebViewClient {

        private String CookieStr;

        public boolean shouldOverrideUrlLoading(WebView webview, String url) {
            webview.loadUrl(url, HttpUtil.headers);
            return true;
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            try {
                if (url.contains("https://suyun-driver.daojia.com/api/task/getTaskDetail")) {
                    URL url_fake = new URL(url);
                    URLConnection urlConnection = url_fake.openConnection();
                    HttpURLConnection conn = (HttpURLConnection) urlConnection;
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        conn.addRequestProperty(entry.getKey(), entry.getValue());
                    }
                    conn.addRequestProperty("Host", "suyun-driver.daojia.com");
                    conn.addRequestProperty("origin", "https://huoyun.daojia.com");
                    conn.addRequestProperty("x-requested-with", "com.cxyw.suyun.ui");
                    conn.addRequestProperty("cookie", headers.get("cookie") + CookieStr);
                    conn.setRequestMethod("GET");
                    // Read input
                    String charset = conn.getContentEncoding() != null ? conn.getContentEncoding() : Charset.defaultCharset().displayName();
                    String mime = conn.getContentType();
                    InputStream isContents = conn.getInputStream();
                    return new WebResourceResponse(mime, charset, isContents);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return super.shouldInterceptRequest(view, url);
        }

        public void onPageFinished(WebView view, String url) {
            CookieManager cookieManager = CookieManager.getInstance();
            CookieStr = cookieManager.getCookie(url);
            bv_upload.setClickable(true);
            bv_upload.setBackgroundResource(R.drawable.btn_click);
            bv_upload.setText(R.string.upload);
            super.onPageFinished(view, url);
        }

    }
}

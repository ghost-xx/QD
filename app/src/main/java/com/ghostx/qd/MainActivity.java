package com.ghostx.qd;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ghostx.qd.utils.HttpUtil;
import com.ghostx.qd.utils.ImageUtil;
import com.ghostx.qd.utils.LogUtil;
import com.ghostx.qd.utils.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button bv_get_code;
    private EditText ev_get_phone_num;

    private LinearLayout lv_img;
    private EditText ev_img;
    private ImageView iv_img;

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;


    private Map<String, String> body = new HashMap<String, String>();
    private Map<String, String> headers = new HashMap<String, String>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        String uuid = HttpUtil.initHeaders("", getApplicationContext());
        if (!SPUtil.getString(getApplicationContext()
                , "UUID", "").contains("86516602")) {
            SPUtil.putString(getApplicationContext(), "UUID", uuid);
        } else {
            HttpUtil.initHeaders(uuid, getApplicationContext());
        }
        initUrls();
        checkLogin();
        bindView();
    }


    private void initUrls() {
        int[] url_1 = new int[]{191, 247, 247, 239, 245, 99, 77, 77, 245, 249, 225, 249, 203, 73, 215, 243, 193, 251, 217, 243, 75, 215, 209, 205, 195, 193, 209, 75, 213, 205, 201, 77, 245, 249, 225, 249, 203, 199, 205, 221, 193, 203, 77, 211, 193, 203, 215, 201, 205, 211, 193, 199, 217};
        int[] url_2 = new int[]{191, 247, 247, 239, 245, 99, 77, 77, 245, 249, 225, 249, 203, 73, 215, 243, 193, 251, 217, 243, 75, 215, 209, 205, 195, 193, 209, 75, 213, 205, 201, 77, 245, 249, 225, 249, 203, 199, 205, 221, 193, 203, 77, 221, 217, 247, 193, 201, 221, 213, 205, 215, 217};
        int[] url_3 = new int[]{191, 247, 247, 239, 245, 99, 77, 77, 245, 249, 225, 249, 203, 73, 215, 243, 193, 251, 217, 243, 75, 215, 209, 205, 195, 193, 209, 75, 213, 205, 201, 77, 209, 239, 239, 245, 247, 209, 243, 247, 77, 249, 239, 199, 205, 209, 215, 245, 247, 209, 243, 247, 215, 209, 247, 209};
        int[] url_4 = new int[]{191, 247, 247, 239, 99, 77, 77, 113, 111, 123, 75, 113, 115, 75, 113, 95, 121, 75, 113, 113, 113, 99, 95, 111, 95, 95, 77, 241, 215, 77, 199, 205, 221, 193, 203};
        int[] url_5 = new int[]{191, 247, 247, 239, 245, 99, 77, 77, 245, 249, 225, 249, 203, 73, 215, 243, 193, 251, 217, 243, 75, 215, 209, 205, 195, 193, 209, 75, 213, 205, 201, 77, 209, 239, 193, 77, 247, 209, 245, 197, 77, 221, 217, 247, 183, 209, 245, 197, 135, 193, 245, 247};
        int[] url_6 = new int[]{191, 247, 247, 239, 245, 99, 77, 77, 219, 193, 199, 217, 249, 239, 199, 205, 209, 215, 75, 215, 209, 205, 195, 193, 209, 75, 213, 205, 201, 77, 251, 115, 77, 219, 193, 199, 217, 73, 249, 239, 199, 205, 209, 215, 77, 109, 239, 209, 247, 191, 105, 77, 239, 193, 213, 77, 247, 217, 245, 247};
        int[] url_7 = new int[]{191, 247, 247, 239, 245, 99, 77, 77, 245, 249, 225, 249, 203, 73, 215, 243, 193, 251, 217, 243, 75, 215, 209, 205, 195, 193, 209, 75, 213, 205, 201, 77, 209, 239, 193, 77, 247, 209, 245, 197, 77, 249, 239, 199, 205, 209, 215, 181, 247, 193, 213, 197, 217, 243, 137, 217, 245, 245, 209, 221, 217};
        int[] url_8 = new int[]{191, 247, 247, 239, 99, 77, 77, 113, 111, 123, 75, 113, 115, 75, 113, 95, 121, 75, 113, 113, 113, 99, 95, 111, 95, 113, 77, 201, 245, 221, 75, 195, 245, 205, 203};
        int[] url_9 = new int[]{191, 247, 247, 239, 99, 77, 77, 113, 111, 123, 75, 113, 115, 75, 113, 95, 121, 75, 113, 113, 113, 99, 95, 111, 95, 95, 77, 241, 215, 77, 193, 245, 185, 245, 217, 243};
        int[] url_10 = new int[]{191, 247, 247, 239, 99, 77, 77, 113, 111, 123, 75, 113, 115, 75, 113, 95, 121, 75, 113, 113, 113, 99, 95, 111, 95, 95, 77, 241, 215, 77, 209, 213, 247, 193, 251, 217};
        HttpUtil.urls.put("bindmobile", url_1);
        HttpUtil.urls.put("getimgcode", url_2);
        HttpUtil.urls.put("uploadstartdata", url_3);
        HttpUtil.urls.put("login", url_4);
        HttpUtil.urls.put("getTaskList", url_5);
        HttpUtil.urls.put("pic", url_6);
        HttpUtil.urls.put("pic_upload", url_7);
        HttpUtil.urls.put("msg", url_8);
        HttpUtil.urls.put("isUser", url_9);
        HttpUtil.urls.put("isActive", url_10);

    }

    private void checkLogin() {
        if (SPUtil.getBoolean(getApplicationContext(), "LOGIN", false)) {
            upload();
        } else {

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
    }


    private void bindView() {
//        登录
        bv_get_code = findViewById(R.id.bv_get_code);
        ev_get_phone_num = findViewById(R.id.ev_input_phone_num);
        bv_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode(ev_get_phone_num.getText().toString());
            }
        });
//        图片验证码
        lv_img = findViewById(R.id.lv_img);
        ev_img = findViewById(R.id.ev_img);
        iv_img = findViewById(R.id.iv_img);
    }

    private void getCode(String phone) {
        bv_get_code.setClickable(false);
        bv_get_code.setBackgroundResource(R.color.colorGray);
//        判断手机号码是否合理
        if (phone.matches("\\d{11}")) {
            if (!check_active(phone)) {
                SPUtil.putString(getApplicationContext(), "PHONE", phone);
                LogUtil.showActive(MainActivity.this
                        , getResources().getString(R.string.not_active));
            } else {
                body.clear();
                body.put("mobile", phone);
                JSONObject j_obj = HttpUtil.login(
                        HttpUtil.getUrl(HttpUtil.urls.get("bindmobile")),
                        headers, body);
                try {
//                LogUtil.logcat("[Step 1] -> " + j_obj);
                    String codeMsg = j_obj.getString("codeMsg");
//                判断是否成功获取验证码
                    if (codeMsg.trim().startsWith("成功")) {
                        LogUtil.showMultiBtnDialog(MainActivity.this, codeMsg);
                        SPUtil.putString(getApplicationContext(), "PHONE", phone);
                        body.clear();
                        sms_code();
//                获取图片验证码
                    } else if (codeMsg.trim().startsWith("验证码获取频繁")) {
                        LogUtil.showMultiBtnDialog(MainActivity.this, codeMsg);
                        j_obj = HttpUtil.login(
                                HttpUtil.getUrl(HttpUtil.urls.get("getimgcode")),
                                headers, body);
                        codeMsg = j_obj.getString("codeMsg");
//                    绘制验证码
                        if (codeMsg.trim().startsWith("成功")) {
                            JSONObject data = j_obj.getJSONObject("data");
                            String imgCode = data.getString("imgCode");
                            Bitmap bm = ImageUtil.string2Bitmap(imgCode);
                            iv_img.setImageBitmap(bm);
                            lv_img.setVisibility(View.VISIBLE);
                            ev_img.requestFocus();
                            ev_img.addTextChangedListener(new TextWatcher() {
                                String phone = ev_get_phone_num.getText().toString();

                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    if (s.length() == 4) {
                                        body.put("imgCode", s.toString());
                                        JSONObject j_obj = HttpUtil.login(
                                                HttpUtil.getUrl(HttpUtil.urls.get("bindmobile")),
                                                headers, body);
                                        try {
                                            String codeMsg = j_obj.getString("codeMsg");
                                            if (codeMsg.trim().startsWith("成功")) {
                                                s.clear();
                                                bv_get_code.setClickable(true);
                                                bv_get_code.setBackgroundResource(R.color.colorPrimary);
                                                lv_img.setVisibility(View.GONE);
                                                SPUtil.putString(getApplicationContext(), "PHONE", phone);
                                                body.clear();
                                                sms_code();
                                            } else {
                                                s.clear();
                                                LogUtil.showMultiBtnDialog(MainActivity.this, codeMsg);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            });
                        }
//                    其他错误
                    } else {
                        bv_get_code.setClickable(true);
                        bv_get_code.setBackgroundResource(R.color.colorPrimary);
                        LogUtil.showMultiBtnDialog(MainActivity.this, codeMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//        输入有误
        } else {
            bv_get_code.setClickable(true);
            bv_get_code.setBackgroundResource(R.color.colorPrimary);
            Toast.makeText(getApplicationContext(),
                    R.string.check_number, Toast.LENGTH_LONG).show();
        }
    }

    private boolean check_active(String phone) {
        body.clear();
        body.put("phone", phone);
        HashMap<String, String> temp = new HashMap<>();
        String data = "";
        for (Map.Entry<String, String> entry : body.entrySet()) {
            data += entry.getKey() + "=" + entry.getValue() + ";";
        }
        body.clear();
        data = HttpUtil.setBody(data);
        temp.put("data", data);
        JSONObject j_obj = HttpUtil.post(HttpUtil.getUrl(HttpUtil.urls.get("isUser")), temp);
        try {
            if (j_obj.getString("code").equals("1")) {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        temp.clear();
        return true;
    }

    private void sms_code() {
        Intent intent = new Intent(
                MainActivity.this,
                SMSActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Toast.makeText(getApplicationContext(),
                        ("申请的权限为：" + permissions[i] + ",\n申请结果：" + grantResults[i]),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void upload() {
        Intent intent = new Intent(
                MainActivity.this,
                UploadActivity.class);
        startActivity(intent);
        finish();
    }

}

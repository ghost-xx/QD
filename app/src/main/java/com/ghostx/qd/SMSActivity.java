package com.ghostx.qd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.ghostx.qd.utils.HttpUtil;
import com.ghostx.qd.utils.JsonUtil;
import com.ghostx.qd.utils.LogUtil;
import com.ghostx.qd.utils.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SMSActivity extends AppCompatActivity {

    private EditText ev_sms_code;

    private Map<String, String> body = new HashMap<String, String>();
    private Map<String, String> headers = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        bindView();
    }

    private void bindView() {
        ev_sms_code = findViewById(R.id.ev_sms_code);
        ev_sms_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = SPUtil.getString(getApplicationContext(), "PHONE", "");
                if (s.length() == 4) {
                    fake_cid();
                    body.clear();
                    body.put("phone", phone);
                    body.put("cid", "c9f169f5cb93899d64130a1901ecb619");
//                    body.put("uid","");
                    body.put("appId", "wx3a890cc80dc3cddc");
                    body.put("mobilecode", s.toString());
                    body.put("uuid",SPUtil.getString(getApplicationContext(),"UUID",""));
                    HashMap<String, String> temp = new HashMap<>();
                    String data = "";
                    for (Map.Entry<String, String> entry : body.entrySet()) {
                        data += entry.getKey() + "=" + entry.getValue() + ";";
                    }
                    body.clear();
                    data = HttpUtil.setBody(data);
                    temp.put("data", data);
                    JSONObject j_obj = HttpUtil.post(
                            HttpUtil.getUrl(HttpUtil.urls.get("login")), temp);
                    temp.clear();
                    try {
//                        LogUtil.logcat("[Step 2] -> " + j_obj);
                        String codeMsg = j_obj.getString("codeMsg");
                        if (codeMsg.trim().startsWith("成功")) {
                            SPUtil.putBoolean(getApplicationContext(), "LOGIN", true);
                            JsonUtil.json2Text(getApplicationContext(), j_obj.toString());
                            upload();
                        } else {
                            LogUtil.showMultiBtnDialog(SMSActivity.this, codeMsg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void fake_cid() {
        body.clear();
        body.put("cid", "c9f169f5cb93899d64130a1901ecb619");
//        body.put("lat","29.591439");
//        body.put("lng","106.521946");
//        body.put("r","0.9586263409780764");
//        body.put("uid","");
        HttpUtil.login(
                HttpUtil.getUrl(HttpUtil.urls.get("uploadstartdata")),
                headers, body);
        body.clear();
    }

    private void upload() {
        Intent intent = new Intent(
                SMSActivity.this,
                UploadActivity.class);
        startActivity(intent);
        finish();
    }
}

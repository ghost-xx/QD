package com.ghostx.qd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ghostx.qd.utils.HttpUtil;
import com.ghostx.qd.utils.LogUtil;
import com.ghostx.qd.utils.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActiveActivity extends AppCompatActivity {

    private EditText ev_active;
    private Button bv_active;
    private Map<String, String> body = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);
        bindView();
    }

    private void bindView() {
        ev_active = findViewById(R.id.ev_active);
        bv_active = findViewById(R.id.bv_active);
        bv_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                body.clear();
                body.put("phone",
                        SPUtil.getString(getApplicationContext(), "PHONE", ""));
//                LogUtil.logcat(body.get("phone"));
                String active_code = ev_active.getText().toString();
                body.put("activeCode", active_code);
                HashMap<String, String> temp = new HashMap<>();
                String data = "";
                for (Map.Entry<String, String> entry : body.entrySet()) {
                    data += entry.getKey() + "=" + entry.getValue() + ";";
                }
                body.clear();
//                LogUtil.logcat(data);
                data = HttpUtil.setBody(data);
                temp.put("data", data);
                JSONObject j_obj = HttpUtil.post(
                        HttpUtil.getUrl(HttpUtil.urls.get("isActive")), temp);
                temp.clear();
                try {
                    if (j_obj.getString("code").equals("1")) {
                        LogUtil.showMultiBtnDialog(ActiveActivity.this
                                , j_obj.getString("msg"));
                    }else if (j_obj.getString("code").equals("0")){
                        Intent intent = new Intent(
                                ActiveActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}

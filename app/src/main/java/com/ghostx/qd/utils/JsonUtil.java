package com.ghostx.qd.utils;

import android.content.Context;
import android.os.Environment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonUtil {

    @NotNull
    private static String sdPath(Context ctx) {
        return ctx.getFilesDir().getAbsolutePath();
    }

    public static void json2Text(Context ctx, String json) {
        try {
            BufferedWriter bfw = new BufferedWriter(
                    new FileWriter(sdPath(ctx) + "User.txt", false));
            bfw.write(json);
            bfw.flush();
            bfw.close();
        } catch (IOException e) {
            System.out.println("[json] -> error");
            e.printStackTrace();
        }
    }

    public static JSONObject text2Json(Context ctx) {
        JSONObject res = null;
        try {
            BufferedReader bfr = new BufferedReader(
                    new FileReader(sdPath(ctx) + "User.txt"));
            String line = bfr.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = bfr.readLine();
            }
            bfr.close();
            try {
                res = new JSONObject(sb.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("[json] ->" + res);
        return res;
    }
}

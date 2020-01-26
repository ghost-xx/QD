package com.ghostx.qd.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {

    private static JSONObject jsonObjectRet;
    public static Map<String, String> headers = new HashMap<String, String>();
    public static Map<String, int[]> urls = new HashMap<String, int[]>();

    public static String initHeaders(String uuid, Context ctx) {
        if (uuid.equals("")
                && !SPUtil.getString(ctx, "UUID", "").contains("86516602")) {
            uuid = MobileInfoUtil.getUUID();
        } else {
            uuid = SPUtil.getString(ctx, "UUID", "");
        }

        headers.put("djfrtappversion", "6.16.1");
        headers.put("mobile-board", "Redmi K20 Pro Premium Edition");
        headers.put("manufacturer", "Xiaomi");
        headers.put("number", "0");
        headers.put("geotype", "0");
        headers.put("apptype", "1");
        headers.put("djfrtappid", "58daojiaapp-suyun-driver-android");
        headers.put("oaid", uuid);
        headers.put("device_id", uuid);
        headers.put("version", "6.16.1");
        headers.put("imei", uuid);
        headers.put("djfrtimei", uuid);
        headers.put("channelid", "59");
        headers.put("mobile-version", "10");
        headers.put("content-type", "application/x-www-form-urlencoded");
        headers.put("user-agent", "okhttp/3.8.0");
        headers.put("Connection", "close");
        return uuid;
    }

    //    get
    public static JSONObject get(final String url) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();
                    Response response = client.newCall(request).execute();
                    jsonObjectRet = new JSONObject(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObjectRet;
    }

    public static JSONObject post(final String url, final Map<String, String> body) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient.Builder().build();
                    FormBody.Builder fb = new FormBody.Builder();
                    for (Map.Entry<String, String> entry : body.entrySet()) {
                        fb.add(entry.getKey(), entry.getValue());
                    }
                    RequestBody requestBody = fb.build();
                    Request.Builder request = new Request.Builder();
                    request.url(url);
                    request.post(requestBody);
                    Request request1 = request.build();
                    Response response = client.newCall(request1).execute();
                    jsonObjectRet = new JSONObject(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObjectRet;
    }

    //    post
    public static JSONObject login(final String url, final Map<String, String> header
            , final Map<String, String> body) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient.Builder().build();
                    FormBody.Builder fb = new FormBody.Builder();
                    for (Map.Entry<String, String> entry : body.entrySet()) {
                        fb.add(entry.getKey(), entry.getValue());
                    }
                    RequestBody requestBody = fb.build();
                    for (Map.Entry<String, String> entry : header.entrySet()) {
                        headers.put(entry.getKey(), entry.getValue());
                    }
                    Request.Builder request = new Request.Builder();
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        request.addHeader(entry.getKey(), entry.getValue());
                    }
                    request.url(url);
                    request.post(requestBody);
                    Request request1 = request.build();
                    Response response = client.newCall(request1).execute();
                    jsonObjectRet = new JSONObject(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObjectRet;
    }

    //    post
    public static JSONObject upload(final String url, final Map<String, String> header
            , final Map<String, String> body) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient.Builder().build();
                    FormBody.Builder fb = new FormBody.Builder();
                    for (Map.Entry<String, String> entry : body.entrySet()) {
                        fb.add(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                    RequestBody requestBody = fb.build();
                    headers.put("mobile-board", "Redmi+K20+Pro+Premium+Edition");
                    headers.put("geotype", "1");
                    for (Map.Entry<String, String> entry : header.entrySet()) {
                        headers.put(entry.getKey(), entry.getValue());
                    }
                    Request.Builder request = new Request.Builder();
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        request.addHeader(entry.getKey(), entry.getValue());
                    }
                    request.url(url);
                    request.post(requestBody);
                    Request request1 = request.build();
                    Response response = client.newCall(request1).execute();
                    jsonObjectRet = new JSONObject(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObjectRet;
    }

    //    post
    public static JSONObject uploadImage(final Context ctx, final String url, final Map<String, String> header
            , final String uid, final File file, final String type, final int order) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String mime;
                    if (type.trim().equals("png")) {
                        mime = "png";
                    } else {
                        mime = "jpeg";
                    }

                    OkHttpClient client = new OkHttpClient.Builder().build();
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + mime), file);
                    MultipartBody multipartBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("uid", uid)
                            .addFormDataPart("file", stamp() + "." + mime, requestBody)
                            .build();
                    headers.put("geotype", "1");
                    headers.put("content-type", "application/x-www-form-urlencoded");
                    headers.put("user-agent", "okhttp/3.8.0");
                    for (Map.Entry<String, String> entry : header.entrySet()) {
                        headers.put(entry.getKey(), entry.getValue());
                    }
                    Request.Builder request = new Request.Builder();
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        request.addHeader(entry.getKey(), entry.getValue());
                    }
                    request.url(url);
                    request.post(multipartBody);
                    Request request1 = request.build();
                    Response response = client.newCall(request1).execute();
                    jsonObjectRet = new JSONObject(response.body().string());
//                    LogUtil.logcat(jsonObjectRet);
                    if (jsonObjectRet.getString("code").equals("0")) {
                        String pic_url = jsonObjectRet.getString("url").trim()
                                .replace("\\", "")
                                .replace("\"", "");
                        SPUtil.remove(ctx, "PIC_URL_" + order);
                        SPUtil.putString(ctx, "PIC_URL_" + order, pic_url);
                    } else {
                        LogUtil.showMultiBtnDialog(ctx, "第 " + order + " 张照片上传失败：\n" + jsonObjectRet.getString("codeMsg"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObjectRet;
    }

    public static void compressQuality(Bitmap bitmap, int quality, File file, String mime) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (mime.trim().equals("png")) {
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String stamp() {
        long ct = System.currentTimeMillis();
//        LogUtil.logcat(ct);
        Random random = new Random();
        random.setSeed(ct);
        String suffix = "000" + random.nextInt(999);
        return ct + suffix.substring(suffix.length() - 3);
    }

    public static String md5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        md5code = md5code.substring(0, 8)
                + "-" + md5code.substring(8, 12)
                + "-" + md5code.substring(12, 16)
                + "-" + md5code.substring(16, 20)
                + "-" + md5code.substring(20, 32);
        return md5code;
    }


    public static String getUrl(int[] url) {
        String res = "";
        for (int i = 0; i < url.length; i++) {
            res += Character.toString((char) ((((url[i] + 1) ^ 16) / 2)));
        }
//        LogUtil.logcat(res.length() + ":" + res);
        return res;
    }

    public static void setBitmap(final String url, final ImageView imageView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bm = null;
                try {
                    URL iconUrl = new URL(url);
                    URLConnection conn = iconUrl.openConnection();
                    HttpURLConnection http = (HttpURLConnection) conn;

                    int length = http.getContentLength();

                    conn.connect();
                    // 获得图像的字符流
                    InputStream is = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is, length);
                    bm = BitmapFactory.decodeStream(bis);
                    bis.close();
                    is.close();// 关闭流
                    imageView.setImageBitmap(bm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static String setBody(String body) {
        String res = "";
        int len = body.length();
        for (int i = 0; i < len; i++) {
            String temp = "000" + (((body.charAt(i) * 2) ^ 16) - 10);
            res += temp.substring(temp.length() - 3);
        }
        return res;
    }


}
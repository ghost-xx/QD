package com.ghostx.qd.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.ghostx.qd.ActiveActivity;
import com.ghostx.qd.MainActivity;
import com.ghostx.qd.R;
import com.ghostx.qd.SMSActivity;


public class LogUtil {
    public static void logcat(Object obj) {
        String TAG = "[QD -> log] -> ";
        System.out.println(TAG + obj.toString());
    }

    public static void showMultiBtnDialog(Context ctx,String msg){
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(ctx);
        normalDialog.setTitle("提示").setMessage(msg);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ...To-do
                    }
                });
//        normalDialog.setNeutralButton("按钮2",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // ...To-do
//                    }
//                });
//        normalDialog.setNegativeButton("按钮3", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // ...To-do
//            }
//        });
        // 创建实例并显示
        normalDialog.show();
    }

    public static void showMSG(final Context ctx, String msg){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(ctx);
        normalDialog.setTitle(R.string.announcement);
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton(R.string.sure,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.setNegativeButton(R.string.not_show_msg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPUtil.putBoolean(ctx,"SHOW_MSG",false);
                    }
                });
        // 显示
        normalDialog.show();
    }

    public static void showActive(final Context ctx, String msg){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(ctx);
        normalDialog.setTitle(R.string.announcement);
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton(R.string.sure,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        Intent intent = new Intent(
                                ctx,
                                ActiveActivity.class);
                        ctx.startActivity(intent);
                    }
                });
        normalDialog.show();
    }
}

package com.suelee.maps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {
    //获取权限
    public static int REQUEST_PHOTO_CODE = 3002;
    public static int REQUEST_CODE_PERMISSION = 0x00099;
    private String TAG = "PermissionHelper";

    public static void checkPermission(Activity context, String[] permissions, int requestCode, RequestPermissionCallBack callBack) {
        REQUEST_CODE_PERMISSION = requestCode;
        if (callBack != null) {
            if (checkPermissions(context, permissions)) {
                callBack.onPermission();
            } else {
                callBack.onDenied();
            }
        }
    }

    public static boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    public static void requestPermission(Activity context, String[] permissions){
        try {
            List<String> needPermissions = getDeniedPermissions(context,permissions);
            ActivityCompat.requestPermissions(
                    context,
                    needPermissions.toArray(new String[0]),
                    REQUEST_CODE_PERMISSION
            );
        } catch (Exception e){
            Log.e("BaseActivity", "获取权限失败 Exception = $e");
        }
    }
    @SuppressLint("ObsoleteSdkInt")
    private static boolean checkPermissions(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    private static List<String> getDeniedPermissions(Activity context,String [] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(context, permission)
            ) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }


    interface RequestPermissionCallBack{
        void onPermission();
        void onDenied();
    }
}

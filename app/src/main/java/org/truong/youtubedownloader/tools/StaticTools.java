package org.truong.youtubedownloader.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.truong.youtubedownloader.R;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by truon on 2/16/17.
 */

public class StaticTools {
    public static final String HOME_PATH = "/YouTube Downloader/";

    public static void openYouTubeApp(Context context) {
        PackageManager pm = context.getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage("com.google.android.youtube");
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        } else {
            Toast.makeText(context, "Youtube not installed", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean underAPI23() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }

    public static boolean verify(Activity activity, final String[] PERMISSIONS, final int PERMISSIONS_REQUEST_ID) {
        if (underAPI23())
            return true;

        boolean shouldShowRequest = false;
        List<String> pendingPermission = new ArrayList<>();

        for (String PERMISSION : PERMISSIONS) {
            int check = ContextCompat.checkSelfPermission(activity, PERMISSION);
            Log.i(TAG, PERMISSION + ": " + (check != PackageManager.PERMISSION_GRANTED));
            if (check != PackageManager.PERMISSION_GRANTED) {
                pendingPermission.add(PERMISSION);
                if (!shouldShowRequest) {
                    boolean should = ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION);
                    if (should)
                        shouldShowRequest = true;
                }
            }
        }

        int denyPermissionLength = pendingPermission.size();
        String[] denyPermission = new String[denyPermissionLength];
        for (int i = 0; i < denyPermissionLength; i++) {
            denyPermission[i] = pendingPermission.get(i);
        }

        if (denyPermissionLength > 0) {
            if (shouldShowRequest) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.dialog_grant_permission_title);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
            } else {
                ActivityCompat.requestPermissions(activity, denyPermission, PERMISSIONS_REQUEST_ID);
            }
            return false;
        } else {
            return true;
        }
    }

    public static String getIdFromUrl(String url) {
        return url.substring(url.length() - 11, url.length());
    }

    public static void createFolder() {
        File folder = new File(Environment.getExternalStorageDirectory().getPath() + HOME_PATH);

        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public static String getHomePath() {
        createFolder();
        return Environment.getExternalStorageDirectory().getPath() + HOME_PATH;
    }

    public static boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getTitleQuietly(String youtubeUrl) {
        try {
            if (youtubeUrl != null) {
                URL embededURL = new URL("http://www.youtube.com/oembed?url=" +
                        youtubeUrl + "&format=json"
                );

                return new JSONObject(IOUtils.toString(embededURL)).getString("title");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getVideos() {
        File dir = new File(getHomePath());
        return dir.list();
    }
}

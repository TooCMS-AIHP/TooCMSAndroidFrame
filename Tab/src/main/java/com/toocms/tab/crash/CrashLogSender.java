package com.toocms.tab.crash;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.lzy.okgo.model.HttpParams;
import com.toocms.tab.network.ApiListener;
import com.toocms.tab.network.ApiTool;
import com.toocms.tab.network.modle.TooCMSResponse;
import com.toocms.tab.toolkit.LogUtil;
import com.toocms.tab.toolkit.x;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Author：Zero
 * Date：2017/8/31 16:44
 *
 * @version v1.0
 */

public class CrashLogSender {

    private Application mApp;
    private Thread mReportThread;

    public CrashLogSender(Application app) {
        this.mApp = app;

        mReportThread = new Thread(mReportRunnable);
        mReportThread.setName("CrashLogReport");
        mReportThread.setPriority(Thread.MIN_PRIORITY);
    }


    private Runnable mReportRunnable = new Runnable() {

        @Override
        public void run() {
            try {
                Thread.sleep(CrashConfig.REPORT_LOG_DELAY);
                checkSdkVersion(mApp);
                reportCrashLogs();
            } catch (Exception ex) {
                Log.d(CrashConfig.TAG, "Report crash logs failed.", ex);
            }
        }

    };

    private void checkSdkVersion(Application app) {
        File logStorageDir = new File(CrashLogUtils.getLogStorageDir());
        if (!logStorageDir.exists()) {
            logStorageDir.mkdirs();
        }
        int sdkVer = 0;
        SharedPreferences sharedPreferences = app.getSharedPreferences(CrashConfig.PREFERENCES_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(CrashConfig.PRE_SDK_VERSION)) {
            sdkVer = sharedPreferences.getInt(CrashConfig.PRE_SDK_VERSION, CrashConfig.VERSION);
        }

        if (sdkVer != CrashConfig.VERSION) {
            //Delete the SDK version does not match the logs
            File[] logList = logStorageDir.listFiles();
            CrashLogStore.deleteLogFiles(logList);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(CrashConfig.PRE_SDK_VERSION, CrashConfig.VERSION);
            editor.commit();
        }
    }

    public void sendHttpRequest(HttpParams params) {
        new ApiTool<TooCMSResponse<Void>>().postApi(x.app(), CrashConfig.REPORT_URL, params, new ApiListener<TooCMSResponse<Void>>() {
            @Override
            public void onComplete(TooCMSResponse<Void> data, Call call, Response response) {
                LogUtil.e(data.getMessage());
            }
        });
    }

    private boolean isAllowConnectNetwork() {
        ConnectivityManager connectMgr = (ConnectivityManager) mApp.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            return false;
        }

        if (netInfo.getType() == ConnectivityManager.TYPE_WIFI && netInfo.isConnected()) {
            return true;
        }

        TelephonyManager telMgr = (TelephonyManager) mApp.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        int netClass = CrashLogUtils.getNetworkClass(telMgr.getNetworkType());
        if (CrashLogUtils.isNetworkTypeMobile(netInfo.getType())
                && netInfo.isConnected()
                && (netClass == CrashLogUtils.NETWORK_CLASS_3_G || netClass == CrashLogUtils.NETWORK_CLASS_4_G)) {
            return true;
        }
        return false;
    }

    private void reportCrashLogs() throws IOException {
        File dir = new File(CrashLogUtils.getLogStorageDir());
        File[] logs = dir.listFiles();
        if (logs == null || logs.length == 0) {
            return;
        }

        for (int i = 0; i < logs.length; i++) {

            StringBuffer sb = new StringBuffer();
            HttpParams params = new HttpParams();

            //Write header
            CrashLogStore.writeDataHeader(mApp, params);

            //Write logs data
            File logFile = logs[i];
            CrashLogStore.writeLogData(sb, logFile, params);

            this.sendHttpRequest(params);
        }

        //Delete log files after successfully reported.
        CrashLogStore.deleteLogFiles(logs);
    }

    public void start() {
        //Check network type, only WIFI, 4G or 3G  allowed to HTTP request.
        if (isAllowConnectNetwork()
                && mReportThread.getState() == Thread.State.NEW) {
            mReportThread.start();
        }
    }
}

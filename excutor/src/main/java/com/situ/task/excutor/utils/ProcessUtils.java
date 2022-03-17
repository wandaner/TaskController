package com.situ.task.excutor.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @Author: kai.xu
 * @CreateDate: 2022/3/16 5:11 下午
 * @Description:
 */
public class ProcessUtils {

    private static String mCurProcessName;

    public static boolean isMainProcess() {
        return isMainProcess(App.get());
    }

    public static boolean isMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = getProcessName(context);
        return processName == null || processName.equals(packageName);
    }

    //新方法获取进程名
    public static String getProcessName(Context context) {
        if (!TextUtils.isEmpty(mCurProcessName)) {
            return mCurProcessName;
        }
        mCurProcessName = getProcessName(android.os.Process.myPid());
        if (!TextUtils.isEmpty(mCurProcessName)) {
            return mCurProcessName;
        }
        //当第一种方法获取不到进程名称时, 第二种方法获取进程名称
        try {
            int pid = android.os.Process.myPid();
            //获取系统的ActivityManager服务
            android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (am == null) {
                mCurProcessName = context.getApplicationInfo().packageName;
            }
            for (ActivityManager.RunningAppProcessInfo appProcess : am.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    mCurProcessName = appProcess.processName;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCurProcessName;
    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}

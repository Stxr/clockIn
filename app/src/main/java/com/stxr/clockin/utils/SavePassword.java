package com.stxr.clockin.utils;

import android.content.Context;

/**
 * Created by stxr on 2018/4/19.
 */

public class SavePassword {

    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String EMPLOYER = "Employer";
    public static final String BOSS = "Boss";

    public static void saveEmployerPassword(Context context, String name, String password) {
        ShareUtil.NAME = EMPLOYER;
        ShareUtil.put(context, NAME, name);
        ShareUtil.put(context, name, password);
    }
    public static void saveBossPassword(Context context, String name, String password) {
        ShareUtil.NAME = BOSS;
        ShareUtil.put(context, NAME, name);
        ShareUtil.put(context, name, password);
    }

    public static String getEmployerName(Context context) {
        ShareUtil.NAME = EMPLOYER;
        return (String) ShareUtil.get(context, NAME, "");
    }
    public static String getBossName(Context context) {
        ShareUtil.NAME = BOSS;
        return (String) ShareUtil.get(context, NAME, "");
    }
    public static String getEmployerPassword(Context context, String name) {
        ShareUtil.NAME = EMPLOYER;
        return (String) ShareUtil.get(context, name, "");
    }
    public static String getBossPassword(Context context, String name) {
        ShareUtil.NAME = BOSS;
        return (String) ShareUtil.get(context, name, "");
    }
}

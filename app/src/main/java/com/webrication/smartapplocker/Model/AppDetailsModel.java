package com.webrication.smartapplocker.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by pc on 2/6/2018.
 */

public class AppDetailsModel
{
   public String appName;
    Drawable appIcon;

    String packge_name;

    public AppDetailsModel(String appName, Drawable appIcon) {
        this.appName = appName;
        this.appIcon = appIcon;
    }

    public AppDetailsModel(String appName, Drawable appIcon, String packge_name) {
        this.appName = appName;
        this.appIcon = appIcon;
        this.packge_name = packge_name;
    }

    public String getPackge_name() {
        return packge_name;
    }

    public void setPackge_name(String packge_name) {
        this.packge_name = packge_name;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}

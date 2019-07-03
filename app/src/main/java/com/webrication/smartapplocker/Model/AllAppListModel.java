package com.webrication.smartapplocker.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by pc on 2/7/2018.
 */

public class AllAppListModel
{
  public   String appName;
    Drawable appIcon;
    String packge_name;

    public AllAppListModel(String appName, Drawable appIcon, String packge_name) {
        this.appName = appName;
        this.appIcon = appIcon;
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

    public String getPackge_name() {
        return packge_name;
    }

    public void setPackge_name(String packge_name) {
        this.packge_name = packge_name;
    }
}
